
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.OrientAP;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import com.newgen.bapi.*;
import static com.newgen.common.General.convertNewgenDateToSapDate;
import com.newgen.common.*;
import com.newgen.formController.*;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

public class Head implements FormListener {

    //FormReference objFormReference = null; //FormContext.getCurrentInstance().getFormReference();
    FormReference formObject = null;
    FormConfig formConfig = null;
    String activityName = null;
    String engineName = null;
    String sessionId = null;
    String folderId = null;
    String serverUrl = null;
    String processInstanceId = null;
    String workItemId = null;
    String userName = null;
    String processDefId = null;
    String Query = null;
    String tempdata1;
    String today, today1;
    String LVControlName;
    int fiscalYear;
    int difference;
    List<List<String>> result;
    List<List<String>> resultarray;
    PickList objPicklist;
    List<List<String>> resultarray1;
    General objGeneral = null;
    MigoCheck migocheck = null;
    introduction intro = null;
    gateEntry objgateentry = null;
    store objstore = null;
    accounts objaccounts = null;
    finance objfinance = null;
    quality objquality = null;
    service objservice = null;
    audit objaudit = null;
    Matching objmatching = null;
    taxcode_list objtaxcode_list = null;
    StringAddition objStringAddition = null;
    goodsmvt_cancel objgoodsmvt_cancel = null;
    entrysheet_delete objentrysheet_delete = null;
    hsn_sac objhsn_sac = null;
    po_getdetail objpo_getdetail = null;
    asn_detail objasn_detail = null;
    XMLParser xmlParser = new XMLParser();
    public static Color disablefield = new Color(239, 237, 237);

    @Override
    public void continueExecution(String arg0, HashMap<String, String> arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        IRepeater repeaterControlF4 = formObject.getRepeaterControl("Frame4");
        IRepeater repeaterControlF6 = formObject.getRepeaterControl("Frame6");
        IRepeater repeaterControlF5 = formObject.getRepeaterControl("Frame5");
        objGeneral = new General();
        objmatching = new Matching();
        objStringAddition = new StringAddition();
        objpo_getdetail = new po_getdetail();
        objasn_detail = new asn_detail();
        if (pEvent.getType().name().equalsIgnoreCase("VALUE_CHANGED")) {
            //******************************************************************************************************************   
//****************RAhul 15/oct/2020******************//


            if (pEvent.getSource().getName().equalsIgnoreCase("po_total")) {
                String tcs_flag = formObject.getNGValue("tsc_flag");

                if (tcs_flag.equalsIgnoreCase("Yes")) {
                    formObject.clear("tcs");
                    String po_total = formObject.getNGValue("po_total");
                    double po_total_db = Double.parseDouble(po_total);
                    // double tcs = 0.00075 * po_total_db;

                    double tcs = 0.001 * po_total_db;

                    double tcs_roundOff = Math.round(tcs * 100.0) / 100.0;
                    String tcs1 = String.valueOf(tcs_roundOff);
                    System.out.println("TCS1 is ::-" + tcs1);
                    formObject.setNGValue("tcs", tcs1);

                    String freight = formObject.getNGValue("freight_value");
                    System.out.println("Freight Value is ::" + freight);
                    if (freight.equalsIgnoreCase("")) {
                        freight = "0";
                    }
                    double total_inv = po_total_db + tcs_roundOff + Double.parseDouble(freight);
                    double total_inv_roundOff = Math.round(total_inv * 100.0) / 100.0;
                    String total_invoice = String.valueOf(total_inv_roundOff);
                    formObject.setNGValue("total_inv", total_invoice);//po_total+tcs+freight

                    formObject.RaiseEvent("WFSave");
                    objmatching.updateMatch("Total Amount");
                } else {
                    formObject.clear("tcs");
                    String po_total = formObject.getNGValue("po_total");
                    double po_total_db = Double.parseDouble(po_total);


                    String freight = formObject.getNGValue("freight_value");
                    System.out.println("Freight Value is ::" + freight);
                    if (freight.equalsIgnoreCase("")) {
                        freight = "0";
                    }
                    double total_inv = po_total_db + 0.0 + Double.parseDouble(freight);
                    double total_inv_roundOff = Math.round(total_inv * 100.0) / 100.0;
                    String total_invoice = String.valueOf(total_inv_roundOff);
                    formObject.setNGValue("total_inv", total_invoice);//po_total+tcs+freight

                    formObject.RaiseEvent("WFSave");
                    objmatching.updateMatch("Total Amount");
                }


            }

            if (pEvent.getSource().getName().equalsIgnoreCase("tsc_flag")) {
                String tcs_flag = formObject.getNGValue("tsc_flag");
                if (tcs_flag.equalsIgnoreCase("No")) {
                    String po_total = formObject.getNGValue("po_total");
                    formObject.clear("tcs");
                    formObject.setNGValue("tcs", "0.00");

                    String freight = formObject.getNGValue("freight_value");
                    System.out.println("Freight Value is 1::" + freight);
                    if (freight.equalsIgnoreCase("")) {
                        freight = "0";
                    }
                    double total_inv = Double.parseDouble(po_total) + 0.0 + Double.parseDouble(freight);
                    double total_inv_roundOff = Math.round(total_inv * 100.0) / 100.0;
                    String total_invoice = String.valueOf(total_inv_roundOff);
                    formObject.setNGValue("total_inv", total_invoice);//po_total+tcs+freight

                    formObject.RaiseEvent("WFSave");
                    objmatching.updateMatch("Total Amount");

                } else {
                    formObject.clear("tcs");
                    String po_total = formObject.getNGValue("po_total");
                    double po_total_db = Double.parseDouble(po_total);
                    double tcs = 0.001 * po_total_db;
                    double tcs_roundOff = Math.round(tcs * 100.0) / 100.0;
                    String tcs1 = String.valueOf(tcs_roundOff);
                    formObject.setNGValue("tcs", tcs1);

                    String freight = formObject.getNGValue("freight_value");
                    System.out.println("Freight Value is 2::" + freight);
                    if (freight.equalsIgnoreCase("")) {
                        freight = "0";
                    }
                    double total_inv = po_total_db + tcs_roundOff + Double.parseDouble(freight);
                    double total_inv_roundOff = Math.round(total_inv * 100.0) / 100.0;
                    String total_invoice = String.valueOf(total_inv_roundOff);
                    formObject.setNGValue("total_inv", total_invoice);//po_total+tcs+freight


                    formObject.RaiseEvent("WFSave");
                    objmatching.updateMatch("Total Amount");

                }
            }


            if (pEvent.getSource().getName().equalsIgnoreCase("freight_value")) {

                String freight = formObject.getNGValue("freight_value");
                String po_total = formObject.getNGValue("po_total");
                String tcs = formObject.getNGValue("tcs");
                if (freight.equalsIgnoreCase("")) {
                    freight = "0";
                }
                if (tcs.equalsIgnoreCase("")) {
                    tcs = "0";
                }
                double freight_db = Double.parseDouble(freight);
                double po_total_db = Double.parseDouble(po_total);
                double tcs_db = Double.parseDouble(tcs);

                double total_inv_db = freight_db + po_total_db + tcs_db;
                double total_inv_roundOff = Math.round(total_inv_db * 100.0) / 100.0;
                String total_inv = String.valueOf(total_inv_roundOff);
                formObject.setNGValue("total_inv", total_inv);

                objmatching.updateMatch("Total Amount");
                formObject.RaiseEvent("WFSave");
            }





            //****************RAhul 15/oct/2020 END******************//       

            if (pEvent.getSource().getName().equalsIgnoreCase("asn_number")) {

                formObject.clear("q_multiplepo");
                String asn_number_temp = formObject.getNGValue("asn_number");
                Query = "Select count(*) from ext_orientap where processid = '" + processInstanceId + "'"
                        + "and asn_number like '%" + asn_number_temp + "%'";
                System.out.println("Query : " + Query);
                result = formObject.getDataFromDataSource(Query);
                if (Integer.parseInt(result.get(0).get(0)) == 0) {
                    if (activityName.equalsIgnoreCase("Gate Entry")) {
                        System.out.println("Inside asn_number value changeR");
                        Set<String> asnPoNumber = new HashSet<>();
                        //Set<String> asnPoItem = new HashSet<>();

                        HashMap<String, String> map = new HashMap<String, String>();

                        String asn_number = "", Purchaseorder = "";
                        System.out.println("asn_number_temp.length() :" + asn_number_temp.length());
                        int difference = 10 - asn_number_temp.length();
                        System.out.println("difference :" + difference);
                        if (difference > 0) {
                            asn_number = String.format("%0" + (10 - asn_number_temp.length()) + "d%s", 0, asn_number_temp);
                            System.out.println("asn_number :" + asn_number);
                        } else {
                            asn_number = asn_number_temp;
                        }
                        //System.out.println("asn_number Value : " + asn_number);
                        String inputXml = objasn_detail.getASN_DETAIL(asn_number);
                        System.out.println("inputXml :" + inputXml);
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_ZBAPI_AP_AUTOMATION_ASN_DETAIL");
                        System.out.println("outputXml :" + outputXml);
                        xmlParser.setInputXML(outputXml);
                        System.out.println("OUTPUT IS ::-" + outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            String INV_NO = xmlParser.getValueOf("INV_NO");
                            System.out.println("INV_NO :" + INV_NO);
                            String DOC_DAT = xmlParser.getValueOf("DOC_DAT");
                            System.out.println("DOC_DAT :" + DOC_DAT);
                            String poexist = "";
                            if (!INV_NO.equalsIgnoreCase("")) {
                          //  if (INV_NO.equalsIgnoreCase("")) {
                                System.out.println("repeaterControl :" + repeaterControl);
                                repeaterControl.clear();
                                float invoice_total = 0.0f;
                                ////System.out.println("Values : " + INV_NO + " // " + DOC_DAT);
                                int countbefore = repeaterControl.getRepeaterRowCount();
                                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "ASN_TAB"); objList.hasMoreElements(); objList.skip()) {
                                    //  poexist = "N";
                                    Purchaseorder = objList.getVal("PURCH_ORD");
                                    String ITEM_NO = objList.getVal("ITEM_NO");
                                    String MATERIAL = objList.getVal("MATERIAL");
                                    String QUANT = objList.getVal("QUANT");
                                    String PLANT = objList.getVal("PLANT");
                                    String PROFIT_CTR = objList.getVal("PROFIT_CTR");
                                    String STORE_LOC = objList.getVal("STORE_LOC");
                                    String BATCH = objList.getVal("BATCH");
                                    String VAL_TYPE = objList.getVal("VAL_TYPE");
                                    String VGPOS = objList.getVal("VGPOS");
                                    System.out.println("BATCH = " + BATCH);
                                    System.out.println("VAL_TYPE =" + VAL_TYPE);
                                    System.out.println("VGPOS =" + VAL_TYPE);

                                    Query = "Select MATERIAL,SHORT_TEXT,NET_PRICE,UNIT,PO_ITEM,BASE_UNIT,BASE_UOM_ISO,TAX_CODE from complex_orient_po_item where PO_NUMBER = '" + Purchaseorder + "' and pinstanceid ='" + processInstanceId + "'";
                                    result = formObject.getDataFromDataSource(Query);
                                    System.out.println("Query is :::- " + Query);
                                    System.out.println("Result is :::- " + result);

                                    int size = result.size();
                                    System.out.println("size is :::- " + size);
                                    if (size > 0) {
                                        for (int i = 0; i < size; i++) {
                                            String MATERIAL_ASN = result.get(i).get(0);
                                            String SHORT_TEXT = result.get(i).get(1);
                                            String NET_PRICE = result.get(i).get(2);
                                            String UNIT = result.get(i).get(3);
                                            String PO_ITEM = result.get(i).get(4);
                                            String BASE_UNIT = result.get(i).get(5);
                                            String BASE_UOM_ISO = result.get(i).get(6);
                                            String TAX_CODE = result.get(i).get(7);

                                            String PO_ITEM_a = String.format("%06d", new Object[]{Long.valueOf(PO_ITEM)});

                                            if (VGPOS.equalsIgnoreCase(PO_ITEM_a) && MATERIAL.equalsIgnoreCase(MATERIAL_ASN)) {

                                                float total = Float.parseFloat(QUANT) * Float.parseFloat(NET_PRICE);
                                                String line_total = "" + total;
                                                String Tax_Value = objGeneral.calculateTax(line_total, TAX_CODE);
                                                float totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(Tax_Value);
                                                System.out.println("Total with tax" + totalwithtax);
                                                String Total_amount = "" + totalwithtax;
                                                repeaterControl.addRow();

                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_item", MATERIAL);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_item_desc", SHORT_TEXT);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_quantity", QUANT);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_price_per_unit", NET_PRICE);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_line_total_wtax", line_total);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_amount_wtax", Total_amount);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_plant", PLANT);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_weightunit", UNIT);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_po_item", PO_ITEM);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_base_uom", BASE_UNIT);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_base_uom_iso", BASE_UOM_ISO);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_tax_code", TAX_CODE);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_store_location", STORE_LOC);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_profit_centre", PROFIT_CTR);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_batch", BATCH);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_valuation", VAL_TYPE);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_taxvalue", Tax_Value);
                                                repeaterControl.setValue(countbefore, "q_Orient_invoice_item_qa_required", "");
                                                repeaterControl.setValue(countbefore, "q_orient_invoice_item_purchaseorder", Purchaseorder);
                                                repeaterControl.setValue(countbefore, "q_orient_invoice_item_asn_item", ITEM_NO);
                                                countbefore++;
                                            }


                                        }



                                    }

                                }



                                  formObject.setNGValue("InvoiceNo", INV_NO);
