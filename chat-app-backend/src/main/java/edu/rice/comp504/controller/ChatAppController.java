package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.adapter.DispatchAdapter;
import edu.rice.comp504.adapter.WebSocketAdapter;
import edu.rice.comp504.model.Response;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {

    /**
     * Chat App entry point.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        Gson gson = new Gson();
        DispatchAdapter dis = new DispatchAdapter();

        staticFiles.location("/public");
        webSocket("/chatapp", WebSocketAdapter.class);
        init();

        before("/*", (request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "*");
            if (!request.pathInfo().equals("/login") && !request.pathInfo().equals("/register") && !request.pathInfo().equals("/chatapp")) {

                // If request method is OPTIONS, it is a preflight cors, no check required
                if (!request.requestMethod().equals("OPTIONS")) {
                    // check if user login with userId in header
                    if (request.headers("userId") == null) {
                        halt(401, "user not login");
                    }
                    // check if the userId exist
                    long userId = Long.parseLong(request.headers("userId"));
                    if (!dis.userExist(userId)) {
                        halt(401, "user not found");
                    }
                    // check if user been banned
                    if (dis.isBanned(userId)) {
                        halt(401, "user been banned");
                    }
                }
            }
        });

        post("/login", (request, response) -> {
            String username = request.queryMap("username").value();
            String password = request.queryMap("password").value();
            return gson.toJson(dis.login(username, password));
        });

        post("/register", (request, response) -> {
            String username = request.queryMap("username").value();
            String password = request.queryMap("password").value();
            int age = request.queryMap("age").integerValue().intValue();
            String imageUrl = request.queryMap("imageUrl").value();
            String interests = request.queryMap("interests").value();
            String school = request.queryMap("school").value();
            return gson.toJson(dis.register(username, password, age, imageUrl, interests, school));
        });

        get("/logout", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.logout(userId));
        });

        get("/getPublicChatrooms", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getPublicChatroom(userId));
        });

        get("/getMyChatrooms", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getMyChatroom(userId));
        });

        get("/getUserNotification", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getUserNotification(userId));
        });

        post("/createRoom", (request, response) -> {
            String roomName = request.queryMap("roomName").value();
            int roomSize = request.queryMap("roomSize").integerValue();
            boolean isPrivate = request.queryMap("isPrivate").booleanValue();
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.createRoom(roomName, roomSize, isPrivate, userId));
        });

        get("/getListofUser/:chatroomId", (request, response) -> {
            long chatroomId = Long.parseLong(request.params(":chatroomId"));
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getListofUser(chatroomId, userId));
        });

        get("/getListofBlockedUser/:chatroomId", (request, response) -> {
            long chatroomId = Long.parseLong(request.params(":chatroomId"));
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getListofBlockedUser(chatroomId, userId));
        });

        get("/getChatRoom/:chatroomId", (request, response) -> {
            long chatroomId = Long.parseLong(request.params(":chatroomId"));
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.getChatRoom(chatroomId, userId));
        });

        post("/leaveChatRoom", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long chatRoomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson( dis.leaveChatRoom(chatRoomId, userId));
        });

        post("/joinRoom", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long chatRoomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.joinRoom(chatRoomId, userId));
        });

        post("/opInvitation/:notificationId", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long notificationId = Long.parseLong(request.params(":notificationId"));
            boolean accept = request.queryMap("accept").booleanValue();
            return gson.toJson(dis.opInvitation(notificationId, accept, userId));
        });

        post("/removeUser", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long removedUserId = request.queryMap("user").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.removeUser(userId, removedUserId, chatroomId));
        });

        post("/blockUser", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long blockUserId = request.queryMap("user").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.blockUser(userId, blockUserId, chatroomId));
        });

        get("/getListofUserToInvite/:chatroomId", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long chatroomId = Long.parseLong(request.params(":chatroomId"));
            return gson.toJson(dis.getListofUserToInvite(userId, chatroomId));
        });

        post("/sendInvite", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long receiverId = request.queryMap("user").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.sendInvite(userId, receiverId, chatroomId));
        });

        post("/unblockUser", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long unblockUserId = request.queryMap("user").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.unblockUser(userId, unblockUserId, chatroomId));
        });

        post("/editMessage", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long messageId = request.queryMap("messageId").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            String content = request.queryMap("content").value();
            return gson.toJson(dis.editMessage(userId, messageId, chatroomId, content));
        });

        post("/removeMessage", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            long messageId = request.queryMap("messageId").longValue();
            long chatroomId = request.queryMap("chatRoomId").longValue();
            return gson.toJson(dis.removeMessage(userId, messageId, chatroomId));
        });

        post("/leaveAllChatroom", (request, response) -> {
            long userId = Long.parseLong(request.headers("userId"));
            return gson.toJson(dis.leaveAllChatroom(userId));
        });

        get("/getUserInfo/:userId", (request, response) -> {
            long userId = Long.parseLong(request.params(":userId"));
            return gson.toJson(dis.getUserInfo(userId));
        });

    }


    /**
     * Get the heroku assigned port number.
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
