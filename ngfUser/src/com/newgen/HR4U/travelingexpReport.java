/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.HR4U;

import com.newgen.common.General;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

/**
 *
 * @author dell
 */
public class travelingexpReport implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();
    String Query = "";

    public String getHTML() {
        System.out.println("Inside HTML Table 1");
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        IRepeater repeaterControl2 = formObject.getRepeaterControl("Frame4");
        IRepeater repeaterControl3 = formObject.getRepeaterControl("Frame5");
        IRepeater repeaterControl4 = formObject.getRepeaterControl("Frame6");
        objGeneral = new General();
        int rowIndex = repeaterControl.getRepeaterRowCount();
        int rowIndex2 = repeaterControl2.getRepeaterRowCount();
        int rowIndex3 = repeaterControl3.getRepeaterRowCount();
        int rowIndex4 = repeaterControl4.getRepeaterRowCount();
        
        String html_page = "<HTML><HEAD></HEAD><BODY>"
         +"<style>\n"
                +"table, th, td {\n" +
                "border: 1px solid black;\n" +
                "border-collapse: collapse;\n" +
                "}\n" +
                "th{\n" +
                "padding: 5px;\n" +
                "text-align: left;\n" +
                "font-size: 12px \n"+
                "}\n" +
                "td{\n" +
                "padding: 5px;\n" +
                "text-align: left;    \n" +
                "font-size: 11px"+                
                "}\n" +
                "h4{\n" +
                "font-size: 13px"+                
                "}\n" +
                "h3{\n" +
                "font-size: 13px"+                
                "}\n" +                
                "#para{\n" +
                "font-size: 12px"+                
                "}\n" +
                "</style>\n"
          + "<p>Hello,</p>\n"
          + "<p>Below are the details pending for your response</p>\n"
          + "<p id='para'> <b>Voucher Code</b> : " + formObject.getNGValue("voucher_number") + "</p>\n"      
          +"<h3>Travelling Fare :</h3>\n"
          +"<table>"      
          + "<tr>"
              +"<th>Places Visited</th>"
              +"<th>Departure Date</th>" 
              +"<th>Arrival Date</th>"
              +"<th>Travel Mode</th>"
              +"<th>Remarks</th>" 
              +"<th>Rate/km</th>"
              +"<th>Stay Period Days</th>"
              +"<th>Paid by Self</th>" 
              +"<th>Paid by Co.</th>"
              +"<th>Paid by Self(Edit)</th>"        
            +"</tr>";
               
                for (int i = 0; i < rowIndex; i++) {
                    String places_visited = repeaterControl.getValue(i, "q_travellingare_places_visited");
                    String departure_date = repeaterControl.getValue(i, "q_travellingare_departure_date");
                    String arrival_date = repeaterControl.getValue(i, "q_travellingare_arrival_date");
                    String travel_mode = repeaterControl.getValue(i, "q_travellingare_travel_mode");
                    String remarks = repeaterControl.getValue(i, "q_travellingare_remarks");
                    String rate_km = repeaterControl.getValue(i, "q_travellingare_rate_km");
                    String stay_periods_day = repeaterControl.getValue(i, "q_travellingare_stay_periods_day");
                    String Paid_By_self = repeaterControl.getValue(i, "q_travellingare_Paid_By_self");
                    String Paid_By_Company = repeaterControl.getValue(i, "q_travellingare_Paid_By_Company");
                    String Paid_By_self_edt = repeaterControl.getValue(i, "q_travellingare_Paid_By_self_edt");
                
                    
              html_page = html_page + "<tr>"
              +"<td>"+places_visited+"</td>"
              +"<td>"+departure_date+"</td>" 
              +"<td>"+arrival_date+"</td>"
              +"<td>"+travel_mode+"</td>"
              +"<td>"+remarks+"</td>" 
              +"<td>"+rate_km+"</td>"
              +"<td>"+stay_periods_day+"</td>"
              +"<td>"+Paid_By_self+"</td>" 
              +"<td>"+Paid_By_Company+"</td>"
              +"<td>"+Paid_By_self_edt+"</td>"        
              +"</tr></table>";
                }
                
             html_page = html_page +"<h3>Lodging & Boarding :</h3>\n"
              +"<table>"
              +"<tr>"
              +"<th>From</th>"
              +"<th>T0</th>" 
              +"<th>City</th>"
              +"<th>Remarks</th>" 
              +"<th>With Bill</th>"
              +"<th>Without Bill</th>"
              +"<th>Paid by Self</th>" 
              +"<th>Paid by Co.</th>"
              +"<th>Paid by Self(Edit)</th>"        
            +"</tr>";   
                
                for (int i = 0; i < rowIndex2; i++) {
                    String From_Date = repeaterControl2.getValue(i, "q_lodgingboarding_From_Date");
                    String To_Date = repeaterControl2.getValue(i, "q_lodgingboarding_To_Date");
                    String city = repeaterControl2.getValue(i, "q_lodgingboarding_city");
                    String remarks = repeaterControl2.getValue(i, "q_lodgingboarding_remarks");
                    String without_bill = repeaterControl2.getValue(i, "q_lodgingboarding_without_bill");
                    String with_bill = repeaterControl2.getValue(i, "q_lodgingboarding_with_bill");
                    String Pay_By_self = repeaterControl2.getValue(i, "q_lodgingboarding_Pay_By_self");
                    String Paid_By_Company = repeaterControl2.getValue(i, "q_lodgingboarding_Paid_By_Company");
                    String Paid_By_self_edt = repeaterControl2.getValue(i, "q_lodgingboarding_Paid_By_self_edt");
                
                    
              html_page = html_page + "<tr>"
              +"<td>"+From_Date+"</td>"
              +"<td>"+To_Date+"</td>" 
              +"<td>"+city+"</td>"
              +"<td>"+remarks+"</td>"
              +"<td>"+with_bill+"</td>" 
              +"<td>"+without_bill+"</td>"
              +"<td>"+Pay_By_self+"</td>" 
              +"<td>"+Paid_By_Company+"</td>"
              +"<td>"+Paid_By_self_edt+"</td>"        
              +"</tr></table>";
                }
                
              html_page = html_page+"<h3>Local Conveyance :</h3>\n"
              +"<table>"
              +"<tr>"
              +"<th>Date</th>"
              +"<th>From</th>" 
              +"<th>To</th>"
              +"<th>Mode of Travel</th>"
              +"<th>Remarks</th>"
              +"<th>Paid by Self</th>" 
              +"<th>Paid by Co.</th>"
              +"<th>Paid by Self(Edit)</th>"        
            +"</tr>";   
                
                for (int i = 0; i < rowIndex3; i++) {
                    String fdate = repeaterControl3.getValue(i, "q_localconveyance_FDate");
                    String fromcity = repeaterControl3.getValue(i, "q_localconveyance_FromCity");
                    String tocity = repeaterControl3.getValue(i, "q_localconveyance_ToCity");
                    String travel_mode = repeaterControl3.getValue(i, "q_localconveyance_travel_mode");
                    String remarks = repeaterControl3.getValue(i, "q_localconveyance_remarks");
                    String Paid_By_self = repeaterControl3.getValue(i, "q_localconveyance_Paid_By_self");
                    String Paid_By_Company = repeaterControl3.getValue(i, "q_localconveyance_Paid_By_Company");
                    String Paid_By_self_edt = repeaterControl3.getValue(i, "q_localconveyance_Paid_By_self_edt");
                
                    
              html_page = html_page + "<tr>"
              +"<td>"+fdate+"</td>"
              +"<td>"+fromcity+"</td>" 
              +"<td>"+tocity+"</td>"
              +"<td>"+travel_mode+"</td>"
              +"<td>"+remarks+"</td>" 
              +"<td>"+Paid_By_self+"</td>" 
              +"<td>"+Paid_By_Company+"</td>"
              +"<td>"+Paid_By_self_edt+"</td>"        
              +"</tr></table>";
                }
                
                
              html_page = html_page+"<h3>Summary of Expense :</h3>\n"
              +"<table>"
              +"<tr>"
              +"<th>Daily Allowance</th>"
              +"<th>Loacal Conveyence</th>" 
              +"<th>Lodging and Boarding Expenses</th>"
              +"<th>Other Allowance</th>"
              +"<th>Out of Pocket Allownace</th>"
              +"<th>Self Arrange Allowance</th>" 
              +"<th>Travelling Fare</th>"
              +"<th>Allowance</th>"
              +"<th>Paid by Company</th>" 
              +"<th>Paid by Self</th>"
              +"<th>Total</th>"         
              +"</tr>";   
                
                for (int i = 0; i < rowIndex4; i++) {
                    String daily_allowance = repeaterControl4.getValue(i, "q_summaryofexpense_DailyAllowance");
                    String loacl_conveyance = repeaterControl4.getValue(i, "q_summaryofexpense_Local_Conveyance");
                    String lodge_board = repeaterControl4.getValue(i, "q_summaryofexpense_Lodging_and_Boarding_Expenses");
                    String other_allowance = repeaterControl4.getValue(i, "q_summaryofexpense_Other_Allowance");
                    String out_of_pocket = repeaterControl4.getValue(i, "q_summaryofexpense_Out_Of_Pocket_Allowance");
                    String self_arrange_allowance = repeaterControl4.getValue(i, "q_summaryofexpense_Self_Arrange_Allowance");
                    String travelling_fare = repeaterControl4.getValue(i, "q_summaryofexpense_Travelling_Fare");
                    String allowance = repeaterControl4.getValue(i, "q_summaryofexpense_Allowance");
                    String paid_by_company = repeaterControl4.getValue(i, "q_summaryofexpense_Paid_By_Company");
                    String paid_by_self = repeaterControl4.getValue(i, "q_summaryofexpense_Paid_By_self");
                    String total = repeaterControl4.getValue(i, "q_summaryofexpense_Total");

                
                    
              html_page = html_page + "<tr>"
              +"<td>"+daily_allowance+"</td>"
              +"<td>"+loacl_conveyance+"</td>" 
              +"<td>"+lodge_board+"</td>"
              +"<td>"+other_allowance+"</td>"
              +"<td>"+out_of_pocket+"</td>" 
              +"<td>"+self_arrange_allowance+"</td>"
              +"<td>"+travelling_fare+"</td>"
              +"<td>"+allowance+"</td>" 
              +"<td>"+paid_by_company+"</td>"
              +"<td>"+paid_by_self+"</td>"
              +"<td>"+total+"</td>"        
              +"</tr></table><br><br>";
              }
                
            html_page = html_page
            +"****************************************************************************************************<br>" +
            "This is an automatically generated email - please do not reply to it. If you have any queries please email at naveen.kumar@orientelectric.com<br>" +
            "****************************************************************************************************"        
            +"</Body></HTML>";
            
            return html_page;
    }
}
