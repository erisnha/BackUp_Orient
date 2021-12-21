/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapBapi;

import com.awc.methods.*;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;

/**
 *
 * @author dms
 */
public class Bapi_Gstn_Pan_Tds {

    DMSXmlResponse xmlResponse;
    CommonFunctions objCommonFunctions = new CommonFunctions();
    XMLParser xmlParser = new XMLParser();
    String inputXml, outputxml;

    public String getVendorGSTN(String VendorCode) {
        String gstn = "";

        try {
            inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_VENDOR_DET");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<VENDOR>" + VendorCode + "</VENDOR>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";


            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_VENDOR_DET Input :: " + inputXml);
            outputxml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_VENDOR_DET Output XML :: " + outputxml);
            xmlResponse = new DMSXmlResponse(outputxml);

            if ("0".equalsIgnoreCase(xmlResponse.getVal("MainCode"))) {
                gstn = xmlResponse.getVal("GST_NO");
            }
            if (xmlResponse.getVal("MainCode").equalsIgnoreCase("400")) {
                    LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                    LogProcessing.errorlogs.info(xmlResponse.getVal("Description"));
                     if(xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist"))
                    {
                    System.exit(0);
                    }
                }
        } catch (Exception e) {
            // System.out.println("Exception is :::" + e.getMessage());
            LogProcessing.errorlogs.info("Exception From getVendorGSTN :: " + e.getMessage());

        }
        return gstn;
    }

    public String getVendorPAN(String VendorCode) {
        String pan = "";
        try {
            inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_VENDOR_PAN");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<VENDOR>" + VendorCode + "</VENDOR>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";


            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_VENDOR_PAN Input :: " + inputXml);
            outputxml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_VENDOR_PAN Output XML :: " + outputxml);
            xmlResponse = new DMSXmlResponse(outputxml);

            if ("0".equalsIgnoreCase(xmlResponse.getVal("MainCode"))) {
                pan = xmlResponse.getVal("PAN_NO");
            }
            if (xmlResponse.getVal("MainCode").equalsIgnoreCase("400")) {
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                LogProcessing.errorlogs.info(xmlResponse.getVal("Description"));
                 if(xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist"))
                    {
                    System.exit(0);
                    }
            }
        } catch (Exception e) {
            //System.out.println("Exception is :::" + e.getMessage());
            LogProcessing.errorlogs.info("Exception From getVendorPAN :: " + e.getMessage());
        }
        return pan;
    }

    public void getTDS(String venderCode) {

        try {
            inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_TDS");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<VENDOR>" + venderCode + "</VENDOR>"
                    + "<COMP_CODE>1000</COMP_CODE>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";

            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_TDS Input :: " + inputXml);
            outputxml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("ZBAPI_AP_AUTOMATION_TDS Output XML :: " + outputxml);

            xmlParser.setInputXML(outputxml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputxml);

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                String xmlnew = "";
                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TDS_TAB"); objList.hasMoreElements(); objList.skip()) {
                    String DESC = objList.getVal("DESC");
                    String COUNTRY = objList.getVal("COUNTRY");
                    String WITHT = objList.getVal("WITHT");
                    String WT_WITHCD = objList.getVal("WT_WITHCD");
                    String QSATZ = objList.getVal("QSATZ");

                    objCommonFunctions.attribute = objCommonFunctions.attribute + "<q_orient_witholding>"
                            + "<applied>Yes</applied>"
                            + "<amount>0</amount>"
                            + "<country>" + COUNTRY + "</country>"
                            + "<tax_type>" + WITHT + "</tax_type>"
                            + "<tax_code>" + WT_WITHCD + "</tax_code>"
                            + "<tax>" + QSATZ + "</tax>"
                            + "<description>" + DESC + "</description>"
                            + "</q_orient_witholding>";

                }
            }
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("400")) {
                    LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                    LogProcessing.errorlogs.info(xmlParser.getValueOf("Description"));
                     if(xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist"))
                    {
                    System.exit(0);
                    }
                }

        } catch (Exception e) {
            // System.out.println("Exception is ::::" + e.getMessage());
            LogProcessing.errorlogs.info("Exception in ServiceTDS ::::" + e.getMessage());
        }
    }
}
