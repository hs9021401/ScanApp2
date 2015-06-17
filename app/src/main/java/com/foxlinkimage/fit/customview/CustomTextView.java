package com.foxlinkimage.fit.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Alex on 2015/6/9.
 */
public class CustomTextView extends TextView {
    private static Typeface font;

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (font == null) {
            font = Typeface.createFromAsset(getContext().getAssets(), "fonts/afa.ttf");
        }
        setTypeface(font);
        setTextColor(Color.parseColor("#acaae4"));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
    }

}
