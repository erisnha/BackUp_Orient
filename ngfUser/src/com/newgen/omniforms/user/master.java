/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.omniforms.user;

import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import com.newgen.omniforms.listener.FormListener;
import com.newgen.master.Head;
import com.newgen.omniforms.FormConfig;

/**
 *
 * @author Admin
 */
public class master implements IFormListenerFactory {

    @Override
    public FormListener getListener() {
        // TODO Auto-generated method stub

        String sActivityName = null;
        
        FormConfig objConfig = FormContext.getCurrentInstance().getFormConfig();

        sActivityName = objConfig.getConfigElement("ActivityName");
                System.out.println("**********sActivityName :" + sActivityName);

        if (sActivityName.equalsIgnoreCase("master"))
                 {
            System.out.println("Apply:User");
            return new Head();
        }
        return null;
    }
}
