package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.user.User;

import java.util.ArrayList;

public class PublicChatRoom extends ChatRoom{

    public PublicChatRoom(long id, String roomName, boolean isPrivate, User admin, int roomSize) {
        super(id, roomName, isPrivate, admin, roomSize);
    }

}
