/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.main;

import com.awc.methods.LogProcessing;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncServiceInvoiceParking {

    public static void main(String[] args) {
        try {
            System.out.println("Scheduler started successfully");
            System.out.println("Press ctrl+c to stop");
            LogProcessing.settingLogFiles();
            Timer t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {
                    try {
                        new Processing().startProcessing();
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(SyncServiceInvoiceParking.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    System.out.println("------------- Waiting for Next Workitems--------------");
                }
            }, 0, 7200000);

        } catch (Exception e) {
            System.out.println("Exception from main method" + e);
        }
    }
//    public static void main(String[] args) {
//         try {
//                        new Processing().startProcessing();
//                    } catch (IOException ex) {
//                        Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(SyncServiceInvoiceParking.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    System.out.println("------------- Waiting for Next Workitems--------------");
//                }
}
