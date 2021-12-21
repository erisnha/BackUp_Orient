/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.bapi;

import com.newgen.common.General;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Latitude 3460
 */
public class asn_detail implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "", HSN_TAB = "";

    public String getASN_DETAIL(String asn_number) {
        System.out.println("Inside ASN_DETAIL");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_ASN_DETAIL");
        inputXml = inputXml + "<Parameters><ImportParameters>"
                + "<ASN_NO>" + asn_number + "</ASN_NO>"
                + "</ImportParameters></Parameters></WFSAPInvokeFunction_Input>";

        return inputXml;
    }

}
