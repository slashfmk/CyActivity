package com.group3sc2.cyactivity.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

    public static String formattedDate(Date date){
        return sdf.format(date);
    }
}
