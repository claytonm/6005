package gui;

import javax.swing.*;
import java.awt.*;
import gui.ConversationsModel.Conversation;

/**
 * Created by clay on 9/10/16.
 */
public class ConversationsRenderer extends JLabel implements ListCellRenderer<Conversation>  {

    @Override
    public Component getListCellRendererComponent(JList<? extends Conversation> list, Conversation conversation, int key,
                                                  boolean isSelected, boolean cellHasFocus) {
        Integer id = conversation.getKey();
        setText(conversation.getName());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }

    public ConversationsRenderer() {
        setOpaque(true);
    }
}
