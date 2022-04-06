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
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class AddCustomerController implements Initializable {

    @FXML private TextField CustomerName;
    @FXML private TextField Address;
    @FXML private TextField PostalCode;
    @FXML private TextField PhoneNumber;
    @FXML private ComboBox Division;
    @FXML private ComboBox CountryData;
    @FXML private Button Save;
    private String country;
    private String division;
    private int divisionId;
    private String user ;
    private database wgu = new database();
    private ObservableList<String> Countries;
    private ObservableList<String> StateDivisions;
    private ObservableList<String> KingdomDivisions;        
    private ObservableList<String> CanadaDivisions;
    
    
    public AddCustomerController(String u){user = u;}
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
     System.out.println(user);
     Countries = FXCollections.observableArrayList(
       "United States","Canada","United Kingdoms");
     
     StateDivisions = FXCollections.observableArrayList(
        "Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
        "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts",
        "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
        "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico",
        "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma",
        "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
        "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
        "Wisconsin", "Wyoming");
     
     KingdomDivisions = FXCollections.observableArrayList(
        "England", "Scotland", "Wales","Northern Ireland");
     
     CanadaDivisions = FXCollections.observableArrayList(
         "Alberta", "British Columbia","Manitoba","New Brunswick","Newfoundland and Labrador",
         "Northwest Territories","Nova Scotia","Nunavut","Ontario","Prince Edward Island",
         "QuÃ©bec","Saskatchewan","Yukon");
     CountryData.setItems(Countries);
     wgu.connect();
    }
    /**
     * Saves data from the information in the given fields.
     */
    public void SaveData(){
        boolean fields = true;
        String data = "SELECT Division_ID from first_level_divisions where Division ='"+division+"'";
        try{
        ResultSet Id= wgu.conn().createStatement().executeQuery(data);
        while(Id.next()){divisionId = Id.getInt(1);}
        PreparedStatement sanitized = wgu.conn().prepareStatement("INSERT INTO customers(Customer_Name,Address,Postal_Code,Phone,Create_date,Created_by,Last_Update,Last_Updated_By,Division_ID) VALUES(?, ?, ?, ?, NOW(),?, NOW(), 'script',"+divisionId+");" );
        
        if( CustomerName.getText().isEmpty() != true && CustomerName.getText() !=null)sanitized.setString(1,CustomerName.getText());
        else fields = false;
        
        if( Address.getText().isEmpty() != true && Address.getText() !=null)sanitized.setString(2,Address.getText());
        else fields = false;
        
        if( PostalCode.getText().isEmpty() != true && PostalCode.getText() !=null)sanitized.setString(3,PostalCode.getText()); 
        else fields = false;
        
        if( PhoneNumber.getText().isEmpty() != true && PhoneNumber.getText() !=null)sanitized.setString(4,PhoneNumber.getText()); 
        else fields = false;
        
        if(fields == false) {System.out.println("Oops you forgot a field!");}
        
        if(fields == true) {
            sanitized.setString(5,user);
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
    
    /**
     * Checks the combo box for the selected country.
     */
    @FXML
    public void country() {
        if(CountryData.getValue()=="United States"){
            Division.setItems(StateDivisions);
            country = (String) CountryData.getValue();
        }
        
        else if(CountryData.getValue()=="United Kingdoms"){
            Division.setItems(KingdomDivisions);
            country = (String) CountryData.getValue();
        }
        
        else if(CountryData.getValue()=="Canada"){
            Division.setItems(CanadaDivisions);
            country = (String) CountryData.getValue();
        }
        
    }
    /**
     * Checks the division combo box for the selected 
     */
    @FXML
    public void div() {
            country = (String) CountryData.getValue();
            division = (String)Division.getValue();
    }
    

}