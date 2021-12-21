/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.ServiceProcess;

import com.newgen.common.General;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.ListView;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Latitude 3460
 */
public class setPODetails implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    int difference;
    String Query;
    List<List<String>> result;
    private List<List<String>> result1;

    public void setPOLine() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        String xmlnew = "";
        formObject.clear("ListView2");
        Query = "Select MATERIAL, SHORT_TEXT, QUANTITY, NET_PRICE, NET_VALUE, ACCTASSCAT, PLANT, UNIT, "
                + "PO_ITEM, BASE_UNIT, BASE_UOM_ISO, TAX_CODE, STORE_LOC, PROFIT_CTR, VAL_TYPE from "
                + "complex_orient_po_item where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "'";
        System.out.println("Query : " + Query);
        result = formObject.getDataFromDataSource(Query);
        for (int i = 0; i < result.size(); i++) {
            xmlnew = xmlnew
                    + "<ListItem><SubItem>" + result.get(i).get(0)
                    + "</SubItem><SubItem>" + result.get(i).get(1)
                    + "</SubItem><SubItem>" + result.get(i).get(2)
                    + "</SubItem><SubItem>" + result.get(i).get(3)
                    + "</SubItem><SubItem>" + result.get(i).get(4)
                    + "</SubItem><SubItem>" + result.get(i).get(5)
                    + "</SubItem><SubItem>" + result.get(i).get(6)
                    + "</SubItem><SubItem>" + result.get(i).get(7)
                    + "</SubItem><SubItem>" + result.get(i).get(8)
                    + "</SubItem><SubItem>" + result.get(i).get(9)
                    + "</SubItem><SubItem>" + result.get(i).get(10)
                    + "</SubItem><SubItem>" + result.get(i).get(11)
                    + "</SubItem><SubItem>" + result.get(i).get(12)
                    + "</SubItem><SubItem>" + result.get(i).get(13)
                    + "</SubItem><SubItem>" + ""
                    + "</SubItem><SubItem>" + result.get(i).get(14)
                    + "</SubItem><SubItem>" + ""
                    + "</SubItem></ListItem>";
        }
        System.out.println("XML : " + xmlnew);
        formObject.NGAddListItem("ListView2", xmlnew);
    }

    public void setEntryLine() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        String xmlnew = "";
        System.out.println("Inside Entry Line");
        ListView lv0 = (ListView) formObject.getComponent("q_orient_entrysheet_sel");
        int rowcount = lv0.getRowCount();
        System.out.println("Row count Entrysheet: " + rowcount);
        for (int j = 0; j < rowcount; j++) {
            Query = "select PO_ITEM,MAT_DOC,PSTNG_DATE,QUANTITY,VAL_LOCCUR,CURRENCY from complex_orient_poitem_history "
                    + "where pinstanceid like '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                    + "and HIST_TYPE in ('D','E') "
                    + "and REF_DOC = '" + formObject.getNGValue("q_orient_entrysheet_sel", j, 0) + "'";
            System.out.println("Query : " + Query);
            result = formObject.getDataFromDataSource(Query);
            for (int i = 0; i < result.size(); i++) {
                xmlnew = xmlnew
                        + "<ListItem><SubItem>" + result.get(i).get(0)
                        + "</SubItem><SubItem>" + result.get(i).get(1)
                        //  + "</SubItem><SubItem>" + result.get(i).get(2)
                        + "</SubItem><SubItem>" + result.get(i).get(3)
                        + "</SubItem><SubItem>" + result.get(i).get(4)
                        + "</SubItem><SubItem>" + result.get(i).get(5)
                        + "</SubItem></ListItem>";
            }
        }
        System.out.println("XML : " + xmlnew);
        formObject.NGAddListItem("ListView1", xmlnew);
    }

    public void setPoTotal(String entrysheetno) {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        objGeneral = new General();
        float totalwithtax;
        Query = "Select VAL_LOCCUR , PO_ITEM from complex_orient_poitem_history where pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                + "and MAT_DOC = '" + entrysheetno + "'";
        System.out.println("Query : " + Query);
        result = formObject.getDataFromDataSource(Query);
        System.out.println("Rsult : " + result.get(0).get(0));

        Query = "select TAX_CODE from complex_orient_po_item where "
                + "pinstanceid = '" + formConfig.getConfigElement("ProcessInstanceId") + "' "
                + "and PO_ITEM = '" + result.get(0).get(1) + "'";
        System.out.println("Query : " + Query);
        result1 = formObject.getDataFromDataSource(Query);
        String Taxvalue = objGeneral.calculateTax(result.get(0).get(0), result1.get(0).get(0));
        System.out.println("Tax value " + Taxvalue);

        totalwithtax = Float.parseFloat(result.get(0).get(0)) + Float.parseFloat(Taxvalue);
        System.out.println("TotalwithTax : " + totalwithtax);
        String amount_po = formObject.getNGValue("amount_po");
        System.out.println("Po Amount : " + amount_po);
        
        if(amount_po.equalsIgnoreCase("")){
            amount_po = "0";
        }        
        
        float total = Float.parseFloat(amount_po) + totalwithtax;
        System.out.println("Total : " + total);
        formObject.setNGValue("amount_po", total);
    }
}
