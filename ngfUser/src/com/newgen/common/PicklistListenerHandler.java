package com.newgen.common;

import com.newgen.bapi.taxcode_list;
import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.Form;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.component.behavior.EventListenerImplementor;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.util.Constant.EVENT;
import java.util.Arrays;

@SuppressWarnings("serial")
public class PicklistListenerHandler extends EventListenerImplementor implements
        Serializable {
    // public String sProcessName = "UBN_FTO_";

    FormReference formObject = null;
    FormConfig formConfig = null;
    PickList pickList = null;
    String controlName = "";
    String filter_value = "";
    String query = "";
    String engineName = "";
    String sessionId = "";
    String folderId = "";
    String destFolderIndex = "";
    String docIndexs = "";
    XMLParser xmlParser = new XMLParser();
    taxcode_list objtaxcode_list = null;
    General objGeneral = null;
    List<List<String>> taxList = new ArrayList<List<String>>();
    String processInstanceId_picklistlistnerhandler;

    public PicklistListenerHandler(String picklistid) {
        super(picklistid);
    }

    public PicklistListenerHandler(String picklistid, EVENT compId) {
        super(picklistid, compId);
    }

    /*
     * This method will is called when click OK on Picklist and will set the
     * values in the Controls from picklist. (non-Javadoc)
     * 
     * @see com.newgen.omniforms.component.behavior.EventListenerImplementor#
     * btnOk_Clicked(javax.faces.event.ActionEvent)
     */
    @Override
    public void btnOk_Clicked(ActionEvent ae) {
        FormReference formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        PickList m_objPickList = FormContext.getCurrentInstance().getDefaultPickList();
        Form obj = (Form) FormContext.getCurrentInstance().getFormReference();
        controlName = m_objPickList.getAssociatedTxtCtrl();
        // m_objPickList.get
        //System.out.println(" controlName " + controlName);
        //System.out.println(" obj :" + obj);
        String sProcessName = formObject.getWFProcessName();
        General objGeneral = new General();
        //System.out.println("--------------------------i am here---------------------------");

    }

    @Override
    public void btnNext_Clicked(ActionEvent ae) {
        // PickList objPckList =
        // FormContext.getCurrentInstance().getFormReference().getNGPickList(true);
        // System.out.println(" Fetched Records = " +
        // objPckList.getM_iTotalRecordsFetched());
    }

    /*
     * This method is called on Search click button of Picklist ,we can search
     * on the value in search text box. (non-Javadoc)
     * 
     * @see com.newgen.omniforms.component.behavior.EventListenerImplementor#
     * btnSearch_Clicked(javax.faces.event.ActionEvent)
     */
    @Override
    public void btnSearch_Clicked(ActionEvent ae) {

        //System.out.println("Inside method btnSearch_Clicked");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        pickList = FormContext.getCurrentInstance().getDefaultPickList();
         processInstanceId_picklistlistnerhandler = formConfig.getConfigElement("ProcessInstanceId");
        controlName = pickList.getAssociatedTxtCtrl();
        filter_value = pickList.getSearchFilterValue().toUpperCase();
        List<List<String>> arrayList = new ArrayList();
        objtaxcode_list = new taxcode_list();
        objGeneral = new General();
        String inputXml = objtaxcode_list.getTAX_CODE_LIST();
        String outputXml = objGeneral.executeWithCallBroker(inputXml,processInstanceId_picklistlistnerhandler+"_ZBAPI_AP_AUTOMATION_TAX_CODE");
        xmlParser.setInputXML(outputXml);
        WFXmlResponse objXmlResponse = new WFXmlResponse(outputXml);
        //System.out.println("After xml response");
        if (xmlParser.getValueOf("MainCode").equalsIgnoreCase("0")) {
            for (WFXmlList objList = objXmlResponse.createList("TableParameters", "TAX_TAB"); objList.hasMoreElements(); objList.skip()) {
                //System.out.println("Inside TAX_CODE loop");
                String TAX_CODE = objList.getVal("TAX_CODE");
                //System.out.println("TAX CODE : " + TAX_CODE);
                String DESC = objList.getVal("DESC");
                //System.out.println("DESC : " + DESC);

                if (!(filter_value.equalsIgnoreCase("") || filter_value.equalsIgnoreCase("*"))) {
                    if ((TAX_CODE.toUpperCase()).contains(filter_value.trim())
                            || (DESC.toUpperCase()).contains(filter_value.trim())) {
                        taxList.add(Arrays.asList(TAX_CODE, DESC));
                    }
                } else {
                    taxList.add(Arrays.asList(TAX_CODE, DESC));
                }
            }
        }
        pickList.setBatchRequired(true);
        pickList.setBatchSize(100);
        pickList.populateData(taxList);
        pickList.setVisible(true);
    }
}
