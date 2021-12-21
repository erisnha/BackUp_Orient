package com.awc.methods;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPDBRecoverDocData;
import ISPack.ISUtil.JPISException;
import ISPack.ISUtil.JPISIsIndex;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import com.newgen.niplj.generic.NGIMException;
import java.sql.Statement;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;

public class CommonFunctions {

    public static String cabinetName = null, processdefid = null, sessionid = null, serverIP = null,
            serverPort = null, dbUserName = null, dbPassword = null, databaseDriverSource = null,
            databaseDriverClass = null, dmsUserName = null, dmsPassword = null, sapHostName = null, sapInstance = null, inputxml = null, outputxml = null,
            StartDate = null, WIStatus, WIMessage, attribute = "", qualityactivityid = null, parkingactivityid = null, postingactivityid = null,
            sapUserName = null, sapPassword = null, volumeIndex = null, plant = null, vendorCode = null, documentPath = null, vendorName = "";
    static DMSXmlResponse xmlResponse;
    public static Connection con = null;
    public static HashSet<String> pendingpid = new HashSet();
    //public static HashSet<String> successpid = new HashSet();
    public static HashMap successpid = new HashMap();
    public static HashMap failurepid = new HashMap();
    public static List<String> enrtySheet_list = new ArrayList();
    long millis = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(millis);
    public String currentDate = date.toString();

    public static void initialiseProperties() throws FileNotFoundException, IOException {
        String finaldir = System.getenv("JBOSS_HOME") + "\\bin\\Orient\\conf\\conf.properties";
        Properties prop = new Properties();
        InputStream is = new FileInputStream(finaldir);
        prop.load(is);
        cabinetName = prop.getProperty("EngineName");
        serverIP = prop.getProperty("ServerIP");
        serverPort = prop.getProperty("ServerPort");
        dbUserName = prop.getProperty("UserName");
        dbPassword = prop.getProperty("Password");
        databaseDriverClass = prop.getProperty("DatabaseDriverClass");
        databaseDriverSource = prop.getProperty("DatabaseDriverSource");
        sapHostName = prop.getProperty("SapHostName");
        sapInstance = prop.getProperty("SapInstance");
        sapUserName = prop.getProperty("SapUserName");
        sapPassword = prop.getProperty("SapPassword");
//        dmsUserName = prop.getProperty("SyncUserName1");
//        dmsPassword = prop.getProperty("SyncPassword1");

        dmsUserName = prop.getProperty("DMSUserName");
        dmsPassword = prop.getProperty("DMSPassword");
        documentPath = prop.getProperty("service_DocumentPath");


        //  processdefid = prop.getProperty("SupplyPoProcessDefID");
        processdefid = prop.getProperty("ServiceProcessDefID");
        parkingactivityid = prop.getProperty("SEVICEPROCESS_SchedulerParkingActivityID");

    }

