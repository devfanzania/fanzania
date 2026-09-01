package com.yorker.fanzania.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;


/**
 * Created by su on 8/12/15.
 */
public class RoundTrans implements Transformation {


    private int mColor= Color.BLACK;
        static Context con;
    static Context context;

    public RoundTrans(Context con) {
        RoundTrans.con =con;
    }

    @Override
        public Bitmap transform(Bitmap source) {
            // TODO Auto-generated method stub
            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Paint paint1=new Paint();
            final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
            final RectF rectF = new RectF(rect);
        int mCornerRadius = 210;
        final float roundPx = mCornerRadius;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            BitmapFactory.Options dimensions = new BitmapFactory.Options();
            dimensions.inJustDecodeBounds = true;
//            Bitmap mBitmap = BitmapFactory.decodeResource(con.getResources(), R.drawable.loader, dimensions);
//            int height = dimensions.outHeight;
//            int width =  dimensions.outWidth;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, rect, rect, paint);

            // draw border
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
        int mBorderSize = 1;
        paint.setStrokeWidth((float) mBorderSize);
            canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);
            //-------------------



            if(source != output) source.recycle();

            return output;
        }

        @Override
        public String key() {
            // TODO Auto-generated method stub
            return "grayscaleTransformation()";
        }


}
