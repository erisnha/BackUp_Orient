/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.master;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.validator.ValidatorException;

import java.util.Date;

import com.newgen.common.General;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.PickList;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.event.ComponentEvent;
import com.newgen.omniforms.event.FormEvent;
import com.newgen.omniforms.listener.FormListener;
import java.util.Calendar;

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
    String Query = null, Query1 = null;
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
        objGeneral = new General();
        if (pEvent.getType().name().equalsIgnoreCase("VALUE_CHANGED")) {
            //******************************************************************************************************************   
            if (pEvent.getSource().getName().equalsIgnoreCase("Combo2")) {
                System.out.println("Inside Combo2 of values value change");
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("Upto")) {
                    formObject.setVisible("Text10", true);
                    formObject.setVisible("Combo4", true);
                    formObject.setVisible("Text11", false);
                    formObject.setVisible("Combo3", false);
                    formObject.setVisible("Label12", false);

                }
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("From-To")) {
                    formObject.setVisible("Text10", true);
                    formObject.setVisible("Combo4", true);
                    formObject.setVisible("Text11", true);
                    formObject.setVisible("Combo3", true);
                    formObject.setVisible("Label12", true);
                }
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("Above")) {
                    formObject.setVisible("Text10", true);
                    formObject.setVisible("Combo4", true);
                    formObject.setVisible("Text11", false);
                    formObject.setVisible("Label12", false);

                }

            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Combo10")) {
                if ("Okhla".equalsIgnoreCase(formObject.getNGValue("Combo10"))) {
                    formObject.clear("Combo11");
                    formObject.addItem("Combo11", "62 Phase3", "62 Phase3");
                    formObject.addItem("Combo11", "82 Phase3", "82 Phase3");
                }
                if ("Faridabad".equalsIgnoreCase(formObject.getNGValue("Combo10"))) {
                    formObject.clear("Combo11");
                    formObject.addItem("Combo11", "Sector-6", "Sector-6");

                }
                if ("Guwahati".equalsIgnoreCase(formObject.getNGValue("Combo10"))) {
                    formObject.clear("Combo11");
                    formObject.addItem("Combo11", "Guwahati", "Guwahati");

                }
                if ("Kolkata".equalsIgnoreCase(formObject.getNGValue("Combo10"))) {
                    formObject.clear("Combo11");
                    formObject.addItem("Combo11", "Kolkata", "Kolkata");

                }
                if ("Noida".equalsIgnoreCase(formObject.getNGValue("Combo10"))) {
                    formObject.clear("Combo11");
                    formObject.addItem("Combo11", "C1-30", "C1-30");
                    formObject.addItem("Combo11", "D209", "D209");
                }

            }

        }
        if (pEvent.getType().name().equalsIgnoreCase("TAB_CLICKED")) {
            System.out.println("------------Inside Tab------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Tab1")) {
            }
        }

        if (pEvent.getType().name().equalsIgnoreCase("MOUSE_CLICKED")) {

            System.out.print("------------Inside Mouse Click------------------");
            String finalcon = null;

            if (pEvent.getSource().getName().equalsIgnoreCase("Button7")) {
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("Upto")) {
                    finalcon = "Upto " + formObject.getNGValue("Text10") + " " + formObject.getNGValue("Combo4");
                }
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("From-To")) {
                    finalcon = "From " + formObject.getNGValue("Text10") + " " + formObject.getNGValue("Combo4") + " to " + formObject.getNGValue("Text11") + " " + formObject.getNGValue("Combo3");
                }
                if (formObject.getNGValue("Combo2").equalsIgnoreCase("Above")) {
                    finalcon = "Above " + formObject.getNGValue("Text10") + " " + formObject.getNGValue("Combo4");

                }
                formObject.setNGValue("Text5", finalcon);
                formObject.ExecuteExternalCommand("NGAddRow", "q_doa_usermapping");
                formObject.RaiseEvent("WFSave");

            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Button13")) {
                formObject.ExecuteExternalCommand("NGAddRow", "q_orient_depot_master");

                formObject.RaiseEvent("WFSave");

            }
            if (pEvent.getSource().getName().equalsIgnoreCase("Button14")) {
                formObject.ExecuteExternalCommand("NGModifyRow", "q_orient_depot_master");

                formObject.RaiseEvent("WFSave");

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

//            ************************************************************************************
        } catch (Exception e) {
            System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());
        }
    }

    @Override
    public void formPopulated(FormEvent arg0) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        // TODO Auto-generated method stub     
        try {

            ///
            String comboID = null, commentId = null;

            objGeneral = new General();
            System.out.println("----------------------Intiation Workstep Loaded from form populated.---------------------------");

            System.out.println("------------------------------------");
            formObject.setNGValue("addedby1", userName);
            formObject.setNGValue("addedby2", userName);
            formObject.setNGValue("addedby3", userName);
            formObject.setNGValue("addedby4", userName);
            formObject.setNGValue("uname", userName);

            System.out.println("Value of Activity Name" + activityName);
            formObject.setNGValue("Text8", "9000");
            formObject.setEnabled("Text8", false);

            if (activityName.equalsIgnoreCase("master")) {
              //  System.out.println("1st field " + formObject.getNGValue("q_doa_usermapping", 0, 0));

                Query = "select area_doa from complex_orient_doa_area";
                List<List<String>> userarray1 = new ArrayList();
                userarray1 = formObject.getDataFromDataSource(Query);
                for (int j = 0; j < userarray1.size(); j++) {
                    formObject.addItem("Combo1", userarray1.get(j).get(0));

                }

                Query1 = "select UserName from PDBUser";
                List<List<String>> userarray2 = new ArrayList();
                userarray2 = formObject.getDataFromDataSource(Query1);
                for (int i = 0; i < userarray2.size(); i++) {
                    formObject.addItem("Combo5", userarray2.get(i).get(0));
                    formObject.addItem("Combo6", userarray2.get(i).get(0));
                    formObject.addItem("Combo7", userarray2.get(i).get(0));
                    formObject.addItem("Combo8", userarray2.get(i).get(0));

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in FieldValueBagSet::::" + e.getMessage());

        }
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
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);
        formObject.setNGValue("addedon1", today);
        formObject.setNGValue("addedby1", userName);
        formObject.setNGValue("addedon2", today);
        formObject.setNGValue("addedby2", userName);
        formObject.setNGValue("addedon3", today);
        formObject.setNGValue("addedby3", userName);
        formObject.setNGValue("addedon4", today);
        formObject.setNGValue("addedby4", userName);

        System.out.print("-------------------Save form  started---------");
        formObject.setNGValue("Text8", "9000");

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
