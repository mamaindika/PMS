/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import GUIS.PMSLoginMain;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author boc
 */
public class getJtextField {

   
    
   
    
public boolean IfexistValue(Connection con,String kpiName) throws SQLException{
        
        String empnum, phonenum;
        System.out.println(kpiName);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            pstmt = con.prepareStatement("SELECT * FROM PMSKPI01 WHERE KPINAME=?"); 

            pstmt.setString(1,kpiName);      

            rs = pstmt.executeQuery(); 
            if( rs!= null){
                return true;
            }
            
        }catch(Exception e){
            System.out.println("this "+e);
        }
        finally{
          rs.close();                      
          pstmt.close();   
        }
          
    return false;
    }
    
    public String setTextfields(Connection con,String kpiName) throws SQLException{
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String kpiname = null;
        String method = null;
        PMSLoginMain pmslm = new PMSLoginMain();
        try{
            pstmt = con.prepareStatement("SELECT * FROM PMSKPI01 WHERE KPINAME=?"); 

            pstmt.setString(1,kpiName);      

            rs = pstmt.executeQuery(); 
            while (rs.next()) {               // Position the cursor                  4 
                kpiname = rs.getString(2).trim();        // Retrieve the first column value
                method = rs.getString(4);      // Retrieve the first column value
                
            }
            
            System.out.println(kpiname+method);
            
            String mesg = kpiname+"~"+method;
            return mesg;
        }catch(Exception e){
            System.out.println(e);
        }
        finally{
          rs.close();                      
          pstmt.close();   
        }
     return null;
    }
    
}
