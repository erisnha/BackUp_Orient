/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.sapbapi;

import com.awc.methods.CommonFunctions;
import com.awc.methods.LogProcessing;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author dms
 */
public class Validation {

    CommonFunctions objCommonFunctions = null;

    public String checkBeforeParking(String processInstanceID) {
        objCommonFunctions = new CommonFunctions();
        ResultSet RS_CHECK = null, RS_CHECK1 = null, RS_CHECK2 = null, RS_CHECK3 = null, RS_SUM = null;
        Statement ST_CHECK = null, ST_CHECK1 = null, ST_CHECK2 = null, ST_CHECK3 = null, ST_SUM = null;
        String Query = "", PO_ITEM = "", REF_DOC_NO = "", QUANTITY = "", VAL_LOCCUR = "", message;
        int flag = 0, flag1 = 0, flag2 = 0;
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        String currentDate = date.toString();
        double quantity_sum = 0;
        try {
            ST_CHECK = objCommonFunctions.con.createStatement();
            ST_CHECK1 = objCommonFunctions.con.createStatement();
            ST_CHECK2 = objCommonFunctions.con.createStatement();
            ST_CHECK3 = objCommonFunctions.con.createStatement();
            ST_SUM = objCommonFunctions.con.createStatement();
            Query = " select REF_DOC_NO,PO_ITEM, QUANTITY, VAL_LOCCUR from complex_orient_poitem_history"
                    + " where pinstanceid = '" + processInstanceID + "'  and MOVE_TYPE = '105'";
            LogProcessing.xmllogs.info("Query is " + Query);
            RS_CHECK = ST_CHECK.executeQuery(Query);
            while (RS_CHECK.next()) {
                REF_DOC_NO = RS_CHECK.getString(1);
                PO_ITEM = RS_CHECK.getString(2);
                QUANTITY = RS_CHECK.getString(3);
                VAL_LOCCUR = RS_CHECK.getString(4);

                Query = " select count(*) from complex_orient_invoice_item "
                        + " where pinstanceid = '" + processInstanceID + "' and po_item =" + PO_ITEM;
                RS_CHECK1 = ST_CHECK1.executeQuery(Query);
                LogProcessing.xmllogs.info("Query is " + Query);
                while (RS_CHECK1.next()) {
                    flag = RS_CHECK1.getInt(1);
                }
                LogProcessing.xmllogs.info("Flag PO item:" + flag);
                if (flag > 0) {

                    Query = " select count(*) from complex_orient_invoice_item "
                            + " where pinstanceid = '" + processInstanceID + "' and po_item =" + PO_ITEM
                            + " and cast(quantity as numeric(38,3)) = '" + QUANTITY + "'";
//                    System.out.println("flag1 ::= "+Query);
                    RS_CHECK2 = ST_CHECK2.executeQuery(Query);
                    LogProcessing.xmllogs.info("Query is " + Query);
                    while (RS_CHECK2.next()) {
                        flag1 = RS_CHECK2.getInt(1);
                    }
                    LogProcessing.xmllogs.info("Flag1 PO item:" + flag1);
                    if (flag1 > 0) {
//                        Query = " select count(*) from complex_orient_invoice_item "
//                                + " where pinstanceid = '" + processInstanceID + "' and po_item =" + PO_ITEM
//                                + " and cast(quantity as numeric(38,3)) = '" + QUANTITY + "' and "
//                                + "ROUND(cast(line_total_wtax as numeric(38,3)),1) = ROUND(cast('" + VAL_LOCCUR + "' as numeric(38,3)),1) ";
                       
                        
                         Query = " select ROUND(cast(line_total_wtax as numeric(38,3)),1) from complex_orient_invoice_item "
                                + " where pinstanceid = '" + processInstanceID + "' and po_item =" + PO_ITEM
                                + " and cast(quantity as numeric(38,3)) = '" + QUANTITY + "'";
                                
                        double amount = 0.0;
                        double valloccur = Double.parseDouble(VAL_LOCCUR);
                       
                        RS_CHECK3 = ST_CHECK3.executeQuery(Query);
                        LogProcessing.xmllogs.info("Query is " + Query);
                        while (RS_CHECK3.next()) {
                            amount = RS_CHECK3.getDouble(1);
                        }
                        LogProcessing.xmllogs.info("Flag2 PO item:" + flag2);
                        if ((amount+5) > valloccur && (amount-5)< valloccur) {
                            return "Success";
                        } else {
                            message = "Base amount of invoice and GRN doesn't match for line item " + PO_ITEM;
                            LogProcessing.xmllogs.info(message + " :- " + processInstanceID);
//                            LogProcessing.errorlogs.info(Query);
                            LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                            objCommonFunctions.setException(message);
                            return "Failure";
                        }
                    } else {
                        message = "Quantity of invoice and GRN doesn't match for line item " + PO_ITEM;
                        LogProcessing.xmllogs.info(processInstanceID + " :- " + message);
                        LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                        objCommonFunctions.setException(message);
                        return "Failure";
                    }
                } else {
                    message = "Mismatch in invoice and GRN lines. Line item" + PO_ITEM + " doesnt exist in invoice line";
                    LogProcessing.xmllogs.info(message + " :- " + processInstanceID);
                    LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
                    objCommonFunctions.setException(message);
                    return "Failure";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Exception" + e.getMessage());
            //System.out.println(e.getMessage() + " :- " + processInstanceID);
            message = e.getMessage();
            LogProcessing.errorlogs.info(processInstanceID + " :- " + message);
            objCommonFunctions.setException(message);
            return "Failure";
        }
        return "Success";
    }
}
