package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.user.User;

public interface IChatRoomFac {

    public ChatRoom makeChatRoom(String chatRoomName, boolean isPrivate, User admin, int roomSize);

}
