/*
 * 
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

/**
 *
 * @author dms
 */
public class Bapi_Service_Park {

    DMSXmlResponse xmlResponse;
    String Query, Query1;
    CommonFunctions objCommonFunctions = null;
    XMLParser xmlParser = new XMLParser();

    public String pushInvoiceForPark(String processInstanceID) throws SQLException {

        ResultSet RS_SheetDetails = null, RS_TDS = null, RS_HeaderData = null, RS_BusPlc = null, RS_EntrySheet = null, RS_EntrySheetDetails = null, RS_ItemData = null, RS_POITEM = null, RS_WITHHOLDING = null, RS_POHistory = null, RS_ISOUnit = null, RS_EXT_LINE = null;
        Statement ST_SheetDeatils = null, ST_TDS = null, ST_HeaderData = null, ST_BusPlc = null, ST_EntrySheet = null, ST_EntrySheetDetails = null, ST_ItemData = null, ST_POITEM = null, ST_WITHHOLDING = null, ST_POHistory = null, ST_ISOUnit = null, ST_EXT_LINE = null, st_updatepartk = null;


        int temp_doc_item = 0, difference = 0;
        float totalamount = 0f;
        String SRV_BASED_IV = "", busPlc_val = "", itemdata_ser = "", inv_doc_item = "", poitem1 = "", PurchaseOrderNo = "", InvoiceNo = "", Plant = "", FISCALYEAR = "", vendor_code = "", base_uom = "";
        String invoiceDate = "", postingDate = "", currency = "", comp_code = "", invoiceAmount = "", message = "", ParkingSyncStatus = "", updatedquantity = null, updatedlineamount = null;

        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String currentDate = date.toString();

        try {
            objCommonFunctions = new CommonFunctions();
            ST_HeaderData = objCommonFunctions.con.createStatement();
            ST_BusPlc = objCommonFunctions.con.createStatement();
            ST_EntrySheet = objCommonFunctions.con.createStatement();
            ST_EntrySheetDetails = objCommonFunctions.con.createStatement();
            ST_ItemData = objCommonFunctions.con.createStatement();
            ST_POITEM = objCommonFunctions.con.createStatement();
            ST_WITHHOLDING = objCommonFunctions.con.createStatement();
            ST_POHistory = objCommonFunctions.con.createStatement();
            ST_ISOUnit = objCommonFunctions.con.createStatement();
            ST_EXT_LINE = objCommonFunctions.con.createStatement();
            ST_TDS = objCommonFunctions.con.createStatement();
            ST_SheetDeatils = objCommonFunctions.con.createStatement();
            st_updatepartk = objCommonFunctions.con.createStatement();
            Query = "Select PurchaseOrderNo,InvoiceNo,InvoiceDate,posting_date,currency,comp_code,invoiceAmount,vendor_code from ext_serviceProcess where processid = '" + processInstanceID + "'";
            // System.out.println("Query is :::::" + Query);
            RS_HeaderData = ST_HeaderData.executeQuery(Query);
            while (RS_HeaderData.next()) {
                PurchaseOrderNo = RS_HeaderData.getString(1);
                InvoiceNo = RS_HeaderData.getString(2);
                invoiceDate = RS_HeaderData.getString(3);
                postingDate = RS_HeaderData.getString(4);
                currency = RS_HeaderData.getString(5);
                comp_code = RS_HeaderData.getString(6);
                invoiceAmount = RS_HeaderData.getString(7);
                vendor_code = RS_HeaderData.getString(8);

            }


            String inputXml = objCommonFunctions.sapInvokeXML("BAPI_PO_GETDETAIL1");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PURCHASEORDER>" + PurchaseOrderNo + "</PURCHASEORDER>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            // System.out.println("Input xml : " + inputXml);
            LogProcessing.xmllogs.info("INPUT BAPI_PO_GETDETAIL1 :::" + inputXml);
            String outputXml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);

            xmlParser.setInputXML(outputXml);
            LogProcessing.xmllogs.info("OUTPUT BAPI_PO_GETDETAIL1 =" + outputXml);

            WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "POITEM"); objList.hasMoreElements(); objList.skip()) {
                    SRV_BASED_IV = objList.getVal("SRV_BASED_IV");
                    //          System.out.println("SRV_BASED_IV Value : " + SRV_BASED_IV);
                }
            }
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("400")) {
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                LogProcessing.errorlogs.info(xmlParser.getValueOf("Description"));
                if (xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist")) {
                    System.exit(0);
                }
            }
