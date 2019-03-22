package shiva.joshi.common.customs.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;


public class FontCache {

    public final static String TAG = FontCache.class.getName();

    enum FontName {
        RAILWAY("Raleway"),
        ROBOTO("Roboto"),
        ALEGREYA_SANS("AlegreyaSans"),
        FONT_AWESOME("fontawesome-webfont");
        private String fontName;

        FontName(String value) {
            this.fontName = value;
        }

        public String getFontName() {
            return fontName;
        }
    }

    public static final int REGULAR = -1;
    public static final int BOLD = 10;
    public static final int ITALIC = 11;
    public static final int LIGHT = 12;
    public static final int MEDIUM = 13;


    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    private static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontName, typeface);
        }
        return typeface;
    }

    public static Typeface getTypeface(Context context, String fontName, int textStyle) {
        if (fontName.contentEquals(FontName.RAILWAY.getFontName())) {
            switch (textStyle) {
                case BOLD: // bold
                    return getTypeface("Raleway-Bold.ttf", context);

                case ITALIC: // italic
                    return getTypeface("Raleway-Italic.ttf", context);

                case LIGHT: // Light
                    return getTypeface("Raleway-Light.ttf", context);

                case MEDIUM: // Medium
                    return getTypeface("Raleway-Medium.ttf", context);
                default:
                    return getTypeface("Raleway-Regular.ttf", context);
            }
        } else if (fontName.contentEquals(FontName.ROBOTO.getFontName())) {
            switch (textStyle) {
                case BOLD: // bold
                    return getTypeface("Roboto-Bold.ttf", context);

                case ITALIC: // italic
                    return getTypeface("Roboto-Italic.ttf", context);

                case LIGHT: // Light
                    return getTypeface("Roboto-Light.ttf", context);
                case MEDIUM: // Medium
                    return getTypeface("Roboto-Medium.ttf", context);
                default:
                    return getTypeface("Roboto-Regular.ttf", context);
            }
        } else if (fontName.contentEquals(FontName.ALEGREYA_SANS.getFontName())) {
            switch (textStyle) {
                case BOLD: // bold
                    return getTypeface("AlegreyaSans-Bold.ttf", context);

                case ITALIC: // italic
                    return getTypeface("AlegreyaSans-Italic.ttf", context);

                case LIGHT: // Light
                    return getTypeface("AlegreyaSans-Light.ttf", context);
                case MEDIUM: // Medium
                    return getTypeface("AlegreyaSans-Medium.ttf", context);
                default:
                    return getTypeface("AlegreyaSans-Regular.ttf", context);
            }
        }
        else if (fontName.contentEquals(FontName.FONT_AWESOME.getFontName())) {
            switch (textStyle) {
                case BOLD: // bold
                    return getTypeface("fontawesome-webfont.ttf", context);
                case ITALIC: // italic
                    return getTypeface("fontawesome-webfont.ttf", context);
                case LIGHT: // Light
                    return getTypeface("fontawesome-webfont.ttf", context);
                case MEDIUM: // Medium
                    return getTypeface("fontawesome-webfont.ttf", context);
                default:
                    return getTypeface("fontawesome-webfont.ttf", context);
            }
        } else {
            // no matching font found
            // return null so Android just uses the standard font (Roboto)
            return null;
        }

    }

}