/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Sergio Cabrera
 */
@ServerEndpoint("/socket")
public class WebSocketServer {
    
    private static List<Session> sessions = Collections.synchronizedList(new ArrayList<Session>());
    
    @OnOpen
    public void open(Session session) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Session opended with id: ".concat(session.getId()));
        sessions.add(session);
    }
    
    @OnClose
    public void close(Session session) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Session closed with id: ".concat(session.getId()));
        sessions.remove(session);
    }
    
    @OnMessage
    public void message(String message, Session session) throws IOException {
        synchronized(sessions) {
            //JSONObject json = new JSONObject(message);
            for(Session s : sessions)
                s.getBasicRemote().sendText(message);
        }
    }
    
    @OnError
    public void error(Throwable t) {        
        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, t.getMessage(), t);
    }
}
