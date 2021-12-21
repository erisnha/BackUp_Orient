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

public class taxcode_list implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "", inputXml = "", HSN_TAB = "";

    public String getTAX_CODE_LIST() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        inputXml = objGeneral.sapInvokeXML("ZBAPI_AP_AUTOMATION_TAX_CODE");
        inputXml = inputXml + "</WFSAPInvokeFunction_Input>";
//        inputXml = "<WFSAPInvokeFunction_Input><Option>WFSAPInvokeFunction</Option><SAPConnect>"
//                + "<SAPHostName>OEECCDV</SAPHostName><SAPInstance>00</SAPInstance><SAPClient>110</SAPClient>"
//                + "<SAPUserName>AWCDMS2</SAPUserName><SAPPassword>sapsupport@123</SAPPassword>"
//                + "<SAPLanguage>EN</SAPLanguage></SAPConnect>"
//                + "<SAPFunctionName>ZBAPI_AP_AUTOMATION_TAX_CODE</SAPFunctionName><Parameters>"
//                + "<ImportParameters><LANGU>EN</LANGU><PROCEDURE>YAXINN</PROCEDURE></ImportParameters>"
//                + "</Parameters></WFSAPInvokeFunction_Input>";
        
        return inputXml;
    }

}
