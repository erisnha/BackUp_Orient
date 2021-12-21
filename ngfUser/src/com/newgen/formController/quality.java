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

/**
 *
 * @author Latitude 3460
 */
public class quality implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public void setQualityFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_quantity", true);
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_batch", true);
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_store_location", true);
        formObject.setSelectedSheet("Tab1", 2);
        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setBackcolor("InvoiceAmount", disablefield);
        formObject.setSheetEnable("Tab1", 11, false);
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("InvoiceAmount", false);
        formObject.setEnabled("InvoiceDate", false);
        formObject.setEnabled("InvoiceNo", false);
        formObject.setEnabled("GSTN", false);
        formObject.setEnabled("gstn_inv", false);
        formObject.setEnabled("pan_inv", false);
        formObject.setEnabled("PAN", false);
        formObject.setBackcolor("GSTN", disablefield);
        formObject.setBackcolor("gstn_inv", disablefield);
        formObject.setBackcolor("pan_inv", disablefield);
        formObject.setBackcolor("PAN", disablefield);
        formObject.setSheetVisible("Tab1", 6, false);
        formObject.setSheetEnable("Tab1", 9, false);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setSheetEnable("Tab1", 1, false);
        formObject.setSheetVisible("Tab1", 8, false);
        formObject.setSheetVisible("Tab2", 5, false);
        if ("Y".equalsIgnoreCase(formObject.getNGValue("qualityChildWIFlag"))) {

            formObject.clear("quality_decision");
            formObject.addItem("quality_decision", "Quality Accepted", "Quality Accepted");
            formObject.setEnabled("quality_decision", false);
        }
        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.setEnabled("Combo10", false);
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
        }
        formObject.setVisible("Button1", false);
        formObject.setVisible("Button2", false);
        formObject.setVisible("Button6", false);
        formObject.setVisible("Combo9", false);
        formObject.setVisible("movement_type", false);
        formObject.setVisible("Label53", false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        formObject.setSheetEnable("Tab1", 7, false);
        formObject.setSheetEnable("Tab1", 8, false);
        formObject.setSheetVisible("Tab1", 10, true);
        formObject.setSheetEnable("Tab1", 10, false);
//        formObject.setVisible("quality_decision", true);
//        formObject.setVisible("qualityhold_decision", false);
//        formObject.setVisible("remarks_quality", true);
//        formObject.setVisible("remarks_qualityhold", false);
    }

    public void setQualityHoldFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_quantity", true);
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_batch", true);
        repeaterControl.setDisabled(0, "q_Orient_invoice_item_store_location", true);

        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setBackcolor("InvoiceAmount", disablefield);
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("GSTN", false);
        formObject.setEnabled("gstn_inv", false);
        formObject.setEnabled("pan_inv", false);
        formObject.setEnabled("PAN", false);
        formObject.setBackcolor("GSTN", disablefield);
        formObject.setBackcolor("gstn_inv", disablefield);
        formObject.setBackcolor("pan_inv", disablefield);
        formObject.setBackcolor("PAN", disablefield);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab1", 8, false);
        formObject.setSheetEnable("Tab1", 2, false);
        formObject.setSheetEnable("Tab1", 9, false);
        formObject.setSheetEnable("Tab1", 11, false);
        formObject.setEnabled("InvoiceAmount", false);
        formObject.setEnabled("InvoiceDate", false);
        formObject.setEnabled("InvoiceNo", false);
        formObject.setSheetVisible("Tab1", 6, false);
        formObject.setSheetVisible("Tab2", 5, false);
        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setSheetEnable("Tab1", 1, false);
        formObject.setSheetVisible("Tab1", 10, true);
        formObject.setSheetEnable("Tab1", 10, true);
        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.setEnabled("Combo10", false);
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
        }
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        //still need to fix left and top and fix the remarks
//        formObject.setVisible("quality_decision", false);
//        formObject.setVisible("qualityhold_decision", true);
//        formObject.setVisible("remarks_quality", false);
//        formObject.setVisible("remarks_qualityhold", true);
//        formObject.setTop("remarks_qualityhold", 73);
//        formObject.setLeft("remarks_qualityhold", 83);
//        formObject.setTop("qualityhold_decision", 35);
//        formObject.setLeft("qualityhold_decision", 81);
    }

}
