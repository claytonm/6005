package gui;

import javax.swing.*;
import gui.MemberModel.Member;

import java.awt.*;

/**
 * Created by clay on 8/11/16.
 */
public class MemberRenderer extends JLabel implements ListCellRenderer<Member> {

    @Override
    public Component getListCellRendererComponent(JList<? extends Member> list, Member member, int key,
                                                  boolean isSelected, boolean cellHasFocus) {
        int id = member.getKey();
        setText(member.getName());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }

    public MemberRenderer() {
        setOpaque(true);
    }
}
