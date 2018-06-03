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
public class ChatInfoTest {
    /**
     * Test of getName method, of class ChatInfo.
     */
    @Test
    public void testGetName() {
        String name = "Chat";
        ChatInfo chatInfo = new ChatInfo(name, 0, 0);
        assertSame(name, chatInfo.getName());
    }

    /**
     * Test of getNumberOfMembers method, of class ChatInfo.
     */
    @Test
    public void testGetNumberOfMembers() {
        ChatInfo chatInfo = new ChatInfo("Chat", 0, 0);
        assertEquals(0, chatInfo.getNumberOfMembers(), 0);
    }

    /**
     * Test of getNewMessages method, of class ChatInfo.
     */
    @Test
    public void testGetNewMessages() {
        ChatInfo chatInfo = new ChatInfo("Chat", 0, 0);
        assertEquals(0, chatInfo.getNewMessages(), 0);
    }

    /**
     * Test of setName method, of class ChatInfo.
     */
    @Test
    public void testSetName() {
        String name = "Name";
        ChatInfo chatInfo = new ChatInfo("Chat", 0, 0);
        chatInfo.setName(name);
        assertSame(name, chatInfo.getName());
    }

    /**
     * Test of setNumberOfMembers method, of class ChatInfo.
     */
    @Test
    public void testSetNumberOfMembers() {
        ChatInfo chatInfo = new ChatInfo("Chat", 0, 0);
        chatInfo.setNumberOfMembers(20);
        assertEquals(20, chatInfo.getNumberOfMembers(), 0);
    }

    /**
     * Test of setNewMessages method, of class ChatInfo.
     */
    @Test
    public void testSetNewMessages() {
        ChatInfo chatInfo = new ChatInfo("Chat", 0, 0);
        chatInfo.setNewMessages(30);
        assertEquals(30, chatInfo.getNewMessages(), 0);
    }
    
}
