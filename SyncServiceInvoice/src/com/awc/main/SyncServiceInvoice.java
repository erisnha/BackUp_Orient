/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.main;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyncServiceInvoice {
    
    
    public static void main(String[] args) {
        try {
             System.out.println("Scheduler Started Successfully new ");
             System.out.println("Press ctrl+C to stop the service");
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        new Processing().startProcessing();
                    } catch (IOException ex) {
                        Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(SyncServiceInvoice.class.getName()).log(Level.SEVERE, null, ex);
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
//                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (ClassNotFoundException ex) {
//                        Logger.getLogger(SyncServiceInvoice.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    System.out.println("------------- Waiting for Next Workitems--------------");
//                }
    
    
}
