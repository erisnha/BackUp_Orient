/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapbapi;

import com.awc.methods.*;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author dms
 */
public class Bapi_QualityCheck {

    String Query = null;
    CommonFunctions objCommonFunctions = null;
    private DMSXmlResponse xmlResponse;

    public String qualityCheck(String processInstanceID) {

        objCommonFunctions = new CommonFunctions();
        XMLParser xmlParser = new XMLParser();
        int countA = 0, countR = 0;
        String Query = null, material = "", mat_doc = "", po_number = "", message = "", invoiceno = "";
        ResultSet RS_CHECK = null, RS_CHECK1 = null, RS_setmdoc = null;
        Statement ST_CHECK = null, ST_CHECK1 = null, ST_setmdoc = null;
        try {
            ST_CHECK = objCommonFunctions.con.createStatement();
            ST_CHECK1 = objCommonFunctions.con.createStatement();
            ST_setmdoc = objCommonFunctions.con.createStatement();

            Query = "select mdoc105,PurchaseOrderNo,invoiceno from ext_orientAP where processid ='" + processInstanceID + "'";
            RS_CHECK = ST_CHECK.executeQuery(Query);
            while (RS_CHECK.next()) {
                mat_doc = RS_CHECK.getString(1);
                po_number = RS_CHECK.getString(2);
                invoiceno = RS_CHECK.getString(3);
            }
            // System.out.println("mdoc is"+mat_doc);
            if (mat_doc == null || mat_doc == "NULL") {
                // System.out.println(processInstanceID);
                String inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_PO_HIST");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PO_NUM>" + po_number + "</PO_NUM>"
                        + "<INV_NUM>" + invoiceno.toUpperCase() + "</INV_NUM>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                LogProcessing.xmllogs.info("INPUT ZBAPI_AP_AUTOMATION_PO_HIST " + processInstanceID + " ::-" + inputXml);
                String outputxml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_PO_HIST " + processInstanceID + " ::-" + outputxml);
                xmlResponse = new DMSXmlResponse(outputxml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputxml);
                if (xmlResponse.getVal("MainCode").equalsIgnoreCase("0")) {
                    if (xmlResponse.getVal("TYPE").equalsIgnoreCase("S")) {
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_HISTORY"); objList.hasMoreElements(); objList.skip()) {

                            mat_doc = objList.getVal("BELNR");


                            Query = "update ext_orientAP set mdoc105 = '" + mat_doc + "' where processid ='" + processInstanceID + "'";
                            //   System.out.println(Query);
                            ST_CHECK.executeUpdate(Query);
                        }

                    }
                }
                
            }

            Query = "select mdoc105 from ext_orientAP where processid ='" + processInstanceID + "'";

            RS_setmdoc = ST_setmdoc.executeQuery(Query);
            while (RS_setmdoc.next()) {
                mat_doc = RS_setmdoc.getString(1);
            }
            // System.out.println(mat_doc);

            if ("".equalsIgnoreCase(mat_doc) || mat_doc == null) {

                objCommonFunctions.setException("migo105 not done");
                //objCommonFunctions.attribute = objCommonFunctions.attribute + "<quality_decision>migo105 not done</quality_decision>";
                return "migo105 not done";
            }

            Query = "select item from complex_orient_invoice_item where pinstanceid ='" + processInstanceID + "'";
            // System.out.println("Query is ::" + Query);
            RS_CHECK1 = ST_CHECK1.executeQuery(Query);
            while (RS_CHECK1.next()) {
                material = RS_CHECK1.getString(1);

                String inputXml = objCommonFunctions.sapInvokeXML("BAPI_INSPLOT_GETLIST");
                inputXml = inputXml + "<Parameters>" + "<ImportParameters>"
                        + "<MATERIAL>" + material + "</MATERIAL>"
                        + "<MAT_DOC>" + mat_doc + "</MAT_DOC>"
                        // 10-FEB-2021 + "<PO_NUMBER>" + po_number + "</PO_NUMBER>"
                        + "<PO_NUMBER></PO_NUMBER>"
                        + "</ImportParameters>" + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";

                //  System.out.println("BAPI_INSPLOT_GETLIST inputxml : " + inputXml);
                LogProcessing.xmllogs.info("BAPI_INSPLOT_GETLIST inputxml " + processInstanceID + " : " + inputXml);


                String outputXml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                xmlParser.setInputXML(outputXml);
                LogProcessing.xmllogs.info("OUTPUT BAPI_INSPLOT_GETLIST " + processInstanceID + " : " + outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                    String inputXml1 = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_QI_STATUS");
                    inputXml1 = inputXml1 + "<Parameters>"
                            + "<ImportParameters>"
                            + "<INSP_LOT>" + xmlParser.getValueOf("INSPLOT") + "</INSP_LOT>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";

                    LogProcessing.xmllogs.info("INPUT ZBAPI_AP_AUTOMATION_QI_STATUS " + processInstanceID + " : " + inputXml1);
                    String outputXml1 = DMSCallBroker.execute(inputXml1, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                    xmlParser.setInputXML(outputXml1);
                    LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_QI_STATUS " + processInstanceID + " : " + outputXml1);
                    WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXml1);


                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                        String MESSAGE = xmlParser.getValueOf("MESSAGE");
                        //System.out.println("MESSAGE Value : " + MESSAGE);

                        String VCODE = xmlParser.getValueOf("VCODE");
                        // System.out.println("VCODE Value : " + VCODE);

                        if (MESSAGE.equalsIgnoreCase("QI not done")) {
                            message = "Kindly Perform Quality Assurance before proceeding further for workitem " + processInstanceID;
                            //   System.out.println(message);
                            objCommonFunctions.setException(message);
                            LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                            return "Failure";
                        }
                        if (VCODE.equalsIgnoreCase("A")) {
                            countA++;
                        } else {
                            countR++;
                        }

                    } else {
                        message = "Error in ZBAPI_AP_AUTOMATION_QI_STATUS";
                        // System.out.println(message);
                        LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                        objCommonFunctions.setException(message);
                        return "Failure";
                    }
                }
                else {
                    message = "Error in BAPI_INSPLOT_GETLIST";
                    //System.out.println(message);
                    LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                    objCommonFunctions.setException(message);
                    return "Failure";
                }

            }

            if (countA > countR) {
                message = "Quality_decision is : Quality Accepted";
                //System.out.println(message + " for " + processInstanceID);
                LogProcessing.errorlogs.info(processInstanceID + " :- " + message);

                objCommonFunctions.attribute = objCommonFunctions.attribute + "<quality_decision>Quality Accepted</quality_decision>";
                return "Success";


            } else if (countA == countR) {
                message = "Quality_decision is : Quality Rejected";
                // System.out.println(message + " for " + processInstanceID);
                LogProcessing.errorlogs.info(processInstanceID + " :- " + message);

                objCommonFunctions.attribute = objCommonFunctions.attribute + "<quality_decision>Quality Rejected</quality_decision>";
                return "Success";

            } else {
                message = "quality_decision is : Quality Rejected";
                // System.out.println(message + " for " + processInstanceID);
                LogProcessing.errorlogs.info(processInstanceID + " :- " + message);

                objCommonFunctions.attribute = objCommonFunctions.attribute + "<quality_decision>Quality Rejected</quality_decision>";
                return "Success";
            }


        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            //System.out.println(message);
            LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
            objCommonFunctions.setException(message);
            return "Failure";
        }

    }
}
