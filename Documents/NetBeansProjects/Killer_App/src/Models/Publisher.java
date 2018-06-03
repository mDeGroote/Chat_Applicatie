/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc
 */
public class Publisher implements IPublisher, Serializable, Remote{
    
    private Map<String, ArrayList<IListener>> listeners = new HashMap<>();
    private Map<String, Status> users = new HashMap<>();

    public Publisher(){
        this(new String[0]);
    }

    public Publisher(String[] properties){
        
        for(String property : properties) {
            listeners.put(property, new ArrayList<>());
        }
        RegisterProperty("FriendInfo");
    }
    
    

    @Override
    public void SubscribeListener(IListener listener, String property) {
        if(CheckPropertyExists(property)) {
            listeners.get(property).add(listener);
        }
    }

    @Override
    public void UnsubscribeListener(IListener listener, String property) {
        if(CheckPropertyExists(property)) {
            listeners.get(property).remove(listener);
        }
    }

    @Override
    public void Inform(String property, Object oldValue, Object newValue) {
        ArrayList<IListener> listenersToBeInformed = new ArrayList<>();
        if(CheckPropertyExists(property)) {
            listenersToBeInformed.addAll(listeners.get(property));
            for(IListener subscribedListener : listenersToBeInformed) {
                PropertyChangeEvent event = new PropertyChangeEvent(this, property, oldValue, newValue);
                try {
                    subscribedListener.PropertyChange(event);
                } catch (RemoteException ex) {
                    Logger.getLogger(Publisher.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        }
    }

    @Override
    public void RegisterProperty(String property) {
        if(!CheckPropertyExists(property)) {
            listeners.put(property, new ArrayList<>());
        }
    }

    @Override
    public void UnregisterProperty(String property) {
        if(CheckPropertyExists(property)) {
            listeners.remove(property);
        }
    }
    
    public Boolean CheckPropertyExists(String property) {
        return listeners.containsKey(property);
    }

    @Override
    public void setUserStatus(User user) {
        if(users.get(user.getName()) != null) {
            users.replace(user.getName(), user.getStatus());
        }
        else {
            users.put(user.getName(), user.getStatus());
        }
    }

    @Override
    public ArrayList<User> getFriendsStatus(ArrayList<User> friends) {
        for(User friend: friends) {
            friend.SetStatus(users.get(friend.getName()));
            if(friend.getStatus() == null) {
                friend.SetStatus(Status.Unknown);
            }
        }
        return friends;
    }
    
}
