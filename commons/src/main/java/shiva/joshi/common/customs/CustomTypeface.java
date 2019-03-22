package shiva.joshi.common.customs;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Author - J.K.Joshi
 * Date -  28-09-2016.
 */
public class CustomTypeface {

    // apply custom font and color to menu items
    public static void applyFontToMenuItems(Menu menu, Typeface typeface) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableStringBuilder spanString = new SpannableStringBuilder(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new CustomTypefaceSpan("", typeface), 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            item.setTitle(spanString);
        }
    }

    // apply custom font and color to menu items
    public static void applyFontToMenuItems(Menu menu, Typeface typeface ,int color) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableStringBuilder spanString = new SpannableStringBuilder(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new CustomTypefaceSpan("", typeface), 0, spanString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(color), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
    }
}