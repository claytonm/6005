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

    // Conversation class

    protected static class Conversation {
        List<String> names;
        List<Member> members;
        List<Comment> comments;
        int key;


        protected Conversation(List<String> names, Conversations conversations) {
            this.names = Collections.synchronizedList(names);
            Collections.sort(this.names);
            this.comments = Collections.synchronizedList(new ArrayList());
            this.key = makeKey(conversations);
            conversations.addConversation(this);
            this.members = Collections.synchronizedList(new ArrayList());
        }

        protected void addMember(Member member) {

            this.names.add(member.getName());
            this.members.add(member);
        }

        public List<Member> getMembers() {return this.members;}

        protected void removeMember(Member member, Conversations conversations) {
            this.names.remove(member.getName());
            // if only one person is left in conversation,
            // remove conversation from conversations
            if (names.size() == 1) {
                conversations.removeConversation(this.getKey());
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
            this.comments.add(comment);
        }

        protected int getKey() {
            return this.key;
        }


        protected static class Comment {
            private int memberKey;
            private String comment;

            protected Comment(int memberKey, String comment) {
                this.memberKey = memberKey;
                this.comment = comment;
            }

            public int getMemberKey() {
                return memberKey;
            }

            public String getComment() {
                return comment;
            }

            public String toString(Members members) {
                return members.getMember(memberKey).getName() + ":" + comment;
            }
        }

        public String toString(Members members) {
            String commentsString = "";
            for (Comment comment : this.comments) {
                commentsString = commentsString + comment.toString(members) + ",";
            }
            return commentsString.substring(0,commentsString.length()-1);
        }

        public String namesString() {
            String namesString = "";
            for (String name : names) {
                namesString = namesString + name + ",";
            }
            return getKey() + ":" + namesString.substring(0,namesString.length()-1);
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