//*****************************BUS_PLACE*********************************//
            Query = "select distinct plant from complex_orient_service_po_item where pinstanceid = '" + processInstanceID + "'";
            RS_BusPlc = ST_BusPlc.executeQuery(Query);
            //System.out.println("Plant Query is :::" + Query);
            while (RS_BusPlc.next()) {
                Plant = RS_BusPlc.getString(1);
            }
            String inputXml1 = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_BUSPLACE");
            inputXml1 = inputXml1 + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PLANT>" + Plant + "</PLANT>"
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


            Query = "Select * from complex_orient_entrysheet_sel where pinstanceid = '" + processInstanceID + "'";
            RS_EntrySheet = ST_EntrySheet.executeQuery(Query);
            //System.out.println("Query is " + Query);

            while (RS_EntrySheet.next()) {
                //Service PO parking  ------------------------------------------------------
                Query = "Select PO_ITEM,REF_DOC_YR,REF_DOC_IT,VAL_LOCCUR,REF_DOC,QUANTITY "
                        + "from complex_orient_poitem_history where pinstanceid = '" + processInstanceID + "' "
                        + "and HIST_TYPE = 'E' "
                        + "and REF_DOC = '" + RS_EntrySheet.getString(1) + "'";

                System.out.println("Query : " + Query);
                RS_EntrySheetDetails = ST_EntrySheetDetails.executeQuery(Query);
                int count = 0;
                while (RS_EntrySheetDetails.next()) {
                    count++;
                    int num = Integer.parseInt(RS_EntrySheetDetails.getString(1));
                    String poItem = String.format("%05d", num);
                    String Query2 = "select QUANTITY, VAl_LOCCUR_TDSBASE from complex_service_SheetDetails where pistanceid =  '" + processInstanceID + "' "
                            + "and MAT_DOC = '" + RS_EntrySheet.getString(1) + "' and po_item = '" + poItem + "'";
                    RS_SheetDetails = ST_SheetDeatils.executeQuery(Query2);
                    int countflag = 0;
                    while (RS_SheetDetails.next()) {
                        countflag++;
                        updatedquantity = RS_SheetDetails.getString(1);
                        updatedlineamount = RS_SheetDetails.getString(2);
                        if (countflag == count) {
                            break;
                        }
                    }
//                   System.out.println("FETECHING APPLIED AMOUNT FROM ENTRY SHEET DETAILS");
//                   System.out.println(" QUERY : "+Query2);
//                   System.out.println("QUANTITY :"+updatedquantity+" AA : "+updatedlineamount);
//                    totalamount = totalamount + Float.parseFloat(RS_EntrySheetDetails.getString(4));//***comment by vidit***//
                    temp_doc_item = temp_doc_item + 1;

                    difference = 6 - String.valueOf(temp_doc_item).length();
                    if (difference > 0) {
                        inv_doc_item = String.format("%0" + (difference) + "d%s", 0, String.valueOf(temp_doc_item));
                    } else {
                        inv_doc_item = String.valueOf(temp_doc_item);
                    }

                    String po_item_temp1 = RS_EntrySheetDetails.getString(1);
                    difference = 5 - po_item_temp1.length();
                    if (difference > 0) {
                        poitem1 = String.format("%0" + (5 - po_item_temp1.length()) + "d%s", 0, po_item_temp1);

                    } else {
                        poitem1 = po_item_temp1;

                    }

                    //***********************************************************//
                    String REF_DOC_IT = "";
                    difference = 4 - RS_EntrySheetDetails.getString(3).length();
                    if (difference > 0) {
                        REF_DOC_IT = String.format("%0" + (difference) + "d%s", 0, RS_EntrySheetDetails.getString(3));
                    } else {
                        REF_DOC_IT = RS_EntrySheetDetails.getString(3);
                    }
                    //***********************************************************//

                    //***********************************************************//
                    String po_unit_iso = "", sheet_item = "", OUTL_IND = "", pckg_no = "";
                    Query = "select PCKG_NO from complex_orient_service_po_item where "
                            + "pinstanceid='" + processInstanceID + "' "
                            + "and PO_ITEM = '" + po_item_temp1 + "'";

                    System.out.println("Query : " + Query);
                    //System.out.println("Query isounit : " + Query);
                    RS_ISOUnit = ST_ISOUnit.executeQuery(Query);

                    if (RS_ISOUnit.next()) {

                        pckg_no = RS_ISOUnit.getString(1);
                        // System.out.println("pckg_no " + pckg_no);
                    }

                    //***********************************************************//
//                    Query = "select EXT_LINE,UOM_ISO,BASE_UOM from complex_orient_entry_service where  pinstanceid='" + processInstanceID + "' "
//                            + "and PCKG_NO = (select SUBPCKG_NO from complex_orient_entry_service where  pinstanceid='" + processInstanceID + "' "
//                            + "and PCKG_NO = '" + pckg_no + "' )";

                    Query = "select EXT_LINE,UOM_ISO,BASE_UOM from complex_service_SheetDetails where  pistanceid='" + processInstanceID + "' and po_item ='" + poitem1 + "' and MAT_DOC='" + RS_EntrySheet.getString(1) + "'";

                    System.out.println("Query is :::" + Query);

                    RS_EXT_LINE = ST_EXT_LINE.executeQuery(Query);
                    int count1 = 0;
                    //***********************************************************//
                    while (RS_EXT_LINE.next()) {
                        count1++;
                        sheet_item = RS_EXT_LINE.getString(1);
                        po_unit_iso = RS_EXT_LINE.getString(2);
                        base_uom = RS_EXT_LINE.getString(3);
                        if (count == count1) {
                            break;
                        }
                    }


                    difference = 10 - sheet_item.length();
                    if (difference > 0) {
                        sheet_item = String.format("%0" + (difference) + "d%s", 0, sheet_item);
                    } else {
                        sheet_item = sheet_item;
                    }
                    //***********************************************************//

                    //  System.out.println("Sheet Item : " + sheet_item);

                    //***********************************************************//
                    itemdata_ser = itemdata_ser + "<ITEMDATA>"
                            + "<INVOICE_DOC_ITEM>" + inv_doc_item + "</INVOICE_DOC_ITEM>"
                            + "<PO_NUMBER>" + PurchaseOrderNo + "</PO_NUMBER>"
                            + "<PO_ITEM>" + poitem1 + "</PO_ITEM>"
                            + "<REF_DOC_YEAR>" + RS_EntrySheetDetails.getString(2) + "</REF_DOC_YEAR>"
                            + "<REF_DOC_IT>" + REF_DOC_IT + "</REF_DOC_IT>"
                            + "<ITEM_AMOUNT>" + updatedlineamount + "</ITEM_AMOUNT>"
                            + "<SHEET_NO>" + RS_EntrySheetDetails.getString(5) + "</SHEET_NO>"
                            + "<SHEET_ITEM>" + sheet_item + "</SHEET_ITEM>";

                    if (SRV_BASED_IV.equalsIgnoreCase("X")) {
                        // System.out.println("Inside SRV_BASED_IV == 'X'");

                        Query = "select TAX_CODE from complex_orient_service_po_item where "
                                + "pinstanceid = '" + processInstanceID + "' "
                                + "and PO_ITEM in (select PO_ITEM from complex_orient_poitem_history "
                                + "where pinstanceid = '" + processInstanceID + "' "
                                + "and MAT_DOC = '" + RS_EntrySheet.getString(1) + "') ";
                        System.out.println("Query fot item PO " + Query);
                        RS_POITEM = ST_POITEM.executeQuery(Query);

                        String TAX_CODE = "";
                        while (RS_POITEM.next()) {
                            TAX_CODE = RS_POITEM.getString(1);
                        }
                        if ("NULL".equalsIgnoreCase(po_unit_iso) || "null".equalsIgnoreCase(po_unit_iso) || "".equalsIgnoreCase(po_unit_iso) || null == po_unit_iso) {

                            itemdata_ser = itemdata_ser + "<PO_UNIT>" + base_uom + "</PO_UNIT>"
                                    + "<TAX_CODE>" + TAX_CODE + "</TAX_CODE>"
                                    + "<QUANTITY>" + updatedquantity + "</QUANTITY>" //from entry sheet details
                                    + "</ITEMDATA>";
                        } else {

                            itemdata_ser = itemdata_ser + "<PO_UNIT_ISO>" + po_unit_iso + "</PO_UNIT_ISO>"
                                    + "<TAX_CODE>" + TAX_CODE + "</TAX_CODE>"
                                    + "<QUANTITY>" + updatedquantity + "</QUANTITY>" //from entry sheet details
                                    + "</ITEMDATA>";
                        }



                    } else {
                        // System.out.println("Inside SRV_BASED_IV != 'X'");
                        itemdata_ser = itemdata_ser + "<PO_UNIT_ISO></PO_UNIT_ISO>"
                                + "<QUANTITY></QUANTITY>"
                                + "</ITEMDATA>";
                    }
                    //  System.out.println("Outside SRV_BASED_IV X");

                }
                // System.out.println("Outside for 2");
            }


            // System.out.println("Item Data service : " + itemdata_ser);
            Query = "Select tax_type,tax_code,tax,amount,applied from complex_orient_witholding where pinstanceid = '" + processInstanceID + "'";
            //Query = "Select tax_type,tax_code,tax,tds_amount from complex_tds_calculation where pinstanceid = '" + processInstanceID + "' and applied='Yes'";
            System.out.println("Query complex_orient_witholding " + Query);
            RS_WITHHOLDING = ST_WITHHOLDING.executeQuery(Query);


            String withholdingtax = "";

