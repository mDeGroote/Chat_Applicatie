/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mariac
 */
public class PrivateMessageTest {

    /**
     * Test of getSender method, of class PrivateMessage.
     */
    @Test
    public void testGetSender() {
        User sender = new User("sender");
        PrivateMessage privateMessage = new PrivateMessage(new User("reciever"), sender, "text");
        assertEquals(sender, privateMessage.getSender());
    }

    /**
     * Test of getText method, of class PrivateMessage.
     */
    @Test
    public void testGetText() {
        String text = "text";
        PrivateMessage privateMessage = new PrivateMessage(new User("reciever"), new User("sender"), text);
        assertEquals(text, privateMessage.getText());
    }

    /**
     * Test of getID method, of class PrivateMessage.
     */
    @Test
    public void testGetID() {
        PrivateMessage privateMessage = new PrivateMessage(0, new User("reciever"), new User("sender"), "text");
        assertEquals(0, privateMessage.getID(), 0);
    }

    /**
     * Test of getSendername method, of class PrivateMessage.
     */
    @Test
    public void testGetSendername() {
        User sender = new User("sender");
        PrivateMessage privateMessage = new PrivateMessage(new User("reciever"), sender, "text");
        assertEquals("sender", privateMessage.getSendername());
    }

    /**
     * Test of getReciever method, of class PrivateMessage.
     */
    @Test
    public void testGetReciever() {
        User reciever = new User("reciever");
        PrivateMessage privateMessage = new PrivateMessage(reciever, new User("sender"), "text");
        assertEquals(reciever, privateMessage.getReciever());
    }
    
}
