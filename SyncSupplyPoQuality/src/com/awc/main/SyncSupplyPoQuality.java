/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.main;

import com.awc.methods.LogProcessing;
import com.newgen.omni.wf.util.excp.NGException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rahul
 */
public class SyncSupplyPoQuality {

    /**
     * @param args the command line arguments
     */
    public static void main(String ar[]) {
        try {
            LogProcessing.settingLogFiles();
            final Processing s = new Processing();
            Timer t = new Timer();
            t.schedule(new TimerTask() {

                @Override
                public void run() {                  
                    try {
                        System.out.println("Scheduler started successfully");
                        System.out.println("Press ctrl+c to stop");
                        s.startProcessing();
                    } catch (IOException | ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(SyncSupplyPoQuality.class.getName()).log(Level.SEVERE, null, ex);
                    } 

                    
                }
            }, 0, 7200000);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}