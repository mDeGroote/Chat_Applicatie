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
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class MessagesController implements Initializable {
    @FXML TableView<IMessage> table;
    @FXML TableColumn sender;
    @FXML TableColumn text;
    private User user;
    private ObservableList<IMessage> messages = FXCollections.observableArrayList();
    private MessagesRMIController controller;
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sender.setCellValueFactory(new PropertyValueFactory<Message, String>("sendername"));
        text.setCellValueFactory(new PropertyValueFactory<Message, String>("text"));
        table.setItems(messages);
    } 
    
    public void SetUser(User user) {
        this.user = user;
        try {
            controller = new MessagesRMIController(this, this.user);
        } 
        catch (RemoteException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
            return;
        }
        catch(RuntimeException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR){
            });
            return;
        }
        messages.addAll(controller.GetMessages(this.user));
    }
    
    public void OpenMessage() {
        if(table.getSelectionModel().getSelectedItem() != null) {
            IMessage message = table.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("OpenedMessage.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    OpenedMessageController openedMessageController = loader.getController();
                    openedMessageController.SetFields(user, message);
                }
            });
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage oldStage = (Stage)table.getScene().getWindow();
            oldStage.close();
            threadpool.shutdown();
            try{
                controller.UnsubscribeListener(user);
            }
            catch(NullPointerException ex) {               
            }
            stage.show();
        }
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
                MainScreenController mainScreenController = loader.getController();
                mainScreenController.SetUser(user);
            }
        });
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage) table.getScene().getWindow();
        oldStage.close();
        threadpool.shutdown();
        try{
            controller.UnsubscribeListener(user);
        }
        catch(NullPointerException ex) {               
        }
        stage.show();
    }
    
    public void GetMessage(PropertyChangeEvent event) {
        IMessage message = (IMessage)event.getNewValue();
        messages.add(message);
    }
    
    public void ToSendMessage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SendMessage.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                SendMessageController sendMessageController = loader.getController();
                sendMessageController.setUser(user);
                controller.UnsubscribeListener(user);
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
    
    public void RemoveMessage() {
        if(table.getSelectionModel().getSelectedItem()!= null){
            IMessage message = table.getSelectionModel().getSelectedItem();
            if(controller == null) {
                try {
                    controller = new MessagesRMIController(this, user);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding and the message could not be deleted, please try again later");
                    alert.show();
                    return;
                
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers aren't responding and the message could not be deleted, please try again later");
                    alert.show();
                    return;
                }
            }
            controller.RemoveMessage(message);
            table.getItems().remove(message);
        }
    }
    
}
