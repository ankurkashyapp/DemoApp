package me.dm7.barcodescanner.core;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ViewFinderView extends View implements IViewFinder {
    private static final String TAG = "ViewFinderView";

    private Rect mFramingRect;

    private static final float PORTRAIT_WIDTH_RATIO = 6f/8;
    private static final float PORTRAIT_WIDTH_HEIGHT_RATIO = 0.75f;

    private static final float LANDSCAPE_HEIGHT_RATIO = 5f/8;
    private static final float LANDSCAPE_WIDTH_HEIGHT_RATIO = 1.4f;
    private static final int MIN_DIMENSION_DIFF = 50;

    private static final float DEFAULT_SQUARE_DIMENSION_RATIO = 5f / 8;

    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private int scannerAlpha;
    private static final int POINT_SIZE = 10;
    private static final long ANIMATION_DELAY = 0l;

    private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
    private final int mDefaultMaskColor = getResources().getColor(R.color.viewfinder_mask);
    private final int mDefaultBorderColor = getResources().getColor(R.color.viewfinder_border);
    private final int mDefaultBorderStrokeWidth = getResources().getInteger(R.integer.viewfinder_border_width);
    private final int mDefaultBorderLineLength = getResources().getInteger(R.integer.viewfinder_border_length);

    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    protected boolean mSquareViewFinder;
    private boolean mIsLaserEnabled;
    private float mBordersAlpha;
    private int mViewFinderOffset = 0;
    private float Y;
    private float dY = 2.5f;
    private int screenH;
    private float acc = 0.4f;
    private int framesPerSecond = 60;

    public ViewFinderView(Context context) {
        super(context);
        init();
    }

    public ViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        //set up laser paint
        mLaserPaint = new Paint();
        mLaserPaint.setColor(mDefaultLaserColor);
        mLaserPaint.setStyle(Paint.Style.FILL);

        //finder mask paint
        mFinderMaskPaint = new Paint();
        mFinderMaskPaint.setColor(mDefaultMaskColor);

        //border paint
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mDefaultBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);
        mBorderPaint.setAntiAlias(true);

        mBorderLineLength = mDefaultBorderLineLength;
    }

    @Override
    public void setLaserColor(int laserColor) {
        mLaserPaint.setColor(laserColor);
    }

    @Override
    public void setMaskColor(int maskColor) {
        mFinderMaskPaint.setColor(maskColor);
    }

    @Override
    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
    }

    @Override
    public void setBorderStrokeWidth(int borderStrokeWidth) {
        mBorderPaint.setStrokeWidth(borderStrokeWidth);
    }

    @Override
    public void setBorderLineLength(int borderLineLength) {
        mBorderLineLength = borderLineLength;
    }

    @Override
    public void setLaserEnabled(boolean isLaserEnabled) { mIsLaserEnabled = isLaserEnabled; }

    @Override
    public void setBorderCornerRounded(boolean isBorderCornersRounded) {
        if (isBorderCornersRounded) {
            mBorderPaint.setStrokeJoin(Paint.Join.ROUND);
        } else {
            mBorderPaint.setStrokeJoin(Paint.Join.BEVEL);
        }
    }

    @Override
    public void setBorderAlpha(float alpha) {
        int colorAlpha = (int) (255 * alpha);
        mBordersAlpha = alpha;
        mBorderPaint.setAlpha(colorAlpha);
    }

    @Override
    public void setBorderCornerRadius(int borderCornersRadius) {
        mBorderPaint.setPathEffect(new CornerPathEffect(borderCornersRadius));
    }

    @Override
    public void setViewFinderOffset(int offset) {
        mViewFinderOffset = offset;
    }

    // TODO: Need a better way to configure this. Revisit when working on 2.0
    @Override
    public void setSquareViewFinder(boolean set) {
        mSquareViewFinder = set;
    }

    public void setupViewFinder() {
        updateFramingRect();
    }

    public Rect getFramingRect() {
        return mFramingRect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(getFramingRect() == null) {
            return;
        }

        drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);

        if (mIsLaserEnabled) {
            drawLaser(canvas);
        }
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = getFramingRect();
        
        canvas.drawRect(0, 0, width, framingRect.top, mFinderMaskPaint);
        canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(framingRect.right + 1, framingRect.top, width, framingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(0, framingRect.bottom + 1, width, height, mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = getFramingRect();

        // Top-left corner
        Path path = new Path();
        path.moveTo(framingRect.left, framingRect.top + mBorderLineLength);
        path.lineTo(framingRect.left, framingRect.top);
        path.lineTo(framingRect.left + mBorderLineLength, framingRect.top);
        canvas.drawPath(path, mBorderPaint);

        // Top-right corner
        path.moveTo(framingRect.right, framingRect.top + mBorderLineLength);
        path.lineTo(framingRect.right, framingRect.top);
        path.lineTo(framingRect.right - mBorderLineLength, framingRect.top);
        canvas.drawPath(path, mBorderPaint);

        // Bottom-right corner
        path.moveTo(framingRect.right, framingRect.bottom - mBorderLineLength);
        path.lineTo(framingRect.right, framingRect.bottom);
        path.lineTo(framingRect.right - mBorderLineLength, framingRect.bottom);
        canvas.drawPath(path, mBorderPaint);

        // Bottom-left corner
        path.moveTo(framingRect.left, framingRect.bottom - mBorderLineLength);
        path.lineTo(framingRect.left, framingRect.bottom);
        path.lineTo(framingRect.left + mBorderLineLength, framingRect.bottom);
        canvas.drawPath(path, mBorderPaint);
    }

    public void drawLaser(Canvas canvas) {
        Rect framingRect = getFramingRect();
        
        // Draw a red "laser scanner" line through the middle to show decoding is active

        mLaserPaint.setColor(getResources().getColor(R.color.cyan1));
        mLaserPaint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
        scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
        canvas.drawRect(framingRect.left + 2, Y, framingRect.right -3, Y + 1, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan15));
        canvas.drawRect(framingRect.left + 2, Y+3, framingRect.right -3 , Y + 5, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan15));
        canvas.drawRect(framingRect.left + 2, Y+7, framingRect.right -3 , Y + 9, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan14));
        canvas.drawRect(framingRect.left + 2, Y+11, framingRect.right -3 , Y + 13, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan14));
        canvas.drawRect(framingRect.left + 2, Y+15, framingRect.right -3 , Y + 17, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan13));
        canvas.drawRect(framingRect.left + 2, Y+19, framingRect.right -3, Y + 21, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan13));
        canvas.drawRect(framingRect.left + 2, Y+23, framingRect.right -3, Y + 25, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan12));
        canvas.drawRect(framingRect.left + 2, Y+27, framingRect.right -3, Y + 29, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan11));
        canvas.drawRect(framingRect.left + 2, Y+31, framingRect.right -3, Y + 33, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan11));
        canvas.drawRect(framingRect.left + 2, Y+35, framingRect.right -3, Y + 37, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan10));
        canvas.drawRect(framingRect.left + 2, Y+39, framingRect.right -3, Y + 41, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan9));
        canvas.drawRect(framingRect.left + 2, Y+43, framingRect.right -3, Y + 45, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan9));
        canvas.drawRect(framingRect.left + 2, Y+47, framingRect.right -3, Y + 49, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan8));
        canvas.drawRect(framingRect.left + 2, Y+51, framingRect.right -3, Y + 53, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan7));
        canvas.drawRect(framingRect.left + 2, Y+55, framingRect.right -3, Y + 57, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan6));
        canvas.drawRect(framingRect.left + 2, Y+59, framingRect.right -3, Y + 61, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan5));
        canvas.drawRect(framingRect.left + 2, Y+63, framingRect.right -3, Y + 65, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan4));
        canvas.drawRect(framingRect.left + 2, Y+67, framingRect.right -3, Y + 69, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan3));
        canvas.drawRect(framingRect.left + 2, Y+71, framingRect.right -3, Y + 73, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan2));
        canvas.drawRect(framingRect.left + 2, Y+75, framingRect.right -3, Y + 77, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan1));
        canvas.drawRect(framingRect.left + 2, Y+79, framingRect.right -3, Y + 81, mLaserPaint);
        mLaserPaint.setColor(getResources().getColor(R.color.cyan1));
        canvas.drawRect(framingRect.left + 2, Y+83, framingRect.right -3, Y + 85, mLaserPaint);

        /*canvas.drawRect(framingRect.left + 2, Y - 7, framingRect.right - 1, Y + 8, mLaserPaint);
        canvas.drawRect(framingRect.left + 2, Y - 13, framingRect.right - 1, Y + 14, mLaserPaint);
        canvas.drawRect(framingRect.left + 2, Y - 19, framingRect.right - 1, Y + 20, mLaserPaint);
        canvas.drawRect(framingRect.left + 2, Y - 25, framingRect.right - 1, Y + 26, mLaserPaint);
        canvas.drawRect(framingRect.left + 2, Y - 31, framingRect.right - 1, Y + 32, mLaserPaint);*/
        //canvas.drawRect(framingRect.left + 2, Y + 37, framingRect.right -3, Y + 40, mLaserPaint);
        Y+=dY; //Increase or decrease vertical position.
        if (Y >= framingRect.bottom - 85 || Y  <= framingRect.top) {
            dY=(-1)*dY; //Reverse speed when bottom hit.
        }
        Y+=(int)acc;
        Log.e("Current Y position", ""+Y);
        Log.e("Current dY position", ""+dY);
        //invalidate();
        postInvalidateDelayed(ANIMATION_DELAY,
                framingRect.left - POINT_SIZE,
                framingRect.top - POINT_SIZE,
                framingRect.right + POINT_SIZE,
                framingRect.bottom + POINT_SIZE);
        //postInvalidateOnAnimation();

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
        screenH = getFramingRect().height();
        Y = (float)getFramingRect().top;
        Log.d("Framing Top: ", String.valueOf(getFramingRect().top));
        Log.d("Framing Bottom: ", String.valueOf(getFramingRect().bottom));
        Log.d("Framing Left: ", String.valueOf(getFramingRect().left));
        Log.d("Framing Right: ", String.valueOf(getFramingRect().right));
        //invalidate();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(getContext());

        if(mSquareViewFinder) {
            if(orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * DEFAULT_SQUARE_DIMENSION_RATIO);
                width = height;
            } else {
                width = (int) (getWidth() * DEFAULT_SQUARE_DIMENSION_RATIO);
                height = Math.round(width * LANDSCAPE_WIDTH_HEIGHT_RATIO);
            }
        } else {
            if(orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * LANDSCAPE_HEIGHT_RATIO);
                width = (int) (LANDSCAPE_WIDTH_HEIGHT_RATIO * height);
            } else {
                width = (int) (getWidth() * PORTRAIT_WIDTH_RATIO);
                height = (int) (PORTRAIT_WIDTH_HEIGHT_RATIO * width);
            }
        }

        if(width > getWidth()) {
            width = getWidth() - MIN_DIMENSION_DIFF;
        }

        /*if(height > getHeight()) {
            height = getHeight() - MIN_DIMENSION_DIFF;
        }*/

        int leftOffset = (viewResolution.x - width) / 2;
        int topOffset = (viewResolution.y - height) / 2;
        mFramingRect = new Rect(leftOffset + 43, topOffset - 28, leftOffset + width - 43, topOffset + height + 28);
    }
}

