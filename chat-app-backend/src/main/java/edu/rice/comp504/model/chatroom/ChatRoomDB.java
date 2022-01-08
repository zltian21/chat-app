package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatRoom Factory used to create and maintain the chat rooms.
 * Holds the list of the chat rooms.
 */
// Use Singleton Design Pattern for ChatRoomDB, MessageDB and UserDB
public class ChatRoomDB implements IChatRoomDB{
    private static ChatRoomDB chatRoomDBIns = null;
    private List<ChatRoom> allChatRooms = new ArrayList<>();

    /**
     * Get chatroomDB instance.
     * @return chatroomDB instance.
     */
    public static ChatRoomDB getInstance() {
        if (chatRoomDBIns == null) {
            chatRoomDBIns = new ChatRoomDB();
        }
        return chatRoomDBIns;
    }

    /**
     * Get all the public chat room that the user do not join in.
     *
     */
    public ArrayList<ChatRoom> getPublicChatroom() {
        ArrayList<ChatRoom> publicChatroomList = new ArrayList<>();
        for (int i = 0; i < allChatRooms.size(); i++) {
            if (!allChatRooms.get(i).isPrivate()) {
                publicChatroomList.add(allChatRooms.get(i));
            }
        }
        return publicChatroomList;
    }

    /**
     * Get  chat room according to the chatroomId.
     *
     */
    public ChatRoom getChatRoom(long chatroomId) {
        for (int i = 0; i < allChatRooms.size(); i++) {
            if (allChatRooms.get(i).getId() == chatroomId) {
                return allChatRooms.get(i);
            }
        }
        return null;
    }

    /**
     * Add a chat room to ChatRoomDB.
     * @param chatroom  chatroom instance
     */
    public void addChatRoom(ChatRoom chatroom) {
        allChatRooms.add(chatroom);
    }

    /**
     * Remove a chat room from ChatRoomDB.
     * @param chatroomId  chatroomId that should be removed
     */
    public void removeChatRoom(long chatroomId) {
        for (int i = 0; i < allChatRooms.size(); i++) {
            if (allChatRooms.get(i).getId() == chatroomId) {
                allChatRooms.remove(i);
                break;
            }
        }
    }

    /**
     * Check if the roomName already exist
     * @param roomName room name
     * @return if roomName exist.
     */
    public boolean roomNameExist(String roomName) {
        for (int i = 0; i < allChatRooms.size(); i++) {
            if (allChatRooms.get(i).getRoomName().equals(roomName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the private chatroom.
     * Only used for unit test
     * @return private chatroom list.
     */
    public ArrayList<ChatRoom> getPrivateChatroom() {
        ArrayList<ChatRoom> privateChatroomList = new ArrayList<ChatRoom>();
        for (int i = 0; i < allChatRooms.size(); i++) {
            if (allChatRooms.get(i).isPrivate()) {
                privateChatroomList.add(allChatRooms.get(i));
            }
        }
        return privateChatroomList;
    }



}
