/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Marc
 */
public interface IMessage {
    User getSender();
    String getText();
    int getID();
    String getSendername();
}
