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
public class store implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public void setStoreFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        formObject.setSelectedSheet("Tab1", 1);
        formObject.setSheetEnable("Tab1", 1, true);
        formObject.setSheetVisible("Tab1", 2, false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        formObject.setSheetVisible("Tab1", 6, false);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab1", 8, true);
        formObject.setSheetVisible("Tab2", 5, false);
        formObject.setSheetEnable("Tab1", 11, false);
        formObject.setVisible("Combo13", true);

        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("InvoiceAmount", true);
        formObject.setEnabled("InvoiceDate", true);
        formObject.setEnabled("InvoiceNo", true);
//        formObject.setEnabled("GSTN", false);
//        formObject.setEnabled("gstn_inv", false);
//        formObject.setEnabled("pan_inv", false);
//        formObject.setEnabled("PAN", false);
//        formObject.setBackcolor("GSTN", disablefield);
//        formObject.setBackcolor("gstn_inv", disablefield);
//        formObject.setBackcolor("pan_inv", disablefield);
//        formObject.setBackcolor("PAN", disablefield);

        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setSheetEnable("Tab2", 0, false);

        formObject.setSheetEnable("Tab1", 9, false);
        formObject.setSheetVisible("Tab1", 10, false);

        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
//
//        formObject.setVisible("Button2", false);
//        formObject.setVisible("store_decision", true);
//        formObject.setVisible("storehold_decsion", false);
//        formObject.setVisible("remarks_store", true);
//        formObject.setVisible("remarks_storehold", false);

        if ("Y".equalsIgnoreCase(formObject.getNGValue("storeChildWIFlag"))) {
            formObject.clear("movement_type");
            formObject.addItem("movement_type", "Purchase Return (161)", "161");
            //formObject.clear("movement_type");
            formObject.clear("store_decision");
            formObject.addItem("store_decision", "Migo Performed", "Migo Performed");
            formObject.setEnabled("store_decision", false);
        } else {
            formObject.addItem("movement_type", "Release GR Stock block (105)", "105");
            //System.out.println("combo9 " + formObject.getNGValueAt("movement_type", 0));
        }

        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.clear("movement_type");
            //formObject.addItem("movement_type", "161", "161");
            formObject.addItem("movement_type", "101", "101");
            formObject.addItem("Combo10", "Line Rejection", "0001");
            formObject.addItem("Combo10", "Short Material", "0002");

            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);

            formObject.clear("Combo10");

        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
            formObject.clear("movement_type");
            formObject.addItem("movement_type", "Release GR Stock block (105)", "105");
        }

    }

    public void setStoreHoldFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        formObject.setSelectedSheet("Tab1", 9);
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("InvoiceAmount", true);
        formObject.setEnabled("InvoiceDate", true);
        formObject.setEnabled("InvoiceNo", true);
        formObject.setSheetVisible("Tab1", 6, false);
        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setSheetEnable("Tab1", 0, false);
        formObject.setSheetEnable("Tab2", 0, false);
        formObject.setSheetVisible("Tab2", 5, false);
        //System.out.println("Above");
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab1", 8, false);
        //System.out.println("Below");
        formObject.setSheetVisible("Tab1", 2, false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        formObject.setVisible("Button2", false);
        formObject.setEnabled("GSTN", false);
        formObject.setEnabled("gstn_inv", false);
        formObject.setEnabled("pan_inv", false);
        formObject.setEnabled("PAN", false);
        formObject.setBackcolor("GSTN", disablefield);
        formObject.setBackcolor("gstn_inv", disablefield);
        formObject.setBackcolor("pan_inv", disablefield);
        formObject.setBackcolor("PAN", disablefield);
        formObject.setSheetEnable("Tab1", 1, false);
        formObject.setSheetEnable("Tab1", 9, true);
        formObject.setSheetVisible("Tab1", 10, false);
        formObject.setSheetEnable("Tab1", 11, false);
//
//        formObject.setVisible("store_decision", false);
//        formObject.setVisible("storehold_decsion", true);
//        formObject.setVisible("remarks_store", false);
//        formObject.setVisible("remarks_storehold", true);
//        formObject.setTop("remarks_storehold", 73);
//        formObject.setLeft("remarks_storehold", 83);
//        formObject.setTop("storehold_decsion", 35);
//        formObject.setLeft("storehold_decsion", 81);
        formObject.addItem("movement_type", "Release GR Stock block (105)", "105");
    }
}
