package gui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by clay on 8/9/16.
 */

public class MemberModel  {
    Map<Integer, String> members;
    volatile DefaultListModel model;


    public class Member {
        String name;
        int key;

        public Member(String name, int key) {
            this.name = name;
            this.key = key;
        }

        public String getName() {return this.name;}
        public int getKey() {return this.key;}

        public String toString() {return Integer.toString(this.key);}

    }

    public void addMembers(String reply) {
        String[] memberStrings = reply.split("\\s+")[1].split(",");
        for (String member : memberStrings) {
            String[] nameID = member.split(":");
            String name = nameID[0];
            int id = Integer.valueOf(nameID[1]);
            if (!(members.containsKey(id))) {
                members.put(id, name);
                model.addElement(new Member(name, id));
            }
        }
    }

    public MemberModel () {
        members = new HashMap<Integer, String>();
        model = new DefaultListModel<Member>();
    }

    public DefaultListModel getModel() { return model;}
}
