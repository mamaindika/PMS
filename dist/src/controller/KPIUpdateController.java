/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author boc
 */
public class KPIUpdateController {
    
    public void KPIUpdate(String kpiName,String kpistatus,Connection con) throws SQLException{
           
            
            if(con != null){
            System.out.println(kpistatus);
                
                String query;
                query = "UPDATE  PMSKPI01 SET KPISTAT='"+kpistatus+"'WHERE KPINAME='"+kpiName+"'";
                
                try{
                Statement ps = con.createStatement();
                
                ResultSet rs = ps.executeQuery(query);
               
                rs.close();
                ps.close();
               
                System.out.println("Update Successfully");
                }
                catch(Exception ee){
                    System.out.println("Exception is "+ee);
                }
                finally{
                    con.close();
                }
            }
     
    }
    
}
