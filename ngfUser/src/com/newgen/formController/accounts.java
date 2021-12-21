/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.formController;

import static com.newgen.OrientAP.Head.disablefield;
import com.newgen.common.General;
import com.newgen.common.WFXmlList;
import com.newgen.common.WFXmlResponse;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Latitude 3460
 */
public class accounts implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";
    String processInstanceId_accounts;

    public void setAccoutsFormValidation() {
        //System.out.println("Inside Accounts");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
         processInstanceId_accounts = formConfig.getConfigElement("ProcessInstanceId");
        objGeneral = new General();
        formObject.setVisible("Frame5", false);
        formObject.setSelectedSheet("Tab1", 3);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setVisible("Button12", true);
        formObject.setVisible("movement_type", false);
        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setEnabled("GSTN", false);
        formObject.setEnabled("gstn_inv", false);
        formObject.setEnabled("pan_inv", false);
        formObject.setEnabled("PAN", false);
        formObject.setBackcolor("GSTN", disablefield);
        formObject.setBackcolor("gstn_inv", disablefield);
        formObject.setBackcolor("pan_inv", disablefield);
        formObject.setBackcolor("PAN", disablefield);
        formObject.setSheetVisible("Tab1", 8, false);
        formObject.setSheetVisible("Tab1", 9, false);
        formObject.setSheetVisible("Tab1", 10, false);

        formObject.setSheetVisible("Tab1", 11, false);
        formObject.setEnabled("InvoiceAmount", true);
        formObject.setEnabled("InvoiceDate", true);
        formObject.setEnabled("InvoiceNo", true);

        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.setEnabled("Combo10", false);
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
        }

        if ("ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setSheetVisible("Tab1", 6, true);
            formObject.setSheetEnable("Tab1", 6, false);
            formObject.setSheetVisible("Tab2", 1, false);
            formObject.setSheetEnable("Tab2", 4, true);
            formObject.setEnabled("service_itemselect", false);
            formObject.setEnabled("Frame6", false);
            formObject.setEnabled("DatePicker4", false);
            formObject.setEnabled("DatePicker5", false);

//            formObject.setVisible("accounts_decision", true);
//            formObject.setVisible("Label88", true);

            Query = "select service_itemselect from ext_orientAP where "
                    + "processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'";
            //System.out.println("Query fot item PO " + Query);
            List<List<String>> result = formObject.getDataFromDataSource(Query);
            //System.out.println("Result size : " + result.size() + " -Selected item value :" + result.get(0).get(0));
            //System.out.println("Inside Else : Result " + result.get(0).get(0));
            formObject.addItem("service_itemselect", result.get(0).get(0));

            Query = "select TAX_CODE from complex_orient_po_item where "
                    + "pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                    + "and SHORT_TEXT = '" + result.get(0).get(0) + "'";
            //System.out.println("Query fot item PO " + Query);
            result = formObject.getDataFromDataSource(Query);
            //System.out.println("Result size : " + result.size() + " - Tax Code value :" + result.get(0).get(0));
            formObject.setNGValue("taxcode_service", result.get(0).get(0));

        } else {
            formObject.setSheetVisible("Tab1", 6, false);
//            formObject.setVisible("accounts_decision", false);
//            formObject.setVisible("Label88", false);
        }

        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setSheetEnable("Tab1", 1, false);
        formObject.setSheetEnable("Tab1", 2, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setSheetEnable("Tab1", 5, false);
        formObject.setVisible("Button1", false);
        formObject.setVisible("Button2", false);
        formObject.setVisible("Button6", false);
        formObject.setVisible("Combo9", false);
        formObject.setVisible("Label53", false);
        formObject.setSheetVisible("Tab1", 4, false);
        if ("".equalsIgnoreCase(formObject.getNGValue("remarks_service"))) {
            formObject.setSheetVisible("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 6, false);

        } else {
            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetVisible("Tab1", 1, false);
            formObject.setSheetVisible("Tab1", 2, false);
            formObject.setVisible("entrysheet_no", true);
            formObject.setEnabled("entrysheet_no", false);
            formObject.setVisible("Label69", true);
            formObject.setSheetEnable("Tab1", 5, false);
            formObject.setSheetVisible("Tab1", 5, true);
            formObject.setSheetVisible("Tab1", 6, true);
            formObject.setSheetEnable("Tab1", 6, false);
        }
        //System.out.println("Befor setting inv park value");
        formObject.setNGValue("Text31", formObject.getNGValue("inv_park"));
        //System.out.println("After setting inv park value");

        /*if ("9000".equalsIgnoreCase(formObject.getNGValue("pur_group"))) {
                    int repCount = repeaterControl.getRepeaterRowCount();
                    for (int i = 0; i < repCount; i++) {
                        String item = repeaterControl.getValue(i, "q_Orient_invoice_item_item");
                        Query = "select MATERIAL,QUANTITY,VAL_FORCUR from complex_orient_poitem_history "
                                + "where pinstanceid = '" + processInstanceId + "' "
                                + "and ref_doc_no_long = '" + formObject.getNGValue("InvoiceNo") + "' "
                                + "and material = '"+item+"'";
                        System.out.println("Query for depo :  " + Query);
                        resultarray = formObject.getDataFromDataSource(Query);
                        if (resultarray.size() > 0) {
                            System.out.println("Inside result more than 0 and setting repeater row updated data.");
                            repeaterControl.setValue(i, "q_Orient_invoice_item_quantity", resultarray.get(i).get(1));
                            repeaterControl.setValue(i, "q_Orient_invoice_item_amount_wtax", resultarray.get(i).get(2));
                        } else {
                            System.out.println("Indside 0 row count. deleteing row from repeater.");
                            repeaterControl.removeRow(i);
                        }
                    }

                } */
        formObject.clear("ListView8");
        String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_TDS");
        inputXml = inputXml + "<Parameters>"
                + "<ImportParameters>"
                + "<VENDOR>" + formObject.getNGValue("vendor_code") + "</VENDOR>"
                + "<COMP_CODE>1000</COMP_CODE>"
                + "</ImportParameters>"
                + "</Parameters>"
                + "</WFSAPInvokeFunction_Input>";
        String outputXml = objGeneral.executeWithCallBroker(inputXml,processInstanceId_accounts+"_ZBAPI_AP_AUTOMATION_TDS");
        xmlParser.setInputXML(outputXml);
        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
        // System.out.println("After xml response outputxml : " + outputXml);
        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

            String xmlnew = "";
            for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TDS_TAB"); objList.hasMoreElements(); objList.skip()) {
                String COUNTRY = objList.getVal("COUNTRY");
                String WITHT = objList.getVal("WITHT");
                String WT_WITHCD = objList.getVal("WT_WITHCD");
                String QSATZ = objList.getVal("QSATZ");
                String DESC = objList.getVal("DESC");

                formObject.addItem("Combo7", DESC);

                xmlnew = xmlnew + "<ListItem><SubItem>" + COUNTRY
                        + "</SubItem><SubItem>" + WITHT
                        + "</SubItem><SubItem>" + WT_WITHCD
                        + "</SubItem><SubItem>" + QSATZ
                        + "</SubItem><SubItem>" + DESC
                        + "</SubItem></ListItem>";
            }
            //System.out.println("XML : " + xmlnew);
            formObject.NGAddListItem("ListView8", xmlnew);

        }
    }
}
