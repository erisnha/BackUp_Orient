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

public class hsn_sac implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "", HSN_TAB = "";

    public String getPOSERV_HSN(String PCKG_NO) {
        //System.out.println("Inside goodsmvt_cancel");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        Query = "select PCKG_NO , EXT_LINE from complex_orient_entry_service "
                + "where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                + "and PCKG_NO = '" + PCKG_NO + "'";
        //System.out.println("Query : " + Query);
        List<List<String>> result = formObject.getDataFromDataSource(Query);
        for (int i = 0; i < result.size(); i++) {
            //System.out.println("Select flag value true");
            HSN_TAB = HSN_TAB
                    + "<HSN_TAB>"
                    + "<PCKG_NO>" + result.get(i).get(0) + "</PCKG_NO>"
                    + "<PO_LINE>" + result.get(i).get(1) + "</PO_LINE>"
                    + "</HSN_TAB>";
        }

        inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_POSERV_HSN");
        inputXml = inputXml + "<Parameters><TableParameters>"
                + HSN_TAB
                + "</TableParameters></Parameters></WFSAPInvokeFunction_Input>";

        return inputXml;
    }

}
