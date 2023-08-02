package com.driver;

import java.util.*;

public class WhatsappRepository {

    private HashMap<String,User> userDb;

    private HashSet<String> mobileNo;

    private HashMap<Message, User> senderMap;

    private HashMap<Group, List<Message>> groupMessageMap;

    private HashMap<Group, User> adminMap;
    private HashMap<Group, List<User>> groupUserMap;

    private HashSet<User> admins;

    private int customGroupCount;

    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.mobileNo = new HashSet<>();
        this.userDb = new HashMap<>();
        this.admins = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public String createUser(String name, String mobile) {
        if (mobileNo.contains(mobile)){
            throw new RuntimeException("User already exists");
        }
        mobileNo.add(mobile);
        User user = new User();
        user.setName(name);
        user.setMobile(mobile);
        userDb.put(mobile,user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        customGroupCount++;
        String groupName = "Group "+customGroupCount;
        Group group = new Group(groupName,users.size());
        adminMap.put(group,users.get(0));
        admins.add(users.get(0));
        groupUserMap.put(group, users);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId,content);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        if(!adminMap.containsKey(group)){
            throw  new RuntimeException("Group does not exist");
        }else {
            List<User> list= groupUserMap.get(group);
            for(User u : list){
                if(u == sender){
                    senderMap.put(message,sender);

                    List<Message> groupList = groupMessageMap.getOrDefault(group, new ArrayList<Message>());
                    groupList.add(message);
                    groupMessageMap.put(group, groupList);
                    return groupList.size();
                }
            }
            throw new RuntimeException("You are not allowed to send message");
        }
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!adminMap.containsKey(group)) {
            throw new RuntimeException("Group does not exist");
        }
        User admin = adminMap.get(group);
        if(admin != approver){
            throw new RuntimeException("Approver does not have rights");
        }
        List<User> groupList = groupUserMap.get(group);
        for(User s : groupList){
            if(s == user){
                adminMap.remove(group,approver);
                adminMap.put(group, user);
                return "SUCCESS";
            }
        }
        throw new RuntimeException("User is not a participant");
    }


    public int removeUser(User user) {
        for (Group g : groupUserMap.keySet()){
            List<User> list = groupUserMap.get(g);
            if (list.contains(user)){
                if (admins.contains(user)){
                    throw new RuntimeException("Cannot remove admin");
                }
                mobileNo.remove(user.getMobile());
                userDb.remove(user.getMobile(),user);
                for (Message m : senderMap.keySet()){
                    if (senderMap.get(m)==user){
                        senderMap.remove(m,user);
                    }
                    groupMessageMap.remove(g);
                }
            }
        }

    }
}