//                                System.out.println("Invocie Toatal : " + invoice_total);
//                                formObject.setNGValue("InvoiceAmount", invoice_total);
                                formObject.setNGValue("InvoiceDate", objGeneral.convertSapDateToNewgenDate(DOC_DAT));
                                formObject.RaiseEvent("WFSave");


                                //******************RAHUL---ADD Row in invocie REpeater*******************8//
                                int toalrow = repeaterControl.getRepeaterRowCount();

                                double po_total = 0.0;
                                System.out.println("Invocie Toatal **************: " + invoice_total);
                                for (int i = 0; i < toalrow; i++) {
                                    boolean rep_check = objGeneral.isRepRowDeleted("Frame3", i);
                                    if (!rep_check) {
                                    po_total = po_total + Double.parseDouble(repeaterControl.getValue(i, "q_Orient_invoice_item_amount_wtax"));
                                     }
                                }
                                double po_total_roundOff = Math.round(po_total * 100.0) / 100.0;
                                System.out.println("PO TOTAL :::" + po_total_roundOff + " " + toalrow);
                                formObject.setNGValue("po_total", String.valueOf(po_total_roundOff));
                                objmatching.updateMatch("Total Amount");
                                formObject.RaiseEvent("WFSave");
                                //******************RAHUL*******************8//

                            }
                        }
                    }
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("service_status")) {
                String status_val = formObject.getNGValue("service_status");

                if ("Approve".equalsIgnoreCase(status_val)) {
                    formObject.setVisible("route_to", false);
                } else {
                    formObject.setVisible("route_to", true);
                    formObject.clear("route_to");
                    if (activityName.equalsIgnoreCase("Level1")) {
                        formObject.addComboItem("route_to", "Previous Stage", "Previous Stage");
                    } else if (activityName.equalsIgnoreCase("Level2")) {
                        formObject.addComboItem("route_to", "Level1", "Level1");
                    } else if (activityName.equalsIgnoreCase("Level3")) {
                        formObject.addComboItem("route_to", "Level1", "Level1");
                        formObject.addComboItem("route_to", "Level2", "Level2");
                    } else if (activityName.equalsIgnoreCase("Level4")) {
                        formObject.addComboItem("route_to", "Level1", "Level1");
                        formObject.addComboItem("route_to", "Level2", "Level2");
                        formObject.addComboItem("route_to", "Level3", "Level3");
                    }

                }

            }

            if (pEvent.getSource().getName().equalsIgnoreCase("po_cat")) {
                String Val = formObject.getNGValue("po_cat");
                ////System.out.println("Inside PO Category");
                if (Val.equalsIgnoreCase("Service")) {
                    ////System.out.println("Visible");
                    formObject.setVisible("PurchaseOrderNo", true);
                    formObject.setVisible("Label3", true);
                } else {
                    ////System.out.println("INVisible");
                    formObject.setVisible("PurchaseOrderNo", false);
                    formObject.setVisible("Label3", false);
                }

            }

            /////////////////////// STORE DECISION ////////////////////////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("store_decision")) {

                if (activityName.equalsIgnoreCase("Store")) {
                    ////System.out.println("Inside Store Decision");
                    String store_dec = formObject.getNGValue("store_decision");
                    String mdoc_value = formObject.getNGValue("mdoc105");
                    String mdocrev_value = formObject.getNGValue("mdoc105_rev");

                    ////System.out.println(store_dec);
                    ////System.out.println(mdoc_value);
                }
            }

            /////////////////////////////////////////////////////////////////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("q_Orient_invoice_item_quantity")
                    || pEvent.getSource().getName().equalsIgnoreCase("q_orient_invoice_item_tax_code")) {
                ////System.out.println("Inside value change");
                if (!"ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                    float total, totalwithtax;
                    float priceperunit;
                    float quantity;
                    int rowcount = repeaterControl.getChangeIndex();
                    ////System.out.println("Count row : " + rowcount);
                    String Tax_Code = repeaterControl.getValue(rowcount, "q_Orient_invoice_item_tax_code");
                    ////System.out.println("Tax Code : " + Tax_Code);

                    if (repeaterControl.getValue(rowcount, "q_Orient_invoice_item_quantity").equalsIgnoreCase("")) {
                        throw new ValidatorException(new FacesMessage("Please enter value of quantity", ""));
                    } else {
                        quantity = Float.parseFloat(repeaterControl.getValue(rowcount, "q_Orient_invoice_item_quantity"));
                        ////System.out.println("Quantity : " + quantity);
                    }

                    if (repeaterControl.getValue(rowcount, "q_Orient_invoice_item_price_per_unit").equalsIgnoreCase("")) {
                        throw new ValidatorException(new FacesMessage("Please enter value of price per unit", ""));
                    } else {
                        priceperunit = Float.parseFloat(repeaterControl.getValue(rowcount, "q_Orient_invoice_item_price_per_unit"));
                        ////System.out.println("Priceperunit : " + priceperunit);
                    }

                    total = quantity * priceperunit;
                    String line_total = "" + total;
                    String Tax_Value = objGeneral.calculateTax(line_total, Tax_Code);
                    ////System.out.println("Line total" + line_total + "Tax Value" + Tax_Value);
                    totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(Tax_Value);
                    ////System.out.println("Total with tax" + totalwithtax);
                    String Total_amount = "" + totalwithtax;
                    //   String priceperunit = repeaterControl.getValue(rowcount, "");
                    repeaterControl.setValue(rowcount, "q_Orient_invoice_item_line_total_wtax", line_total);
                    repeaterControl.setValue(rowcount, "q_Orient_invoice_item_amount_wtax", Total_amount);
                    repeaterControl.setValue(rowcount, "q_Orient_invoice_item_taxvalue", Tax_Value);
                    objmatching.updateMatch("Total Amount");
                }



            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Text40")) {

                float quantity_invoice = Float.parseFloat(formObject.getNGValue("Text40"));
                //System.out.println("inv q" + quantity_invoice);
                String PO_grid = "q_orient_po_item";
                ListView lv0 = (ListView) formObject.getComponent(PO_grid);
                int selectrow = lv0.getSelectedRowIndex();

                String refreshflag = formObject.getNGValue("Text137");
                if ("Y".equalsIgnoreCase(refreshflag)) {
                    selectrow = selectrow - 1;
                }
                String poq = formObject.getNGValue(PO_grid, selectrow, 2);
                //System.out.println("POQ" + poq);
                float quantity_po = Float.parseFloat(poq);
                //System.out.println("po q: " + quantity_po);
                if (quantity_invoice > quantity_po) {
                    //System.out.println("inside first exception");
                    throw new ValidatorException(new FacesMessage("Quantity can't be higher than the quantity of selected PO", ""));
                }


                float total, totalwithtax;
                total = Float.parseFloat(formObject.getNGValue("Text40")) * Float.parseFloat(formObject.getNGValue("Text43"));
                // String line_total = "" + total;
//****Rahul 26/oct/2020*****//
                double roundOff = Math.round(total * 100.0) / 100.0;
                String line_total = String.valueOf(roundOff);
//****Rahul 26/oct/2020*****//
                String tax_code = formObject.getNGValue("Text55");
                String tax_value = objGeneral.calculateTax(line_total, tax_code);

                totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(tax_value);

                formObject.setNGValue("Text41", line_total);
                formObject.setNGValue("Text42", totalwithtax);
                formObject.setNGValue("Text62", tax_value);

            }
            if (pEvent.getSource().getName().equalsIgnoreCase("duplicateCheck")) {
                String dup_value = formObject.getNGValue("duplicateCheck");
                if ("N".equalsIgnoreCase(dup_value)) {
                    formObject.setVisible("Label24", false);
                } else {
                    formObject.setVisible("Label24", true);
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("entrysheet_no")) {
                if ("".equalsIgnoreCase("entrysheet_no")) {
                    formObject.setVisible("entrysheet_no", false);
                    formObject.setEnabled("entrysheet_no", false);
                    formObject.setVisible("Label69", false);
                } else {
                    formObject.setVisible("entrysheet_no", true);
                    formObject.setEnabled("entrysheet_no", false);
                    formObject.setVisible("Label69", true);
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("storehold_decsion")) {
                String storedes = "";
                storedes = formObject.getNGValue("storehold_decsion");
                ////System.out.println("Store hold decision value : " + storedes);
                if (activityName.equalsIgnoreCase("Store Hold")) {
                    if (storedes.equalsIgnoreCase("")) {
                        formObject.setVisible("Label78", false);
                        formObject.setVisible("remarks_storehold", false);
                        formObject.setVisible("Text108", false);
                        formObject.setVisible("Label77", false);
                        formObject.setVisible("Button20", false);
                        formObject.setVisible("Button19", false);
                        formObject.setVisible("Text9", false);
                        formObject.setVisible("Label7", false);
                        formObject.setEnabled("claiminsurance", false);
                        formObject.setEnabled("debit_to_transport", false);
                        formObject.setEnabled("replacement", false);
                        formObject.setBackcolor("claiminsurance", disablefield);
                        formObject.setBackcolor("debit_to_transport", disablefield);
                        formObject.setBackcolor("replacement", disablefield);
                        formObject.setNGValue("claiminsurance", "");
                        formObject.setNGValue("debit_to_transport", "");
                        formObject.setNGValue("replacement", "");

                    }

                    if (storedes.equalsIgnoreCase("Purchase Return Required")) {
                        formObject.setVisible("Text108", true);
                        formObject.setVisible("Label77", true);
                        formObject.setVisible("Button20", true);
                        formObject.setVisible("Button19", true);
                        formObject.setEnabled("Text108", true);

                        formObject.setVisible("Text9", true);
                        formObject.setEnabled("Text9", true);
                        formObject.setVisible("Label7", true);

                        formObject.setEnabled("claiminsurance", false);
                        formObject.setEnabled("debit_to_transport", false);
                        formObject.setEnabled("replacement", false);
                        formObject.setBackcolor("claiminsurance", disablefield);
                        formObject.setBackcolor("debit_to_transport", disablefield);
                        formObject.setBackcolor("replacement", disablefield);
                        formObject.setNGValue("claiminsurance", "");
                        formObject.setNGValue("debit_to_transport", "");
                        formObject.setNGValue("replacement", "");

                        String fullname = formObject.getNGValue("po_createdby");
                        ////System.out.println("Full name : " + fullname);
                        Query = "select MailId from pdbuser where concat(personalname,familyname) like '%" + fullname + "%'";
                        ////System.out.println("Query : " + Query);
                        resultarray = formObject.getDataFromDataSource(Query);
                        String email = resultarray.get(0).get(0);
                        ////System.out.println("Email ID : " + email);
                        formObject.setNGValue("Text9", email);

                    }
                    if (storedes.equalsIgnoreCase("Debit To Transporter")) {
                        formObject.setBackcolor("debit_to_transport", Color.white);
                        formObject.setEnabled("debit_to_transport", true);
                        formObject.setEnabled("claiminsurance", false);
                        formObject.setBackcolor("claiminsurance", disablefield);
                        formObject.setNGValue("claiminsurance", "");
                        formObject.setEnabled("replacement", false);
                        formObject.setVisible("Text108", false);
                        formObject.setVisible("Label77", false);
                        formObject.setVisible("Button20", false);
                        formObject.setVisible("Button19", false);
                        formObject.setVisible("Text9", false);
                        formObject.setVisible("Label7", false);
                        formObject.setEnabled("remarks_storehold", true);

                    } else if (storedes.equalsIgnoreCase("Claim Insurance")) {
                        formObject.setBackcolor("claiminsurance", Color.white);
                        formObject.setEnabled("claiminsurance", true);
                        formObject.setEnabled("debit_to_transport", false);
                        formObject.setEnabled("replacement", false);

                        formObject.setBackcolor("replacement", disablefield);
                        formObject.setBackcolor("debit_to_transport", disablefield);
                        formObject.setVisible("Text108", false);
                        formObject.setVisible("Label77", false);
                        formObject.setVisible("Button20", false);
                        formObject.setVisible("Button19", false);
                        formObject.setVisible("Text9", false);
                        formObject.setVisible("Label7", false);
                        formObject.setEnabled("remarks_storehold", true);

                    } else if (storedes.equalsIgnoreCase("Replacement")) {
                        formObject.setEnabled("replacement", true);
                        formObject.setBackcolor("replacement", Color.white);
                        formObject.setBackcolor("claiminsurance", disablefield);
                        formObject.setBackcolor("debit_to_transport", disablefield);
                        formObject.setNGValue("claiminsurance", "");
                        formObject.setNGValue("debit_to_transport", "");
                        formObject.setEnabled("claiminsurance", false);
                        formObject.setEnabled("debit_to_transport", false);
                        formObject.setVisible("Text108", false);
                        formObject.setVisible("Label77", false);
                        formObject.setVisible("Button20", false);
                        formObject.setVisible("Button19", false);
                        formObject.setVisible("Text9", false);
                        formObject.setVisible("Label7", false);
                        formObject.setEnabled("remarks_storehold", true);
                    }
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Text108")) {
                tempdata1 = formObject.getNGValue("Text108");
                formObject.setNGValue("pr_number", tempdata1);
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("qualityhold_decision")) {
                if (activityName.equalsIgnoreCase("Quality_Hold")) {
                    if (formObject.getNGValue("qualityhold_decision").equalsIgnoreCase("")) {
                        formObject.setVisible("Label79", true);
                        formObject.setVisible("text116", false);
                        formObject.setVisible("Button21", false);
                        formObject.setVisible("Text110", false);
                        formObject.setVisible("Button13", false);
                        formObject.setVisible("Label82", false);
                        formObject.setVisible("Label80", false);
                        formObject.setVisible("Text116", false);
                        formObject.setVisible("Label81", false);
                        formObject.setVisible("remarks_qualityhold", false);
                    } else if (formObject.getNGValue("qualityhold_decision").equalsIgnoreCase("Purchase Return Required")) {
                        formObject.setVisible("text116", false);
                        formObject.setVisible("Button21", false);
                        formObject.setVisible("Text110", true);
                        formObject.setVisible("Button13", false);
                        formObject.setVisible("Label82", true);
                        formObject.setVisible("Label80", true);
                        formObject.setVisible("Text116", true);
                        formObject.setVisible("Label82", true);
                        formObject.setVisible("Button21", true);
                        formObject.setVisible("Label81", true);
                        formObject.setVisible("remarks_qualityhold", true);
                        formObject.setVisible("Button13", true);
                    } else if (formObject.getNGValue("qualityhold_decision").equalsIgnoreCase("No Purchase Return Required")) {
                        formObject.setVisible("text116", false);
                        formObject.setVisible("Button21", false);
                        formObject.setVisible("Text110", false);
                        formObject.setVisible("Button13", false);
                        formObject.setVisible("Label82", false);
                        formObject.setVisible("Label80", false);
                        formObject.setVisible("Text116", false);
                        formObject.setVisible("Label81", true);
                        formObject.setVisible("remarks_qualityhold", true);
                    } else {
                        formObject.setVisible("text116", true);
                        formObject.setVisible("Button21", true);
                        formObject.setVisible("Text110", true);
                        formObject.setVisible("Button13", true);
                        formObject.setVisible("Label82", true);
                        formObject.setVisible("Label80", true);
                        formObject.setVisible("Text116", true);
                    }
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Text35")) {
                tempdata1 = formObject.getNGValue("Text35");

                formObject.setNGValue("pr_number", tempdata1);
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("InvoiceNo")) {
                ////System.out.println("Inside Invoice No value change");
                formObject.setNGValue("InvoiceNumber", formObject.getNGValue("InvoiceNo"));

            }
            if (pEvent.getSource().getName().equalsIgnoreCase("InvoiceDate")) {
                ////System.out.println("Inside Invoice No value change");
                formObject.setNGValue("InvoiceDt", formObject.getNGValue("InvoiceDate"));
                formObject.setNGValue("invdate_sap", formObject.getNGValue("InvoiceDate"));

            }

            if (pEvent.getSource().getName().equalsIgnoreCase("GSTN")) {
                ////System.out.println("Inside GSTN value change");
                objmatching.updateMatch("GSTN");
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("InvoiceAmount")) {
                ////System.out.println("Inside InvoiceAmount value change");
                if ("ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                    if (activityName.equalsIgnoreCase("Service Entry")) {
                        objmatching.updateMatch("Service Total Amount");
                    }
                } else {
                    objmatching.updateMatch("Total Amount");
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("gstn_inv")) {
                ////System.out.println("Inside ORIENT GSTN value change");
                objmatching.updateMatch("ORIENT GSTN");
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("pan")) {
                ////System.out.println("Inside PAN value change");
                objmatching.updateMatch("PAN");
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("pan_inv")) {
                ////System.out.println("Inside Vendor PAN value change");
                objmatching.updateMatch("PAN_INV");
            }

            /////////// ACCOUNTS DROPDOWN MENU POPULATE ////////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("Combo7")) {
                ////System.out.println("Inside value change of DropDown");
            }

            ///////////////////////////////////////////////////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("service_itemselect")) {
                ////System.out.println("Inside select sevice item change");
                String PCKG_NO = "", hsn_sac = "";
                String service_itemselect = formObject.getNGValue("service_itemselect");
                service_itemselect = service_itemselect.replaceAll("'", "%");
                ////System.out.println("Value : " + service_itemselect);
                repeaterControlF5.clear();
                objhsn_sac = new hsn_sac();
                Query = "select PCKG_NO from complex_orient_po_item where pinstanceid = '" + processInstanceId + "' and "
                        + "SHORT_TEXT like '" + service_itemselect + "'";
                ////System.out.println("Query 1 : " + Query);
                result = formObject.getDataFromDataSource(Query);
                PCKG_NO = result.get(0).get(0);

                String Query2 = "select SUBPCKG_NO from complex_orient_entry_service where "
                        + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + PCKG_NO + "'";
                ////System.out.println("Query 2 : " + Query2);
                result = formObject.getDataFromDataSource(Query2);
                PCKG_NO = result.get(0).get(0);

                String Query3 = "select EXT_LINE, SHORT_TEXT, QUANTITY, GR_PRICE ,NET_VALUE  from complex_orient_entry_service "
                        + "where pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + PCKG_NO + "'";
                ////System.out.println("Query 3 : " + Query3);
                result = formObject.getDataFromDataSource(Query3);
                ////System.out.println("result size : " + result.size());

                String inputXml = objhsn_sac.getPOSERV_HSN(PCKG_NO);
                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_ZBAPI_AP_AUTOMATION_POSERV_HSN");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                ////System.out.println("MainCode Value before loop: " + xmlParser.getValueOf("MainCode"));
                for (int i = 0; i < result.size(); i++) {
                    ////System.out.println("MainCode Value after loop: " + xmlParser.getValueOf("MainCode"));
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        ////System.out.println("Inside Main Code zero");
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "HSN_TAB"); objList.hasMoreElements(); objList.skip()) {

                            int difference = 0;
                            String ext_line_temp = result.get(i).get(0), ext_line = "";
                            difference = 18 - ext_line_temp.length();
                            if (difference > 0) {
                                ext_line = String.format("%0" + (10 - ext_line_temp.length()) + "d%s", 0, ext_line_temp);
                            } else {
                                ext_line = ext_line_temp;
                            }
                            if (objList.getVal("PO_LINE").equalsIgnoreCase(ext_line)) {
                                ////System.out.println("PO_Line : " + objList.getVal("PO_LINE") + "/ Ext_Line : " + ext_line);
                                hsn_sac = objList.getVal("TAX_TARRIF");
                                ////System.out.println("HSN SAC : " + hsn_sac);
                                break;
                            }
                        }
                    }
                    repeaterControlF5.addRow();
                    repeaterControlF5.setValue(i, "Text125", result.get(i).get(0));
                    repeaterControlF5.setValue(i, "Text126", result.get(i).get(1));
                    repeaterControlF5.setValue(i, "Text127", result.get(i).get(2));
                    repeaterControlF5.setValue(i, "Text128", result.get(i).get(3));
                    repeaterControlF5.setValue(i, "Text129", result.get(i).get(4));
                    repeaterControlF5.setValue(i, "Text130", hsn_sac);
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Text127")) {
                int rowcount = repeaterControlF5.getChangeIndex();
                float gross_price, quantity = 0f;
                String PCKG_NO, NET_VALUE;
                String service_itemselect = formObject.getNGValue("service_itemselect");
                service_itemselect = service_itemselect.replaceAll("'", "%");
                ////System.out.println("Count row : " + rowcount);

                if (repeaterControlF5.getValue(rowcount, "Text127").equalsIgnoreCase("")) {
                    throw new ValidatorException(new FacesMessage("Please enter value of quantity", ""));
                } else {
                    quantity = Float.parseFloat(repeaterControlF5.getValue(rowcount, "Text127"));
                    ////System.out.println("Quantity : " + quantity);
                }

                Query = "select PCKG_NO from complex_orient_po_item where pinstanceid = '" + processInstanceId + "' and "
                        + "SHORT_TEXT like '" + service_itemselect + "'";
                ////System.out.println("Query 1 : " + Query);
                result = formObject.getDataFromDataSource(Query);
                PCKG_NO = result.get(0).get(0);

                String Query2 = "select SUBPCKG_NO from complex_orient_entry_service where "
                        + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + PCKG_NO + "'";
                ////System.out.println("Query 2 : " + Query2);
                result = formObject.getDataFromDataSource(Query2);
                PCKG_NO = result.get(0).get(0);

                String Query3 = "select GR_PRICE from complex_orient_entry_service "
                        + "where pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + PCKG_NO + "' "
                        + "and EXT_LINE = '" + repeaterControlF5.getValue(rowcount, "Text125") + "'";
                ////System.out.println("Query 3 : " + Query3);
                result = formObject.getDataFromDataSource(Query3);
                NET_VALUE = result.get(0).get(0);
                gross_price = quantity * Float.parseFloat(NET_VALUE);
                ////System.out.println("Gross value : " + gross_price);
                repeaterControlF5.setValue(rowcount, "Text129", gross_price + "");
                objmatching.updateMatch("Service Total Amount");
            }
        }

        if (pEvent.getType().name().equalsIgnoreCase("TAB_CLICKED")) {
            ////System.out.println("------------Inside Tab------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Tab2")) {
                ////System.out.println("inside tab2");
                if (formObject.getSelectedSheet("Tab2") == 0) {
                    formObject.RaiseEvent("WFSave");
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Tab1")) {
                ////System.out.println("inside tab1");
                if (formObject.getSelectedSheet("Tab1") == 8) {
                    repeaterControlF4.clear();
                    String matdoc_form = "";
                    String movtype_form = "";
                    if (activityName.equalsIgnoreCase("Gate Entry")) {
                        matdoc_form = formObject.getNGValue("mdoc103");
                        movtype_form = "103";
                    }

                    if (activityName.equalsIgnoreCase("Store")) {
                        matdoc_form = formObject.getNGValue("mdoc105");
                        movtype_form = "105";
                    }
                    ////System.out.println("Material No: " + matdoc_form);
                    if (!matdoc_form.equalsIgnoreCase("")) {
                        List<String> HeaderNames = new ArrayList<String>();
                        // HeaderNames.add("");

                        HeaderNames.add("Material Document");
                        HeaderNames.add("Item");
                        HeaderNames.add("Material");
                        HeaderNames.add("Quantity");
                        HeaderNames.add("MatDoc Item");

                        repeaterControlF4.setRepeaterHeaders(HeaderNames);
                        String inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL");
                        inputXml = inputXml + "<Parameters>"
                                + "<ImportParameters>"
                                + "<PURCHASEORDER>" + formObject.getNGValue("PurchaseOrderNo") + "</PURCHASEORDER>"
                                + "<HISTORY>X</HISTORY>"
                                + "</ImportParameters>"
                                + "</Parameters>"
                                + "</WFSAPInvokeFunction_Input>";
                        ////System.out.println("Input xml : " + inputXml);
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_PO_GETDETAIL");
                        xmlParser.setInputXML(outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        // //System.out.println("After xml response outputxml : " + outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEM_HISTORY"); objList.hasMoreElements(); objList.skip()) {

                                String MOVE_TYPE = objList.getVal("MOVE_TYPE");
                                String MAT_DOC = objList.getVal("MAT_DOC");
                                if (MOVE_TYPE.equalsIgnoreCase(movtype_form)
                                        && MAT_DOC.equalsIgnoreCase(matdoc_form)) {
                                    String PO_ITEM = objList.getVal("PO_ITEM");
                                    String MATERIAL = objList.getVal("MATERIAL");
                                    String QUANTITY = objList.getVal("QUANTITY");
                                    String MATDOC_ITM = objList.getVal("MATDOC_ITM");

                                    ////System.out.println(PO_ITEM + "-" + QUANTITY + "-" + MATDOC_ITM);
                                    ////System.out.println("Row Count" + repeaterControlF4.getRepeaterRowCount());
                                    repeaterControlF4.addRow();
                                    ////System.out.println("Row Count" + repeaterControlF4.getRepeaterRowCount());
                                    int rowCount = repeaterControlF4.getRepeaterRowCount();
                                    repeaterControlF4.setValue(rowCount - 1, "Text24", MAT_DOC);
                                    repeaterControlF4.setValue(rowCount - 1, "Text38", PO_ITEM);
                                    repeaterControlF4.setValue(rowCount - 1, "Text97", MATERIAL);
                                    repeaterControlF4.setValue(rowCount - 1, "Text98", QUANTITY);
                                    repeaterControlF4.setValue(rowCount - 1, "Text99", MATDOC_ITM);

                                }
                            }
                        }
                    }
                }
            }
        }

        if (pEvent.getType().name().equalsIgnoreCase("MOUSE_CLICKED")) {
            ////System.out.print("------------Inside Mouse Click------------------");

            //***********************RAHUL 17-09-2020 START*******************************************//


            if (pEvent.getSource().getName().equalsIgnoreCase("Button24")) {
                String resolutionRemarks = formObject.getNGValue("Text138");
                if (resolutionRemarks.equalsIgnoreCase("") || resolutionRemarks.equalsIgnoreCase(null)) {
                    throw new ValidatorException(new FacesMessage("Please enter resoution remarks", ""));
                }
                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                String currentDate = date.toString();
                formObject.setNGValue("Text139", "True");
                formObject.setNGValue("Text140", currentDate);
                formObject.setNGValue("Text141", userName);
                formObject.ExecuteExternalCommand("NGModifyRow", "q1_exception_details");
            }

            //***********************RAHUL 17-09-2020 END*******************************************//
            if (pEvent.getSource().getName().equalsIgnoreCase("Button27")) {
                ////System.out.println("Button click 27");
                String PO = formObject.getNGValue("PurchaseOrderNo");
                String multiPO = formObject.getNGValue("Text132");
                Query = "Select count(*) from complex_orient_multiplepo where pinstanceid = '" + processInstanceId + "' "
                        + "and ponumber = '" + multiPO + "'";
                ////System.out.println("Query : " + Query);
                result = formObject.getDataFromDataSource(Query);
                ////System.out.println("Result value : " + result.get(0).get(0));
                if (PO.equalsIgnoreCase(multiPO)
                        || Integer.parseInt(result.get(0).get(0)) > 0) {
                    throw new ValidatorException(new FacesMessage("The details of PO number : " + multiPO + " already added", ""));
                } else {
                    String xmlnew = "";
                    String outputXml = objpo_getdetail.getPO_GETDETAIL(multiPO);
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    ////System.out.println("After xml response");
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        String vendor_code1 = formObject.getNGValue("vendor_code");
                        String vendor_code2 = xmlParser.getValueOf("VENDOR");
                        if (xmlParser.getValueOf("PO_NUMBER").equalsIgnoreCase(multiPO)) {
                            if (vendor_code1.equalsIgnoreCase(vendor_code2)) {
                                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {
                                    xmlnew = xmlnew
                                            + "<ListItem><SubItem>" + objList.getVal("MATERIAL")
                                            + "</SubItem><SubItem>" + objList.getVal("SHORT_TEXT")
                                            + "</SubItem><SubItem>" + objList.getVal("QUANTITY")
                                            + "</SubItem><SubItem>" + objList.getVal("NET_PRICE")
                                            + "</SubItem><SubItem>" + objList.getVal("NET_VALUE")
                                            + "</SubItem><SubItem>" + objList.getVal("ACCTASSCAT")
                                            + "</SubItem><SubItem>" + objList.getVal("PLANT")
                                            + "</SubItem><SubItem>" + objList.getVal("UNIT")
                                            + "</SubItem><SubItem>" + objList.getVal("PO_ITEM")
                                            + "</SubItem><SubItem>" + objList.getVal("BASE_UNIT")
                                            + "</SubItem><SubItem>" + objList.getVal("BASE_UOM_ISO")
                                            + "</SubItem><SubItem>" + objList.getVal("TAX_CODE")
                                            + "</SubItem><SubItem>" + objList.getVal("STORE_LOC")
                                            + "</SubItem><SubItem>" + objList.getVal("PROFIT_CTR")
                                            + "</SubItem><SubItem>" + ""
                                            + "</SubItem><SubItem>" + objList.getVal("VAL_TYPE")
                                            + "</SubItem><SubItem>" + ""
                                            + "</SubItem><SubItem>" + multiPO
                                            + "</SubItem><SubItem>" + objList.getVal("PRICE_UNIT")
                                            + "</SubItem><SubItem>" + objList.getVal("ORDERPR_UN")
                                            + "</SubItem><SubItem>" + objList.getVal("ORDERPR_UN_ISO")
                                            + "</SubItem></ListItem>";

                                }
                                ////System.out.println("XML : " + xmlnew);
                                formObject.NGAddListItem("q_orient_po_item", xmlnew);
                                formObject.ExecuteExternalCommand("NGAddRow", "q_multiplepo");
                                objgateentry.setGEFormValidation();
                            } else {
                                throw new ValidatorException(new FacesMessage("Enter PO of same vendor only", ""));
                            }
                        } else {
                            throw new ValidatorException(new FacesMessage(xmlParser.getValueOf("MESSAGE"), ""));
                        }
                    }
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button28")) {
                ////System.out.println("Button click 27");
                String MultiPO_grid = "q_multiplepo", PO_grid = "q_orient_po_item";
                ListView lv_pogrid = (ListView) formObject.getComponent(PO_grid);
                int rowcount = lv_pogrid.getRowCount();

                ListView lv0 = (ListView) formObject.getComponent(MultiPO_grid);
                int selectrow = lv0.getSelectedRowIndex();
                String multiPO_val = formObject.getNGValue(MultiPO_grid, selectrow, 0);
                ////System.out.println("Row count : " + rowcount);
                int j = 0;
                for (int i = 0; i < rowcount; i++) {
                    String ponumber_pogrid = formObject.getNGValue(PO_grid, i, 17);
                    ////System.out.println("Selected PO : " + multiPO_val + " PoGrid Po Number : " + ponumber_pogrid);
                    if (ponumber_pogrid.equalsIgnoreCase(multiPO_val)) {
                        j++;
                        ////System.out.println("Inside material value match");
                        formObject.setSelectedIndex(PO_grid, i);
                        formObject.ExecuteExternalCommand("NGDeleteRow", PO_grid);
                        ////System.out.println("RowCount : " + rowcount);
                        rowcount = lv_pogrid.getRowCount() - j;
                        ////System.out.println("RowCount-1 : " + rowcount);
                        i = 0;
                    }
                }
                formObject.ExecuteExternalCommand("NGDeleteRow", MultiPO_grid);
                formObject.RaiseEvent("WFSave");
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button23")) {
                ////System.out.println("Button 23 click");
                String xmlnew = "";
                String outputXml = objpo_getdetail.getPO_GETDETAIL(formObject.getNGValue("PurchaseOrderNo"));
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                ////System.out.println("After xml response");
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    if (xmlParser.getValueOf("PO_NUMBER").equalsIgnoreCase(formObject.getNGValue("PurchaseOrderNo"))) {
                        formObject.setNGValue("comp_code", xmlParser.getValueOf("CO_CODE"));
                        formObject.setNGValue("po_type", xmlParser.getValueOf("DOC_TYPE"));
                        formObject.setNGValue("po_createdby", xmlParser.getValueOf("CREATED_BY"));
                        formObject.setNGValue("vendor_code", xmlParser.getValueOf("VENDOR"));
                        formObject.setNGValue("pur_org", xmlParser.getValueOf("PURCH_ORG"));
                        formObject.setNGValue("pur_group", xmlParser.getValueOf("PUR_GROUP"));
                        formObject.setNGValue("po_date", xmlParser.getValueOf("DOC_DATE"));
                        formObject.setNGValue("currency", xmlParser.getValueOf("CURRENCY"));
                        formObject.setNGValue("vendor_name", xmlParser.getValueOf("VEND_NAME"));
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {
                            xmlnew = xmlnew
                                    + "<ListItem><SubItem>" + objList.getVal("MATERIAL")
                                    + "</SubItem><SubItem>" + objList.getVal("SHORT_TEXT")
                                    + "</SubItem><SubItem>" + objList.getVal("QUANTITY")
                                    + "</SubItem><SubItem>" + objList.getVal("NET_PRICE")
                                    + "</SubItem><SubItem>" + objList.getVal("NET_VALUE")
                                    + "</SubItem><SubItem>" + objList.getVal("ACCTASSCAT")
                                    + "</SubItem><SubItem>" + objList.getVal("PLANT")
                                    + "</SubItem><SubItem>" + objList.getVal("UNIT")
                                    + "</SubItem><SubItem>" + objList.getVal("PO_ITEM")
                                    + "</SubItem><SubItem>" + objList.getVal("BASE_UNIT")
                                    + "</SubItem><SubItem>" + objList.getVal("BASE_UOM_ISO")
                                    + "</SubItem><SubItem>" + objList.getVal("TAX_CODE")
                                    + "</SubItem><SubItem>" + objList.getVal("STORE_LOC")
                                    + "</SubItem><SubItem>" + objList.getVal("PROFIT_CTR")
                                    + "</SubItem><SubItem>" + ""
                                    + "</SubItem><SubItem>" + objList.getVal("VAL_TYPE")
                                    + "</SubItem><SubItem>" + ""
                                    + "</SubItem><SubItem>" + formObject.getNGValue("PurchaseOrderNo")
                                    + "</SubItem><SubItem>" + objList.getVal("PRICE_UNIT")
                                    + "</SubItem></ListItem>";

                        }
                        ////System.out.println("XML : " + xmlnew);
                        formObject.NGAddListItem("q_orient_po_item", xmlnew);

                        objgateentry.setGEFormValidation();

                    } else {
                        formObject.setNGValue("comp_code", "");
                        formObject.setNGValue("po_type", "");
                        formObject.setNGValue("po_createdby", "");
                        formObject.setNGValue("vendor_code", "");
                        formObject.setNGValue("pur_org", "");
                        formObject.setNGValue("pur_group", "");
                        formObject.setNGValue("po_date", "");
                        formObject.setNGValue("currency", "");
                        formObject.setNGValue("vendor_name", "");
                        formObject.setNGValue("gstn_sap", "");
                        formObject.setNGValue("orient_gstn", "");
                        formObject.setNGValue("pan_sap", "");
                        formObject.setNGValue("orient_PAN", "");
                        ListView lv0 = (ListView) formObject.getComponent("q_orient_po_item");
                        int rowcount = lv0.getRowCount();
                        ////System.out.println("Row count : " + rowcount);
//                        int ar[] = formObject.getSelectedIndices("q_orient_po_item");
//                        for(int ){}

                        for (int i = 0; i < rowcount; i++) {
                            ////System.out.println("I : " + i);
                            formObject.setSelectedIndex("q_orient_po_item", 0);
                            formObject.ExecuteExternalCommand("NGDeleteRow", "q_orient_po_item");
                        }
                        formObject.ExecuteExternalCommand("NGClear", "q_orient_po_item");
                        formObject.clear("q_orient_po_item");
                        // formObject.RaiseEvent("WFSave");

                        throw new ValidatorException(new FacesMessage(xmlParser.getValueOf("MESSAGE"), ""));
                    }
                }

            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button22")
                    || pEvent.getSource().getName().equalsIgnoreCase("Button25")) {
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
                    ////System.out.println("After xml response");
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TAX_TAB"); objList.hasMoreElements(); objList.skip()) {
                            ////System.out.println("Inside TAX_CODE loop");
                            String TAX_CODE = objList.getVal("TAX_CODE");
                            ////System.out.println("TAX CODE : " + TAX_CODE);
                            String DESC = objList.getVal("DESC");
                            ////System.out.println("DESC : " + DESC);
                            taxList.add(Arrays.asList(TAX_CODE, DESC));
                        }
                    }
                    objPicklist.populateData(taxList);
                    objPicklist.setVisible(true);
                    ////System.out.println("###Populate data");
                } catch (Exception e) {
                    ////System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("gateexception_flag")) {
                ////System.out.println("Inside Exception flag");
                String gateexception_flag = formObject.getNGValue("gateexception_flag");
                ////System.out.println("Value : " + gateexception_flag);
                if (gateexception_flag.equalsIgnoreCase("True")) {
                    formObject.setNGValue("gate_decision", "Exception");
                } else {
                    formObject.setNGValue("gate_decision", "Pending");
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("q_orient_service_itemsel_select_flag")) {
                ////System.out.println("Inside click Flag select val");
                int rowcount = repeaterControlF6.getChangeIndex();
                String selectFlag_val = repeaterControlF6.getValue(rowcount, "q_orient_service_itemsel_select_flag");
                ////System.out.println("Select val " + selectFlag_val);
                if (selectFlag_val.equalsIgnoreCase("true")) {
                    ////System.out.println("Flag True");
                    objmatching.updateMatch("Service Total Amount");
                } else {
                    ////System.out.println("Flag False");
                    objmatching.updateMatch("Service Total Amount");
                }

            }

            if (pEvent.getSource().getName().equalsIgnoreCase("q_orient_po_item")) {
                int selectRowIndex = formObject.getSelectedIndex("q_orient_po_item");
                //Change for price unit > 1
                String unitprice = formObject.getNGValue("q_orient_po_item", selectRowIndex, 3);
                String perpcs = formObject.getNGValue("q_orient_po_item", selectRowIndex, 18);
                float perpcsprice = Float.parseFloat(unitprice) / Float.parseFloat(perpcs);
                formObject.setNGValue("Text43", perpcsprice);
                formObject.setNGValue("Text135", "1");
                String material = formObject.getNGValue("q_orient_po_item", selectRowIndex, 0);
                String poitem = formObject.getNGValue("q_orient_po_item", selectRowIndex, 8);
                String poitem1;
                difference = 5 - poitem.length();
                if (difference > 0) {
                    poitem1 = String.format("%0" + (5 - poitem.length()) + "d%s", 0, poitem);

                } else {
                    poitem1 = poitem;
                }

                String inputXml = objGeneral.sapInvokeXML("BAPI_BATCH_CREATE");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<MATERIAL>" + material + "</MATERIAL>"
                        + "</ImportParameters>"
                        + "</Parameters>"
                        + "</WFSAPInvokeFunction_Input>";
                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_BATCH_CREATE");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    String batch_val = xmlParser.getValueOf("BATCH");
                    if ("".equalsIgnoreCase(batch_val)) {
                        formObject.setEnabled("Text65", false);
                        formObject.setNGValue("Text64", "N");
                    } else {
                        formObject.setNGValue("Text65", batch_val);
                        formObject.setEnabled("Text65", true);
                        formObject.setNGValue("Text64", "Y");
                    }
                } else {
                }
                float totalwithtax;
                String line_total = formObject.getNGValue("q_orient_po_item", selectRowIndex, 4);
                String tax_code = formObject.getNGValue("q_orient_po_item", selectRowIndex, 11);
                String tax_value = objGeneral.calculateTax(line_total, tax_code);
                formObject.setNGValue("Text62", tax_value);
                if (tax_value.equalsIgnoreCase("")) {
                    totalwithtax = Float.parseFloat(line_total);
                } else {
                    totalwithtax = Float.parseFloat(line_total) + Float.parseFloat(tax_value);
                }
                formObject.setNGValue("Text42", totalwithtax);
                inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL1");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PURCHASEORDER>"
                        + formObject.getNGValue("PurchaseOrderNo")
                        + "</PURCHASEORDER>"
                        + "</ImportParameters>"
                        + "</Parameters></WFSAPInvokeFunction_Input>";

                outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_PO_GETDETAIL1");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXml);
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    for (WFXmlList objList = objXmlResponse1.createList("TableParameters", "POITEM"); objList.hasMoreElements(); objList.skip()) {
                        String po_item = objList.getVal("PO_ITEM");
                        String hsn_sac = objList.getVal("BRAS_NBM");
                        if (po_item.equalsIgnoreCase(poitem1)) {
                            formObject.setNGValue("Text121", hsn_sac);
                            break;
                        }
                    }
                }

                //Valuation Type CR change :
                String valType = formObject.getNGValue("q_orient_po_item", selectRowIndex, 15);
                System.out.println("Val Type :" + valType);
                if ("".equalsIgnoreCase(valType)
                        || null == valType) {
                    System.out.println("Inside Val type blank");
                    String inputXmlValarea = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_VAL_AREA");
                    inputXmlValarea = inputXmlValarea + "<Parameters>"
                            + "<ImportParameters>"
                            + "<MATERIAL>" + formObject.getNGValue("Text23") + "</MATERIAL>"
                            + "<PLANT>" + formObject.getNGValue("Text44") + "</PLANT>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    System.out.println("Input XML : " + inputXmlValarea);
                    String outputXmlvalarea = objGeneral.executeWithCallBroker(inputXmlValarea, processInstanceId + "_ZBAPI_AP_AUTOMATION_VAL_AREA");
                    xmlParser.setInputXML(outputXmlvalarea);
                    WFXmlResponse objXmlResponsevalarea = new WFXmlResponse(outputXmlvalarea);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        System.out.println("Iside main code 0");
                        for (WFXmlList objList = objXmlResponsevalarea.createList("TableParameters", "TT_VAL_AREA"); objList.hasMoreElements(); objList.skip()) {
                            System.out.println("Inside for loop");
                            valType = objList.getVal("VAL_TYPE");
                            System.out.println("Valtype value : " + valType);
                            formObject.addComboItem("Combo17", valType, valType);
                        }
                    }
                }


            }

            //pr new work Item
            //email on PR*******************************************************************
            if (pEvent.getSource().getName().equalsIgnoreCase("Button19")) {
                String email = "";
                email = formObject.getNGValue("Text9");
                if (email.equalsIgnoreCase("")) {
                    throw new ValidatorException(new FacesMessage("Please enter PO creator email id", ""));
                } else {
                    throw new ValidatorException(new CustomExceptionHandler("Mail", email, "", new HashMap()));
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button21")) {
                String email = "";
                email = formObject.getNGValue("Text116");
                if (email.equalsIgnoreCase("")) {
                    throw new ValidatorException(new FacesMessage("Please enter PO creator email id", ""));
                } else {
                    throw new ValidatorException(new CustomExceptionHandler("Mail", email, "", new HashMap()));
                }
            }
            //******************************************************************************
            if (pEvent.getSource().getName().equalsIgnoreCase("Button20")) {
                String pr_number = formObject.getNGValue("Text108");
                String processid = "";
                String childwi = "";
                if (!pr_number.equalsIgnoreCase("")) {

                    String sup_session = objGeneral.connectFlow();
                    if (sup_session == null || sup_session.equals("")) {
                        throw new ValidatorException(new FacesMessage("Error while creating connection. Please contact your Administrator! ", ""));
                    } else {
                        if ("Store Hold".equalsIgnoreCase(activityName)) {
                            childwi = "<storeChildWIFlag>Y</storeChildWIFlag>";
                        }
                        if ("Quality_Hold".equalsIgnoreCase(activityName)) {
                            childwi = "<qualityChildWIFlag>Y</qualityChildWIFlag>";
                        }
                        String attributs = childwi + "<PurchaseOrderNo>" + pr_number + "</PurchaseOrderNo>";
                        String inputXml = "<?xml version=\"1.0\"?>"
                                + "<WFUploadWorkItem_Input>"
                                + "<Option>WFUploadWorkItem</Option>"
                                + "<EngineName>" + engineName + "</EngineName>"
                                + "<SessionId>" + sup_session + "</SessionId>"
                                + "<ValidationRequired></ValidationRequired>"
                                + "<ProcessDefId>" + processDefId + "</ProcessDefId>"
                                + "<InitiateFromActivityId>63</InitiateFromActivityId>"
                                + "<DataDefName></DataDefName>"
                                + "<InitiateAlso>Y</InitiateAlso>"
                                + "<UserDefVarFlag>Y</UserDefVarFlag>"
                                + "<Documents></Documents>"
                                + "<Attributes>" + attributs + "</Attributes>"
                                + "</WFUploadWorkItem_Input>";
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_WFUploadWorkItem");
                        xmlParser.setInputXML(outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            processid = xmlParser.getValueOf("ProcessInstanceId");
                            if ("".equalsIgnoreCase(processid)) {
                                throw new ValidatorException(new FacesMessage("//System Error. Please contact your Administrator! ", ""));
                            } else {
                                formObject.RaiseEvent("WFSave");
                                String inputXml1 = "<?Xml version=1.0?>"
                                        + "<WFLinkWorkitem_Input>"
                                        + "<Option>WFLinkWorkitem</Option>"
                                        + "<EngineName>" + engineName + "</EngineName>"
                                        + "<SessionId>" + sessionId + "</SessionId>"
                                        + "<Operation>A</Operation>"
                                        + "<WorkItemId>1</WorkItemId>"
                                        + "<ProcessInstanceID>" + processInstanceId + "</ProcessInstanceID>"
                                        + "<LinkedProcessInstanceID>" + processid + "</LinkedProcessInstanceID>"
                                        + "</WFLinkWorkitem_Input>";
                                String outputXml1 = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_WFLinkWorkitem");
                                xmlParser.setInputXML(outputXml1);
                                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                                    formObject.setEnabled("Button19", false);
                                    formObject.setEnabled("Button20", false);
                                    formObject.setEnabled("Text108", false);
                                    formObject.setEnabled("Text9", false);
                                    formObject.setEnabled("storehold_decision", false);
                                    throw new ValidatorException(new FacesMessage("Linked Purchase Return WorkItem ID : " + processid, ""));
                                } else {
                                    throw new ValidatorException(new FacesMessage("Error while linking purchase return. Please contact your Administrator! ", ""));
                                }
                            }
                        } else {
                            throw new ValidatorException(new FacesMessage("Error while creating Purchase Return workitem. Please contact your Administrator! ", ""));
                        }
                    }
                } else {
                    throw new ValidatorException(new FacesMessage("Please Enter Return Purchase Order Number", ""));
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button13")) {
                String pr_number = formObject.getNGValue("Text110");
                String processid = "";
                String childwi = "";
                if (!pr_number.equalsIgnoreCase("")) {

                    String sup_session = objGeneral.connectFlow();
                    //System.out.println("Supervisor session : " + sup_session);
                    if (sup_session == null || sup_session.equals("")) {
                        //System.out.println("Session ID is NULL");
                        throw new ValidatorException(new FacesMessage("Error while creating connection. Please contact your Administrator! ", ""));
                    } else {
                        if ("Store Hold".equalsIgnoreCase(activityName)) {
                            childwi = "<storeChildWIFlag>Y</storeChildWIFlag>";
                        }
                        if ("Quality_Hold".equalsIgnoreCase(activityName)) {
                            childwi = "<qualityChildWIFlag>Y</qualityChildWIFlag>";
                        }
                        String attributs = childwi + "<PurchaseOrderNo>" + pr_number + "</PurchaseOrderNo>";
                        String inputXml = "<?xml version=\"1.0\"?>"
                                + "<WFUploadWorkItem_Input>"
                                + "<Option>WFUploadWorkItem</Option>"
                                + "<EngineName>" + engineName + "</EngineName>"
                                + "<SessionId>" + sup_session + "</SessionId>"
                                + "<ValidationRequired></ValidationRequired>"
                                + "<ProcessDefId>" + processDefId + "</ProcessDefId>"
                                + "<InitiateFromActivityId>63</InitiateFromActivityId>"
                                + "<DataDefName></DataDefName>"
                                + "<InitiateAlso>Y</InitiateAlso>"
                                + "<UserDefVarFlag>Y</UserDefVarFlag>"
                                + "<Documents></Documents>"
                                + "<Attributes>" + attributs + "</Attributes>"
                                + "</WFUploadWorkItem_Input>";
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_WFUploadWorkItem");
                        //System.out.println("output xml " + outputXml);
                        xmlParser.setInputXML(outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            processid = xmlParser.getValueOf("ProcessInstanceId");
                            if ("".equalsIgnoreCase(processid)) {
                                throw new ValidatorException(new FacesMessage("//System Error. Please contact your Administrator! ", ""));
                            } else {
                                formObject.RaiseEvent("WFSave");
                                //processInstanceId
                                String inputXml1 = "<?Xml version=1.0?>"
                                        + "<WFLinkWorkitem_Input>"
                                        + "<Option>WFLinkWorkitem</Option>"
                                        + "<EngineName>" + engineName + "</EngineName>"
                                        + "<SessionId>" + sessionId + "</SessionId>"
                                        + "<Operation>A</Operation>"
                                        + "<WorkItemId>1</WorkItemId>"
                                        + "<ProcessInstanceID>" + processInstanceId + "</ProcessInstanceID>"
                                        + "<LinkedProcessInstanceID>" + processid + "</LinkedProcessInstanceID>"
                                        + "</WFLinkWorkitem_Input>";

                                String outputXml1 = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_WFLinkWorkitem");

                                xmlParser.setInputXML(outputXml1);
                                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                                    //System.out.println("output xml 1 " + outputXml1);
                                    formObject.setEnabled("Button13", false);
                                    formObject.setEnabled("Button21", false);
                                    formObject.setEnabled("Text110", false);
                                    formObject.setEnabled("Text116", false);
                                    formObject.setEnabled("qualityhold_decision", false);

                                    throw new ValidatorException(new FacesMessage("Linked Purchase Return WorkItem ID : " + processid, ""));
                                } else {
                                    throw new ValidatorException(new FacesMessage("Error while linking purchase return. Please contact your Administrator! ", ""));
                                }

                            }
                        } else {
                            throw new ValidatorException(new FacesMessage("Error while creating Purchase Return workitem. Please contact your Administrator! ", ""));
                        }
                    }
                } else {
                    throw new ValidatorException(new FacesMessage("Please Enter Return Purchase Order Number", ""));
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button6")) {
                //System.out.println("Inside Button 6");
                formObject.RaiseEvent("WFSave");
            }

            ////////// ACCOUNTS ADD BUTTON ///////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("Button16")) {
                //System.out.println("Inside Button 16");
                int count = 0;
                String drop_val = formObject.getNGValue("Combo7");
                ListView lv1 = (ListView) formObject.getComponent("ListView8");
                int lvRowCount = lv1.getRowCount();
                //add for loop to iterate list view.
                for (int x = 0; x < lvRowCount; x++) {
                    //if list view description matched with combo item exit
                    String desc = formObject.getNGValue("ListView8", x, 4);
                    //System.out.println(desc + " <==> " + drop_val);
                    if (desc.equalsIgnoreCase(drop_val)) {
                        //System.out.println("Increasing Count");
                        count++;
                        break;
                    }
                }

                if (count == 0) {
                    //System.out.println("Inside Count");
                    String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_TDS");
                    inputXml = inputXml + "<Parameters>"
                            + "<ImportParameters>"
                            + "<VENDOR>" + formObject.getNGValue("vendor_code") + "</VENDOR>"
                            + "<COMP_CODE>1000</COMP_CODE>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_ZBAPI_AP_AUTOMATION_TDS");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    // //System.out.println("After xml response outputxml : " + outputXml);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {

                        String xmlnew = "";
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TDS_TAB"); objList.hasMoreElements(); objList.skip()) {
                            String DESC = objList.getVal("DESC");
                            String COUNTRY = objList.getVal("COUNTRY");
                            String WITHT = objList.getVal("WITHT");
                            String WT_WITHCD = objList.getVal("WT_WITHCD");
                            String QSATZ = objList.getVal("QSATZ");

                            if (DESC.equalsIgnoreCase(drop_val)) {

                                xmlnew = xmlnew + "<ListItem><SubItem>" + COUNTRY
                                        + "</SubItem><SubItem>" + WITHT
                                        + "</SubItem><SubItem>" + WT_WITHCD
                                        + "</SubItem><SubItem>" + QSATZ
                                        + "</SubItem><SubItem>" + DESC
                                        + "</SubItem></ListItem>";

                                //System.out.println("XML : " + xmlnew);
                                formObject.NGAddListItem("ListView8", xmlnew);
                                formObject.setNGValue("tdsflag", "true");
                                formObject.RaiseEvent("WFSave");
                            }
                        }
                    }
                } else {
                    throw new ValidatorException(new FacesMessage("Already exist.", ""));

                }
            }

            ////////// ACCOUNTS DELETE BUTTON ///////////////////
            if (pEvent.getSource().getName().equalsIgnoreCase("Button17")) {
                //System.out.println("Inside Button 17");
                formObject.ExecuteExternalCommand("NGDeleteRow", "ListView8");
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button2")) {
                String item_po = formObject.getNGValue("Text23");
                String purchaseorder_po = formObject.getNGValue("Text23");
                int rowIndex = repeaterControl.getRepeaterRowCount();
                boolean exist = false;
                int exist_row = 0;

                boolean addRow = false;
                for (int i = 0; i < rowIndex; i++) {
                    String item_invoice = repeaterControl.getValue(i, "q_Orient_invoice_item_item");
                    String item_invoice_po = repeaterControl.getValue(i, "q_orient_invoice_item_purchaseorder");
                    if (item_invoice.equalsIgnoreCase(item_po)
                            && item_invoice_po.equalsIgnoreCase(purchaseorder_po)) {
                        exist = true;
                        exist_row = i;
                        break;
                    }
                }
                if (exist) {
                    boolean rep_check = objGeneral.isRepRowDeleted("Frame3", exist_row);
                    if (rep_check) {
                        Query = "Select count(*) from complex_orient_invoice_item where pinstanceid = '" + processInstanceId + "'"
                                + " and item = '" + repeaterControl.getValue(exist_row, "q_Orient_invoice_item_item") + "' "
                                + "and PURCHASEORDER = '" + repeaterControl.getValue(exist_row, "q_orient_invoice_item_purchaseorder") + "'";
                        result = formObject.getDataFromDataSource(Query);
                        if (result.get(0).get(0).equalsIgnoreCase("0")) {
                            addRow = true;
                        } else {
                            addRow = false;
                        }
                    }
                } else {
                    addRow = true;
                }
                System.out.println("Add Row Flag : " + addRow);

                if (addRow) {
                    String PO_grid = "q_orient_po_item";
                    ListView lv0 = (ListView) formObject.getComponent(PO_grid);
                    int selectrow = lv0.getSelectedRowIndex();

                    String refreshflag = formObject.getNGValue("Text137");
                    if ("Y".equalsIgnoreCase(refreshflag)) {
                        selectrow = selectrow - 1;
                    }
                    float quantity_invoice = Float.parseFloat(formObject.getNGValue("Text40"));
                    String poitem = formObject.getNGValue(PO_grid, selectrow, 0);
                    String poq = formObject.getNGValue(PO_grid, selectrow, 2);
                    float quantity_po = Float.parseFloat(poq);
                    if (quantity_invoice > quantity_po) {
                        throw new ValidatorException(new FacesMessage("Quantity can't be higher than the quantity of selected PO", ""));
                    } else {
                        //  String qa_required = objGeneral.getQACheck(formObject.getNGValue("Text23"), formObject.getNGValue("Text44"));
                        int countbefore = repeaterControl.getRepeaterRowCount();
                        repeaterControl.addRow();
                        System.out.println(countbefore);
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_item", formObject.getNGValue("Text23"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_item_desc", formObject.getNGValue("Text39"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_quantity", formObject.getNGValue("Text40"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_price_per_unit", formObject.getNGValue("Text43"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_line_total_wtax", formObject.getNGValue("Text41"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_amount_wtax", formObject.getNGValue("Text42"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_plant", formObject.getNGValue("Text44"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_weightunit", formObject.getNGValue("Text45"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_po_item", formObject.getNGValue("Text46"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_base_uom", formObject.getNGValue("Text54"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_base_uom_iso", formObject.getNGValue("Text53"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_tax_code", formObject.getNGValue("Text55"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_store_location", formObject.getNGValue("Text48"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_profit_centre", formObject.getNGValue("Text49"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_batch", formObject.getNGValue("Text65"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_valuation", formObject.getNGValue("Combo17"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_taxvalue", formObject.getNGValue("Text62"));
                        repeaterControl.setValue(countbefore, "q_Orient_invoice_item_qa_required", "");
                        repeaterControl.setValue(countbefore, "q_orient_invoice_item_purchaseorder", formObject.getNGValue("Text133"));
                        repeaterControl.setValue(countbefore, "q_orient_invoice_item_asn_item", "");
                        objmatching.updateMatch("Total Amount");
//                        formObject.RaiseEvent("WFSave");

                        //******************RAHUL---ADD Row in invocie REpeater*******************8//
                        int toalrow = repeaterControl.getRepeaterRowCount();

                        double po_total = 0.0;

                        for (int i = 0; i < toalrow; i++) {
                            boolean rep_check = objGeneral.isRepRowDeleted("Frame3", i);
                            if (!rep_check) {
                                po_total = po_total + Double.parseDouble(repeaterControl.getValue(i, "q_Orient_invoice_item_amount_wtax"));
                            }
                        }
                        double po_total_roundOff = Math.round(po_total * 100.0) / 100.0;
                        System.out.println("PO TOTAL :::" + po_total_roundOff + " " + toalrow);
                        formObject.setNGValue("po_total", String.valueOf(po_total_roundOff));

                        formObject.RaiseEvent("WFSave");
                        objmatching.updateMatch("Total Amount");
                        //******************RAHUL*******************8//
                    }



                } else {
                    throw new ValidatorException(new FacesMessage("Already added for selected Item Code", ""));
                }


            }
            //*************************************************************************
            //Post Goods Movement-BAPI_GOODSMVT_CREATE ***START***
            if (pEvent.getSource().getName().equalsIgnoreCase("Button1")) {
                if (formObject.getNGValue("InvoiceNo").equalsIgnoreCase("")) {
                    throw new ValidatorException(new FacesMessage("Please set Invoice Number", ""));
                }

                if (formObject.getNGValue("InvoiceDate").equalsIgnoreCase("")) {
                    throw new ValidatorException(new FacesMessage("Please set Invoice Date", ""));
                }
                if (formObject.getItemCount("List1") > 0) {
                    throw new ValidatorException(new FacesMessage("Please clear all exceptions to proceed further", ""));
                } else {
                    String linetotal = objmatching.getPOLineAmount();
                    String invoiceamount = formObject.getNGValue("InvoiceAmount");

                    //CR Code added for 103 duplicate check **Start**
                    if (activityName.equalsIgnoreCase("Gate Entry")) {
                        String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_MIGOCHECK");
                        inputXml = inputXml + "<Parameters>"
                                + "<ImportParameters>"
                                + "<PURCH_ORD>" + formObject.getNGValue("PurchaseOrderNo") + "</PURCH_ORD>"
                                + "<REF_DOC>" + formObject.getNGValue("InvoiceNo") + "</REF_DOC>"
                                + "<VENDOR></VENDOR>"
                                + "</ImportParameters></Parameters></WFSAPInvokeFunction_Input>";
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_GOODSMVT_CREATE");
                        xmlParser.setInputXML(outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            System.out.println("Inisde maincode");
                            for (WFXmlList objList = objXmlResponse.createList("ExportParameters", "RETURN"); objList.hasMoreElements(); objList.skip()) {
                                System.out.println("Inside for loop");
                                String RETURN = xmlParser.getValueOf("TYPE");
                                System.out.println("Return VAlue :" + RETURN);
                                if ("E".equalsIgnoreCase(RETURN)) {
                                    throw new ValidatorException(new FacesMessage("The GRN with same invoice number has been already processed.", ""));
                                } else {
                                    throw new ValidatorException(new CustomExceptionHandler(linetotal, invoiceamount, "", new HashMap()));
                                }
                            }
                        }
                    } else {
                        throw new ValidatorException(new CustomExceptionHandler(linetotal, invoiceamount, "", new HashMap()));
                    }
                }
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Button10")) {

                String movetype_field = "", material = "", po_item = "", goodsmvt_item = "", document_no = null,
                        matdoc = null, ref_docTag = null, message1 = null, poasn_tags = "",
                        asn_number = formObject.getNGValue("asn_number");

                if (asn_number.equalsIgnoreCase("") // || activityName.equalsIgnoreCase("Gate Entry")
                        ) {

                    if (activityName.equalsIgnoreCase("Store")) {
                        movetype_field = formObject.getNGValue("mdoc105");
                    }
                    if (activityName.equalsIgnoreCase("Gate Entry")) {
                        movetype_field = formObject.getNGValue("mdoc103");
                    }
                    int rowcountinvoice = repeaterControl.getRepeaterRowCount();
                    ////System.out.println("rowcount : " + rowcountinvoice);
                    if (rowcountinvoice <= 0) {
                        throw new ValidatorException(new FacesMessage("Please add row for Invoice", ""));
                    } else if ("103".equalsIgnoreCase(formObject.getNGValue("movement_type")) && "".equalsIgnoreCase(formObject.getNGValue("remarks_gateentry"))) {
                        throw new ValidatorException(new FacesMessage("Please enter some remarks before Goods Receipts/103", ""));
                    } else if ("101".equalsIgnoreCase(formObject.getNGValue("movement_type")) && "".equalsIgnoreCase(formObject.getNGValue("remarks_gateentry"))) {
                        throw new ValidatorException(new FacesMessage("Please enter some remarks before Goods Receipts/101", ""));
                    } else if ("105".equalsIgnoreCase(formObject.getNGValue("movement_type")) && "".equalsIgnoreCase(formObject.getNGValue("remarks_store"))) {
                        throw new ValidatorException(new FacesMessage("Please enter some remarks before Release GR Stock Block/105", ""));
                    } else {
                        for (int i = 0; i < rowcountinvoice; i++) {
                            boolean rep_check = objGeneral.isRepRowDeleted("Frame3", i);
                            if (!rep_check) { //added for duplicity
                                //System.out.println("inside first for");
                                String material_temp = repeaterControl.getValue(i, "q_Orient_invoice_item_item");//formObject.getNGValue(LVControlName, i, 0);
                                material = objGeneral.trimMaterialNumber(material_temp, processInstanceId);

//                                difference = 18 - material_temp.length();
//                                if (difference > 0) {
//                                    material = String.format("%0" + (18 - material_temp.length()) + "d%s", 0, material_temp);
//                                } else {
//                                    material = material_temp;
//                                }
//                                

                                String po_item_temp = repeaterControl.getValue(i, "q_Orient_invoice_item_po_item");//formObject.getNGValue(LVControlName, i, 8);
                                difference = 5 - po_item_temp.length();
                                if (difference > 0) {
                                    po_item = String.format("%0" + (5 - po_item_temp.length()) + "d%s", 0, po_item_temp);
                                } else {
                                    po_item = po_item_temp;
                                }

                                if (formObject.getNGValue("movement_type").equalsIgnoreCase("103")
                                        || formObject.getNGValue("movement_type").equalsIgnoreCase("101")
                                        || formObject.getNGValue("movement_type").equalsIgnoreCase("161")) {
                                    ref_docTag = "<REF_DOC></REF_DOC>";
                                } else if (formObject.getNGValue("movement_type").equalsIgnoreCase("105")) {
                                    Query = "select max (mat_doc) from  complex_orient_poitem_history where "
                                            + "pinstanceid = '" + processInstanceId + "' and move_type = '103' "
                                            + "and REF_DOC_NO  = '" + formObject.getNGValue("InvoiceNo") + "'";
                                    result = formObject.getDataFromDataSource(Query);
                                    ref_docTag = "<REF_DOC>" + result.get(0).get(0) + "</REF_DOC>";
                                }
                                String reasontag = "";
                                if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                                    String reason = formObject.getNGValue("Combo10");
                                    reasontag = "<MOVE_REAS>" + reason + "</MOVE_REAS>";
                                } else {
                                    reasontag = "";
                                }

                                goodsmvt_item = goodsmvt_item + "<GOODSMVT_ITEM>"
                                        + "<MATERIAL>" + material + "</MATERIAL>"
                                        + "<PLANT>" + repeaterControl.getValue(i, "q_Orient_invoice_item_plant") + "</PLANT>"
                                        + "<MOVE_TYPE>" + formObject.getNGValue("movement_type") + "</MOVE_TYPE>"
                                        + "<ENTRY_QNT>" + repeaterControl.getValue(i, "q_Orient_invoice_item_quantity") + "</ENTRY_QNT>"
                                        + "<ENTRY_UOM>" + repeaterControl.getValue(i, "q_Orient_invoice_item_base_uom") + "</ENTRY_UOM>"
                                        + "<PO_NUMBER>" + repeaterControl.getValue(i, "q_orient_invoice_item_purchaseorder") + "</PO_NUMBER>"
                                        + "<PO_ITEM>" + po_item + "</PO_ITEM>"
                                        + poasn_tags
                                        + "<STGE_LOC>" + repeaterControl.getValue(i, "q_Orient_invoice_item_store_location") + "</STGE_LOC>"
                                        + "<BATCH>" + repeaterControl.getValue(i, "q_Orient_invoice_item_batch") + "</BATCH>"
                                        + "<VAL_TYPE>" + repeaterControl.getValue(i, "q_Orient_invoice_item_valuation") + "</VAL_TYPE>"
                                        + ref_docTag
                                        + reasontag
                                        + "<MVT_IND>B</MVT_IND>"
                                        + "<PROFIT_CTR>" + repeaterControl.getValue(i, "q_Orient_invoice_item_profit_centre") + "</PROFIT_CTR>"
                                        + "</GOODSMVT_ITEM>";
                            }
                        }
                        //System.out.println("GoodSmvt_item xml: " + goodsmvt_item);
                        String inputXml = objGeneral.sapInvokeXML("BAPI_GOODSMVT_CREATE");
                        inputXml = inputXml + "<Parameters>"
                                + "<ImportParameters>"
                                + "<GOODSMVT_HEADER>"
                                + "<PSTNG_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("postingDate")) + " 00:00:00.0</PSTNG_DATE>"
                                + "<DOC_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</DOC_DATE>"
                                + "<REF_DOC_NO>" + formObject.getNGValue("InvoiceNo").toUpperCase() + "</REF_DOC_NO>"
                                //+ "<PR_UNAME>AWCDMS</PR_UNAME>"
                                + "<PR_UNAME>" + userName + "</PR_UNAME>"
                                + "<HEADER_TXT>" + userName + "</HEADER_TXT>"
                                + "</GOODSMVT_HEADER>"
                                + "<GOODSMVT_CODE>"
                                + "<GM_CODE>01</GM_CODE>"
                                + "</GOODSMVT_CODE>"
                                + "</ImportParameters>"
                                + "<TableParameters>"
                                + goodsmvt_item
                                + "</TableParameters>"
                                + "</Parameters>"
                                + "</WFSAPInvokeFunction_Input>";
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_GOODSMVT_CREATE");
                        xmlParser.setInputXML(outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            document_no = xmlParser.getValueOf("MAT_DOC");
                            matdoc = xmlParser.getValueOf("MATERIALDOCUMENT");
                            message1 = xmlParser.getValueOf("MESSAGE");
                            if ("".equalsIgnoreCase(matdoc)) {
                                throw new ValidatorException(new FacesMessage("Alert : " + message1, ""));
                            } else {
                                if (activityName.equalsIgnoreCase("Store")) {
                                    formObject.setEnabled("mdoc105", true);
                                    formObject.setNGValue("mdoc105", document_no);
                                    formObject.setNGValue("store_decision", "Migo Performed");
                                    formObject.setNGValue("mdoc105_rev", "");
                                }
                                if (activityName.equalsIgnoreCase("Gate Entry")) {
                                    formObject.setNGValue("mdoc103", document_no);
                                    formObject.setNGValue("gate_decision", "Processed");
                                    formObject.setNGValue("mdoc103_rev", "");
                                    formObject.setNGValue("gateexception_flag", true);
                                    formObject.setEnabled("gateexception_flag", false);
                                }

                                formObject.RaiseEvent("WFSave");
                                throw new ValidatorException(new FacesMessage("Material Number :" + document_no, ""));
                            }
                        } else {
                            throw new ValidatorException(new FacesMessage("Please Contact Your Administrator", ""));
                        }
                    }
                } else {
                    throw new ValidatorException(new CustomExceptionHandler("Button10", "InvokeAsn", "", new HashMap()));
                }
            }
            //Post Goods Movement-BAPI_GOODSMVT_CREATE ***END***

            //Park Incomingvoice detils-BAPI_INCOMINGINVOICE_PARK ***START***
            if (pEvent.getSource().getName().equalsIgnoreCase("Button5")) {
                String linetotal = "", taxvalue = "", invoiceamount = "";// invoicegrid = "q_orient_invoice_item";
                float line, tax, sum = 0, invamount = 0;
                int rowcountpo9 = repeaterControl.getRepeaterRowCount();
                for (int l = 0; l < rowcountpo9; l++) {
                    linetotal = repeaterControl.getValue(l, "q_Orient_invoice_item_line_total_wtax");
                    taxvalue = repeaterControl.getValue(l, "q_Orient_invoice_item_taxvalue");
                    line = Float.parseFloat(linetotal);
                    if ("".equalsIgnoreCase(taxvalue)) {
                        tax = 0;
                    } else {
                        tax = Float.parseFloat(taxvalue);
                    }
                    sum = sum + line + tax;
                }
                linetotal = "" + sum;
                invoiceamount = formObject.getNGValue("InvoiceAmount");
                throw new ValidatorException(new CustomExceptionHandler(invoiceamount, linetotal, "", new HashMap()));
            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Button9")) {
                if ("".equalsIgnoreCase(formObject.getNGValue("inv_park"))) {
                    if ("".equalsIgnoreCase(formObject.getNGValue("comp_code"))
                            || formObject.getNGValue("comp_code").equalsIgnoreCase("null")) {
                        throw new ValidatorException(new FacesMessage("Please enter value for Company Code", ""));
                    }

                    String po_item = "", poitem1 = "";
                    String itemdata = "";
                    String materialdata = "", debitcredit = "";
                    String invmaterial = "", histmaterial = "", baseuom = "", baseuomiso = "";
                    String LVControlNamehis = "ListView6";
                    if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                        debitcredit = "H";
                    } else {
                        debitcredit = "S";
                    }

                    //XML to creation get "Business Place" 
                    String busPlc_val = "";
                    String inputXml1 = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_BUSPLACE");
                    inputXml1 = inputXml1 + "<Parameters>"
                            + "<ImportParameters>"
                            + "<PLANT>" + repeaterControl.getValue(0, "q_Orient_invoice_item_plant") + "</PLANT>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    String outputXml1 = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_ZBAPI_AP_AUTOMATION_BUSPLACE");
                    xmlParser.setInputXML(outputXml1);
                    WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXml1);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        busPlc_val = xmlParser.getValueOf("BUSS_PLACE");
                    } else {
                        busPlc_val = "";
                    }

                    // XML creation for "ITEMDATA"
                    Query = "select REF_DOC_NO, MATERIAL, QUANTITY, MAT_DOC, DOC_DATE,VAL_LOCCUR, MOVE_TYPE, PO_ITEM, REF_DOC_YR, "
                            + "REF_DOC_IT, TAX_CODE, REF_DOC,purchaseorder "
                            + "from complex_orient_poitem_history where pinstanceid = '" + processInstanceId + "' "
                            + " order by PO_ITEM";

                    System.out.println("Query History : " + Query);
                    resultarray1 = formObject.getDataFromDataSource(Query);
                    ListView lv0 = (ListView) formObject.getComponent(LVControlNamehis);
                    String LVPOname = "q_orient_po_item";
                    ListView lv1 = (ListView) formObject.getComponent(LVPOname);
                    int rowcountpo = lv1.getRowCount();
                    int lvRowCount = resultarray1.size();//lv0.getRowCount();
                    System.out.println("List view row count :- " + lvRowCount + "--PO row count :-" + rowcountpo);
                    String po_unit = "", tax_code = "", po_unit_iso = "", item_no = "", val_type = "", po_number = "",
                            orderpr_un = "", orderpr_un_iso = "";
                    for (int i = 0; i < lvRowCount; i++) {

                        String invoicedocitem = String.valueOf(i + 1) + "0";
                        invoicedocitem = String.format("%0" + (6 - invoicedocitem.length()) + "d%s", 0, invoicedocitem);
                        String po_item_temp1 = resultarray1.get(i).get(7);
                        difference = 5 - po_item_temp1.length();
                        if (difference > 0) {
                            poitem1 = String.format("%0" + (5 - po_item_temp1.length()) + "d%s", 0, po_item_temp1);

                        } else {
                            poitem1 = resultarray1.get(i).get(7);
                        }
                        histmaterial = resultarray1.get(i).get(1);
                        String po_item_temp = resultarray1.get(i).get(7);
                        difference = 6 - po_item_temp.length();
                        if (difference > 0) {
                            po_item = String.format("%0" + (6 - po_item_temp.length()) + "d%s", 0, po_item_temp);
                        }
                        String store_matdoc = formObject.getNGValue("mdoc105");
                        if (("105".equalsIgnoreCase(resultarray1.get(i).get(6))
                                && store_matdoc.equalsIgnoreCase(resultarray1.get(i).get(3)))
                                || "101".equalsIgnoreCase(resultarray1.get(i).get(6))) {
                            String parkrefdoc = "";
                            Query = "Select po.PO_UNIT_ISO,po.UNIT,po.TAX_CODE,po.PO_NUMBER,inv.valuation,po.ORDERPR_UN,"
                                    + "po.ORDERPR_UN_ISO,inv.plant from "
                                    + "complex_orient_po_item po, complex_orient_invoice_item inv  "
                                    + "where po.pinstanceid = inv.pinstanceid "
                                    + "and po.pinstanceid='" + processInstanceId + "' "
                                    + "and po.MATERIAL='" + histmaterial + "' "
                                    + "and po.PO_NUMBER = '" + (resultarray1.get(i).get(12)).substring(0, 10) + "'";

                            System.out.println("Query itemdata : " + Query);
                            resultarray = formObject.getDataFromDataSource(Query);
                            if (resultarray.size() > 0) {
                                po_unit_iso = resultarray.get(0).get(0);
                                po_unit = resultarray.get(0).get(1);
                                tax_code = resultarray.get(0).get(2);
                                po_number = resultarray.get(0).get(3);
                                parkrefdoc = resultarray1.get(i).get(11);
                                // val_type = resultarray.get(0).get(4);
                                orderpr_un = resultarray.get(0).get(5);
                                orderpr_un_iso = resultarray.get(0).get(6);
                                String plant = resultarray.get(0).get(7);

                                // val_type = resultarray.get(0).get(4);

                                String inputXmlValarea = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_VAL_AREA");
                                inputXmlValarea = inputXmlValarea + "<Parameters>"
                                        + "<ImportParameters>"
                                        + "<MATERIAL>" + histmaterial + "</MATERIAL>"
                                        + "<PLANT>" + plant + "</PLANT>"
                                        + "</ImportParameters>"
                                        + "</Parameters>"
                                        + "</WFSAPInvokeFunction_Input>";
                                System.out.println("Input XML : " + inputXmlValarea);
                                String outputXmlvalarea = objGeneral.executeWithCallBroker(inputXmlValarea, processInstanceId + "_ZBAPI_AP_AUTOMATION_VAL_AREA");
                                xmlParser.setInputXML(outputXmlvalarea);
                                WFXmlResponse objXmlResponsevalarea = new WFXmlResponse(outputXmlvalarea);
                                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                                    System.out.println("Iside main code 0");
                                    for (WFXmlList objList = objXmlResponsevalarea.createList("TableParameters", "TT_VAL_AREA"); objList.hasMoreElements(); objList.skip()) {
                                        System.out.println("Inside for loop");
                                        val_type = objList.getVal("VAL_TYPE");
                                        System.out.println("Valtype value : " + val_type);
                                        break;
                                    }
                                }
                                System.out.println("Valtype value after break : " + val_type);
                                itemdata = itemdata + "<ITEMDATA>"
                                        + "<INVOICE_DOC_ITEM>" + invoicedocitem + "</INVOICE_DOC_ITEM>"
                                        + "<PO_NUMBER>" + po_number + "</PO_NUMBER>"
                                        + "<PO_UNIT>" + po_unit + "</PO_UNIT>"
                                        + "<PO_UNIT_ISO>" + po_unit_iso + "</PO_UNIT_ISO>"
                                        + "<PO_ITEM>" + poitem1 + "</PO_ITEM>"
                                        + "<REF_DOC>" + parkrefdoc + "</REF_DOC>"
                                        + "<VALUATION_TYPE>" + val_type + "</VALUATION_TYPE>"
                                        + "<REF_DOC_YEAR>" + resultarray1.get(i).get(8) + "</REF_DOC_YEAR>"
                                        + "<REF_DOC_IT>" + resultarray1.get(i).get(9) + "</REF_DOC_IT>"
                                        + "<ITEM_AMOUNT>" + resultarray1.get(i).get(5) + "</ITEM_AMOUNT>"
                                        + "<QUANTITY>" + resultarray1.get(i).get(2) + "</QUANTITY>"
                                        + "<PO_PR_UOM>" + orderpr_un + "</PO_PR_UOM>"
                                        + "<PO_PR_UOM_ISO>" + orderpr_un_iso + "</PO_PR_UOM_ISO>"
                                        + "<TAX_CODE>" + tax_code + "</TAX_CODE>";
                                System.out.println("itemdata before pounit : " + itemdata);
                                itemdata = itemdata + "</ITEMDATA>";

                                for (int k = 0; k < rowcountpo; k++) {
                                    if (resultarray1.get(i).get(7).equalsIgnoreCase(formObject.getNGValue(LVPOname, k, 8))
                                            && resultarray1.get(i).get(12).equalsIgnoreCase(formObject.getNGValue(LVPOname, k, 17))) {
                                        materialdata = materialdata + "<MATERIALDATA>"
                                                + "<INVOICE_DOC_ITEM>" + poitem1 + "</INVOICE_DOC_ITEM>"
                                                + "<MATERIAL>" + formObject.getNGValue(LVPOname, k, 0) + "</MATERIAL>"
                                                + "<VAL_AREA>" + formObject.getNGValue(LVPOname, k, 6) + "</VAL_AREA>"
                                                + "<DB_CR_IND>" + debitcredit + "</DB_CR_IND>"
                                                + "<VALUATION_TYPE>" + val_type + "</VALUATION_TYPE>"
                                                + "<QUANTITY>" + formObject.getNGValue(LVPOname, k, 2) + "</QUANTITY>"
                                                + "<BASE_UOM>" + formObject.getNGValue(LVPOname, k, 9) + "</BASE_UOM>"
                                                + "<BASE_UOM_ISO>" + formObject.getNGValue(LVPOname, k, 10) + "</BASE_UOM_ISO>";
                                        if ("".equalsIgnoreCase(formObject.getNGValue(LVPOname, k, 11)) || formObject.getNGValue(LVPOname, k, 11).equalsIgnoreCase("null")) {
                                            materialdata = materialdata + "<TAX_CODE></TAX_CODE>"
                                                    + "</MATERIALDATA>";
                                        } else {
                                            materialdata = materialdata + "<TAX_CODE>" + formObject.getNGValue(LVPOname, k, 11) + "</TAX_CODE>"
                                                    + "</MATERIALDATA>";
                                        }
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("itemdata xml : " + itemdata);
                    System.out.println("material data : " + materialdata);
                    LVControlName = "ListView8";
                    lv0 = (ListView) formObject.getComponent(LVControlName);
                    lvRowCount = lv0.getRowCount();
                    String withholdingtax = "";
                    formObject = FormContext.getCurrentInstance().getFormReference();
                    formConfig = FormContext.getCurrentInstance().getFormConfig();

                    IRepeater repeaterControl1 = formObject.getRepeaterControl("Frame3");
                    objGeneral = new General();
                    String linetotal = "";
                    float line = 0.0f, lineTotal;
                    int rowcountpo9 = repeaterControl1.getRepeaterRowCount();
                    for (int l = 0; l < rowcountpo9; l++) {
                        boolean rep_check = objGeneral.isRepRowDeleted("Frame3", l);

                        if (!rep_check) {
                            linetotal = repeaterControl.getValue(l, "q_Orient_invoice_item_line_total_wtax");
                            line = line + Float.parseFloat(linetotal);
                        }
                    }
                    for (int i = 0; i < lvRowCount; i++) {
                        float wi_percent = Float.parseFloat(formObject.getNGValue(LVControlName, i, 3));
                        float wi_base_amount = (wi_percent / 100) * line;
                        withholdingtax = withholdingtax + "<WITHTAXDATA>"
                                + "<SPLIT_KEY>000001</SPLIT_KEY>"
                                + "<WI_TAX_TYPE>" + formObject.getNGValue(LVControlName, i, 1) + "</WI_TAX_TYPE>"
                                + "<WI_TAX_CODE>" + formObject.getNGValue(LVControlName, i, 2) + "</WI_TAX_CODE>"
                                + "<WI_TAX_BASE>" + line + "</WI_TAX_BASE>"
                                + "<WI_TAX_AMT>" + Math.round(Math.ceil(wi_base_amount)) + "</WI_TAX_AMT>"
                                + "</WITHTAXDATA>";
                    }

                    //******WithHolding Tax********//
                    String inputXml = objGeneral.sapInvokeXML("BAPI_INCOMINGINVOICE_PARK");
                    inputXml = inputXml + "<Parameters><ImportParameters>"
                            + "<HEADERDATA>"
                            + "<INVOICE_IND>X</INVOICE_IND>"
                            + "<DOC_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</DOC_DATE>"
                            + "<PSTNG_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("postingDate")) + " 00:00:00.0</PSTNG_DATE>"
                            + "<REF_DOC_NO>" + formObject.getNGValue("InvoiceNo") + "</REF_DOC_NO>"
                            + "<COMP_CODE>" + formObject.getNGValue("comp_code") + "</COMP_CODE>"
                            + "<CURRENCY>" + formObject.getNGValue("currency") + "</CURRENCY>"
                            + "<GROSS_AMOUNT>" + formObject.getNGValue("InvoiceAmount") + "</GROSS_AMOUNT>"
                            + "<HEADER_TXT>" + userName + "</HEADER_TXT>"
                            + "<BUSINESS_PLACE>" + busPlc_val + "</BUSINESS_PLACE>"
                            + "<CALC_TAX_IND>X</CALC_TAX_IND>"
                            + "</HEADERDATA>"
                            + "</ImportParameters>"
                            + "<TableParameters>"
                            + itemdata
                            + materialdata
                            + withholdingtax
                            + "</TableParameters></Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    inputXml = inputXml.replace("null", "");
                    inputXml = inputXml.replace("NULL", "");
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_INCOMINGINVOICE_PARK");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        String INVOICEDOCNUMBER = xmlParser.getValueOf("INVOICEDOCNUMBER");
                        if ("".equalsIgnoreCase(INVOICEDOCNUMBER)) {
                            String message1 = xmlParser.getValueOf("MESSAGE");
                            throw new ValidatorException(new FacesMessage("Error while parking: " + message1, ""));
                        } else {
                            formObject.setNGValue("inv_park", INVOICEDOCNUMBER);
                            String FISCALYEAR = xmlParser.getValueOf("FISCALYEAR");
                            String inputXml_getFinalDoc = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_ACC_DOC_NO");
                            inputXml_getFinalDoc = inputXml_getFinalDoc + "<Parameters><ImportParameters>"
                                    + "<LV_REFKEY>" + INVOICEDOCNUMBER + FISCALYEAR + "</LV_REFKEY>"
                                    + "</ImportParameters>"
                                    + "</Parameters>"
                                    + "</WFSAPInvokeFunction_Input>";
                            String outputXml_getFinalDoc = objGeneral.executeWithCallBroker(inputXml_getFinalDoc, processInstanceId + "_ZBAPI_AP_AUTOMATION_ACC_DOC");
                            xmlParser.setInputXML(outputXml_getFinalDoc);
                            WFXmlResponse objXmlResponse_getFinalDoc = new WFXmlResponse(outputXml_getFinalDoc);
                            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                                String AC_DOC_NO = xmlParser.getValueOf("AC_DOC_NO");
                                if (!AC_DOC_NO.equalsIgnoreCase("")) {
                                    formObject.setNGValue("inv_park_sap", AC_DOC_NO);
                                    formObject.RaiseEvent("WFSave");
                                    throw new ValidatorException(new FacesMessage("Invoice Document Number :" + INVOICEDOCNUMBER, ""));
                                } else {
                                    throw new ValidatorException(new FacesMessage("Error while fetching parked document number", ""));
                                }
                            }
                        }
                    } else {
                        throw new ValidatorException(new FacesMessage("Please Contact Your Administrator", ""));
                    }
                } else {
                    throw new ValidatorException(new FacesMessage("Parking is already done for this Purchase order and Invoice. Invoice Document Number is : " + formObject.getNGValue("inv_park")));
                }
            }
            //Park Incomingvoice detils-BAPI_INCOMINGINVOICE_PARK ***END***

            //Post Incomingvoice detils-BAPI_INCOMINGINVOICE_CREATE1 ***START***
            //Post Incomingvoice detils-BAPI_INCOMINGINVOICE_POST ***START***
            if (pEvent.getSource().getName().equalsIgnoreCase("Button4")) {
                ////System.out.println("Inside Post Incoming invoice Button");
                String po_item = "", poitem1 = "";
                String itemdata = "";
                String materialdata = "", debitcredit = "";
                String invmaterial = "", histmaterial = "", baseuom = "", baseuomiso = "";
                String LVControlNamehis = "ListView6";
                String LVPOname = "q_orient_po_item";

                if ("".equalsIgnoreCase(formObject.getNGValue("inv_post"))) {
                    String inputXml1 = "";
                    inputXml1 = objGeneral.sapInvokeXML("BAPI_INCOMINGINVOICE_POST");
                    inputXml1 = inputXml1 + "<Parameters>"
                            + "<ImportParameters>"
                            + "<INVOICEDOCNUMBER>" + formObject.getNGValue("inv_park") + "</INVOICEDOCNUMBER>"
                            + "<FISCALYEAR>" + fiscalYear + "</FISCALYEAR>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";

                    String outputXml = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_BAPI_INCOMINGINVOICE_POST");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    ////System.out.println("After xml response");
                    String INVOICEDOCNUMBER = "", return_number = "", return_message = "";

                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        return_number = xmlParser.getValueOf("NUMBER");
                        return_message = xmlParser.getValueOf("MESSAGE");
                        ////System.out.println("Message Post  : " + return_number);

                        if (return_number.equalsIgnoreCase("000")) {
                            formObject.setNGValue("inv_post", formObject.getNGValue("inv_park"));
                            formObject.setNGValue("finance_decision", "Posted");
                            formObject.setEnabled("finance_decision", false);
                            formObject.setEnabled("remarks_finance", false);
                            formObject.RaiseEvent("WFSave");
                            throw new ValidatorException(new FacesMessage("Successfully Posted. Document Number : " + formObject.getNGValue("inv_park"), ""));
                        } else {
                            throw new ValidatorException(new FacesMessage("Error while Posting." + return_message, ""));
                        }

                    } else {
                        throw new ValidatorException(new FacesMessage("Please Contact Your Administrator", ""));
                    }
                } else {
                    throw new ValidatorException(new FacesMessage("Posting is already done for this Purchase order and Invoice. Invoice Document Number is : " + formObject.getNGValue("inv_park")));
                }

            }
            //Post Incoming Invoice detils-BAPI_INCOMINGINVOICE_CREATE1 ***END***
            //Post Incoming Invoice detils-BAPI_INCOMINGINVOICE_POST ***END***

            if (pEvent.getSource().getName().equalsIgnoreCase("Button14")) {
                ////System.out.println("Inside Post Incoming invoice Button");
                String po_item = "", poitem1 = "";
                String itemdata = "";
                String materialdata = "", debitcredit = "";
                String invmaterial = "", histmaterial = "", baseuom = "", baseuomiso = "";
                String LVControlNamehis = "ListView6";
                String LVPOname = "q_orient_po_item";

                if ("ZPRT".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                    debitcredit = "H";
                } else {
                    debitcredit = "S";
                }

                // XML creation for "ITEMDATA"
                ListView lv0 = (ListView) formObject.getComponent(LVControlNamehis);
                ListView lv1 = (ListView) formObject.getComponent(LVPOname);
                int rowcountpo = lv1.getRowCount();
                int lvRowCount = lv0.getRowCount();
                String item_no = "", po_unit = "", po_unit_iso = "";
                for (int i = 0; i < lvRowCount; i++) {

                    String po_item_temp1 = formObject.getNGValue(LVControlNamehis, i, 7);
                    difference = 5 - po_item_temp1.length();
                    if (difference > 0) {
                        poitem1 = String.format("%0" + (5 - po_item_temp1.length()) + "d%s", 0, po_item_temp1);

                    } else {
                        poitem1 = formObject.getNGValue(LVControlNamehis, i, 7);
                    }
                    histmaterial = formObject.getNGValue(LVControlNamehis, i, 1);
                    String po_item_temp = formObject.getNGValue(LVControlNamehis, i, 7);
                    difference = 6 - po_item_temp.length();
                    if (difference > 0) {
                        po_item = String.format("%0" + (6 - po_item_temp.length()) + "d%s", 0, po_item_temp);
                    }
                    if ("105".equalsIgnoreCase(formObject.getNGValue(LVControlNamehis, i, 6))
                            || "101".equalsIgnoreCase(formObject.getNGValue(LVControlNamehis, i, 6))) {
                        String parkrefdoc = "";
                        Query = "select PO_UNIT_ISO,UNIT,MATERIAL from complex_orient_po_item where pinstanceid='" + processInstanceId + "' "
                                + "and MATERIAL='" + histmaterial + "'";
                        //System.out.println("Query itemdata : " + Query);
                        resultarray = formObject.getDataFromDataSource(Query);

                        po_unit_iso = resultarray.get(0).get(0);
                        po_unit = resultarray.get(0).get(1);
                        parkrefdoc = formObject.getNGValue(LVControlNamehis, i, 13);
                        itemdata = itemdata + "<ITEMDATA>"
                                + "<INVOICE_DOC_ITEM>" + po_item + "</INVOICE_DOC_ITEM>"
                                + "<PO_NUMBER>" + formObject.getNGValue("PurchaseOrderNo") + "</PO_NUMBER>"
                                + "<PO_UNIT>" + po_unit + "</PO_UNIT>"
                                + "<PO_UNIT_ISO>" + po_unit_iso + "</PO_UNIT_ISO>"
                                + "<PO_ITEM>" + poitem1 + "</PO_ITEM>"
                                + "<REF_DOC>" + parkrefdoc + "</REF_DOC>"
                                + "<REF_DOC_YEAR>" + formObject.getNGValue(LVControlNamehis, i, 8) + "</REF_DOC_YEAR>"
                                + "<REF_DOC_IT>" + formObject.getNGValue(LVControlNamehis, i, 9) + "</REF_DOC_IT>";
                        if ("".equalsIgnoreCase(formObject.getNGValue(LVControlNamehis, i, 10)) || formObject.getNGValue(LVControlNamehis, i, 10).equalsIgnoreCase("null")) {
                            itemdata = itemdata + "<TAX_CODE></TAX_CODE>";

                        } else {
                            itemdata = itemdata + "<TAX_CODE>" + formObject.getNGValue(LVControlNamehis, i, 10) + "</TAX_CODE>";
                        }

                        itemdata = itemdata + "<ITEM_AMOUNT>" + formObject.getNGValue(LVControlNamehis, i, 5) + "</ITEM_AMOUNT>"
                                + "<QUANTITY>" + formObject.getNGValue(LVControlNamehis, i, 2) + "</QUANTITY>"
                                + "</ITEMDATA>";

                        //XML creation for "MATERIALDATA"
                        for (int k = 0; k < rowcountpo; k++) {
                            //matching invoice_doc_item
                            if (formObject.getNGValue(LVControlNamehis, i, 7).equalsIgnoreCase(formObject.getNGValue("q_orient_po_item", k, 8))) {

                                materialdata = materialdata + "<MATERIALDATA>"
                                        + "<INVOICE_DOC_ITEM>" + poitem1 + "</INVOICE_DOC_ITEM>"
                                        + "<MATERIAL>" + formObject.getNGValue("q_orient_po_item", k, 0) + "</MATERIAL>"
                                        + "<VAL_AREA>" + formObject.getNGValue("q_orient_po_item", k, 6) + "</VAL_AREA>"
                                        + "<DB_CR_IND>" + debitcredit + "</DB_CR_IND>"
                                        + "<QUANTITY>" + formObject.getNGValue("q_orient_po_item", k, 2) + "</QUANTITY>"
                                        + "<BASE_UOM>" + formObject.getNGValue("q_orient_po_item", k, 9) + "</BASE_UOM>"
                                        + "<BASE_UOM_ISO>" + formObject.getNGValue("q_orient_po_item", k, 10) + "</BASE_UOM_ISO>";
                                if ("".equalsIgnoreCase(formObject.getNGValue("q_orient_po_item", k, 11)) || formObject.getNGValue("q_orient_po_item", k, 11).equalsIgnoreCase("null")) {
                                    materialdata = materialdata + "<TAX_CODE></TAX_CODE>"
                                            + "</MATERIALDATA>";
                                } else {
                                    materialdata = materialdata + "<TAX_CODE>" + formObject.getNGValue("q_orient_po_item", k, 11) + "</TAX_CODE>"
                                            + "</MATERIALDATA>";
                                }

                            }
                        }
                    }
                }
                String inputXml = objGeneral.sapInvokeXML("BAPI_INCOMINGINVOICE_CREATE1");
                inputXml = inputXml + "<Parameters><ImportParameters>"
                        + "<HEADERDATA>"
                        + "<INVOICE_IND>X</INVOICE_IND>"
                        + "<DOC_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</DOC_DATE>"
                        + "<PSTNG_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("postingDate")) + " 00:00:00.0</PSTNG_DATE>"
                        + "<REF_DOC_NO>" + formObject.getNGValue("InvoiceNo") + "</REF_DOC_NO>"
                        + "<COMP_CODE>" + formObject.getNGValue("comp_code") + "</COMP_CODE>"
                        + "<CURRENCY>" + formObject.getNGValue("currency") + "</CURRENCY>"
                        + "<GROSS_AMOUNT>" + formObject.getNGValue("InvoiceAmount") + "</GROSS_AMOUNT>"
                        + "<HEADER_TXT>" + userName + "</HEADER_TXT>"
                        + "<CALC_TAX_IND>X</CALC_TAX_IND>"
                        + "<SIMULATION>X</SIMULATION>"
                        + "</HEADERDATA>"
                        + "</ImportParameters>"
                        + "<TableParameters>"
                        + itemdata
                        + materialdata
                        + "</TableParameters></Parameters>"
                        + "</WFSAPInvokeFunction_Input>";

                ////System.out.println("Input XML : " + inputXml);
            }

            //Post Incoming Invoice detils-BAPI_ENTRYSHEET_CREATE ***START***
            if (pEvent.getSource().getName().equalsIgnoreCase("Button11")) {
                workItemId = formConfig.getConfigElement("WorkitemId");
                ////System.out.println("Inside Button11 BAPI_ENTRYSHEET_CREATE");
                if ("".equalsIgnoreCase(formObject.getNGValue("DatePicker4")) || "".equalsIgnoreCase(formObject.getNGValue("DatePicker5"))) {
                    ////System.out.println("inside validator exception");
                    throw new ValidatorException(new FacesMessage("Please enter Begin Date and Ending Date", ""));
                } else {
                    ////System.out.println("inside else.....!!!!");
                    String entrysheetaccountassignent = "", extline = "", entrysheetservices = "",
                            pckgno = "", store_loc = "", po_item = "", subpkgno = "";
                    ListView lvService = (ListView) formObject.getComponent("q_orient_entry_service");
                    ListView lvAccAssignment = (ListView) formObject.getComponent("q_orient_acc_assignment");
                    ArrayList<String> l1 = new ArrayList<String>();

                    int r_count_service = lvService.getRowCount();
                    int r_count_accassignment = lvAccAssignment.getRowCount();
                    int r_count_invoice = repeaterControl.getRepeaterRowCount();
                    int r_count_service_sel = repeaterControlF6.getRepeaterRowCount();
                    String material = "";
                    material = formObject.getNGValue("service_itemselect");
                    String material1 = material.replace("'", "%");
                    Query = "select PCKG_NO,STORE_LOC,PO_ITEM from complex_orient_po_item where SHORT_TEXT "
                            + "like '%" + material1 + "%' and pinstanceid='" + processInstanceId + "'";
                    //System.out.println("query : " + Query);
                    resultarray = formObject.getDataFromDataSource(Query);
                    ////System.out.println("size of array : " + resultarray.size());
                    pckgno = resultarray.get(0).get(0);
                    store_loc = resultarray.get(0).get(1);
                    po_item = resultarray.get(0).get(2);
                    /*
                     * Get selected lines
                     */
                    for (int l = 0; l < r_count_service_sel; l++) {
                        ////System.out.println("inside second for");
                        String selectFlag_val = repeaterControlF6.getValue(l, "q_orient_service_itemsel_select_flag");
                        if (selectFlag_val.equalsIgnoreCase("true")) {
                            if (extline.equalsIgnoreCase("")) {
                                extline = "'" + repeaterControlF6.getValue(l, "q_orient_service_itemsel_line_no") + "'";
                            } else {
                                extline = extline + ",'" + repeaterControlF6.getValue(l, "q_orient_service_itemsel_line_no") + "'";
                            }
                        }
                    }
                    /*
                     *
                     */

                    Query = "select count(SERVICE) from complex_orient_entry_service where pinstanceid = '" + processInstanceId + "'";
                    result = formObject.getDataFromDataSource(Query);
                    ////System.out.println("Size : " + result.get(0).get(0));

                    if (Integer.parseInt(result.get(0).get(0)) > 0) {
                        //XML creation of ENTRYSHEETACCOUNTASSIGNMENT when Service Number is present
                        for (int k = 0; k < r_count_accassignment; k++) {
                            ////System.out.println("inside first for");
                            entrysheetaccountassignent = entrysheetaccountassignent + "<ENTRYSHEETACCOUNTASSIGNMENT>"
                                    //q_orient_acc_assignment
                                    + "<PCKG_NO>" + pckgno + "</PCKG_NO>"
                                    + "<SERIAL_NO>" + formObject.getNGValue("q_orient_acc_assignment", k, 1) + "</SERIAL_NO>"
                                    + "<GL_ACCOUNT>" + formObject.getNGValue("q_orient_acc_assignment", k, 4) + "</GL_ACCOUNT>"
                                    + "<PROFIT_CTR>" + formObject.getNGValue("q_orient_acc_assignment", k, 5) + "</PROFIT_CTR>"
                                    + "</ENTRYSHEETACCOUNTASSIGNMENT>";
                        }

                        //XML creation of ENTRYSHEETSERVICES
                        Query = "Select PCKG_NO ,LINE_NO ,SERVICE , EXT_LINE, OUTL_LEVEL, OUTL_NO, OUTL_IND, "
                                + "SUBPCKG_NO, SERV_TYPE, QUANTITY, BASE_UOM, UOM_ISO, OVF_TOL, PRICE_UNIT, "
                                + "GR_PRICE, SHORT_TEXT, TAX_CODE, MATL_GROUP from complex_orient_entry_service "
                                + "where pinstanceid = '" + processInstanceId + "'"
                                + "and PCKG_NO = '" + pckgno + "' "
                                + "union "
                                + "Select PCKG_NO ,LINE_NO ,SERVICE , EXT_LINE, OUTL_LEVEL, OUTL_NO, OUTL_IND, "
                                + "SUBPCKG_NO, SERV_TYPE, QUANTITY, BASE_UOM, UOM_ISO, OVF_TOL, PRICE_UNIT, "
                                + "GR_PRICE, SHORT_TEXT, TAX_CODE, MATL_GROUP from complex_orient_entry_service "
                                + "where pinstanceid = '" + processInstanceId + "' and "
                                + "PCKG_NO in (Select SUBPCKG_NO from complex_orient_entry_service where "
                                + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + pckgno + "')"
                                + " and EXT_LINE in (" + extline + ")";
                        //System.out.println("Query : " + Query);
                        result = formObject.getDataFromDataSource(Query);
                        for (int k = 0; k < result.size(); k++) {

                            entrysheetservices = entrysheetservices + "<ENTRYSHEETSERVICES>"
                                    + "<PCKG_NO>" + result.get(k).get(0) + "</PCKG_NO>"
                                    + "<LINE_NO>" + result.get(k).get(1) + "</LINE_NO>"
                                    + "<SERVICE>" + result.get(k).get(2) + "</SERVICE>"
                                    + "<EXT_LINE>" + result.get(k).get(3) + "</EXT_LINE>"
                                    + "<OUTL_LEVEL>" + result.get(k).get(4) + "</OUTL_LEVEL>"
                                    + "<OUTL_NO>" + result.get(k).get(5) + "</OUTL_NO>"
                                    + "<OUTL_IND>" + result.get(k).get(6) + "</OUTL_IND>"
                                    + "<SUBPCKG_NO>" + result.get(k).get(7) + "</SUBPCKG_NO>"
                                    + "<SERV_TYPE>" + result.get(k).get(8) + "</SERV_TYPE>"
                                    + "<QUANTITY>" + result.get(k).get(9) + "</QUANTITY>"
                                    + "<BASE_UOM>" + result.get(k).get(10) + "</BASE_UOM>"
                                    + "<UOM_ISO>" + result.get(k).get(11) + "</UOM_ISO>"
                                    + "<OVF_TOL>" + result.get(k).get(12) + "</OVF_TOL>"
                                    + "<PRICE_UNIT>" + result.get(k).get(13) + "</PRICE_UNIT>"
                                    + "<GR_PRICE>" + result.get(k).get(14) + "</GR_PRICE>"
                                    + "<SHORT_TEXT>" + result.get(k).get(15) + "</SHORT_TEXT>"
                                    + "<TAX_CODE>" + result.get(k).get(16) + "</TAX_CODE>"
                                    + "<MATL_GROUP>" + result.get(k).get(17) + "</MATL_GROUP>"
                                    + "</ENTRYSHEETSERVICES>";
                        }
                        //for ENTRY SHEET HEADER DATA
                        String entrysheetheader = "";
                        ////System.out.println("Row count : " + r_count_invoice);
//                        for (int l = 0; l < r_count_invoice; l++) {
//                            //System.out.println("inside 3rd for");

                        entrysheetheader = entrysheetheader + "<LOCATION>" + store_loc + "</LOCATION>"
                                + "<REF_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</REF_DATE>"
                                + "<BEGDATE>" + convertNewgenDateToSapDate(formObject.getNGValue("DatePicker4")) + " 00:00:00.0</BEGDATE>"
                                + "<ENDDATE>" + convertNewgenDateToSapDate(formObject.getNGValue("DatePicker5")) + " 00:00:00.0</ENDDATE>"
                                + "<PCKG_NO>" + pckgno + "</PCKG_NO>"
                                + "<SHORT_TEXT>" + material + "</SHORT_TEXT>"
                                + "<PO_NUMBER>" + formObject.getNGValue("PurchaseOrderNo") + "</PO_NUMBER>"
                                + "<PO_ITEM>" + po_item + "</PO_ITEM>"
                                //   + "<SCORE_TIME></SCORE_TIME>"
                                //    + "<SCORE_QUAL></SCORE_QUAL>"
                                + "<DOC_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</DOC_DATE>"
                                + "<POST_DATE>" + today + " 00:00:00.0</POST_DATE>"
                                + "<REF_DOC_NO>" + formObject.getNGValue("InvoiceNo") + "</REF_DOC_NO>"
                                + "<ACCASSCAT>K</ACCASSCAT>"
                                + "<ACCEPTANCE>X</ACCEPTANCE>";

//                        }
                        String inputXml = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_CREATE");
                        inputXml = inputXml + "<Parameters>"
                                + "<ImportParameters>"
                                + "<ENTRYSHEETHEADER>"
                                + entrysheetheader
                                + "</ENTRYSHEETHEADER>"
                                + "</ImportParameters>"
                                + "<TableParameters>"
                                + entrysheetaccountassignent
                                + entrysheetservices
                                + "</TableParameters>"
                                + "</Parameters></WFSAPInvokeFunction_Input>";

                        inputXml = inputXml.replaceAll("null", "");
                        inputXml = inputXml.replaceAll("&", "");
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_ENTRYSHEET_CREATE");
                        xmlParser.setInputXML(outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        ////System.out.println("After xml response");
                        String entrymessage = "", entrysheet_no = "";
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            entrysheet_no = xmlParser.getValueOf("ENTRYSHEET");
                            formObject.setNGValue("entrysheet_no", entrysheet_no);
                            entrymessage = xmlParser.getValueOf("MESSAGE");
                            formObject.setNGValue("accounts_decision", "Park");
                            formObject.RaiseEvent("WFSave");
                            throw new ValidatorException(new FacesMessage(entrymessage, ""));
                        }
                        /////// When Service Number is absent /////////////////////  
                    } else {
                        try {
                            String quantity = "", net_val = "";

                            Query = "select EXT_LINE, EDITION, OUTL_LEVEL, OUTL_IND, QUANTITY, BASE_UOM, UOM_ISO, OVF_TOL, "
                                    + "PRICE_UNIT, GR_PRICE, SHORT_TEXT, FROM_LINE, PERS_NO, CON_PCKG, "
                                    + "CON_LINE, TMP_PCKG, TMP_LINE, LIMIT_LINE, TARGET_VAL, BASLINE_NO, BEGINTIME, ENDTIME, "
                                    + "FORM_VAL1, FORM_VAL2, FORM_VAL3, FORM_VAL4, FORM_VAL5, USERF1_NUM, USERF2_NUM, HI_LINE_NO, "
                                    + "SERVICE_ITEM_KEY, NET_VALUE,LINE_NO from complex_orient_entry_service where "
                                    + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + pckgno + "' "
                                    + "Union "
                                    + "select EXT_LINE, EDITION, OUTL_LEVEL, OUTL_IND, QUANTITY, BASE_UOM, UOM_ISO, OVF_TOL, "
                                    + "PRICE_UNIT, GR_PRICE, SHORT_TEXT, FROM_LINE, PERS_NO, CON_PCKG, "
                                    + "CON_LINE, TMP_PCKG, TMP_LINE, LIMIT_LINE, TARGET_VAL, BASLINE_NO, BEGINTIME, ENDTIME, "
                                    + "FORM_VAL1, FORM_VAL2, FORM_VAL3, FORM_VAL4, FORM_VAL5, USERF1_NUM, USERF2_NUM, HI_LINE_NO, "
                                    + "SERVICE_ITEM_KEY, NET_VALUE,LINE_NO from complex_orient_entry_service where "
                                    + "pinstanceid = '" + processInstanceId + "' and PCKG_NO in "
                                    + "(Select SUBPCKG_NO from complex_orient_entry_service where "
                                    + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + pckgno + "')"
                                    + " and EXT_LINE in (" + extline + ")";
                            //System.out.println("Query :" + Query);
                            result = formObject.getDataFromDataSource(Query);
                            int temp = 0;
                            for (int l = 0; l < result.size(); l++) {
                                ////System.out.println("inside of else");
                                int pkg_no = l + 1;
                                int subpkg_no = 0;
                                String pln_line = "0";
                                int pkcno_new = pkg_no;
                                ////System.out.println("OUTL_IND : " + result.get(l).get(3));
                                if ("X".equalsIgnoreCase(result.get(l).get(3))) {
                                    ////System.out.println("Inside OUTL_IND : X ");
                                    subpkg_no = pkg_no + 1;
                                    pln_line = "32";
                                    temp = subpkg_no;
                                    quantity = result.get(l).get(4);
                                    net_val = result.get(l).get(31);
                                } else {
                                    ////System.out.println("Inside OUTL_IND : EMPTY ");
                                    subpkg_no = 0;
                                    pln_line = result.get(l).get(32);
                                    pkcno_new = temp;
                                    Query = "select quantity,gross_price from complex_orient_service_itemsel where "
                                            + "pinstanceid =  '" + processInstanceId + "'  "
                                            + "and line_no = '" + result.get(l).get(0) + "'";
                                    //System.out.println("Query : " + Query);
                                    resultarray1 = formObject.getDataFromDataSource(Query);
                                    quantity = resultarray1.get(0).get(0);
                                    net_val = resultarray1.get(0).get(0);
                                }

                                //int plnline;
                                String plnpkg;
                                if (l == 0) {
                                    //plnline = 0;
                                    plnpkg = "0";

                                } else {
                                    String Query1 = "select SUBPCKG_NO from complex_orient_entry_service where "
                                            + "pinstanceid = '" + processInstanceId + "' and PCKG_NO = '" + pckgno + "'";
                                    //System.out.println("Query : " + Query1);
                                    resultarray = formObject.getDataFromDataSource(Query1);
                                    ////System.out.println("After get data");
                                    String x = resultarray.get(0).get(0);
                                    ////System.out.println("Result : " + x);
                                    //plnline = pkg_no;
                                    plnpkg = x;
                                }
                                ////System.out.println("Package Number before :" + pkg_no);

                                entrysheetservices = entrysheetservices + "<ENTRYSHEETSERVICES>"
                                        + "<PCKG_NO>" + pkcno_new + "</PCKG_NO>"
                                        + "<LINE_NO>" + pkg_no + "</LINE_NO>"
                                        //  + "<EXT_LINE>" + result.get(l).get(0) + "</EXT_LINE>"
                                        + "<EXT_LINE>" + result.get(l).get(0) + "</EXT_LINE>"
                                        + "<EDITION>" + result.get(l).get(1) + "</EDITION>"
                                        + "<OUTL_LEVEL>" + result.get(l).get(2) + "</OUTL_LEVEL>"
                                        + "<OUTL_IND>" + result.get(l).get(3) + "</OUTL_IND>"
                                        + "<SUBPCKG_NO>" + subpkg_no + "</SUBPCKG_NO>"
                                        //  + "<QUANTITY>" + result.get(l).get(4) + "</QUANTITY>"
                                        + "<QUANTITY>" + quantity + "</QUANTITY>"
                                        + "<BASE_UOM>" + result.get(l).get(5) + "</BASE_UOM>"
                                        + "<UOM_ISO>" + result.get(l).get(6) + "</UOM_ISO>"
                                        + "<OVF_TOL>" + result.get(l).get(7) + "</OVF_TOL>"
                                        + "<PRICE_UNIT>" + result.get(l).get(8) + "</PRICE_UNIT>"
                                        + "<GR_PRICE>" + result.get(l).get(9) + "</GR_PRICE>"
                                        + "<SHORT_TEXT>" + result.get(l).get(10) + "</SHORT_TEXT>"
                                        + "<FROM_LINE>1</FROM_LINE>"
                                        + "<PERS_NO>" + result.get(l).get(12) + "</PERS_NO>"
                                        + "<PLN_PCKG>" + plnpkg + "</PLN_PCKG>"
                                        // + "<PLN_LINE>" + plnline + "</PLN_LINE>"
                                        + "<PLN_LINE>" + pln_line + "</PLN_LINE>"
                                        + "<CON_PCKG>" + result.get(l).get(13) + "</CON_PCKG>"
                                        + "<CON_LINE>" + result.get(l).get(14) + "</CON_LINE>"
                                        + "<TMP_PCKG>" + result.get(l).get(15) + "</TMP_PCKG>"
                                        + "<TMP_LINE>" + result.get(l).get(16) + "</TMP_LINE>"
                                        + "<LIMIT_LINE>" + result.get(l).get(17) + "</LIMIT_LINE>"
                                        + "<TARGET_VAL>" + result.get(l).get(18) + "</TARGET_VAL>"
                                        + "<BASLINE_NO>" + result.get(l).get(19) + "</BASLINE_NO>"
                                        + "<BEGINTIME>" + result.get(l).get(20) + "</BEGINTIME>"
                                        + "<ENDTIME>" + result.get(l).get(21) + "</ENDTIME>"
                                        + "<FORM_VAL1>" + result.get(l).get(22) + "</FORM_VAL1>"
                                        + "<FORM_VAL2>" + result.get(l).get(23) + "</FORM_VAL2>"
                                        + "<FORM_VAL3>" + result.get(l).get(24) + "</FORM_VAL3>"
                                        + "<FORM_VAL4>" + result.get(l).get(25) + "</FORM_VAL4>"
                                        + "<FORM_VAL5>" + result.get(l).get(26) + "</FORM_VAL5>"
                                        + "<USERF1_NUM>" + result.get(l).get(27) + "</USERF1_NUM>"
                                        + "<USERF2_NUM>" + result.get(l).get(28) + "</USERF2_NUM>"
                                        + "<HI_LINE_NO>" + result.get(l).get(29) + "</HI_LINE_NO>"
                                        + "<SERVICE_ITEM_KEY>" + result.get(l).get(30) + "</SERVICE_ITEM_KEY>"
                                        //+ "<NET_VALUE>" + result.get(l).get(31) + "</NET_VALUE>"
                                        + "<NET_VALUE>" + net_val + "</NET_VALUE>"
                                        + "</ENTRYSHEETSERVICES>";
                                ////System.out.println("Package Number After :" + pkg_no);
                            }
                        } catch (Exception e) {
                            //System.out.println("Exception " + e);
                        }
                        ////System.out.println("entrysheetservices : " + entrysheetservices);
                        //for ENTRY SHEET HEADER DATA
                        String entrysheetheader = "";
                        ////System.out.println("Row count for r_count_invoice : " + r_count_invoice);
//                        for (int l = 0; l < r_count_invoice; l++) {
//                            //System.out.println("inside 3rd for");
                        entrysheetheader = entrysheetheader
                                + "<PCKG_NO>1</PCKG_NO>"
                                + "<SHORT_TEXT>" + material + "</SHORT_TEXT>"
                                + "<PO_NUMBER>" + formObject.getNGValue("PurchaseOrderNo") + "</PO_NUMBER>"
                                + "<PO_ITEM>" + po_item + "</PO_ITEM>"
                                //   + "<SCORE_TIME></SCORE_TIME>"
                                //    + "<SCORE_QUAL></SCORE_QUAL>"
                                + "<DOC_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("InvoiceDate")) + " 00:00:00.0</DOC_DATE>"
                                + "<POST_DATE>" + today + " 00:00:00.0</POST_DATE>"
                                + "<REF_DOC_NO>" + formObject.getNGValue("InvoiceNo") + "</REF_DOC_NO>"
                                + "<ACCASSCAT>K</ACCASSCAT>"
                                + "<ACCEPTANCE>X</ACCEPTANCE>"
                                + "<SCORE_TIME>0</SCORE_TIME>"
                                + "<SCORE_QUAL>0</SCORE_QUAL>";

                        //    }
                        String inputXml = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_CREATE");
                        inputXml = inputXml + "<Parameters>"
                                + "<ImportParameters>"
                                + "<ENTRYSHEETHEADER>"
                                + entrysheetheader
                                + "</ENTRYSHEETHEADER>"
                                + "</ImportParameters>"
                                + "<TableParameters>"
                                + entrysheetservices
                                + "</TableParameters>"
                                + "</Parameters></WFSAPInvokeFunction_Input>";

                        inputXml = inputXml.replaceAll("null", "");
                        inputXml = inputXml.replaceAll("&", "");
                        String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_ENTRYSHEET_CREATE");
                        xmlParser.setInputXML(outputXml);
                        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                        ////System.out.println("After xml response");
                        String entrymessage = "", entrysheet_no = "";
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            entrysheet_no = xmlParser.getValueOf("ENTRYSHEET");
                            formObject.setNGValue("entrysheet_no", entrysheet_no);
                            entrymessage = xmlParser.getValueOf("MESSAGE");
                            formObject.setNGValue("accounts_decision", "Park");
                            formObject.RaiseEvent("WFSave");
                            throw new ValidatorException(new FacesMessage(entrymessage, ""));

                        }

                    }
                }
            }

            // Entry sheet delete
            if (pEvent.getSource().getName().equalsIgnoreCase("Button7")) {
                ////System.out.println("Innside service entry sheet delete");
                objentrysheet_delete = new entrysheet_delete();

                String inputXml = objentrysheet_delete.getENTRYSHEET_RESET_RELEASE_Input();
                ////System.out.println("Input xml : " + inputXml);
                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_ENTRYSHEET_RESET_RELEASE");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    String number = xmlParser.getValueOf("NUMBER");
                    String msg = xmlParser.getValueOf("MESSAGE");
                    if (number.equalsIgnoreCase("000")) {
                        String inputXml1 = objentrysheet_delete.getENTRYSHEET_DELETE_Input();
                        String outputXml1 = objGeneral.executeWithCallBroker(inputXml1, processInstanceId + "_BAPI_ENTRYSHEET_DELETE");
                        xmlParser.setInputXML(outputXml1);
                        WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXml1);
                        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                            String number1 = xmlParser.getValueOf("NUMBER");
                            String msg1 = xmlParser.getValueOf("MESSAGE");
                            if (number1.equalsIgnoreCase("000")) {
                                ////System.out.println("Number of 2nd XML : " + number1);
                                ////System.out.println("Message of 2nd XML : " + msg1);
                            } else {
                                throw new ValidatorException(new FacesMessage(msg1, ""));
                            }
                        }
                    } else {
                        throw new ValidatorException(new FacesMessage(msg, ""));
                    }

                }
            }

            //fetch updated TAX
            if (pEvent.getSource().getName().equalsIgnoreCase("Button12")) {

                String po_item = "", material = "", tax_code = "";
                int poitem = 0;

                String inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL");
                inputXml = inputXml + "<Parameters>"
                        + "<ImportParameters>"
                        + "<PURCHASEORDER>"
                        + formObject.getNGValue("PurchaseOrderNo")
                        + "</PURCHASEORDER>"
                        + "<ITEMS>X</ITEMS>"
                        + "</ImportParameters>"
                        + "</Parameters></WFSAPInvokeFunction_Input>";

                String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_PO_GETDETAIL");
                xmlParser.setInputXML(outputXml);
                WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                ////System.out.println("After xml response");
                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                    for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {
                        po_item = objList.getVal("PO_ITEM");
                        material = objList.getVal("MATERIAL");
                        tax_code = objList.getVal("TAX_CODE");
                        poitem = Integer.parseInt(po_item);
                        po_item = "" + poitem;
                        ////System.out.println("po_item : " + po_item + " material : " + material + " tax_code : " + tax_code);
                        Query = "select TAX_CODE from complex_orient_po_item "
                                + "where pinstanceid='" + processInstanceId + "' "
                                + "and MATERIAL='" + material + "' "
                                + "and PO_ITEM like '%" + po_item + "%'";
                        ////System.out.println("Query : " + Query);
                        resultarray = formObject.getDataFromDataSource(Query);
                        ////System.out.println("result array : " + resultarray.get(0).get(0));
                        if (tax_code.equalsIgnoreCase(resultarray.get(0).get(0))) {
                            ////System.out.println("if");
                        } else {
                            String quantity = "", price_per = "", total = "", tax_value = "";
                            float quan = 0, price = 0, sum = 0;

                            ////System.out.println("else");
                            Query = "UPDATE complex_orient_po_item "
                                    + "set TAX_CODE='" + tax_code + "' "
                                    + "where pinstanceid='" + processInstanceId + "' "
                                    + "and MATERIAL='" + material + "' "
                                    + "and PO_ITEM like '%" + po_item + "%'";
                            ////System.out.println("update query 1 : " + Query);
                            formObject.saveDataIntoDataSource(Query);
                            //for invoice line items

                            ////System.out.println("poitem : " + po_item);
                            Query = "select quantity,price_per_unit from complex_orient_invoice_item "
                                    + "where pinstanceid='" + processInstanceId + "' "
                                    + "and item='" + material + "' "
                                    + "and po_item like '%" + po_item + "%'";
                            ////System.out.println("Query for invoice : " + Query);
                            resultarray = formObject.getDataFromDataSource(Query);
                            ////System.out.println("qunatity : " + resultarray.get(0).get(0) + " price per unit : " + resultarray.get(0).get(1));
                            quantity = resultarray.get(0).get(0);
                            price_per = resultarray.get(0).get(1);
                            quan = Float.parseFloat(quantity);
                            price = Float.parseFloat(price_per);
                            sum = quan * price;
                            total = "" + sum;
                            tax_value = objGeneral.calculateTax(total, tax_code);
                            sum = sum + Float.parseFloat(tax_value);
                            total = "" + sum;
                            Query = "update complex_orient_invoice_item set "
                                    + "tax_code='" + tax_code + "', "
                                    + "taxvalue='" + tax_value + "', "
                                    + "line_total_wtax='" + total + "' "
                                    + "where pinstanceid='" + processInstanceId + "' "
                                    + "and item=='" + material + "' "
                                    + "and po_item like '%" + po_item + "%'";
                            ////System.out.println("update query 2 : " + Query);
                            formObject.saveDataIntoDataSource(Query);
                        }
                    }

                }

            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button15")) {
                String matdoc = "", message1 = "";
                objgoodsmvt_cancel = new goodsmvt_cancel();
                int repeater_rowcount = repeaterControl.getRepeaterRowCount();
                if (repeater_rowcount > 0) {
                    String mdoc_id = "", decisio_id = "", decision_val = "", mdoc_id_rev = "";
                    if (activityName.equalsIgnoreCase("Gate Entry")) {
                        mdoc_id = "mdoc103";
                        mdoc_id_rev = "mdoc103_rev";
                        decisio_id = "gate_decision";
                        decision_val = "Reversed";
                    }

                    if (activityName.equalsIgnoreCase("Store")) {
                        mdoc_id = "mdoc105";
                        mdoc_id_rev = "mdoc105_rev";
                        decisio_id = "store_decision";
                        decision_val = "Migo Reversed";
                    }

                    String inputXml = objgoodsmvt_cancel.getGoodsmvt_Cancel_Input(fiscalYear, mdoc_id);
                    // //System.out.println("Input xml : " + inputXml);
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_GOODSMVT_CANCEL");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        matdoc = xmlParser.getValueOf("MAT_DOC");
                        message1 = xmlParser.getValueOf("MESSAGE");
                        if ("".equalsIgnoreCase(matdoc)) {
                            throw new ValidatorException(new FacesMessage("Alert : " + message1, ""));
                        } else {
                            formObject.setNGValue(mdoc_id, "");
                            formObject.setEnabled(decisio_id, true);
                            formObject.setNGValue(mdoc_id_rev, matdoc);
                            formObject.setNGValue(decisio_id, decision_val);
                            repeaterControlF4.clear();
                            formObject.RaiseEvent("WFSave");
                            throw new ValidatorException(new FacesMessage("Reversal Material Number :" + matdoc, ""));
                        }
                    }
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button18")) {
                if ("Yes".equalsIgnoreCase(formObject.getNGValue("reversal_req"))) {
                    String inputXml = objGeneral.sapInvokeXML("BAPI_INCOMINGINVOICE_CANCEL");
                    inputXml = inputXml + "<Parameters>"
                            + "<ImportParameters>"
                            + "<INVOICEDOCNUMBER>" + formObject.getNGValue("inv_post") + "</INVOICEDOCNUMBER>"
                            + "<FISCALYEAR>" + fiscalYear + "</FISCALYEAR>"
                            + "<REASONREVERSAL>" + formObject.getNGValue("reversal_reason") + "</REASONREVERSAL>"
                            + "<POSTINGDATE>" + convertNewgenDateToSapDate(formObject.getNGValue("postingDate")) + " 00:00:00.0</POSTINGDATE>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_INCOMINGINVOICE_CANCEL");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);

                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        ////System.out.println("Inside Main Code 0");
                        String rev_doc_no = xmlParser.getValueOf("INVOICEDOCNUMBER_REVERSAL");
                        if (!rev_doc_no.equalsIgnoreCase("")) {
                            formObject.setNGValue("inv_post_rev", rev_doc_no);
                            formObject.setNGValue("inv_park", "");
                            formObject.setNGValue("inv_post", "");
                            throw new ValidatorException(new CustomExceptionHandler("Reversal", rev_doc_no, "", new HashMap()));
                        } else {
                            throw new ValidatorException(new FacesMessage("Error while document reversal : " + xmlParser.getValueOf("MESSAGE"), ""));
                        }
                    } else {
                        throw new ValidatorException(new FacesMessage("Please try after sometime or contact your administrative", ""));
                    }
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button26")) {
                System.out.println("Button click 22");
                String purchaseorderno = "";
                ListView lv1 = (ListView) formObject.getComponent("q_multiplepo");
                int rowcount = lv1.getRowCount();
                formObject.clear("q_orient_po_item");
                //   formObject.RaiseEvent("WFSave");

                for (int i = 0; i < rowcount + 1; i++) {
                    if (i == 0) {
                        purchaseorderno = formObject.getNGValue("PurchaseOrderNo");
                    } else {
                        purchaseorderno = formObject.getNGValue("q_multiplepo", i, 0);
                    }
                    String xmlnew = "";
                    String outputXml = objpo_getdetail.getPO_GETDETAIL(purchaseorderno);
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        if (xmlParser.getValueOf("PO_NUMBER").equalsIgnoreCase(purchaseorderno)) {
                            for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_ITEMS"); objList.hasMoreElements(); objList.skip()) {
                                xmlnew = xmlnew
                                        + "<ListItem><SubItem>" + objList.getVal("MATERIAL")
                                        + "</SubItem><SubItem>" + objList.getVal("SHORT_TEXT")
                                        + "</SubItem><SubItem>" + objList.getVal("QUANTITY")
                                        + "</SubItem><SubItem>" + objList.getVal("NET_PRICE")
                                        + "</SubItem><SubItem>" + objList.getVal("NET_VALUE")
                                        + "</SubItem><SubItem>" + objList.getVal("ACCTASSCAT")
                                        + "</SubItem><SubItem>" + objList.getVal("PLANT")
                                        + "</SubItem><SubItem>" + objList.getVal("UNIT")
                                        + "</SubItem><SubItem>" + objList.getVal("PO_ITEM")
                                        + "</SubItem><SubItem>" + objList.getVal("BASE_UNIT")
                                        + "</SubItem><SubItem>" + objList.getVal("BASE_UOM_ISO")
                                        + "</SubItem><SubItem>" + objList.getVal("TAX_CODE")
                                        + "</SubItem><SubItem>" + objList.getVal("STORE_LOC")
                                        + "</SubItem><SubItem>" + objList.getVal("PROFIT_CTR")
                                        + "</SubItem><SubItem>" + ""
                                        + "</SubItem><SubItem>" + objList.getVal("VAL_TYPE")
                                        + "</SubItem><SubItem>" + ""
                                        + "</SubItem><SubItem>" + purchaseorderno
                                        + "</SubItem><SubItem>" + objList.getVal("PRICE_UNIT")
                                        + "</SubItem><SubItem>" + objList.getVal("ORDERPR_UN")
                                        + "</SubItem><SubItem>" + objList.getVal("ORDERPR_UN_ISO")
                                        + "</SubItem></ListItem>";
                            }
                            System.out.println("XML : " + xmlnew);
                            formObject.NGAddListItem("q_orient_po_item", xmlnew);
                        }
                    }
                }
                formObject.setNGValue("Text137", "Y");
                formObject.RaiseEvent("WFSave");
            }
        }
    }

    @Override
    public void formLoaded(FormEvent arg0) {
        // TODO Auto-generated method stub
        //System.out.println(" -------------------Intiation Workstep Loaded from formloaded.----------------");
        // TODO Auto-generated method stub
        ////System.out.println("form Loaded called");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        ////System.out.println("formObject :" + formObject);
        ////System.out.println("formConfig :" + formConfig);
        try {
            activityName = formObject.getWFActivityName();
            engineName = formConfig.getConfigElement("EngineName");
            sessionId = formConfig.getConfigElement("DMSSessionId");
            folderId = formConfig.getConfigElement("FolderId");
            serverUrl = formConfig.getConfigElement("ServletPath");
            //ServletUrl = serverUrl + "/servlet/ExternalServlet";
            processInstanceId = formConfig.getConfigElement("ProcessInstanceId");
            workItemId = formConfig.getConfigElement("WorkitemId");
            userName = formConfig.getConfigElement("UserName");
            processDefId = formConfig.getConfigElement("ProcessDefId");
            //System.out.println("ProcessInstanceId===== " + processInstanceId);
            //System.out.println("Activityname=====" + activityName);
            //System.out.println("CabinetName====" + engineName);
            //System.out.println("sessionId====" + sessionId);
            //System.out.println("Username====" + userName);
            //System.out.println("workItemId====" + workItemId);

//            ************************************************************************************
        } catch (Exception e) {
            //System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
        }
    }

    @Override
    public void formPopulated(FormEvent arg0) {
        //System.out.println("-- void formPopulated(FormEvent arg0) {
        long start = System.currentTimeMillis();
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        objgateentry = new gateEntry();
        objstore = new store();
        objquality = new quality();
        objaccounts = new accounts();
        objfinance = new finance();
        objservice = new service();
        objaudit = new audit();
        objmatching = new Matching();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        IRepeater repeaterControlF6 = formObject.getRepeaterControl("Frame6");
        IRepeater repeaterControlF5 = formObject.getRepeaterControl("Frame5");
        Date date = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        fiscalYear = cal.get(Calendar.YEAR);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
        today = formatter.format(date);
        today1 = formatter1.format(date);
        String comboID = null, commentId = null;
        String Amount = formObject.getNGValue("InvoiceAmount");
        Amount = Amount.replaceAll(",", "");
        formObject.setNGValue("InvoiceAmount", Amount);
        formObject.setNGValue("InvoiceNo", formObject.getNGValue("InvoiceNo").toUpperCase());
        int rowcount12 = repeaterControl.getRepeaterRowCount();

        String po_number = formObject.getNGValue("PurchaseOrderNo");

        //++++++++++++++++++++++++ To SET PROFIT CENTER+++++++++++++++++++++++++++++++++++++++++++//    

        String Query_prft = "select distinct PROFIT_CTR, PLANT from complex_orient_po_item "
                + "where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                + " and PROFIT_CTR is not null and plant is not null";
        //System.out.println("Query for profit center : " + Query);
        List<List<String>> result_prft = formObject.getDataFromDataSource(Query_prft);
        if (result_prft.size() > 0) {
            //System.out.println("Profit Center value : " + result.get(0).get(0));
            formObject.setNGValue("ProfitCentre", result_prft.get(0).get(0));
            formObject.setNGValue("Plant", result_prft.get(0).get(1));
        }
//++++++++++++++++++++++++ To SET PROFIT CENTER+++++++++++++++++++++++++++++++++++++++++++//

        if (po_number.length() > 10) {
            ////System.out.println("Update Po number" + po_number.substring(0, 10));
            formObject.setNGValue("PurchaseOrderNo", po_number.substring(0, 10));
        }

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
        ////System.out.println("Set headers");
        repeaterControl.setRepeaterLinkColor(Color.white);
        formObject.setSheetVisible("Tab1", 12, false);// Rahul 15 January 2021
        formObject.setSheetVisible("Tab1", 13, false);// Rahul 15 January 2021
        formObject.setSheetVisible("Tab2", 2, false);
        String val = formObject.getNGValue("pr_number");
        formObject.setNGValue("Text108", val);
        formObject.setEnabled("freight_value", false);
        formObject.setEnabled("manual_inv_park", true);
        formObject.setEnabled("tsc_flag", false);
        try {

//            objGeneral.setPOHistory(processInstanceId); // Set PO History
            formObject.setSheetVisible("Tab2", 3, false);
            // Activity wise form validation---------------*Start*------------
            if (activityName.equalsIgnoreCase("Manual_Introduction")) {
                intro.setIntroFormValidation();
            }
            //---------------*Rahul 17-09-2020 Start*------------
            if (activityName.equalsIgnoreCase("ParkingException")) {


                formObject.setSheetVisible("Tab2", 2, false);
                formObject.setSheetVisible("Tab2", 3, false);
                formObject.setSheetVisible("Tab2", 4, false);
                formObject.setSheetVisible("Tab2", 5, false);

                formObject.setSheetEnable("Tab1", 0, false);
                formObject.setSheetEnable("Tab1", 1, false);
                formObject.setSheetEnable("Tab1", 2, false);

                formObject.setSheetVisible("Tab1", 3, false);
                formObject.setSheetVisible("Tab1", 4, false);
                formObject.setSheetVisible("Tab1", 5, false);
                formObject.setSheetVisible("Tab1", 6, false);
                formObject.setSheetVisible("Tab1", 7, false);

                formObject.setSheetVisible("Tab1", 8, false);
                formObject.setSheetVisible("Tab1", 9, false);
                formObject.setSheetVisible("Tab1", 10, false);
                formObject.setSheetVisible("Tab1", 11, false);
                formObject.setSheetVisible("Tab1", 12, true);
                formObject.setSheetVisible("Tab1", 13, false);

            }

            if (activityName.equalsIgnoreCase("PostingException")) {


                formObject.setSheetVisible("Tab2", 2, false);
                formObject.setSheetVisible("Tab2", 3, false);
                formObject.setSheetVisible("Tab2", 4, false);
                formObject.setSheetVisible("Tab2", 5, false);

                formObject.setSheetEnable("Tab1", 0, false);
                formObject.setSheetEnable("Tab1", 1, false);
                formObject.setSheetEnable("Tab1", 2, false);

                formObject.setSheetVisible("Tab1", 3, false);
                formObject.setSheetVisible("Tab1", 4, false);
                formObject.setSheetVisible("Tab1", 5, false);
                formObject.setSheetVisible("Tab1", 6, false);
                formObject.setSheetVisible("Tab1", 7, false);

                formObject.setSheetVisible("Tab1", 8, false);
                formObject.setSheetVisible("Tab1", 9, false);
                formObject.setSheetVisible("Tab1", 10, false);
                formObject.setSheetVisible("Tab1", 11, false);
                formObject.setSheetVisible("Tab1", 12, false);
                formObject.setSheetVisible("Tab1", 13, true);

            }
            // -------*Rahul 17-09-2020 End*------------







            if (activityName.equalsIgnoreCase("Gate Entry")) {
                RepeaterController rc = new RepeaterController();
                rc.setInvoiceLine();
                objgateentry.setGEFormValidation();
                Query = "select Count(*) from ext_orientAP e , WFINSTRUMENTTABLE w where w.ProcessInstanceID = e.processid "
                        + "and PurchaseOrderNo ='" + formObject.getNGValue("PurchaseOrderNo") + "' "
                        + "AND InvoiceNo='" + formObject.getNGValue("InvoiceNo") + "' and w.ActivityName <> 'Discard'";
                //System.out.println("Duplicate flag query: " + Query);
                List<List<String>> countarray = formObject.getDataFromDataSource(Query);
                String count = countarray.get(0).get(0);
                ////System.out.println("PO and invoice in //System count: " + count);
                if (count.equalsIgnoreCase("1")) {
                    formObject.setNGValue("duplicateCheck", "N");
                } else {
                    formObject.setNGValue("duplicateCheck", "Y");
                }
                formObject.setEnabled("freight_value", true);
                formObject.setEnabled("tsc_flag", true);
            }

            if (activityName.equalsIgnoreCase("Store")
                    || activityName.equalsIgnoreCase("MIGO Cancelled")) {
                objstore.setStoreFormValidation();
                objGeneral.setPOHistory(processInstanceId);
            }

            if (activityName.equalsIgnoreCase("Store Hold")) {
                objstore.setStoreHoldFormValidation();
            }

            if (activityName.equalsIgnoreCase("Quality")) {
                objquality.setQualityFormValidation();
            }

            if (activityName.equalsIgnoreCase("Quality_Hold")) {
                objquality.setQualityHoldFormValidation();
            }

            if (activityName.equalsIgnoreCase("Accounts")) {
                objaccounts.setAccoutsFormValidation();
            }

            if (activityName.equalsIgnoreCase("Finance")) {
                objfinance.setFinanceFormValidation();
                formObject.setSheetVisible("Tab1", 9, false);
                formObject.setSheetVisible("Tab1", 10, false);
                if ("ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))) {
                    formObject.setSheetVisible("Tab2", 1, false);
                    formObject.setSheetEnable("Tab2", 4, false);
                }
            }


            if (activityName.equalsIgnoreCase("Audit")) {
                objaudit.setAuditFormValidation();
            }

            if (activityName.equalsIgnoreCase("Exception")) {
                formObject.setEnabled("Frame3", false);
                formObject.setVisible("Button1", false);
                formObject.setVisible("movement_type", false);
                formObject.setVisible("Combo10", false);
                formObject.setVisible("Label53", false);
                formObject.setVisible("Label65", false);
                formObject.setVisible("Button2", false);
                formObject.setVisible("Label65", false);
                formObject.setVisible("Button2", false);
                formObject.setSheetVisible("Tab1", 0, true);
                formObject.setSheetVisible("Tab1", 1, false);
                formObject.setSheetVisible("Tab1", 2, false);
                formObject.setSheetVisible("Tab1", 3, false);
                formObject.setSheetVisible("Tab1", 4, false);
                formObject.setSheetVisible("Tab1", 5, false);
                formObject.setSheetVisible("Tab1", 6, false);
                formObject.setSheetVisible("Tab1", 7, false);
                formObject.setSheetVisible("Tab1", 8, false);
                formObject.setSheetVisible("Tab1", 9, false);
                formObject.setSheetVisible("Tab1", 10, false);
                formObject.setSheetVisible("Tab2", 5, false);
                formObject.setSheetEnable("Tab1", 0, false);
                //  formObject.setEnabled("APForm12", false);

            }

            formObject.setSheetVisible("Tab2", 4, false);
            if (activityName.equalsIgnoreCase("Gate Entry")
                    || activityName.equalsIgnoreCase("Store")) {
                objmatching.updateMatch("Total Amount");
                objmatching.updateMatch("Price Matching");
            }
            objmatching.updateMatch("GSTN");
            objmatching.updateMatch("ORIENT GSTN");
            objmatching.updateMatch("PAN");
            objmatching.updateMatch("PAN_INV");

            if (formObject.getNGValue("mdoc103").equalsIgnoreCase("")) {
                ////System.out.println("Inside empty mdoc103");
            } else {
                formObject.setNGValue("gate_decision", "Processed");
            }

            // Activity wise form validation---------------*End*------------
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());

        }
        long end = System.currentTimeMillis();
        long elapsedTime = end - start;

        System.out.println("Start -:: " + start);
        System.out.println("End -:: " + end);
        System.out.println("Elapsed -:: " + elapsedTime);
    }

    @Override
    public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        //System.out.print("-------------------save form completed---------");
    }

    @Override
    public void saveFormStarted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        //System.out.print("-------------------Save form  started---------");

    }

    @Override
    public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

    }

    @Override
    public void submitFormStarted(FormEvent arg0) {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        //System.out.println("***submmit form started*****");
        migocheck = new MigoCheck();
//*********************RAHUL************************//

        if ("Store".equalsIgnoreCase(activityName)) {


            String invoice_no = formObject.getNGValue("InvoiceNo");

            List<String> mat_doc_list = new LinkedList<>();

            String matdoc_reversenot = "";

            Query = "Select distinct ref_doc from complex_orient_poitem_history where pinstanceid ='" + processInstanceId + "' "
                    + "and move_type ='106' ";
            System.out.println("Query is :::" + Query);
            resultarray1 = formObject.getDataFromDataSource(Query);
            if (resultarray1.size() > 0) {

                for (int i = 0; i < resultarray1.size(); i++) {
                    mat_doc_list.add(resultarray1.get(i).get(0));
                }
            }


            Query = "Select distinct mat_doc from complex_orient_poitem_history where pinstanceid ='" + processInstanceId + "' "
                    + "and move_type ='105' order by mat_doc asc";
            System.out.println("Query is :::" + Query);
            resultarray1 = formObject.getDataFromDataSource(Query);
            if (resultarray1.size() > 0) {

                for (int i = 0; i < resultarray1.size(); i++) {
                    if (!mat_doc_list.contains(resultarray1.get(i).get(0))) {
                        matdoc_reversenot = resultarray1.get(i).get(0);
                    }
                }
            }





            //*****************************7-06-2021---Start******************//

            String po_item = "", purchaseorder = "", quantity = "", po_item_his = "", purchaseorder_his = "", quantity_his = "";

            Query = "Select po_item,purchaseorder,quantity from complex_orient_invoice_item where pinstanceid ='" + processInstanceId + "'";
            resultarray = formObject.getDataFromDataSource(Query);
            System.out.println("Query is :::" + Query);

            if (resultarray.size() > 0) {

                for (int j = 0; j < resultarray.size(); j++) {
                    po_item = resultarray.get(j).get(0);
                    purchaseorder = resultarray.get(j).get(1);
                    quantity = resultarray.get(j).get(2);

                    System.out.println("po_item :- " + po_item);
                    System.out.println("purchaseorder :- " + purchaseorder);
                    System.out.println("quantity :- " + quantity);

                    Query = "Select quantity from complex_orient_poitem_history where pinstanceid ='" + processInstanceId + "' "
                            + "and move_type ='105' and ref_doc_no ='" + invoice_no + "' and mat_doc = '" + matdoc_reversenot + "' and po_item='" + po_item + "' and purchaseorder='" + purchaseorder + "'";
                    result = formObject.getDataFromDataSource(Query);
                    System.out.println("Query is :::" + Query);
                    String store_decision = (String) formObject.getNGValue("store_decision");
                    System.out.println("Store Decision is :::" + store_decision);
                    if (!"Discard".equalsIgnoreCase(store_decision)) {
                        if (result.size() > 0) {
                            quantity_his = result.get(0).get(0);
                            System.out.println("quantity_his :- " + quantity_his);
                            System.out.println(Double.parseDouble(quantity));
                            System.out.println(Double.parseDouble(quantity_his));
                            if (Double.parseDouble(quantity) != Double.parseDouble(quantity_his)) {
                                throw new ValidatorException(new FacesMessage("Quantity Mismatch...", ""));
                            }
                        } else {
                            throw new ValidatorException(new FacesMessage("PO_Item Mismatch...", ""));
                        }
                    }
                }
            }



            //*****************************7-06-2021---END******************// 

        }

        if ("ParkingException".equalsIgnoreCase(activityName)) {
            String ParkingException_decision = (String) formObject.getNGValue("ParkingException_decision");
            if (ParkingException_decision.equalsIgnoreCase("Send for posting")) {

                String purchaseorderno = (String) formObject.getNGValue("PurchaseOrderNo");
                String outputXML = objpo_getdetail.getPO_GETDETAIL_HISTORY(purchaseorderno);

                System.out.println("OUTPUT OF getPO_GETDETAIL_HISTORY :: " + outputXML);
                xmlParser.setInputXML(outputXML);
                WFXmlResponse objXmlResponse1 = new WFXmlResponse(outputXML);
                String inv_park = "";

                if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {


                    for (WFXmlList objList1 = objXmlResponse1.createList("TableParameters", "PO_HISTORY"); objList1.hasMoreElements(); objList1.skip()) {
                        inv_park = objList1.getVal("BELNR");
                        String parkingStatus = objList1.getVal("BEWTP");
                        System.out.println("Parkingstatus :::" + parkingStatus);
                        System.out.println("inv_park :::" + inv_park);
                        if (parkingStatus.equalsIgnoreCase("T") || parkingStatus.equalsIgnoreCase("Q")) {
                            formObject.setNGValue("inv_park", inv_park);

                        }

                    }

                }
                if (!"".equalsIgnoreCase((String) formObject.getNGValue("manual_inv_park"))) {
                    formObject.RaiseEvent("WFSave");
                    String manual_inv_park = (String) formObject.getNGValue("manual_inv_park");
                    formObject.setNGValue("inv_park", manual_inv_park);

                }
                String inv_park_status = (String) formObject.getNGValue("inv_park");
                if (inv_park_status.equalsIgnoreCase("")) {

                    throw new ValidatorException(new FacesMessage("Please First Park the dcoument before send for posting", ""));

                }
                Query = "update ext_orientAP set inv_park_sap = '" + inv_park + "' where processid ='" + processInstanceId + "'";
                formObject.saveDataIntoDataSource(Query);
                formObject.RaiseEvent("WFSave");
            }

        }



        //*********************RAHUL************************// 




//code for DMS query**********************************************************************
        if ("Manual_Introduction".equalsIgnoreCase(activityName)) {
            Query = "select documentindex from pdbdocumentcontent "
                    + "where parentfolderindex"
                    + "= (select var_rec_1 from wfinstrumenttable "
                    + "where processinstanceid='" + processInstanceId + "')";
            //System.out.println("Query for document " + Query);
            resultarray = formObject.getDataFromDataSource(Query);
            if (resultarray.size() <= 0) {
                throw new ValidatorException(new FacesMessage("Please attach Invoice Document", ""));
            }

            String val = formObject.getNGValue("po_cat");
            String type = formObject.getNGValue("PurchaseOrderNo");
            if (val.equalsIgnoreCase("Service") && type.equalsIgnoreCase("")) {
                throw new ValidatorException(new FacesMessage("Please enter the Purchase Order", ""));
            }
        }

        if ("Gate Entry".equalsIgnoreCase(activityName)) {
            Query = "select documentindex from pdbdocumentcontent "
                    + "where parentfolderindex"
                    + "= (select var_rec_1 from wfinstrumenttable "
                    + "where processinstanceid='" + processInstanceId + "')";
            //System.out.println("Query for document " + Query);
            resultarray = formObject.getDataFromDataSource(Query);
            if (resultarray.size() <= 0) {
                throw new ValidatorException(new FacesMessage("Please attach Invoice Document ", ""));
            }

            int rowcount = repeaterControl.getRepeaterRowCount();
            if (rowcount == 0) {
                throw new ValidatorException(new FacesMessage("Please enter invoice line in invoice tab", ""));
            }

            String gateDecision = formObject.getNGValue("gate_decision");
            ////System.out.println("Gate decision : " + gateDecision);
            if (!gateDecision.equalsIgnoreCase("Exception")
                    || !gateDecision.equalsIgnoreCase("Discard")) {
                int migoStatus;
                if ("9000".equalsIgnoreCase(formObject.getNGValue("pur_org"))) {
                    migoStatus = migocheck.getMigoStatus("101");
                } else {
                    int migoStatus101 = migocheck.getMigoStatus("101");
                    int migoStatus103 = migocheck.getMigoStatus("103");
                    ////System.out.println(migoStatus101 + " - - " + migoStatus103);
                    migoStatus = migoStatus101 + migoStatus103;
                }
                ////System.out.println("Migo Status : " + migoStatus);

                String mdocID = "mdoc103";
                ////System.out.println("mdoc form ID : " + mdocID);
                if (migoStatus == 0 && formObject.getNGValue(mdocID).equalsIgnoreCase("") && !gateDecision.equalsIgnoreCase("Discard")) {
                    throw new ValidatorException(new FacesMessage("Please perform GRN for the invoice", ""));
                }
            }
//            else {
//                formObject.setNGValue("gate_decision", "Exception");
//            }
            formObject.setNGValue("store_decision", "Pending");

            // ***********************10/feb/2021************************8
            String InvoiceAmount = formObject.getNGValue("InvoiceAmount");
            String total_inv = formObject.getNGValue("total_inv");
            System.out.println("InvoiceAmount : " + InvoiceAmount + " total_inv : " + total_inv);
            float inv_amount = Float.valueOf(InvoiceAmount);
            float total_inv1 = Float.valueOf(total_inv);

            float diff = inv_amount - total_inv1;

            System.out.println("InvoiceAmount : " + inv_amount + " total_inv1 : " + total_inv1);
            if ((diff > 10) || (diff < -10)) {
                throw new ValidatorException(new FacesMessage("Amount Mismatch !!!", ""));
            }
            // ***********************10/feb/2021************************8

        }
        System.out.println("Befre store");
        System.out.println("Store decision : " + formObject.getNGValue("store_decision"));
        if ("Store".equalsIgnoreCase(activityName)
                && !formObject.getNGValue("store_decision").equalsIgnoreCase("Discard")) {
            System.out.println("Inside Store");
            int migoStatus = migocheck.getMigoStatus105();
            //  System.out.println("Migo status : " + migoStatus);
            if (migoStatus == 0 && formObject.getNGValue("mdoc105").equalsIgnoreCase("")
                    || (formObject.getNGValue("mdoc105").equalsIgnoreCase("")
                    && (formObject.getNGValue("store_desicion").equalsIgnoreCase("Pending")
                    || formObject.getNGValue("store_desicion").equalsIgnoreCase("Migo Reversed")))) {
                throw new ValidatorException(new FacesMessage("Please perform MIGO-105 to proceed further", ""));
            } else {
                //    System.out.println("Inside else");
                String Item = formObject.getNGValue("mdoc105");
                System.out.println("Mdoc 105 quality " + Item);
                int rowcount = repeaterControl.getRepeaterRowCount();
                String qa_required = objGeneral.getQACheck(Item);
                formObject.setNGValue("qa_required", qa_required);
                int rowcount11 = repeaterControl.getRepeaterRowCount();
                for (int i = 0; i < rowcount11; i++) {
                    repeaterControl.setValue(i, "q_Orient_invoice_item_qa_required", qa_required);
                }
            }
        }

        if ("Store hold".equalsIgnoreCase(activityName)) {
            String storedes = "";

            storedes = formObject.getNGValue("storehold_decsion");
            if (storedes.equalsIgnoreCase("Debit To Transporter")) {
                if ("".equalsIgnoreCase(formObject.getNGValue("debit_to_transport"))) {
                    throw new ValidatorException(new FacesMessage("Please Enter value for Debit To Transporter", ""));

                } else if (storedes.equalsIgnoreCase("Claim Insurance")) {
                    if ("".equalsIgnoreCase(formObject.getNGValue("claiminsurance"))) {
                        throw new ValidatorException(new FacesMessage("Please Enter value for Claim Insurance", ""));
                    }
                } else if (storedes.equalsIgnoreCase("Replacement")) {
                    if ("".equalsIgnoreCase(formObject.getNGValue("replacement"))) {
                        throw new ValidatorException(new FacesMessage("Please Enter value for Sales Bill", ""));
                    }
                }
            }
        }

        if ("ZSER".equalsIgnoreCase(formObject.getNGValue("po_type"))
                && !(activityName.equalsIgnoreCase("Accounts")
                || activityName.equalsIgnoreCase("Finance")
                || activityName.equalsIgnoreCase("Audit"))) {
            String assignedTo = "", email = "", mailbody = "", purc_group, inv_amount, amount_status, service_status;
            inv_amount = formObject.getNGValue("InvoiceAmount");
            purc_group = formObject.getNGValue("pur_group");
            service_status = formObject.getNGValue("service_status");
            ////System.out.println("inv_amount" + inv_amount + "purc_group" + purc_group);
            amount_status = objGeneral.setUserforDOA(inv_amount, purc_group);
            String route_to = formObject.getNGValue("route_to");
            if (amount_status.equalsIgnoreCase("")) {
                assignedTo = "NA";
            } else {
                if (activityName.equalsIgnoreCase("Service Entry")) {

                    if (formObject.getItemCount("List1") > 0) {
                        throw new ValidatorException(new FacesMessage("Please clear all exceptions to proceed further", ""));
                    } else {
                        Query = "select l1_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                        ////System.out.println("Query to assignedUser  ServiceEntry: " + Query);
                    }
                }

                if (activityName.equalsIgnoreCase("Level1")) {
                    if ("Approve".equalsIgnoreCase(service_status)) {
                        Query = "select l2_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status)) {
                        Query = "";
                    }
                    ////System.out.println("Query to assignedUser  L1: " + Query);
                }
                if (activityName.equalsIgnoreCase("Level2")) {
                    if ("Approve".equalsIgnoreCase(service_status)) {
                        Query = "select l3_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status)) {
                        Query = "select l1_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    ////System.out.println("Query to assignedUser  L2: " + Query);
                }
                if (activityName.equalsIgnoreCase("Level3")) {
                    if ("Approve".equalsIgnoreCase(service_status)) {
                        Query = "select l4_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status) && "Level1".equalsIgnoreCase(route_to)) {
                        Query = "select l1_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status) && "Level2".equalsIgnoreCase(route_to)) {
                        Query = "select l2_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    ////System.out.println("Query to assignedUser  L3: " + Query);
                }

                if (activityName.equalsIgnoreCase("Level4")) {
                    if ("Approve".equalsIgnoreCase(service_status)) {
                        Query = "";
                    }
                    if ("Reject".equalsIgnoreCase(service_status) && "Level1".equalsIgnoreCase(route_to)) {
                        Query = "select l1_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status) && "Level2".equalsIgnoreCase(route_to)) {
                        Query = "select l2_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    if ("Reject".equalsIgnoreCase(service_status) && "Level3".equalsIgnoreCase(route_to)) {
                        Query = "select l3_userid from complex_orient_doa_usermapping where purch_group = '" + purc_group + "' and "
                                + "values_doa = '" + amount_status + "'";
                    }
                    ////System.out.println("Query to assignedUser  L4: " + Query);
                }
                if (!"".equalsIgnoreCase(Query)) {
                    result = formObject.getDataFromDataSource(Query);
                    assignedTo = result.get(0).get(0);
                    ////System.out.println("Assigned User : " + assignedTo);
                }
                if ("".equalsIgnoreCase(assignedTo) || amount_status.equalsIgnoreCase("")) {
                    assignedTo = "NA";
                }
            }

            if ("Service Entry".equalsIgnoreCase(activityName)) {

                Query = "Delete from complex_orient_service_itemsel where pinstanceid = '" + processInstanceId + "'";
                ////System.out.println("Delete Query : " + Query);
                formObject.saveDataIntoDataSource(Query);

                IRepeater repeaterControlF5 = formObject.getRepeaterControl("Frame5");
                int rowcount = repeaterControlF5.getRepeaterRowCount();
                ////System.out.println("Row Count : " + rowcount);
                for (int i = 0; i < rowcount; i++) {
                    Query = "INSERT INTO dbo.complex_orient_service_itemsel"
                            + "(pinstanceid"
                            + ",short_text"
                            + ",quantity"
                            + ",price_per_unit"
                            + ",line_no"
                            + ",select_flag"
                            + ",gross_price"
                            + ",hsn_sac)"
                            + "VALUES"
                            + "('" + processInstanceId + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text126") + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text127") + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text128") + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text125") + "',"
                            + " '" + repeaterControlF5.getValue(i, "CheckBox3") + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text129") + "',"
                            + " '" + repeaterControlF5.getValue(i, "Text130") + "')";

                    ////System.out.println("Insert Query : " + Query);
                    formObject.saveDataIntoDataSource(Query);
                }
            }

            if ("Service".equalsIgnoreCase(activityName)) {
                ////System.out.println("Inside Service Entry sheet Validation");
                assignedTo = "NA";
                Query = "select PreviousStage from WFINSTRUMENTTABLE where ProcessInstanceID = '" + processInstanceId + "'";
                ////System.out.println("Query : " + Query);
                resultarray = formObject.getDataFromDataSource(Query);
                String prev_step = resultarray.get(0).get(0);
                if (prev_step.equalsIgnoreCase("Accounts")
                        && formObject.getNGValue("accounts_decision").equalsIgnoreCase("Cancel Entry Sheet")) {
                    ////System.out.println("Inside Service Entry sheet Validation seconf if condition");
                    String inputXml = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_GETDETAIL");
                    inputXml = inputXml + "<Parameters>"
                            + "<ImportParameters>"
                            + "<ENTRYSHEET>" + formObject.getNGValue("entrysheet_no") + "</ENTRYSHEET>"
                            + "</ImportParameters>"
                            + "</Parameters>"
                            + "</WFSAPInvokeFunction_Input>";
                    String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId + "_BAPI_ENTRYSHEET_GETDETAIL");
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    String del_ind = "";

                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        ////System.out.println("Inside service if");
                        del_ind = xmlParser.getValueOf("DELETE_IND");
                        ////System.out.println("Value of Delete ind " + del_ind);
                    }
                    ////System.out.println("Delete index : " + del_ind);

                    if (!"X".equalsIgnoreCase(del_ind)) {
                        throw new ValidatorException(new FacesMessage("Please Delete the Entrysheet to move further", ""));
                    }
                }
            } //entrysheet_no
            formObject.setNGValue("assignedTo", assignedTo);

            if (!"".equalsIgnoreCase(assignedTo)
                    || !assignedTo.equalsIgnoreCase(null)
                    || !assignedTo.equalsIgnoreCase("null")
                    || !assignedTo.equalsIgnoreCase("NA")) {
                email = objGeneral.getEmailId(assignedTo);
                mailbody = objGeneral.getEmailBody(assignedTo, processInstanceId);
                formObject.setNGValue("email", email);
                formObject.setNGValue("mailbody", mailbody);
            }

            String xmlnew = "";
            xmlnew = xmlnew + "<ListItem><SubItem>" + activityName
                    + "</SubItem><SubItem>" + userName
                    + "</SubItem><SubItem>" + today1
                    + "</SubItem><SubItem>" + service_status
                    + "</SubItem><SubItem>" + formObject.getNGValue("Text29")
                    + "</SubItem></ListItem>";
            // // //System.out.println("xml2 is" + xmlnew);
            ////System.out.println("q_serviceapproval XML : " + xmlnew);
            formObject.NGAddListItem("q_serviceapproval", xmlnew);
            formObject.clear("Text29");
