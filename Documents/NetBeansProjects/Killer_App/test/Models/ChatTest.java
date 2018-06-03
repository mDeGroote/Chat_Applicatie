/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marc
 */
public class ChatTest {

    /**
     * Test of GetUsers method, of class Chat.
     */
    @Test
    public void testGetUsers() {
        ArrayList<User> users = new ArrayList<>();
        Chat chat = new Chat("chat", users, new ArrayList<>());
        assertSame(users, chat.GetUsers());
    }

    /**
     * Test of GetMessages method, of class Chat.
     */
    @Test
    public void testGetMessages() {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        Chat chat = new Chat("chat", new ArrayList<User>(), messages);
        assertSame(messages, chat.GetMessages());
    }

    /**
     * Test of GetName method, of class Chat.
     */
    @Test
    public void testGetName() {
        String name = "Chat";
        Chat chat = new Chat(name, new ArrayList<User>(), new ArrayList<>());
        assertSame(name, chat.GetName());
    }
    
}
