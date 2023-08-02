package com.driver;

import java.util.List;

public class WhatsappService {

    WhatsappRepository whatsappRepository = new WhatsappRepository();
    public String createUser(String name, String mobile) {
        return whatsappRepository.createUser(name,mobile);
    }

    public Group createGroup(List<User> users) {
        if(users.size()==2){
            return new Group(users.get(1).getName(),2);
        }
        return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {
        return whatsappRepository.createMessage(content);
    }

    public int sendMessage(Message message, User sender, Group group) {
        return whatsappRepository.sendMessage(message, sender, group);
    }

    public String changeAdmin(User approver, User user, Group group) {
        return whatsappRepository.changeAdmin(approver, user,group);
    }

    public int removeUser(User user) {
        return whatsappRepository.removeUser(user);
    }
}