// 8feb2021         //  Query1 = "select VAL_LOCCUR_TDSBASE from complex_service_SheetDetails where pistanceid = '" + processInstanceID + "'";
//            RS_TDS = ST_TDS.executeQuery(Query1);
//            while (RS_TDS.next()) {
//                totalamount += Float.parseFloat(RS_TDS.getString(1));
//    8feb2021        }

            // System.out.println("Gross Value  Total = " + totalamount);

            //System.out.println("-BigDecimal---" + decimal);


            //  System.out.println("Line total is " + decimal);
            while (RS_WITHHOLDING.next()) {
                if ("Yes".equalsIgnoreCase(RS_WITHHOLDING.getString(5))) {
                    BigDecimal decimal = new BigDecimal(RS_WITHHOLDING.getString(4).replace(",", ""));
                    float wi_percent = Float.parseFloat(RS_WITHHOLDING.getString(3));
                    float wi_base_amount = (wi_percent / 100) * Float.parseFloat(RS_WITHHOLDING.getString(4).replace(",", ""));
                    withholdingtax = withholdingtax + "<WITHTAXDATA>"
                            + "<SPLIT_KEY>000001</SPLIT_KEY>"
                            + "<WI_TAX_TYPE>" + RS_WITHHOLDING.getString(1) + "</WI_TAX_TYPE>"
                            + "<WI_TAX_CODE>" + RS_WITHHOLDING.getString(2) + "</WI_TAX_CODE>"
                            + "<WI_TAX_BASE>" + decimal.setScale(2, BigDecimal.ROUND_FLOOR) + "</WI_TAX_BASE>"
                            + "<WI_TAX_AMT>" + Math.round(Math.ceil(wi_base_amount)) + "</WI_TAX_AMT>"
                            + "</WITHTAXDATA>";
                } else {
                    withholdingtax = withholdingtax + "<WITHTAXDATA>"
                            + "<SPLIT_KEY>000001</SPLIT_KEY>"
                            + "<WI_TAX_TYPE>" + RS_WITHHOLDING.getString(1) + "</WI_TAX_TYPE>"
                            + "<WI_TAX_CODE></WI_TAX_CODE>"
                            + "<WI_TAX_BASE>0</WI_TAX_BASE>"
                            + "<WI_TAX_AMT>0</WI_TAX_AMT>"
                            + "</WITHTAXDATA>";

                }
            }

            //  System.out.println("withholdingtax ::::" + withholdingtax);
            //******WithHolding Tax********//

