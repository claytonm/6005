package gui;


import client.Client;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.EventListener;


/**
 * Created by clay on 8/7/16.
 */
public class ChatGUI extends JFrame {

    private final static int PORT = 4444;
    private String name;
    private JList memberList;
    private JList conversationList;
    private JList conversationsActiveList;
    private JLabel memberListLable;
    private JLabel conversationListLable;
    private JButton startConversationButton;
    private JButton joinConversationButton;
    private JScrollPane members;
    private JScrollPane conversations;
    private JList conversationsActive;


    public void setLayout(GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(memberListLable)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(members)
                                .addComponent(startConversationButton))
                        .addComponent(conversationListLable)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(conversations)
                                .addComponent(joinConversationButton))
                        .addComponent(conversationsActive)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(memberListLable)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(members)
                                .addComponent(startConversationButton))
                        .addComponent(conversationListLable)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(conversations)
                                .addComponent(joinConversationButton))
                        .addComponent(conversationsActive)

        );
    }



    public ChatGUI(final Client client, String name) throws IOException {

        // register user <name>
        client.join(name);

        // initiate the data models
        final MemberModel memberModel = new MemberModel();
        final ConversationsModel conversationsModel = new ConversationsModel();
        final ConversationsActiveModel conversationsActiveModel = new ConversationsActiveModel(client);

        // start background thread to listen
        // for data updates from server
        new Thread(new Runnable() {
            public void run() {
                try {
                    client.read(memberModel, conversationsModel, conversationsActiveModel);
                } catch (IOException e) {
                    System.err.println(e.getMessage() + "Hello world");
                }
            }
        }).start();

        // initiate all JComponents
        memberList = new JList(memberModel.getModel());
        memberList.setCellRenderer(new MemberRenderer());
        members = new JScrollPane(memberList);
        conversationList = new JList(conversationsModel.getModel());
        conversationList.setCellRenderer(new ConversationsRenderer());
        conversations = new JScrollPane(conversationList);
        memberListLable = new JLabel("Click member names to start a conversation");
        conversationListLable = new JLabel("Or join a conversation");
        startConversationButton = new JButton("Start conversation");
        joinConversationButton = new JButton("Join conversation");
        conversationsActive = new JList(conversationsActiveModel.getModel());
        conversationsActive.setCellRenderer(new ActiveConversationsRenderer());


        // initiate action listener
        Listener listener = new Listener(startConversationButton,
                joinConversationButton,
                memberList,
                conversationList,
                client);

        // associate listener to buttons
        startConversationButton.addActionListener(listener);
        joinConversationButton.addActionListener(listener);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);
    }

    public static void main(final String[] args) throws IOException {

        // get user name from command line
        final String name = args[0];
        final Client client = new Client(PORT);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatGUI main = new ChatGUI(client, name);
                    main.setVisible(true);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}
