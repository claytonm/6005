package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * Created by clay on 8/15/16.
 */
public class ActiveConversationsRenderer
        implements ListCellRenderer<ConversationsActiveModel.ActiveConversationComponents> {

    public Component getListCellRendererComponent(JList<? extends ConversationsActiveModel.ActiveConversationComponents> list,
                                                  final ConversationsActiveModel.ActiveConversationComponents conversation,
                                                  int key,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        // conversation.getActiveConversation().pack();
        conversation.getActiveConversation().setVisible(true);
        return conversation.getActiveConversation();
    }
}