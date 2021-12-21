package com.awc.methods;

import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

public class CommonFunctions {

    public static String cabinetName = null, processdefid = null, sessionid = null, serverIP = null,
            serverPort = null, dbUserName = null, dbPassword = null, databaseDriverSource = null,
            databaseDriverClass = null, dmsUserName = null, dmsPassword = null, sapHostName = null, sapInstance = null, inputxml = null, outputxml = null, serviceprocessdefid = null,
            StartDate = null, WIStatus, WIMessage, attribute = "", service_parkingactivityid = null, service_postingactivityid = null, sapUserName = null, sapPassword = null;
    static DMSXmlResponse xmlResponse;
    public static Connection con = null;
    public static HashSet<String> pendingpid = new HashSet();
    public static HashSet<String> successpid = new HashSet();
    public static HashMap failurepid = new HashMap();
    
    long millis = System.currentTimeMillis();
    java.sql.Date date = new java.sql.Date(millis);
    String currentDate = date.toString();

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

        //  processdefid = prop.getProperty("SupplyPoProcessDefID");
        processdefid = prop.getProperty("OrientAPProcessDefID");
        serviceprocessdefid = prop.getProperty("ServiceProcessDefID");
        service_parkingactivityid = prop.getProperty("SEVICEPROCESS_SchedulerParkingActivityID");
        service_postingactivityid = prop.getProperty("SEVICEPROCESS_SchedulerPostingActivityID");



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

    public void forwardWI(String processInstanceID, String activityid) throws IOException {
        pendingpid.add(processInstanceID);
        inputxml = " <WMGetWorkItem_Input> " + " <Option>WMGetWorkItem</Option>" + " <EngineName>"
                + CommonFunctions.cabinetName + "</EngineName>" + " <SessionId>" + sessionid + "</SessionId>"
                + " <ProcessInstanceId>" + processInstanceID + "</ProcessInstanceId>" + " <WorkItemId>1</WorkItemId> "
                + " </WMGetWorkItem_Input>";
        LogProcessing.xmllogs.info("Get Work Item Input :: " + inputxml);
        outputxml = DMSCallBroker.execute(inputxml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
        LogProcessing.xmllogs.info("Get Work Item Output XML :: " + outputxml);
        xmlResponse = new DMSXmlResponse(outputxml);
        String status = xmlResponse.getVal("MainCode");
        if ("0".equalsIgnoreCase(status)) {
            inputxml = "<WMAssignWorkItemAttributes_Input>" + "<Option>WMAssignWorkItemAttributes</Option>"
                    + "<EngineName>" + CommonFunctions.cabinetName + "</EngineName>" + "<SessionId>" + sessionid
                    + "</SessionId>" + "<ProcessInstanceId>" + processInstanceID + "</ProcessInstanceId>"
                    + "<WorkItemId>1</WorkItemId>" + "<ActivityId>" + activityid + "</ActivityId>"
                    + "<ProcessDefId>" + CommonFunctions.processdefid + "</ProcessDefId>"
                    + "<LastModifiedTime></LastModifiedTime>" + "<ActivityType>10</ActivityType>"
                    + "<complete>D</complete>" + "<UserDefVarFlag>Y</UserDefVarFlag>" + "<Documents></Documents>"
                    + "<Attributes>" + attribute + "</Attributes>" + "</WMAssignWorkItemAttributes_Input>";
            LogProcessing.xmllogs.info("Assign Work Item Input :: " + inputxml);
            outputxml = DMSCallBroker.execute(inputxml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("Assign Work Item Output XML :: " + outputxml);
            xmlResponse = new DMSXmlResponse(outputxml);
            String assignstatus = xmlResponse.getVal("MainCode");
            if ("0".equalsIgnoreCase(assignstatus)) {
                WIStatus = "Success";
                WIMessage = "";
                successpid.add(processInstanceID);
            } else {
                WIStatus = "Failure";
                WIMessage = "Error in WMAssignWorkItem..." + assignstatus + "..." + xmlResponse.getVal("Subject");
                failurepid.put(processInstanceID, WIMessage);
                LogProcessing.errorlogs.info("Error in WMAssignWorkItem for PID: " + processInstanceID);
            }
        } else {
            WIStatus = "Failure";
            WIMessage = "Error in WMGetWorkItem ..." + status + "..." + xmlResponse.getVal("Subject");
            failurepid.put(processInstanceID, WIMessage);
            LogProcessing.errorlogs.info("Error in WMGetWorkItem for PID: " + processInstanceID);
        }

    }

    public String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }


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
        attribute = attribute + "<ParkingSyncStatus>Failure</ParkingSyncStatus>"
                + "<ParkingSyncDate>" + currentDate + "</ParkingSyncDate>"
                + "<q_exception_details>"
                + "<errormessage>" + message + "</errormessage>"
                + "<errordate>" + currentDate + "</errordate>"
                + "<resolutionstatus>False</resolutionstatus>"
                + "</q_exception_details>";
    }
}
