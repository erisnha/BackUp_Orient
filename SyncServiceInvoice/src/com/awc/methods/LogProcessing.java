/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.methods;

import java.io.*;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public final class LogProcessing {

    public static Logger xmllogs = null, errorlogs = null, summlogs = null;

    public static void settingLogFiles() {
        InputStream is = null;
        try {
            String currentdir = System.getProperty("user.dir");
            String filePath = currentdir + File.separator + "log4j.properties";
            is = new BufferedInputStream(new FileInputStream(filePath));
            Properties ps = new Properties();
            ps.load(is);
            is.close();
            org.apache.log4j.LogManager.shutdown();
            PropertyConfigurator.configure(ps);
            xmllogs = Logger.getLogger("XML");
            summlogs = Logger.getLogger("Summary");
            errorlogs = Logger.getLogger("Error");
        } catch (IOException e) {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException te) {
                errorlogs.info("Error in setting Logger===> " + te);
            }
        }
    }
}
