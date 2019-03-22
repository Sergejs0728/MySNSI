package shiva.joshi.common.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author - J.K.Joshi
 * Date -  21-02-2017.
 */

public class StringsOperations {

    private final static int MAX_TEXT_LENGTH = 140;

    // email validation :
    public static boolean isValidEmailAddress(final String mailAddress) {
        if (mailAddress.isEmpty())
            return false;

        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (mailAddress != null) {
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(mailAddress);
            return matcher.matches();
        }
        return false;

    }

    /* Get text from editText */
    public static String getTextFromEditText(View editText) {
        if (editText instanceof EditText)
            return ((EditText) editText).getText().toString().trim();
        else if (editText instanceof TextView)
            return ((TextView) editText).getText().toString().trim();
        else
            return "";
    }

    /* Limiting the width of text */
    private static String mReadMore = " ...Read More";

    public static Spannable limitTheWidthOfText(@NonNull String textValue, int color) {
        if (textValue.length() > MAX_TEXT_LENGTH) {
            textValue = textValue.substring(0, 120) + mReadMore;
            Spannable wordToSpan = new SpannableString(textValue);
            wordToSpan.setSpan(new ForegroundColorSpan(color), textValue.length() - mReadMore.length(), textValue.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordToSpan;
        }
        return new SpannableString(textValue);
    }

    public static boolean isMoreThanLimit(@Nullable String textValue) {
        if (textValue == null || textValue.isEmpty() || textValue.length() < MAX_TEXT_LENGTH)
            return false;
        return true;
    }

    public static Spannable getSpannableUnderLineString(String textValue, int startIndex, int endIndex, int color) {
        Spannable wordToSpan = new SpannableString(textValue);
        wordToSpan.setSpan(new UnderlineSpan(), startIndex, endIndex, 0);
        wordToSpan.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordToSpan;

    }

    /*Make first letter to upper case*/
    public static String getStringWithFirstLetterInUpperCase(String value) {
        if (value == null || value.isEmpty())
            return "";
        else if (value.length() == 1)
            return Character.toUpperCase(value.charAt(0)) + "";
        else
            return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    public static String getFontAwesomeIconString(Context context, String value) {
        String packageName = context.getPackageName();
        value = value.trim();
        String fixedString = "fa fa-";
        String fontTempName = value.substring(fixedString.length(), value.length()).replaceAll("-", "_").replaceAll(" ", "_");
        String fontName = "fa_" + fontTempName;
        //AppLogger.Logger.debug("getFontAwesomeIconString", fontName);
        try {
            int resId = context.getResources().getIdentifier(fontName, "string", packageName);
            return context.getString(resId);
        } catch (Exception ex) {
            int resId = context.getResources().getIdentifier("fa_desktop", "string", packageName);
            return context.getString(resId);
        }

    }

    public static int getResoureceIdFromString(Context context, String value) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(value, "string", packageName);
    }


    public static  Map<String, String>  bundleToHashMapConversion(Bundle bundle, Gson gson) throws Exception {
        final Set<String> keySet = bundle.keySet();
        Map<String, String> hashMap = new HashMap<>(keySet.size());
        for (final String key : keySet) {
            hashMap.put(key, bundle.getString(key));
        }
        Log.e("bundleToJsonString", "Exception: " +gson.toJson(hashMap));
        return hashMap;
    }


}
