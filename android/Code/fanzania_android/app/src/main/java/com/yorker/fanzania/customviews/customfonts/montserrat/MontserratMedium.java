package com.yorker.fanzania.customviews.customfonts.montserrat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yorker.fanzania.customviews.customfonts.FontCache;

/**
 * Created by su on 8/7/15.
 */
public class MontserratMedium extends TextView {


    public MontserratMedium(Context context) {
        super(context);
        init();
    }


    public MontserratMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MontserratMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        super.setTypeface(FontCache.get("Montserrat_Medium.ttf", getContext()));
    }
}
