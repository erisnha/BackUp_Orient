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
import com.awc.sapbapi.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Processing {

    public void startProcessing() throws IOException, ClassNotFoundException, SQLException {

        Statement ST_INVOICEQUALITY = null, ST_MAINTAINHISTORY = null;
        ResultSet RS_INVOICEQUALITY = null;
        String qualitySyncStatus = "";
        String Query = null, sessionID = null;

        try {

            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String today = formatter.format(date);

            CommonFunctions common = new CommonFunctions();
            StartDate = common.getCurrentDateTime();
            LogProcessing.settingLogFiles();
            CommonFunctions.initialiseProperties();
            Class.forName(CommonFunctions.databaseDriverClass);
            con = DriverManager.getConnection(CommonFunctions.databaseDriverSource, CommonFunctions.dbUserName,
                    CommonFunctions.dbPassword);

            if (!"".equalsIgnoreCase(common.connectCall())) {
                ST_INVOICEQUALITY = con.createStatement();
                ST_MAINTAINHISTORY = con.createStatement();

                //*************Quality Start*************************************************// 
                common.pendingpid.clear();
                common.successpid.clear();
                common.failurepid.clear();
                Query = "select wf.ProcessInstanceID from WFINSTRUMENTTABLE wf, ext_orientAP ext "
                        + "where wf.ProcessInstanceID = ext.processid "
                        + "and wf.ActivityId='" + common.qualityactivityid + "' "
                        + "and wf.ProcessDefID = '" + common.processdefid + "' ";
                // System.out.println("Query :" + Query);
                RS_INVOICEQUALITY = ST_INVOICEQUALITY.executeQuery(Query);
                while (RS_INVOICEQUALITY.next()) {
                    LogProcessing.xmllogs.info("************************************************************");
                    LogProcessing.xmllogs.info("Starting processing for PID :" + RS_INVOICEQUALITY.getString(1));
                    common.attribute = "";
                    common.exceptionflagcounter = 0;
                    qualitySyncStatus = new Bapi_QualityCheck().qualityCheck(RS_INVOICEQUALITY.getString(1));
                    //System.out.println("qualitySyncStatus::::" + qualitySyncStatus);
                    //System.out.println("Quality Status is :::" + qualitySyncStatus);
                    if (common.exceptionflagcounter > 0) {
                        common.attribute = common.attribute + "<quality_decision>" + qualitySyncStatus + "</quality_decision>"
                                + "<ParkingSyncDate>" + today + "</ParkingSyncDate>";
                    }

                    if (qualitySyncStatus.equalsIgnoreCase("Success")) {
                    common.forwardWI(RS_INVOICEQUALITY.getString(1), common.qualityactivityid);
                     }
                    Query = "insert into complex_orient_transaction_log(status,transactionid,pinstanceid,processdefid,acted_on,acted_by,activity_name)"
                            + "values ('Quality','','" + RS_INVOICEQUALITY.getString(1) + "','" + common.processdefid + "','" + today + "','" + common.dmsUserName + "','QualityScheduler')";

                    ST_MAINTAINHISTORY.execute(Query);

                }
                common.setSummaryLogs("Quality");

                //*************Quality End*************************************************//
                common.disconnectCall();
                try {
                    RS_INVOICEQUALITY.close();
                    ST_INVOICEQUALITY.close();
                    ST_MAINTAINHISTORY.close();

                } catch (SQLException e) {
                    LogProcessing.errorlogs.info("Error in closing the get WI query connections :" + e.getMessage());
                }
            }
            con.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            LogProcessing.errorlogs.info("Error in Processing :" + e.getMessage());
        }
    }
}
