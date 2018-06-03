/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Marc
 */
public class Chat implements Serializable{
    private String name;
    private ArrayList<User> users;
    private ArrayList<ChatMessage> messages;

    public Chat(String name, ArrayList<User> users, ArrayList<ChatMessage> messages) {
        this.name = name;
        this.users = users;
        this.messages = messages;
    }
    
    public ArrayList<User> GetUsers() {
        return users;
    }
    
    public ArrayList<ChatMessage> GetMessages() {
        return messages;
    }
    
    public String GetName() {
        return name;
    }
}
