/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPIServices;

import java.awt.Desktop;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author boc
 */
public class generateAllServices {

    public boolean allGenerate(Connection conPMSLIB, String year, String data, String period) throws SQLException {
        PreparedStatement pstmt = null,pmstGen=null;
        ResultSet rs = null,rsgen=null;
        PreparedStatement pstmtPmsgen01 = null;
        ResultSet rspmsgen01 = null;
        
        try{
        
            
            File f = new File("All"+year+"_"+period+"_"+data+".xls");
            WritableWorkbook w = Workbook.createWorkbook(f);
            WritableSheet s = w.createSheet("Demo", 0);
            s.addCell(new Label(0, 0,"KPI Number"));
            s.addCell(new Label(1, 0,"Branch Code"));
            s.addCell(new Label(2, 0,"Period"));
            s.addCell(new Label(3, 0,"Data Type"));
            s.addCell(new Label(4, 0,"Value"));
       
            int genCount = 1;
            int exlSheetcnt = 0;
            try{
               System.out.println(year+data+period);
                pstmtPmsgen01 = conPMSLIB.prepareStatement("select * from pmsgen01 where(GENSTAT='1' AND GENYEAR ='"+year+"'AND GENTYPE ='"+data+"'AND GENPERD ='"+period+"')");
                  
                rspmsgen01 = pstmtPmsgen01.executeQuery();
                System.out.println("vvhfhuh");
                if(!rspmsgen01.next()){
                    System.out.println("There is no Query Result");
                }
                else{
                    do{
                        System.out.println(rspmsgen01.getString(1));
                        try{
                            pstmt = conPMSLIB.prepareStatement("select * from PMSDAT01 where(DAT1KPI='"+rspmsgen01.getString(1)+"' AND DAT1YEA ='"+year+"' AND DAT1PRD='"+period+"' AND DAT1TYP='"+data+"')ORDER BY DAT1BRN");
                            rs = pstmt.executeQuery();
                            if(!rs.next()){
                                System.out.println("There is no Query Result PMSDAT01");
                            }
                            else{
                                do{
                                    if(genCount%60000 !=0){
                                        String kpiNo =  rs.getString(1);
                                        String bCode =  rs.getString(2);
                                        String perd =  rs.getString(4);
                                        String dta =  rs.getString(5);
                                        String val =  rs.getString(6);

                                        s.addCell(new Label(0, genCount, kpiNo));
                                        s.addCell(new Label(1, genCount, bCode));
                                        s.addCell(new Label(2, genCount, perd));
                                        s.addCell(new Label(3, genCount, dta));
                                        s.addCell(new Label(4, genCount,val));
                                       
                                        
                                    }
                                    else{
                                        genCount=0;
                                        exlSheetcnt++;
                                        f = new File(""+year+"_"+period+"_"+data+Integer.toString(exlSheetcnt)+".xls");
                                        w = Workbook.createWorkbook(f);
                                        s = w.createSheet("Demo", 0);
                                        
                                        
                                    }
                                    genCount++;
                                }
                                while(rs.next());
                            }
                            pmstGen = conPMSLIB.prepareStatement("UPDATE pmsgen01 SET GENSTAT='5'WHERE(GENKPIN = '"+rspmsgen01.getString(1)+"'AND GENYEAR ='"+year+"'AND GENTYPE ='"+data+"'AND GENPERD ='"+period+"')");

                            try{
                                rsgen = pmstGen.executeQuery();  
                            }
                            catch(Exception ee){
                                
                            }
                            
                        }
                        catch(Exception et){
                        }
                        
                    }
                    while(rspmsgen01.next());
                }   
            }catch(Exception e){
                System.out.println("thissaas "+e);
            }
            finally{
               rspmsgen01.close();
               pstmtPmsgen01.close();
            }
            
            w.write();
            w.close();
            
            File f1 = new File("All"+year+"_"+period+"_"+data+".xls");
            Desktop dt = Desktop.getDesktop();
            dt.open(f1);
            
            return true;
        }
        catch(Exception g){
        }
        finally{
        
            
        }
    return false;
    }
    
    
   
}
