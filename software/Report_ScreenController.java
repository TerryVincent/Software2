/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import javafx.beans.value.*; 
import javafx.event.ActionEvent; 
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Vishnu
 */
public class Report_ScreenController implements Initializable {
    @FXML private TableView<ObservableList> table = new TableView<ObservableList>();
    @FXML private TableView<ObservableList>  CTable = new TableView<ObservableList>();
    @FXML private TableColumn Appointment_ID; 
    @FXML private TableColumn Title;
    @FXML private TableColumn Description;
    @FXML private TableColumn Location;
    @FXML private TableColumn Contact;
    @FXML private TableColumn Type;
    @FXML private TableColumn Start_Date_and_Time;
    @FXML private TableColumn End_Date_and_Time;
    @FXML private TableColumn Customer_ID;
    @FXML private TableColumn CustomerID;
    @FXML private TableColumn CustomerName;
    @FXML private TextArea numbers;
    @FXML private DatePicker DatePicker;
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();
    private String user;
    private String password;
    private Connection conn = null;
    private int month = 0;
    String DATE_FORMAT = "yyyy-MM-d HH:mm:ss";
    
    
    /**
     * .
     * @param U saves the user data in the constructor
     */
    public Report_ScreenController(String U){user = U;}

    /**
     * Initializes the controller class
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
     database wgu = new database();
     wgu.connect();
     ResultSet rs = wgu.appData();
     
    Appointment_ID.setCellValueFactory(new ObservableListCallBack(0));           
    Title.setCellValueFactory(new ObservableListCallBack(1));
    Description.setCellValueFactory(new ObservableListCallBack(2));
    Location.setCellValueFactory(new ObservableListCallBack(3));
    Contact.setCellValueFactory(new ObservableListCallBack(4));
    Type.setCellValueFactory(new ObservableListCallBack(5));
    Start_Date_and_Time.setCellValueFactory(new ObservableListCallBack(6));
    End_Date_and_Time.setCellValueFactory(new ObservableListCallBack(7));
    Customer_ID.setCellValueFactory(new ObservableListCallBack(8));
     try {
      while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(rs.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z ");
                        String format = localZonedDateTime.format(formatter2);
                        row.add(format);
                        
                    }
                    else
                    row.add(rs.getString(i));
                }
               
                data.add(row);
                
                
    }
      table.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}
    //////////////////////////////////////////
    /////Customer Table///////////////////////
    //////////////////////////////////////////
    
    CustomerID.setCellValueFactory(new ObservableListCallBack(0));           
    CustomerName.setCellValueFactory(new ObservableListCallBack(1));
    rs = wgu.Customer();
    try {
      while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.println(rs.getString(i));
                    row.add(rs.getString(i));
                }
                data.add(row);
    
    }
      CTable.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}
    }    
    
    /**
     * Posts all the appointments in the database to the table view.
     */
    public void Appointments(){
    table.getItems().clear();
     database wgu = new database();
     wgu.connect();
     ResultSet rs = wgu.appData();
     try {
      while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(rs.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z ");
                        String format = localZonedDateTime.format(formatter2);
                        row.add(format);
                        
                    }
                    else
                    row.add(rs.getString(i));
                }
               
                data.add(row);
                
                
    }
      table.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}
    
    
}  

    /**
     * posts all of Anikas appointments in the database if there are any
     */
    public void Anika(){
table.getItems().clear();
database wgu = new database();
wgu.connect();
ResultSet A = wgu.CData(1);
try {
      while (A.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= A.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(A.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z ");
                        String format = localZonedDateTime.format(formatter2);
                        row.add(format);
                        
                    }
                    else
                    row.add(A.getString(i));
                }
               
                data.add(row);
                
                
    }
      table.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}


}

    /**
     * posts all of Daniels appointments in the database if there are any
     */
    public void Daniel(){
table.getItems().clear();
database wgu = new database();
wgu.connect();
ResultSet A = wgu.CData(2);
try {
      while (A.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= A.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(A.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z ");
                        String format = localZonedDateTime.format(formatter2);
                        row.add(format);
                        
                    }
                    else
                    row.add(A.getString(i));
                }
               
                data.add(row);
                
                
    }
      table.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}


}

    /**
     * posts all of Lee li  appointments in the database if there are any
     */
    public void Li(){
table.getItems().clear();
database wgu = new database();
wgu.connect();
ResultSet A = wgu.CData(3);
try {
      while (A.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= A.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(A.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z ");
                        String format = localZonedDateTime.format(formatter2);
                        row.add(format);
                        
                    }
                    else
                    row.add(A.getString(i));
                }
               
                data.add(row);
                
                
    }
      table.getItems().addAll(data);
      data.clear();
     }catch(Exception e){System.out.println(e);}


}
 public void Initial(){
 database wgu = new database();
 wgu.connect();
 month = DatePicker.getValue().getMonthValue();
 ResultSet A = wgu.Initial(month);
 int count = 0;
 try{
 while (A.next()){ count++;}
 }catch(Exception e){System.out.println(e);}
 numbers.setText("There are "+count+" appointments");
 System.out.println(count);
 }
 public void FollowUp(){
 database wgu = new database();
 wgu.connect();
 ResultSet A = wgu.FollowUp(DatePicker.getValue().getMonthValue());
 int count = 0;
 try{
 while (A.next()){ count++;}
 }catch(Exception e){System.out.println(e);}
 numbers.setText("There are "+count+" appointments");
 System.out.println(count);
 }
 public void Final(){
 database wgu = new database();
 wgu.connect();
 ResultSet A = wgu.Final(DatePicker.getValue().getMonthValue());
 int count = 0;
 try{
 while (A.next()){ count++;}
 }catch(Exception e){System.out.println(e);}
 numbers.setText("There are "+count+" appointments");
 System.out.println(count);
 }

}
