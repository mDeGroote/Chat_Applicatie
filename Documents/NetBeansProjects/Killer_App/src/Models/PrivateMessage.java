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
public class PrivateMessage extends Message implements Serializable, IMessage{
    private User reciever;

    public PrivateMessage(int id, User reciever, User sender, String text) {
        this.id = id;
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    public PrivateMessage(User reciever, User sender, String text) {
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    @Override
    public User getSender() {
        return this.sender;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public String getSendername() {
        return this.sender.getName();
    }
    
    public User getReciever() {
        return this.reciever;
    }
    
}