//            System.out.println("Inv " + invoiceDate);
//            System.out.println("Post " + postingDate);
//
//            System.out.println("Currency : " + currency);

            String vendor_region = new VendorRegion().getVendorRegion(PurchaseOrderNo);
            inputXml1 = "";
            inputXml1 = objCommonFunctions.sapInvokeXML("BAPI_INCOMINGINVOICE_PARK");
            inputXml1 = inputXml1 + "<Parameters>"
                    + "<ImportParameters>"
                    + "<HEADERDATA>"
                    + "<INVOICE_IND>X</INVOICE_IND>"
                    + "<DOC_DATE>" + invoiceDate + " 00:00:00.0</DOC_DATE>"
                    + "<PSTNG_DATE>" + currentDate + " 00:00:00.0</PSTNG_DATE>"
                    + "<REF_DOC_NO>" + InvoiceNo + "</REF_DOC_NO>"
                    + "<COMP_CODE>" + comp_code + "</COMP_CODE>"
                    + "<CURRENCY>" + currency + "</CURRENCY>"
                    + "<GROSS_AMOUNT>" + invoiceAmount + "</GROSS_AMOUNT>"
                    + "<HEADER_TXT>" + objCommonFunctions.dmsUserName + "</HEADER_TXT>"
                    + "<BUSINESS_PLACE>" + busPlc_val + "</BUSINESS_PLACE>"
                    + "<GST_PARTNER>" + vendor_code + "</GST_PARTNER>"
                    + "<PLACE_OF_SUP>" + vendor_region + "</PLACE_OF_SUP>"
                    + "<CALC_TAX_IND>X</CALC_TAX_IND>"
                    + "</HEADERDATA>"
                    + "</ImportParameters>"
                    + "<TableParameters>"
                    + itemdata_ser
                    + withholdingtax
                    + "</TableParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            // System.out.println("Input : " + inputXml1);
            inputXml1 = inputXml1.replace("null", "");
            inputXml1 = inputXml1.replace("NULL", "");
            LogProcessing.xmllogs.info(" Input Ivoice Bapi :::" + inputXml1);
            String outputXml_PARK = DMSCallBroker.execute(inputXml1, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            xmlParser.setInputXML(outputXml_PARK);
            LogProcessing.xmllogs.info(" Output Ivoice Bapi :::" + outputXml_PARK);
            // System.out.println("Input : " + outputXml_PARK);
            String INVOICEDOCNUMBER = "";

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                INVOICEDOCNUMBER = xmlParser.getValueOf("INVOICEDOCNUMBER");
                FISCALYEAR = xmlParser.getValueOf("FISCALYEAR");
                // System.out.println("Document no: " + INVOICEDOCNUMBER);
                if ("".equalsIgnoreCase(INVOICEDOCNUMBER)) {

                    message = xmlParser.getValueOf("MESSAGE");
                    //  System.out.println(message);
                    LogProcessing.errorlogs.info(message);

                    objCommonFunctions.setException(message);

                    ParkingSyncStatus = "Failure";
                } else {

                    String parkupdate = "update ext_serviceprocess set inv_park='" + INVOICEDOCNUMBER + "',fiscalyear='" + FISCALYEAR + "' where processid='" + processInstanceID + "'";
                    LogProcessing.summlogs.info("Parking Number Update for ProcessID " + processInstanceID + " is " + INVOICEDOCNUMBER);
                    st_updatepartk.executeUpdate(parkupdate);
                    objCommonFunctions.attribute = objCommonFunctions.attribute + "<parkingSyncStatus>Success</parkingSyncStatus><parkingSyncDate>" + currentDate + "</parkingSyncDate><Inv_Park>" + INVOICEDOCNUMBER + "</Inv_Park><fiscalyear>" + FISCALYEAR + "</fiscalyear>";
                    ParkingSyncStatus = "Success";
                }


            } else {
                message = "Please Contact Your Administrator";
                //  System.out.println(message);
                LogProcessing.errorlogs.info(message);

                objCommonFunctions.setException(message);

                ParkingSyncStatus = "Failure";
            }
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("400")) {
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                LogProcessing.errorlogs.info(xmlParser.getValueOf("Description"));
                if (xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist")) {
                    System.exit(0);
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            //  System.out.println(message);
            LogProcessing.errorlogs.info(message);

            objCommonFunctions.setException(message);

            ParkingSyncStatus = "Failure";
        }

        return ParkingSyncStatus;
    }
}
