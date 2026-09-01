package com.yorker.fanzania.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.yorker.fanzania.R;

public class CustomButton extends AppCompatButton {
    String customFont;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        int cf = a.getInteger(R.styleable.CustomButton_fontName, 0);
        int fontName = 0;
        switch (cf) {
            case 1:
                fontName = R.string.font_Medium;
                break;
            case 2:
                fontName = R.string.font_Regular;
                break;
            case 3:
                fontName = R.string.font_Light;
                break;
            default:
                fontName = R.string.font_Light;
                break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }
}

