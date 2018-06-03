/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killer_app;

import Models.RegisterDatabaseController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Marc
 */
public class RegisterController implements Initializable {
    @FXML TextField username;
    @FXML TextField password;
    @FXML TextField name;
    private RegisterDatabaseController controller;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {      
    }

    public void Register() {
        if(!username.getText().isEmpty() && !password.getText().isEmpty() && !name.getText().isEmpty()) {
            try{
                controller = new RegisterDatabaseController();
            }
            catch(Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Servers down");
                alert.setContentText("The servers are not responding, please try again later.");
                alert.show();
                return;
            }
            try{
                controller.RegisterUser(name.getText(), username.getText(), password.getText());
            }
            catch(IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Name taken");
                alert.setContentText("Name already exists");
                alert.show();
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage oldStage = (Stage)name.getScene().getWindow();
            oldStage.close();
            stage.show();
        }
    }
    
    public void Back() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLDocument.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage oldStage = (Stage)name.getScene().getWindow();
        oldStage.close();
        stage.show();
    }
    
}
