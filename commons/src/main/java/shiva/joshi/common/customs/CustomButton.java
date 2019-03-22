package shiva.joshi.common.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import shiva.joshi.common.R;
import shiva.joshi.common.customs.fonts.FontCache;


public class CustomButton extends Button {

    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            applyCustomFont(context, attrs, 0);
        }
    }

    public CustomButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            applyCustomFont(context, null, 0);
        }
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            applyCustomFont(context, attrs, defStyle);
        }
    }

    private void applyCustomFont(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs,R.styleable.CustomButton);

        String fontName = attributeArray.getString(R.styleable.CustomButton_font);
        int textStyle = attributeArray.getInteger(R.styleable.CustomButton_textStyle, Typeface.NORMAL);

        if (fontName == null) {
            return;
        }

        // fetch type face :
        Typeface customFont = FontCache.getTypeface(context, fontName, textStyle);

        //Setting face
        setTypeface(customFont);
        // setting formation
        setTransformationMethod(null);
        attributeArray.recycle();


    }
}