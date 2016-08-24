package client;

import gui.MemberModel;
import gui.ConversationsModel;
import gui.ConversationsActiveModel;
import gui.MultipleConversationsGUI;

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
        key = (int)(100 * Math.random());
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(String input) {
        this.key = Integer.valueOf(input.split("\\s+")[1]);
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
                     MultipleConversationsGUI multipleConversationsGUI) throws IOException  {
        String reply = in.readLine();
        while (reply != null) {
            handle(reply, memberModel, conversationsModel, multipleConversationsGUI);
            reply = in.readLine();
        }
    }

    public String parseConversationString(String conversationString) {
        return conversationString.replaceAll("~", "\n") + "\n";
    }

    public void handle(String reply,
                       MemberModel memberModel,
                       ConversationsModel conversationsModel,
                       MultipleConversationsGUI multipleConversationsGUI) throws IOException {
        if (reply.startsWith("members")) {
            memberModel.addMembers(reply);
        } else if (reply.startsWith("conversations")) {
            conversationsModel.addConversations(reply);
        } else if (reply.startsWith("start")) {
            int conversationKey = Integer.valueOf(reply.split("\\s+")[1].split("@")[0]);
            System.out.println("Start converstion with key: " + conversationKey);
            multipleConversationsGUI.addConversation(conversationKey);
        } else if (reply.startsWith("comment")) {
            int conversationKey = Integer.valueOf(reply.split("\\s+")[2]);
            String conversation = reply.split("\\s+")[3];
            System.out.println("Add comment to conversation: " + conversationKey + " " + conversation);
            multipleConversationsGUI.addComment(conversationKey, reply);
        } else if (reply.startsWith("post")) {
            System.out.println("Posting comment: " + reply);
            int conversationKey = Integer.valueOf(reply.split("@")[1]);
            String conversation = reply.split("@")[2] + "\n";
            System.out.println("Add comment to conversation: " + conversationKey + " " + conversation);
            multipleConversationsGUI.addComment(conversationKey, conversation);
        }   else if (reply.startsWith("key")) {
            setKey(reply);
        }   else if (reply.startsWith("join")) {
            conversationsModel.joinConversations(reply);
        }   else if (reply.startsWith("useradded")) {
            System.out.println("User added: " + reply);
            int conversationKey = Integer.valueOf(reply.split("@")[1]);
            String conversationString = reply.split("@")[2];
            System.out.println("Conversation string: " + conversationString);
            multipleConversationsGUI.joinConversation(conversationKey, parseConversationString(conversationString));
        } else if (reply.startsWith("leave")) {
            String names = reply.split("\\s+")[1].split(":")[1];
            int conversationKey = Integer.valueOf(reply.split("\\s+")[2]);
            conversationsModel.leaveConversations(names, conversationKey);
        } else if (reply.startsWith("remove")) {
            int conversationKey = Integer.valueOf(reply.split("\\s+")[1]);
            conversationsModel.removeConversations(conversationKey);
        }
    }
}
