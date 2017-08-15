/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



/**
 *
 * @author boc
 */

/*************connection for Login****************/
public class services {

    private  String IPAddress;
    private  String LoginLib;
    private  String ProcessACTlib;
    private  String ProcessBUDlib;
    private  String PMSLibForGenAndInt;
    private  String IPAddressPMS;
    
    
    
    
    
    //private String IPAddress;
    
    
        
    

    public services() throws FileNotFoundException, IOException {
        
        FileReader reader = new FileReader("src/ServiceConf.properties");
        
        Properties prp= new Properties(); 
        prp.load(reader);
        
        this.IPAddress = prp.getProperty("IPAddress");
        this.LoginLib = prp.getProperty("LoginLib");
        
        this.ProcessACTlib = prp.getProperty("ProcessACTlib");
        this.ProcessBUDlib = prp.getProperty("ProcessBUDlib");
        this.PMSLibForGenAndInt = prp.getProperty("PMSLibForGenAndInt");
        this.IPAddressPMS = prp.getProperty("IPAddressPMS");
        
       
        
    }
       
    
    /*********** connection for login***********/
    public  boolean makeConnectionAS400(String UName,String PWord) throws SQLException{
      Connection con = null;  
       try {

            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
            System.out.println(LoginLib);
             
          String URL = "jdbc:as400:"+IPAddress+";naming=system;errors=full;libraries="+LoginLib+"";
           
            con = DriverManager.getConnection(URL,UName,PWord);
            
            return true;
      
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
             con.close();
        }
       return false;
     }
/*********connection to db BOCPRODDTA for data type process***********/
    public  Connection makeConnectionAS400BOCPRODDTA(String UName,String PWord,String data) throws SQLException{
         
    String URL = null; 
    Connection con = null;  
       try {

                        
           if(data.equals("Budget")){
              // URL = "jdbc:as400:"+IPAddress+";naming=system;errors=full;libraries=BOCBWPRDTA";
              URL = "jdbc:as400:"+IPAddress+";naming=system;errors=full;libraries="+ProcessBUDlib+"";
           }
           else if(data.equals("Actuals")){
               URL = "jdbc:as400:"+IPAddress+";naming=system;errors=full;libraries="+ProcessACTlib+""; 
              // URL = "jdbc:as400:"+IPAddress+";naming=system;errors=full;libraries=BOCBWPRDTA";
           }
           
            
            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
            
            con = DriverManager.getConnection(URL,UName,PWord);
            return con;
      
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        
       return null;
     }
    
    
 /*********connection to db PMSLIB***********/
    public  Connection makeConnectionAS400DB(String UName,String PWord) throws SQLException{
         
      
    Connection con = null;  
        try {

            DriverManager.registerDriver(new com.ibm.as400.access.AS400JDBCDriver()); 
            
            String URL = "jdbc:as400:"+IPAddressPMS+";naming=system;errors=full;libraries="+PMSLibForGenAndInt+"";    
             
            con = DriverManager.getConnection(URL,UName,PWord);
            return con;
      
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        finally{
            
        }
       return null;
     }

  

}
            
           
    
    

