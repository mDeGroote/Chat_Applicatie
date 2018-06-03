/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Marc
 */
public class ChatInfo {
    private SimpleStringProperty name;
    private SimpleIntegerProperty numberOfMembers;
    private SimpleIntegerProperty newMessages;

    public ChatInfo(String name, int numberOfMembers, int newMessages) {
        this.name = new SimpleStringProperty(name);
        this.numberOfMembers = new SimpleIntegerProperty(numberOfMembers);
        this.newMessages = new SimpleIntegerProperty(newMessages);
    } 

    public String getName() {
        return name.get();
    }

    public int getNumberOfMembers() {
        return numberOfMembers.get();
    }

    public int getNewMessages() {
        return newMessages.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers.set(numberOfMembers);
    }

    public void setNewMessages(int newMessages) {
        this.newMessages.set(newMessages);
    }
    
    
}
