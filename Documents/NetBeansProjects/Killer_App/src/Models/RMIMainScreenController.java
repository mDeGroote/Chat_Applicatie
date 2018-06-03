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
import killer_app.MainScreenController;

/**
 *
 * @author Marc
 */
public class RMIMainScreenController extends UnicastRemoteObject implements IListener{
    private Registry registry;
    private IPublisher publisherController; 
    private MainScreenController controller;
    private IDatabase database;

    public RMIMainScreenController(MainScreenController controller, User user) throws RemoteException{
        try {       
            this.controller = controller;
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            publisherController = (IPublisher)registry.lookup("ChatServer");
            database = (IDatabase)registry.lookup("Database");
            publisherController.setUserStatus(user);
            publisherController.Inform("FriendInfo", null, user);
        } catch (RemoteException ex) {
            throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    

    @Override
    public void PropertyChange(PropertyChangeEvent event) throws RemoteException{
        ChatMessage message = (ChatMessage)event.getNewValue();
        controller.NewMessage(message);
    }
    
    public ArrayList<ChatInfo> GetChats(User user) {
        return database.GetChats(user);
    }
    
    public ArrayList<User> GetFriends(User user) {
        return database.GetFriends(user);
    }
    
    public void SubscribeToChat(ChatInfo chatInfo) {
        try {
            publisherController.SubscribeListener(this, "chat:" + chatInfo.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(RMIMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void UnsubscribeToChat(ChatInfo chatInfo) {
        try {
            publisherController.UnsubscribeListener(this, "chat:" + chatInfo.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(RMIMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Chat GetChat(String chatname) {
        return database.GetChat(chatname);
    }
    
    public void SetUserStatus(User user) {
        try {
            publisherController.setUserStatus(user);
            publisherController.Inform("FriendInfo", null, user);
        } catch (RemoteException ex) {
            Logger.getLogger(RMIMainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
