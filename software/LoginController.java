/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;

import javafx.scene.control.ChoiceBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.ZoneId;  
import java.time.format.TextStyle;
import java.util.Locale;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Date;
import  java.time.* ;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Vishnu
 */


/**
 *
 * @author Vishnu
 */
public class LoginController implements Initializable {

     @FXML
    private Button submit;

    @FXML
    private Label location;
    
    @FXML
    private TextField user;
    
    @FXML
    private ChoiceBox Language;
    
    @FXML
    private TextField password;
    private ResourceBundle bundle;
    private Locale locale;
    ZoneId z = ZoneId.systemDefault();
    /**
     * Initializes the controller class.
     * LAMBDA is used for event handling for the submit button, since it can only be used in the initialize portion of the controller code.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    System.out.println(locale.getDefault());
    Instant instant = Instant.now();    
         try {
      File myObj = new File("login_activity.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }   
    
    submit.setOnAction((event) -> {
    database wgu = new database();
    wgu.connect();
    if(wgu.login(user.getText(),password.getText()) == true)
        {
           try {
                FileWriter myWriter = new FileWriter("login_activity.txt",true);
                BufferedWriter bw = new BufferedWriter(myWriter);
                bw.newLine();
                bw.write("Succesful Login by " + user.getText() + " on " +Instant.now().toString()  );
                bw.close();
                myWriter.close();
                } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                   }
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        }
    
    else if(wgu.login(user.getText(),password.getText())== false && Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("fr_CA")){
      System.out.println("Nom d'utilisateur ou mot de passe incorrect");
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Erreur d'identification");
      alert.setHeaderText(null);
      alert.setContentText("Erreur de connexion, utilisateur ou mot de passe incorrect");
      alert.showAndWait();
      try {
      FileWriter myWriter = new FileWriter("login_activity.txt",true);
      BufferedWriter bw = new BufferedWriter(myWriter);
      bw.newLine();
      bw.write("Failed Login by " + user.getText() + " on " +Instant.now().toString());
      bw.close();
      myWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }
    
    else 
        {
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
           alert.setTitle("Login error");
           alert.setHeaderText(null);
           alert.setContentText("Login error, incorrect user or password");
           alert.showAndWait();
           try {
                FileWriter myWriter = new FileWriter("login_activity.txt",true);
                BufferedWriter bw = new BufferedWriter(myWriter);
                bw.newLine();
                bw.write("Failed Login by " + user.getText() + " on " +Instant.now().toString());
                bw.close();
                myWriter.close();
                } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                   }
            Stage stage = (Stage) submit.getScene().getWindow();
            stage.close();
        }
    });
    location.setText(z.getId());
    Language.getItems().add(0,"English");
    Language.getItems().add(1,"French");
    if(Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("fr_CA")){
       Language.getSelectionModel().select(1);
       loadLang("fr");
    }
    else
        Language.getSelectionModel().select(0);
    
    }    
    
    /**
     *  This method loads the resource bundle for FR settings.
     * @param lang used to identify the appropriate language needed 
     */
    private void loadLang(String lang){
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("software.lang",locale);
        password.setPromptText(bundle.getString("password"));
        user.setPromptText(bundle.getString("user"));
        submit.setText(bundle.getString("submit"));
        
    }   
}
