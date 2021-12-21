/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapbapi;

import com.awc.methods.*;
import com.awc.sapBapi.VendorRegion;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Bapi_IncomingInvoice_Park {

    public String pushInvoiceForPark(String processInstanceID) throws SQLException {

        DMSXmlResponse xmlResponse;
        String Query;
        CommonFunctions objCommonFunctions = null;
        XMLParser xmlParser = new XMLParser();
        int difference;

        String inv_park = null, comp_code = null, po_type = null, plant1 = null, mdoc105 = null, ParkingSyncStatus = null;
        String message = "";
        String po_item = "", poitem1 = "", purchaseOrderNo = "";
        String itemdata = "";

        String materialdata = "", debitcredit = "", invoiceDate = "", postingDate = "", invoiceNo = "", currency = "", invoiceamount = "";
        String invmaterial = "", histmaterial = "", baseuom = "", baseuomiso = "", busPlc_val = "", vendor_code = "", tcs_flag = "", tcs = "";

        ResultSet RS_HeaderData = null, RS_PoLine = null, RS_PoHistory = null, RS_PoInvLine = null, RS_RptrINvoiceItem = null,RS_Update=null,RS_MATDOC=null;
        Statement ST_HeaderData = null, ST_PoLine = null, ST_PoHistory = null, ST_PoInvLine = null, ST_RptrINvoiceItem = null, ST_Update = null,ST_MATDOC=null;

        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String currentDate = date.toString();

        try {
            objCommonFunctions = new CommonFunctions();
            ST_HeaderData = objCommonFunctions.con.createStatement();
            ST_PoLine = objCommonFunctions.con.createStatement();
            ST_PoHistory = objCommonFunctions.con.createStatement();
            ST_PoInvLine = objCommonFunctions.con.createStatement();
            ST_RptrINvoiceItem = objCommonFunctions.con.createStatement();
            ST_Update = objCommonFunctions.con.createStatement();
            ST_MATDOC = objCommonFunctions.con.createStatement();

            Statement st_park = objCommonFunctions.con.createStatement();
            Statement st_updatepartk = objCommonFunctions.con.createStatement();
            Statement st_date = objCommonFunctions.con.createStatement();

            Query = "Select inv_park,comp_code,po_type,mdoc105,vendor_code,invoiceDate,invoiceNo,currency,invoiceamount,"
                    + "(select top 1 plant from complex_orient_invoice_item where pinstanceid =  processid) as plant, tsc_flag, tcs,purchaseorderno "
                    + "from ext_orientAP where processid = '" + processInstanceID + "'";
            //System.out.println("Query is :::::" + Query);
            RS_HeaderData = ST_HeaderData.executeQuery(Query);
            while (RS_HeaderData.next()) {
                inv_park = RS_HeaderData.getString(1);
                comp_code = RS_HeaderData.getString(2);
                po_type = RS_HeaderData.getString(3);
                mdoc105 = RS_HeaderData.getString(4);
                vendor_code = RS_HeaderData.getString(5);
                invoiceDate = RS_HeaderData.getString(6);
                invoiceNo = RS_HeaderData.getString(7);
                currency = RS_HeaderData.getString(8);
                invoiceamount = RS_HeaderData.getString(9);
                plant1 = RS_HeaderData.getString(10);
                tcs_flag = RS_HeaderData.getString(11);
                tcs = RS_HeaderData.getString(12);
                purchaseOrderNo = RS_HeaderData.getString(13);
            }



            if ("".equalsIgnoreCase(inv_park)
                    || "null".equalsIgnoreCase(inv_park)
                    || "NULL".equalsIgnoreCase(inv_park)
                    || inv_park == null) {

                //  System.out.println("processInstanceID is :" + processInstanceID);
                if (comp_code == null) {
                    message = "Please enter value for Company Code";
                    //System.out.println(message);

                    objCommonFunctions.setException(message);
                    LogProcessing.errorlogs.info(processInstanceID + " :- " + message);

                    return "Failure";
                }

                if ("ZPRT".equalsIgnoreCase(po_type)) {
                    debitcredit = "H";
                } else {
                    debitcredit = "S";
                }
//XML to creation get "Business Place"  
                busPlc_val = new BusinessPlaceBapi().getBussinessPlace(objCommonFunctions, plant1, xmlParser);

// XML creation for "ITEMDATA"
                Query = "select REF_DOC_NO, MATERIAL, QUANTITY, MAT_DOC, DOC_DATE,VAL_LOCCUR, MOVE_TYPE, PO_ITEM, REF_DOC_YR, "
                        + "REF_DOC_IT, TAX_CODE, REF_DOC,purchaseorder "
                        + "from complex_orient_poitem_history where pinstanceid = '" + processInstanceID + "' "
                        + " order by PO_ITEM";

                // System.out.println("Query History : " + Query);
                RS_PoHistory = ST_PoHistory.executeQuery(Query);

                Query = "Select PO_ITEM,PO_NUMBER,MATERIAL,PLANT,QUANTITY,BASE_UNIT,BASE_UOM_ISO,TAX_CODE from complex_orient_po_item where pinstanceid ='" + processInstanceID + "' ";
                RS_PoLine = ST_PoLine.executeQuery(Query);

                String po_unit = "", tax_code = "", po_unit_iso = "", item_no = "", val_type = "", po_number = "",
                        orderpr_un = "", orderpr_un_iso = "";

                for (int i = 0; RS_PoHistory.next(); i++) {
                    String invoicedocitem = String.valueOf(i + 1) + "0";
                    invoicedocitem = String.format("%0" + (6 - invoicedocitem.length()) + "d%s", 0, invoicedocitem);

                    String po_item_temp1 = RS_PoHistory.getString(8);

                    difference = 5 - po_item_temp1.length();
                    if (difference > 0) {
                        poitem1 = String.format("%0" + (5 - po_item_temp1.length()) + "d%s", 0, po_item_temp1);

                    } else {
                        poitem1 = RS_PoHistory.getString(8);
                    }

                    histmaterial = RS_PoHistory.getString(2);
                    String po_item_temp = RS_PoHistory.getString(8);
                    difference = 6 - po_item_temp.length();
                    if (difference > 0) {
                        po_item = String.format("%0" + (6 - po_item_temp.length()) + "d%s", 0, po_item_temp);
                    }


                    if ("105".equalsIgnoreCase(RS_PoHistory.getString(7))&& null == mdoc105) {
                        Query = "select distinct MAT_DOC from complex_orient_poitem_history where pinstanceid ='" + processInstanceID + "' and MOVE_TYPE='105'";
                        RS_MATDOC = ST_MATDOC.executeQuery(Query);
                        String mat_doc = null;
                       while(RS_MATDOC.next()){
                             mat_doc = RS_MATDOC.getString(1);
                         }
                        LogProcessing.xmllogs.info("Matdoc ::- " + mat_doc);
                        Query = "update ext_orientAP set mdoc105='"+mat_doc+"' where processid='" + processInstanceID + "'";
                        LogProcessing.xmllogs.info("Update Query is ::- " + Query);
                        LogProcessing.xmllogs.info("Update Result ::- " + ST_Update.executeUpdate(Query));
                        
                        Query = "Select mdoc105 from ext_orientAP where processid ='"+processInstanceID+"'";
                        RS_Update = ST_Update.executeQuery(Query);
                        while(RS_Update.next()){
                          mdoc105 =   RS_Update.getString(1);
                          LogProcessing.xmllogs.info("Update mdoc105 ::- " + mdoc105);
                        }
                    }
                    
                    String store_matdoc = mdoc105;
                    
                    
                    if (("105".equalsIgnoreCase(RS_PoHistory.getString(7))
                            && store_matdoc.equalsIgnoreCase(RS_PoHistory.getString(4)))
                            || "101".equalsIgnoreCase(RS_PoHistory.getString(7))) {
                        String parkrefdoc = "";
                        Query = "Select po.PO_UNIT_ISO,po.UNIT,po.TAX_CODE,po.PO_NUMBER,inv.valuation,po.ORDERPR_UN,"
                                + "po.ORDERPR_UN_ISO,inv.plant from "
                                + "complex_orient_po_item po, complex_orient_invoice_item inv  "
                                + "where po.pinstanceid = inv.pinstanceid "
                                + "and po.pinstanceid='" + processInstanceID + "' "
                                + "and po.MATERIAL='" + histmaterial + "' "
                                + "and po.PO_NUMBER = '" + (RS_PoHistory.getString(13)).substring(0, 10) + "'";

                        RS_PoInvLine = ST_PoInvLine.executeQuery(Query);

                        if (RS_PoInvLine.next()) {
                            po_unit_iso = RS_PoInvLine.getString(1);
                            po_unit = RS_PoInvLine.getString(2);
                            tax_code = RS_PoInvLine.getString(3);
                            po_number = RS_PoInvLine.getString(4);
                            parkrefdoc = RS_PoHistory.getString(12);

                            orderpr_un = RS_PoInvLine.getString(6);
                            orderpr_un_iso = RS_PoInvLine.getString(7);
                            String plant = RS_PoInvLine.getString(8);

                            String inputXmlValarea = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_VAL_AREA");
                            inputXmlValarea = inputXmlValarea + "<Parameters>"
                                    + "<ImportParameters>"
                                    + "<MATERIAL>" + histmaterial + "</MATERIAL>"
                                    + "<PLANT>" + plant + "</PLANT>"
                                    + "</ImportParameters>"
                                    + "</Parameters>"
                                    + "</WFSAPInvokeFunction_Input>";
                            LogProcessing.xmllogs.info("INPUT ZBAPI_AP_AUTOMATION_VAL_AREA " + processInstanceID + " ::- " + inputXmlValarea);

                            String outputXmlvalarea = DMSCallBroker.execute(inputXmlValarea, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                            xmlParser.setInputXML(outputXmlvalarea);
                            LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_VAL_AREA " + processInstanceID + " ::- " + outputXmlvalarea);
                            WFXmlResponse objXmlResponsevalarea = new WFXmlResponse(outputXmlvalarea);
                            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                                // System.out.println("Iside main code 0");
                                for (WFXmlList objList = objXmlResponsevalarea.createList("TableParameters", "TT_VAL_AREA"); objList.hasMoreElements(); objList.skip()) {
                                    // System.out.println("Inside for loop");
                                    val_type = objList.getVal("VAL_TYPE");
                                    //  System.out.println("Valtype value : " + val_type);
                                    break;
                                }
                            }
                            // System.out.println("Valtype value after break : " + val_type);
                            itemdata = itemdata + "<ITEMDATA>"
                                    + "<INVOICE_DOC_ITEM>" + invoicedocitem + "</INVOICE_DOC_ITEM>"
                                    + "<PO_NUMBER>" + po_number + "</PO_NUMBER>"
                                    + "<PO_UNIT>" + po_unit + "</PO_UNIT>"
                                    + "<PO_UNIT_ISO>" + po_unit_iso + "</PO_UNIT_ISO>"
                                    + "<PO_ITEM>" + poitem1 + "</PO_ITEM>"
                                    + "<REF_DOC>" + parkrefdoc + "</REF_DOC>"
                                    + "<VALUATION_TYPE>" + val_type + "</VALUATION_TYPE>"
                                    + "<REF_DOC_YEAR>" + RS_PoHistory.getString(9) + "</REF_DOC_YEAR>"
                                    + "<REF_DOC_IT>" + RS_PoHistory.getString(10) + "</REF_DOC_IT>"
                                    + "<ITEM_AMOUNT>" + RS_PoHistory.getString(6) + "</ITEM_AMOUNT>"
                                    + "<QUANTITY>" + RS_PoHistory.getString(3) + "</QUANTITY>"
                                    + "<PO_PR_UOM>" + orderpr_un + "</PO_PR_UOM>"
                                    + "<PO_PR_UOM_ISO>" + orderpr_un_iso + "</PO_PR_UOM_ISO>"
                                    + "<TAX_CODE>" + tax_code + "</TAX_CODE>";
                            // System.out.println("itemdata before pounit : " + itemdata);
                            itemdata = itemdata + "</ITEMDATA>";

                            for (int k = 0; RS_PoLine.next(); k++) {

                                if (RS_PoHistory.getString(8).equalsIgnoreCase(RS_PoLine.getString(1))
                                        && RS_PoHistory.getString(13).equalsIgnoreCase(RS_PoLine.getString(2))) {

                                    materialdata = materialdata + "<MATERIALDATA>"
                                            + "<INVOICE_DOC_ITEM>" + poitem1 + "</INVOICE_DOC_ITEM>"
                                            + "<MATERIAL>" + RS_PoLine.getString(3) + "</MATERIAL>"
                                            + "<VAL_AREA>" + RS_PoLine.getString(4) + "</VAL_AREA>"
                                            + "<DB_CR_IND>" + debitcredit + "</DB_CR_IND>"
                                            + "<VALUATION_TYPE>" + val_type + "</VALUATION_TYPE>"
                                            + "<QUANTITY>" + RS_PoLine.getString(5) + "</QUANTITY>"
                                            + "<BASE_UOM>" + RS_PoLine.getString(6) + "</BASE_UOM>"
                                            + "<BASE_UOM_ISO>" + RS_PoLine.getString(7) + "</BASE_UOM_ISO>";

                                    if (RS_PoLine.getString(8) == null) {
                                        materialdata = materialdata + "<TAX_CODE></TAX_CODE>"
                                                + "</MATERIALDATA>";
                                    } else {
                                        materialdata = materialdata + "<TAX_CODE>" + RS_PoLine.getString(8) + "</TAX_CODE>"
                                                + "</MATERIALDATA>";
                                    }
                                }
                            }
                        }
                    }
                }
                LogProcessing.xmllogs.info("itemdata xml " + processInstanceID + " ::- " + itemdata);
                LogProcessing.xmllogs.info("material data " + processInstanceID + " ::- " + materialdata);

                String withholdingtax = "";
                String linetotal = "";
                float line = 0.0f, lineTotal;

                Query = "Select line_total_wtax from complex_Orient_invoice_item where pinstanceid ='" + processInstanceID + "'";
                RS_RptrINvoiceItem = ST_RptrINvoiceItem.executeQuery(Query);
                while (RS_RptrINvoiceItem.next()) {
                    linetotal = RS_RptrINvoiceItem.getString(1);
                    line = line + Float.parseFloat(linetotal);
                }

                String inputXml_TDS = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_TDS");
                inputXml_TDS = inputXml_TDS + "<Parameters>"
                        + "<ImportParameters>"
                        + "<VENDOR>" + vendor_code + "</VENDOR>"
                        + "<COMP_CODE>1000</COMP_CODE>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";

                LogProcessing.xmllogs.info(" INPUT ZBAPI_AP_AUTOMATION_TDS " + processInstanceID + " ::- " + inputXml_TDS);

                String outputXml_TDS = DMSCallBroker.execute(inputXml_TDS, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                xmlParser.setInputXML(outputXml_TDS);
                LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_TDS " + processInstanceID + " ::- " + outputXml_TDS);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml_TDS);

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TDS_TAB"); objList.hasMoreElements(); objList.skip()) {
                        String COUNTRY = objList.getVal("COUNTRY");
                        String WITHT = objList.getVal("WITHT");
                        String WT_WITHCD = objList.getVal("WT_WITHCD");
                        String QSATZ = objList.getVal("QSATZ");
                        String DESC = objList.getVal("DESC");

                        float wi_percent = Float.parseFloat(QSATZ);
                        float wi_base_amount = (wi_percent / 100) * line;
                        withholdingtax = withholdingtax + "<WITHTAXDATA>"
                                + "<SPLIT_KEY>000001</SPLIT_KEY>"
                                + "<WI_TAX_TYPE>" + WITHT + "</WI_TAX_TYPE>"
                                + "<WI_TAX_CODE>" + WT_WITHCD + "</WI_TAX_CODE>"
                                + "<WI_TAX_BASE>" + Math.round(Math.ceil(line)) + "</WI_TAX_BASE>"
                                + "<WI_TAX_AMT>" + Math.round(Math.ceil(wi_base_amount)) + "</WI_TAX_AMT>"
                                + "</WITHTAXDATA>";

                    }

                    LogProcessing.xmllogs.info("Withholdingtax XML " + processInstanceID + " ::- " + withholdingtax);
                }
                //******WithHolding Tax********//
//                Double inv_amt = new Double(invoiceAmount);
//                DecimalFormat df = new DecimalFormat("#.00");
                String glaccountdata = "";
                if ("Yes".equalsIgnoreCase(tcs_flag)) {
                    glaccountdata = "<GLACCOUNTDATA>"
                            + "<INVOICE_DOC_ITEM>000001</INVOICE_DOC_ITEM>"
                            + "<GL_ACCOUNT>0000212003</GL_ACCOUNT>"
                            + "<ITEM_AMOUNT>" + tcs + "</ITEM_AMOUNT>"
                            + "<COMP_CODE>" + comp_code + "</COMP_CODE>"
                            + "<DB_CR_IND>S</DB_CR_IND>"
                            + "</GLACCOUNTDATA>";
                }
                LogProcessing.xmllogs.info("GLACCOUNTDATA XML " + processInstanceID + " ::- " + glaccountdata);
                String vendor_region = new VendorRegion().getVendorRegion(purchaseOrderNo);//===Vender REgion====//
                LogProcessing.xmllogs.info("Vednor Region for ::: " + processInstanceID + " ::- " + vendor_region);
                String inputXml_PARK = objCommonFunctions.sapInvokeXML("BAPI_INCOMINGINVOICE_PARK");
                inputXml_PARK = inputXml_PARK + "<Parameters><ImportParameters>"
                        + "<HEADERDATA>"
                        + "<INVOICE_IND>X</INVOICE_IND>"
                        + "<DOC_DATE>" + invoiceDate + "</DOC_DATE>"
                        + "<PSTNG_DATE>" + currentDate + " 00:00:00.0</PSTNG_DATE>"
                        // + "<PSTNG_DATE>2020-09-30 00:00:00.0</PSTNG_DATE>"
                        + "<REF_DOC_NO>" + invoiceNo + "</REF_DOC_NO>"
                        + "<SIMULATION>X</SIMULATION>"
                        + "<COMP_CODE>" + comp_code + "</COMP_CODE>"
                        + "<SECCO>" + comp_code + "</SECCO>"
                        + "<CURRENCY>" + currency + "</CURRENCY>"
                        + "<GROSS_AMOUNT>" + invoiceamount + "</GROSS_AMOUNT>"
                        + "<HEADER_TXT>" + objCommonFunctions.dmsUserName + "</HEADER_TXT>"
                        + "<BUSINESS_PLACE>" + busPlc_val + "</BUSINESS_PLACE>"
                        + "<CALC_TAX_IND>X</CALC_TAX_IND>"
                        + "<GST_PARTNER>" + vendor_code + "</GST_PARTNER>"
                        + "<PLACE_OF_SUP>" + vendor_region + "</PLACE_OF_SUP>"
                        + "</HEADERDATA>"
                        + "</ImportParameters>"
                        + "<TableParameters>"
                        + itemdata
                        + materialdata
                        + withholdingtax
                        + glaccountdata
                        + "</TableParameters></Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                inputXml_PARK = inputXml_PARK.replace("null", "");
                inputXml_PARK = inputXml_PARK.replace("NULL", "");

                LogProcessing.xmllogs.info(" Input Ivoice Bapi " + processInstanceID + " ::- " + inputXml_PARK);
                String outputXml_PARK = DMSCallBroker.execute(inputXml_PARK, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                xmlParser.setInputXML(outputXml_PARK);
                LogProcessing.xmllogs.info(" Output Ivoice Bapi " + processInstanceID + " ::- " + outputXml_PARK);
                WFXmlResponse objXmlResponse_PARK = new WFXmlResponse(outputXml_PARK);

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    String INVOICEDOCNUMBER = xmlParser.getValueOf("INVOICEDOCNUMBER");

                    if ("".equalsIgnoreCase(INVOICEDOCNUMBER)) {
                        message = xmlParser.getValueOf("MESSAGE");
                        LogProcessing.errorlogs.info("Error while parking " + processInstanceID + " ::- " + message);
                        objCommonFunctions.setException(message);
                        ParkingSyncStatus = "Failure";
                    } else {
                        String FISCALYEAR = xmlParser.getValueOf("FISCALYEAR");
                        BigDecimal bd_invociamount = new BigDecimal(invoiceamount);
                        String parkupdate = "update ext_orientAP set inv_park='" + INVOICEDOCNUMBER + "' ,fiscalyear ='" + FISCALYEAR + "' ,invoiceamount_int ='" + bd_invociamount + "' where processid='" + processInstanceID + "'";
                        System.out.println("Update Query is :: " + parkupdate);
                        LogProcessing.summlogs.info("Parking Number Update for ProcessID " + processInstanceID + " is " + INVOICEDOCNUMBER);
                        st_updatepartk.executeUpdate(parkupdate);

                        String inputXml_getFinalDoc = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_ACC_DOC_NO");
                        inputXml_getFinalDoc = inputXml_getFinalDoc + "<Parameters><ImportParameters>"
                                + "<LV_REFKEY>" + INVOICEDOCNUMBER + FISCALYEAR + "</LV_REFKEY>"
                                + "</ImportParameters>"
                                + "</Parameters>"
                                + "</WFSAPInvokeFunction_Input>";
                        String outputXml_getFinalDoc = DMSCallBroker.execute(inputXml_getFinalDoc, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                        xmlParser.setInputXML(outputXml_getFinalDoc);
                        WFXmlResponse objXmlResponse_getFinalDoc = new WFXmlResponse(outputXml_getFinalDoc);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            String AC_DOC_NO = xmlParser.getValueOf("AC_DOC_NO");
                            if (!AC_DOC_NO.equalsIgnoreCase("")) {
                                //Query to update inv_park_sap-----------Saurish
                                Query = "update ext_orientAP set inv_park_sap='" + AC_DOC_NO + "',accounts_decision='Park' where processid='" + processInstanceID + "'";
                                LogProcessing.xmllogs.info("Park Update Query ::: " + Query);
                                st_park.executeUpdate(Query);
                                Query = "update ext_orientAP set parkingSyncDate='" + currentDate + "' where processid='" + processInstanceID + "'";
                                st_date.executeUpdate(Query);
                                //-----------------------------------

                                // objCommonFunctions.attribute = objCommonFunctions.attribute + "<parkingSyncStatus>Success</parkingSyncStatus><parkingSyncDate>" + currentDate + "</parkingSyncDate><inv_park>" + INVOICEDOCNUMBER + "</inv_park><inv_park_sap>" + AC_DOC_NO + "</inv_park_sap><accounts_decision>Park</accounts_decision>";
                                //  System.out.println("Invoice Document Number :" + INVOICEDOCNUMBER);
                                ParkingSyncStatus = "Success";
                            } else {
                                message = "Error while fetching parked document number";
                                //System.out.println(message);
                                LogProcessing.errorlogs.info(message + " ::- " + processInstanceID);

                                objCommonFunctions.setException(message);

                                ParkingSyncStatus = "Failure";
                            }
                        }
                    }
                } else {
                    message = xmlParser.getValueOf("Description");
                    LogProcessing.errorlogs.info(message + " ::- " + processInstanceID);
                    objCommonFunctions.setException(message);
                    ParkingSyncStatus = "Failure";
                }
            } else {
                message = "Parking is already done for this Purchase order and Invoice. Invoice Document Number is : " + inv_park;
                // System.out.println(message);
                LogProcessing.errorlogs.info(message + " ::- " + processInstanceID);

                objCommonFunctions.setException(message);
                ParkingSyncStatus = "Failure";
            }
            st_updatepartk.close();
            st_park.close();
            st_date.close();
        } catch (Exception e) {
            e.printStackTrace();

            message = e.getMessage();
            objCommonFunctions.setException(message);
            LogProcessing.errorlogs.info("Exception in Bapi_IncomingInvoice_Park " + processInstanceID + " ::- " + e.getMessage());

            ParkingSyncStatus = "Failure";

        }
        try {
            //Query to update synch Status------Saurish------
            Statement st_synch = objCommonFunctions.con.createStatement();
            Query = "update ext_orientAP set parkingSyncStatus='" + ParkingSyncStatus + "'  where processid='" + processInstanceID + "'";
            st_synch.executeUpdate(Query);
            st_synch.close();
            //-------------------------------------------------
        } catch (Exception e) {
            LogProcessing.errorlogs.info("Error in Updating Parking Status to table::::Parking Status :::::" + ParkingSyncStatus);
        }

        return ParkingSyncStatus;
    }
}
