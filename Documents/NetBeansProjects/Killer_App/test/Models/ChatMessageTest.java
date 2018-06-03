/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.image.BufferedImage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marc
 */
public class ChatMessageTest {
    /**
     * Test of GetSender method, of class ChatMessage.
     */
    @Test
    public void testGetSender() {
        User sender = new User("sender");
        ChatMessage chatMessage = new ChatMessage("reciever", sender, "text");
        assertSame(sender, chatMessage.getSender());
    }

    /**
     * Test of GetReciever method, of class ChatMessage.
     */
    @Test
    public void testGetReciever() {
        String reciever = "reciever";
        ChatMessage chatMessage = new ChatMessage(reciever, new User("sender"), "text");
        assertSame(reciever, chatMessage.GetReciever());
    }

    /**
     * Test of GetText method, of class ChatMessage.
     */
    @Test
    public void testGetText() {
        String text = "text";
        ChatMessage chatMessage = new ChatMessage("reciever", new User("sender"), text);
        assertSame(text, chatMessage.getText());
    }

    /**
     * Test of GetID method, of class ChatMessage.
     */
    @Test
    public void testGetID() {
        ChatMessage chatMessage = new ChatMessage(0, "chat", new User("Sender"), "text");
        assertEquals(0, chatMessage.getID(), 0);
    }
    
}
