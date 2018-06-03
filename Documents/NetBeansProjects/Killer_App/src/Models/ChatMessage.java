/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Marc
 */
public class ChatMessage implements IMessage, Serializable{
    private String reciever;
    private User sender;
    private String text;
    private int id;

    public ChatMessage(String reciever, User sender, String text) {
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
    
    public ChatMessage(int id,String reciever, User sender, String text) {
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
    }
  
    @Override
    public User getSender() {
        return sender;
    }

    
    public String GetReciever() {
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
