/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.common;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import java.util.ArrayList;
import java.util.List;
import com.newgen.common.General;
import java.awt.Color;
import java.math.BigDecimal;

/**
 *
 * @author Latitude 3460
 */
public class RepeaterController {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    List<List<String>> resultarray;

    public void setInvoiceLine() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        List<String> HeaderNames = new ArrayList<String>();
        HeaderNames.add("Material");
        HeaderNames.add("Item Description");
        HeaderNames.add("Quantity");
        HeaderNames.add("Price per Unit");
        HeaderNames.add("Line total");
        HeaderNames.add("Total amount");
        HeaderNames.add("Plant");
        HeaderNames.add("Unit");
        HeaderNames.add("PO Item");
        HeaderNames.add("Base UOM");
        HeaderNames.add("Base UOM ISO");
        HeaderNames.add("Tax Code");
        HeaderNames.add("Location");
        HeaderNames.add("Profit Centre");
        HeaderNames.add("Batch");
        HeaderNames.add("Valuation");
        HeaderNames.add("Tax Value");
        HeaderNames.add("QA required");
        HeaderNames.add("Po Number");
        HeaderNames.add("ASN Item");
        repeaterControl.setRepeaterHeaders(HeaderNames);
        //System.out.println("Set headers");
        repeaterControl.setRepeaterLinkColor(Color.white);

