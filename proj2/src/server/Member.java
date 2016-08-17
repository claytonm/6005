package server;

import server.Conversations.Conversation.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by clay on 8/3/16.
 */

public class Member {
    private String name;
    private int key;
    private List<Integer> convoKeys;

    public Member(String name, Members members) {
        this.name = name;
        this.key = makeKey(members);
        convoKeys = Collections.synchronizedList(new ArrayList());
        members.addMember(this);
    }

    public int getKey() { return this.key;}

    private int makeKey(Members members) {
        int key = (int)(Members.MAX_MEMBERS * Math.random());
        // if key already exists in conversations,
        // try another key
        if (members.containsKey(key)) {
            makeKey(members);
        }
        return key;
    }

    public String getName() {return this.name;}

    public void joinConversation(Conversations conversations, int key) {
        conversations.getConversation(key).addMember(this);
        convoKeys.add(conversations.getConversation(key).getKey());
    }

    public void leaveConversation(Conversations conversations, int key) {
        convoKeys.remove(conversations.getConversation(key).getKey());
        conversations.getConversation(key).removeMember(this, conversations);
    }

    public void comment(Conversations conversations, int key, String comment) {
        conversations.getConversation(key).addComment(new Comment(this.getKey(), comment));
    }

    public String toString() {
        return name.trim() + ":" + key;
    }
}
