package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Сервер запущено на порту 12345...");
            MessageController messageController = new MessageController();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, messageController);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Помилка запуску сервера: " + e.getMessage());
        }
    }

    public static void broadcastMessages(List<Messages> messages) {
        for (ClientHandler client : clients) {
            client.sendMessages(messages);
        }
    }
}

