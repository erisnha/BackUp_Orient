/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.newgen.common;

import com.newgen.omniforms.FormConfig;
import com.newgen.omniforms.FormReference;
import com.newgen.omniforms.context.FormContext;
import java.io.Serializable;

/**
 *
 * @author Latitude 3460
 */
public class StringAddition implements Serializable{

    FormReference formObject = null;
    FormConfig formConfig = null;

    public String getStringSum(String s1, String s2) {

        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

        System.out.println("Inside getStringSum : " + s1 + "/" + s2);
        String s_s1[] = s1.split("\\.");
        //System.out.println("Len" + s_s1.length);
        String before_dec_s1 = s_s1[0];
        String after_dec_s1 = "0." + s_s1[1];
        //System.out.println("Values a_s1: " + before_dec_s1 + " /b_s2: " + after_dec_s1);

        String s_s2[] = s2.split("\\.");
        String before_decs_s2 = s_s2[0];
        String after_dec_s2 = "0." + s_s2[1];
        //System.out.println("Values a_s2: " + before_decs_s2 + " /b_s2: " + after_dec_s2);

        String a = add(before_dec_s1, before_decs_s2);
        //System.out.println("Before decimal sum :" + a);

        float b = Float.parseFloat(after_dec_s1) + Float.parseFloat(after_dec_s2);
        //System.out.println("After decimal sum :" + b);

        String s_s3[] = String.valueOf(b).split("\\.");
        String before_decs_s3 = s_s3[0];
        String after_dec_s3 = s_s3[1];
        //System.out.println("Values a_s3: " + before_decs_s3 + " /b_s3: " + after_dec_s3);

        String c = add(a, before_decs_s3);
        //System.out.println("C : " + c);

        String final_total = c + "." + after_dec_s3;
        //System.out.println("Fianl Total : " + final_total);

        return final_total;
    }

    public String add(String a, String b) throws NumberFormatException {

        formObject = FormContext.getCurrentInstance().getFormReference();
        formConfig = FormContext.getCurrentInstance().getFormConfig();

        StringBuilder buf = new StringBuilder();
        int i1 = a.length() - 1;
        int i2 = b.length() - 1;
        double carry = 0;
        while (i1 >= 0 || i2 >= 0 || carry != 0) {
            float x1 = i1 < 0 ? 0 : Float.parseFloat(Character.toString(a
                    .charAt(i1)));

            float x2 = i2 < 0 ? 0 : Float.parseFloat(Character.toString(b
                    .charAt(i2)));

            double sum = x1 + x2 + carry;
            if (sum > 9) {
                carry = 1.00;
                sum = (sum - 10.0);
            } else {
                carry = 0.00;
            }
            int add = (int) sum;
            buf.append(add);
            i1--;
            i2--;

        }
        buf.reverse();

        //System.out.println(buf);
        return String.valueOf(buf);
    }
}
