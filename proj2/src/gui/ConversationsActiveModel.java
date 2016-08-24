package gui;

import client.Client;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by clay on 8/15/16.
 */

public class ConversationsActiveModel {

    volatile DefaultListModel model;
    Client client;

    public class ActiveConversationComponents  {
        Integer key;
        String names;
        String text;
        ActiveConversation activeConversation;

        public ActiveConversationComponents(int key, String names, String text) {
            this.key = key;
            this.names = names;
            this.text = text;
            this.activeConversation = new ActiveConversation(text, client, this);
        }

        public Integer getKey() {
            return key;
        }

        public ActiveConversation getActiveConversation() {
            return this.activeConversation;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof ActiveConversationComponents)) return false;
            ActiveConversationComponents otherConversation = (ActiveConversationComponents)other;
            return getKey().equals(otherConversation.getKey());
        }
    }


    public ConversationsActiveModel(Client client) {
        this.client = client;
        model = new DefaultListModel<ActiveConversationComponents>();
    }

    public DefaultListModel getModel() {

        return model;
    }
}
