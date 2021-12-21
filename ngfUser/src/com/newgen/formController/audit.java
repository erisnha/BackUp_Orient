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
public class audit implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public void setAuditFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        
        formObject.setSheetVisible("Tab1", 8, false);
        formObject.setEnabled("Frame3", false);
        formObject.setSheetEnable("Tab1", 0, false);
         formObject.setNGValue("Text30", formObject.getNGValue("inv_post"));
        formObject.setNGValue("Text31", formObject.getNGValue("inv_park"));
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
        formObject.setSheetEnable("Tab1", 5, false);
        formObject.setSheetEnable("Tab1", 9, false);
        formObject.setSheetEnable("Tab1", 10, false);
        formObject.setSheetEnable("Tab2", 0, false);
        
        formObject.setSheetEnable("Tab1", 4, false);
        formObject.setBackcolor("PurchaseOrderNo", disablefield);
        formObject.setBackcolor("po_date", disablefield);
        formObject.setBackcolor("InvoiceNo", disablefield);
        formObject.setBackcolor("InvoiceDate", disablefield);
        formObject.setBackcolor("InvoiceAmount", disablefield);
        formObject.setSheetEnable("Tab1", 11, false);
        formObject.setVisible("Button1", false);
        formObject.setVisible("Button2", false);
        formObject.setVisible("Button6", false);
        formObject.setVisible("Combo9", false);
        formObject.setVisible("Label53", false);
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
        } else {
            formObject.setSheetVisible("Tab1", 6, false);
        }
    }
}
