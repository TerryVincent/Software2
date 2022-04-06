/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;

/**
 *
 * @author Vishnu
 */
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import javafx.beans.value.*; 
import javafx.event.ActionEvent; 
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.*;

/**
 *
 * @author Vishnu
 */
public class CalendarController implements Initializable {
    @FXML private TableView<ObservableList> table = new TableView<ObservableList>();
    @FXML private TableColumn Appointment_ID; 
    @FXML private TableColumn Title;
    @FXML private TableColumn Description;
    @FXML private TableColumn Location;
    @FXML private TableColumn Contact;
    @FXML private TableColumn Type;
    @FXML private TableColumn Start_Date_and_Time;
    @FXML private TableColumn End_Date_and_Time;
    @FXML private TableColumn Customer_ID;
    @FXML private Button add_Customer;
    @FXML private Button Modify_Customer;
    @FXML private Button add_Appointment;
    @FXML private Button Modify_Appointment;
    @FXML private Button Report_Screen;
    @FXML private DatePicker DatePicker; 
    @FXML private RadioButton Month ;
    @FXML private RadioButton Week ;
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();
    private String user;
    private String password;
    private Connection conn = null;

    /**
     *
     * @param u Constructor used to save the user data.
     */
    public CalendarController(String u){
        user = u;
    }
    
    /**
     * Initializes the controller by setting up the date picker along with a schedule of all appointments in the table view.
     * LAMBDA is used for event handling for the report button, since it can only be used in the initialize portion of the controller code.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle)  {
     
        
     ToggleGroup group = new ToggleGroup();
     Month.setToggleGroup(group);
     Week.setToggleGroup(group); 
     database wgu = new database();
     wgu.connect();
     ResultSet rs = wgu.appData();
     String DATE_FORMAT = "yyyy-MM-d HH:mm:ss";
    
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
                        System.out.println(localZonedDateTime);
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
      appointmentCheck();
     }catch(Exception e){System.out.println(e);}
     
    ////////////////////////////////////
    //Datepicker////////////////////////
    ////////////////////////////////////
    
     DatePicker.setShowWeekNumbers(true);
     DatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if(date.getDayOfWeek()==DayOfWeek.SATURDAY||date.getDayOfWeek() == DayOfWeek.SUNDAY)
                setDisable(true);
            }
        });
     DatePicker.setEditable(false);
    
     EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
            
            public void handle(ActionEvent e) 
            {   
                
                ResultSet rs = wgu.MData(DatePicker.getValue().getMonthValue());
                try {
                while (rs.next()) {
                //Iterate Row
                ObservableList<String> row2 = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(rs.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z");
                        String format = localZonedDateTime.format(formatter2);
                        row2.add(format);
                        
                    }
                    else
                    row2.add(rs.getString(i));
                }
                data.add(row2);
                
    }
      table.getItems().clear();
      table.refresh();
      table.getItems().addAll(data);
      data.clear();
      table.refresh();
     }catch(Exception s){System.out.println(s);}
         }};  
     
     
     EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() { 
           
            public void handle(ActionEvent e) 
        {       
                System.out.println(DatePicker.getValue());
                LocalDate date = DatePicker.getValue(); 
                int weekOfYear = date.get(WeekFields.ISO.weekOfYear());
                ResultSet rs = wgu.WData(weekOfYear);
                try {
                while (rs.next()) {
                //Iterate Row
                ObservableList<String> row2 = FXCollections.observableArrayList();
                
                
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 7 || i == 8){
                        ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(rs.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a z");
                        String format = localZonedDateTime.format(formatter2);
                        row2.add(format);
                     
                    }
                    else
                    row2.add(rs.getString(i));
                }
                
                data.add(row2);
                
    }
      table.getItems().clear();
      table.refresh();
      table.getItems().addAll(data);
      data.clear();
      table.refresh();
     }catch(Exception s){System.out.println(s);}
         }};

     group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
        { 
            public void changed(ObservableValue<? extends Toggle> ob,  
                                                    Toggle o, Toggle n) 
            { 
  
                RadioButton rb = (RadioButton)group.getSelectedToggle(); 
  
                if (rb == Month) { 
                    DatePicker.setOnAction(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Month");
                alert.setHeaderText(null);
                alert.setContentText("Select a month in the datepicker");
                alert.showAndWait(); 
                }
                else if (rb == Week){
                    DatePicker.setOnAction(event2);
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("week");
                alert.setHeaderText(null);
                alert.setContentText("Select a week in the datepicker");
                alert.showAndWait(); 
                }
            } 
        }); 
     
    
     
     ///////////////////////////////////
     //////////////////////////////////
     Report_Screen.setOnAction((event3)->{
         
        Stage primaryStage = new Stage();
        software.Report_ScreenController controller = new software.Report_ScreenController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Report_Screen.fxml"));
        loader.setController(controller);
        try{
        Pane vbox = loader.load();
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();}catch (Exception e){} 
     
        
     });
     
    }
   
    /**
     * moves to the AddCustomer screen
     * @throws Exception
     */
    public void AddCustomer() throws Exception {
        Stage primaryStage = new Stage();
        software.AddCustomerController controller = new software.AddCustomerController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add_customer.fxml"));
        loader.setController(controller);
        Pane vbox = loader.load();  
        //software.LoginController controller = new software.LoginController();
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
        Stage stage = (Stage) add_Customer.getScene().getWindow();
        stage.close();
        

    }
    
