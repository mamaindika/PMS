/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author boc
 */
public class selectServices {
    
                
public static void selectPMSKPI01(Connection conBOCPRODDTA) throws SQLException{
    
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    String query = "select* from PMSKPI01";
    
    pstmt = conBOCPRODDTA.prepareStatement(query); 
    rs = pstmt.executeQuery(query); 
    while (rs.next()) {               
                   
        rs.getString(1);
        System.out.println(rs.getString(1));
     }



}
    
}
