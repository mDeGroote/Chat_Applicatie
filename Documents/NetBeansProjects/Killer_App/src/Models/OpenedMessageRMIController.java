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
import killer_app.OpenedMessageController;

/**
 *
 * @author Marc
 */
public class OpenedMessageRMIController extends UnicastRemoteObject{
    private OpenedMessageController controller;
    private Registry registry;
    private IDatabase database;

    public OpenedMessageRMIController(OpenedMessageController controller) throws RemoteException{
        this.controller = controller;
        try {
            this.registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            this.database = (IDatabase)registry.lookup("Database");
        } catch (RemoteException ex) {
            throw new RemoteException(ex.getMessage());
        } catch (NotBoundException ex) {
            throw new RuntimeException();
        }
    }
    
    public void RemoveMessage(IMessage message) {
        database.RemoveMessage(message);
    }
    
    public void AcceptGroupInvitation(GroupInvitation groupInvitation) {
        database.AcceptGroupInvitation(groupInvitation);
    }
    
    public void AcceptFriendRequest(IMessage message, User reciever) {
        database.AcceptFriendRequest(message, reciever);
    }
}
