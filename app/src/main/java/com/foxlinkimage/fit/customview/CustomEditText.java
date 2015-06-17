package com.foxlinkimage.fit.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * Created by Alex on 2015/6/10.
 */
public class CustomEditText extends EditText {
    private static Typeface font;

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (font == null) {
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/afa.ttf");
        }
        setTypeface(font);
        setTextColor(Color.parseColor("#acaae4"));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }

}
