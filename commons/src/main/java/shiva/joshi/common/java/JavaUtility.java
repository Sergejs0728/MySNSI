package shiva.joshi.common.java;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import shiva.joshi.common.LibApp;

/**
 * Author - J.K.Joshi
 * Date -  27-10-2016.
 */

public class JavaUtility {
    private static String TAG = DateTimeUtility.class.getName();

    // Convert FEET TO METER
    public static double feetToMeter(double feet) {
        try {
            return feet * 0.3048d;
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    // Convert METER TO FEET
    public static double meterToFeet(double meter) {
        try {
            return meter * 3.28084d;
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    // Convert CELSIUS TO FAHRENHEIT
    public static double celsiusToFahrenheit(double celsius) {
        try {
            return (celsius * 1.8d) + 32;
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    // Convert  FAHRENHEIT TO CELSIUS
    public static double fahrenheitToCelsius(double fahrenheit) {
        try {
            return (fahrenheit - 32) * .55d;
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    //Convert bar to psi and vice versa
    public static double barToPsi(double bar) {
        try {
            return (bar * 14.5038d);
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    public static double psiToBar(double psi) {
        try {
            return (psi * 0.068947697587212d);
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    /* Weight */
    public static double kilogramToPound(double kilogram) {
        try {
            return (kilogram * 2.20462d);
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    public static double poundToKilogram(double pound) {
        try {
            return (pound * 0.453592d);
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return 0d;
    }

    //Format  float or double value up to 2 digit
    public static String formatUptoTwoDigit(double value) {
        try {
            return String.valueOf(Double.parseDouble(new DecimalFormat("###.##").format(value)));
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return "0";
    }


    public static double getExpoToDouble(double value) {
        try {
            long longValue = new BigDecimal(value).longValue();
            return (longValue);
        } catch (NumberFormatException ex) {
            LibApp.showException(TAG, ex);
        }
        return value;
    }

    public static float round(float input, float step) {
        try {
            return ((Math.round(input / step)) * step);
        } catch (Exception ex) {
            LibApp.showException(TAG, ex);
        }
        return input;
    }

    public static long getRandomId() {
        Random rand = new Random();
        return rand.nextLong();
    }

    public static String getFileNameFromUrl(String path) throws IndexOutOfBoundsException, NumberFormatException {
        String file = path.substring(path.lastIndexOf('/') + 1, path.length());
        return file;
    }

}
//Double.parseDouble(new DecimalFormat("###.##").format(
