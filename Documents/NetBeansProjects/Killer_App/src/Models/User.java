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
public class User implements Serializable{
    private String name;
    private transient ArrayList<User> friends;
    private Status status;

    public User(String name, ArrayList<User> friends, Status status) {
        this.name = name;
        this.friends = friends;
        this.status = status;
    }
    
    public User(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }
    
    public void SetStatus(Status status) {
        this.status = status; 
    }
    
    public ArrayList<User> getFriends(){
        return friends;
    }
    
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    
}
