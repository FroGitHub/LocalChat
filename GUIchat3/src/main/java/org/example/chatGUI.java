package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class chatGUI {

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
    private JPanel textFieldPanel;

    public chatGUI(JButton button) {
        frame = new JFrame("Simple GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300);
        this.button = button;
    }

    public void createTextFields() {
        // Створення панелі
        panel = new JPanel();

        // Створення текстового поля
        textField = new JTextField("mess", 15);
        nameField = new JTextField("name", 15);

        textLabel = new JLabel("hehehe");
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Створення JScrollPane для textLabel
        scrollPane = new JScrollPane(textLabel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 150));
    }

    public void addElements() {

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(nameField, BorderLayout.NORTH);

        textFieldPanel = new JPanel();
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

            // зразу визначаємо кномпу, бо я ще не придумав як обійтися без сокет і його штучок
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        out.writeObject(new Messages(nameField.getText(), textField.getText()));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            Thread messageReceiver = new Thread() {
                public void run() {
                    final String[] text = {""};
                    while (true) {
                        try {
                            List<Messages> gottenMessFromServer = (List<Messages>) in.readObject();
                            SwingUtilities.invokeLater(() -> {
                                textLabel.setText("");
                                text[0] = "";
                                for (int i = gottenMessFromServer.size() - 1; i >= 0; i--) {
                                    text[0] += gottenMessFromServer.get(i).toString() + "<br>";
                                }
                                textLabel.setText("<html>" + text[0] + "</html>");
                            });
                        } catch (IOException | ClassNotFoundException ex) {
                            System.out.println("Помилка при отриманні повідомлень: " + ex.getMessage());
                            ex.printStackTrace();
                            break;
                        }
                    }
                    try { // хз треба ця фігня чи ні, навсяк буде
                        socket.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        System.out.println("Помилка при закритті ресурсів: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            };

            messageReceiver.start();
            frame.getContentPane().add(panel);
            frame.setVisible(true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
