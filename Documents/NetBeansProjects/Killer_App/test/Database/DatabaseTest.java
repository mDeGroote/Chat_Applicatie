/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Models.Chat;
import Models.ChatInfo;
import Models.ChatMessage;
import Models.FriendRequest;
import Models.GroupInvitation;
import Models.IMessage;
import Models.PrivateMessage;
import Models.User;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marc
 */
public class DatabaseTest {
    private Database database = new Database();
    
    public DatabaseTest() {
        try{
            database.RegisterUser("TestUser", "TestUser", "TestUser");
            database.RegisterUser("TestUser2", "TestUser2", "TestUser2");
            database.CreateChat(null, "TestChat");
        }
        catch(Exception ex) {
            
        }
    }

    /**
     * Test of RegisterUser method, of class Database.
     */
    @Test
    public void testRegisterUser() {
        database.RegisterUser("RegisterTest", "RegisterTest", "RegisterTest");
        assertNotNull(database.Login("RegisterTest", "RegisterTest"));
    }

    /**
     * Test of GetChatMessages method, of class Database.
     */
    @Test
    public void testGetChatMessages() {
        database.SendChatMessage(new ChatMessage("TestChat", new User("TestUser"), "test"));
        ArrayList<ChatMessage> chatMessages = database.GetChatMessages("TestChat", null);
        for(ChatMessage chatMessage : chatMessages) {
            if(chatMessage.getText().equals("test")) {
                assertTrue(true);
                return;
            }
        }
        assertTrue(false);
    }

    /**
     * Test of GetFriends method, of class Database.
     */
    @Test
    public void testGetFriends() {
        User user = new User("TestUser");
        FriendRequest friendRequest = new FriendRequest(user, new User("TestUser2"), "Would you like to be my friend?");
        database.FriendRequest(friendRequest.GetReciever(), friendRequest.getSender());
        database.AcceptFriendRequest(friendRequest, user);
        ArrayList<User> users = database.GetFriends(user);
        for(User friend: users) {
            if(friend.getName().equals("TestUser2")) {
                assertTrue(true);
                return;
            }
        }
        assertTrue(false);
    }

    /**
     * Test of Login method, of class Database.
     */
    @Test
    public void testLogin() {
        User user = database.Login("TestUser", "TestUser");
        assertEquals(user.getName(), "TestUser");
    }

