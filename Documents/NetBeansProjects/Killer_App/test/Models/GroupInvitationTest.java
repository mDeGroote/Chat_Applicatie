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
 * @author Marc
 */
public class GroupInvitationTest {
    /**
     * Test of GetSender method, of class GroupInvitation.
     */
    @Test
    public void testGetSender() {
        User sender = new User("sender");
        GroupInvitation groupInvitation = new GroupInvitation(new User("reciever"), sender, "text");
        assertSame(sender, groupInvitation.getSender());
    }

    /**
     * Test of GetReciever method, of class GroupInvitation.
     */
    @Test
    public void testGetReciever() {
        User reciever = new User("reciever");
        GroupInvitation groupInvitation = new GroupInvitation(reciever, new User("sender"), "text");
        assertSame(reciever, groupInvitation.GetReciever());
    }

    /**
     * Test of GetText method, of class GroupInvitation.
     */
    @Test
    public void testGetText() {
        String text = "text";
        GroupInvitation groupInvitation = new GroupInvitation(new User("reciever"), new User("sender"), text);
        assertSame(text, groupInvitation.getText());
    }

    /**
     * Test of GetID method, of class GroupInvitation.
     */
    @Test
    public void testGetID() {
        
    }
    
}
