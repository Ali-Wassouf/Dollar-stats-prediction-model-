/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facebookcrawl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ehab Bshara
 */
public class Normalize {
    
    public static Map nomalize(String post)
    {
        Map<String,Object> result = new HashMap<>();
        String city  = null;
        int sell = 0;
        int buy = 0;
        String[] sp = post.split("\\ |\\#|_");
        if((post.contains("الشراء")||post.contains("شراء"))&&(post.contains("المبيع")||post.contains("مبيع")))
        {
            for (int i = 0; i < sp.length; i++) {
                if (sp[i].equals("دمشق") || sp[i].equals("حلب") || sp[i].equals("اللاذقية") || sp[i].equals("طرطوس")||sp[i].equals("حمص")) {
                    city = sp[i];
                } else if (sp[i].equals("شراء") || sp[i].equals("الشراء")) {
                    if (isInteger(sp[i + 1])) {
                        buy = Integer.parseInt(sp[i + 1]);
                    }
                    if (isInteger(sp[i + 2])) {
                        buy = Integer.parseInt(sp[i + 2]);
                    }

                } else if (sp[i].equals("مبيع") || sp[i].equals("المبيع")) {
                    if (isInteger(sp[i + 1])) {
                        sell = Integer.parseInt(sp[i + 1]);
                    }
                    if (isInteger(sp[i + 2])) {
                        sell = Integer.parseInt(sp[i + 2]);
                    }
                }
        }
        //System.out.print("city:"+city+"\nbuy"+sell+"\nsell"+buy);
        if(city == null)
            return null ;
        result.put("city", city);
        result.put("sell", sell);
        result.put("buy", buy);
        return result;
        }
        else return null;
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}
