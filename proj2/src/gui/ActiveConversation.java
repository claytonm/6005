package gui;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import gui.ConversationsActiveModel.ActiveConversationComponents;


/**
 * Created by clay on 8/15/16.
 */
public class ActiveConversation extends JFrame implements ActionListener  {

    Client client;
    ActiveConversationComponents conversationComponents;
    ConversationsActiveModel model;
    JTextField textField;
    JTextArea textArea;

    public void setLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textField)
                        .addComponent(textArea)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(textField)
                        .addComponent(textArea)

        );
    }

    public ActiveConversation(String text, Client client, ActiveConversationComponents conversationComponents) {

        this.client = client;
        this.conversationComponents = conversationComponents;
        textField = new JTextField(20);
        textField.setText("Type comment here...");
        textArea = new JTextArea(5, 20);
        textArea.setText(text);

        textField.addActionListener(this);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);

    }

    public void addComment(String text) throws IOException {
        String output = "comment " + client.getKey() + " " + conversationComponents.getKey() + " " + text;
        System.out.println(output);
        client.send(output);
    }


    public void actionPerformed(ActionEvent e) {
        try {
            if (textField.equals(e.getSource())) {
                addComment(textField.getText());
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
