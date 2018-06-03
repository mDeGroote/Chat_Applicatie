/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Database.IDatabase;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import killer_app.SendMessageController;

/**
 *
 * @author Marc
 */
public class SendMessageRMIController extends UnicastRemoteObject{
    private SendMessageController controller;
    private Registry registry;
    private IDatabase database;
    private IPublisher publisher;

    public SendMessageRMIController(SendMessageController controller) throws RemoteException{
        this.controller = controller;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            database = (IDatabase) registry.lookup("Database");
            publisher = (IPublisher)registry.lookup("ChatServer");
        } catch (RemoteException ex) {
            throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    public void SendMessage(PrivateMessage privateMessage) {
        database.PrivateMessage(privateMessage);
        try {
            publisher.Inform("user:" + privateMessage.getReciever().getName(), null, privateMessage);
        } catch (RemoteException ex) {
            Logger.getLogger(SendMessageRMIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
