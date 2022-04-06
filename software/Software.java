/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.URL;
import java.util.*;
/**
 *
 * @author Vishnu
 */
public class Software extends Application {

    /**
     * @param args the command line arguments
     */
    static database wgu = new database();
    public static void main(String[] args) {
        
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
     
        Pane vbox = (Pane) FXMLLoader.load(getClass().getResource("Login1.fxml")); 
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    

    
    }
}
