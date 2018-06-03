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
import java.util.ArrayList;
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
public class FriendListController implements Initializable {
    @FXML TableView<User> Table;
    @FXML TableColumn name;
    @FXML TableColumn status;
    @FXML TextField friendname;
    @FXML TextField groupname;
    @FXML ComboBox<Status> userStatus;
    private User user;
    private FriendsRMIController controller;
    private ObservableList<User> friends;
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        status.setCellValueFactory(new PropertyValueFactory<User, Status>("status"));
        userStatus.getItems().add(Status.Busy);
        userStatus.getItems().add(Status.Invisible);
        userStatus.getItems().add(Status.Online);
    }

    public void setUser(User user) {
        this.user = user;
        try {
            controller = new FriendsRMIController(this);
            user.setFriends(controller.GetFriendsStatus(this.user));
            friends = FXCollections.observableList(user.getFriends());
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
            for(User friend: friends) {
                if(friend.getStatus().equals(Status.Invisible)) {
                    friend.SetStatus(Status.Offline);
                }
            }
        }
        catch(NullPointerException ex) {
        }
        Table.setItems(friends);
    }

    public void SendFriendRequest() {
        if(!friendname.getText().isEmpty()) {
            if(controller == null) {
                try {
                    controller = new FriendsRMIController(this);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and friendrequest could not be sent, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and friendrequest could not be sent, please try again later");
                    alert.show();
                    return;
                }
            }
            threadpool.execute(new Runnable() {
            @Override
                public void run() {
                    try{
                        controller.SendFriendRequest(new User(friendname.getText()), user);
                    }
                    catch(IllegalArgumentException ex) {
                        Platform.runLater(new AlertRunnable("Invalid name", "Server could not find the user, please check the name and try again", Alert.AlertType.ERROR));
                        return;
                    }
                    Platform.runLater(new AlertRunnable("FriendRequest Sent!!", "A friendrequest has been sent to the user", Alert.AlertType.INFORMATION));
                }
            });
        }
    }
    
    public void RemoveFriend() {
        if(Table.getSelectionModel().getSelectedItem() != null) {
            if(controller == null) {
                try {
                    controller = new FriendsRMIController(this);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and friend could not be removed, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and friend could not be removed, please try again later");
                    alert.show();
                    return;
                }
            }
            User user = Table.getSelectionModel().getSelectedItem();
            controller.RemoveFriend(user, new User(this.user.getName()));
            Table.getItems().remove(user);
        }
    }
    
        
    public void Back() {
        try{
            controller.UnsubscribeListener();
        }
        catch(NullPointerException ex) {
            
        }
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
        Stage oldStage = (Stage) Table.getScene().getWindow();
        oldStage.close();
        stage.show();
    }
    
    public void CreateGroupChat() {
        if(!groupname.getText().isEmpty() && Table.getSelectionModel().getSelectedItem() != null) {
            if(controller == null) {
                try {
                    controller = new FriendsRMIController(this);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and group could not be created, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and group could not be created, please try again later");
                    alert.show();
                    return;
                }
            }
            ArrayList<User> users = new ArrayList<>();
            users.add(user);
            User friend = Table.getSelectionModel().getSelectedItem();
            users.add(new User(friend.getName()));
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    controller.CreateGroupChat(users, groupname.getText());
                    Platform.runLater(new AlertRunnable("Group created!!", "Your group has been created", Alert.AlertType.INFORMATION));
                }
            });        
        }
    }
    
    public void setFriendStatus(PropertyChangeEvent event) {
        User friend = (User)event.getNewValue();
        for(User friendInfo: user.getFriends()) {
            if(friendInfo.getName().equals(friend.getName())) {
                user.getFriends().remove(friendInfo);
                if(friend.getStatus().equals(Status.Invisible)) {
                    friendInfo.SetStatus(Status.Offline);
                }
                else {
                    friendInfo.SetStatus(friend.getStatus());
                }
                user.getFriends().add(friendInfo);
                friends.setAll(user.getFriends());
            }
        }
    }
    
    public void setUserStatus() {
        if(userStatus.getSelectionModel().getSelectedItem() != null) {
            if(controller == null) {
                try {
                    controller = new FriendsRMIController(this);
                } catch (RemoteException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and status could not be changed, please try again later");
                    alert.show();
                    return;
                }
                catch(RuntimeException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Servers down");
                    alert.setContentText("The servers arent responding and status could not be changed, please try again later");
                    alert.show();
                    return;
                }
            }
            threadpool.execute(new Runnable() {
                @Override
                public void run() {
                    controller.SetUserStatus(user);
                    user.SetStatus(userStatus.getSelectionModel().getSelectedItem());
                    Platform.runLater(new AlertRunnable("Status changed", "User status has been changed", Alert.AlertType.INFORMATION));
                }
            }); 
        }

    }
}
