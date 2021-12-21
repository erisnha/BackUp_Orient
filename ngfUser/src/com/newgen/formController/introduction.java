/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.formController;

import com.newgen.common.General;
import com.newgen.common.XMLParser;
import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.component.IRepeater;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

/**
 *
 * @author Latitude 3460
 */
public class introduction implements Serializable {

    FormReference formObject = null;
    FormConfig formConfig = null;
    General objGeneral = null;
    XMLParser xmlParser = new XMLParser();

    public void setIntroFormValidation() {
        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();
        IRepeater repeaterControl = formObject.getRepeaterControl("Frame3");
        objGeneral = new General();
        
        formObject.setSheetVisible("Tab1", 1, false);
        formObject.setSheetVisible("Tab1", 2, false);
        formObject.setSheetVisible("Tab1", 3, false);
        formObject.setSheetVisible("Tab1", 4, false);
        formObject.setSheetVisible("Tab1", 5, false);
        //System.out.println("date.... " + formObject.getNGValue("po_date"));

    }

}
