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

        Statement ST_INVOICEPARK = null, ST_JOBTYPE = null, ST_JOBTYPE1 = null, ST_MAINTAINHISTORY = null, st_alreadyparked = null, ST_DeleteExceptionDetails;
        ResultSet RS_INVOICEPARK = null, RS_JOBTYPE = null, RS_JOBTYPE1 = null, RS_AlreadyParked = null;
        String parkingSyncStatus = "", getHistoryStatus = "", processInstanceId = "";
        String Query = null;
        CommonFunctions common = new CommonFunctions();
        try {
            Date date = Calendar.getInstance().getTime();
            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String today = formatter.format(date);

            StartDate = common.getCurrentDateTime();
            LogProcessing.settingLogFiles();
            CommonFunctions.initialiseProperties();
            Class.forName(CommonFunctions.databaseDriverClass);
            con = DriverManager.getConnection(CommonFunctions.databaseDriverSource, CommonFunctions.dbUserName,
                    CommonFunctions.dbPassword);
            if (!"".equalsIgnoreCase(common.connectCall())) {

                ST_INVOICEPARK = con.createStatement();
                st_alreadyparked = con.createStatement();
                ST_JOBTYPE = con.createStatement();
                ST_JOBTYPE1 = con.createStatement();
                ST_MAINTAINHISTORY = con.createStatement();
                ST_DeleteExceptionDetails = con.createStatement();
                //*************PArking Start*****************//

                common.pendingpid.clear();
                common.successpid.clear();
                common.failurepid.clear();
                PoHistory po_history = new PoHistory();
                Validation validation = new Validation();
                Query = "select wf.ProcessInstanceID from WFINSTRUMENTTABLE wf, ext_orientAP ext "
                        + "where wf.ProcessInstanceID = ext.processid "
                        + "and wf.ActivityId='" + common.parkingactivityid + "' "
                        + "and wf.ProcessDefID = '" + common.processdefid + "' "
                        + "and ext.inv_park is null";

                System.out.println("Query :" + Query);
                RS_INVOICEPARK = ST_INVOICEPARK.executeQuery(Query);
                System.out.println("RS_INVOICEPARK = " + RS_INVOICEPARK);
                while (RS_INVOICEPARK.next()) {

                    processInstanceId = RS_INVOICEPARK.getString(1);
                    System.out.println("processInstanceId =" + processInstanceId);

                    ST_DeleteExceptionDetails.execute("delete from complex_exception_details where pinstanceid='" + processInstanceId + "'");

                    LogProcessing.xmllogs.info("************************************************************");
                    LogProcessing.xmllogs.info("Starting processing for PID :" + processInstanceId);
                    common.attribute = "";
                    common.exceptionflagcounter = 0;
                    getHistoryStatus = po_history.getsetPoHistory(processInstanceId); //capture the status flag and do not execite further sap steps and move workitem to exception stage.
                    if (getHistoryStatus.equalsIgnoreCase("Success")) {
                        if (validation.checkBeforeParking(processInstanceId).equalsIgnoreCase("Success")) {
                            parkingSyncStatus = new Bapi_IncomingInvoice_Park().pushInvoiceForPark(processInstanceId); // Use camel case change java name as Bapi_IncomingInvoice_Park
                            System.out.println("Parking Status is :::" + parkingSyncStatus);
                        }
                    }
                    Query = "select top 1 ITEM_CAT from complex_orient_po_item where "
                            + "pinstanceid ='" + processInstanceId + "'";
                    System.out.println("Query is ::" + Query);
                    RS_JOBTYPE = ST_JOBTYPE.executeQuery(Query);
                    String jobtypepo = "";
                    while (RS_JOBTYPE.next()) {
                        jobtypepo = RS_JOBTYPE.getString(1);
                    }
                    common.attribute = common.attribute + "<jobtypepo>" + jobtypepo + "</jobtypepo>";
                    if (common.exceptionflagcounter > 0) {
                        common.attribute = common.attribute + "<ParkingSyncStatus>Failure</ParkingSyncStatus>"
                                + "<ParkingSyncDate>" + today + "</ParkingSyncDate>";
                    }
                    common.forwardWI(processInstanceId, common.parkingactivityid);

                    Query = "insert into complex_orient_transaction_log(status,transactionid,pinstanceid,processdefid,acted_on,acted_by,activity_name)"
                            + "values ('Parked','','" + processInstanceId + "','" + common.processdefid + "','" + today + "','" + common.dmsUserName + "','SchedulerParking')";

                    ST_MAINTAINHISTORY.execute(Query);
                    RS_JOBTYPE.close();
                }


                //Movement of already parked invoices------Saurish------
                Query = "select wf.ProcessInstanceID from WFINSTRUMENTTABLE wf, ext_orientAP ext "
                        + "where wf.ProcessInstanceID = ext.processid "
                        + "and wf.ActivityId='" + common.parkingactivityid + "' "
                        + "and wf.ProcessDefID = '" + common.processdefid + "' "
                        + "and ext.inv_park is not null";
                RS_AlreadyParked = st_alreadyparked.executeQuery(Query);
                System.out.println("Query is :::" + Query);
                while (RS_AlreadyParked.next()) {
                    System.out.println("Result is : " + RS_AlreadyParked.getString(1));
                    common.attribute = "";
                    Query = "select top 1 ITEM_CAT from complex_orient_po_item where  pinstanceid ='" + RS_AlreadyParked.getString(1) + "'";
                     System.out.println("Query is ::" + Query);
                    RS_JOBTYPE1 = ST_JOBTYPE1.executeQuery(Query);
                    String jobtypepo = "";
                    while (RS_JOBTYPE1.next()) {
                        jobtypepo = RS_JOBTYPE1.getString(1);
                    }
                    common.attribute = common.attribute + "<jobtypepo>" + jobtypepo + "</jobtypepo>";
                    common.forwardWI(RS_AlreadyParked.getString(1), common.parkingactivityid);
                }

                common.setSummaryLogs("Parking");
                //-------------------------------------------------------------------------
                //*************PArking End*****************//

//                common.disconnectCall();
//                try {
//                    RS_INVOICEPARK.close();
//                    ST_INVOICEPARK.close();
//                    ST_JOBTYPE.close();
//                    ST_MAINTAINHISTORY.close();
//                } catch (SQLException e) {
//                    LogProcessing.errorlogs.info("Error in closing the get WI query connections :" + e.getMessage());
//                }
            }
//            con.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            LogProcessing.errorlogs.info("Error in Processing :" + e.getMessage());
            common.setSummaryLogs("Parking");
        } finally {
            common.disconnectCall();
            RS_INVOICEPARK.close();
            ST_INVOICEPARK.close();
            ST_JOBTYPE.close();
            ST_MAINTAINHISTORY.close();
            con.close();
        }
    }
}
