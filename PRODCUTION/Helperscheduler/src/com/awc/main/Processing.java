package com.awc.main;

import com.awc.methods.CommonFunctions;
import com.awc.methods.LogProcessing;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static com.awc.methods.CommonFunctions.StartDate;
import static com.awc.methods.CommonFunctions.con;


public class Processing {

    public void startProcessing() throws IOException, ClassNotFoundException {

        Statement ST_INVOICEPARK = null;
        ResultSet RS_INVOICEPARK = null;
       int count =0;
        String Query = null;


        try {
            CommonFunctions common = new CommonFunctions();
            StartDate = common.getCurrentDateTime();
            LogProcessing.settingLogFiles();
            CommonFunctions.initialiseProperties();
            Class.forName(CommonFunctions.databaseDriverClass);
            con = DriverManager.getConnection(CommonFunctions.databaseDriverSource, CommonFunctions.dbUserName,
                    CommonFunctions.dbPassword);
            if (!"".equalsIgnoreCase(common.connectCall())) {

                ST_INVOICEPARK = con.createStatement();
           
                
                    //  Query = "select processid from ext_orientAP where inv_park is null and processid in (select ProcessInstanceID from WFINSTRUMENTTABLE where ActivityName = 'ParkingException')";  
                    // Query = "select processid,EntryDATETIME,inv_park from ext_orientAP ext,WFINSTRUMENTTABLE wf where wf.ProcessInstanceID = ext.processid and wf.ActivityName='Finance' and EntryDATETIME >= '2021-02-12 21:00:00' and EntryDATETIME < '2021-02-13 12:59:23.653' and inv_park is NULL"; 
                     Query = "select ProcessInstanceID from WFINSTRUMENTTABLE where ProcessDefID = '1' and ActivityName='Quality' and EntryDATETIME >= '2021-01-15 12:43:23.260' and EntryDATETIME <= '2021-12-04 12:43:23.260'";
                       /* Query = "select wf.ProcessInstanceID from WFINSTRUMENTTABLE wf, ext_serviceprocess ext "
                                + "where wf.ProcessInstanceID = ext.processid "
                                + "and wf.ActivityId='" + common.service_parkingactivityid + "' "
                                + "and wf.ProcessDefID = '" + common.serviceprocessdefid + "' ";*/
                        System.out.println("Query :" + Query);
                        
                        RS_INVOICEPARK = ST_INVOICEPARK.executeQuery(Query);
                        System.out.println("RAHUL :::--"+ common.qualityactivityid);
                        while (RS_INVOICEPARK.next()) {
                            
                            count++;
                           // common.attribute = "<ParkingException_decision>Send for parking</ParkingException_decision>";
                            common.attribute = "<quality_decision>Quality Accepted</quality_decision>";
                            LogProcessing.xmllogs.info("************************************************************");
                            LogProcessing.xmllogs.info("Starting processing for PID :" + RS_INVOICEPARK.getString(1));
                           
                            System.out.println(RS_INVOICEPARK.getString(1));

                           
                            common.forwardWI(RS_INVOICEPARK.getString(1), common.qualityactivityid);


                }
                        System.out.println("Count is :::"+count);
                common.disconnectCall();
                try {

                    RS_INVOICEPARK.close();
                    ST_INVOICEPARK.close();
                } catch (SQLException e) {
                    LogProcessing.errorlogs.info("Error in closing the get WI query connections :" + e.getMessage());
                }
            }
            con.close();
            common.setSummaryLogs();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            LogProcessing.errorlogs.info("Error in Processing :" + e.getMessage());
        }
    }
}
