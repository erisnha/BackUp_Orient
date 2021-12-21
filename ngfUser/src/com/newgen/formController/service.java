/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.formController;

import static com.newgen.OrientAP.Head.disablefield;
import com.newgen.common.General;
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
public class service implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";
    List<List<String>> result;

    public void setServiceFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();

        formObject.setVisible("Frame5", false);
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("InvoiceAmount", false);
        formObject.setEnabled("InvoiceDate", false);
        formObject.setEnabled("InvoiceNo", false);
        formObject.setEnabled("asn_number", false);
        formObject.setSheetVisible("Tab1", 11, false);
        formObject.setEnabled("GSTN", false);
        formObject.setEnabled("gstn_inv", false);
        formObject.setEnabled("pan_inv", false);
        formObject.setEnabled("PAN", false);

        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setBackcolor("InvoiceAmount", disablefield);
        formObject.setBackcolor("asn_number", disablefield);

        formObject.setBackcolor("GSTN", disablefield);
        formObject.setBackcolor("gstn_inv", disablefield);
        formObject.setBackcolor("pan_inv", disablefield);
        formObject.setBackcolor("PAN", disablefield);

        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, true);
        formObject.setSheetVisible("Tab1", 0, false);
        formObject.setSheetVisible("Tab1", 1, false);
        formObject.setSheetVisible("Tab1", 2, false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, true);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab1", 8, false);
        formObject.setSheetVisible("Tab2", 5, false);
        formObject.setSheetVisible("Tab2", 1, false);
        formObject.setSheetVisible("Tab1", 9, false);

        formObject.setSheetEnable("Tab1", 5, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setEnabled("Frame6", false);

        formObject.setVisible("Button2", false);
        formObject.setVisible("store_decision", false);
        formObject.setVisible("storehold_decsion", false);
        formObject.setVisible("remarks_store", false);
        formObject.setVisible("remarks_storehold", false);

        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.setEnabled("Combo10", false);
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
        }
        Query = "select service_itemselect from ext_orientAP where processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'";
        //System.out.println("Query fot item PO " + Query);
        result = formObject.getDataFromDataSource(Query);
        //System.out.println("Result size : " + result.size() + " - value :" + result.get(0).get(0));
        //System.out.println("Inside Else : Result " + result.get(0).get(0));
        formObject.addItem("service_itemselect", result.get(0).get(0));
    }
}
