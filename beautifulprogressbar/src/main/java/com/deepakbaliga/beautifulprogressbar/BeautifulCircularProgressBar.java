package com.deepakbaliga.beautifulprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by deepakbaliga on 06/01/17.
 */

public class BeautifulCircularProgressBar extends View {


    private final static int DEFAULT_PADDING = 80;
    private final static float START_ANGLE = 130f;
    private final static float END_ANGLE = 280f;
    private int defaultSize;
    private int arcDistance;
    private int width;
    private int height;

    private Shader shaderGray = new LinearGradient(0,0,0,750, Color.parseColor("#666666"),Color.parseColor("#66666666"), Shader.TileMode.MIRROR);

    private Paint mMiddleArcPaint,mMiddleArcSecondaryPaint;

    private Paint mCurrentTextPaint;
    private float radius;
    private RectF mMiddleRect, mMiddleSecondaryRect;
    DashPathEffect dashPath = new DashPathEffect(new float[]{10,20}, (float)1.0);


    private int mMinNum = 0;
    private int mMaxNum = 100;
    private int mCurrentNum = 0;
    private float mCurrentAngle = 0f;
    private float mMaxAngle = 280f;





    private Paint mNeedle;


    public float progress = 0f; // TODO private

    private boolean mShowText = true;

    private String mFont = "sans-serif-condensed";


    public BeautifulCircularProgressBar(Context context) {
        this(context, null);

    }

    public BeautifulCircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);


    }

    public BeautifulCircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context.obtainStyledAttributes(attrs, R.styleable.BeautifulCircularProgressBar));

    }




    private void init(TypedArray attrs) {

        defaultSize = dp2px(250);
        arcDistance = dp2px(12);

        mMiddleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleArcPaint.setStrokeWidth(56);
        mMiddleArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //mMiddleArcPaint.setStrokeCap(Paint.Cap.BUTT);

        mMiddleArcSecondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleArcSecondaryPaint.setStrokeWidth(15);
        mMiddleArcSecondaryPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        mCurrentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentTextPaint.setColor(Color.parseColor("#777777"));
        mCurrentTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentTextPaint.setTypeface(Typeface.create(mFont, Typeface.NORMAL));

        mNeedle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedle.setStrokeWidth(18);
        mNeedle.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedle.setStrokeCap(Paint.Cap.ROUND);


        attrs.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveMeasure(widthMeasureSpec, defaultSize),
                resolveMeasure(heightMeasureSpec, defaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        radius = width / 2;

        mMiddleRect = new RectF(
                DEFAULT_PADDING + arcDistance, DEFAULT_PADDING + arcDistance,
                width - DEFAULT_PADDING - arcDistance, height - DEFAULT_PADDING - arcDistance);

        mMiddleSecondaryRect = new RectF(
                DEFAULT_PADDING  + arcDistance + 75, DEFAULT_PADDING  + 75 + arcDistance,
                width - DEFAULT_PADDING - arcDistance- 75 , height - DEFAULT_PADDING - 75 - arcDistance );

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawMiddleArc(canvas);
        drawMiddleArcSmall(canvas);
        drawCenterText(canvas);

    }

    private void drawCenterText(@NonNull Canvas canvas) {
        if (mShowText) {
            mCurrentTextPaint.setTextSize(dp2px(26));
            canvas.drawText(String.valueOf(mCurrentNum)+"%  ", getWidth()/2, getHeight()/2, mCurrentTextPaint);
        }
    }

    /**
     * @param canvas
     */
    private void drawMiddleArc(@NonNull Canvas canvas) {
        float r = (radius - DEFAULT_PADDING) - arcDistance - arcDistance + 100;
        float s = (radius - DEFAULT_PADDING) - arcDistance - arcDistance;
        float toX = width / 2 + (float) Math.cos(Math.toRadians(mCurrentAngle + START_ANGLE)) * (r);
        float toY = width / 2 + (float) Math.sin(Math.toRadians(mCurrentAngle + START_ANGLE)) * (r);

        float fromX = width / 2 + (float) Math.cos(Math.toRadians(mCurrentAngle + START_ANGLE)) * (s);
        float fromY = width / 2 + (float) Math.sin(Math.toRadians(mCurrentAngle + START_ANGLE)) * (s);
        int margin = 0;


        mNeedle.setColor(getResources().getColor(R.color.red));

        //This is the outer most arc
        DashPathEffect dashPath = new DashPathEffect(new float[]{10,20}, (float)1.0);
        mMiddleArcPaint.setShader(new LinearGradient(0,0,0,750,getResources().getColor(R.color.red),getResources().getColor(R.color.purple), Shader.TileMode.MIRROR));
        mMiddleArcPaint.setPathEffect(dashPath);
        canvas.drawArc(mMiddleRect, START_ANGLE, END_ANGLE, false, mMiddleArcPaint);

        canvas.drawLine(fromX, fromY + margin, toX, toY, mNeedle);
    }


    private void drawMiddleArcSmall(@NonNull Canvas canvas) {


        mNeedle.setColor(getResources().getColor(R.color.red));

        mMiddleArcSecondaryPaint.setShader(shaderGray);
        mMiddleArcSecondaryPaint.setPathEffect(dashPath);
        canvas.drawArc(mMiddleSecondaryRect, START_ANGLE, END_ANGLE, false, mMiddleArcSecondaryPaint);


    }

    public int resolveMeasure(int measureSpec, int defaultSize) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, defaultSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            default:
                result = defaultSize;
        }

        return result;
    }

    public void setProgress(@FloatRange(from = 0.0f, to=1.0f) float progress) {
        this.progress = progress;
        mCurrentNum = (int) ((progress * (mMaxNum - mMinNum)) + mMinNum);
        mCurrentAngle = mMaxAngle * progress;
        postInvalidate();
    }


    public int dp2px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }



    public void setTextColor(@ColorInt int color) {
        mCurrentTextPaint.setColor(color);
    }
}
