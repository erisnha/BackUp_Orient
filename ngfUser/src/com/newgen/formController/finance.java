/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.formController;

import static com.newgen.OrientAP.Head.disablefield;
import com.newgen.common.General;
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
public class finance implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public void setFinanceFormValidation() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        formObject.setSelectedSheet("Tab1", 4);
        formObject.setNGValue("Text30", formObject.getNGValue("inv_post"));
        formObject.setNGValue("Text31", formObject.getNGValue("inv_park"));
        formObject.setSheetVisible("Tab1", 11, false);
//                repeaterControl.setDisabled(0, "q_Orient_invoice_item_quantity", true);
//                repeaterControl.setDisabled(0, "q_Orient_invoice_item_batch", true);
//                repeaterControl.setDisabled(0, "q_Orient_invoice_item_store_location", true);
        formObject.setEnabled("Frame3", false);
        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setVisible("Frame5", false);
        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.setEnabled("Combo10", false);
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
        }
        formObject.setSheetEnable("Tab1", 1, false);
        formObject.setSheetEnable("Tab1", 2, false);
        formObject.setSheetEnable("Tab1", 3, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setSheetVisible("Tab2", 5, false);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab1", 8, false);
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
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setBackcolor("InvoiceAmount", disablefield);
        formObject.setEnabled("InvoiceAmount", false);
        formObject.setEnabled("InvoiceDate", false);
        formObject.setEnabled("InvoiceNo", false);

        formObject.setVisible("movement_type", false);
        formObject.setSheetEnable("Tab1", 5, false);
        formObject.setVisible("Button1", false);
        formObject.setVisible("Button2", false);
        formObject.setVisible("Button6", false);
        formObject.setVisible("Combo9", false);
        formObject.setVisible("Label53", false);
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
//        formObject.setEnabled("InvoiceAmount", false);
//        formObject.setEnabled("InvoiceDate", false);
//        formObject.setEnabled("InvoiceNo", false);
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

        if ("ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setSheetVisible("Tab1", 6, true);
            formObject.setSheetEnable("Tab1", 6, false);

            Query = "select service_itemselect from ext_orientAP where processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'";
            //System.out.println("Query fot item PO " + Query);
            List<List<String>> result = formObject.getDataFromDataSource(Query);
            //System.out.println("Result size : " + result.size() + " - value :" + result.get(0).get(0));
            //System.out.println("Inside Else : Result " + result.get(0).get(0));
            formObject.addItem("service_itemselect", result.get(0).get(0));

        } else {
            formObject.setSheetVisible("Tab1", 6, false);
        }


        //Fetch updated park document
        String inputXml_getFinalDoc = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_ACC_DOC_NO");
        inputXml_getFinalDoc = inputXml_getFinalDoc + "<Parameters><ImportParameters>"
                + "<LV_REFKEY>" + formObject.getNGValue("inv_park") + "2019" + "</LV_REFKEY>"
                + "</ImportParameters>"
                + "</Parameters>"
                + "</WFSAPInvokeFunction_Input>";
        String outputXml_getFinalDoc = objGeneral.executeWithCallBroker(inputXml_getFinalDoc, "" + "_ZBAPI_AP_AUTOMATION_ACC_DOC");
        xmlParser.setInputXML(outputXml_getFinalDoc);
        WFXmlResponse objXmlResponse_getFinalDoc = new WFXmlResponse(outputXml_getFinalDoc);
        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
            String AC_DOC_NO = xmlParser.getValueOf("AC_DOC_NO");
            if (!AC_DOC_NO.equalsIgnoreCase("")) {
                formObject.setNGValue("inv_park_sap", AC_DOC_NO);
            }
        }
        //

        //dms folder 
        String plant = "", path = "", po = "", invoice = "", pur_group = "", pur_org = "";
        pur_org = formObject.getNGValue("pur_org");
        pur_group = formObject.getNGValue("pur_group");
        plant = repeaterControl.getValue(0, "q_Orient_invoice_item_plant");
        po = formObject.getNGValue("PurchaseOrderNo");
        invoice = formObject.getNGValue("InvoiceNo");
        //System.out.println("Plant : " + plant + " po : " + po + " invoice : " + invoice + " pur_group: " + pur_group);
        if ("9000".equalsIgnoreCase(pur_group)) {
            String material = "", division = "";
            material = formObject.getNGValue("q_orient_po_item", 0, 0);
            division = objGeneral.DivisionOfPo(material);
            path = objGeneral.DMSPathforDepot(plant, division);
            //System.out.println("PATH : " + path);
            formObject.setNGValue("dmspath", path);
            formObject.RaiseEvent("WFSave");

        } else {
            path = objGeneral.DMSAdapterfolderplant(plant, po, invoice);
            formObject.setNGValue("dmspath", path);
            formObject.RaiseEvent("WFSave");
        }
    }
}
