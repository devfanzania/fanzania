package com.yorker.fanzania.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.SwitchCompat;

public class CustomSwitchCompact extends SwitchCompat {

    public CustomSwitchCompact(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomSwitchCompact(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSwitchCompact(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface myFonts = Typeface.createFromAsset(getContext().getAssets(),
                    "Montserrat_Regular.ttf");
            setTypeface(myFonts);
        }
    }
}
