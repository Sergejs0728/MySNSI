package shiva.joshi.common.helpers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.Locale;

import shiva.joshi.common.logger.AppLogger;

public class ExternalIntentHelper {

    private static final String TAG = ExternalIntentHelper.class.getName();

    public static String OpenEmailChooser(Context context, String[] to, String subject, String message) {
        try {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/html");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            context.startActivity(Intent.createChooser(sendIntent, "Send Email"));
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
            return ex.getMessage();
        }
        return null;
    }

    public static String composeEmail(Context context, String[] addresses, String subject, String message) {

        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, message);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
            return ex.getMessage();
        }
        return null;
    }


    public static String openSkype(Context context, String userName) {
        try {
            Intent skypeIntent = new Intent(Intent.ACTION_SENDTO);
            skypeIntent.setClassName("com.skype.raider", "com.skype.raider.Main");
            skypeIntent.setData(Uri.parse("skype:" + userName));
            context.startActivity(skypeIntent);
        } catch (Exception ex) {
            AppLogger.Logger.error(TAG, ex.getMessage(), ex);
            return ex.getMessage();
        }
        return null;
    }


    public static boolean openMap(Context context, double latitude, double longitude,int zoom,String label) {

        Uri gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", latitude, longitude,zoom,latitude, longitude,label));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            context.startActivity(mapIntent);
            return true;
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                context.startActivity(unrestrictedIntent);
                return true;
            } catch (ActivityNotFoundException innerEx) {
                AppLogger.showToast(TAG, "Please install a maps application");
            }
        }
        return false;
    }


}