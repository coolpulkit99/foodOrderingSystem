package com.weekday.orderingsystem.ov1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {
    public static DecimalFormat getDecimalFormatter(){
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
		DecimalFormat df = new DecimalFormat("#.##########", otherSymbols);
        return df;
    }
}
