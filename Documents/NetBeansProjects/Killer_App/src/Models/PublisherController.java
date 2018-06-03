/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public class PublisherController extends UnicastRemoteObject implements IPublisher{
    private Publisher publisher = new Publisher();

    public PublisherController() throws RemoteException {
    }
    
    
    
    @Override
    public void SubscribeListener(IListener listener, String property) throws RemoteException{
        publisher.SubscribeListener(listener, property);
    }

    @Override
    public void UnsubscribeListener(IListener listener, String property) throws RemoteException{
        publisher.UnsubscribeListener(listener, property);
    }

    @Override
    public void Inform(String property, Object oldValue, Object newValue) throws RemoteException{
        publisher.Inform(property, oldValue, newValue);
    }

    @Override
    public void RegisterProperty(String property) throws RemoteException{
        publisher.RegisterProperty(property);
    }

    @Override
    public void UnregisterProperty(String property) throws RemoteException{
        publisher.UnregisterProperty(property);
    }

    @Override
    public void setUserStatus(User user) {
        publisher.setUserStatus(user);
    }

    @Override
    public ArrayList<User> getFriendsStatus(ArrayList<User> friends) {
        return publisher.getFriendsStatus(friends);
    }
    
}
