/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.OrientAP.Head;
import com.newgen.omniforms.FormConfig;

/**
 *
 * @author Admin
 */
public class OrientAP implements IFormListenerFactory {

    @Override
    public FormListener getListener() {
        // TODO Auto-generated method stub

        String sActivityName = null;
        
        FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();

        sActivityName = objConfig.getConfigElement("ActivityName");
                System.out.println("**********sActivityName :" + sActivityName);

        if (sActivityName.equalsIgnoreCase("Manual_Introduction")
                || sActivityName.equalsIgnoreCase("Error")
                || sActivityName.equalsIgnoreCase("Gate Entry")
                || sActivityName.equalsIgnoreCase("Exception")
                || sActivityName.equalsIgnoreCase("MIGO Cancelled")
                || sActivityName.equalsIgnoreCase("Store")
                || sActivityName.equalsIgnoreCase("Store Hold")
                || sActivityName.equalsIgnoreCase("Quality")
                || sActivityName.equalsIgnoreCase("Quality_Hold")
                || sActivityName.equalsIgnoreCase("Accounts")
                 || sActivityName.equalsIgnoreCase("Finance")
                || sActivityName.equalsIgnoreCase("Audit")
                || sActivityName.equalsIgnoreCase("ParkingException")
                || sActivityName.equalsIgnoreCase("PostingException")) {
            System.out.println("Apply:User");
            return new Head();
        }
        return null;
    }
}
