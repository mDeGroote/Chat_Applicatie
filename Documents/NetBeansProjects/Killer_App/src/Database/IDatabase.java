/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Models.*;
import java.rmi.Remote;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public interface IDatabase extends Remote{
    void RegisterUser(String name, String username, String password);
    ArrayList<User> GetFriends(User user);
    User Login(String username, String password);
    ArrayList<ChatInfo> GetChats(User user);
    void FriendRequest(User reciever, User sender);
    void AcceptFriendRequest(IMessage message, User reciever);
    void RemoveMessage(IMessage message);
    void RemoveFriend(User user1, User user2);
    void CreateChat(ArrayList<User> users, String chatname);
    Chat GetChat(String chatname);
    void SendChatMessage(ChatMessage chatMessage);
    ArrayList<IMessage> GetMessages(User user);
    void PrivateMessage(PrivateMessage privateMessage);
    void GroupInvitation(GroupInvitation groupInvitation);
    void AcceptGroupInvitation(GroupInvitation groupInvitation);
    void RemoveUserFromChat(User user, Chat chat);
}
