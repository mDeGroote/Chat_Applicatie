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
public class FriendRequest extends Message implements IMessage, Serializable{
    private User reciever;

    public FriendRequest(int id, User reciever, User sender, String text) {
        this.id = id;
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    public FriendRequest(User reciever, User sender, String text) {
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    
    @Override
    public User getSender() {
        return sender;
    }

    
    public User GetReciever() {
        return reciever;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getSendername() {
        return sender.getName();
    }
    
}
