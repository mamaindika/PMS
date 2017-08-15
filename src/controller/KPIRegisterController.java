/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import service.services;

/**
 *
 * @author boc
 */
public class KPIRegisterController {
private    Statement ps = null;    
private ResultSet rs;    

    public void KPIRegister(String kpiNumber, String kpiName, String kpiDescription, String addiDescription, String method, String responsiblePerson,String userName,String passWord) throws SQLException, IOException{
            services s = new services();
           

            Connection con = s.makeConnectionAS400DB(userName,passWord);
            //PMSKPI01 KK = new PMSKPI01(kpiNumber,kpiName,kpiDescription,addiDescription,method,responsiblePerson);
            
            
            if(con != null){
            System.out.println("database connection successful");
                
                String query;
                query = "INSERT INTO PMSKPI01 VALUES('"+kpiNumber+"','"+kpiName+"','"+kpiDescription+"','"+addiDescription+"','"+responsiblePerson+"','"+method+"','A')";
                
                try{
                ps = con.createStatement();
                
                rs = ps.executeQuery(query);
               
                rs.close();                       // Close the ResultSet                 4 
                ps.close();
                con.close();
                //System.out.println("Written Successfully");
                }
                catch(Exception ee){
                    System.out.println("Exception is "+ee);
                }
            }
     
    }
    
}
