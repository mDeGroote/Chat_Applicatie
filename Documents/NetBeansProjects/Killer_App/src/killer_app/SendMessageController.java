/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killer_app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import Models.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class SendMessageController implements Initializable {
    @FXML ComboBox<User> friends;
    @FXML TextArea messageText;
    private User user;
    private SendMessageRMIController controller;
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void SendMessage() {
        if(friends.getSelectionModel().getSelectedItem() != null && !messageText.getText().isEmpty()) {
            if(controller == null) {
                try {
                    controller = new SendMessageRMIController(this);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding and your message could not be sent, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding and your message could not be sent, please try again later");
                    alert.show();
                    return;
                }
            }
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    PrivateMessage privateMessage = new PrivateMessage(friends.getSelectionModel().getSelectedItem(), user, messageText.getText());
                    controller.SendMessage(privateMessage);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Sended");
                            alert.setContentText("Message send!!");
                            alert.showAndWait();
                            Back();
                        }
                    });
                }
            });
        }
    }
    
    public void Back() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Messages.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SendMessageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                MessagesController messagesController = loader.getController();
                messagesController.SetUser(user);
            }
        });
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage)friends.getScene().getWindow();
        oldStage.close();
        threadpool.shutdown();
        stage.show();
    }
    
    public void setUser(User user) {
        this.user = user;
        friends.setItems(FXCollections.observableList(user.getFriends()));
        try {
            controller = new SendMessageRMIController(this);
        } catch (RemoteException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
        }
        catch(RuntimeException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
        }
    }
}
