/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killer_app;

import Models.RMIMainScreenController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Models.*;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class MainScreenController implements Initializable{
    @FXML Button Logout;
    @FXML TableView<ChatInfo> Table;
    @FXML TableColumn chatname;
    @FXML TableColumn numberofmembers;
    @FXML TableColumn newmessages;
    private User user;
    private ObservableList<ChatInfo> info = FXCollections.observableArrayList();
    private RMIMainScreenController RMIMainScreenController;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        chatname.setCellValueFactory(new PropertyValueFactory<ChatInfo, String>("name"));
        numberofmembers.setCellValueFactory(new PropertyValueFactory<ChatInfo, Integer>("numberOfMembers"));
        newmessages.setCellValueFactory(new PropertyValueFactory<ChatInfo, Integer>("newMessages"));
        Table.setItems(info);
    }
    
    public void SetUser(User user) {
        this.user = user;
        try {
            RMIMainScreenController = new RMIMainScreenController(this, user);
        } catch (RuntimeException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR) {
            });
            return;
        } catch (RemoteException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR) {
            });
            return;
        }
        AddChats();
    }

    public void Logout() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage)Logout.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        newStage.setScene(scene);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                user.SetStatus(Status.Offline);
                try{
                    RMIMainScreenController.SetUserStatus(user);
                    for(ChatInfo chatInfo : info) {
                        RMIMainScreenController.UnsubscribeToChat(chatInfo);
                    }
                }
                catch(NullPointerException ex) {
                }
            }
        });
        threadPool.shutdown();
        newStage.show();
    }
    
    public void AddChats() {
        try{
            info.addAll(RMIMainScreenController.GetChats(user));
            for(ChatInfo chatInfo: info) {
                RMIMainScreenController.SubscribeToChat(chatInfo);
            }
        }
        catch(NullPointerException ex) {
            Platform.runLater(new AlertRunnable("Servers down", "The servers aren't responding. You might not see the information you requested, please try again later", Alert.AlertType.ERROR) {
            });
        }
    }
    
    public void NewMessage(ChatMessage chatMessage) {
        for(ChatInfo chatInfo: info) {
            if(chatInfo.getName().equals(chatMessage.GetReciever())) {
                info.remove(chatInfo);
                chatInfo.setNewMessages(chatInfo.getNewMessages() + 1);
                info.add(chatInfo);
            }
        }
    }
    
    public void toFriendList() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FriendList.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FriendListController controller = loader.getController();
                controller.setUser(user);
            }
        });
        threadPool.execute(thread);
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldstage = (Stage)Table.getScene().getWindow();
        oldstage.close();
        for(ChatInfo chatInfo: info) {
            try{
                RMIMainScreenController.UnsubscribeToChat(chatInfo);               
            }
            catch(NullPointerException ex) {             
            }
        }
        stage.show();
    }
    
    public void OpenChat() {
        if(Table.getSelectionModel().getSelectedItem() != null) {
            ChatInfo chatInfo = Table.getSelectionModel().getSelectedItem();
            Chat chat = RMIMainScreenController.GetChat(chatInfo.getName());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Chat.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ChatController controller = loader.getController();
                    try{
                        controller.SetFields(user, chat);                  
                    }
                    catch(RuntimeException ex) {                       
                    }
                }
            });
            threadPool.execute(thread);
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage oldstage = (Stage)Table.getScene().getWindow();
            oldstage.close();
            try{
                for(ChatInfo chatToUnsubscribe: info) {
                    RMIMainScreenController.UnsubscribeToChat(chatToUnsubscribe);
                }
            }
            catch(NullPointerException ex) {
                
            }
            stage.show();
        }
    }
    
    public void ToMessages() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Messages.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MessagesController messagesController = loader.getController();
                messagesController.SetUser(user);
                for(ChatInfo chatInfo: info) {
                    RMIMainScreenController.UnsubscribeToChat(chatInfo);
                }
            }
        });
        threadPool.execute(thread);
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage)Table.getScene().getWindow();
        oldStage.close();
        stage.show();
    }
    
}
