package server;

import server.Conversations.Conversation;
import server.Conversations.Conversation.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Chat server runner.
 */
public class Server {

    private final static int PORT = 4444;
    private ServerSocket serverSocket;
    private Conversations conversations;
    private Members members;
    // private ArrayList<PrintWriter> outs = new ArrayList();
    private HashMap<Integer, PrintWriter> outs = new HashMap<Integer, PrintWriter>();

    /**
     *
     * @param port: port on which server will listen
     * @throws IOException
     */
    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        conversations = new Conversations();
        members = new Members();
    }

    // parse client input
    public String getMemberName(String input)  {
        return input.split("\\s+")[1];
    }
    public int getMemberKey(String input) {
        return Integer.valueOf(input.split("\\s+")[1]);
    }
    public int getOtherMemberKey(String input) {
        return Integer.valueOf(input.split("\\s+")[2]);
    }
    public int getConversationKey(String input) {
        return Integer.valueOf(input.split("\\s+")[2]);
    }
    public String getComment(String input) {return input.split("\\s+")[3];}

    public void send(String string, PrintWriter out) {
        out.println(string);
        out.flush();
    }

    public void sendAll(String string, HashMap<Integer, PrintWriter> outs) {
        for (Integer key : outs.keySet()) {
            send(string, outs.get(key));
        }
    }

    public void registerNewMember(String input, PrintWriter out) {

        // create new Member object
        String memberName = getMemberName(input);
        Member newMember = new Member(memberName, members);
        members.addMember(newMember);

        // return Member key to client
        send("key " + newMember.getKey(), out);

        // add client PrintWriter to Server
        outs.put(newMember.getKey(), out);

        // return existing conversations to client
        if (conversations.getSize() > 0) {
            send("conversations " + conversations.toString(), out);
        }

        // send updated Member list to all client PrintWriters
        sendAll("members " + members.toString(), outs);
    }

    public void startConversation(String input) {
        int memberKey = getMemberKey(input);
        int otherMemberKey = getOtherMemberKey(input);
        String memberName = members.getMember(memberKey).getName();
        String memberNameOther = members.getMember(otherMemberKey).getName();
        List<String> names = new ArrayList<String>();
        names.add(memberName);
        names.add(memberNameOther);

        // create new conversation
        Conversation conversation = new Conversation(conversations);

        // add members to conversation
        conversations.getConversation(conversation.getKey()).addMember(members.getMember(memberKey));
        conversations.getConversation(conversation.getKey()).addMember(members.getMember(otherMemberKey));

        // send updated Conversations list to all client PrintWriters
        sendAll("conversations " + conversations.toString(), outs);

        send("start " + conversation.getKey() + "@" + conversation.namesString() + "@" + "Start...", outs.get(memberKey));
        send("start " + conversation.getKey() + "@" + conversation.namesString() + "@" + "Start...", outs.get(otherMemberKey));
    }


    public void joinConversation(String input) {
        int memberKey = getMemberKey(input);
        int conversationKey = getConversationKey(input);
        conversations.getConversation(conversationKey).addMember(members.getMember(memberKey));
        members.getMember(memberKey).joinConversation(conversations, conversationKey);

        // Inform all users that user has joined conversation.
        sendAll("joined " +
                conversationKey + ";" +
                conversations.getConversation(conversationKey).namesString(),
                outs);

        System.out.println("User added to conversation: " +
                conversations.getConversation(conversationKey).toString(members));
        // Actually add user to conversation
        send("useradded@" +
                conversationKey + "@" +
                conversations.getConversation(conversationKey).toString(members),
                outs.get(memberKey));
    }


    public void leaveConversation(String input) {
        int memberKey = getMemberKey(input);
        int conversationKey = getConversationKey(input);
        if (conversations.hasConversation(conversationKey)) {
            conversations.getConversation(conversationKey).removeMember(members.getMember(memberKey));
            String outString;
            if (conversations.getConversation(conversationKey).countMembers() < 2) {
                String names = conversations.getConversation(conversationKey).namesString();
                conversations.removeConversation(conversationKey);
                outString = "leave " + names + " " + conversationKey;
                sendAll(outString, outs);
                outString = "remove " + conversationKey;
                sendAll(outString, outs);
            } else {
                String names = conversations.getConversation(conversationKey).namesString();
                outString = "leave " + names + " " + conversationKey;
                sendAll(outString, outs);
            }
        }
    }


    public void comment(String input) {
        int memberKey = Integer.valueOf(input.split("@")[1]);
        // int conversationKey = getConversationKey(input);
        int conversationKey = Integer.valueOf(input.split("@")[2]);
        if (conversations.hasConversation(conversationKey)) {
            String comment = input.split("@")[3];
            String outComment = members.getMember(memberKey).getName() + ":" + comment;
            List<Member> conversationMembers = conversations.getConversation(conversationKey).getMembers();
            // String comment = getComment(input);
            conversations.getConversation(conversationKey).addComment(new Comment(memberKey, comment));

            String names = conversations.getConversation(conversationKey).namesString();

            System.out.println("Leaving a comment: " +
                    conversations.getConversation(conversationKey).toString(members));

            for (Member member : conversationMembers) {
                System.out.println("Send to member: " + member.getKey());
                PrintWriter out = outs.get(member.getKey());
                String outString = "post@" +
                        conversationKey + "@" +
                        outComment;
                send(outString, out);
            }
        }
    }

    public void getMemberNames(PrintWriter out) {
        out.print(members.toString() + "\n");
        out.flush();
    }

    public void invalidMessage(String input) throws Exception {
        System.err.print("Invalid intput: " + input + "\n");
    }

    public void handle(String input, PrintWriter out) throws Exception {
        if (input.startsWith("join")) {joinConversation(input);}
        else if (input.startsWith("leave")) {leaveConversation(input);}
        else if (input.startsWith("register")) {registerNewMember(input, out);}
        else if (input.startsWith("start")) {startConversation(input);}
        else if (input.startsWith("members")) {getMemberNames(out);}
        else if (input.startsWith("post")) {comment(input);}
        else {invalidMessage(input);}
    }

    private void handleConnection(Socket socket) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
            String line = in.readLine();
            while (line != null) {
                handle(line, out);
                line = in.readLine();
            }
            } finally {
            out.close();
            in.close();
        }
    }

    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            // handle the client
            new Thread(new Runnable() {
                public void run() {
                    try {
                        handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace(); // but don't terminate serve()
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) throws Exception, IOException {
        Server server = new Server(PORT);
        server.serve();
    }
}