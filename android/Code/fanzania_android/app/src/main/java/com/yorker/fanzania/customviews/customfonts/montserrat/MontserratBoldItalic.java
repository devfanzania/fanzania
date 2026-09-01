package com.yorker.fanzania.customviews.customfonts.montserrat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yorker.fanzania.customviews.customfonts.FontCache;

/**
 * Created by su on 8/7/15.
 */
public class MontserratBoldItalic extends TextView {


    public MontserratBoldItalic(Context context) {
        super(context);
        init();
    }


    public MontserratBoldItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MontserratBoldItalic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        super.setTypeface(FontCache.get("Montserrat_BoldItalic.ttf", getContext()));
    }
}
