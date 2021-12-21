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
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

/**
 *
 * @author Latitude 3460
 */
public class goodsmvt_cancel implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "", GOODSMVT_MATDOCITEM = "";

    public String getGoodsmvt_Cancel_Input(int fiscalYear , String mdoc_id) {
        //System.out.println("Inside goodsmvt_cancel");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        IRepeater repeaterControlF4 = formObject.getRepeaterControl("Frame4");
        objGeneral = new General();
        int repeater_rowcount = repeaterControl.getRepeaterRowCount();
        for (int i = 0; i < repeater_rowcount; i++) {
            //System.out.println("Select flag value true");
            GOODSMVT_MATDOCITEM = GOODSMVT_MATDOCITEM
                    + "<MATDOC_ITEM>" + repeaterControlF4.getValue(i, "Text99") + "</MATDOC_ITEM>";
        }

        inputXml = objGeneral.sapInvokeXML("BAPI_GOODSMVT_CANCEL");
        inputXml = inputXml + "<Parameters><ImportParameters>"
                + "<MATERIALDOCUMENT>" + formObject.getNGValue(mdoc_id) + "</MATERIALDOCUMENT>"
                + "<MATDOCUMENTYEAR>" + fiscalYear + "</MATDOCUMENTYEAR>"
                + "<GOODSMVT_PSTNG_DATE>" + convertNewgenDateToSapDate(formObject.getNGValue("postingDate")) + "</GOODSMVT_PSTNG_DATE>"
                + "</ImportParameters><TableParameters>"
                + "<GOODSMVT_MATDOCITEM>"
                + GOODSMVT_MATDOCITEM
                + "</GOODSMVT_MATDOCITEM>"
                + "</TableParameters></Parameters></WFSAPInvokeFunction_Input>";

        return inputXml;
    }

}
