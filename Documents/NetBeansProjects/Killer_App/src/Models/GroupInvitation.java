/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;

/**
 *
 * @author Marc
 */
public class GroupInvitation extends Message implements IMessage, Serializable{
    private User reciever;

    public GroupInvitation(int id, User reciever, User sender, String text) {
        this.id = id;
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    public GroupInvitation(User reciever, User sender, String text) {
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    public User GetReciever() {
        return reciever;
    }

    public String getText() {
        return text;
    }

    public int getID() {
        return id;
    }

    public User getSender() {
        return sender;
    }
    
    public String getSendername() {
        return sender.getName();
    }
    
}
