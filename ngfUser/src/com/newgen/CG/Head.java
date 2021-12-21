/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.CG;

/**
 *
 * @author awcadf
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

import com.newgen.common.General;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.excp.CustomExceptionHandler;
import com.newgen.omniforms.listener.FormListener;
import java.util.ArrayList;
import java.util.regex.*;

public class Head implements FormListener {

    //FormReference objFormReference = null; //FormContext.getCurrentInstance().getFormReference();
    FormReference formObject = null;
    FormConfig formConfig = null;
    String activityName = null;
    String engineName = null;
    String sessionId = null;
    String folderId = null;
    String FILE = null;
    String serverUrl = null;
    String processInstanceId = null;
    String workItemId = null;
    String userName = null;
    String processDefId = null;
    String Query = null;
    List<List<String>> result;
    PickList objPicklist;
    General objGeneral = null;

    @Override
    public void continueExecution(String arg0, HashMap<String, String> arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void eventDispatched(ComponentEvent pEvent) throws ValidatorException {
        // TODO Auto-generated method stub
        System.out.println("Value Change Event :" + pEvent);
        System.out.println("pEvent.getType() :" + pEvent.getType());
        System.out.println("pEvent.getType().name() :" + pEvent.getType().name());
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame1");
        objGeneral = new General();
        if (pEvent.getType().name().equalsIgnoreCase("VALUE_CHANGED")) {
//******************************************************************************************************************   
            if (pEvent.getSource().getName().equalsIgnoreCase("q_childtest_lv_name")) {
                System.out.println("Inside value change");
            }
        }
        if (pEvent.getType().name().equalsIgnoreCase("TAB_CLICKED")) {
            System.out.println("------------Inside Tab------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Tab1")) {
            }
        }

        if (pEvent.getType().name().equalsIgnoreCase("MOUSE_CLICKED")) {
            System.out.print("------------Inside Mouse Click------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Button1")) {
                //  throw new ValidatorException(new CustomExceptionHandler("Mail_Hrms", email_combo, "", new HashMap()));
            }

        }
    }

    @Override
    public void formLoaded(FormEvent arg0) {
        // TODO Auto-generated method stub
        System.out.println(" -------------------Intiation Workstep Loaded from formloaded.----------------");
        // TODO Auto-generated method stub
        System.out.println("form Loaded called");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        System.out.println("formObject :" + formObject);
        System.out.println("formConfig :" + formConfig);
        try {
            activityName = formObject.getWFActivityName();
            engineName = formConfig.getConfigElement("EngineName");
            sessionId = formConfig.getConfigElement("DMSSessionId");
            folderId = formConfig.getConfigElement("FolderId");
            serverUrl = formConfig.getConfigElement("ServletPath");
            //ServletUrl = serverUrl + "/servlet/ExternalServlet";
            processInstanceId = formConfig.getConfigElement("ProcessInstanceId");
            workItemId = formConfig.getConfigElement("WorkitemId");
            userName = formConfig.getConfigElement("UserName");
            processDefId = formConfig.getConfigElement("ProcessDefId");
            System.out.println("ProcessInstanceId===== " + processInstanceId);
            System.out.println("Activityname=====" + activityName);
            System.out.println("CabinetName====" + engineName);
            System.out.println("sessionId====" + sessionId);
            System.out.println("Username====" + userName);
            System.out.println("workItemId====" + workItemId);

//  ************************************************************************************
        } catch (Exception e) {
            System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
        }
    }

    @Override
    public void formPopulated(FormEvent arg0) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        // TODO Auto-generated method stub     
        objGeneral = new General();
        System.out.println("----------------------Intiation Workstep Loaded from form populated.---------------------------");
        System.out.println("Setting Frame2 Header");
        IRepeater repeaterControl2 = formObject.getRepeaterControl("Frame2");
        List<String> HeaderNames2 = new ArrayList<String>();
        HeaderNames2.add("Description");
        HeaderNames2.add("Quantity");
        HeaderNames2.add("UOM");
        HeaderNames2.add("Rate");
        HeaderNames2.add("Value");
        repeaterControl2.setRepeaterHeaders(HeaderNames2);

        System.out.println("Setting Frame3 Header");
        IRepeater repeaterControl3 = formObject.getRepeaterControl("Frame3");
        List<String> HeaderNames3 = new ArrayList<String>();
        HeaderNames3.add("Supplier Name");
        HeaderNames3.add("Price");
        HeaderNames3.add("Lead Time");
        HeaderNames3.add("Status");
        HeaderNames3.add("Remarks");
        HeaderNames3.add("Final Price");
        repeaterControl3.setRepeaterHeaders(HeaderNames3);

        //-----------------------------------------------------------------------------------------------------
    }

    @Override
    public void saveFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        System.out.print("-------------------save form completed---------");
    }

    @Override
    public void saveFormStarted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

    }

    @Override
    public void submitFormCompleted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

    }

    @Override
    public void submitFormStarted(FormEvent arg0) throws ValidatorException {
        // TODO Auto-generated method stub
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

        System.out.println("******activityName****" + activityName);

        //****************************************************************************************
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encrypt(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String decrypt(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
