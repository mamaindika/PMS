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
import KPIServices.generateAllServices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import jxl.write.WriteException;

/**
 *
 * @author boc
 */
public class KPIGenerateController {
    
    public void KPIGenerate(Connection conPMSLIB,String kpiNumber,String year,String data,String period) throws SQLException, IOException, WriteException{
        
        PreparedStatement pstmt = null,pmstGen=null;
        ResultSet rs = null,rsgen=null;
        
        if(data.equals("Actuals")){
                data ="ACT";
            }
            else if(data.equals("Budget")){
                data ="BUD";
        }
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
        
        switch (kpiNumber) {
            case "A00001":
                FeeIncomeServicess fis = new FeeIncomeServicess();
                boolean rst = fis.FeeIncomeGenerate(conPMSLIB,kpiNumber,year,data,period);
                break;
//
            case "A00002":
                NonInterestExpencesServicess nies = new NonInterestExpencesServicess();
                boolean rstB03796 = nies.NonInterestExpencesGenerate(conPMSLIB,kpiNumber,year,data,period);
                break;
            case "A00013":
                {
                    IntIncomeGenThroPawingAdvancesServicess iigtpa = new IntIncomeGenThroPawingAdvancesServicess();
                    boolean rstB00201 = iigtpa.IntIncomeGenThroPawingAdvancesGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            case "A00012":
                {
                    InterestIncomeGeneratedThroughAdvancesExcptPwnngServicess iigtpa = new InterestIncomeGeneratedThroughAdvancesExcptPwnngServicess();
                    boolean rstB00201 = iigtpa.IntIncomGenThrAdvExPwnGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            case "A00028":
                {
                    NPAforAllAdvancesServicess iigtpa = new NPAforAllAdvancesServicess();
                    boolean rstB00028 = iigtpa.NPAforAllAdvancesGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            case "A00029":
                {
                    NPAforAllAdvancesExcePawningServicess   naaep = new NPAforAllAdvancesExcePawningServicess();
                    boolean rstB00029 = naaep.NPAforAllAdvancesExcePawningGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            case "A00030":
                {
                    NPAforAllAdvancesExcePawningServicess   naaep = new NPAforAllAdvancesExcePawningServicess();
                    boolean rstB00030 = naaep.NPAforAllAdvancesExcePawningGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            case "A00032":
                {
                    NPAforPawningAdvancesServices   nfpa = new NPAforPawningAdvancesServices();
                    boolean rstB00030 = nfpa.NPAforPawningAdvancesGenerate(conPMSLIB,kpiNumber,year,data,period);
                    break;
                }
            default:
                break;
        }
        
    }

    public void KPIGenerateAll(Connection conPMSLIB, String year, String data, String period) throws SQLException {
        
        if(data.equals("Actuals")){
                data ="ACT";
            }
            else if(data.equals("Budget")){
                data ="BUD";
        }
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
        generateAllServices gas = new generateAllServices();
        boolean rstAll = gas.allGenerate(conPMSLIB,year,data,period);
    }
    
}
