package edu.rice.comp504.adapter;

import edu.rice.comp504.model.ChatApp;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.SessionDB;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {

    private ChatApp chatApp = ChatApp.getInstance();

    /**
     * Open user's session.
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        String username = session.getUpgradeRequest().getParameterMap().get("username").get(0);
        SessionDB.addSessionUser(session, username);
    }

    /**
     * Close the user's session.
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        SessionDB.removeUser(session);
    }

    /**
     * Send a message.
     * @param session  The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        chatApp.receivedMsg(message);
    }
}
