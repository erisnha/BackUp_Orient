package com.awc.sapBapi;

import com.awc.methods.*;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlResponse;

/**
 *
 * @author dms
 */
public class VendorRegion {

    DMSXmlResponse xmlResponse;
    CommonFunctions objCommonFunctions = null;
    XMLParser xmlParser = new XMLParser();

    public VendorRegion() {
    }

    public String getVendorRegion(String PONumber) {
        String vendor_region = "";
        try {
            objCommonFunctions = new CommonFunctions();
            String inputXml = objCommonFunctions.sapInvokeXML("BAPI_PO_GETDETAIL");
            inputXml = inputXml + "<Parameters>"
                    + "<ImportParameters>"
                    + "<PURCHASEORDER>" + PONumber + "</PURCHASEORDER>"
                    + "<HISTORY>X</HISTORY>"
                    + "<SERVICES>X</SERVICES>"
                    + "</ImportParameters>"
                    + "</Parameters>"
                    + "</WFSAPInvokeFunction_Input>";
            //System.out.println("Input xml : " + inputXml);

            LogProcessing.xmllogs.info(" INPUT of VendorRegion_BAPI_PO_GETDETAIL : " + inputXml);

            String outputXml = DMSCallBroker.execute(inputXml, CommonFunctions.serverIP, Short.parseShort("3333"), 0);
            xmlParser.setInputXML(outputXml);
            LogProcessing.xmllogs.info("OUTPUT VendorRegion_BAPI_PO_GETDETAIL : " + outputXml);
            WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);

            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
                vendor_region = xmlParser.getValueOf("REGION");
                // System.out.println("Vendor Region is :: "+vendor_region);
            } else {
                // System.out.println("Main Code is not 0");
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails In VendorRegion::- Main Code is not 0");

            }
            if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("400")) {
                LogProcessing.errorlogs.info("Error from Bapi_PO_GetDetails ::- Main Code is 400");
                LogProcessing.errorlogs.info(xmlParser.getValueOf("Description"));
                if (xmlParser.getValueOf("Description").equalsIgnoreCase("com.sap.conn.jco.JCoException: (106) JCO_ERROR_RESOURCE: Destination KEYOEPRDAPPN180001 does not exist")) {
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogProcessing.errorlogs.info("Exception from Bapi_PO_GetDetails In VendorRegion :: " + e.getMessage());
        }

        return vendor_region;
    }
}
