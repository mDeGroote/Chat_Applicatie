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
import killer_app.MessagesController;

/**
 *
 * @author Marc
 */
public class MessagesRMIController extends UnicastRemoteObject implements IListener{
    private Registry registry;
    private IDatabase database;
    private IPublisher publisher;
    private MessagesController controller;

    public MessagesRMIController(MessagesController controller, User user) throws RemoteException{
        try {
            this.controller = controller;
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            database = (IDatabase)registry.lookup("Database");
            publisher = (IPublisher)registry.lookup("ChatServer");
            publisher.RegisterProperty("user:" + user.getName());
            publisher.SubscribeListener(this, "user:" + user.getName());
        } catch (RemoteException ex) {
           throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    public ArrayList<IMessage> GetMessages(User user) {
        return database.GetMessages(user);
    }

    @Override
    public void PropertyChange(PropertyChangeEvent event) throws RemoteException {
        controller.GetMessage(event);
    }
    
    public void UnsubscribeListener(User user) {
        try {
            publisher.UnsubscribeListener(this, user.getName());
        } catch (RemoteException ex) {
            Logger.getLogger(MessagesRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RemoveMessage(IMessage message) {
        database.RemoveMessage(message);
    }
    
    
}
