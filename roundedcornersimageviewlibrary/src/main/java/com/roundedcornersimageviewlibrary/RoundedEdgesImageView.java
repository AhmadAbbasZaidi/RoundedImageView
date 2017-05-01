package com.roundedcornersimageviewlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

public class RoundedEdgesImageView extends ImageView {

    int color = Color.WHITE;

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    int borderWidth = 10;

    public void setColor(int color) {
        this.color = color;
    }

    public RoundedEdgesImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius, int color, int borderWidth) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#D1D1D1"));
        RectF rec = new RectF(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        canvas.drawRoundRect(rec, finalBitmap.getWidth() / 10, finalBitmap.getWidth() / 10, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        Paint p = new Paint();
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeWidth(borderWidth);
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        // BORDER
        RectF r = new RectF(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawRoundRect(r, finalBitmap.getWidth() / 10, finalBitmap.getWidth() / 10, p);

        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = getBitmap();

        if (b != null) {
            try {
                Bitmap bitmap = b.copy(Config.ARGB_8888, true);

                int radius = getWidth(); //Radius = width

                Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, radius, color,borderWidth);

                canvas.drawBitmap(roundBitmap, 0, 0, null);
            } catch (OutOfMemoryError error) {
                //error.printStackTrace();
            }
        }

    }

    public Bitmap getBitmap() {
        try {
            Drawable drawable = this.getDrawable();
            if (drawable instanceof GlideBitmapDrawable) {
                return ((BitmapDrawable) this.getDrawable()).getBitmap();
            } else if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) this.getDrawable()).getBitmap();
            }
        } catch (Throwable t) {

        }
        return null;
    }


}

