/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.scene.control.Alert;

/**
 *
 * @author Marc
 */
public class AlertRunnable implements Runnable{
    private String title;
    private String content;
    private Alert.AlertType alerttype;

    public AlertRunnable(String title, String content, Alert.AlertType alerttype) {
        this.title = title;
        this.content = content;
        this.alerttype = alerttype;
    }
    
    

    @Override
    public void run() {
        Alert alert = new Alert(alerttype);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    
}
