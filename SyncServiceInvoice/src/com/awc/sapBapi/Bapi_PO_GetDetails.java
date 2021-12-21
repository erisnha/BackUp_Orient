/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapBapi;

import com.awc.main.Processing;
import com.awc.methods.*;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dms
 */
public class Bapi_PO_GetDetails {

    DMSXmlResponse xmlResponse;
    CommonFunctions objCommonFunctions = null;
    XMLParser xmlParser = new XMLParser();
    XMLParser xmlParser_1 = new XMLParser();
    Map<String, String> poItem_taxCode = new HashMap<String, String>();

    public void getPODetails(String PONumber) {
        String comp_code = "";
        Float totalwithtax = 0.0f;
        try {
            objCommonFunctions = new CommonFunctions();
            String inputXml = objCommonFunctions.sapInvokeXML("BAPI_PO_GETDETAIL");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PURCHASEORDER>" + PONumber + "</PURCHASEORDER>"
                    + "<HISTORY>X</HISTORY>"
                    + "<SERVICES>X</SERVICES>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            //System.out.println("Input xml : " + inputXml);

            LogProcessing.xmllogs.info(" INPUT BAPI_PO_GETDETAIL : " + inputXml);

            String outputXml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            xmlParser.setInputXML(outputXml);
            LogProcessing.xmllogs.info("OUTPUT BAPI_PO_GETDETAIL : " + outputXml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                comp_code = xmlParser.getValueOf("CO_CODE");
                objCommonFunctions.vendorCode = null;
                objCommonFunctions.vendorName = null;
                objCommonFunctions.vendorCode = xmlParser.getValueOf("VENDOR");///Setting VendorCode to run GSTN and TDS Bapi//
                objCommonFunctions.vendorName = xmlParser.getValueOf("VEND_NAME");
                objCommonFunctions.attribute = objCommonFunctions.attribute
                        + "<comp_code>" + comp_code + "</comp_code>"
                        + "<PoDate>" + xmlParser.getValueOf("CREATED_ON") + "</PoDate>"
                        + "<po_type>" + xmlParser.getValueOf("DOC_TYPE") + "</po_type>"
                        + "<pur_group>" + xmlParser.getValueOf("PUR_GROUP") + "</pur_group>"
                        + "<pur_org>" + xmlParser.getValueOf("PURCH_ORG") + "</pur_org>"
                        + "<vendor_code>" + objCommonFunctions.vendorCode + "</vendor_code>"
                        + "<vendor_name>" + objCommonFunctions.vendorName + "</vendor_name>"
                        + "<currency>" + xmlParser.getValueOf("CURRENCY") + "</currency>";


                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {

                    objCommonFunctions.attribute = objCommonFunctions.attribute
                            + "<q_orient_service_po_item>"
                            + objList.getVal("PO_ITEMS")
                            + "</q_orient_service_po_item>";
                    objCommonFunctions.plant = null;
                    objCommonFunctions.plant = "P" + objList.getVal("PLANT");

                    poItem_taxCode.put(objList.getVal("PO_ITEM"), objList.getVal("TAX_CODE"));

                }


                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEM_HISTORY"); objList.hasMoreElements(); objList.skip()) {
                    objCommonFunctions.attribute = objCommonFunctions.attribute
                            + "<q_orient_poitem_history>"
                            + objList.getVal("PO_ITEM_HISTORY")
                            + "</q_orient_poitem_history>";

                    if (objCommonFunctions.enrtySheet_list.contains(objList.getVal("REF_DOC"))) {
                        //******************NEW CODE *******************************// 

                        if (objList.getVal("HIST_TYPE").equalsIgnoreCase("D")) {
                            String entrysheet_no = objList.getVal("REF_DOC");

                            String inputXml_BAPI_ENTRYSHEET_GETDETAIL = objCommonFunctions.sapInvokeXML("BAPI_ENTRYSHEET_GETDETAIL");
                            inputXml_BAPI_ENTRYSHEET_GETDETAIL = inputXml_BAPI_ENTRYSHEET_GETDETAIL + "<Parameters>"
                                    + "<ImportParameters>"
                                    + "<ENTRYSHEET>" + entrysheet_no + "</ENTRYSHEET>"
                                    + "<LONG_TEXTS></LONG_TEXTS>"
                                    + "</ImportParameters>"
                                    + "</Parameters>"
                                    + "</WFSAPInvokeFunction_Input>";


                            LogProcessing.xmllogs.info(" INPUT BAPI_ENTRYSHEET_GETDETAIL : " + inputXml_BAPI_ENTRYSHEET_GETDETAIL);

                            String outputXml_BAPI_ENTRYSHEET_GETDETAIL = DMSCallBroker.execute(inputXml_BAPI_ENTRYSHEET_GETDETAIL, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
                            xmlParser_1.setInputXML(outputXml_BAPI_ENTRYSHEET_GETDETAIL);
                            LogProcessing.xmllogs.info("OUTPUT BAPI_ENTRYSHEET_GETDETAIL : " + outputXml_BAPI_ENTRYSHEET_GETDETAIL);
                            WFXmlResponse objXmlResponse_entrysheet_getdetail = new WFXmlResponse(outputXml_BAPI_ENTRYSHEET_GETDETAIL);

                            if (xmlParser_1.getValueOf("MainCode").equalsIgnoreCase("0")) {

                                String po_item = xmlParser_1.getValueOf("PO_ITEM");
                                int header_pack_no = Integer.parseInt(xmlParser_1.getValueOf("PCKG_NO"));
                                String curr = xmlParser_1.getValueOf("CURRENCY");
                                int subpack_no = 0;
                                System.out.println(po_item + " " + header_pack_no + " " + curr);
                                for (WFXmlList objList_1 = objXmlResponse_entrysheet_getdetail.createList("TableParameters", "ENTRYSHEET_SERVICES"); objList_1.hasMoreElements(); objList_1.skip()) {

                                    int pack_no = Integer.parseInt(objList_1.getVal("PCKG_NO"));
                                    if (pack_no == header_pack_no) {
                                        subpack_no = Integer.parseInt(objList_1.getVal("SUBPCKG_NO"));
                                    }
                                    if(pack_no == subpack_no){
                                        String VAL_LOCCUR = objList_1.getVal("GROSS_VAL");
                                        String EXT_LINE = objList_1.getVal("EXT_LINE");
                                        String BASE_UOM = objList_1.getVal("BASE_UOM");
                                        String UOM_ISO = objList_1.getVal("UOM_ISO");
                                        String Tax_code = poItem_taxCode.get(po_item);
                                        String Tax_Value = objCommonFunctions.calculateTax(VAL_LOCCUR, Tax_code, comp_code, curr);
                                        objCommonFunctions.attribute = objCommonFunctions.attribute
                                                + "<q_service_entrySheetDetails>"
                                                + "<po_item>" + po_item + "</po_item>"
                                                + "<MAT_DOC>" + entrysheet_no + "</MAT_DOC>"
                                                // + "<PSTNG_DATE>" + objList.getVal("PSTNG_DATE") + "</PSTNG_DATE>"
                                                + "<QUANTITY>" + objList_1.getVal("QUANTITY") + "</QUANTITY>"
                                                + "<VAL_LOCCUR>" + VAL_LOCCUR + "</VAL_LOCCUR>"
                                                + "<VAL_LOCCUR_TDSBASE>" + VAL_LOCCUR + "</VAL_LOCCUR_TDSBASE>"
                                                + "<CURRENCY>" + curr + "</CURRENCY>"
                                                + "<EXT_LINE>" + EXT_LINE + "</EXT_LINE>"
                                                + "<BASE_UOM>" + BASE_UOM + "</BASE_UOM>"
                                                + "<UOM_ISO>" + UOM_ISO + "</UOM_ISO>"
                                                + "</q_service_entrySheetDetails>";


                                        totalwithtax = totalwithtax + Float.parseFloat(VAL_LOCCUR) + Float.parseFloat(Tax_Value);
                                        //  System.out.println("Total With TAx :::"+totalwithtax);
                                    }
                                }


                            }
                            if (xmlParser_1.getValueOf("MainCode").equalsIgnoreCase("400")) {
                                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                                LogProcessing.errorlogs.info(xmlParser_1.getValueOf("Description"));
                                if (xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist")) {
                                    System.exit(0);
                                }
                            }

                            //******************NEW CODE *******************************// 
                            //*********************************11-FEB-2021***********************//            


//                        String VAL_LOCCUR = objList.getVal("VAL_LOCCUR");
//                        String po_item = objList.getVal("PO_ITEM");
//                        String Tax_code = poItem_taxCode.get(po_item);
//                        String currency = objList.getVal("CURRENCY");
//                        String Tax_Value = objCommonFunctions.calculateTax(VAL_LOCCUR, Tax_code, comp_code, currency);
//                        
//                        if (objList.getVal("HIST_TYPE").equalsIgnoreCase("D") 
////                                || objList.getVal("HIST_TYPE").equalsIgnoreCase("E")
//                                ) {
//                            objCommonFunctions.attribute = objCommonFunctions.attribute
//                                    + "<q_service_entrySheetDetails>"
//                                    + "<po_item>" + po_item + "</po_item>"
//                                    + "<MAT_DOC>" + objList.getVal("MAT_DOC") + "</MAT_DOC>"
//                                   // + "<PSTNG_DATE>" + objList.getVal("PSTNG_DATE") + "</PSTNG_DATE>"
//                                    + "<QUANTITY>" + objList.getVal("QUANTITY") + "</QUANTITY>"
//                                    + "<VAL_LOCCUR>" + VAL_LOCCUR + "</VAL_LOCCUR>"
//                                    + "<VAL_LOCCUR_TDSBASE>" + VAL_LOCCUR + "</VAL_LOCCUR_TDSBASE>"
//                                    + "<CURRENCY>" + currency + "</CURRENCY>"
//                                    + "</q_service_entrySheetDetails>";
//                            
//                             totalwithtax = totalwithtax + Float.parseFloat(VAL_LOCCUR) + Float.parseFloat(Tax_Value);
//                           //  System.out.println("Total With TAx :::"+totalwithtax);



                            // }
                            //*********************************11-FEB-2021***********************// 
                        }
                    }

                }

                objCommonFunctions.attribute = objCommonFunctions.attribute + "<amount_po>" + totalwithtax + "</amount_po>";

                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEM_SERVICES"); objList.hasMoreElements(); objList.skip()) {
                    objCommonFunctions.attribute = objCommonFunctions.attribute
                            + "<q_orient_entry_service>"
                            + objList.getVal("PO_ITEM_SERVICES")
                            + "</q_orient_entry_service>";

                }

            }
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("400")) {
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                LogProcessing.errorlogs.info(xmlParser.getValueOf("Description"));
                if (xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist")) {
                    System.exit(0);
                }
            } else {
                // System.out.println("Main Code is not 0");
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is not 0");

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogProcessing.errorlogs.info("Exception from Bapi_PO_GetDetails :: " + e.getMessage());
        }
    }
}