    /**
     * Test of GetChats method, of class Database.
     */
    @Test
    public void testGetChats() {
        GroupInvitation groupInvitation = new GroupInvitation(new User("TestUser"), new User("TestChat"), "Would you like to join our group?");
        database.GroupInvitation(groupInvitation);
        database.AcceptGroupInvitation(groupInvitation);
        ArrayList<ChatInfo> chats = database.GetChats(new User("TestUser"));
        for(ChatInfo chat: chats) {
            if(chat.getName().equals("TestChat")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of FriendRequest method, of class Database.
     */
    @Test
    public void testFriendRequest() {
        User user = new User("TestUser");
        database.FriendRequest(user, new User("TestUser2"));
        ArrayList<IMessage> messages = database.GetMessages(user);
        for(IMessage message : messages) {
            if(message.getSendername().equals("TestUser2")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of AcceptFriendRequest method, of class Database.
     */
    @Test
    public void testAcceptFriendRequest() {
        User reciever = new User("TestUser");
        FriendRequest friendRequest = new FriendRequest(reciever, new User("TestUser2"), "Would you like to be my friend?");
        database.FriendRequest(reciever, friendRequest.getSender());
        database.AcceptFriendRequest(friendRequest, reciever);
        for(User friend: database.GetFriends(reciever)) {
            if(friend.getName().equals("TestUser2")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of RemoveMessage method, of class Database.
     */
    @Test
    public void testRemoveMessage() {
        FriendRequest friendRequest = new FriendRequest(new User("TestUser"), new User("TestUser2"), "Would you like to be my friend");
        try{
            database.FriendRequest(friendRequest.GetReciever(), friendRequest.getSender());
        }
        catch(Exception ex) {
            
        }
        database.RemoveMessage(friendRequest);
        for(IMessage message: database.GetMessages(friendRequest.GetReciever())) {
            if(message.getSender().getName().equals(friendRequest.getSender().getName()) && message.getText().equals(friendRequest.getText())) {
                assertTrue(false);
                return;
            }
        }
        assertFalse(false);
    }

    /**
     * Test of RemoveFriend method, of class Database.
     */
    @Test
    public void testRemoveFriend() {
        User reciever = new User("TestUser");
        FriendRequest friendRequest = new FriendRequest(reciever, new User("TestUser2"), "Would you like to be my friend?");
        database.FriendRequest(friendRequest.GetReciever(), friendRequest.getSender());
        database.AcceptFriendRequest(friendRequest, friendRequest.GetReciever());
        database.RemoveFriend(friendRequest.GetReciever(), friendRequest.getSender());
        ArrayList<User> users = database.GetFriends(reciever);
        for(User user: users) {
            if(user.getName().equals("TestUser2")) {
                assertTrue(false);
                return;
            }
        }
        assertFalse(false);
    }

    /**
     * Test of CreateChat method, of class Database.
     */
    @Test
    public void testCreateChat() {
        try{
            database.CreateChat(null, "CreateChatTest");
        }
        catch(Exception ex) {
            
        }
        assertEquals(database.GetChat("CreateChatTest").GetName(), "CreateChatTest");
    }

    /**
     * Test of GetChatUsers method, of class Database.
     */
    @Test
    public void testGetChatUsers() {
        Chat chat = new Chat("Marcje", null, null);
        ArrayList<User> users = database.GetChatUsers(chat.GetName());
        for(User user: users) {
            if(user.getName().equals("Marc")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of GetChat method, of class Database.
     */
    @Test
    public void testGetChat() {
        String chatname = "TestChat";
        assertNotNull(database.GetChat(chatname));
        
    }

    /**
     * Test of SendChatMessage method, of class Database.
     */
    @Test
    public void testSendChatMessage() {
        database.SendChatMessage(new ChatMessage("TestChat", new User("TestUser"), "text"));
        ArrayList<ChatMessage> chatMessages = database.GetChatMessages("TestChat", null);
        for(ChatMessage chatMessage: chatMessages) {
            if(chatMessage.getSender().getName().equals("TestUser") && chatMessage.getText().equals("text")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of GetMessages method, of class Database.
     */
    @Test
    public void testGetMessages() {
        User user = new User("TestUser");
        PrivateMessage privateMessage = new PrivateMessage(user, new User("TestUser2"), "test");
        database.PrivateMessage(privateMessage);
        ArrayList<IMessage> messages = database.GetMessages(user);
        for(IMessage message : messages) {
            if(message.getText().equals("test") && message.getSendername().equals("TestUser2")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of PrivateMessage method, of class Database.
     */
    @Test
    public void testPrivateMessage() {
        PrivateMessage privateMessage = new PrivateMessage(new User("TestUser"), new User("TestUser2"), "hey");
        database.PrivateMessage(privateMessage);
        for(IMessage message: database.GetMessages(privateMessage.getReciever())) {
            if(message.getSender().getName().equals(privateMessage.getSender().getName()) && message.getText().equals(privateMessage.getText())) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of GroupInvitation method, of class Database.
     */
    @Test
    public void testGroupInvitation() {
        GroupInvitation groupInvitation = new GroupInvitation(new User("TestUser"),new User("TestChat"), "Would you like to join our group");
        database.GroupInvitation(groupInvitation);
        for(IMessage message: database.GetMessages(groupInvitation.GetReciever())) {
            if(message.getSender().getName().equals(groupInvitation.getSender().getName()) && message.getText().equals(groupInvitation.getText())) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of AcceptGroupInvitation method, of class Database.
     */
    @Test
    public void testAcceptGroupInvitation() {
        GroupInvitation groupInvitation = new GroupInvitation(new User("TestUser"),new User("TestChat"), "Would you like to join our group");
        database.GroupInvitation(groupInvitation);
        database.AcceptGroupInvitation(groupInvitation);
        for(User user: database.GetChatUsers("TestChat")) {
            if(user.getName().equals("TestUser")) {
                assertTrue(true);
                return;
            }
        }
        assertFalse(true);
    }

    /**
     * Test of RemoveUserFromChat method, of class Database.
     */
    @Test
    public void testRemoveUserFromChat() {
        database.RemoveUserFromChat(new User("TestUser"), new Chat("TestChat", null, null));
        for(User user: database.GetChatUsers("TestChat")) {
            if(user.getName().equals("TestUser")) {
                assertTrue(false);
                return;
            }
        }
        assertFalse(false);
    }
    
}
