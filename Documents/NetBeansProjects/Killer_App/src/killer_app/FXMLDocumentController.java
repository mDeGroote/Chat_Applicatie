/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killer_app;

import Models.LoginController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Models.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Marc
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML Label label;
    @FXML TextField usernameField;
    @FXML TextField passwordField;
    private LoginController loginController;
    private Client client = new Client();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    

    public void Login() throws IOException {
        if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            try{
                loginController = new LoginController();
            }
            catch(Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers are currently not responding, try again later");
                alert.show();
                return;
            }
            User user = loginController.Login(usernameField.getText(), passwordField.getText());
            if(user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid login");
                alert.setContentText("Invalid username and password");
                alert.show();
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainScreen.fxml"));
            loader.load();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MainScreenController controller = loader.getController();
                    controller.SetUser(user);
                }
            });
            thread.start();
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage oldstage = (Stage)usernameField.getScene().getWindow();
            oldstage.close();
            stage.show();
        }
    }
    
    public void toRegister() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Register.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldstage = (Stage)usernameField.getScene().getWindow();
        oldstage.close();
        stage.show();
    }
    
}