        int repeater_rowcount = repeaterControl.getRepeaterRowCount();
        //System.out.println("Rep row count" + repeater_rowcount);
        if (repeater_rowcount == 0 && "ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
            //System.out.println("Inside repeater rowcount = 0");
            String LVPO_item = "q_orient_po_item";
            ListView lvPoItem = (ListView) formObject.getComponent(LVPO_item);
            int lvPoItemRowCount = lvPoItem.getRowCount();
            for (int i = 0; i < lvPoItemRowCount; i++) {
                //System.out.println("Inside lv for");
                String Item = formObject.getNGValue(LVPO_item, i, 0);
                String Item_Description = formObject.getNGValue(LVPO_item, i, 1);
                String Quantity = formObject.getNGValue(LVPO_item, i, 2);
                String Price_per_Unit = formObject.getNGValue(LVPO_item, i, 3);
                String Line_total = formObject.getNGValue(LVPO_item, i, 4);
                String Total_amount = formObject.getNGValue(LVPO_item, i, 5);
                String Plant = formObject.getNGValue(LVPO_item, i, 6);
                String Unit = formObject.getNGValue(LVPO_item, i, 7);
                String PO_Item = formObject.getNGValue(LVPO_item, i, 8);
                String Base_UOM = formObject.getNGValue(LVPO_item, i, 9);
                String Base_UOM_ISO = formObject.getNGValue(LVPO_item, i, 10);
                String Tax_Code = formObject.getNGValue(LVPO_item, i, 11);
                String Location = formObject.getNGValue(LVPO_item, i, 12);
                String Profit_Centre = formObject.getNGValue(LVPO_item, i, 13);
                String Valuation = formObject.getNGValue(LVPO_item, i, 15);
                String Batch = "";
                String Tax_Value = "";

                if (!Item.equalsIgnoreCase("")) {
                    Batch = objGeneral.calculateBatch(Item);
                }
                String line_total;
                float quan, price, total, totalwithtax;
                quan = Float.parseFloat(Quantity);
                price = Float.parseFloat(Price_per_Unit);
                total = quan * price;
                line_total = "" + total;
                Tax_Value = objGeneral.calculateTax(line_total, Tax_Code);
                //System.out.println("Line total" + line_total + "Tax Value" + Tax_Value);
                if (Tax_Value.equalsIgnoreCase("")) {
                    totalwithtax = Float.parseFloat(line_total);
                } else {
                    totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(Tax_Value);
                }

                //System.out.println("Batch created : " + Batch + "/Total with tax : " + totalwithtax);
                Total_amount = "" + totalwithtax;
                //  String qa_required = objGeneral.getQACheck(Item, Plant);
                //    System.out.println("qa_required " + qa_required);
//                System.out.println(Item + "-" + Item_Description + "-" + Quantity + "-" + Price_per_Unit + "-" + Line_total + "-"
//                        + Total_amount + "-" + Plant + "-" + Unit + "-" + PO_Item + "-" + Base_UOM + "-" + Base_UOM_ISO + "-" + Tax_Code + "-"
//                        + Location + "-" + Profit_Centre + "-" + Valuation + "-" + Batch + "-" + Tax_Value);
                repeaterControl.addRow();
                repeaterControl.setValue(i, "q_Orient_invoice_item_item", Item);
                repeaterControl.setValue(i, "q_Orient_invoice_item_item_desc", Item_Description);
                repeaterControl.setValue(i, "q_Orient_invoice_item_quantity", Quantity);
                repeaterControl.setValue(i, "q_Orient_invoice_item_price_per_unit", Price_per_Unit);
                repeaterControl.setValue(i, "q_Orient_invoice_item_line_total_wtax", Line_total);
                repeaterControl.setValue(i, "q_Orient_invoice_item_amount_wtax", Total_amount);
                repeaterControl.setValue(i, "q_Orient_invoice_item_plant", Plant);
                repeaterControl.setValue(i, "q_Orient_invoice_item_weightunit", Unit);
                repeaterControl.setValue(i, "q_Orient_invoice_item_po_item", PO_Item);
                repeaterControl.setValue(i, "q_Orient_invoice_item_base_uom", Base_UOM);
                repeaterControl.setValue(i, "q_Orient_invoice_item_base_uom_iso", Base_UOM_ISO);
                repeaterControl.setValue(i, "q_Orient_invoice_item_tax_code", Tax_Code);
                repeaterControl.setValue(i, "q_Orient_invoice_item_store_location", Location);
                repeaterControl.setValue(i, "q_Orient_invoice_item_profit_centre", Profit_Centre);
                repeaterControl.setValue(i, "q_Orient_invoice_item_batch", Batch);
                repeaterControl.setValue(i, "q_Orient_invoice_item_valuation", Valuation);
                repeaterControl.setValue(i, "q_Orient_invoice_item_taxvalue", Tax_Value);
                //   repeaterControl.setValue(i, "q_Orient_invoice_item_qa_required", qa_required);
                repeaterControl.setValue(i, "q_Orient_invoice_item_qa_required", "");
                repeaterControl.setValue(i, "q_orient_invoice_item_purchaseorder", "");
                repeaterControl.setValue(i, "q_orient_invoice_item_asn_item", "");
            }
        } else {
            //System.out.println("Inside else");
            for (int i = 0; i < repeater_rowcount; i++) {

                String quantity = repeaterControl.getValue(i, "q_orient_invoice_item_quantity");
                String priceperunit = repeaterControl.getValue(i, "q_orient_invoice_item_price_per_unit");
                String linetotal = repeaterControl.getValue(i, "q_orient_invoice_item_line_total_wtax");
             //   String po_item = repeaterControl.getValue(i, "q_orient_invoice_item_po_item");
                quantity = objGeneral.checkSpace(quantity);
                priceperunit = objGeneral.checkSpace(priceperunit);
                linetotal = objGeneral.checkSpace(linetotal);

                repeaterControl.setValue(i, "q_orient_invoice_item_quantity", quantity);
                repeaterControl.setValue(i, "q_orient_invoice_item_price_per_unit", priceperunit);
                repeaterControl.setValue(i, "q_orient_invoice_item_line_total_wtax", linetotal);

                String material = objGeneral.MaterialAppend(repeaterControl.getValue(i, "q_Orient_invoice_item_item"));
                //System.out.println("Material : " + material);
                String Query = "select PLANT, UNIT, PO_ITEM, BASE_UNIT, BASE_UOM_ISO, TAX_CODE, STORE_LOC, PROFIT_CTR, VAL_TYPE,PO_NUMBER "
                        + "from complex_orient_po_item where "
                        + "pinstanceid='" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                        + "and MATERIAL like '%" + material + "' "
                        + "and PO_NUMBER = '"+repeaterControl.getValue(i, "q_orient_invoice_item_purchaseorder")+"'";
                //System.out.println("Query itemdata : " + Query);
                resultarray = formObject.getDataFromDataSource(Query);
                //System.out.println("Result size : " + resultarray.size());
                if (resultarray.size() > 0) {
                    String Batch = objGeneral.calculateBatch(material);
                    //System.out.println("After batch calculate" + Batch);
                    repeaterControl.setValue(i, "q_Orient_invoice_item_batch", Batch);
                    repeaterControl.setValue(i, "q_Orient_invoice_item_plant", resultarray.get(0).get(0));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_weightunit", resultarray.get(0).get(1));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_po_item", resultarray.get(0).get(2));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_base_uom", resultarray.get(0).get(3));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_base_uom_iso", resultarray.get(0).get(4));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_tax_code", resultarray.get(0).get(5));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_store_location", resultarray.get(0).get(6));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_profit_centre", resultarray.get(0).get(7));
                    repeaterControl.setValue(i, "q_Orient_invoice_item_valuation", resultarray.get(0).get(8));
                    repeaterControl.setValue(i, "q_orient_invoice_item_purchaseorder", resultarray.get(0).get(9));
                    String line_total = repeaterControl.getValue(i, "q_orient_invoice_item_line_total_wtax");
                    String Tax_Value = objGeneral.calculateTax(line_total, resultarray.get(0).get(5));
                    float totalwithtax;
                    if (Tax_Value.equalsIgnoreCase("")) {
                        totalwithtax = Float.parseFloat(line_total);
                    } else {
                        totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(Tax_Value);
                    }
                    String Total_amount = "" + totalwithtax;

                    repeaterControl.setValue(i, "q_Orient_invoice_item_taxvalue", Tax_Value);
                    repeaterControl.setValue(i, "q_Orient_invoice_item_amount_wtax", Total_amount);
                }
            }
        }
    }
}
