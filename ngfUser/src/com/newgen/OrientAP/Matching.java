/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.OrientAP;

import com.newgen.common.General;
import com.newgen.common.StringAddition;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListBox;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Latitude 3460
 */
public class Matching implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    StringAddition objStringAddition = null;
    XMLParser xmlParser = new XMLParser();
    List<List<String>> resultarray;
    private String invAmount;
    int count = 0;

    public void updateMatch(String caseType) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        objStringAddition = new StringAddition();
        float difftolerance = 5.0f;
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        IRepeater repeaterControlF6 = formObject.getRepeaterControl("Frame6");
        IRepeater repeaterControlF5 = formObject.getRepeaterControl("Frame5");
        ////System.out.println("Inside updateMatch");

        switch (caseType) {
            case "GSTN":
                if (formObject.getNGValue("GSTN").equalsIgnoreCase(formObject.getNGValue("gstn_sap"))) {
                    try {
                        int itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            if (itemValue.equalsIgnoreCase("- Purchase order Vendor GSTN not matching with Invoice GSTN")) {
                                formObject.removeItem("List1", i);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    int count = 0;
                    int itemCount = formObject.getItemCount("List1");
                    for (int i = 0; i < itemCount; i++) {
                        String itemValue = formObject.getNGValueAt("List1", i);
                        if (itemValue.equalsIgnoreCase("- Purchase order Vendor GSTN not matching with Invoice GSTN")) {
                            count++;
                            break;
                        }
                    }
                    if (count == 0) {
                        formObject.addItem("List1", "- Purchase order Vendor GSTN not matching with Invoice GSTN");
                    }
                }
                break;

            case "ORIENT GSTN":
                if (formObject.getNGValue("gstn_inv").equalsIgnoreCase(formObject.getNGValue("orient_gstn"))) {
                    try {
                        int itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            if (itemValue.equalsIgnoreCase("- Purchase order Orient GSTN not matching with Invoice GSTN")) {
                                formObject.removeItem("List1", i);
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    int count = 0;
                    int itemCount = formObject.getItemCount("List1");
                    for (int i = 0; i < itemCount; i++) {
                        String itemValue = formObject.getNGValueAt("List1", i);
                        if (itemValue.equalsIgnoreCase("- Purchase order Orient GSTN not matching with Invoice GSTN")) {
                            count++;
                            break;
                        }
                    }
                    if (count == 0) {
                        formObject.addItem("List1", "- Purchase order Orient GSTN not matching with Invoice GSTN");
                    }
                }
                break;

            case "PAN":
                invAmount = formObject.getNGValue("InvoiceAmount");
                // //System.out.println("Invoice Amount : " + invAmount);
                if (Float.parseFloat(invAmount) >= 200000) {
                    //  //System.out.println("Inside >=200000");
                    if (formObject.getNGValue("PAN").equalsIgnoreCase(formObject.getNGValue("pan_sap"))) {
                        try {
                            int itemCount = formObject.getItemCount("List1");
                            for (int i = 0; i < itemCount; i++) {
                                String itemValue = formObject.getNGValueAt("List1", i);
                                if (itemValue.equalsIgnoreCase("- Purchase order Vendor PAN not matching with Invoice PAN")) {
                                    formObject.removeItem("List1", i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        int count = 0;
                        int itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            if (itemValue.equalsIgnoreCase("- Purchase order Vendor PAN not matching with Invoice PAN")) {
                                count++;
                                break;
                            }
                        }
                        if (count == 0) {
                            formObject.addItem("List1", "- Purchase order Vendor PAN not matching with Invoice PAN");
                        }
                    }
                }
                break;

            case "PAN_INV":
                invAmount = formObject.getNGValue("InvoiceAmount");
                ////System.out.println("Invoice Amount : " + invAmount);
                if (Float.parseFloat(invAmount) >= 200000) {
                    //System.out.println("Inside >=200000");
                    if (formObject.getNGValue("pan_inv").equalsIgnoreCase(formObject.getNGValue("orient_PAN"))) {
                        try {
                            int itemCount = formObject.getItemCount("List1");
                            for (int i = 0; i < itemCount; i++) {
                                String itemValue = formObject.getNGValueAt("List1", i);
                                if (itemValue.equalsIgnoreCase("- Purchase order Orient PAN not matching with Invoice PAN")) {
                                    formObject.removeItem("List1", i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        int count = 0;
                        int itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            if (itemValue.equalsIgnoreCase("- Purchase order Orient PAN not matching with Invoice PAN")) {
                                count++;
                                break;
                            }
                        }
                        if (count == 0) {
                            formObject.addItem("List1", "- Purchase order Orient PAN not matching with Invoice PAN");
                        }
                    }
                }
                break;

            case "Total Amount":
                
               // System.out.println("Inside Total amount caseType");
                String invoiceAmount = formObject.getNGValue("InvoiceAmount");
                String total_inv = formObject.getNGValue("total_inv");
                if (total_inv.equalsIgnoreCase("")) {
                    total_inv = "0";
                }
                // String POLineAmount = getPOLineAmount();
                if (!invoiceAmount.equalsIgnoreCase("")) {
                    float difference_amount = Float.parseFloat(invoiceAmount) - Float.parseFloat(total_inv);
                    if (Float.parseFloat(invoiceAmount) == (Float.parseFloat(total_inv))
                            || (-difftolerance <= difference_amount && difference_amount <= difftolerance)) {
                        //System.out.println("Inside new amount check");
                        try {
                            int itemCount = formObject.getItemCount("List1");
                            for (int i = 0; i < itemCount; i++) {
                                String itemValue = formObject.getNGValueAt("List1", i);
                                //System.out.println("Item Value first if: " + itemValue);
                                if (itemValue.contains("Invoice total amount is not matching as per PO.")) {
                                    formObject.removeItem("List1", i);
                                    //System.out.println("Item Removed");
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        //System.out.println("Inside else TotalAmount match");
                        int count = 0;
                        int itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            //System.out.println("TotalAmount else-for / Item Value : " + itemValue);
                            if (itemValue.contains("Invoice total amount is not matching as per PO.")) {
                                count++;
                                break;
                            }
                        }
                         if (count ==0) {
                        
                            formObject.addItem("List1", "- Invoice total amount is not matching as per "
                                    + "PO. Invoice Amount: " + formObject.getNGValue("InvoiceAmount")
                                    + ", PO Amount: " + Float.parseFloat(total_inv) + ", Difference :"
                                    + (Float.parseFloat(total_inv) - Float.parseFloat(formObject.getNGValue("InvoiceAmount"))));
                            //System.out.println("Item Added");
                        }
                    }
                }
                break;

            case "Price Matching":
                String material_temp = "";
                int difference;
                //System.out.println("Inside Price Matching caseType");
                formObject = FormContext.getCurrentInstance().getFormReference();
                formConfig = FormContext.getCurrentInstance().getFormConfig();
                IRepeater repeaterControl1 = formObject.getRepeaterControl("Frame3");
                objGeneral = new General();
                String materialnumber = "";
                float priceperunit;
                // ListView lv9 = (ListView) formObject.getComponent(invoicegrid);
                int rowcountpo9 = repeaterControl1.getRepeaterRowCount();
                for (int l = 0; l < rowcountpo9; l++) {
                    boolean rep_check = objGeneral.isRepRowDeleted("Frame3", l);
                    if (!rep_check) {
                        material_temp = repeaterControl.getValue(l, "q_orient_invoice_item_item");
                        if (!material_temp.equalsIgnoreCase("")) {
                            materialnumber = objGeneral.MaterialAppend(material_temp);
                        }
//                        difference = 18 - material_temp.length();
//                        //System.out.println("Difference: " + difference);
//                        if (difference > 0) {
//                            //System.out.println("Inside differencce material");
//                            materialnumber = String.format("%0" + (18 - material_temp.length()) + "d%s", 0, material_temp);
//                            //System.out.println("material " + materialnumber);
//                        } else {
//                            materialnumber = material_temp;
//                        }                 
                        priceperunit = Float.parseFloat(repeaterControl.getValue(l, "q_orient_invoice_item_price_per_unit"));
                        if (matchMaterialNumber(materialnumber) == true) {
                            if (matchPriceUnit(materialnumber, priceperunit) == false) {
                                formObject.addItem("List1", "- Invoice and PO price per unit of material number " + materialnumber + " does not match");
                            }
                        } else {
                            formObject.addItem("List1", "- Invoice Material Number " + materialnumber + " is not matching with PO Item Number");
                        }
                    }
                }
                break;

            case "Service Total Amount":

                int itemCount = formObject.getItemCount("List1");
                for (int i = 0; i < itemCount; i++) {
                    String itemValue = formObject.getNGValueAt("List1", i);
                    //System.out.println("Item Value first if: " + itemValue);
                    if ("null".equalsIgnoreCase(itemValue) || null == itemValue) {
                        //System.out.println("Item value check");
                        //System.out.println("Value of item value ::::: " + itemValue);
                    } else {
                        if (itemValue.contains("Invoice total amount is not matching as per PO.")) {
                            try {
                                formObject.removeItem("List1", i);
                            } catch (Exception ex) {
                                Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //System.out.println("Item Removed");
                        }
                    }
                }

                int count = repeaterControlF5.getRepeaterRowCount();//repeaterControlF6.getRepeaterRowCount();
                float totalAmount = 0f,
                 totalAmount_tax = 0f;
                String taxcode;
                String gross_value = "0.0";
                for (int i = 0; i < count; i++) {
                    String selectFlag_val = repeaterControlF5.getValue(i, "CheckBox3");//repeaterControlF6.getValue(i, "q_orient_service_itemsel_select_flag");
                    if (selectFlag_val.equalsIgnoreCase("true")) {
                        String gross_value_temp = repeaterControlF5.getValue(i, "Text129");//repeaterControlF6.getValue(i, "q_orient_service_itemsel_gross_price");
                        // totalAmount = totalAmount + Float.parseFloat(gross_value);
                        gross_value = objStringAddition.getStringSum(gross_value_temp, gross_value);
                        //System.out.println("Gross Value = " + gross_value);
                    }
                }

                String serviceitem = formObject.getNGValue("service_itemselect");
                //System.out.println("Service item : " + serviceitem);

                if ("null".equalsIgnoreCase(serviceitem)
                        || null == serviceitem) {
                    String LVPO_item = "q_orient_po_item";
                    ListView lvPoItem = (ListView) formObject.getComponent(LVPO_item);
                    serviceitem = formObject.getNGValue(LVPO_item, 0, 1);
                    //System.out.println("service item inside null : " + serviceitem);

                }
                //System.out.println("Service item final: " + serviceitem);
                serviceitem = serviceitem.replaceAll("'", "%");
                String query = "select TAX_CODE from complex_orient_po_item where "
                        + "pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' and "
                        + "SHORT_TEXT like '" + serviceitem + "' ";
                //System.out.println("Query : " + query);
                resultarray = formObject.getDataFromDataSource(query);
                taxcode = resultarray.get(0).get(0);
                //System.out.println("Tax code : " + taxcode);

                // String tax_value = objGeneral.calculateTax(totalAmount + "", taxcode);
                String tax_value = objGeneral.calculateTax(gross_value, taxcode);
                //System.out.println("Tax Value : " + tax_value);

                String totalsum = objStringAddition.getStringSum(gross_value, tax_value);

                totalAmount_tax = Float.parseFloat(totalsum);
                //System.out.println("Total calculated amount with tax : " + totalsum);

                invoiceAmount = formObject.getNGValue("InvoiceAmount");
                float difference_amount = Float.parseFloat(invoiceAmount) - totalAmount_tax;
                if (Float.parseFloat(invoiceAmount) == (totalAmount_tax)
                        || (-difftolerance <= difference_amount && difference_amount <= difftolerance)) {
                    //System.out.println("Inside new amount check");
                    try {
                        itemCount = formObject.getItemCount("List1");
                        for (int i = 0; i < itemCount; i++) {
                            String itemValue = formObject.getNGValueAt("List1", i);
                            //System.out.println("Item Value first if: " + itemValue);
                            if (itemValue.contains("Invoice total amount is not matching as per PO.")) {
                                formObject.removeItem("List1", i);
                                //System.out.println("Item Removed");
                                break;
                            }
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(Matching.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //System.out.println("Inside else TotalAmount match");
                    count = 0;
                    itemCount = formObject.getItemCount("List1");
                    for (int i = 0; i < itemCount; i++) {
                        String itemValue = formObject.getNGValueAt("List1", i);
                        //System.out.println("TotalAmount else-for / Item Value : " + itemValue);
                        if (itemValue.contains("Invoice total amount is not matching as per PO.")) {
                            count++;
                            break;
                        }
                    }
                    if (count == 0) {
                        formObject.addItem("List1", "- Invoice total amount is not matching as per "
                                + "PO. Invoice Amount: " + formObject.getNGValue("InvoiceAmount")
                                + ", PO Amount: " + totalAmount_tax + ", Difference :"
                                + (totalAmount_tax - Float.parseFloat(formObject.getNGValue("InvoiceAmount"))));
                        //System.out.println("Item Added");
                    }
                }
                break;
        }

        if (formObject.getItemCount("List1") > 0
                && formObject.getWFActivityName().equalsIgnoreCase("Gate Entry")) {
            formObject.setNGValue("gateexception_flag", true);
            formObject.setNGValue("gate_decision", "Exception");
            formObject.setEnabled("gateexception_flag", false);
        } else if (formObject.getItemCount("List1") == 0
                && formObject.getWFActivityName().equalsIgnoreCase("Gate Entry")) {
            formObject.setNGValue("gateexception_flag", false);
            formObject.setNGValue("gate_decision", "Pending");
            formObject.setEnabled("gateexception_flag", true);
        }
    }

    public String getPOLineAmount() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        String linetotal = "", taxvalue = "", invoiceamount = "";// invoicegrid = "q_orient_invoice_item";
        float line, tax, sum = 0, invamount = 0;
        // ListView lv9 = (ListView) formObject.getComponent(invoicegrid);
        int rowcountpo9 = repeaterControl.getRepeaterRowCount();
        for (int l = 0; l < rowcountpo9; l++) {
            boolean rep_check = objGeneral.isRepRowDeleted("Frame3", l);

            if (!rep_check) {
                linetotal = repeaterControl.getValue(l, "q_Orient_invoice_item_line_total_wtax");
                taxvalue = repeaterControl.getValue(l, "q_Orient_invoice_item_taxvalue");
                //System.out.println("Line total : " + linetotal);
                line = Float.parseFloat(linetotal);
                if ("".equalsIgnoreCase(taxvalue)) {
                    tax = 0;
                } else {
                    tax = Float.parseFloat(taxvalue);
                }

                sum = sum + line + tax;
            }
        }
        linetotal = "" + sum;
        //System.out.println("line total as per PO " + linetotal);
        return linetotal;
    }

    public boolean matchPriceUnit(String materialnumber, float price) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        String LVPOname = "q_orient_po_item";
        ListView lv1 = (ListView) formObject.getComponent(LVPOname);
        objGeneral = new General();
        String material = "", priceperunit = "", perpcs = "";
        boolean result = false;
        int rowcountpo = lv1.getRowCount();
        for (int l = 0; l < rowcountpo; l++) {
            material = formObject.getNGValue(LVPOname, l, 0);
            material = objGeneral.MaterialAppend(material);
            priceperunit = formObject.getNGValue(LVPOname, l, 3);
            perpcs = formObject.getNGValue(LVPOname, l, 18);
            //System.out.println(material);
            //System.out.println(materialnumber);
            //System.out.println("-----float error" + priceperunit + "---"+perpcs);
            float perpcsprice = Float.parseFloat(priceperunit) / Float.parseFloat(perpcs);
            //System.out.println("perpcsPrice : " + perpcsprice);
            if (material.equalsIgnoreCase(materialnumber)) {
                if (perpcsprice == price) {
                    result = true;
                }
            }

        }
        return result;
    }

    public boolean matchMaterialNumber(String materialnumber) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        String LVPOname = "q_orient_po_item";
        ListView lv1 = (ListView) formObject.getComponent(LVPOname);
        objGeneral = new General();
        String material = "";
        boolean result = false;
        int rowcountpo = lv1.getRowCount();
        for (int l = 0; l < rowcountpo; l++) {
            material = formObject.getNGValue(LVPOname, l, 0);
            material = objGeneral.MaterialAppend(material);
            //System.out.println("matchMaterialNumber:PO:" + material + ":with Invoice:" + materialnumber);
            if (material.equalsIgnoreCase(materialnumber)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
