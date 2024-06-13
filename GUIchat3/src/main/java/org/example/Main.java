package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args){

        chatGUI chatGUI = new chatGUI(new JButton("Send"));
        chatGUI.createTextFields();
        chatGUI.addElements();
        chatGUI.startClient();


    }
}