//            formObject.setNGValue("service_status", "Approve");

        }

        ////System.out.println("before set transaction");
        objGeneral.setTransactionLogs(); //set transsaction logs
        ////System.out.println("After set transaction");
//****************************************************************************************
//for division from BAPI_MATERIAL_GET_DETAIL***********************************************
        if ("Finance".equalsIgnoreCase(activityName)) {
            if (formObject.getNGValue("finance_decision").equalsIgnoreCase("Posted")) {
                String post_flag = "N";
                Query = "select ponumber from complex_orient_multiplepo where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'"
                        + " union "
                        + "select PurchaseOrderNo from ext_orientAP where processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' ";
                ////System.out.println("Po query " + Query);
                resultarray = formObject.getDataFromDataSource(Query);
                for (int i = 0; i < resultarray.size() || post_flag.equalsIgnoreCase("N"); i++) {
                    String outputXml = objpo_getdetail.getPO_GETDETAIL_HISTORY(resultarray.get(i).get(0));
                    xmlParser.setInputXML(outputXml);
                    WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
                    // ////System.out.println("After xml response outputxml : " + outputXml);
                    if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                        ////System.out.println("Inside Main code zero");
                        for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_HISTORY"); objList.hasMoreElements(); objList.skip()) {
                            ////System.out.println("Inside po history for loop");
                            String HIST_TYPE = objList.getVal("BEWTP");
                            String MAT_DOC = objList.getVal("BELNR");
                            ////System.out.println("Hist type : " + HIST_TYPE);
                            ////System.out.println("Mat Doc : " + MAT_DOC);
                            if (MAT_DOC.equalsIgnoreCase(formObject.getNGValue("inv_park"))
                                    && "Q".equalsIgnoreCase(HIST_TYPE)) {
                                post_flag = "Y";
                                break;
                            }
                        }
                    }
                }
                if (post_flag.equalsIgnoreCase("Y")) {
                    formObject.setNGValue("inv_post", formObject.getNGValue("inv_park"));
                } else {
                    throw new ValidatorException(new FacesMessage("Please perform the posting to proceed further", ""));
                }
            } else {
                formObject.setNGValue("inv_park", "");
            }

//        String inputXml = objGeneral.sapInvokeXML("BAPI_MATERIAL_GET_DETAIL");
//        inputXml = inputXml + "<Parameters>"
//                + "<ImportParameters>"
//                + "<MATERIAL>" + formObject.getNGValue("q_orient_po_item", 0, 0) + "</MATERIAL>"
//                + "</ImportParameters>"
//                + "</Parameters>"
//                + "</WFSAPInvokeFunction_Input>";
//        String outputXml = objGeneral.executeWithCallBroker(inputXml);
//        xmlParser.setInputXML(outputXml);
//        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
//        //System.out.println("After xml response outputxml : " + outputXml);
//        String DIVISION = "";
//
//        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
//            DIVISION = xmlParser.getValueOf("DIVISION");
//
//        }
//        //System.out.println("Division : " + DIVISION);
        }

//*******************************************************************************************
    }

    @Override
    public void initialize() {
        //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encrypt(String string) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return string;
    }

    @Override
    public String decrypt(String string) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return string;
    }
}
