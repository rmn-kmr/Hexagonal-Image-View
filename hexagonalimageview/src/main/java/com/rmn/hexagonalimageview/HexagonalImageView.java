package com.rmn.hexagonalimageview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;


/**
 * Created by rmn on 12-11-2017.
 */

public class HexagonalImageView extends android.support.v7.widget.AppCompatImageView {
    Paint paint;
    Path path;
    RectF boundry;
    Canvas mCanvas;
    private Bitmap targetBitmap;
    private int strokeWidth,strokeColor;
    public HexagonalImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        path = new Path();
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.HexagonalImageView);
        strokeWidth=typedArray.getLayoutDimension(R.styleable.HexagonalImageView_strokeWidth,2);
        strokeColor=typedArray.getColor(R.styleable.HexagonalImageView_strokeColor,Color.parseColor("white"));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null || getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        float k = Math.min(getPivotX(), getPivotY());
        Log.e("k", Math.round(k) + "");
        Bitmap drawnBitmap = drawCanvas(bitmap, Math.round(2 * (k - 0)));
        canvas.drawBitmap(drawnBitmap, getPivotX()-k, getPivotY()-k, null);
    }

    private Bitmap drawCanvas(Bitmap recycledBitmap, int width) {
        Log.e("width", width + "");
        final Bitmap bitmap = verifyRecycledBitmap(recycledBitmap, width);

        final Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);
        final Path path = new Path();


        final Rect rect = new Rect(0, 0, width, width);
        final int offset = (int) (width / (double) 2 * Math.tan(30 * Math.PI / (double) 180)); // (width / 2) * tan(30deg)
        final int length = width - (2 * offset);

//        path.moveTo(cx, y0);
//        path.lineTo(xn, ae);
//        path.lineTo(xn, a1);
//        path.lineTo(cx, yn);
//        path.lineTo(x0, a1);
//        path.lineTo(x0, ae);
//        path.close();

        path.moveTo(width / 2, 0); // top
        path.lineTo(0, offset); // left top
        path.lineTo(0, offset + length); // left bottom
        path.lineTo(width / 2, width); // bottom
        path.lineTo(width, offset + length); // right bottom
        path.lineTo(width, offset); // right top
        path.close(); //back to top

        Paint paint = new Paint();
        paint.setStrokeWidth(strokeWidth);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint); // draws the bitmap for the image

        paint.reset();

        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(10));
        paint.setAntiAlias(true); // draws the border

        canvas.drawPath(path, paint);

        path.reset();
        path.moveTo(0, offset); // message so border doesn't over extend
        path.lineTo(0,offset+length); // massage so border doesn't over extend
        path.close();
        paint.setStrokeWidth(2);
        paint.setPathEffect(new CornerPathEffect(5));
        canvas.drawPath(path, paint); // draw left border a bit thicker

        path.reset();
        path.moveTo(width, offset );  // massage so border doesn't over extend
        path.lineTo(width, offset + length );  // massage so border doesn't over extend
        path.close();
        canvas.drawPath(path, paint); // draw right border a bit thicker

        return output;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    private static Bitmap verifyRecycledBitmap(Bitmap bitmap, int width) {
        final Bitmap recycledBitmap;
        if (bitmap.getWidth() != width || bitmap.getHeight() != width) {
            recycledBitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
        } else {
            recycledBitmap = bitmap;
        }
        return recycledBitmap;
    }


//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.CYAN);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//
//        int width= this.getMeasuredWidth()/2;
//        int height=this.getMeasuredHeight()/2;
//        int min=Math.min(width,height);
//
//        Log.e("getMeasuredWidth",getMeasuredWidth()+"");
//        Log.e("getWidth",getWidth()+"");
//        Log.e("getMeasuredHeight",getMeasuredHeight()+"");
//        Log.e("getHeight",getHeight()+"");
//        Log.e("getLeft",getLeft()+"");
//        Log.e("getX",getX()+"");
//        Log.e("getPivotX",getPivotX()+"");
//        Log.e("getRight",getRight()+"");
//        Log.e("getY",getY()+"");
//        Log.e("getPivotY",getPivotY()+"");
//
//        float  k=  Math.min(getPivotX(),getPivotY());
//        float cx=getPivotX();
//        float cy=getPivotY();
//        float x0=cx-k;
//        float xn=cx+k;
//        float y0=cy-k;
//        float yn=cy+k;
//        float a= (float) (yn/(1+Math.sqrt(2)));
//        float ae= (float) (a/(Math.sqrt(2)));
//        float a1=a+ae;
//
//          boundry=new RectF(x0,y0,xn,yn);
//
//        path.moveTo(cx,y0);
//        path.lineTo(xn,ae);
//        path.lineTo(xn,a1);
//        path.lineTo(cx,yn);
//        path.lineTo(x0,a1);
//        path.lineTo(x0,ae);
//        path.lineTo(cx,y0);
//
//        //path.addRect(boundry, Path.Direction.CCW);
//        //canvas.drawCircle(100,100,100,paint);
//
//       Drawable drawable= getDrawable();
//        if (drawable==null)
//            return;
//
//        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
//
//          targetBitmap = Bitmap.createBitmap(Math.round(k),
//                Math.round(k),Bitmap.Config.ARGB_8888);
//
//        mCanvas=new Canvas(targetBitmap);
//
//        canvas.clipPath(path);
//        canvas.drawPath(path,paint);
////        Bitmap ppp=bitmap;
////        mCanvas.drawBitmap(ppp,
////                new Rect(0, 0, ppp.getWidth(),
////                        ppp.getHeight()),
////                new Rect(0, 0, Math.round(k),
////                        Math.round(k)), paint);
//
//           }


}
