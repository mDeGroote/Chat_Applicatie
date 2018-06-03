/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Marc
 */
public interface IListener extends Remote{
    void PropertyChange(PropertyChangeEvent event) throws RemoteException;
}
