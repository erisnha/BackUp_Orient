/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapbapi;

import com.awc.methods.CommonFunctions;
import com.awc.methods.LogProcessing;
import com.awc.methods.WFXmlList;
import com.awc.methods.WFXmlResponse;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PoHistory {

    public String getsetPoHistory(String processInstanceID) throws SQLException {

        System.out.println("inside getsetPoHistory");
        DMSXmlResponse xmlResponse = null;
        String Query = null;
        CommonFunctions objCommonFunctions = null;
        int counter = 0;
        ResultSet RS_Invoice = null, RS_PONumber = null;
        Statement ST_Invoice = null, ST_PONumber = null, stmt = null;
        String setHistoryStatus = null;

        try {
            objCommonFunctions = new CommonFunctions();
            stmt = objCommonFunctions.con.createStatement();
            ST_Invoice = objCommonFunctions.con.createStatement();
            ST_PONumber = objCommonFunctions.con.createStatement();
            System.out.println("processInstanceID before delete query -= " + processInstanceID);
            stmt.executeUpdate("delete from complex_orient_poitem_history where pinstanceid = '" + processInstanceID + "'");

            Query = "Select InvoiceNo from ext_orientAP where processid = '" + processInstanceID + "'";
            System.out.println("Query = " + Query);
            RS_Invoice = ST_Invoice.executeQuery(Query);
            String invoiceno = null;
            while (RS_Invoice.next()) {
                invoiceno = RS_Invoice.getString(1);
            }
            Query = "select ponumber from complex_orient_multiplepo where pinstanceid = '" + processInstanceID + "'"
                    + " union "
                    + "select PurchaseOrderNo from ext_orientAP where processid = '" + processInstanceID + "' ";
            System.out.println("Po query " + Query);
            RS_PONumber = ST_PONumber.executeQuery(Query);
            while (RS_PONumber.next()) {
                String ponumber = RS_PONumber.getString(1);
                System.out.println("PO Number ::::" + ponumber);
                String inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_PO_HIST");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PO_NUM>" + ponumber + "</PO_NUM>"
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
                            System.out.println("Inside for loop po history");
                            String ref_doc_no = objList.getVal("XBLNR");
                            String material = objList.getVal("MATNR");
                            String quantity = objList.getVal("MENGE");
                            String mat_doc = objList.getVal("BELNR");
                            String doc_date = objList.getVal("BLDAT");
                            String val_loccur = objList.getVal("WRBTR");
                            String move_type = objList.getVal("BWART");
                            String po_item = objList.getVal("EBELP");
                            String ref_doc_yr = objList.getVal("LFGJA");
                            String ref_doc_it = objList.getVal("LFPOS");
                            String tax_code = objList.getVal("MWSRZ");
                            String ref_doc = objList.getVal("LFBNR");

                            Query = "Insert into complex_orient_poitem_history (pinstanceid,REF_DOC_NO, MATERIAL, QUANTITY, MAT_DOC, DOC_DATE, "
                                    + "VAL_LOCCUR, MOVE_TYPE, PO_ITEM, REF_DOC_YR, REF_DOC_IT, TAX_CODE, REF_DOC,purchaseorder) "
                                    + "values('" + processInstanceID + "','" + ref_doc_no + "','" + material + "','" + quantity + "','" + mat_doc + "','" + doc_date + "',"
                                    + "'" + val_loccur + "','" + move_type + "','" + po_item + "','" + ref_doc_yr + "','" + ref_doc_it + "',"
                                    + "'" + tax_code + "','" + ref_doc + "','" + ponumber + "')";
                            System.out.println("Query - " + Query);
                            stmt.execute(Query);
                        }
                        counter++;
                        setHistoryStatus = "Success";

                    }
                } else if (xmlResponse.getVal("MainCode").equalsIgnoreCase("400")) {
                    LogProcessing.errorlogs.info("Error from ZBAPI_AP_AUTOMATION_PO_HIST ::- Main Code is 400");
                    LogProcessing.errorlogs.info(xmlResponse.getVal("Description"));

                    System.exit(0);

                } else {
                    objCommonFunctions.setException("ZBAPI_AP_AUTOMATION_PO_HIST get FAILS");
                    LogProcessing.errorlogs.info("ZBAPI_AP_AUTOMATION_PO_HIST get FAILS ::- " + processInstanceID);
                    setHistoryStatus = "Failure";
                    return setHistoryStatus;
                }
            }

            if (counter == 0) {
                objCommonFunctions.setException("PO history record not found. Kindly validate PO Number, Invoice Number and GRN details.");
                LogProcessing.errorlogs.info("PO history record not found. Kindly validate PO Number, Invoice Number and GRN details.  ::- " + processInstanceID);
                setHistoryStatus = "Failure";
            }
        } catch (Exception e) {
            e.printStackTrace();

            LogProcessing.errorlogs.info("Exception PO_HISTORY " + processInstanceID + " ::-" + e.getMessage());
            objCommonFunctions.setException(e.getMessage());
            setHistoryStatus = "Failure";
        } finally {
            RS_Invoice.close();
            RS_PONumber.close();
            ST_Invoice.close();
            ST_PONumber.close();
            stmt.close();
        }
        return setHistoryStatus;
    }
}