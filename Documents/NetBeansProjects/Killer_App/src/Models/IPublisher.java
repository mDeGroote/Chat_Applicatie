/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public interface IPublisher extends Remote{
    void SubscribeListener(IListener listener, String property) throws RemoteException;
    void UnsubscribeListener(IListener listener, String property) throws RemoteException;
    void Inform(String property, Object oldValue, Object newValue) throws RemoteException;
    void RegisterProperty(String property) throws RemoteException;
    void UnregisterProperty(String property) throws RemoteException;
    void setUserStatus(User user) throws RemoteException;
    ArrayList<User> getFriendsStatus(ArrayList<User> friends) throws RemoteException;
}
