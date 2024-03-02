package com.example.zenzone;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Constant {
    public static String DB="zenzone";
    public static String intro="intro";

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd hh:mm:ss", Locale.US);

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }



    public static String individual_chat="individual_chat";
    public static String single_chat="single_chat";
    public static String users="Users";
    public static String group="group";
    public static String members="members";
    public static String group_members="group_members";

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }
    public static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "hh:mm:ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static long getDiff(String dated) {
        long diffDays = 0;
        try {

            Date date1 = null;
            Date date2 = null;
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String reg_date = dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
            Log.i("CONS", "getDiff: "+dated+"::"+reg_date);
            date1 = dateFormat.parse(dated);

            date2 = dateFormat.parse(reg_date);
            long diff = date2.getTime()-date1.getTime()  ;
            System.out.println("difference between days: " + diff+" "+date1.getTime()+" "+date2.getTime());
            diffDays =  diff / (60*60  * 1000);
            System.out.println("difference between days: " + diffDays);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return diffDays;
    }
}
