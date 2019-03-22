package shiva.joshi.common.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import shiva.joshi.common.R;
import shiva.joshi.common.customs.fonts.FontCache;


public class CustomTextView extends TextView {


    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            applyCustomFont(context, attrs, 0);
        }

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            applyCustomFont(context, attrs, defStyle);
        }
    }

    private void applyCustomFont(Context context, AttributeSet attrs, int defStyle) {

        TypedArray attributeArray = context.obtainStyledAttributes(attrs,R.styleable.CustomTextView);

        String fontName  = attributeArray.getString(R.styleable.CustomTextView_font);
        int textStyle = attributeArray.getInteger(R.styleable.CustomTextView_textStyle, Typeface.NORMAL);

        if (fontName == null) {
            return;
        }

        // fetch type face :
        Typeface customFont = FontCache.getTypeface(context, fontName, textStyle);
        setTypeface(customFont);

        attributeArray.recycle();
    }
}