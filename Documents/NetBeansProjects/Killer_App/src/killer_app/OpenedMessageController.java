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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class OpenedMessageController implements Initializable {
    @FXML TextArea messageText;
    @FXML Button acceptButton;
    @FXML Button declineButton;
    private User user;
    private IMessage message;
    private OpenedMessageRMIController controller;
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void SetFields(User user, IMessage message) {
        this.user = user;
        this.message = message;
        if(message.getClass().equals(PrivateMessage.class)) {
            acceptButton.visibleProperty().set(false);
            declineButton.visibleProperty().set(false);
        }
        messageText.setText(message.getText());
        try {
            controller = new OpenedMessageRMIController(this);
        } catch (RemoteException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
        }
        catch(RuntimeException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
        }
    }
    
    public void Back() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Messages.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FriendListController.class.getName()).log(Level.SEVERE, null, ex);
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
        Stage oldStage = (Stage) messageText.getScene().getWindow();
        oldStage.close();
        threadpool.shutdown();
        stage.show();
    }
    
    public void Accept() {
        if(controller == null) {
            try {
                controller = new OpenedMessageRMIController(this);
            } catch (RemoteException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding and the accept request could not be processed, please try again later");
                alert.show();
                return;
            }
            catch(RuntimeException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding and the accept request could not be processed, please try again later");
                alert.show();
                return;
            }
        }
        if(message.getClass().equals(GroupInvitation.class)) {
            GroupInvitation groupInvitation = new GroupInvitation(message.getID(), user, message.getSender(), message.getText());
            controller.AcceptGroupInvitation(groupInvitation);
        }
        else {
            FriendRequest friendRequest = new FriendRequest(message.getID(), user, message.getSender(), message.getText());
            controller.AcceptFriendRequest(message, user);
        }
        Back();
    }
    
    public void Decline() {
        if(controller == null) {
            try {
                controller = new OpenedMessageRMIController(this);
            } catch (RemoteException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding and the decline request could not be processed, please try again later");
                alert.show();
                return;
            }
            catch(RuntimeException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding and the decline request could not be processed, please try again later");
                alert.show();
                return;
            }
        }
        controller.RemoveMessage(message);
        Back();
    }
    
}
