package com.awc.main;

import au.com.bytecode.opencsv.CSVReader;
import com.awc.methods.CommonFunctions;
import com.awc.methods.LogProcessing;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author dms
 */
public class MedtaData_CSV_Reader {

    CommonFunctions common = null;
    int x = 0;
    String Query = "";

    public void getCSVPathInfo(CommonFunctions common) {
        String absoultePath = "C:\\Users\\dms\\OneDrive - Orient Electric\\TPCFormsUAT";
        this.common = common;


        try {

            Statement search_st = common.con.createStatement();
            ResultSet search_rs = null;
            LogProcessing.xmllogs.info("*******************CSV_READER*****" + common.StartDate + "*********************");
            File file = new File(absoultePath);
            File[] directoryListing = file.listFiles();
            K:
            for (File file1 : directoryListing) {

                if (file1.isDirectory()) {
                    String fileName = file1.getName();
                    // System.out.println("File Name is :: "+fileName);
                    Query = "Select SharePointRequestId from Ext_SharePointIntegration where SharePointRequestId = '" + fileName + "'";
                    //   System.out.println("Query is ::: " + Query);
                    search_rs = search_st.executeQuery(Query);

                    if (search_rs.next()) {
                        continue K;
                    }
                    String metadata_csv_path = absoultePath + File.separator + fileName + File.separator + "MetaData.csv";
                    csvRead(metadata_csv_path);
                } else {
                    continue;
                }
                if (x == 1) {
                    //  System.out.println(file1.delete());
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void csvRead(String metadata_csv_path) throws SQLException {
        FileReader filereader = null;


        PreparedStatement PS_INSERT = null;
        try {


            File metadata_csv = new File(metadata_csv_path);
            String PONO = "", POReference = "", Service = "", Bill = "", BillDate = "", Amount = "", Vendor = "", DocumentName = "", AbsolutePath = "", CreatedBy = "";

            filereader = new FileReader(metadata_csv);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            int i;
            int j = 1;
            try {
                l:
                while ((nextRecord = csvReader.readNext()) != null) {

                    i = 0;
                    for (String cell : nextRecord) {
                        i++;

                        if (j == 1) {
                            j++;
                            continue l;
                        }
                        if (i == 1) {

                            PONO = cell;

                        }
                        if (i == 2) {

                            POReference = cell;


                        }
                        if (i == 3) {
                            //  Vendor = cell;
                            Service = cell;
                        }
                        if (i == 4) {
                            Bill = cell;
                        }
                        if (i == 5) {
                              System.out.println("Cell is ::" + cell);
                            LogProcessing.xmllogs.info("*******Cell Date of POReference " + POReference + " :- " + cell + "*******");


                            
//                              SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                              SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//                              Date  date = sdf.parse(cell);
//                             
//                             
//                              BillDate = sdf1.format(date);
//                              System.out.println("Cell is ::" + BillDate);
                        BillDate = cell;

                            //    System.out.println("Bill date is :::" + BillDate);
                            LogProcessing.xmllogs.info("*******BillDate of POReference " + POReference + " :- " + BillDate + "*******");


                        }
                        if (i == 6) {
                            Amount = cell;
                        }
                        if (i == 7) {
                            //Service = cell;
                            Vendor = cell;
                        }
                        if (i == 8) {
                            DocumentName = cell;
                        }
                        if (i == 9) {
                            AbsolutePath = cell;
                        }
                        if (i == 10) {
                            CreatedBy = cell;
                        }

                    }
                    PS_INSERT = common.con.prepareStatement("insert into Ext_SharepointIntegration (Record_Id,SharePointRequestId,PoNumber,ServiceEntryNo,InvoiceNumber,InvoiceDate,InvoiceAmount,VendorCode,DocumentName,DocumentPath,ProcessInstanceId,CreationDate,created_by) values(next value for recordid,?,?,?,?,?,?,?,?,?,'NOT_ASSIGN',?,?)");
                    PS_INSERT.setString(1, POReference.trim());
                    PS_INSERT.setString(2, PONO.trim());
                    PS_INSERT.setString(3, Service.trim().replace("&", ";#"));
                    PS_INSERT.setString(4, Bill.trim());

                    PS_INSERT.setString(5, BillDate.trim());
                    PS_INSERT.setString(6, Amount.trim());
                    PS_INSERT.setString(7, Vendor.trim());
                    PS_INSERT.setString(8, DocumentName.trim());
                    PS_INSERT.setString(9, AbsolutePath.trim());
                    PS_INSERT.setString(10, common.currentDate.trim());
                    PS_INSERT.setString(11, CreatedBy.trim());
                    //  System.out.println(PONO + " " + POReference + " " + Service + " " + Bill + " " + BillDate + " " + Amount + " " + Vendor + " " + DocumentName + " " + AbsolutePath);
                    x = PS_INSERT.executeUpdate();

                    //System.out.println("Output of Prepared statement is ::" + x);


                }

            } catch (Exception ex) {
                //System.out.println("Exception ex is :::" + ex);
                LogProcessing.errorlogs.info("Exception From CSV Reader ex:: " + ex.getMessage());
                ex.printStackTrace();
            }
        } catch (Exception e) {
            // System.out.println("Exception e is :::" + e);
            LogProcessing.errorlogs.info("Exception From CSV Reader e:: " + e.getMessage());
        }
    }
}
