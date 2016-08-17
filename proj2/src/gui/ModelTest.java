package gui;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clay on 8/14/16.
 */
public class ModelTest extends AbstractListModel {

    volatile Map<Integer, String> conversations;
//    volatile DefaultListModel<String> model;

    public int getSize() {return conversations.size();}

    public Object getElementAt(int index) {
        return conversations.get(index);
    }

    public ModelTest() {
        conversations = new HashMap<Integer, String>();
//        model = new DefaultListModel(conversations.values().toArray(new String[0]));

    }

    public void addConversations(String reply) {
        String[] conversationsStrings = reply.split("\\s+")[1].split(";");
        for (String conversation : conversationsStrings) {
            String[] conversationID = conversation.split(":");
            int id = Integer.valueOf(conversationID[0]);
            String names = conversationID[1];
            if (!(conversations.containsKey(id))) {
                conversations.put(id, names);
            }
        }
    }

    public void joinConversations(String reply) {
        String[] conversationsStrings = reply.split("\\s+")[1].split(";");
        int id = Integer.valueOf(conversationsStrings[0]);
        String names = conversationsStrings[1];
        conversations.put(id, names);
    }
}
