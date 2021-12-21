package com.newgen.ServiceProcess;

import com.newgen.bapi.po_getdetail;
import com.newgen.bapi.taxcode_list;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

import com.newgen.common.General;
import static com.newgen.common.General.convertNewgenDateToSapDate;
import com.newgen.common.PicklistListenerHandler;
import com.newgen.common.WFXmlList;
import com.newgen.common.WFXmlResponse;
import com.newgen.common.XMLParser;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.faces.application.FacesMessage;

public class Head implements FormListener {

    //FormReference objFormReference = null; //FormContext.getCurrentInstance().getFormReference();
    FormReference formObject = null;
    FormConfig formConfig = null;
    String activityName = null;
    String engineName = null;
    String sessionId = null;
    String folderId = null;
    String FILE = null;
    String serverUrl = null;
    String processInstanceId = null;
    String workItemId = null;
    String userName = null;
    String processDefId = null;
    String Query, mail_query = null;
    po_getdetail objpo_getdetail = null;
    List<List<String>> result, result1;
    PickList objPicklist;
    General objGeneral = null;
    private String today;
    XMLParser xmlParser = new XMLParser();
    XMLParser xmlParser_1 = new XMLParser();
    private String activityId;
    setPODetails objsetpodetails = null;
    private String today1;
    private int difference;
    private List<List<String>> resultarray;
    private int fiscalYear;
    taxcode_list objtaxcode_list = null;
    private List<String> enrtySheet_list = new ArrayList();
    private Map<String, String> val_loccur_tdsbase_map = new HashMap<String, String>();
    private Map<String, String> poItem_taxCode = new HashMap<String, String>();

    @Override
    public void continueExecution(String arg0, HashMap<String, String> arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        objsetpodetails = new setPODetails();
        objpo_getdetail = new po_getdetail();


        if (pEvent.getType().name().equalsIgnoreCase("MOUSE_CLICKED")) {
            System.out.print("------------Inside Mouse Click------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Button2")) {
                System.out.println("Calling ----getWithHoldingTDS------");
                formObject.clear("q_orient_witholding");
                getWithHoldingTDS(formObject.getNGValue("vendor_code"));
            }


            if (pEvent.getSource().getName().equalsIgnoreCase("Button22")) {

                String entrysheetno = formObject.getNGValue("EntrySheetNo");
                ListView lv0 = (ListView) formObject.getComponent("q_orient_entrysheet_sel");
                int rowcount = lv0.getRowCount();
                System.out.println("Row count : " + rowcount);
                if (rowcount > 0) {
                    for (int i = 0; i < rowcount; i++) {
                        if (entrysheetno.equalsIgnoreCase(formObject.getNGValue("q_orient_entrysheet_sel", i, 0))) {
                            throw new ValidatorException(new FacesMessage("Entry Sheet no already added.", ""));
                        } else {
                            formObject.ExecuteExternalCommand("NGAddRow", "q_orient_entrysheet_sel");
                            objsetpodetails.setPoTotal(entrysheetno);
                        }
                    }
                } else {
                    formObject.ExecuteExternalCommand("NGAddRow", "q_orient_entrysheet_sel");
                    objsetpodetails.setPoTotal(entrysheetno);
                }

            }


            //*******************RAhul 18/Nov/2020***************************//        

            if (pEvent.getSource().getName().equalsIgnoreCase("Button1")) {
                formObject.clear("q_service_entrySheetDetails");
                formObject.ExecuteExternalCommand("NGModifyRow", "q_orient_entrysheet_sel");
                formObject.RaiseEvent("WFSave");
                enrtySheet_list.clear();
                poItem_taxCode.clear();
                String xmlnew = "";
                Float totalwithtax = 0.0f;

                ListView lv1 = (ListView) formObject.getComponent("q_orient_entrysheet_sel");
                int lvRowCount = lv1.getRowCount();
                for (int x = 0; x < lvRowCount; x++) {

                    String entrySheet = formObject.getNGValue("q_orient_entrysheet_sel", x, 0);
                    System.out.println("Entry sheet is ::" + entrySheet);
                    enrtySheet_list.add(entrySheet);

                }

                String inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PURCHASEORDER>" + formObject.getNGValue("PurchaseOrderNo") + "</PURCHASEORDER>"
                        + "<HISTORY>X</HISTORY>"
                        + "<SERVICES>X</SERVICES>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";

                System.out.println("Input xml BAPI_PO_GETDETAIL : " + inputXml);


                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "BAPI_PO_GETDETAIL");

                System.out.println("outputXml is ::" + outputXml);

                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                    System.out.println("Inside MAin Code");
                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {

                        poItem_taxCode.put(objList.getVal("PO_ITEM"), objList.getVal("TAX_CODE"));

                    }


                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEM_HISTORY"); objList.hasMoreElements(); objList.skip()) {

                        System.out.println("enrtySheet_list size is ::" + enrtySheet_list.size());
                        for (int i = 0; i < enrtySheet_list.size(); i++) {
                            System.out.println("enrtySheet_list ::" + enrtySheet_list.get(i));
                        }

                        String entrysheet_no = objList.getVal("REF_DOC");
                        if (enrtySheet_list.contains(entrysheet_no)) {

//                            String VAL_LOCCUR = objList.getVal("VAL_LOCCUR");
//                            String po_item = objList.getVal("PO_ITEM");
//                            String Tax_code = poItem_taxCode.get(po_item);
//
//                            String Tax_Value = objGeneral.calculateTax(VAL_LOCCUR, Tax_code);
//                            System.out.println(Tax_code + "" + Tax_Value);

                            if (objList.getVal("HIST_TYPE").equalsIgnoreCase("D")) //                                    || objList.getVal("HIST_TYPE").equalsIgnoreCase("E")
                            {

                                String inputXml_BAPI_ENTRYSHEET_GETDETAIL = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_GETDETAIL");

                                inputXml_BAPI_ENTRYSHEET_GETDETAIL = inputXml_BAPI_ENTRYSHEET_GETDETAIL + "<Parameters>"
                                        + "<ImportParameters>"
                                        + "<ENTRYSHEET>" + entrysheet_no + "</ENTRYSHEET>"
                                        + "<LONG_TEXTS></LONG_TEXTS>"
                                        + "</ImportParameters>"
                                        + "</Parameters>"
                                        + "</WFSAPInvokeFunction_Input>";

                                System.out.println(" INPUT BAPI_ENTRYSHEET_GETDETAIL : " + inputXml_BAPI_ENTRYSHEET_GETDETAIL);
                                String outputXml_BAPI_ENTRYSHEET_GETDETAIL = objGeneral.executeWithCallBroker(inputXml_BAPI_ENTRYSHEET_GETDETAIL, processInstanceId + "BAPI_ENTRYSHEET_GETDETAIL");

                                System.out.println(" OUTPUT outputXml_BAPI_ENTRYSHEET_GETDETAIL : " + outputXml_BAPI_ENTRYSHEET_GETDETAIL);

                                xmlParser_1.setInputXML(outputXml_BAPI_ENTRYSHEET_GETDETAIL);
                                WFXmlResponse objXmlResponse_1 = new WFXmlResponse(outputXml_BAPI_ENTRYSHEET_GETDETAIL);

                                if (xmlParser_1.getValueOf("MainCode").equalsIgnoreCase("0")) {

                                    String po_item = xmlParser_1.getValueOf("PO_ITEM");
                                    int subpack_no = Integer.parseInt(xmlParser_1.getValueOf("PCKG_NO")) + 1;
                                    String curr = xmlParser_1.getValueOf("CURRENCY");


                                    for (WFXmlList objList_1 = objXmlResponse_1.createList("TableParameters", "ENTRYSHEET_SERVICES"); objList_1.hasMoreElements(); objList_1.skip()) {

                                        int pack_no = Integer.parseInt(objList_1.getVal("PCKG_NO"));
                                        if (pack_no == subpack_no) {
                                            String VAL_LOCCUR = objList_1.getVal("GROSS_VAL");
                                            String Tax_code = poItem_taxCode.get(po_item);
                                            String Tax_Value = objGeneral.calculateTax(VAL_LOCCUR, Tax_code);

                                            xmlnew = xmlnew
                                                    + "<ListItem><SubItem>" + po_item
                                                    + "</SubItem><SubItem>" + entrysheet_no
                                                    + "</SubItem><SubItem>" + objList_1.getVal("QUANTITY")
                                                    + "</SubItem><SubItem>" + VAL_LOCCUR
                                                    + "</SubItem><SubItem>" + VAL_LOCCUR
                                                    + "</SubItem><SubItem>" + curr
                                                    + "</SubItem></ListItem>";


                                            totalwithtax = totalwithtax + Float.parseFloat(VAL_LOCCUR) + Float.parseFloat(Tax_Value);
                                            //  System.out.println("Total With TAx :::"+totalwithtax);
                                            formObject.setNGValue("amount_po", totalwithtax);

                                        }
                                    }


                                }


                            }


                        }

                    }
                    formObject.NGAddListItem("q_service_entrySheetDetails", xmlnew);
                    formObject.setNGValue("amount_po", totalwithtax);
                    System.out.println("XML is ::" + xmlnew);
                    System.out.println("totalwithtax is ::" + totalwithtax);