    /**
     * Moves to the modify customer Screen
     * @throws Exception
     */
    public void ModifyCustomer() throws Exception {
        Stage primaryStage = new Stage();
        software.Modify_CustomerController controller = new software.Modify_CustomerController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Modify_customer.fxml"));
        loader.setController(controller);
        Pane vbox = loader.load();  
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
        Stage stage = (Stage) Modify_Customer.getScene().getWindow();
        stage.close();

    }
   
    /**
     * Moves to the AddAppointment Screen
     * @throws Exception
     */
    public void AddAppointment() throws Exception{
        Stage primaryStage = new Stage();
        software.AddAppointmentController controller = new software.AddAppointmentController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Add appointment.fxml"));
        loader.setController(controller);
        Pane vbox = loader.load();  
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
        Stage stage = (Stage) add_Appointment.getScene().getWindow();
        stage.close();
   }
   
    /**
     * Moves to the ModifyAppointment Screen
     * @throws Exception
     */
    public void ModifyAppointment() throws Exception{
        Stage primaryStage = new Stage();
        software.ModifyAppointmentController controller = new software.ModifyAppointmentController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Modify appointment.fxml"));
        loader.setController(controller);
        Pane vbox = loader.load();  
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
        Stage stage = (Stage) Modify_Appointment.getScene().getWindow();
        stage.close();
   }
   
    /**
     * Checks all of the appointments against todays date and time to see if there is an appointment in 15 minutes.
     */
    public void appointmentCheck(){
       String DATE_FORMAT = "yyyy-MM-dd HH:mm";
       String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
       ZonedDateTime zdt = ZonedDateTime.now();
       ZoneId zid = ZoneId.of("UTC");
       zdt = zdt.withZoneSameInstant(zid);
       database wgu = new database();
       wgu.connect();
       ResultSet rs = null;
       boolean appointment = false;
       System.out.println(zdt);
       for(int i = 0;i < 15; i++)
       {
           try{
           if(wgu.Hdata(zdt.plusMinutes(i).format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString()).next() == true)
           {
               rs = wgu.Hdata(zdt.plusMinutes(i).format(DateTimeFormatter.ofPattern(DATE_FORMAT)).toString());
               appointment = true;
           }
           
           }catch(SQLException e){
            System.out.println(e);
        }
           
       }
       if (appointment == false){
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nothing");
                alert.setHeaderText(null);
                alert.setContentText("You have no upcomming appointments");
                alert.showAndWait();         
       }
       if (appointment == true){
           Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Schedule");
                alert.setHeaderText(null);
                try{
                while (rs.next()){
                int aid = rs.getInt(1);
                LocalDateTime sdt = LocalDateTime.parse(rs.getString(2), DateTimeFormatter.ofPattern(DATE_FORMAT2));
                ZoneId zid2 = ZoneId.systemDefault();
                ZonedDateTime sdt2 = sdt.atZone(zid);
                sdt2 = sdt2.withZoneSameInstant(zid2);
                alert.setContentText("Appointment upcoming!The appointment ID is "+aid+ " and the time is " +sdt2);
                
                }
                
                 }catch(SQLException e){ System.out.println(e); }
                alert.showAndWait();         
       }
       
   }
    
}
