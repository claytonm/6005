package server;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clay on 8/3/16.
 */
public final class Members extends AbstractListModel {
    public static final int MAX_MEMBERS = 1000;
    private final Map<Integer, Member> members;

    protected Members() {
        members = Collections.synchronizedMap(new HashMap<Integer, Member>());
    }

    public int getSize() { return members.size();}
    public Member getElementAt(int i) { return members.get(i);}

    protected void addMember(Member member) {
        members.put(member.getKey(), member);
    }

    protected Member getMember(int memberKey) {
        return members.get(memberKey);
    }

    protected void removeMember(Member member) {
        members.remove(member.getKey());
    }

    protected boolean containsKey(int key) {
        return members.containsKey(key);
    }

    protected Map<Integer, Member> getMembers() {return this.members;}

    public String toString() {
        String membersString = "";
        for (Member member : members.values()) {
            membersString = membersString + member.toString() + ",";
        }
        return membersString.substring(0,membersString.length()-1);
    }
}