                    ////System.out.println("XML : " + xmlnew);


                } else {
                    throw new ValidatorException(new FacesMessage("Enter VAlidation", ""));
                }

                formObject.RaiseEvent("WFSave");

            }



            //*******************RAhul 18/Nov/2020***************************// 


            //*******************RAhul 30/oct/2020***************************//

            if (pEvent.getSource().getName().equalsIgnoreCase("Button7")) {
                String resolutionRemarks = formObject.getNGValue("Text11");
                if (resolutionRemarks.equalsIgnoreCase("") || resolutionRemarks.equalsIgnoreCase(null)) {
                    throw new ValidatorException(new FacesMessage("Please enter resoution remarks", ""));
                }
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                String currentDate = date.toString();
                formObject.setNGValue("Text16", "True");
                formObject.setNGValue("Text17", currentDate);
                formObject.setNGValue("Text18", userName);
                formObject.ExecuteExternalCommand("NGModifyRow", "q_exception_details");
            }


            if (pEvent.getSource().getName().equalsIgnoreCase("Button8")) {
                float amount, appliedAmount, val_loccur = 0.0f, val_loccur_tdsbase = 0.0f, appliedamountinitialvalue = 0.0f;
                amount = Float.parseFloat(formObject.getNGValue("Text13"));
                appliedAmount = Float.parseFloat(formObject.getNGValue("Text14"));

                int rowIndex = formObject.getSelectedIndex("q_service_entrySheetDetails");
                appliedamountinitialvalue = Float.parseFloat(formObject.getNGValue("q_service_entrySheetDetails", rowIndex, 4));
                System.out.println("Rowindex :" + rowIndex + " " + appliedamountinitialvalue);

                Query = "select sum(cast(val_loccur as float)) from complex_service_SheetDetails where pistanceid ='" + processInstanceId + "' ";
                System.out.println("Query to check : " + Query);
                result = formObject.getDataFromDataSource(Query);
                val_loccur = Float.parseFloat(result.get(0).get(0));
                System.out.println("val_loccur Value :: " + val_loccur);

                float taxpercent = 0.05f;
                float totalincremntvalue = taxpercent * val_loccur;
                System.out.println("totalincremntvalue Value 50 :: " + totalincremntvalue);

                float totalvalue = val_loccur + totalincremntvalue;
                System.out.println("Total Value 1050:: " + totalvalue);


                Query = "select sum(cast(VAl_LOCCUR_TDSBASE as float)) from complex_service_SheetDetails where pistanceid ='" + processInstanceId + "' ";
                System.out.println("Query to check : " + Query);
                result = formObject.getDataFromDataSource(Query);
                val_loccur_tdsbase = Float.parseFloat(result.get(0).get(0));
                System.out.println("val_loccur_tdsbase Value :: " + val_loccur_tdsbase);
                float total_val_loccur_tdsbase = val_loccur_tdsbase + (appliedAmount - appliedamountinitialvalue);
                System.out.println("total_val_loccur_tdsbase is :- " + total_val_loccur_tdsbase);


                if (total_val_loccur_tdsbase > totalvalue) {
                    throw new ValidatorException(new FacesMessage("Applied amount limit exceeds", ""));
                } else {
                    formObject.ExecuteExternalCommand("NGModifyRow", "q_service_entrySheetDetails");
                    formObject.RaiseEvent("WFSave");

                    objGeneral = new General();
                    String taxCode = "", taxVal = "";
                    Float taxTotal = 0.0f;
                    ListView lv1 = (ListView) formObject.getComponent("q_service_entrySheetDetails");
                    int lvRowCount = lv1.getRowCount();

                    for (int x = 0; x < lvRowCount; x++) {
                        Query = "select TAX_CODE from complex_orient_service_po_item where pinstanceid ='" + processInstanceId + "' and po_item ='" + formObject.getNGValue("q_service_entrySheetDetails", x, 0) + "'";
                        System.out.println("Query to check : " + Query);
                        result1 = formObject.getDataFromDataSource(Query);
                        taxCode = result1.get(0).get(0);
                        taxVal = objGeneral.calculateTax(formObject.getNGValue("q_service_entrySheetDetails", x, 4), taxCode);
                        taxTotal = taxTotal + Float.parseFloat(taxVal) + Float.parseFloat(formObject.getNGValue("q_service_entrySheetDetails", x, 4));
                        System.out.println("TAXVAL :- " + taxVal + " " + "TAXTOTAL :- " + taxTotal);
                    }

                    String amount_po = String.valueOf(taxTotal);
                    System.out.println("Amount PO :: " + amount_po);
                    formObject.setNGValue("amount_po", amount_po);
                    formObject.RaiseEvent("WFSave");


                }


            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button11")) // TDS CALCULATION TAB Button
            {
                formObject.ExecuteExternalCommand("NGModifyRow", "q_orient_witholding");
                formObject.RaiseEvent("WFSave");
            }




            if (pEvent.getSource().getName().equalsIgnoreCase("Button6")) {



                //1. Get the selected row from list view q_orient_service_po_item - you will get the selected row index
                //2. Set ngvalue on that row index and column index
                ListView lv0 = (ListView) formObject.getComponent("q_orient_service_po_item");
                int selectrow = lv0.getSelectedRowIndex();
                //formObject.ExecuteExternalCommand("NGModifyRow", "q_orient_service_po_item");
                formObject.setNGValue("q_orient_service_po_item", selectrow, 11, formObject.getNGValue("Text20"));
                formObject.setNGValue("q_orient_service_po_item", selectrow, 8, formObject.getNGValue("Text35"));
                formObject.RaiseEvent("WFSave");
                //***************************************************//
                val_loccur_tdsbase_map.clear();
                int lvRowCount = lv0.getRowCount();

                for (int x = 0; x < lvRowCount; x++) {
                    String PO_ITEM = formObject.getNGValue("q_orient_service_po_item", x, 8);
                    String tax_code = formObject.getNGValue("q_orient_service_po_item", x, 11);
                    System.out.println("PO_ITEM :- " + PO_ITEM + " tax_code ::" + tax_code);

                    val_loccur_tdsbase_map.put(PO_ITEM, tax_code);

                }


                objGeneral = new General();
                String taxVal = "";
                Float taxTotal = 0.0f;
                ListView lv_entrysheet = (ListView) formObject.getComponent("q_service_entrySheetDetails");
                int lvRowCount_entrysheet = lv_entrysheet.getRowCount();

                for (int x = 0; x < lvRowCount_entrysheet; x++) {
                    String taxcode = formObject.getNGValue("q_service_entrySheetDetails", x, 0);
                    int tax_code = Integer.parseInt(taxcode);

                    taxVal = objGeneral.calculateTax(formObject.getNGValue("q_service_entrySheetDetails", x, 4), val_loccur_tdsbase_map.get(String.valueOf(tax_code)));
                    taxTotal = taxTotal + Float.parseFloat(taxVal) + Float.parseFloat(formObject.getNGValue("q_service_entrySheetDetails", x, 4));
                    System.out.println("TAXVAL :- " + taxVal + " " + "TAXTOTAL :- " + taxTotal);
                }

                System.out.println("Total TAX ::- " + taxTotal);
                String amount_po = String.valueOf(taxTotal); // applied amount with tax
                System.out.println("Amount PO :: " + amount_po);
                formObject.setNGValue("amount_po", amount_po);
                formObject.RaiseEvent("WFSave");


            }


            //*******************RAhul 30/oct/2020***************************//

            if (pEvent.getSource().getName().equalsIgnoreCase("Button3")) {

                objtaxcode_list = new taxcode_list();
                List<List<String>> taxList = new ArrayList<List<String>>();
                try {
                    objPicklist = formObject.getNGPickList("taxcode_service", "Tax Code,Description", true, 100);
                    Color color = new Color(31, 98, 171);
                    objPicklist.setPicklistHeaderBGColor(color);
                    objPicklist.setPicklistHeaderFGColor(Color.WHITE);
                    objPicklist.setWindowTitle("Tax code list");
                    objPicklist.setWidth(200);
                    objPicklist.setHeight(550);
                    objPicklist.addPickListListener(new PicklistListenerHandler(objPicklist.getClientId()));
                    String inputXml = objtaxcode_list.getTAX_CODE_LIST();
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_ZBAPI_AP_AUTOMATION_TAX_CODE");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    System.out.println("After xml response");
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TAX_TAB"); objList.hasMoreElements(); objList.skip()) {
                            System.out.println("Inside TAX_CODE loop");
                            String TAX_CODE = objList.getVal("TAX_CODE");
                            System.out.println("TAX CODE : " + TAX_CODE);
                            String DESC = objList.getVal("DESC");
                            System.out.println("DESC : " + DESC);
                            taxList.add(Arrays.asList(TAX_CODE, DESC));
                        }
                    }
                    objPicklist.populateData(taxList);
                    objPicklist.setVisible(true);
                    System.out.println("###Populate data");
                } catch (Exception e) {
                    System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button5")) {
                System.out.println("Inside button click 5");
                int temp_doc_item = 0;
                String SRV_BASED_IV = "", busPlc_val = "", itemdata_ser = "", inv_doc_item = "", poitem1;
                String invoiceno = formObject.getNGValue("InvoiceNo");
                String inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL1");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PURCHASEORDER>" + formObject.getNGValue("PurchaseOrderNo") + "</PURCHASEORDER>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                System.out.println("Input xml : " + inputXml);
                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_PO_GETDETAIL1");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "POITEM"); objList.hasMoreElements(); objList.skip()) {
                        SRV_BASED_IV = objList.getVal("SRV_BASED_IV");
                        System.out.println("SRV_BASED_IV Value : " + SRV_BASED_IV);
                    }
                }

                Query = "select distinct plant from complex_orient_po_item where pinstanceid = '" + processInstanceId + "'";
                String inputXml1 = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_BUSPLACE");
                inputXml1 = inputXml1 + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PLANT>" + formObject.getDataFromDataSource(Query).get(0).get(0) + "</PLANT>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                System.out.println("Input XML : " + inputXml1);
                String outputXml1 = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_ZBAPI_AP_AUTOMATION_BUSPLACE");
                xmlParser.setInputXML(outputXml1);
                WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXml1);
                System.out.println("After xml response");
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    busPlc_val = xmlParser.getValueOf("BUSS_PLACE");
                } else {
                    busPlc_val = "";
                }

                ListView lv1 = (ListView) formObject.getComponent("q_orient_entrysheet_sel");
                int rowcount = lv1.getRowCount();
                System.out.println("Row count Entrysheet: " + rowcount);
                for (int k = 0; k < rowcount; k++) {
                    //Service PO parking  ------------------------------------------------------
                    Query = "Select PO_ITEM,REF_DOC_YR,REF_DOC_IT,VAL_LOCCUR,REF_DOC,QUANTITY "
                            + "from complex_orient_poitem_history where pinstanceid = '" + processInstanceId + "' "
                            + "and HIST_TYPE = 'E' "
                            + "and REF_DOC = '" + formObject.getNGValue("q_orient_entrysheet_sel", k, 0) + "'";
                    System.out.println("Query : " + Query);
                    result = formObject.getDataFromDataSource(Query);
                    String LVPOname = "q_orient_poitem_history";
                    int latest = 0;
                    for (int i = 0; i < result.size(); i++) {
                        //  if (invoiceno.equalsIgnoreCase(formObject.getNGValue(LVPOname, i, 0))) {
                        temp_doc_item = temp_doc_item + 1;
                        latest = i;
                        difference = 6 - String.valueOf(temp_doc_item).length();
                        if (difference > 0) {
                            inv_doc_item = String.format("%0" + (difference) + "d%s", 0, String.valueOf(temp_doc_item));
                        } else {
                            inv_doc_item = String.valueOf(temp_doc_item);
                        }

                        String po_item_temp1 = result.get(i).get(0);
                        difference = 5 - po_item_temp1.length();
                        if (difference > 0) {
                            poitem1 = String.format("%0" + (5 - po_item_temp1.length()) + "d%s", 0, po_item_temp1);

                        } else {
                            poitem1 = formObject.getNGValue(LVPOname, latest, 7);
                        }

                        //***********************************************************//
                        String po_unit_iso = "", sheet_item = "", OUTL_IND = "";
                        Query = "select UOM_ISO,OUTL_IND from complex_orient_entry_service where "
                                + "pinstanceid='" + processInstanceId + "' "
                                + "and EXT_LINE = '" + po_item_temp1 + "'";
                        System.out.println("Query itemdata : " + Query);
                        resultarray = formObject.getDataFromDataSource(Query);
                        System.out.println("Result size : " + resultarray.size());
                        if (resultarray.size() > 0) {
                            System.out.println("Inside result > 0");
                            po_unit_iso = resultarray.get(0).get(0);
                            System.out.println("po_unit_iso " + po_unit_iso);
                            OUTL_IND = resultarray.get(0).get(1);
                            System.out.println("OUTL_IND " + OUTL_IND);
                            if ("X".equalsIgnoreCase(OUTL_IND)) {
                                sheet_item = "";
                            } else {
                                System.out.println("Inside else");
                                sheet_item = po_item_temp1;
                            }
                        }
                        System.out.println("Sheet Item : " + sheet_item);
                        // String po_unit = resultarray.get(0).get(1);
                        // String taxcode = resultarray.get(0).get(2);

                        //***********************************************************//
                        itemdata_ser = itemdata_ser + "<ITEMDATA>"
                                + "<INVOICE_DOC_ITEM>" + inv_doc_item + "</INVOICE_DOC_ITEM>"
                                + "<PO_NUMBER>" + formObject.getNGValue("PurchaseOrderNo") + "</PO_NUMBER>"
                                + "<PO_ITEM>" + poitem1 + "</PO_ITEM>"
                                + "<REF_DOC_YEAR>" + result.get(i).get(1) + "</REF_DOC_YEAR>"
                                + "<REF_DOC_IT>" + result.get(i).get(2) + "</REF_DOC_IT>"
                                + "<ITEM_AMOUNT>" + result.get(i).get(3) + "</ITEM_AMOUNT>"
                                + "<SHEET_NO>" + result.get(i).get(4) + "</SHEET_NO>"
                                + "<SHEET_ITEM>" + sheet_item + "</SHEET_ITEM>";

                        if (SRV_BASED_IV.equalsIgnoreCase("X")) {
                            System.out.println("Inside SRV_BASED_IV == 'X'");

                            Query = "select TAX_CODE from complex_orient_po_item where "
                                    + "pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                                    + "and PO_ITEM in (select PO_ITEM from complex_orient_poitem_history "
                                    + "where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                                    + "and MAT_DOC = '" + formObject.getNGValue("q_orient_entrysheet_sel", k, 0) + "') ";
                            System.out.println("Query fot item PO " + Query);
                            result1 = formObject.getDataFromDataSource(Query);
//                            System.out.println("Result size : " + result.size() + " - Tax Code value :" + result.get(0).get(0));
//                            formObject.setNGValue("taxcode_service", result.get(0).get(0));

                            itemdata_ser = itemdata_ser + "<PO_UNIT_ISO>" + po_unit_iso + "</PO_UNIT_ISO>"
                                    // + "<PO_UNIT>" + po_unit + "</PO_UNIT>"
                                    + "<TAX_CODE>" + result1.get(0).get(0) + "</TAX_CODE>"
                                    + "<QUANTITY>" + result.get(i).get(5) + "</QUANTITY>"
                                    + "</ITEMDATA>";
                        } else {
                            System.out.println("Inside SRV_BASED_IV != 'X'");
                            itemdata_ser = itemdata_ser + "<PO_UNIT_ISO></PO_UNIT_ISO>"
                                    // + "<PO_UNIT>" + po_unit + "</PO_UNIT>"
                                    // + "<TAX_CODE>" + taxcode + "</TAX_CODE>"
                                    + "<QUANTITY></QUANTITY>"
                                    + "</ITEMDATA>";
                        }
                        System.out.println("Outside SRV_BASED_IV X");
                        //  }
                    }
                    System.out.println("Outside for 2");
                }
                System.out.println("Item Data service : " + itemdata_ser);
                String LVControlName = "ListView3";
                ListView lv0 = (ListView) formObject.getComponent(LVControlName);
                int lvRowCount = lv0.getRowCount();
                System.out.println("Row count : " + lvRowCount);
                String withholdingtax = "";

                float totalAmount = 0f;
                BigDecimal decimal = null;
                for (int j = 0; j < result.size(); j++) {
                    totalAmount = totalAmount + Float.parseFloat(result.get(j).get(3));
                    System.out.println("Gross Value = " + result.get(j).get(3) + " Total = " + totalAmount);
                    decimal = new BigDecimal(totalAmount);
                    System.out.println("--" + decimal);
                }

                System.out.println("Line total is " + decimal);
                for (int i = 0; i < lvRowCount; i++) {
                    float wi_percent = Float.parseFloat(formObject.getNGValue(LVControlName, i, 3));
                    float wi_base_amount = (wi_percent / 100) * totalAmount;
                    withholdingtax = withholdingtax + "<WITHTAXDATA>"
                            + "<SPLIT_KEY>000001</SPLIT_KEY>"
                            + "<WI_TAX_TYPE>" + formObject.getNGValue(LVControlName, i, 1) + "</WI_TAX_TYPE>"
                            + "<WI_TAX_CODE>" + formObject.getNGValue(LVControlName, i, 2) + "</WI_TAX_CODE>"
                            + "<WI_TAX_BASE>" + decimal.setScale(2, BigDecimal.ROUND_FLOOR) + "</WI_TAX_BASE>"
                            + "<WI_TAX_AMT>" + Math.round(Math.ceil(wi_base_amount)) + "</WI_TAX_AMT>"
                            + "</WITHTAXDATA>";
                }
                //******WithHolding Tax********//

                System.out.println("Inv " + formObject.getNGValue("InvoiceDate"));
                System.out.println("Post " + formObject.getNGValue("posting_date"));
                String docdate = convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate"));
                String postingdate = convertNewgenDateToSapDate(formObject.getNGValue("posting_date"));
                System.out.println("date " + docdate + " " + postingdate);
                String currency = formObject.getNGValue("currency");
                System.out.println("Currency : " + currency);
                inputXml1 = "";
                inputXml1 = objGeneral.sapInvokeXML("BAPI_INCOMINGINVOICE_PARK");
                inputXml1 = inputXml1 + "<Parameters>"
                        + "<ImportParameters>"
                        + "<HEADERDATA>"
                        + "<INVOICE_IND>X</INVOICE_IND>"
                        + "<DOC_DATE>" + docdate + " 00:00:00.0</DOC_DATE>"
                        + "<PSTNG_DATE>" + postingdate + " 00:00:00.0</PSTNG_DATE>"
                        + "<REF_DOC_NO>" + invoiceno + "</REF_DOC_NO>"
                        + "<COMP_CODE>1000</COMP_CODE>"
                        + "<CURRENCY>" + currency + "</CURRENCY>"
                        + "<GROSS_AMOUNT>" + formObject.getNGValue("InvoiceAmount") + "</GROSS_AMOUNT>"
                        + "<HEADER_TXT>" + userName + "</HEADER_TXT>"
                        + "<BUSINESS_PLACE>" + busPlc_val + "</BUSINESS_PLACE>"
                        + "<CALC_TAX_IND>X</CALC_TAX_IND>"
                        + "</HEADERDATA>"
                        + "</ImportParameters>"
                        + "<TableParameters>"
                        + itemdata_ser
                        + withholdingtax
                        + "</TableParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                System.out.println("Input : " + inputXml1);

                outputXml = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_BAPI_INCOMINGINVOICE_PARK");
                xmlParser.setInputXML(outputXml);
                objXmlResponse = new WFXmlResponse(outputXml);
                System.out.println("After xml response");
                String INVOICEDOCNUMBER = "", message1 = "";

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    INVOICEDOCNUMBER = xmlParser.getValueOf("INVOICEDOCNUMBER");
                    System.out.println("Document no: " + INVOICEDOCNUMBER);
                    formObject.setNGValue("Inv_Park", INVOICEDOCNUMBER);

                    message1 = xmlParser.getValueOf("MESSAGE");
                    if ("".equalsIgnoreCase(INVOICEDOCNUMBER)) {
                        throw new ValidatorException(new FacesMessage("Error while parking: " + message1, ""));
                    } else {
                        formObject.RaiseEvent("WFSave");

                        throw new ValidatorException(new FacesMessage("Invoice Document Number :" + INVOICEDOCNUMBER, ""));
                    }

                } else {
                    throw new ValidatorException(new FacesMessage("Please Contact Your Administrator", ""));
                }
            }
        }
    }

    @Override
    public void formLoaded(FormEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println(" -------------------Intiation Workstep Loaded from formloaded.----------------");
        // TODO Auto-generated method stub
        System.out.println("form Loaded called");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        System.out.println("formObject :" + formObject);
        System.out.println("formConfig :" + formConfig);
        try {
            //  System.out.println("-->>" + formConfig.getConfigXML());
            activityName = formObject.getWFActivityName();
            activityId = formConfig.getConfigElement("ActivityId");
            engineName = formConfig.getConfigElement("EngineName");
            sessionId = formConfig.getConfigElement("DMSSessionId");
            folderId = formConfig.getConfigElement("FolderId");
            serverUrl = formConfig.getConfigElement("ServletPath");
            //ServletUrl = serverUrl + "/servlet/ExternalServlet";
            processInstanceId = formConfig.getConfigElement("ProcessInstanceId");
            workItemId = formConfig.getConfigElement("WorkitemId");
            userName = formConfig.getConfigElement("UserName");
            processDefId = formConfig.getConfigElement("ProcessDefId");


//  ************************************************************************************
        } catch (Exception e) {
            System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
        }
    }

    @Override
    public void formPopulated(FormEvent arg0) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        // TODO Auto-generated method stub     
        objGeneral = new General();
        objsetpodetails = new setPODetails();
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        fiscalYear = cal.get(Calendar.YEAR);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        today = formatter1.format(date);
        today1 = formatter1.format(date);
        System.out.println("----------------------Intiation Workstep Loaded from form populated.---------------------------");
        formObject.setEnabled("Text31", false);//inv_park text box 18/March/2021-Rahul
        formObject.setEnabled("Button1", false);
        formObject.setEnabled("Button6", false);
        formObject.setEnabled("Text20", false);
        formObject.setEnabled("Text19", false);

        if (activityName.equalsIgnoreCase("ParkingException")) {
            formObject.setSheetVisible("Tab1", 0, false);
            //inv_park text box 18/March/2021-Rahul
            formObject.setSheetVisible("Tab1", 1, true);
            formObject.setEnabled("Text31", true);
            formObject.setEnabled("Text36", false);
            formObject.setEnabled("accounts_decision", false);
            formObject.setEnabled("button5", false);
            //inv_park text box 18/March/2021-Rahul
            formObject.setSheetVisible("Tab1", 2, false);
            formObject.setSheetVisible("Tab1", 3, false);
            formObject.setSheetVisible("Tab1", 4, false);
            formObject.setSheetVisible("Tab1", 5, true);
            formObject.setSheetVisible("Tab1", 6, false);
        }
        if (activityName.equalsIgnoreCase("PostingException")) {
            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetVisible("Tab1", 1, false);
            formObject.setSheetVisible("Tab1", 2, false);
            formObject.setSheetVisible("Tab1", 3, false);
            formObject.setSheetVisible("Tab1", 4, false);
            formObject.setSheetVisible("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 6, true);
        }

        if (activityName.equalsIgnoreCase("ServiceUpload")) {
            //formObject.setVisible("Tab1", false);
            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetVisible("Tab1", 1, false);
            formObject.setSheetVisible("Tab1", 2, false);
            formObject.setSheetVisible("Tab1", 3, false);
            formObject.setSheetVisible("Tab1", 4, true);
            formObject.setSheetVisible("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 6, false);
            formObject.setSheetVisible("Tab2", 1, true);
            formObject.setSheetVisible("Tab2", 2, true);
            Query = "select wf.ProcessInstanceID,wf.ActivityName from ext_serviceprocess ext, WFINSTRUMENTTABLE wf "
                    + "where ext.processid = wf.ProcessInstanceID "
                    + " and ext.PurchaseOrderNo = '" + formObject.getNGValue("PurchaseOrderNo") + "' "
                    + " and ext.EntrySheetNo = '" + formObject.getNGValue("EntrySheetNo") + "'";
            result = formObject.getDataFromDataSource(Query);

            if (result.size() > 0) {
                throw new ValidatorException(new FacesMessage("The invoice for the Purchase order no. " + formObject.getNGValue("PurchaseOrderNo")
                        + " and Entry sheet no. " + formObject.getNGValue("EntrySheetNo") + " has been already processed.", ""));
            }


            formObject.setEnabled("Button1", true);
            formObject.setEnabled("Button6", true);
            formObject.setEnabled("Text20", true);
            formObject.setEnabled("Text19", true);


        }

        if (activityName.equalsIgnoreCase("Audit")) {

            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetEnable("Tab1", 1, false);
            formObject.setSheetEnable("Tab1", 2, false);
            formObject.setSheetVisible("Tab1", 3, false);
            formObject.setSheetEnable("Tab1", 4, false);
            formObject.setSheetVisible("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 6, false);

            formObject.setEnabled("ServiceForm", false);
        }

        if (activityName.equalsIgnoreCase(
                "Finance")) {
            // formObject.setSheetVisible("Tab2", 2, false); //Routed to Finance and withholding details enabled
            formObject.setSheetVisible("Tab1", 0, false);
            //   formObject.setSheetEnable("Tab1", 0, false); //since hidden the approval tab
            formObject.setSheetEnable("Tab1", 1, false);
            formObject.setSheetVisible("Tab1", 2, true);
            formObject.setSheetVisible("Tab1", 3, false);
            formObject.setSheetEnable("Tab1", 4, false);
            formObject.setSheetVisible("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 6, false);

            formObject.setVisible("Button4", false);

            formObject.setEnabled("PurchaseOrderNo", false);
            formObject.setEnabled("EntrySheetNo", false);

            formObject.setEnabled("GSTN", false);
            formObject.setEnabled("gstn_inv", false);
            formObject.setEnabled("pan_inv", false);
            formObject.setEnabled("PAN", false);

            formObject.setVisible("EntrySheetNo", false);
            formObject.setVisible("Button22", false);

            objsetpodetails.setPOLine();
            objsetpodetails.setEntryLine();
        }
    }

    @Override
    public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        System.out.print("-------------------save form completed---------");
    }

    @Override
    public void saveFormStarted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

    }

    @Override
    public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
    }

    @Override
    public void submitFormStarted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        System.out.println("Testing 1");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        String assignedTo = "";
        String inv_amount = formObject.getNGValue("InvoiceAmount");
        String purc_group = formObject.getNGValue("pur_group");
        String service_status = formObject.getNGValue("service_status");
        System.out.println("inv_amount" + inv_amount + "purc_group" + purc_group);



        //*********************RAHUL  1-feb-2021--***********************// 
        if (activityName.equalsIgnoreCase("ServiceUpload")) {
            if (!formObject.getNGValue("initiator_decision").equalsIgnoreCase("Discard")) {
                checkAmountValidation();
            }
            float amount_zero = 0.0f;
            Query = "select amount from complex_orient_witholding where pinstanceid = '" + processInstanceId + "' and applied ='Yes'";
            System.out.println("Query to check : " + Query);
            result = formObject.getDataFromDataSource(Query);
            for (int i = 0; i < result.size(); i++) {
                if (Float.parseFloat(result.get(i).get(0)) <= amount_zero) {
                    throw new ValidatorException(new FacesMessage("Please enter TDS amount !!!", ""));
                }
            }
        }


        if ("ParkingException".equalsIgnoreCase(activityName)) {
            System.out.println("ParkingException_decision is Activity");
            String ParkingException_decision = (String) formObject.getNGValue("ParkingException_decision");
            if (ParkingException_decision.equalsIgnoreCase("Send for posting")) {
                String inv_park_val = (String) formObject.getNGValue("inv_park");
                if ("".equalsIgnoreCase(inv_park_val)) {

                    throw new ValidatorException(new FacesMessage("Please First Park the dcoument before send for posting", ""));

                }
                formObject.RaiseEvent("WFSave");
            }
        }

