/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software;

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
public class Modify_CustomerController implements Initializable {

    @FXML private TableView<ObservableList> table = new TableView<ObservableList>();
    @FXML private TextField CustomerName;
    @FXML private TextField Address;
    @FXML private TextField PostalCode;
    @FXML private TextField PhoneNumber;
    @FXML private TextField CustomerId;
    @FXML private ComboBox Division;
    @FXML private ComboBox CountryData;
    @FXML private Button Save;
    @FXML private Button Delete;
    @FXML private TableColumn Customer_ID;
    @FXML private TableColumn Customer_Name;       
    @FXML private int divisionId;
    @FXML private String country;
    @FXML private String division;
    @FXML private  database wgu = new database();
    @FXML private ObservableList<String> Countries;
    @FXML private ObservableList<String> StateDivisions;
    @FXML private ObservableList<String> KingdomDivisions;        
    @FXML private ObservableList<String> CanadaDivisions;
    @FXML private ObservableList<ObservableList> data =  FXCollections.observableArrayList();
    @FXML private int FC ;
    private String user ;

    /**
     * Constructor
     * @param u used to save the user data
     */
    public Modify_CustomerController(String u){user = u;}
   
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
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
    ResultSet rs = wgu.CustomerData();
    Customer_ID.setCellValueFactory(new ObservableListCallBack(0));           
    Customer_Name.setCellValueFactory(new ObservableListCallBack(1));
 
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
     
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////
   ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    table.getSelectionModel().setCellSelectionEnabled(true);
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        FC =  Integer.parseInt(table.getSelectionModel().getSelectedItem().get(0).toString());
        //System.out.println(FC);
        String data = "SELECT Customer_ID,Customer_Name,Address,Postal_Code,Phone,Division_ID FROM customers WHERE Customer_ID ='"+FC+"'";
        //System.out.println(data);
        try {
          ResultSet rss = wgu.conn().createStatement().executeQuery(data);
          while (rss.next()){
            CustomerId.setText(rss.getString(1));
            CustomerName.setText(rss.getString(2));
            Address.setText(rss.getString(3));
            PostalCode.setText(rss.getString(4));
            PhoneNumber.setText(rss.getString(5));
            divisionId = rss.getInt(6);
            data = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID ="+rss.getString(6); 
            ResultSet cID = wgu.conn().createStatement().executeQuery(data);
            //System.out.println(data);
            data = "SELECT Division FROM first_level_divisions WHERE Division_ID ="+rss.getString(6);
            ResultSet iD = wgu.conn().createStatement().executeQuery(data);
            //System.out.println(data);
                while(cID.next()&& iD.next()){
                 
                 
                  if(cID.getInt(1)== 1){
                    //  Division.getSelectionModel().select(Div);
                      CountryData.getSelectionModel().select("United States");
                      Division.getSelectionModel().select(iD.getString(1));
                        //System.out.println(iD.getString(1));
                        }
                  else if(cID.getInt(1)==2){
                     // Division.getSelectionModel().select(Div);
                      CountryData.getSelectionModel().select("United Kingdoms");
                      Division.getSelectionModel().select(iD.getString(1));
                      //System.out.println(iD.getString(1));
                        }
                  else if(cID.getInt(1)==3){ 
                     // Division.getSelectionModel().select(Div);
                     CountryData.getSelectionModel().select("Canada");
                      
                      Division.getSelectionModel().select(iD.getString(1));
                        //System.out.println(iD.getString(1));
                        }     
                    }
                 
                
         }}catch(Exception e){System.out.println(e);}
        
     }
     });
    
  }    
    
    /**
     * Saves the data of the customer
     */
    public void SaveData(){
        boolean fields = true;
        String data = "SELECT Division_ID from first_level_divisions where Division ='"+division+"'";
        
        try{
        ResultSet Id= wgu.conn().createStatement().executeQuery(data);
        while(Id.next()){divisionId = Id.getInt(1);}    
        PreparedStatement sanitized = wgu.conn().prepareStatement("UPDATE customers SET Customer_Name = ?,Address = ?, Postal_Code = ?, Phone = ?,Last_Updated_By = ?, Division_ID = "+divisionId+" WHERE Customer_ID = "+FC);
  
        if( CustomerName.getText().isEmpty() != true && Address.getText() !=null   )sanitized.setString(1,CustomerName.getText());
        else fields = false;
        
        if( Address.getText().isEmpty() != true && Address.getText() !=null  )sanitized.setString(2,Address.getText());
        else fields = false;
        
        if( PostalCode.getText().isEmpty() != true && PostalCode.getText() !=null )sanitized.setString(3,PostalCode.getText()); 
        else fields = false;
        
        if( PhoneNumber.getText().isEmpty() != true && PhoneNumber.getText() !=null )sanitized.setString(4,PhoneNumber.getText()); 
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
     * Used to identify the country
     */
    @FXML
    public void Country() {
       
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
     * used to identify the division of the country
     */
    @FXML
    public void div() {
            country = (String) CountryData.getValue();
            division = (String)Division.getValue();
    }
    
    /**
     * used to delete the customer from the database
     */
    @FXML
    public void Delete() {
        TablePosition pos = table.getSelectionModel().getSelectedCells().get(0);
        //String info = pos.toString().split(",")[0].substring(1);
        int row = pos.getRow();
        // Item here is the table view type:
         ObservableList<String> item = table.getItems().get(row);
         
         TableColumn col = pos.getTableColumn();
        
        // this gives the value in the selected cell:
        String info = (String) col.getCellObservableValue(item).getValue();
        
        
         try{
        PreparedStatement sanitized = wgu.conn().prepareStatement("DELETE FROM appointments WHERE Customer_ID = ?" );
        sanitized.setString(1,info);
        System.out.println(sanitized);
        sanitized.executeUpdate();
        sanitized = wgu.conn().prepareStatement("DELETE FROM customers WHERE Customer_ID = ?" );
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
    alert.setContentText("You've just deleted a customer");

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
