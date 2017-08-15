/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import KPIServices.FeeIncomeServicess;
import KPIServices.IntIncomeGenThroPawingAdvancesServicess;
import KPIServices.InterestIncomeGeneratedThroughAdvancesExcptPwnngServicess;
import KPIServices.NPAforAllAdvancesExcePawningServicess;
import KPIServices.NPAforAllAdvancesExceptPawningandTODServicess;
import KPIServices.NPAforAllAdvancesServicess;
import KPIServices.NPAforPawningAdvancesServices;
import KPIServices.NonInterestExpencesServicess;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.SwingWorker;

/**
 *
 * @author boc
 */
public class processController {
    

    
    
    public boolean KPIDataProcess(Connection conPMSLIB,Connection conBOCPRODDTA,String data,String kpiNumber,String uName,String year,String period) throws SQLException, IOException{
         
        
        String queryActuals = null,queryBudget = null;
        String selectQuery = null;
        String query =null;
        
                
        //Annual, Bi-Annual, Quarter
        /*convert period to  2 bit*/
        switch (period) {
            case "Annual":
                period = "AN";
                break;
            case "Bi-Annual":
                period = "BI";
                break;
            case "Quarter":
                period = "QU";
                break;
            default:
                break;
        }
            
            //Actuals, Budget
            /*convert method to  3 bit*/
            if(data.equals("Actuals")){
                data ="ACT";
            }
            else if(data.equals("Budget")){
                data ="BUD";
            }
            
        switch (kpiNumber) {
            case "A00001":
                FeeIncomeServicess fis = new FeeIncomeServicess();
                //boolean rstB00006 = fis.FeeIncomeProcess(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,kpiMethod,year,period);
                //ProgressTest l = new ProgressTest();
                fis.setVisible(true);
                SwingWorker workA00001 = fis.createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00001.execute();
                return true;
            case "A00002":
                NonInterestExpencesServicess nies = new NonInterestExpencesServicess();
                nies .setVisible(true);
                SwingWorker workB03796 = nies .createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workB03796.execute();
                return true;
            case "A00013":
            {
                IntIncomeGenThroPawingAdvancesServicess iigtpa = new IntIncomeGenThroPawingAdvancesServicess();
                iigtpa .setVisible(true);
                SwingWorker workB00201 = iigtpa .createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workB00201.execute();
                return true;
            }
            case "A00012":
            {
                InterestIncomeGeneratedThroughAdvancesExcptPwnngServicess iigtpa = new InterestIncomeGeneratedThroughAdvancesExcptPwnngServicess();
                iigtpa .setVisible(true);
                SwingWorker workA00012 = iigtpa .createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00012.execute();
                return true;
            }
            case "A00028":
                NPAforAllAdvancesServicess nfaa = new NPAforAllAdvancesServicess();
                nfaa .setVisible(true);
                SwingWorker workA00028 = nfaa.createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00028.execute();
                return true;
            case "A00029":
                NPAforAllAdvancesExceptPawningandTODServicess nfaae = new NPAforAllAdvancesExceptPawningandTODServicess();
                nfaae .setVisible(true);
                SwingWorker workA00029 = nfaae.createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00029.execute();
                return true;
            case "A00030":
                NPAforAllAdvancesExcePawningServicess nfaaep = new NPAforAllAdvancesExcePawningServicess();
                nfaaep .setVisible(true);
                SwingWorker workA00030 = nfaaep.createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00030.execute();
                return true;
            case "A00032":
                NPAforPawningAdvancesServices nfpa = new NPAforPawningAdvancesServices();
                nfpa .setVisible(true);
                SwingWorker workA00032 = nfpa.createWorker(conPMSLIB,conBOCPRODDTA,data,kpiNumber,uName,year,period);
                workA00032.execute();
                return true;
            default:
                break;
        }
         //      
    return false;
    }
 //
 }


