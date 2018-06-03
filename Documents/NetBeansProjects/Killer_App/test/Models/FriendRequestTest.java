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
public class FriendRequestTest {
    /**
     * Test of GetSender method, of class FriendRequest.
     */
    @Test
    public void testGetSender() {
        User sender = new User("sender");
        FriendRequest friendRequest = new FriendRequest(new User("reciever"), sender, "text");
        assertSame(sender, friendRequest.getSender());
    }

    /**
     * Test of GetReciever method, of class FriendRequest.
     */
    @Test
    public void testGetReciever() {
        User reciever = new User("reciever");
        FriendRequest friendRequest = new FriendRequest(reciever, new User("sender"), "text");
        assertSame(reciever, friendRequest.GetReciever());
    }

    /**
     * Test of GetText method, of class FriendRequest.
     */
    @Test
    public void testGetText() {
        String text = "text";
        FriendRequest friendRequest = new FriendRequest(new User("reciever"), new User("sender"), text);
        assertSame(text, friendRequest.getText());
    }

    /**
     * Test of GetID method, of class FriendRequest.
     */
    @Test
    public void testGetID() {
        
    }
    
}
