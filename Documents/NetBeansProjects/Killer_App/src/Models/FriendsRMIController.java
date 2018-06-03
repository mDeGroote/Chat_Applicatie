/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Database.IDatabase;
import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import killer_app.FriendListController;

/**
 *
 * @author Marc
 */
public class FriendsRMIController extends UnicastRemoteObject implements IListener{
    private FriendListController friendListController;
    private Registry registry;
    private IPublisher publisher;
    private IDatabase database;

    public FriendsRMIController(FriendListController friendListController) throws RemoteException{
        this.friendListController = friendListController;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            database = (IDatabase) registry.lookup("Database");
            publisher = (IPublisher)registry.lookup("ChatServer");
            publisher.SubscribeListener(this, "FriendInfo");
        } catch (RemoteException ex) {
            throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    

    @Override
    public void PropertyChange(PropertyChangeEvent event) throws RemoteException {
        friendListController.setFriendStatus(event);
    }
    
    public void SendFriendRequest(User reciever, User sender) {
        try{
            database.FriendRequest(reciever, sender);
        }
        catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    public void RemoveFriend(User user1, User user2) {
        database.RemoveFriend(user1, user2);
    }
    
    public void CreateGroupChat(ArrayList<User> users, String chatname) {
        database.CreateChat(users, chatname);
    }
    
    public void SetUserStatus(User user) {
        try {
            publisher.setUserStatus(user);
            publisher.Inform("FriendInfo", null, user);
        } catch (RemoteException ex) {
            Logger.getLogger(FriendsRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UnsubscribeListener() {
        try {
            publisher.UnsubscribeListener(this, "FriendInfo");
        } catch (RemoteException ex) {
            Logger.getLogger(FriendsRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<User> GetFriendsStatus(User user) {
        try {
            ArrayList<User> friends = database.GetFriends(user);
            return publisher.getFriendsStatus(friends);
        } catch (RemoteException ex) {
            Logger.getLogger(FriendsRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
