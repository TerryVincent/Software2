/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.TablePosition;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author Vishnu
 */
public class ModifyAppointmentController implements Initializable {

    @FXML private TableView<ObservableList> table = new TableView<ObservableList>();
    @FXML private TextField Appointment;
    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Location;
    @FXML private ComboBox Contact;
    @FXML private TextField Type;
    @FXML private TextField SDT;
    @FXML private TextField EDT;
    @FXML private TextField CID;
    @FXML private TextField User;
    @FXML private database wgu = new database();
    @FXML private TableColumn Appointment_ID;
    @FXML private TableColumn title; 
    @FXML private Button Save;
    @FXML private Button Delete;
    @FXML private ObservableList<String> Contacts;
    @FXML private ObservableList<ObservableList> data =  FXCollections.observableArrayList();
    @FXML private int FC ;
    @FXML private String user ;
    @FXML private String Tcheck1;
    @FXML private String Tcheck2;
    /**
     * 
     * @param u
     */
    public ModifyAppointmentController(String u){user = u;}

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
     wgu.connect();
     ObservableList<String> Contacts = FXCollections.observableArrayList("Anika Costa","Daniel Garcia","Li Lee");
     Contact.setItems(Contacts);
     ResultSet rs = wgu.appData();
     Appointment_ID.setCellValueFactory(new ObservableListCallBack(0));           
     title.setCellValueFactory(new ObservableListCallBack(1));
     try {
      data =  FXCollections.observableArrayList();
      while (rs.next()) {
               
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    
                    row.add(rs.getString(i));
                }
   
                data.add(row);
                System.out.println("Row  [1] added " + row);
                
    }    
      table.getItems().addAll(data);
     }catch(Exception e){System.out.println(e);}
    ////////////
    ///////////
    String DATE_FORMAT = "yyyy-MM-d HH:mm:ss";
    table.getSelectionModel().setCellSelectionEnabled(true);
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        FC =  Integer.parseInt(table.getSelectionModel().getSelectedItem().get(0).toString());
        String data = "SELECT Appointment_ID,Title,Description,Location,Contact_ID,Type,Start, End, Customer_ID,User_ID from appointments WHERE Appointment_ID ='"+FC+"'";
        try {
          ResultSet rss = wgu.conn().createStatement().executeQuery(data);
          while (rss.next()){
            Appointment.setText(rss.getString(1));
            Title.setText(rss.getString(2));
            Description.setText(rss.getString(3));
            Location.setText(rss.getString(4));
            if (rss.getInt(5) == 1)
            {Contact.setValue("Anika Costa");}
            else if (rss.getInt(5) == 2)
            {Contact.setValue("Daniel Garcia");}
            else if (rss.getInt(5) == 3)
            {Contact.setValue("Li Lee");}
            Type.setText(rss.getString(6));
            
            ZoneId zid = ZoneId.of("UTC");
                        ZoneId Default =ZoneId.systemDefault();
                        LocalDateTime ldt = LocalDateTime.parse(rss.getString(7), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        System.out.println(ldt);
                        ZonedDateTime localZonedDateTime = ldt.atZone(zid);
                        System.out.println(localZonedDateTime);
                        localZonedDateTime = localZonedDateTime.withZoneSameInstant(Default);
                        System.out.println(localZonedDateTime);
                        DateTimeFormatter formatter2 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        String format = localZonedDateTime.format(formatter2);
            SDT.setText(format);
            Tcheck1 = format;
            ZoneId zid2 = ZoneId.of("UTC");
                        ZoneId Default2 =ZoneId.systemDefault();
                        LocalDateTime ldt2 = LocalDateTime.parse(rss.getString(8), DateTimeFormatter.ofPattern(DATE_FORMAT));
                        System.out.println(ldt2);
                        ZonedDateTime localZonedDateTime2 = ldt2.atZone(zid2);
                        System.out.println(localZonedDateTime2);
                        localZonedDateTime2 = localZonedDateTime2.withZoneSameInstant(Default2);
                        System.out.println(localZonedDateTime2);
                        DateTimeFormatter formatter3 = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                        String format2 = localZonedDateTime2.format(formatter3);
            EDT.setText(format2);     
            Tcheck2 = format2;
            CID.setText(rss.getString(9));
            User.setText(rss.getString(10));
         }}catch(Exception e){System.out.println(e);}      
     }
     });
    
    }    

    /**
     * Saves data of the appointment to the database
     */
    public void SaveData(){
        boolean fields = true;
        String DATE_FORMAT = "yyyy-MM-d HH:mm:ss";
        String DATE_FORMAT2 = "yyyy-MM-d HH:mm:ss";
        if(EDT.getText().equals(Tcheck2) && SDT.getText().equals(Tcheck1))
        {
        try{
         PreparedStatement sanitized = wgu.conn().prepareStatement("UPDATE appointments SET Appointment_ID = ?,Title = ?, Description = ?, Location = ?,Created_By = ?, type= ?,Customer_ID = ?, User_ID = ?,Contact_ID = ? WHERE Appointment_ID ="+FC);
        if( Appointment.getText().isEmpty() != true && Appointment.getText() !=null   )sanitized.setString(1,Appointment.getText());
        else fields = false;
        
        if( Title.getText().isEmpty() != true && Title.getText() !=null   )sanitized.setString(2,Title.getText());
        else fields = false;
        
        if( Description.getText().isEmpty() != true && Description.getText() !=null  )sanitized.setString(3,Description.getText());
        else fields = false;
        
        if( Location.getText().isEmpty() != true && Location.getText() !=null )sanitized.setString(4,Location.getText()); 
        else fields = false;
        
        if( user !=null )sanitized.setString(5,user); 
        else fields = false;
        
        if( Type.getText().isEmpty() != true && Type.getText() !=null)
        {
            if (Type.getText().equals("Initial"))
                sanitized.setString(6,Type.getText());
            else if (Type.getText().equals("Follow up"))
                sanitized.setString(6,Type.getText()); 
            else if (Type.getText().equals("Final"))
                sanitized.setString(6,Type.getText()); 
            else 
            {   
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Incorrect Type");
                alert.setHeaderText(null);
                alert.setContentText("The type must be Initial,Follow Up, Or Final");
                alert.showAndWait();
                fields = false;
            }
        }
        else fields = false;
        if( CID.getText().isEmpty() != true && CID.getText() !=null )sanitized.setString(7,CID.getText()); 
        else fields = false;
        
        if( User.getText().isEmpty() != true && User.getText() !=null )sanitized.setString(8,User.getText()); 
        else fields = false;   
        
        if(fields == false) {System.out.println("Oops you forgot a field!");}
        if(fields == true) {
            if(Contact.getValue()=="Anika Costa")
              sanitized.setInt(9,1);
            else if(Contact.getValue()=="Daniel Garcia")
                sanitized.setInt(9,2);
            else if(Contact.getValue()=="Li Lee")
                sanitized.setInt(9,3);
            System.out.println(FC);
            sanitized.executeUpdate();
            Stage stage = (Stage) Save.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            try{ 
        software.CalendarController controller2 = new software.CalendarController(user);
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("Calendarview.fxml"));
        loader2.setController(controller2);
        
        Pane vbox2 = loader2.load();  
        Scene scene2 = new Scene(vbox2);
        primaryStage.setScene(scene2);
        primaryStage.show();
        } catch (Exception e){System.out.println (e);}
        }
        }catch(SQLException e){System.out.println(e);}
         }
    else{    
        try{
        PreparedStatement sanitized = wgu.conn().prepareStatement("UPDATE appointments SET Appointment_ID = ?,Title = ?, Description = ?, Location = ?,Created_By = ?, type= ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?,Contact_ID = ? WHERE Appointment_ID ="+FC);
  
        if( Appointment.getText().isEmpty() != true && Appointment.getText() !=null   )sanitized.setString(1,Appointment.getText());
        else fields = false;
        
        if( Title.getText().isEmpty() != true && Title.getText() !=null   )sanitized.setString(2,Title.getText());
        else fields = false;
        
        if( Description.getText().isEmpty() != true && Description.getText() !=null  )sanitized.setString(3,Description.getText());
        else fields = false;
        
        if( Location.getText().isEmpty() != true && Location.getText() !=null )sanitized.setString(4,Location.getText()); 
        else fields = false;
        
        if( user !=null )sanitized.setString(5,user); 
        else fields = false;
        
        if( Type.getText().isEmpty() != true && Type.getText() !=null)
        {
            if (Type.getText().equals("Initial"))
                sanitized.setString(6,Type.getText());
            else if (Type.getText().equals("Follow up"))
                sanitized.setString(6,Type.getText()); 
            else if (Type.getText().equals("Final"))
                sanitized.setString(6,Type.getText()); 
            else 
            {   
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Incorrect Type");
                alert.setHeaderText(null);
                alert.setContentText("The type must be Initial,Follow Up, Or Final");
                alert.showAndWait();
                fields = false;
            }
        }
        else fields = false;
        
        if( SDT.getText().isEmpty() != true && SDT.getText() !=null ){
            ZoneId Default =ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.parse(SDT.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ZonedDateTime zdt = ldt.atZone(Default);
            zdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp time = Timestamp.valueOf(zdt.toLocalDateTime());
            System.out.println(time);
            sanitized.setString(7,time.toString());
        } 
        else fields = false;
        
        if( EDT.getText().isEmpty() != true && EDT.getText() !=null ){
            ZoneId Default =ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.parse(EDT.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ZonedDateTime zdt = ldt.atZone(Default);
            zdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp time = Timestamp.valueOf(zdt.toLocalDateTime());
            System.out.println(time);
            sanitized.setString(8,time.toString());
        } 
        else fields = false;
        
        if( CID.getText().isEmpty() != true && CID.getText() !=null )sanitized.setString(9,CID.getText()); 
        else fields = false;
        
        if( User.getText().isEmpty() != true && User.getText() !=null )sanitized.setString(10,User.getText()); 
        else fields = false;
        
        if(fields == true)
        {
            ZoneId Default =ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.parse(SDT.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ZonedDateTime zdt = ldt.atZone(Default);
            ZoneId Default2 =ZoneId.systemDefault();
            LocalDateTime ldt2 = LocalDateTime.parse(EDT.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            ZonedDateTime zdt2 = ldt2.atZone(Default2);
            
            if (ldt.getDayOfWeek()== ldt.getDayOfWeek().SATURDAY||ldt.getDayOfWeek()== ldt.getDayOfWeek().SUNDAY )  
             {
                System.out.println("Start date can't be on a Saturday or Sunday");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Weekend");
                alert.setHeaderText(null);
                alert.setContentText("Start date can't be on a Saturday or Sunday");
                alert.showAndWait();
                fields = false;
              }
            if(ldt.atZone(ZoneId.of("America/New_York")).getHour()<8|| ldt.atZone(ZoneId.of("America/New_York")).getHour()>=22)
            {   
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Business hours only");
                alert.setHeaderText(null);
                alert.setContentText("between 8am and 10pm est only.");
                alert.showAndWait();
                fields = false;   
            }
            
            if(ldt2.atZone(ZoneId.of("America/New_York")).getHour()<8|| ldt2.atZone(ZoneId.of("America/New_York")).getHour()>=22)
            {   
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Business hours only");
                alert.setHeaderText(null);
                alert.setContentText("between 8am and 10pm est only.");
                alert.showAndWait();
                fields = false;   
            }
            
            if(wgu.Hdata(SDT.getText()).next() == true)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Meeting overlap");
                alert.setHeaderText(null);
                alert.setContentText("You already have a meeting at that time!");
                alert.showAndWait();  
                fields = false;     
            }
            
            if(wgu.Hdata2(EDT.getText()).next() == true)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Meeting overlap");
                alert.setHeaderText(null);
                alert.setContentText("You already have a meeting at that time!!");
                alert.showAndWait();  
                fields = false;     
            }
            
            if (wgu.Hdata3(1).next() == true||wgu.Hdata3(2).next() == true||wgu.Hdata3(3).next() == true)
            {   
                ResultSet rs = wgu.appData();
                if(Contact.getValue()=="Anika Costa")
                rs = wgu.Hdata3(1);
                if(Contact.getValue()=="Daniel Garcia")
                rs = wgu.Hdata3(2);
                if(Contact.getValue()=="Li Lee")
                rs = wgu.Hdata3(3);
                while (rs.next())
                {
                    LocalDateTime StartDT = null;
                    LocalDateTime EndDT = null;
                    ZonedDateTime edt = null;
                    ZonedDateTime sdt = null;
                    ZonedDateTime ZonedStartDT = null;
                    ZonedDateTime ZonedEndDT = null;
                   for (int i = 1; i < rs.getMetaData().getColumnCount(); i++)
                   {
                     ZoneId zid = ZoneId.of("UTC");
                     ZoneId Default3 =ZoneId.systemDefault();
                      StartDT = LocalDateTime.parse(rs.getString(i), DateTimeFormatter.ofPattern(DATE_FORMAT));
                      ZonedStartDT = StartDT.atZone(zid);
                      ZonedStartDT = ZonedStartDT.withZoneSameInstant(Default3);
                      EndDT = LocalDateTime.parse(rs.getString(i+1), DateTimeFormatter.ofPattern(DATE_FORMAT));
                      ZonedEndDT = EndDT.atZone(zid);
                      ZonedEndDT = ZonedEndDT.withZoneSameInstant(Default3);
                      System.out.println(ZonedStartDT+" " + zdt);
                      System.out.println(ZonedEndDT+" " + zdt);
                   }
                   if (zdt.isAfter(ZonedStartDT) && zdt.isBefore(ZonedEndDT))
                   {
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Meeting overlap");
                       alert.setHeaderText(null);
                       alert.setContentText("You already have a meeting at that time!!!!!");
                       alert.showAndWait();  
                       fields = false;  
                   
                   }
                    if(zdt.equals(ZonedStartDT))
                   {
                       System.out.println(ldt+" " + StartDT);
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Meeting overlap");
                       alert.setHeaderText(null);
                       alert.setContentText("You already have a meeting at that time!!?!?!?!!!");
                       alert.showAndWait();  
                       fields = false;  
                   }
                    if (ldt2.isAfter(StartDT) && ldt2.isBefore(EndDT))
                   {
                       System.out.println(ldt2+" " + StartDT);
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Meeting overlap");
                       alert.setHeaderText(null);
                       alert.setContentText("You already have a meeting at that time!!!!!");
                       alert.showAndWait();  
                       fields = false;  
                   
                   }
                    if(ldt2.equals(StartDT))
                   {
                       System.out.println(ldt2+" " + StartDT);
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Meeting overlap");
                       alert.setHeaderText(null);
                       alert.setContentText("You already have a meeting at that time!!?!?!?!!!");
                       alert.showAndWait();  
                       fields = false;  
                   }
                }
         
            }  
      
        }
        
        if(fields == false) {System.out.println("Oops you forgot a field!");}
        
        if(fields == true) {
            if(Contact.getValue()=="Anika Costa")
              sanitized.setInt(11,1);
            else if(Contact.getValue()=="Daniel Garcia")
                sanitized.setInt(11,2);
            else if(Contact.getValue()=="Li Lee")
                sanitized.setInt(11,3);
            System.out.println(FC);
            sanitized.executeUpdate();
            Stage stage = (Stage) Save.getScene().getWindow();
            stage.close();
            Stage primaryStage = new Stage();
            try{ 
        software.CalendarController controller2 = new software.CalendarController(user);
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("Calendarview.fxml"));
        loader2.setController(controller2);
        
        Pane vbox2 = loader2.load();  
        Scene scene2 = new Scene(vbox2);
        primaryStage.setScene(scene2);
        primaryStage.show();
        } catch (Exception e){System.out.println (e);}
        }
        }catch(SQLException e){System.out.println(e);}
        
        }
    }
    /**
     * Deletes the appointment form the selected item from the table view. And then closes the current window and moves back to the previous scene.
     */
    public void DeleteData(){
    TablePosition pos = table.getSelectionModel().getSelectedCells().get(0);
        //String info = pos.toString().split(",")[0].substring(1);
        int row = pos.getRow();
        // Item here is the table view type:
         ObservableList<String> item = table.getItems().get(row);
         
         TableColumn col = pos.getTableColumn();
        
        // this gives the value in the selected cell:
        String info = (String) col.getCellObservableValue(item).getValue();
        
        
         try{
        PreparedStatement sanitized = wgu.conn().prepareStatement("DELETE FROM appointments WHERE Appointment_ID = ?" );
        sanitized.setString(1,info);
        System.out.println(sanitized);
        sanitized.executeUpdate();
 
        }catch(SQLException e){System.out.println(e);}
         
        wgu.connect();
        ResultSet rs =  wgu.CustomerData();
        table.getItems().clear();
        ObservableList<ObservableList> dataD =  FXCollections.observableArrayList();
        try {
        while (rs.next()) {
                //Iterate Row
                ObservableList<String> rows = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    rows.add(rs.getString(i));
                }
   
                dataD.add(rows);
                
    }    
      table.getItems().addAll(dataD);
     
     }catch(Exception e){System.out.println(e);} 
        
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Deletion");
    alert.setHeaderText(null);
    alert.setContentText("You've just deleted  appointment id "+Appointment.getText()+" type "+Type.getText());

    alert.showAndWait();
    Stage stage = (Stage) Delete.getScene().getWindow();
            stage.close();
    Stage primaryStage = new Stage();    
        try{ 
        software.CalendarController controller = new software.CalendarController(user);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Calendarview.fxml"));
        loader.setController(controller);
        
        Pane vbox = loader.load();  
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
        } catch (Exception e){System.out.println (e);}
    
    
    }
     
    
}
