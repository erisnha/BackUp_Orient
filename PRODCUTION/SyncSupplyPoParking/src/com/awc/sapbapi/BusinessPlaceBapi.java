/*
 * To change this template, choose Tools | Templatesand open the template in the editor.
 */
package com.awc.sapbapi;

import com.awc.methods.CommonFunctions;
import com.awc.methods.LogProcessing;
import com.awc.methods.WFXmlResponse;
import com.awc.methods.XMLParser;
import com.newgen.dmsapi.DMSCallBroker;

public class BusinessPlaceBapi {

    public String getBussinessPlace(CommonFunctions objCommonFunctions, String plant, XMLParser xmlParser) {
        System.out.println("Inside BusinessPlaceBapi");
        String busPlc_val = "";
        try {
            String inputXml1 = objCommonFunctions.sapInvokeXML("ZBAPI_AP_AUTOMATION_BUSPLACE");
            inputXml1 = inputXml1 + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PLANT>" + plant + "</PLANT>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";

            LogProcessing.xmllogs.info("INPUT ZBAPI_AP_AUTOMATION_BUSPLACE : " + inputXml1);
            String outputxml = DMSCallBroker.execute(inputXml1, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            LogProcessing.xmllogs.info("OUTPUT ZBAPI_AP_AUTOMATION_BUSPLACE : " + outputxml);
            xmlParser.setInputXML(outputxml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputxml);

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                busPlc_val = xmlParser.getValueOf("BUSS_PLACE");
            } else {
                busPlc_val = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogProcessing.errorlogs.info("Error in  BusinessPlace Bapi :- "+e.getMessage());
        }

        return busPlc_val;

    }
}
