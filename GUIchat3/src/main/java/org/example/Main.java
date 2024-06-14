package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args){

        JButton button = new JButton("Send");
        ChatGUI chatGUI = new ChatGUI(button);
        chatGUI.startClient();


    }
}
