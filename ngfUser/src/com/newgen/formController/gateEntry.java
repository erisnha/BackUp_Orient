/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.formController;

import com.newgen.common.General;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Latitude 3460
 */
public class gateEntry implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public void setGEFormValidation() {
        //System.out.println("Inside Gate Entry");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        boolean checkFormat;
        if (formObject.getNGValue("invdate_sap").matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
            formObject.setNGValue("InvoiceDate", formObject.getNGValue("invdate_sap"));
            checkFormat = true;
        } else {
            checkFormat = false;
        }

        String ponum = formObject.getNGValue("PurchaseOrderNo");
        String invoiceno = formObject.getNGValue("InvoiceNo");
        String invdate = formObject.getNGValue("InvoiceDate");
        String GateEntryNo = objGeneral.convertnamedatetosapdate(invdate);

        //System.out.println("punum : " + ponum + "invoiceno : " + invoiceno + "invdate : " + invdate + "GateEntryNo : " + GateEntryNo);
        String inv_date_extracted = formObject.getNGValue("invdate_sap");
        //System.out.println("Extracted Invoice Date : " + inv_date_extracted);
//                Query = "update ext_orientAP set "
//                        + "GateEntryNo='" + GateEntryNo + "' "
//                        + "where processid='" + processInstanceId + "'";
//                formObject.saveDataIntoDataSource(Query);
        formObject.setNGValue("GateEntryNo", GateEntryNo);
        formObject.RaiseEvent("WFSave");
        //---------------------------------------
        formObject.setEnabled("PurchaseOrderNo", false);
        formObject.setEnabled("po_date", false);
        formObject.setEnabled("InvoiceAmount", true);
        formObject.setEnabled("InvoiceDate", true);
        formObject.setEnabled("InvoiceNo", true);
        formObject.setSheetEnable("Tab1", 11, false);
        formObject.setSheetVisible("Tab1", 1, false);
        formObject.setSheetVisible("Tab1", 2, false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        formObject.setSheetVisible("Tab1", 6, false);
        formObject.setSheetVisible("Tab1", 7, false);
        formObject.setSheetVisible("Tab2", 5, false);
        formObject.setSheetVisible("Tab1", 9, false);
        formObject.setSheetVisible("Tab1", 10, false);

        //System.out.println("Fields disabled");

        Query = "select distinct PROFIT_CTR, PLANT from complex_orient_po_item "
                + "where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                + " and PROFIT_CTR is not null and plant is not null";
        //System.out.println("Query for profit center : " + Query);
        List<List<String>> result = formObject.getDataFromDataSource(Query);
        String Plant = result.get(0).get(1);
        int plant_int = Integer.parseInt(Plant);
        if (result.size() > 0) {
            //System.out.println("Profit Center value : " + result.get(0).get(0));
            formObject.setNGValue("ProfitCentre", result.get(0).get(0));
            formObject.setNGValue("Plant", result.get(0).get(1));
        }

        String vendor_code = formObject.getNGValue("vendor_code");
        String orient_code = "P" + plant_int;

        //String 
        String vendorGSTN = objGeneral.getVendorGSTN(vendor_code);
        //System.out.println("Vendor GSTN : " + vendorGSTN);
        formObject.setNGValue("gstn_sap", vendorGSTN);

        String orientGSTN = objGeneral.getVendorGSTN(orient_code);
        //System.out.println("Orient GSTN : " + orientGSTN);
        formObject.setNGValue("orient_gstn", orientGSTN);

        String vendorPAN = objGeneral.getVendorPAN(vendor_code);
        //System.out.println("Vendor PAN : " + vendorPAN);
        formObject.setNGValue("pan_sap", vendorPAN);

        String orientPAN = objGeneral.getVendorPAN(orient_code);
        //System.out.println("Orient PAN : " + orientPAN);
        formObject.setNGValue("orient_PAN", orientPAN);

        if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            formObject.clear("movement_type");
            //formObject.addItem("movement_type", "161", "161");
            formObject.addItem("movement_type", "Goods Receipt (101)", "101");
            formObject.setVisible("Label65", true);
            formObject.setVisible("Combo10", true);
            formObject.clear("Combo10");
            formObject.addItem("Combo10", "Line Rejection", "0001");
            formObject.addItem("Combo10", "Short Material", "0002");
        } else {
            formObject.setVisible("Label65", false);
            formObject.setVisible("Combo10", false);
            formObject.clear("movement_type");

            if ("9000".equalsIgnoreCase(formObject.getNGValue("pur_org"))) {
                formObject.addItem("movement_type", "Goods Receipt (101)", "101");
                formObject.setVisible("Button1", false);
            } else {
                formObject.addItem("movement_type", "Goods Receipt (103)", "103");
                formObject.addItem("movement_type", "Goods Receipt (101)", "101");

            }
        }

//                if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
//                    formObject.clear("movement_type");
//                    formObject.addItem("movement_type", "161", "161");
//                } else {
//                    formObject.clear("movement_type");
//                    formObject.addItem("movement_type", "Goods Receipt (101)", "101");
//                    
//                    formObject.addItem("movement_type", "Goods Receipt (103)", "103");
//                }
        //System.out.println("combo9 " + formObject.getNGValueAt("movement_type", 0));



    }
}