    public String connectCall() throws IOException {
        inputxml = "<NGOConnectCabinet_Input>" + "<Option>NGOConnectCabinet</Option>" + "<CabinetName>"
                + CommonFunctions.cabinetName + "</CabinetName>" + "<UserName>" + CommonFunctions.dmsUserName
                + "</UserName>" + "<UserPassword>" + CommonFunctions.dmsPassword + "</UserPassword>"
                + "<Scope>ADMIN</Scope>" + "<UserExist>N</UserExist>" + "<CurrentDateTime></CurrentDateTime>"
                + "<UserType>U</UserType>" + "<MainGroupIndex>0</MainGroupIndex>" + "<Locale></Locale>"
                + "</NGOConnectCabinet_Input>";


        LogProcessing.xmllogs.info("Connect XML :: " + inputxml);
        outputxml = DMSCallBroker.execute(inputxml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
        LogProcessing.xmllogs.info("Connect Output XML :: " + outputxml);
        xmlResponse = new DMSXmlResponse(outputxml);
        String status = xmlResponse.getVal("Status");
        volumeIndex = xmlResponse.getVal("ImageVolumeIndex");
        if (status.equalsIgnoreCase("0")) {
            sessionid = xmlResponse.getVal("UserDBId");
            LogProcessing.summlogs.info("Cabinet connected successfully..!!");
        } else {
            sessionid = "";
            LogProcessing.summlogs.info("Error in Connecting to cabinet..!!");
        }
        return sessionid;
    }

    public void disconnectCall() throws IOException {
        inputxml = "<NGODisconnectCabinet_Input>" + "  <Option>NGODisconnectCabinet</Option>" + "  <CabinetName>"
                + CommonFunctions.cabinetName + "</CabinetName>" + "  <UserDBId>" + sessionid + "</UserDBId>"
                + "</NGODisconnectCabinet_Input>";
        LogProcessing.xmllogs.info("Disconnect XML :: " + inputxml);
        outputxml = DMSCallBroker.execute(inputxml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
        LogProcessing.xmllogs.info("Disconnect Output XML :: " + outputxml);
        xmlResponse = new DMSXmlResponse(outputxml);
        if ("0".equalsIgnoreCase(xmlResponse.getVal("Status"))) {
            LogProcessing.summlogs.info("Cabinet Disconnected Successfully..!!!!!");
        } else {
            LogProcessing.errorlogs.info("Error in Disconnecting Cabinet..!!!!!");
        }
    }

    /*
     * (Not in use in this scheduler) public void forwardWI(String
     * processInstanceID, String activityid) throws IOException {
     * pendingpid.add(processInstanceID); inputxml = " <WMGetWorkItem_Input> " +
     * " <Option>WMGetWorkItem</Option>" + " <EngineName>" +
     * CommonFunctions.cabinetName + "</EngineName>" + " <SessionId>" +
     * sessionid + "</SessionId>" + " <ProcessInstanceId>" + processInstanceID +
     * "</ProcessInstanceId>" + " <WorkItemId>1</WorkItemId> " + "
     * </WMGetWorkItem_Input>"; LogProcessing.xmllogs.info("Get Work Item Input
     * :: " + inputxml); outputxml = DMSCallBroker.execute(inputxml,
     * CommonFunctions.serverIP, Short.parseShort("3333"), 0);
     * LogProcessing.xmllogs.info("Get Work Item Output XML :: " + outputxml);
     * xmlResponse = new DMSXmlResponse(outputxml); String status =
     * xmlResponse.getVal("MainCode"); if ("0".equalsIgnoreCase(status)) {
     * inputxml = "<WMAssignWorkItemAttributes_Input>" +
     * "<Option>WMAssignWorkItemAttributes</Option>" + "<EngineName>" +
     * CommonFunctions.cabinetName + "</EngineName>" + "<SessionId>" + sessionid
     * + "</SessionId>" + "<ProcessInstanceId>" + processInstanceID +
     * "</ProcessInstanceId>" + "<WorkItemId>1</WorkItemId>" + "<ActivityId>" +
     * activityid + "</ActivityId>" + "<ProcessDefId>" +
     * CommonFunctions.processdefid + "</ProcessDefId>" +
     * "<LastModifiedTime></LastModifiedTime>" +
     * "<ActivityType>10</ActivityType>" + "<complete>D</complete>" +
     * "<UserDefVarFlag>Y</UserDefVarFlag>" + "<Documents></Documents>" +
     * "<Attributes>" + attribute + "</Attributes>" +
     * "</WMAssignWorkItemAttributes_Input>"; LogProcessing.xmllogs.info("Assign
     * Work Item Input :: " + inputxml); outputxml =
     * DMSCallBroker.execute(inputxml, CommonFunctions.serverIP,
     * Short.parseShort("3333"), 0); LogProcessing.xmllogs.info("Assign Work
     * Item Output XML :: " + outputxml); xmlResponse = new
     * DMSXmlResponse(outputxml); String assignstatus =
     * xmlResponse.getVal("MainCode"); if ("0".equalsIgnoreCase(assignstatus)) {
     * WIStatus = "Success"; WIMessage = ""; successpid.add(processInstanceID);
     * } else { WIStatus = "Failure"; WIMessage = "Error in WMAssignWorkItem..."
     * + assignstatus + "..." + xmlResponse.getVal("Subject");
     * failurepid.put(processInstanceID, WIMessage);
     * LogProcessing.errorlogs.info("Error in WMAssignWorkItem for PID: " +
     * processInstanceID); } } else { WIStatus = "Failure"; WIMessage = "Error
     * in WMGetWorkItem ..." + status + "..." + xmlResponse.getVal("Subject");
     * failurepid.put(processInstanceID, WIMessage);
     * LogProcessing.errorlogs.info("Error in WMGetWorkItem for PID: " +
     * processInstanceID); }
     *
     * }
     */
    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

//    public void setSummaryLogs() {
//        LogProcessing.summlogs.info("*****************************************************************************************************************");
//        LogProcessing.summlogs.info("SYNC Supply Po GRN STARTTIME=" + StartDate);
//        LogProcessing.summlogs.info("*****************************************************************************************************************");
//        LogProcessing.summlogs.info("TotalProcessed=" + pendingpid.size());
//        LogProcessing.summlogs.info("TotalSuccess=" + successpid.size());
//        LogProcessing.summlogs.info("TotalFailure=" + failurepid.size());
//        LogProcessing.summlogs.info("ProcessedPid=" + pendingpid);
//        LogProcessing.summlogs.info("SuccessPid=" + successpid);
//        LogProcessing.summlogs.info("FailurePid=" + failurepid);
//        LogProcessing.summlogs.info("*****************************************************************************************************************");
//        LogProcessing.summlogs.info("SYNC Supply Po Parking END TIME=" + getCurrentDateTime());
//        LogProcessing.summlogs.info("*****************************************************************************************************************");
//        LogProcessing.summlogs.info("");
//
//    }
    public void setSummaryLogs(String activityType) {
        LogProcessing.summlogs.info("*****************************************************************************************************************");
        LogProcessing.summlogs.info("SYNC Invoice Po " + activityType + " STARTTIME=" + StartDate);
        LogProcessing.summlogs.info("*****************************************************************************************************************");
        LogProcessing.summlogs.info("TotalProcessed=" + pendingpid.size());
        LogProcessing.summlogs.info("TotalSuccess=" + successpid.size());
        LogProcessing.summlogs.info("TotalFailure=" + failurepid.size());
        LogProcessing.summlogs.info("ProcessedPid=" + pendingpid);
        LogProcessing.summlogs.info("SuccessPid=" + successpid);
        LogProcessing.summlogs.info("FailurePid=" + failurepid);
        LogProcessing.summlogs.info("*****************************************************************************************************************");
        LogProcessing.summlogs.info("SYNC Invocie Po " + activityType + " END TIME=" + getCurrentDateTime());
        LogProcessing.summlogs.info("*****************************************************************************************************************");
        LogProcessing.summlogs.info("");

    }

    public String sapInvokeXML(String funcName) {
        String sapinvokexml = "<WFSAPInvokeFunction_Input>"
                + "<Option>WFSAPInvokeFunction</Option>"
                + "<SAPConnect>"
                + "<SAPHostName>" + sapHostName + "</SAPHostName>"
                + "<SAPInstance>" + sapInstance + "</SAPInstance>"
                + "<SAPClient>800</SAPClient>"
                + "<SAPUserName>" + sapUserName + "</SAPUserName>"
                + "<SAPPassword>" + sapPassword + "</SAPPassword>"
                + "<SAPLanguage>EN</SAPLanguage>"
                + "</SAPConnect>"
                + "<SAPFunctionName>" + funcName + "</SAPFunctionName>";
        return sapinvokexml;
    }

    public void setException(String message) {
        attribute = "<ParkingSyncStatus>Failure</ParkingSyncStatus>"
                + "<ParkingSyncDate>" + currentDate + "</ParkingSyncDate>"
                + "<q1_exception_details>"
                + "<errormessage>" + message + "</errormessage>"
                + "<errordate>" + currentDate + "</errordate>"
                + "<resolutionstatus>False</resolutionstatus>"
                + "</q1_exception_details>";
    }

    public String uploadWI(String document, String recordId, String POReference, String PurchaseOrderNo, String InvoiceNumber, String plant, String invoicedate) throws ClassNotFoundException, SQLException {
        pendingpid.add(recordId);
        String processInstanceID = "", folderIndex = "";
        Statement st_sharepoint1 = null;
        Class.forName(CommonFunctions.databaseDriverClass);
        con = DriverManager.getConnection(CommonFunctions.databaseDriverSource, CommonFunctions.dbUserName,
                CommonFunctions.dbPassword);
        st_sharepoint1 = con.createStatement();
        try {
            inputxml = "<WFUploadWorkItem_Input>"
                    + "<Option>WFUploadWorkItem</Option>"
                    + "<EngineName>" + CommonFunctions.cabinetName + "</EngineName>"
                    + "<SessionId>" + CommonFunctions.sessionid + "</SessionId>"
                    + "<ProcessDefId>" + CommonFunctions.processdefid + "</ProcessDefId>"
                    + "<InitiateFromActivityId></InitiateFromActivityId>"
                    + "<DataDefName></DataDefName>"
                    + "<InitiateAlso>N</InitiateAlso>"
                    + "<UserDefVarFlag>Y</UserDefVarFlag>"
                    + "<SynchronousRouting>Y</SynchronousRouting>"
                    + "<Fields></Fields>"
                    + "<Documents>" + document + "</Documents>"
                    + "<Attributes>" + CommonFunctions.attribute.replace("%","") + "</Attributes>"
                    + "</WFUploadWorkItem_Input>";
            String newXML = inputxml.replace("&", " and ");


            LogProcessing.xmllogs.info("Upload Work Item Input :: " + newXML);
            outputxml = DMSCallBroker.execute(inputxml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("Upload Work Item Output XML :: " + outputxml);
            xmlResponse = new DMSXmlResponse(outputxml);
            String createstatus = xmlResponse.getVal("MainCode");
            if ("0".equalsIgnoreCase(createstatus)) {
                processInstanceID = xmlResponse.getVal("ProcessInstanceId");
                folderIndex = xmlResponse.getVal("FolderIndex");
                //System.out.println("WFUploadWorkItem Success" + processInstanceID);
                if (!processInstanceID.equalsIgnoreCase("NOT_ASSIGN")) {
                    String Query = "Update Ext_SharepointIntegration set ProcessInstanceId ='" + processInstanceID + "' where Record_Id='" + recordId + "'";
                    //System.out.println(Query);
                    // System.out.println("PID :- " + processInstanceID + " where  Record_Id :- " + recordId);
                    st_sharepoint1.executeUpdate(Query);

                    Query = "Update ext_serviceprocess set processid ='" + processInstanceID + "' where itemindex='" + folderIndex + "'";
                    //  System.out.println(Query);
                    st_sharepoint1.executeUpdate(Query);

                    Query = "update WFINSTRUMENTTABLE set var_str11 ='" + POReference + "' , var_str3 ='" + PurchaseOrderNo + "' , var_str4 ='" + InvoiceNumber + "' , var_str5 ='" + vendorName + "' , var_int1 =" + Integer.parseInt(plant) + ",var_date1 ='" + invoicedate + "' where ProcessInstanceID ='" + processInstanceID + "'";
                    // System.out.println(Query);
                    st_sharepoint1.executeUpdate(Query);
                }
                successpid.put("Workitem created for Record Id:- " + recordId, processInstanceID);
                return processInstanceID;
            } else {
                WIMessage = "Error in WFUploadWorkItem..." + createstatus;
                LogProcessing.errorlogs.info(WIMessage);
                failurepid.put("Workitem failed for Record Id:- " + recordId, WIMessage);
                //System.out.println("WFUploadWorkItem Fail");
                return "NOT_ASSIGN";
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            LogProcessing.errorlogs.info("Error in WFUploadWorkItem..." + e.getMessage());
            failurepid.put("Workitem failed for Record Id:- " + recordId, WIMessage);
            return "NOT_ASSIGN";
        }

    }

    public String addDocument(String documentName, String imageVolumeIndex) throws NGIMException {
        String sDocuments = "";

        String absoultePath = documentPath + documentName;
        // System.out.println("The PAth is Common :::"+absoultePath);
        // System.out.println(absoultePath);
        LogProcessing.xmllogs.info("Absolute Path is" + absoultePath);
        String volumeIndex = imageVolumeIndex;
        System.out.println("imageVolumeIndex ::- "+imageVolumeIndex);
        String docType = "Invoice";

        int docIndex;
        String isIndex = "", noOfPages = "1";
        try {
            JPDBRecoverDocData docDBData = new JPDBRecoverDocData();
            JPISIsIndex newIsIndex = new JPISIsIndex();

            File file1 = new File(absoultePath);

            String fileName = file1.getName();
            if (fileName.toLowerCase().endsWith(".pdf".toLowerCase())) {
                long sizeInBytes = file1.length();
                long sizeInMb = sizeInBytes / (1024 * 1024);
                if (sizeInBytes == 0) {
                    LogProcessing.errorlogs.info("Exception PDF found with size 0 " + fileName);
                    //System.out.println("Exception PDF found with size 0 " + fileName);
                    return null;
                }

                CPISDocumentTxn.AddDocument_MT(null, CommonFunctions.serverIP, Short.parseShort("3333"), CommonFunctions.cabinetName, Short.parseShort(volumeIndex), file1.getAbsolutePath(), docDBData, "1", newIsIndex);

                docIndex = newIsIndex.m_nDocIndex;
                isIndex = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;
                sDocuments = sDocuments + docType + (char) 21 + isIndex + (char) 21 + noOfPages + (char) 21 + file1.length() + (char) 21 + "pdf" + (char) 21 + "I" + (char) 21 + fileName + (char) 25;
                LogProcessing.summlogs.info(fileName + " - " + docType + " added in SMS successfully.");
                // System.out.println(fileName + " - " + docType + " added in SMS successfully.");
            } else {

                String docExt = "";
                if (file1.length() == 0) {
                    LogProcessing.errorlogs.info("File found with size 0." + fileName);
                    // System.out.println("File found with size 0 " + fileName);
                    return null;
                }

                if (fileName.toLowerCase().endsWith(".jpeg".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".jpeg".toLowerCase()));
                    docExt = "jpeg";

                } else if (fileName.toLowerCase().endsWith(".jpg".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".jpg".toLowerCase()));
                    docExt = "jpg";

                } else if (fileName.toLowerCase().endsWith(".png".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".png".toLowerCase()));
                    docExt = "png";

                } else if (fileName.toLowerCase().endsWith(".img".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".img".toLowerCase()));
                    docExt = "img";

                } else if (fileName.toLowerCase().endsWith(".xlsx".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".xlsx".toLowerCase()));
                    docExt = "xlsx";

                } else if (fileName.toLowerCase().endsWith(".xls".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".xls".toLowerCase()));
                    docExt = "xls";

                } else if (fileName.toLowerCase().endsWith(".docx".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".docx".toLowerCase()));
                    docExt = "docx";

                } else if (fileName.toLowerCase().endsWith(".doc".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".doc".toLowerCase()));
                    docExt = "doc";

                } else if (fileName.toLowerCase().endsWith(".zip".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".zip".toLowerCase()));
                    docExt = "zip";
                } else if (fileName.toLowerCase().endsWith(".rar".toLowerCase())) {
                    fileName = fileName.substring(0, fileName.toLowerCase().indexOf(".rar".toLowerCase()));
                    docExt = "rar";
                }

                System.out.println("docExt ::-" + docExt);
                CPISDocumentTxn.AddDocument_MT(null, CommonFunctions.serverIP, Short.parseShort("3333"), CommonFunctions.cabinetName, Short.parseShort(volumeIndex), file1.getAbsolutePath(), docDBData, "1", newIsIndex);
                docIndex = newIsIndex.m_nDocIndex;
                isIndex = newIsIndex.m_nDocIndex + "#" + newIsIndex.m_sVolumeId;
                System.out.println("isIndex ::- "+isIndex);
                sDocuments = sDocuments + docType + (char) 21 + isIndex + (char) 21 + noOfPages + (char) 21 + file1.length() + (char) 21 + docExt + (char) 21 + "I" + (char) 21 + fileName + (char) 25;
                LogProcessing.summlogs.info(fileName + " - " + docType + " added in SMS successfully.");
                System.out.println(fileName + " - " + docType + " added in SMS successfully.");

            }

            //  }
            // }
        } catch (JPISException e) {
            System.out.println(e);
             System.out.println("Exception is :::::" + e.getMessage());
            
            LogProcessing.errorlogs.info("Exception While Adding Document :::::::::::  " + e.getMessage());

        }



        return sDocuments;
    }

    public String calculateTax(String line_total, String TAX_CODE, String comp_code, String currency) {
        //VAL_Loccur//TAx_code

        String tax_val = "";
        try {
            if (line_total.contains(".") && !line_total.contains("E")) {
                int index_of = line_total.indexOf('.');
                int decLength = line_total.substring(index_of, line_total.length()).length();
                if (decLength > 2) {
                    line_total = line_total.substring(0, index_of + 3);
                } else {
                    line_total = line_total;
                }
            } else {
                try {
                    float linetotaltemp = Float.parseFloat(line_total);
                    //System.out.println("Line total temp : " + linetotaltemp);
                    BigDecimal decimal = new BigDecimal(linetotaltemp);
                    //System.out.println("Decimal-> " + decimal);
                    line_total = String.valueOf(decimal);
                } catch (Exception e) {
                    e.printStackTrace();
                    //System.out.println("Exception : " + e.getMessage());
                }
            }

            //System.out.println("Final value : " + line_total);


            String inputXml = sapInvokeXML("ZBAPI_AP_AUTOMATION_TAX_AMOUNT");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<COMP_CODE>" + comp_code + "</COMP_CODE>"
                    + "<TAX_CODE>" + TAX_CODE + "</TAX_CODE>"
                    + "<TAX_JURIS></TAX_JURIS>"
                    + "<CURR_KEY>" + currency + "</CURR_KEY>"
                    + "<NET_AMT>" + line_total + "</NET_AMT>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            //  System.out.println("Input xml: " + inputXml);


            LogProcessing.xmllogs.info("Calculate_TAX XML InputXML :: " + inputXml);
            outputxml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("Calculate_TAX XML outputxml:: " + outputxml);
            xmlResponse = new DMSXmlResponse(outputxml);

            if ("0".equalsIgnoreCase(xmlResponse.getVal("MainCode"))) {
                tax_val = xmlResponse.getVal("TAX_DEDUCT");
            } else {
                tax_val = "";
            }

        } catch (Exception e) {
            // System.out.println("Exception e is :::" + e);
            LogProcessing.errorlogs.info("Exception in Calculate_TAX XML  :: " + e.getMessage());
        }
        return tax_val;
    }
}
