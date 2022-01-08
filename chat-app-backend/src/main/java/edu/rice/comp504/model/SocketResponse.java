package edu.rice.comp504.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SocketResponse {
    private long messageId;
    private String type;
    private boolean isPrivate;
    private String content;
    private JsonObject sender;
    private JsonObject receiver;
    private long chatRoomId;

    /**
     * SocketResponse constructor.
     */
    public SocketResponse(long messageId, String type, boolean isPrivate, String content, JsonObject sender, JsonObject receiver, long chatRoomId) {
        this.messageId = messageId;
        this.type = type;
        this.isPrivate = isPrivate;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.chatRoomId = chatRoomId;
    }
}
