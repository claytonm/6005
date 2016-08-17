package gui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clay on 8/10/16.
 */
public class ConversationsModel  {
    Map<Integer, String> conversations;
    volatile DefaultListModel model;

    public int getSize() {return conversations.size();}


    public class Conversation {
        String names;
        Integer key;

        public Conversation(String names, Integer key) {
            this.names = names;
            this.key = key;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof Conversation)) return false;
            Conversation otherConversation = (Conversation)other;
            return getKey().equals(otherConversation.getKey());
        }

        public String getName() {return this.names;}
        public Integer getKey() {return this.key;}

        public String toString() {return Integer.toString(this.key);}
    }

    public void addConversations(String reply) {
        String[] conversationsStrings = reply.split("\\s+")[1].split(";");
        for (String conversation : conversationsStrings) {
            String[] conversationID = conversation.split(":");
            Integer id = Integer.valueOf(conversationID[0]);
            String names = conversationID[1];
            if (!(conversations.containsKey(id))) {
                conversations.put(id, names);
                Conversation convo = new Conversation(names, id);
                model.addElement(convo);
                System.out.println("Conversation added to model: " + convo);
            }
        }
    }


    public void joinConversations(String reply) {
        String[] conversationsStrings = reply.split("\\s+")[1].split(";");
        Integer id = Integer.valueOf(conversationsStrings[0]);
        Integer index = model.indexOf(new Conversation("names", id));
        String names = conversationsStrings[1].split(":")[1];
        String oldNames = conversations.get(index);
        conversations.put(index, names);
        model.setElementAt(new Conversation(names, id), index);
    }

    public ConversationsModel () {
        conversations = new HashMap<Integer, String>();
        model = new DefaultListModel<Conversation>();
    }

    public DefaultListModel getModel() { return model;}

}
