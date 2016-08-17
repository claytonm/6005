package client;

import gui.MemberModel;
import gui.ConversationsModel;
import gui.ConversationsActiveModel;
import gui.ModelTest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by clay on 8/4/16.
 */
public class Client {

    public static String HOST_NAME = "localhost";
    public Socket clientSocket;
    public BufferedReader in;
    public PrintWriter out;
    public static int PORT = 4444;
    private int key;

    public Client(int port) throws IOException {
        clientSocket = new Socket(HOST_NAME, PORT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
    }

    public void setKey(String input) {
        this.key = Integer.valueOf(input.split("\\s+")[1]);
    }

    public int getKey() {
        return this.key;
    }

    public void send(String message) throws IOException {
        out.println(message);
        out.flush();
    }

    public void join(String name) throws IOException {
        send("register " + name);
    }

    public void read(MemberModel memberModel,
                     ConversationsModel conversationsModel,
                     ConversationsActiveModel activeConversationsModel) throws IOException  {
        String reply = in.readLine();
        while (reply != null) {
            handle(reply, memberModel, conversationsModel, activeConversationsModel);
            reply = in.readLine();
        }
    }

    public void handle(String reply,
                       MemberModel memberModel,
                       ConversationsModel conversationsModel,
                       ConversationsActiveModel activeConversationsModel) throws IOException {
        if (reply.startsWith("members")) {
            memberModel.addMembers(reply);
        } else if (reply.startsWith("conversations")) {
            conversationsModel.addConversations(reply);
        } else if (reply.startsWith("start")) {
            System.out.println("Start converstion.");
            activeConversationsModel.addConversation(reply);
        } else if (reply.startsWith("comment")) {
            System.out.println("Reply starts with comment.");
            activeConversationsModel.addCommentToConversation(reply);
        } else if (reply.startsWith("key")) {
            setKey(reply);
        } else if (reply.startsWith("join")) {
            conversationsModel.joinConversations(reply);
        }
    }
}
