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

public class Processing {

    public void startProcessing() throws IOException, ClassNotFoundException {

        Statement ST_INVOICEPARK = null, ST_INVOICEPOST = null;
        ResultSet RS_INVOICEPARK = null, RS_INVOICEPOST = null;
        String parkingSyncStatus = "", postingSyncStatus = "";
        String Query = null;
        String parkingflag = "", inv_park = "";


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
                ST_INVOICEPOST = con.createStatement();
                for (int i = 1; i <= 2; i++) {



                    if (i == 1) {

                        common.pendingpid.clear();
                        common.successpid.clear();
                        common.failurepid.clear();

//                        Query = "select ProcessInstanceID from WFINSTRUMENTTABLE "
//                                + "where ActivityId='" + common.service_parkingactivityid + "' "
//                                + "and ProcessDefID = '" + common.serviceprocessdefid + "' ";

                        Query = "select wf.ProcessInstanceID,ext.parkingSyncStatus,ext.inv_park from WFINSTRUMENTTABLE wf,ext_serviceprocess ext "
                                + "where ActivityId='" + common.service_parkingactivityid + "' "
                                + "and ProcessDefID = '" + common.serviceprocessdefid + "' and wf.ProcessInstanceID = ext.processid";
                        //System.out.println("Query :" + Query);
                        LogProcessing.xmllogs.info("Query is ::" + Query);
                        RS_INVOICEPARK = ST_INVOICEPARK.executeQuery(Query);
                        while (RS_INVOICEPARK.next()) {
                            common.attribute = "";
                            LogProcessing.xmllogs.info("*********************" + RS_INVOICEPARK.getString(1) + "***************************************");
                            LogProcessing.xmllogs.info("Starting processing for PID :" + RS_INVOICEPARK.getString(1));
                            parkingflag = RS_INVOICEPARK.getString(2);
                            inv_park = RS_INVOICEPARK.getString(3);

//                            if (("".equalsIgnoreCase(inv_park)
//                                    || "null".equalsIgnoreCase(inv_park)
//                                    || "NULL".equalsIgnoreCase(inv_park)
//                                    || inv_park == null )&& ("".equalsIgnoreCase(parkingflag)
//                                    || "null".equalsIgnoreCase(parkingflag)
//                                    || "NULL".equalsIgnoreCase(parkingflag)
//                                    || parkingflag == null||"Failure".equalsIgnoreCase(parkingflag))) {
                            if (true) {
                                System.out.println("Hello ::- " + parkingflag + " " + inv_park);
                                parkingSyncStatus = new Bapi_Service_Park().pushInvoiceForPark(RS_INVOICEPARK.getString(1)); // Use camel case change java name as Bapi_IncomingInvoice_Park
                                common.forwardWI(RS_INVOICEPARK.getString(1), common.service_parkingactivityid);
                                //System.out.println("Parking Status is :::" + parkingSyncStatus);
                                LogProcessing.xmllogs.info("Parking Status is :::" + parkingSyncStatus);


                            } else {
                                System.out.println("********Existing Workitem Forward Begins************");
                                LogProcessing.xmllogs.info("********Existing Workitem Forward Begins************");
                                common.forwardWI(RS_INVOICEPARK.getString(1), common.service_parkingactivityid);
                                LogProcessing.xmllogs.info("Parking Status is ::: Already Parked Just Forward");
                            }
                        }
                    }
                    //*************PArking End*****************//

                    //*************Posting Start****************************//
//                    if (i == -2) {
//                        Query = "select wf.ProcessInstanceID from WFINSTRUMENTTABLE wf, ext_orientAP ext "
//                                + "where wf.ProcessInstanceID = ext.processid "
//                                + "and wf.ActivityId='" + common.service_postingactivityid + "' "
//                                + "and wf.ProcessDefID = '" + common.serviceprocessdefid + "'";
//                        System.out.println("Query :" + Query);
//                        RS_INVOICEPOST = ST_INVOICEPOST.executeQuery(Query);
//                        while (RS_INVOICEPOST.next()) {
//                            common.attribute = "";
//                            getHistoryStatus = new PoHistory().getsetPoHistory(RS_INVOICEPOST.getString(1));
//                            if (getHistoryStatus.equalsIgnoreCase("Success")) {
//                                postingSyncStatus = new Bapi_IncomingInvoice_Create1().pushInvoiceForPost(RS_INVOICEPOST.getString(1)); // Use camel case change java name as Bapi_IncomingInvoice_Park
//                            }
//                            System.out.println("Posting Status is :::" + postingSyncStatus);
//
//                            common.forwardWI(RS_INVOICEPOST.getString(1), common.service_postingactivityid);
//                        }
//                    }
                    //*************Posting End*************************************//
                }
                common.setSummaryLogs("SchedulerParking");
                common.disconnectCall();
                try {

                    // RS_INVOICEPOST.close();
                    ST_INVOICEPOST.close();
                    RS_INVOICEPARK.close();
                    ST_INVOICEPARK.close();
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
