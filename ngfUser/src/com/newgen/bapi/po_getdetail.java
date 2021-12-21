/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.bapi;

import com.newgen.common.General;
import com.newgen.common.WFXmlList;
import com.newgen.common.WFXmlResponse;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

public class po_getdetail implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "", HSN_TAB = "";
    String processInstanceId_po_getdetail;

    public String getPO_GETDETAIL(String PurchaseOrderNo) {
        System.out.println("Inside po_getdetail");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        processInstanceId_po_getdetail = formConfig.getConfigElement("ProcessInstanceId");
        objGeneral = new General();
        String inputXml = objGeneral.sapInvokeXML("BAPI_PO_GETDETAIL");
        inputXml = inputXml + "<Parameters>"
                + "<ImportParameters>"
                + "<PURCHASEORDER>"
                + PurchaseOrderNo
                + "</PURCHASEORDER>"
                + "<ITEMS>X</ITEMS>"
                + "</ImportParameters>"
                + "</Parameters></WFSAPInvokeFunction_Input>";

        String outputXml = objGeneral.executeWithCallBroker(inputXml,processInstanceId_po_getdetail+"_BAPI_PO_GETDETAIL");

        return outputXml;
    }

    public String getPO_GETDETAIL_HISTORY(String ponumber) {
        //System.out.println("Inside po_getdetail_history" + ponumber);
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        processInstanceId_po_getdetail = formConfig.getConfigElement("ProcessInstanceId");
        objGeneral = new General();
        String inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_PO_HIST");
        inputXml = inputXml + "<Parameters>"
                + "<ImportParameters>"
                + "<PO_NUM>" + ponumber + "</PO_NUM>"
                + "<INV_NUM>" + formObject.getNGValue("InvoiceNo") + "</INV_NUM>"
                + "</ImportParameters>"
                + "</Parameters>"
                + "</WFSAPInvokeFunction_Input>";
        System.out.println("Input ZBAPI_AP_PO_HIST :::- "+inputXml );
        String outputXml = objGeneral.executeWithCallBroker(inputXml,processInstanceId_po_getdetail+"_ZBAPI_AP_AUTOMATION_PO_HIST");
        return outputXml;
    }

}
