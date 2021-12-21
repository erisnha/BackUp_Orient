/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.HR4U;

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
            if (pEvent.getSource().getName().equalsIgnoreCase("approver_status")) {
                String val_drop = formObject.getNGValue("approver_status");
                IRepeater repeaterControl4 = formObject.getRepeaterControl("Frame2");
                int rowcount = repeaterControl4.getRepeaterRowCount();
                System.out.println("Row count of frame 2 " + rowcount);
                System.out.println("Inside value change of Approver dropdown");
                if (val_drop.equalsIgnoreCase("Query Raised")) {
                    formObject.setVisible("Button1", true);
                } else {
                    formObject.setVisible("Button1", false);
                }

            }
        }
        if (pEvent.getType().name().equalsIgnoreCase("TAB_CLICKED")) {
            System.out.println("------------Inside Tab------------------");

            if (pEvent.getSource().getName().equalsIgnoreCase("Tab1")) {
            }
        }

        if (pEvent.getType().name().equalsIgnoreCase("MOUSE_CLICKED")) {
            System.out.println("------------Inside Mouse Click------------------");
//
//            if (pEvent.getSource().getName().equalsIgnoreCase("Button2")) {
//            }
            if (pEvent.getSource().getName().equalsIgnoreCase("q_approverdetails")) {
                System.out.println("Inside list view click q_approverdetails");
                ListView lv0 = (ListView) formObject.getComponent("q_approverdetails");
                int selectrow = lv0.getSelectedRowIndex();
                System.out.println("Selected row index : " + selectrow);

                if (selectrow == 0) {
                    formObject.setNGValue("Text57", "560");
                }

                if (selectrow == 1) {
                    formObject.setNGValue("Text57", "561");
                }

                if (selectrow == 2) {
                    formObject.setNGValue("Text57", "562");
                }
            }

            if (pEvent.getSource().getName().equalsIgnoreCase("Button1")) {
                String email = formObject.getNGValue("user_email");
                System.out.println(email);

                ListView lv1 = (ListView) formObject.getComponent("q_approverdetails");
                int rowcount = lv1.getRowCount();
                String cc_emails = "";
                String check_email = "";
                String pattern = ".*@orientelectric.com";
                System.out.println(rowcount);

                travelingexpReport report = new travelingexpReport();
                String email_body = report.getHTML();
                System.out.println(email_body);

                for (int i = 0; i < rowcount; i++) {
                    check_email = formObject.getNGValue("q_approverdetails", i, 8);
                    System.out.println(check_email);
                    boolean x = Pattern.matches(pattern, check_email);
                    System.out.println(x);
                    if (Pattern.matches(pattern, check_email)) {
                        System.out.println("Inside loop of check emails");
                        cc_emails = cc_emails + check_email + ",";
                        System.out.println(cc_emails);
                    }
                }
                System.out.println(cc_emails);
                String email_combo = email + "~" + cc_emails + "!" + email_body;
                System.out.println(email_combo);
                System.out.println("Email sent via HR4U");
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

//        formObject.setEnabled("Frame2", true);
//        formObject.setEnabled("Frame3", true);
//        formObject.setEnabled("Frame4", true);
//        formObject.setEnabled("Frame5", true);
//        formObject.setEnabled("Frame6", true);
        System.out.println("Setting Frame2 Header");
        IRepeater repeaterControl2 = formObject.getRepeaterControl("Frame2");
        List<String> HeaderNames2 = new ArrayList<String>();
        HeaderNames2.add("FDate");
        HeaderNames2.add("From City");
        HeaderNames2.add("To City");
        HeaderNames2.add("Mode of Travel");
        HeaderNames2.add("Expense Type");
        HeaderNames2.add("Reamrks");
        HeaderNames2.add("Noof Hrs");
        HeaderNames2.add("Paid By Self");
        HeaderNames2.add("Paid By Company");
        HeaderNames2.add("Paid By Self(Edit)");
        repeaterControl2.setRepeaterHeaders(HeaderNames2);

        System.out.println("Setting Frame3 Header");
        IRepeater repeaterControl3 = formObject.getRepeaterControl("Frame3");
        List<String> HeaderNames3 = new ArrayList<String>();
        HeaderNames3.add("Places Visited");
        HeaderNames3.add("Departure Date");
        HeaderNames3.add("Arrival Date");
        HeaderNames3.add("Travel Mode");
        HeaderNames3.add("Remarks");
        HeaderNames3.add("Rate/km");
        HeaderNames3.add("Stay Period Days");
        HeaderNames3.add("Paid By Self");
        HeaderNames3.add("Paid By Co.");
        HeaderNames3.add("Paid By Self(Edit)");
        repeaterControl3.setRepeaterHeaders(HeaderNames3);

        System.out.println("Setting Frame4 Header");
        IRepeater repeaterControl4 = formObject.getRepeaterControl("Frame4");
        List<String> HeaderNames4 = new ArrayList<String>();
        HeaderNames4.add("From");
        HeaderNames4.add("To");
        HeaderNames4.add("City");
        HeaderNames4.add("With Bill");
        HeaderNames4.add("Without Bill");
        HeaderNames4.add("Remarks");
        HeaderNames4.add("Paid By Self");
        HeaderNames4.add("Paid By Co.");
        HeaderNames4.add("Paid By Self(Edit)");
        repeaterControl4.setRepeaterHeaders(HeaderNames4);

        System.out.println("Setting Frame5 Header");
        IRepeater repeaterControl5 = formObject.getRepeaterControl("Frame5");
        List<String> HeaderNames5 = new ArrayList<String>();
        HeaderNames5.add("Date");
        HeaderNames5.add("From");
        HeaderNames5.add("To");
        HeaderNames5.add("Mode of Travel");
        HeaderNames5.add("Remarks");
        HeaderNames5.add("Paid By Self");
        HeaderNames5.add("Paid By Co.");
        HeaderNames5.add("Paid By Self(Edit)");
        repeaterControl5.setRepeaterHeaders(HeaderNames5);

        System.out.println("Setting Frame6 Header");
        IRepeater repeaterControl6 = formObject.getRepeaterControl("Frame6");
        List<String> HeaderNames6 = new ArrayList<String>();
        HeaderNames6.add("Daily Allowance");
        HeaderNames6.add("Local Conveyence");
        HeaderNames6.add("Lodging and Boarding Expenses");
        HeaderNames6.add("Other Allowance");
        HeaderNames6.add("Out of Pocket Allowance");
        HeaderNames6.add("Self Arrange Allowance");
        HeaderNames6.add("Travelling Fare");
        HeaderNames6.add("Allowance");
        HeaderNames6.add("Paid By Company");
        HeaderNames6.add("Paid By Self");
        HeaderNames6.add("Toatal");
        repeaterControl6.setRepeaterHeaders(HeaderNames6);

        if ("Approver".equalsIgnoreCase(activityName)) {
            formObject.setEnabled("approver_status", true);
            formObject.setEnabled("approver_remarks", true);
            formObject.setEnabled("Button1", true);
        }

        Query = "select empexp_flag,localexp_flag , travelexp_flag from ext_hr4u where processid = '" + processInstanceId + "' ";
        System.out.println("Query : " + Query);
        result = formObject.getDataFromDataSource(Query);
        System.out.println("Result size : " + result.size());
        if (result.get(0).get(0).equalsIgnoreCase("Y")) {
            formObject.setSheetVisible("Tab1", 0, true);
            formObject.setSheetVisible("Tab1", 1, false);
            formObject.setSheetVisible("Tab1", 2, false);
            System.out.println("Inside frame3 block1");
        }
        if (result.get(0).get(1).equalsIgnoreCase("Y")) {
            formObject.setSheetVisible("Tab1", 1, true);
            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetVisible("Tab1", 2, false);
            System.out.println("Inside frame3 block2");
        }
        if (result.get(0).get(2).equalsIgnoreCase("Y") || activityName.equalsIgnoreCase("Approver")) {
            formObject.setSheetVisible("Tab1", 2, true);
            formObject.setSheetVisible("Tab1", 0, false);
            formObject.setSheetVisible("Tab1", 1, false);
            System.out.println("Inside frame3 block3");

        }

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
