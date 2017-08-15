/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KPIServices;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author boc
 */
public class FeeIncomeServicess extends javax.swing.JFrame {
    
    private String ActualsANlib;
    private String BudgetLib;
    private String validateLib;
    private String InsertLib;
    
   
    /**
     * Creates new form FeeIncomeServicess
     * @throws java.io.IOException
     */
    public FeeIncomeServicess() throws IOException {
        
        initComponents();
        Properties prop = new Properties();
        FileReader reader = new FileReader("src/prop.properties");
        prop.load(reader);
        
        this.ActualsANlib = prop.getProperty("ActualsANLib");
        this.BudgetLib = prop.getProperty("BudgetLib");
        this.validateLib = prop.getProperty("validateLib");
        this.InsertLib = prop.getProperty("InsertLib");
 
    }
     
    
    public SwingWorker createWorker(Connection conPMSLIB,Connection conBOCPRODDTA,String data,String kpiNumber,String uName,String year,String period) {
        return new SwingWorker<Boolean, Integer>() {
            private String dta,prd;
            @Override
            protected Boolean doInBackground() throws Exception {
                
                    
            String selectQuery = null;
            String query =null;
        
            String queryActualsAN = "select gmcntr,sum(gmcbal) as actu12 from "+ActualsANlib+"/MISmap03 m, "+ActualsANlib+"/glp00301 g where mapbcd BETWEEN 51100 AND 51299 and mapbcd != 51103 and g.gmact  = m.mapacn group by gmcntr order by gmcntr";
            String queryBudgetAN = "select gmcntr,sum(gmbu12) as budg12 from "+BudgetLib+"/MISmap03 m, "+BudgetLib+" /glp01301 g where mapbcd BETWEEN 51100 AND 51299 and mapbcd != 51103 and g.gmact  = m.mapacn group by gmcntr order by gmcntr  ";
            String queryBudgetBI = "select gmcntr,sum(gmbu12) as budg6 from "+BudgetLib+"/MISmap03 m, "+BudgetLib+" /glp01301 g where mapbcd BETWEEN 51100 AND 51299 and mapbcd != 51103 and g.gmact  = m.mapacn group by gmcntr order by gmcntr  ";
            String queryBudgetQU = "select gmcntr,sum(gmbu12) as budg3 from "+BudgetLib+"/MISmap03 m, "+BudgetLib+" /glp01301 g where mapbcd BETWEEN 51100 AND 51299 and mapbcd != 51103 and g.gmact  = m.mapacn group by gmcntr order by gmcntr  ";
            
                switch (data) {
                    case "ACT":
                        this.dta ="Actuals";
                        switch (period) {
                            case "AN":
                                selectQuery = queryActualsAN;
                                this.prd = "Annual";
                                break;
                            case "BI":
                                selectQuery = queryActualsAN;
                                this.prd = "Bi-Annual";
                                break;
                            case "QU":
                                selectQuery = queryActualsAN;
                                this.prd = "Quarter";
                                break;
                            default:
                                break;
                        }
                        break;
                    case "BUD":
                        this.dta ="Budget";
                        switch (period) {
                            case "AN":
                                selectQuery = queryBudgetAN;
                                this.prd = "Annual";
                                break;
                            case "BI":
                                selectQuery = queryBudgetBI;
                                this.prd = "Bi-Annual";
                                break;
                            case "QU":
                                selectQuery = queryBudgetQU;
                                this.prd = "Quarter";
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            
            
            Name.setText("Fee Income");
            periodd.setText(prd);
            dataa.setText(dta);
            yearr.setText(year);
           
            try{
            PreparedStatement pstmt = conBOCPRODDTA.prepareStatement(selectQuery); 
            PreparedStatement pstmt1 = conBOCPRODDTA.prepareStatement(selectQuery); 
            ResultSet rs = pstmt.executeQuery(); //for get query count
            ResultSet rs1 = pstmt1.executeQuery();
                
                int count =0;
                while (rs.next()) {               
                   
                    rs.getString(1);
                    count++;
                }
               
                
                int count1 =0;
                while(rs1.next()){
                 
                    count1++;
                    int cnt1 =(int)((double)((float)count1/count)*100);
                    
                    publish(cnt1);
                    SimpleDateFormat dat1 = getDate();
                    String date = dat1.format(new Date());
                    
                     try{
                        String sql ="INSERT INTO "+InsertLib+" VALUES('"+kpiNumber+"','"+rs1.getString(1)+"','"+year+"','"+period+"','"+data+"','"+rs1.getString(2)+"','"+date+"','"+uName+"')";
                       
                        try (Statement cs = conPMSLIB.createStatement()) {
                            ResultSet rs3 = cs.executeQuery(sql);
                            rs3.close();
                        }
                     }
                     catch(Exception e){
                        //System.out.println(e);
                     }
               
                }
                
                try{
                    System.out.println(validateLib);
                    String sqlpmsgen01 = "INSERT INTO "+validateLib+" VALUES('"+kpiNumber+"','"+year+"','"+data+"','"+period+"','1')";
                    Statement cs1 = conPMSLIB.createStatement();
                    ResultSet rs4 = cs1.executeQuery(sqlpmsgen01);
                }
                catch(Exception e){
                    //System.out.println("pmsgen01 "+e);
                }
                finally{
                    //rs4.close();
                    //cs1.close();
                }
                return true;

            }catch(Exception e){
                System.out.println("thisss "+e);
            }
            finally{
               
               conPMSLIB.close();
               conBOCPRODDTA.close();
            }
               return true;
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Get Info
                chunks.stream().forEach((number) -> {
                    if(number<100){
                        ProgressBar.setValue(number);
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                    else{
                        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        UpperPanel.removeAll();
                        UpperPanel.add(ProcessCompletePanel);
                        UpperPanel.repaint();
                        UpperPanel.revalidate();
                        ProgressBar.setValue(100);
                    }
                });
            }

            @Override
            protected void done() {
                boolean bStatus = false;
                try {
                    bStatus = get();
                } catch (InterruptedException | ExecutionException ex) {
                }
                System.out.println("Finished with status " + bStatus);
            }
        };
    } 
     
    private void waitFor (int iMillis) {
        try {
            Thread.sleep(iMillis);
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
    } 


    public boolean FeeIncomeGenerate(Connection conPMSLIB,String kpiNumber,String year,String data,String period) throws SQLException, IOException, WriteException{
        
        PreparedStatement pstmt = null,pmstGen=null;
        ResultSet rs = null,rsgen=null;
        String period1 = null,data1 = null;
        
        try{
        
            pstmt = conPMSLIB.prepareStatement("select * from "+InsertLib+" where(DAT1KPI='"+kpiNumber+"' AND DAT1YEA ='"+year+"' AND DAT1PRD='"+period+"' AND DAT1TYP='"+data+"')ORDER BY DAT1BRN");
            //System.out.println("select * from PMSDAT01 where(DAT1KPI='"+kpiNumber+"' AND DAT1YEA ='"+year+"' AND DAT1PRD='"+period+"' AND DAT1TYP='"+data+"')ORDER BY DAT1BRN");
            rs = pstmt.executeQuery(); 
            File dir = new File("C:\\PMSDATA\\FeeIncome");
            dir.mkdir();
            
            File f = new File("C:\\PMSDATA\\FeeIncome\\FeeIncome"+year+""+period+""+data+".xls");
            WritableWorkbook w = Workbook.createWorkbook(f);
            WritableSheet s = w.createSheet("Demo", 0);
            
            switch (period) {
                case "AN":
                    period1 = "Annual";
                    break;
                case "BI":
                    period1 = "Bi-Annual";
                    break;
                case "QU":
                    period1 = "Quarter";
                    break;
                default:
                    break;
            }
            
            if(data.equals("ACT")){
                data1 = "Actuals";
            }
            else if(data.equals("BUD")){
                data1 = "Budjet";
            }
            
            s.addCell(new Label(0, 0,"KPI Name"));
            s.addCell(new Label(0, 1,"Period"));
            s.addCell(new Label(0, 2,"Data Type"));
            
            s.addCell(new Label(1, 0,"------>>"));
            s.addCell(new Label(1, 1,"------>>"));
            s.addCell(new Label(1, 2,"------>>"));
            
            s.addCell(new Label(2, 0,"Fee Income"));
            s.addCell(new Label(2, 1,period1));
            s.addCell(new Label(2, 2,data1));
            
            s.addCell(new Label(0, 4,"Branch Code"));
            s.addCell(new Label(1, 4,"Value"));
       
            int genCount = 5;
            try{
                while(rs.next()){
                  
                  String bCode =  rs.getString(2);
                  String val =  rs.getString(6);
                  
                  s.addCell(new Label(0, genCount, bCode));
                  s.addCell(new Label(1, genCount,val));
                  genCount++;
                }
            }catch(SQLException | WriteException e){
                System.out.println("this "+e);
            }
            finally{
               rs.close();
               pstmt.close();
            }
            pmstGen = conPMSLIB.prepareStatement("UPDATE "+validateLib+" SET GENSTAT='5'WHERE(GENKPIN = '"+kpiNumber+"'AND GENYEAR ='"+year+"'AND GENTYPE ='"+data+"'AND GENPERD ='"+period+"')");

            try{
               rsgen = pmstGen.executeQuery();  
            }
            catch(Exception ee){
            
            }
            
            w.write();
            w.close();
            
            File f1 = new File("C:\\PMSDATA\\FeeIncome\\FeeIncome"+year+""+period+""+data+".xls");
            Desktop dt = Desktop.getDesktop();
            dt.open(f1);
            
            return true;
        }
        catch(SQLException | IOException | WriteException g){
        }
        finally{
        
            conPMSLIB.close();
        }
    return false;
    }
   
    
    
    public SimpleDateFormat getDate(){
        Date d = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String st=f.toString();

        return f;
    }

   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        query1 = java.beans.Beans.isDesignTime() ? null : ((javax.persistence.EntityManager)null).createQuery("");
        BottomPanel = new javax.swing.JPanel();
        ProgressBar = new javax.swing.JProgressBar();
        UpperPanel = new javax.swing.JPanel();
        ProcessingPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dataa = new javax.swing.JLabel();
        periodd = new javax.swing.JLabel();
        Name = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        yearr = new javax.swing.JLabel();
        ProcessCompletePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Chart = new javax.swing.JButton();
        Charts = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(680, 400));

        BottomPanel.setBackground(new java.awt.Color(153, 255, 153));

        ProgressBar.setBackground(new java.awt.Color(51, 204, 255));
        ProgressBar.setForeground(new java.awt.Color(51, 204, 255));

        javax.swing.GroupLayout BottomPanelLayout = new javax.swing.GroupLayout(BottomPanel);
        BottomPanel.setLayout(BottomPanelLayout);
        BottomPanelLayout.setHorizontalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addContainerGap())
        );
        BottomPanelLayout.setVerticalGroup(
            BottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        getContentPane().add(BottomPanel, java.awt.BorderLayout.PAGE_END);

        UpperPanel.setBackground(new java.awt.Color(255, 255, 153));
        UpperPanel.setLayout(new java.awt.CardLayout());

        ProcessingPanel.setBackground(new java.awt.Color(255, 255, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Processing ...");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Year");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Period");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Data");

        dataa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        periodd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        Name.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Please wait ");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("KPI Name");

        yearr.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout ProcessingPanelLayout = new javax.swing.GroupLayout(ProcessingPanel);
        ProcessingPanel.setLayout(ProcessingPanelLayout);
        ProcessingPanelLayout.setHorizontalGroup(
            ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessingPanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProcessingPanelLayout.createSequentialGroup()
                        .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Name, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE)
                            .addComponent(yearr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(periodd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27))
                    .addGroup(ProcessingPanelLayout.createSequentialGroup()
                        .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        ProcessingPanelLayout.setVerticalGroup(
            ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProcessingPanelLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProcessingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ProcessingPanelLayout.createSequentialGroup()
                        .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yearr, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(periodd, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ProcessingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataa, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        UpperPanel.add(ProcessingPanel, "card2");

        ProcessCompletePanel.setBackground(new java.awt.Color(255, 255, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("Process Completed");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Chart.setText("Chart");
        Chart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ProcessCompletePanelLayout = new javax.swing.GroupLayout(ProcessCompletePanel);
        ProcessCompletePanel.setLayout(ProcessCompletePanelLayout);
        ProcessCompletePanelLayout.setHorizontalGroup(
            ProcessCompletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProcessCompletePanelLayout.createSequentialGroup()
                .addContainerGap(317, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(317, 317, 317))
            .addGroup(ProcessCompletePanelLayout.createSequentialGroup()
                .addGroup(ProcessCompletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProcessCompletePanelLayout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel2))
                    .addGroup(ProcessCompletePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Chart)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ProcessCompletePanelLayout.setVerticalGroup(
            ProcessCompletePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessCompletePanelLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(Chart)
                .addContainerGap())
        );

        UpperPanel.add(ProcessCompletePanel, "card3");

        javax.swing.GroupLayout ChartsLayout = new javax.swing.GroupLayout(Charts);
        Charts.setLayout(ChartsLayout);
        ChartsLayout.setHorizontalGroup(
            ChartsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 687, Short.MAX_VALUE)
        );
        ChartsLayout.setVerticalGroup(
            ChartsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        UpperPanel.add(Charts, "card4");

        getContentPane().add(UpperPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       this.dispose(); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ChartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomPanel;
    private javax.swing.JButton Chart;
    private javax.swing.JPanel Charts;
    private javax.swing.JLabel Name;
    private javax.swing.JPanel ProcessCompletePanel;
    private javax.swing.JPanel ProcessingPanel;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JPanel UpperPanel;
    private javax.swing.JLabel dataa;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel periodd;
    private javax.persistence.Query query1;
    private javax.swing.JLabel yearr;
    // End of variables declaration//GEN-END:variables
}
