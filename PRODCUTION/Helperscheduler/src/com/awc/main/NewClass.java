/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.awc.main;

import java.math.BigDecimal;

/**
 *
 * @author dms
 */
public class NewClass {
    public static void main(String[] args){
        try{
            BigDecimal bd = new BigDecimal("21048994.00");
        System.out.println(bd);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
