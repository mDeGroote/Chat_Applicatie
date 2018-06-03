/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killer_app;

import Models.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class ChatController implements Initializable {
    @FXML TextField textmessage;
    @FXML TableView<ChatMessage> table;
    @FXML TableColumn sender;
    @FXML TableColumn text;
    @FXML ComboBox<User> friends;
    private ObservableList messages = FXCollections.observableArrayList();
    private User user;
    private Chat chat;
    private ChatRMIController controller;
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sender.setCellValueFactory(new PropertyValueFactory<ChatMessage, String>("sendername"));
        text.setCellValueFactory(new PropertyValueFactory<ChatMessage, String>("text"));
        table.setItems(messages);
    }
    
    public void SetFields(User user, Chat chat) {
        this.user = user;
        friends.setItems(FXCollections.observableArrayList());
        friends.getItems().addAll(user.getFriends());
        this.chat = chat;
        try {
            controller = new ChatRMIController(this, chat.GetName());            
        } catch (RemoteException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
            return;
        }
        catch(RuntimeException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
            return;
        }
        try{
            messages.addAll(this.chat.GetMessages());
        }
        catch(NullPointerException ex) {
            
        }
        for(User friend: user.getFriends()) {
            for(User chatuser: this.chat.GetUsers()) {
                if(chatuser.getName().equals(friend.getName())) {
                    friends.getItems().remove(friend);
                }
            }
        }
    }

    public void SendMessage() {
        if(!textmessage.getText().isEmpty()) {
            if(controller == null) {
                try{
                    controller = new ChatRMIController(this, chat.GetName());
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                    alert.show();
                    return;
                }   
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                    alert.show();
                    return;
                }
            }
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    ChatMessage chatMessage = new ChatMessage(chat.GetName(), user, textmessage.getText());
                    controller.SendMessage(chatMessage);
                }
            });
        }
    }
    
    public void GetMessage(PropertyChangeEvent event) {
        ChatMessage chatMessage = (ChatMessage)event.getNewValue();
        messages.add(chatMessage);
    }
    
    public void Back() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainScreen.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FriendListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                controller.UnsubscribeListener(chat.GetName());
                MainScreenController mainScreenController = loader.getController();
                try{
                    mainScreenController.SetUser(user);
                }
                catch(RuntimeException ex) {
                    
                }
            }
        });
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage)table.getScene().getWindow();
        oldStage.close();
        threadpool.shutdown();
        stage.show();
    }
    
    public void AddUser() {
        if(friends.getSelectionModel().getSelectedItem() != null) {
            if(controller == null) {
                try{
                    controller = new ChatRMIController(this, chat.GetName());
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                    alert.show();
                    return;
                }
            }
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    GroupInvitation groupInvitation = new GroupInvitation(friends.getSelectionModel().getSelectedItem(), new User(chat.GetName()), "Would you like to join our group: " + chat.GetName());
                    controller.AddToChat(groupInvitation);
                    Platform.runLater(new AlertRunnable("Groupinvitation sent", "A groupinvitation has been sent to the user", Alert.AlertType.INFORMATION));
                }
            });
        }
    }
    
    public void RemoveUser() {
        if(controller == null) {
            try{
                controller = new ChatRMIController(this, chat.GetName());
            } catch (RemoteException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                alert.show();
                return;
            }
            catch(RuntimeException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers aren't responding. The message could not be sent, please try again later");
                alert.show();
                return;
            }
        }
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                controller.RemoveUserFromChat(user, chat);
            }
        });
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainScreen.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                MainScreenController mainScreenController = loader.getController();
                mainScreenController.SetUser(user);
            }
        });
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage)table.getScene().getWindow();
        oldStage.close();
        threadpool.shutdown();
        stage.show();
    }
    
}
