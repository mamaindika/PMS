/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.sql.SQLException;
import service.services;

/**
 *
 * @author boc
 */
public class LoginController {


    
    public  boolean validateUser(String UName,String PWord) throws SQLException, IOException{
       
        services mc = new services();
        
        boolean rtn = mc.makeConnectionAS400(UName, PWord);
        
        if(rtn == true){
         return true;
        }

    return false;
    }
    
    
}
