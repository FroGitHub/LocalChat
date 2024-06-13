package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final MessageController messageController;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket clientSocket, MessageController messageController) {
        this.clientSocket = clientSocket;
        this.messageController = messageController;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                try {
                    Messages gottenMess = (Messages) in.readObject();
                    System.out.println("Отримано об'єкт: " + gottenMess);
                    messageController.createMessage(gottenMess.getAuthor(), gottenMess.getText_wroten());
                    List<Messages> responseObject = messageController.getMessages();
                    Server.broadcastMessages(responseObject);

                } catch (ClassNotFoundException | IOException e) {
                    System.out.println("Помилка при з'єднанні з клієнтом: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка при створенні потоків: " + e.getMessage());
        }
    }

    public void sendMessages(List<Messages> messages) {
        try {
            out.writeObject(messages);
        } catch (IOException e) {
            System.out.println("Помилка при відправці оновлення клієнтам: " + e.getMessage());
        }
    }
}
