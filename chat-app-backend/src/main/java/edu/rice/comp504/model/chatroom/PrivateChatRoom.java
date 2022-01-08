package edu.rice.comp504.model.chatroom;

import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.user.User;

import java.util.ArrayList;

public class PrivateChatRoom extends ChatRoom {

    /**
     * Additional properties for ChatRoom.
     *
     * @param messages Messages in the ChatRoom.
     * @param blockedUsers List of blocked users.
     * @param users    List of All the users.
     */


    /**
     * Constructor for ChatRoom.
     *
     * @param id       The id of chatRoom.
     * @param isPrivate True if chatRoom is private else false.
     * @param admin    The admin of chatRoom.
     * @param roomSize     The size of chatRoom.
     */

    public PrivateChatRoom(long id, String roomName, boolean isPrivate, User admin, int roomSize) {
        super(id, roomName, isPrivate, admin, roomSize);
    }


    /**
     * Send invite to user.
     *
     * @param userId id of user to whom invite is to be sent.
     */
    public void sendInvite(long userId){


    }

    public ArrayList<AMessage> getMessages() {
        // return messages;
        return null;
    }

    public void setMessages(ArrayList<AMessage> messages) {
        // this.messages = messages;
    }

}
