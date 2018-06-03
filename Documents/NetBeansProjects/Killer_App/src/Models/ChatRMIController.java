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
import java.util.logging.Level;
import java.util.logging.Logger;
import killer_app.ChatController;

/**
 *
 * @author Marc
 */
public class ChatRMIController extends UnicastRemoteObject implements IListener{
    private ChatController controller;
    private Registry registry;
    private IPublisher publisher;
    private IDatabase database;

    public ChatRMIController(ChatController controller, String chatName) throws RemoteException{
        this.controller = controller;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            publisher = (IPublisher)registry.lookup("ChatServer");
            database = (IDatabase)registry.lookup("Database");
            publisher.RegisterProperty("chat:" + chatName);
            publisher.SubscribeListener(this, "chat:" + chatName);
        } catch (RemoteException ex) {
            throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    public void SendMessage(ChatMessage chatMessage) {
        database.SendChatMessage(chatMessage);
        try {
            publisher.Inform("chat:" + chatMessage.GetReciever(), null, chatMessage);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void PropertyChange(PropertyChangeEvent event) throws RemoteException {
        controller.GetMessage(event);
    }
    
    public void GetChat(String chatname) {
        database.GetChat(chatname);
    }
    
    public void AddToChat(GroupInvitation groupInvitation) {
        database.GroupInvitation(groupInvitation);
    }
    
    public void RemoveUserFromChat(User user, Chat chat) {
        database.RemoveUserFromChat(user, chat);
    }
    
    public void UnsubscribeListener(String property){
        try {
            publisher.UnsubscribeListener(this, property);
        } catch (RemoteException ex) {
            Logger.getLogger(ChatRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