//        if ("ParkingException".equalsIgnoreCase(activityName)) {
//            System.out.println("ParkingException_decision is Activity");
//            String ParkingException_decision = (String) formObject.getNGValue("ParkingException_decision");
//            if (ParkingException_decision.equalsIgnoreCase("Send for posting")) {
//                System.out.println("ParkingException_decision is ::- " + ParkingException_decision);
//                String purchaseorderno = (String) formObject.getNGValue("PurchaseOrderNo");
//                String outputXML = objpo_getdetail.getPO_GETDETAIL_HISTORY(purchaseorderno);
//
//                System.out.println("OUTPUT OF getPO_GETDETAIL_HISTORY :: " + outputXML);
//                xmlParser.setInputXML(outputXML);
//                WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXML);
//                String inv_park = "";
//                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
//
//
//                    for (WFXmlList objList1 = objXmlResponse1.createList("TableParameters", "PO_HISTORY"); objList1.hasMoreElements(); objList1.skip()) {
//                        inv_park = objList1.getVal("BELNR");
//                        String parkingStatus = objList1.getVal("BEWTP");
//                        System.out.println("Parkingstatus :::" + parkingStatus);
//                        System.out.println("inv_park :::" + inv_park);
//                        if (parkingStatus.equalsIgnoreCase("T")) {
//                            formObject.setNGValue("Inv_Park", inv_park);
//                            formObject.setNGValue("accounts_decision", "Park");
//
//                        }
//
//                    }
//
//                }
//                String inv_park_status = (String) formObject.getNGValue("Inv_Park");
//                if (inv_park_status.equalsIgnoreCase("")) {
//
//                    throw new ValidatorException(new FacesMessage("Please First Park the dcoument before send for posting", ""));
//
//                }
//
//                formObject.RaiseEvent("WFSave");
//            }
//
//        }
//commoneted on Feb 4 2021
//****************************************************************************************
//for division from BAPI_MATERIAL_GET_DETAIL***********************************************
//        if ("Finance".equalsIgnoreCase(activityName)) {
//            if (formObject.getNGValue("finance_decision").equalsIgnoreCase("Posted")) {
//                String post_flag = "N";
//                String purchaseorderno = (String) formObject.getNGValue("PurchaseOrderNo");
//                String outputXml = objpo_getdetail.getPO_GETDETAIL_HISTORY(purchaseorderno);
//
//                xmlParser.setInputXML(outputXml);
//                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
//
//                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
//                    System.out.println("Inside Main code zero");
//                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_HISTORY"); objList.hasMoreElements(); objList.skip()) {
//                        System.out.println("Inside po history for loop");
//                        String HIST_TYPE = objList.getVal("BEWTP");
//                        String MAT_DOC = objList.getVal("BELNR");
//                        System.out.println("Hist type : " + HIST_TYPE);
//                        System.out.println("Mat Doc : " + MAT_DOC);
//                        if (MAT_DOC.equalsIgnoreCase(formObject.getNGValue("Inv_Park"))
//                                && "Q".equalsIgnoreCase(HIST_TYPE)) {
//                            post_flag = "Y";
//                            break;
//                        }
//                    }
//                }
//
//                if (post_flag.equalsIgnoreCase("Y")) {
//                    formObject.setNGValue("inv_post", formObject.getNGValue("inv_park"));
//                } else {
//                    throw new ValidatorException(new FacesMessage("Please perform the posting to proceed further", ""));
//                }
//            } else {
//                formObject.setNGValue("inv_park", "");
//            }
//
//        }

        //*********************RAHUL  1-feb-2021--***********************// 


    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encrypt(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String decrypt(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //--Vidit : Checking Invoice Amount and EntrySheet Difference---

    public void checkAmountValidation() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

        String InvoiceAmount = formObject.getNGValue("InvoiceAmount");
        String amount_po = formObject.getNGValue("amount_po");
        System.out.println("InvoiceAmount : " + InvoiceAmount + " amount_po : " + amount_po);
        float inv_amount = Float.valueOf(InvoiceAmount);
        float amount_po1 = Float.valueOf(amount_po);
        float diff = inv_amount - amount_po1;

        System.out.println("InvoiceAmount : " + inv_amount + " amount_po : " + amount_po1);
        if ((diff > 10) || (diff < -10)) {
            throw new ValidatorException(new FacesMessage("Amount Mismatch !!!", ""));
        }
    }
    //-----get withholding tax VIDIT-----------------------

    public void getWithHoldingTDS(String vendor_code) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        String inputXml = "", outputxml = "";
        DMSXmlResponse xmlResponse;
        General objCommonFunctions = new General();
        try {
            inputXml = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_TDS");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<VENDOR>" + vendor_code + "</VENDOR>"
                    + "<COMP_CODE>1000</COMP_CODE>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            System.out.println("Vidit--->" + inputXml);
            outputxml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_ZBAPI_AP_AUTOMATION_TDS");

            xmlParser.setInputXML(outputxml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputxml);
            System.out.println("After xml response");

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                String xmlnew = "";

                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TDS_TAB"); objList.hasMoreElements(); objList.skip()) {
                    System.out.println("Inside For loop");
                    String DESC = objList.getVal("DESC");
                    String COUNTRY = objList.getVal("COUNTRY");
                    String WITHT = objList.getVal("WITHT");
                    String WT_WITHCD = objList.getVal("WT_WITHCD");
                    String QSATZ = objList.getVal("QSATZ");

                    xmlnew = xmlnew + " <ListItem>"
                            + "<SubItem>Yes</SubItem>"
                            + "<SubItem>0</SubItem>"// applyer
                            + "<SubItem>" + COUNTRY + "</SubItem>" //invoice number
                            + "<SubItem>" + WITHT + "</SubItem>" //advance date
                            + "<SubItem>" + WT_WITHCD + "</SubItem>" //doc number
                            + "<SubItem>" + QSATZ + "</SubItem>" //total amount
                            + "<SubItem>" + DESC + "</SubItem>"// tds code
                            + "</ListItem>";
                }
                System.out.println(" Final input xml for insertion " + xmlnew);
                formObject.NGAddListItem("q_orient_witholding", xmlnew);
                System.out.println("record inserted");

            }

        } catch (Exception e) {
            System.out.println("Exception is ::::" + e.getMessage());
        }
    }
}
