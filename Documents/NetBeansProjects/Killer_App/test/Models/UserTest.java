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
public class UserTest {
    /**
     * Test of GetStatus method, of class User.
     */
    @Test
    public void testGetStatus() {
        Status status = Status.Offline;
        User user = new User("name", new ArrayList<User>(), status);
        assertEquals(status, user.getStatus());
    }

    /**
     * Test of SetStatus method, of class User.
     */
    @Test
    public void testSetStatus() {
        Status status = Status.Offline;
        User user = new User("name", new ArrayList<User>(), Status.Invisible);
        user.SetStatus(status);
        assertSame(status, user.getStatus());
    }

    /**
     * Test of GetFriends method, of class User.
     */
    @Test
    public void testGetFriends() {
        ArrayList<User> friends = new ArrayList<>();
        User user = new User("name", friends, Status.Busy);
        assertSame(friends, user.getFriends());
    }

    /**
     * Test of GetName method, of class User.
     */
    @Test
    public void testGetName() {
        String name = "name";
        User user = new User(name, new ArrayList<User>(), Status.Busy);
        assertSame(name, user.getName());
    }
    
}
