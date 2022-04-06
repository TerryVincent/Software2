/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vishnu
 */
public class AddAppointmentController implements Initializable {

    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Location;
    @FXML private TextField Type;
    @FXML private TextField SDT;
    @FXML private TextField EDT;
    @FXML private TextField CID;
    @FXML private TextField UID;
    @FXML private ComboBox Contact;
    @FXML private Button Save;
    private String user ;
    private database wgu = new database();
    private ObservableList<String> Contacts;

    /**
     * Initializes the controller class.
     */
    public AddAppointmentController(String u){user = u;}
    public void initialize(URL url, ResourceBundle rb) {
        wgu.connect();
        ObservableList<String> Contacts = FXCollections.observableArrayList("Anika Costa","Daniel Garcia","Li Lee");
        Contact.setItems(Contacts);
        
    }  
    /**
     * Saves the data from every field. Also does checks against EST office hours.
     */
    public void SaveData(){
       
         boolean fields = true;
         String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
         try{
        
        PreparedStatement sanitized = wgu.conn().prepareStatement("INSERT INTO appointments(Title, Description, Location, Type, Start, End,Create_Date,Created_By,Last_Update,Last_Updated_By,Customer_ID,User_ID,Contact_ID)VALUES(?,?,?,?,?,?,NOW(),'asdfdsaf',NOW(),'NOW()',?,?,?)" );
        
        if( Title.getText().isEmpty() != true && Title.getText() !=null   )sanitized.setString(1,Title.getText());
        else fields = false;
        
        if( Description.getText().isEmpty() != true && Description.getText() !=null  )sanitized.setString(2,Description.getText());
        else fields = false;
        
        if( Location.getText().isEmpty() != true && Location.getText() !=null )sanitized.setString(3,Location.getText()); 
        else fields = false;
        
        if( Type.getText().isEmpty() != true && Type.getText() !=null)
        {
            if (Type.getText().equals("Initial"))
                sanitized.setString(4,Type.getText());
            else if (Type.getText().equals("Follow up"))
                sanitized.setString(4,Type.getText()); 
            else if (Type.getText().equals("Final"))
                sanitized.setString(4,Type.getText()); 
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
            LocalDateTime ldt = LocalDateTime.parse(SDT.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            ZonedDateTime zdt = ldt.atZone(Default);
            zdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp time = Timestamp.valueOf(zdt.toLocalDateTime());
            sanitized.setString(5,time.toString());
        } 
        else fields = false;
        
        if( EDT.getText().isEmpty() != true && EDT.getText() !=null ){
            ZoneId Default =ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.parse(EDT.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            ZonedDateTime zdt = ldt.atZone(Default);
            zdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp time = Timestamp.valueOf(zdt.toLocalDateTime());
            sanitized.setString(6,time.toString());
        } 
        else fields = false;
        
        if( CID.getText().isEmpty() != true && CID.getText() !=null )sanitized.setString(7,CID.getText()); 
        else fields = false;
        
        if( UID.getText().isEmpty() != true && UID.getText() !=null )sanitized.setString(8,UID.getText()); 
        else fields = false;
        
        if(fields == true)
        {
            ZoneId Default =ZoneId.systemDefault();
            LocalDateTime ldt = LocalDateTime.parse(SDT.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT));
            ZonedDateTime zdt = ldt.atZone(Default);
            ZoneId Default2 =ZoneId.systemDefault();
            LocalDateTime ldt2 = LocalDateTime.parse(EDT.getText(), DateTimeFormatter.ofPattern(DATE_FORMAT));
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
                alert.setContentText("You already have a meeting at that time!");
                alert.showAndWait();  
                fields = false;     
            }
            
            if (wgu.Hdata3(1).next() == true||wgu.Hdata3(2).next() == true||wgu.Hdata3(3).next() == true)
            {
                ResultSet rs = wgu.appData();
                if(Contact.getValue()=="Anika Costa"){
                System.out.println("In 1");                  
                rs = wgu.Hdata3(1);
                }
                else if(Contact.getValue()=="Daniel Garcia"){
                rs = wgu.Hdata3(2);
                System.out.println("In 2");
                    
                }
                else if(Contact.getValue()=="Li Lee"){
                System.out.println("In 3");
                rs = wgu.Hdata3(3);
                
                }
                while (rs.next())
                {
                    LocalDateTime StartDT = null;
                    LocalDateTime EndDT = null;
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
                   else if(zdt.equals(ZonedStartDT))
                   {
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
              sanitized.setString(9,"1");
            else if(Contact.getValue()=="Daniel Garcia")
                sanitized.setString(9,"2");
            else if(Contact.getValue()=="Li Lee")
                sanitized.setString(9,"3");
    
            sanitized.executeUpdate();
            Stage stage = (Stage) Save.getScene().getWindow();
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
        }catch(SQLException e){System.out.println(e);}
       
        
        
    }
    
}
