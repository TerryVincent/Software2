/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vishnu
 */
package software;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Vishnu
 */
public class database {
   /* Server name: wgudb.ucertify.com
    Port: 3306
    Database name: WJ06e1d
    Username: U06e1d
    Password: 53688741969*/
    private Connection conn = null;
    private Statement stmt = null;
    private String DBUSER = null;
    private String DBPASS = null;
    private String DB_URL = null;
    database(){
    final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
     DB_URL = "jdbc:mysql://wgudb.ucertify.com/WJ06e1d";
    //  Database credentials
     DBUSER = "U06e1d";
     DBPASS = "53688741969";
    }

    /**
     * Connects to the database
     */
    public void connect(){
     try {
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, DBUSER, DBPASS);
                        System.out.println("Connected");
		}catch (SQLException ex) {
			ex.printStackTrace();

		}
 }

    /**
     *
     * @return returns the connection to the database
     */
    public Connection conn(){
  return conn;
 }

    /**
     *
     * @param user Loads the user in the select statement
     * @param password loads the password in the select statement
     * @return
     */
    public boolean login(String user, String password){
    ResultSet results;   
    try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement("SELECT User_Name FROM users WHERE User_Name = ?" );
    sanitized.setString(1,user);
    results =sanitized.executeQuery();
    if(results.next()!= false){
        sanitized = conn.prepareStatement(
        "SELECT User_Name FROM users WHERE User_Name = ? AND password = ? " );
        sanitized.setString(1,user);
        sanitized.setString(2,password);
        results = sanitized.executeQuery();
        if(results.next()!= false){
        System.out.println("Logged in");
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
        return true;
        } catch (Exception e){System.out.println (e);} 
        }
        
        else {System.out.println("Wrong password or user name");return false;}
    
    }    
    
    else {System.out.println("Wrong password or user name");return false;}
    
    }catch(SQLException e){
            System.out.println("login problem");
        }
    return false; 
  }

    /**
     *
     * @return Returns the appointment data from the database
     */
    public ResultSet appData(){
  ResultSet app = null;
  String data = "SELECT Appointment_ID,Title,Description,Location,Contact_ID,Type,Start, End, Customer_ID from appointments";
     try{app = conn.createStatement().executeQuery(data);}catch(SQLException e){System.out.println("in app data");}
  return app;
 }
 
    /**
     *
     * @return returns the customer data from the database
     */
    public ResultSet CustomerData(){
  ResultSet app = null;
  String data = "SELECT Customer_ID,Customer_Name,Address,Postal_Code,Phone from customers";
     try{app = conn.createStatement().executeQuery(data);}catch(SQLException e){System.out.println("in app data");}
  return app;
 }
 
    /**
     *
     * @param month used to identify the month of the appointments
     * @return returns the appointments specific to that month.
     */
    public ResultSet MData(int month){
  ResultSet app = null;
  String s=String.valueOf(month);
  String data = "SELECT Appointment_ID,Title,Description,Location,Contact_ID,Type,Start, End, Customer_ID from appointments WHERE MONTH(START) = ?";  
try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setString(1,s);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
 }
 
    /**
     *
     * @param week used to identify the week of the appointment
     * @return returns the appointments from that week
     */
    public ResultSet WData(int week){
  ResultSet app = null;
  String s=String.valueOf(week);
  String data = "SELECT Appointment_ID,Title,Description,Location,Contact_ID,Type,Start, End, Customer_ID from appointments WHERE WEEK(START) = ?";  
try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setString(1,s);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  System.out.println("Returned");
  return app;
 }
 
    /**
     *
     * @param i used to identify the contact ID
     * @return returns the appointments attached to given contact ID
     */
    public ResultSet CData(int i){
 
 ResultSet app = null;
 String data = "SELECT Appointment_ID,Title,Description,Location,Contact_ID,Type,Start, End, Customer_ID from appointments Where Contact_ID = ?";
 String Cid = String.valueOf(i);
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setString(1,Cid);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
 }
 
    /**
     *
     * @return returns all the customers in the database, along with their name and ID.
     */
    public ResultSet Customer(){
 ResultSet app = null;
 String data = "Select Customer_ID,Customer_Name from customers";
 try{app = conn.createStatement().executeQuery(data);}catch(SQLException e){System.out.println("in app data");}
 return app;
 }
 
    /**
     *
     * @param time used to identify the time of the appointment
     * @return returns the appointment ID and start time from the given time.
     */
    public ResultSet Hdata(String time){
 
 ResultSet app = null;
 String data = "SELECT Appointment_ID, Start from appointments WHERE Start = ?";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setString(1,time);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
 }
  
 public ResultSet Hdata2(String time){
 
 ResultSet app = null;
 String data = "SELECT Appointment_ID, End from appointments WHERE End = ?";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setString(1,time);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
 }
 
 public ResultSet Hdata3(int Contact){
 
 ResultSet app = null;
 String data = "SELECT Start, End from appointments WHERE Contact_ID = ?";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setInt(1,Contact);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
 }   
 public ResultSet Initial(int month){
     ResultSet app = null;
 String data = "Select* from appointments where Type = 'Initial' &&  MONTH(START) = ? ";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setInt(1,month);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;  
 } 
 public ResultSet FollowUp(int month){
     ResultSet app = null;
 String data = "Select* from appointments where Type = 'Follow up ' &&  MONTH(START) = ? ";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setInt(1,month);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
     
 }   
 
 public ResultSet Final(int month){
     ResultSet app = null;
 String data = "Select* from appointments where Type = 'Final' &&  MONTH(START) = ? ";
 try{
    Statement statement = conn.createStatement();
    PreparedStatement sanitized = conn.prepareStatement(data);
    sanitized.setInt(1,month);
    app =sanitized.executeQuery();
    }catch(SQLException e){
        System.out.println(e);
        }
  return app;
     
 }   
 
 
}
       