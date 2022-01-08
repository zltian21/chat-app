package edu.rice.comp504.model.chatroom;

import java.util.ArrayList;

public interface IChatRoomDB {

    // get the public Chat room list
    public ArrayList<ChatRoom> getPublicChatroom();

    public ChatRoom getChatRoom(long chatroomId);

    public void addChatRoom(ChatRoom chatRoom);

    public void removeChatRoom(long chatroomId);
}
