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
import com.awc.methods.XMLParser;
import com.awc.sapBapi.Bapi_Gstn_Pan_Tds;
import com.awc.sapBapi.Bapi_PO_GetDetails;
import com.newgen.dmsapi.DMSCallBroker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Processing {

    String busPlc_val = "";
    String finance_user = "";
    String initiator_user = "";
    String plant = "";
    XMLParser xmlParser = new XMLParser();

    public void startProcessing() throws IOException, ClassNotFoundException {

        Statement st_sharepoint = null;
        ResultSet rs_sharepoint = null;
        Statement st_sharepoint1 = null;
        Statement st_InvDistinct = null;
        ResultSet rs_InvDistinct = null;
        String parkingSyncStatus = "";
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
                new MedtaData_CSV_Reader().getCSVPathInfo(common);
                Bapi_Gstn_Pan_Tds gstn_pan = new Bapi_Gstn_Pan_Tds();
                try {

                    common.pendingpid.clear();
                    common.successpid.clear();
                    common.failurepid.clear();

                    st_sharepoint = con.createStatement();
                    st_sharepoint1 = con.createStatement();
                    st_InvDistinct = con.createStatement();
                    Query = "select ponumber,sharePointRequestId,serviceentryno,invoicenumber, invoicedate,invoiceamount,vendorCode,documentpath,documentname,Record_Id,created_by from Ext_SharepointIntegration where processinstanceid ='NOT_ASSIGN' ";
                    //System.out.println(Query);
                    rs_InvDistinct = st_InvDistinct.executeQuery(Query);
                    LogProcessing.xmllogs.info("Query is ::" + Query);
                    while (rs_InvDistinct.next()) {
                        common.attribute = "";
                        common.enrtySheet_list.clear();
                        String PurchaseOrderNo = rs_InvDistinct.getString(1);
                        String POReference = rs_InvDistinct.getString(2);
                        String serviceentryno = rs_InvDistinct.getString(3);
                        String invoicenumber = rs_InvDistinct.getString(4);

                        String invoicedate = rs_InvDistinct.getString(5);
                        String invoiceamount = rs_InvDistinct.getString(6);
                        //String vendorCode1 = rs_InvDistinct.getString(7);
                        String documentpath = rs_InvDistinct.getString(8);
                        String documentname = rs_InvDistinct.getString(9);
                        String recordId = rs_InvDistinct.getString(10);
                        String created_by = rs_InvDistinct.getString(11);

                        String entrysheet_no[] = serviceentryno.split(";#");
                        for (int i = 0; i < entrysheet_no.length; i++) {
                            common.attribute = common.attribute + "<q_orient_entrysheet_sel>"
                                    + "<entrysheet_no>" + entrysheet_no[i] + "</entrysheet_no>"
                                    + "</q_orient_entrysheet_sel>";

                            common.enrtySheet_list.add(entrysheet_no[i]);
                        }
                        //   System.out.println("List is" + common.enrtySheet_list);

                        String inv_amnt[] = invoiceamount.split(";#");
                        double InvoiceAmount = 0.0;
                       
                        for (int i = 0; i < inv_amnt.length; i++) {
                            double amnt = Double.parseDouble(inv_amnt[i]);
                            InvoiceAmount = InvoiceAmount + amnt;
                        }
                        LogProcessing.xmllogs.info("***************Start Processing recordId  :- " + recordId + "***********************");
                        LogProcessing.xmllogs.info("***************Start Processing POReference  :- " + POReference + "***********************");
                        LogProcessing.xmllogs.info("***************Start Processing PO  :- " + PurchaseOrderNo + "***********************");
                        new Bapi_PO_GetDetails().getPODetails(PurchaseOrderNo);//====Calling Bapi of PODETAILS VendorCode sets here====//

                        //----Fetching Orient Code-------------
                        String vendorCode = common.vendorCode;
                        String gstn = gstn_pan.getVendorGSTN(vendorCode);
                        String pan = gstn_pan.getVendorPAN(vendorCode);
                        String orient_gstn = gstn_pan.getVendorGSTN(new CommonFunctions().plant);
                        String orient_pan = gstn_pan.getVendorPAN(new CommonFunctions().plant);
                        gstn_pan.getTDS(vendorCode);
                        //calling getuserid
                        //System.out.println("calling getuserid");
                        try {
                            getuserid(common.plant, common); //Getting Value of finance_user and initiator_user
                        } catch (Exception e) {
                            System.out.println("Exception is ::-- " + e);
                            LogProcessing.errorlogs.info("Error in Processing 123:" + e.getMessage());
                        }
                        getBusinessPlace(common);//Getting Value of finance_user and initiator_user
                        common.attribute = common.attribute
                                + "<PurchaseOrderNo>" + PurchaseOrderNo + "</PurchaseOrderNo>"
                                + "<InvoiceDate>" + invoicedate + "</InvoiceDate>"
                                + "<InvoiceNo>" + invoicenumber + "</InvoiceNo>"
                                + "<InvoiceAmount>" + InvoiceAmount + "</InvoiceAmount>"
                                + "<gstn_sap>" + gstn + "</gstn_sap>"
                                + "<GSTN>" + gstn + "</GSTN>"
                                + "<pan_sap>" + pan + "</pan_sap>"
                                + "<PAN>" + pan + "</PAN>"
                                + "<orient_gstn>" + orient_gstn + "</orient_gstn>"
                                + "<gstn_inv>" + orient_gstn + "</gstn_inv>"
                                + "<orient_PAN>" + orient_pan + "</orient_PAN>"
                                + "<pan_inv>" + orient_pan + "</pan_inv>"
                                + "<finance_user>" + finance_user + "</finance_user>"
                                + "<initiator_user>" + initiator_user + "</initiator_user>"
                                + "<sharePointRequest>" + POReference + "</sharePointRequest>"
                                + "<plant>" + this.plant + "</plant>"
                                + "<bus_place>" + busPlc_val + "</bus_place>"
                                + "<created_by>" + created_by + "</created_by>";

                        String doc_path = documentpath.replace("/", "\\");
                        String doc_name = doc_path + "\\" + documentname;
                        //  System.out.println("The document PAth is ::" + doc_name);
                        LogProcessing.xmllogs.info("The document Path is ::" + doc_name);
                        String processInstanceID = common.uploadWI(common.addDocument(doc_name, common.volumeIndex), recordId, POReference, PurchaseOrderNo, invoicenumber, plant, invoicedate);

                        //volume Index is set at the time of connect Cabinet//
                        // System.out.println("RETURN PID IS ::::" + processInstanceID);
//                        if (!processInstanceID.equalsIgnoreCase("NOT_ASSIGN")) {
//                            Query = "Update Ext_SharepointIntegration set ProcessInstanceId ='" + processInstanceID + "' where Invoicenumber='" + invoicenumber + "'";
//                            //System.out.println(Query);
//                            System.out.println("PID :- " + processInstanceID + " where Invoice Number :- " + invoicenumber);
//                            st_sharepoint1.executeUpdate(Query);
//
//                            Query = "Update ext_serviceprocess set processid ='" + processInstanceID + "' where Invoiceno='" + invoicenumber + "'";
//                            //  System.out.println(Query);
//                            st_sharepoint1.executeUpdate(Query);
//                        }
                    }

                } catch (Exception e) {
                    System.out.println("Hello");
                    LogProcessing.errorlogs.info("Error in Processing 123:" + e.getMessage());
                }

            }
            common.setSummaryLogs("Service Invoice");
            common.disconnectCall();

            con.close();

        } catch (IOException | ClassNotFoundException | SQLException e) {
            LogProcessing.errorlogs.info("Error in Processing 321:" + e.getMessage());
        }
    }

    private void getuserid(String plant, CommonFunctions common) {
        String Query = "Select initiator_user,finance_user from service_username where plant_code like '" + plant.replace("P", "") + "'";

        try {
            this.plant = plant.replace("P", "");
            Statement st_user = common.con.createStatement();
            ResultSet rs_user = null;
            rs_user = st_user.executeQuery(Query);
            while (rs_user.next()) {
                initiator_user = rs_user.getString(1);
                finance_user = rs_user.getString(2);
            }


        } catch (SQLException e) {

            LogProcessing.errorlogs.info("Error in Processing getuserid :" + e.getMessage());
        }
    }

    private void getBusinessPlace(CommonFunctions common) {
        //*****************************BUS_PLACE*********************************//
        try {

            String inputXml1 = common.sapInvokeXML("ZBAPI_AP_AUTOMATION_BUSPLACE");
            inputXml1 = inputXml1 + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PLANT>" + this.plant + "</PLANT>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            // System.out.println("Input XML : " + inputXml1);

            LogProcessing.xmllogs.info("INPUT ZBAPI_AP_AUTOMATION_BUSPLACE :::" + inputXml1);
            String outputXml1 = DMSCallBroker.execute(inputXml1, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            xmlParser.setInputXML(outputXml1);
            LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_BUSPLACE :::" + outputXml1);

            // System.out.println("After xml response");
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                busPlc_val = xmlParser.getValueOf("BUSS_PLACE");
            } else {
                busPlc_val = "";
            }
//*****************************BUS_PLACE*********************************//
        } catch (Exception e) {
            LogProcessing.errorlogs.info("Error in Processing :" + e.getMessage());
        }


    }
}
