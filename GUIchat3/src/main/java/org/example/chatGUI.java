package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatGUI {

    private JFrame frame;
    private JPanel panel;
    private JLabel textLabel;
    private JTextField textField;
    private JScrollPane scrollPane;
    private JButton button;
    private JPanel inputPanel;
    private JTextField nameField;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ChatGUI(JButton button) {
        this.button = button;
        initializeFrame();
        initializeTextFields();
        addElements();
    }

    private void initializeFrame() {
        frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300);
    }

    private void initializeTextFields() {
        panel = new JPanel();

        textField = new JTextField("mess", 15);
        nameField = new JTextField("name", 15);

        textLabel = new JLabel("hehehe");
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrollPane = new JScrollPane(textLabel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 150));
    }

    private void addElements() {
        inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(nameField, BorderLayout.NORTH);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(textField);
        textFieldPanel.add(button);
        inputPanel.add(textFieldPanel, BorderLayout.CENTER);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public void startClient() {
        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            configureButtonAction();
            startMessageReceiver();

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void configureButtonAction() {
        button.addActionListener(e -> {
            try {
                out.writeObject(new Messages(nameField.getText(), textField.getText()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void startMessageReceiver() {
        new Thread(() -> {
            try {
                while (true) {
                    List<Messages> gottenMessFromServer = (List<Messages>) in.readObject();
                    updateTextLabel(gottenMessFromServer);
                }
            } catch (IOException | ClassNotFoundException ex) {
                handleReceiverException(ex);
            } finally {
                closeResources();
            }
        }).start();
    }

    private void updateTextLabel(List<Messages> messages) {
        SwingUtilities.invokeLater(() -> {
            StringBuilder text = new StringBuilder();
            for (int i = messages.size() - 1; i >= 0; i--) {
                text.append(messages.get(i)).append("<br>");
            }
            textLabel.setText("<html>" + text.toString() + "</html>");
        });
    }

    private void handleReceiverException(Exception ex) {
        System.out.println("Помилка при отриманні повідомлень: " + ex.getMessage());
        ex.printStackTrace();
    }

    private void closeResources() {
        try {
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Помилка при закритті ресурсів: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
