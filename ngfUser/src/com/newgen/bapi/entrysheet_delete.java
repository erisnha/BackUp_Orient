/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.bapi;

import com.newgen.common.General;
import static com.newgen.common.General.convertNewgenDateToSapDate;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

/**
 *
 * @author Latitude 3460
 */
public class entrysheet_delete implements Serializable{

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "";

    public String getENTRYSHEET_RESET_RELEASE_Input() {
        //System.out.println("Inside ENTRYSHEET_RESET_RELEASE_Input");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();

        inputXml = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_RESET_RELEASE");
        inputXml = inputXml + "<Parameters><ImportParameters>"
                + "<ENTRYSHEET>" + formObject.getNGValue("entrysheet_no") + "</ENTRYSHEET>"
                + "<REL_CODE>01</REL_CODE>"
                + "</ImportParameters></Parameters></WFSAPInvokeFunction_Input>";
        //System.out.println("ENTRYSHEET_RESET_RELEASE_Input : " + inputXml);
        return inputXml;
    }

    public String getENTRYSHEET_DELETE_Input() {
        //System.out.println("Inside ENTRYSHEET_DELETE_Input");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();

        inputXml = objGeneral.sapInvokeXML("BAPI_ENTRYSHEET_DELETE");
        inputXml = inputXml + "<Parameters><ImportParameters>"
                + "<ENTRYSHEET>" + formObject.getNGValue("entrysheet_no") + "</ENTRYSHEET>"
                + "</ImportParameters></Parameters></WFSAPInvokeFunction_Input>";
        //System.out.println("ENTRYSHEET_DELETE_Input : " + inputXml);
        return inputXml;
    }
}
