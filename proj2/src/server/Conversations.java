package server;

import javax.swing.*;
import java.util.*;

/**
 * Created by clay on 8/3/16.
 */

public class Conversations {
    static int MAX_CONVERSATIONS = 1000;
    protected Map<Integer, Conversation> conversations;


    private Boolean containsKey(int key) {

        return conversations.keySet().contains(key);
    }

    public int getSize() { return conversations.size();}
    public Conversation getElementAt(int i) { return conversations.get(i);}


    public Conversations() {

        conversations = Collections.synchronizedMap(new HashMap<Integer, Conversation>());
    }

    protected Conversation getConversation(int key) {

        return conversations.get(key);
    }

    protected void removeConversation(int key) {

        conversations.remove(key);
    }

    protected void addConversation(Conversation conversation) {

        conversations.put(conversation.getKey(), conversation);
    }

    protected boolean hasConversation(int conversationKey) {

        return conversations.containsKey(conversationKey);
    }

    // Conversation class

    protected static class Conversation {
        List<Member> members;
        List<Comment> comments;
        int key;


        protected Conversation(Conversations conversations) {
            comments = Collections.synchronizedList(new ArrayList());
            key = makeKey(conversations);
            conversations.addConversation(this);
            members = Collections.synchronizedList(new ArrayList());
        }

        protected void addMember(Member member) {
            members.add(member);
        }

        public List<Member> getMembers() {

            return members;
        }

        public int countMembers() {
            return members.size();
        }

        public boolean hasMembers() {

            return members.size() > 0;
        }

        protected void removeMember(Member member) {
            members.remove(member);
            // if only one person is left in conversation,
            // remove conversation from conversations
            if (members.size() < 2) {
                members.clear();
            }
        }



        private int makeKey(Conversations conversations) {
            int key = (int)(MAX_CONVERSATIONS * Math.random());
            // if key already exists in conversations,
            // try another key
            if (conversations.containsKey(key)) {
                makeKey(conversations);
            }
            return key;
        }

        protected void addComment(Comment comment) {

            comments.add(comment);
        }

        protected int getKey() {

            return key;
        }


        protected static class Comment {
            private int memberKey;
            private String comment;

            protected Comment(int memberKey, String comment) {
                this.memberKey = memberKey;
                this.comment = comment;
            }

            public String getComment() {
                return comment;
            }

            public String toString(Members members) {
                return members.getMember(memberKey).getName() + ":" + getComment();
            }
        }

        public String toString(Members members) {
            String commentsString = "";
            if (comments.size() == 0) {
                return "No comments yet.";
            }
            for (Comment comment : comments) {

                commentsString = commentsString + comment.toString(members) + "~";
            }
            return commentsString.substring(0,commentsString.length()-1);
        }

        public String namesString() {
            String namesString = "";
            for (Member member : members) {
                namesString = namesString + member.getName() + ",";
            }
            if (namesString.length() == 0) {
                return getKey() + ":" + "empty";
            } else {
                return getKey() + ":" + namesString.substring(0,namesString.length()-1);
            }
        }
    }

    public String toString() {
        String conversationsString = "";
        for (Conversation conversation : conversations.values()) {
            conversationsString = conversationsString + conversation.namesString() + ";";
        }
        return conversationsString.substring(0,conversationsString.length()-1);
    }

}
