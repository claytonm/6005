package gui;

import client.Client;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOError;
import java.io.IOException;

/**
 * Created by clay on 8/10/16.
 */
public class Listener implements ActionListener {

    JButton startConversationButton;
    JButton joinConversationButton;
    JList memberList;
    JList conversationList;
    Client client;

    public Listener(JButton startConversationButton,
                    JButton joinConversationButton,
                    JList memberList,
                    JList conversationList,
                    Client client) {
        this.startConversationButton = startConversationButton;
        this.joinConversationButton = joinConversationButton;
        this.client = client;
        this.memberList = memberList;
        this.conversationList = conversationList;
    }

    public void startConversation(Client client, JList memberList) throws IOException {
        String sendString = "start " + client.getKey() + " " + memberList.getSelectedValue().toString();
        System.out.println(sendString);
        client.send(sendString);
    }

    public void joinConversation(Client client, JList conversationList) throws IOException {
        String sendString = "join " + client.getKey() + " " + conversationList.getSelectedValue().toString();
        client.send(sendString);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (startConversationButton.equals(e.getSource())) {
                startConversation(client, memberList);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            startConversation(client, memberList);
                        } catch (IOException e) {
                            System.err.println(e.getMessage() + "Hello world");
                        }
                    }
                }).start();
//                startConversation(client, memberList);
            } else if (joinConversationButton.equals(e.getSource())) {
                joinConversation(client, conversationList);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}

