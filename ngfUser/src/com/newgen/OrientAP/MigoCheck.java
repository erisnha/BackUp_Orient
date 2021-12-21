/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.OrientAP;

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
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Latitude 3460
 */
public class MigoCheck implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    int difference;
    String Query = "";
    String processInstanceId_migocheck;

    /*
     * public int getMigoStatus(String materialdoc) { formObject =
     * FormContext.getCurrentInstance().getFormReference(); formConfig =
     * FormContext.getCurrentInstance().getFormConfig(); objGeneral = new
     * General(); IRepeater repeaterControl =
     * formObject.getRepeaterControl("Frame3"); System.out.println("Inside Migo
     * check : fe" + materialdoc); int matcount = 0;
     *
     * Query = "select ponumber from complex_orient_multiplepo where pinstanceid
     * = '" + formConfig.getConfigElement("ProcessInstanceId") + "'" + " union "
     * + "select PurchaseOrderNo from ext_orientAP where processid = '" +
     * formConfig.getConfigElement("ProcessInstanceId") + "' ";
     * System.out.println("Po query " + Query); List<List<String>> resultarray =
     * formObject.getDataFromDataSource(Query); for (int j = 0; j <
     * resultarray.size(); j++) { String inputXml =
     * objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL"); inputXml = inputXml +
     * "<Parameters>" + "<ImportParameters>" + "<PURCHASEORDER>" +
     * resultarray.get(j).get(0) + "</PURCHASEORDER>" + "<HISTORY>X</HISTORY>" +
     * "</ImportParameters>" + "</Parameters>" + "</WFSAPInvokeFunction_Input>";
     * String outputXml = objGeneral.executeWithCallBroker(inputXml);
     * xmlParser.setInputXML(outputXml); WFXmlResponse objXmlResponse = new
     * WFXmlResponse(outputXml); String poitem = "", ref_doc_no = "", qunati =
     * "", MOVE_TYPE = ""; String poitemrepeat = "", ref_docrepeat = "",
     * qunatrepeat = ""; int rowcount = repeaterControl.getRepeaterRowCount();
     * if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) { for
     * (WFXmlList objList = objXmlResponse.createList("TableParameters",
     * "PO_ITEM_HISTORY"); objList.hasMoreElements(); objList.skip()) {
     *
     * poitem = objList.getVal("PO_ITEM"); ref_doc_no =
     * objList.getVal("REF_DOC_NO"); qunati = objList.getVal("QUANTITY");
     * MOVE_TYPE = objList.getVal("MOVE_TYPE"); difference = 5 -
     * poitem.length(); if (difference > 0) { poitem = String.format("%0" +
     * (difference) + "d%s", 0, poitem); } for (int i = 0; i < rowcount; i++) {
     * poitemrepeat = repeaterControl.getValue(i,
     * "q_Orient_invoice_item_po_item"); ref_docrepeat =
     * formObject.getNGValue("InvoiceNo"); qunatrepeat =
     * repeaterControl.getValue(i, "q_Orient_invoice_item_quantity"); difference
     * = 5 - poitemrepeat.length(); if (difference > 0) { poitemrepeat =
     * String.format("%0" + (difference) + "d%s", 0, poitemrepeat); } if
     * ("103".equalsIgnoreCase(MOVE_TYPE) || "101".equalsIgnoreCase(MOVE_TYPE))
     * { if (poitem.equalsIgnoreCase(poitemrepeat) &&
     * ref_doc_no.equalsIgnoreCase(ref_docrepeat) &&
     * MOVE_TYPE.equalsIgnoreCase(materialdoc)) { if
     * ("".equalsIgnoreCase(objList.getVal("REF_DOC"))) { } else { String
     * matdoc_no = objList.getVal("MAT_DOC"); System.out.println("Mat Doc : " +
     * matdoc_no); matcount++; formObject.setNGValue("mdoc103", matdoc_no);
     * formObject.setNGValue("movement_type", MOVE_TYPE); break; } } } else { if
     * (poitem.equalsIgnoreCase(poitemrepeat) &&
     * ref_doc_no.equalsIgnoreCase(ref_docrepeat) && Float.parseFloat(qunati) ==
     * Float.parseFloat(qunatrepeat) && MOVE_TYPE.equalsIgnoreCase(materialdoc))
     * { System.out.println("Inside else matching all condition"); if
     * ("".equalsIgnoreCase(objList.getVal("REF_DOC"))) { } else { matcount++;
     * break; } } } } } } } return matcount; }
     */
    public int getMigoStatus(String movementType) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        processInstanceId_migocheck = formConfig.getConfigElement("ProcessInstanceId");
        objGeneral = new General();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        // System.out.println("Inside Migo check :  fe" + movementType);
        int matcount = 0;

        Query = "select ponumber from complex_orient_multiplepo where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'"
                + " union "
                + "select PurchaseOrderNo from ext_orientAP where processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' ";
        //System.out.println("Po query " + Query);
        List<List<String>> resultarray = formObject.getDataFromDataSource(Query);
        for (int j = 0; j < resultarray.size(); j++) {
            String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_PO_HIST");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PO_NUM>" + resultarray.get(j).get(0) + "</PO_NUM>"
                    + "<INV_NUM>" + formObject.getNGValue("InvoiceNo") + "</INV_NUM>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId_migocheck + "_ZBAPI_AP_AUTOMATION_PO_HIST");
            xmlParser.setInputXML(outputXml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                //System.out.println("Inside main code");
                String mat_doc1 = "";
                String mnt_type = "104";
                Loop:
                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_HISTORY"); objList.hasMoreElements(); objList.skip()) {
                    //System.out.println("Inside for loop po history");
                    String ref_doc_no = objList.getVal("XBLNR");
                    String material = objList.getVal("MATNR");
                    String quantity = objList.getVal("MENGE");
                    String mat_doc = objList.getVal("BELNR");
                    String doc_date = objList.getVal("BLDAT");
                    String val_loccur = objList.getVal("WRBTR");
                    String move_type = objList.getVal("BWART");
                    String po_item = objList.getVal("EBELP");
                    String ref_doc_yr = objList.getVal("LFGJA");
                    String ref_doc_it = objList.getVal("LFPOS");
                    String tax_code = objList.getVal("MWSRZ");
                    String ref_doc = objList.getVal("LFBNR");

//                    if (movementType.equalsIgnoreCase(move_type)) {
//                        //System.out.println("Mat Doc : " + mat_doc);
//                        matcount++;
//                        formObject.setNGValue("mdoc103", mat_doc);
//                        formObject.setNGValue("gate_decision", "Processed");
//                        break;
//                    }


                    if (movementType.equalsIgnoreCase(move_type)) {
                        matcount++;
                        mat_doc1 = mat_doc;
                        continue Loop;
                    }

                    if (mnt_type.equalsIgnoreCase(move_type) && ref_doc.equalsIgnoreCase(mat_doc1)) {
                        matcount = 0;
                        mat_doc1 = "";
                        continue Loop;
                    }


                }
                if (!mat_doc1.equalsIgnoreCase("")) {

                    formObject.setNGValue("mdoc103", mat_doc1);
                    formObject.setNGValue("gate_decision", "Processed");
                }
            }
        }
        return matcount;
    }

    public int getMigoStatus105() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        System.out.println("Inside getmigostatus105");
        objGeneral = new General();
        int matcount = 0;
        Query = "select ponumber from complex_orient_multiplepo where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'"
                + " union "
                + "select PurchaseOrderNo from ext_orientAP where processid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' ";
        System.out.println("Po query " + Query);
        List<List<String>> resultarray = formObject.getDataFromDataSource(Query);
        for (int i = 0; i < resultarray.size(); i++) {
            String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_PO_HIST");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PO_NUM>" + resultarray.get(i).get(0) + "</PO_NUM>"
                    + "<INV_NUM>" + formObject.getNGValue("InvoiceNo") + "</INV_NUM>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            String outputXml = objGeneral.executeWithCallBroker(inputXml, processInstanceId_migocheck + "_ZBAPI_AP_AUTOMATION_PO_HIST");
            xmlParser.setInputXML(outputXml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                //System.out.println("Inside main code");
                for (WFXmlList objList = objXmlResponse.createList("TableParameters", "PO_HISTORY"); objList.hasMoreElements(); objList.skip()) {
                    //System.out.println("Inside for loop po history");
                    String ref_doc_no = objList.getVal("XBLNR");
                    String material = objList.getVal("MATNR");
                    String quantity = objList.getVal("MENGE");
                    String mat_doc = objList.getVal("BELNR");
                    String doc_date = objList.getVal("BLDAT");
                    String val_loccur = objList.getVal("WRBTR");
                    String move_type = objList.getVal("BWART");
                    String po_item = objList.getVal("EBELP");
                    String ref_doc_yr = objList.getVal("LFGJA");
                    String ref_doc_it = objList.getVal("LFPOS");
                    String tax_code = objList.getVal("MWSRZ");
                    String ref_doc = objList.getVal("LFBNR");

                    if ("105".equalsIgnoreCase(move_type)
                            && formObject.getNGValue("mdoc103").equalsIgnoreCase(ref_doc)) {
                        //System.out.println("Mat Doc : " + mat_doc);
                        matcount++;
                        formObject.setNGValue("mdoc105", mat_doc);
                        formObject.setNGValue("store_decision", "Migo Performed");
                        break;
                    }
                }
            }
        }
        return matcount;
    }
}
