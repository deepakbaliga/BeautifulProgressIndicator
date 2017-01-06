package com.deepakbaliga.beautifulprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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

    private Paint mMiddleArcPaint;

    private Paint mCurrentTextPaint;
    private float radius;
    private RectF mMiddleRect;


    private int mMinNum = 0;
    private int mMaxNum = 100;
    private int mCurrentNum = 0;
    private float mCurrentAngle = 0f;
    private float mMaxAngle = 280f;

    private int decreaseBy20 = 280;




    private Paint mNeedle, mLines;


    public float progress = 0f; // TODO private

    private boolean mShowText = true;

    private String mFont = "sans-serif-condensed";
    private int mTextColor = 0x99ffffff;
    private boolean mShowStartEndText = false;

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
        mMiddleArcPaint.setStrokeWidth(46);
        mMiddleArcPaint.setStyle(Paint.Style.STROKE);
        mMiddleArcPaint.setStrokeCap(Paint.Cap.ROUND);



        mCurrentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentTextPaint.setColor(Color.parseColor("#777777"));
        mCurrentTextPaint.setTextAlign(Paint.Align.CENTER);
        mCurrentTextPaint.setTypeface(Typeface.create(mFont, Typeface.NORMAL));

        mNeedle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedle.setStrokeWidth(18);
        mNeedle.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedle.setStrokeCap(Paint.Cap.ROUND);

        mLines = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLines.setStrokeWidth(22);
        mLines.setStyle(Paint.Style.FILL_AND_STROKE);
        mLines.setStrokeCap(Paint.Cap.ROUND);

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


    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawMiddleArc(canvas);
        drawOutterArc(canvas);
        drawCenterText(canvas);
    }

    private void drawCenterText(@NonNull Canvas canvas) {
        if (mShowText) {
            mCurrentTextPaint.setTextSize(dp2px(34));
            canvas.drawText(String.valueOf(mCurrentNum)+"%", getWidth()/2, getHeight()/2, mCurrentTextPaint);
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
        mMiddleArcPaint.setShader(new LinearGradient(0,0,0,750,getResources().getColor(R.color.red),getResources().getColor(R.color.purple), Shader.TileMode.MIRROR));
        canvas.drawArc(mMiddleRect, START_ANGLE, END_ANGLE, false, mMiddleArcPaint);

        canvas.drawLine(fromX, fromY + margin, toX, toY, mNeedle);
    }

    private void drawOutterArc(@NonNull Canvas canvas) {
        decreaseBy20--;
        if(!(decreaseBy20%20==0))
            return;

        mLines.setColor(Color.WHITE);

        float r = (radius - DEFAULT_PADDING) - arcDistance - arcDistance + 100;
        float s = (radius - DEFAULT_PADDING) - arcDistance - arcDistance;
        float toX = width / 2 + (float) Math.cos(Math.toRadians(mCurrentAngle + START_ANGLE)) * (r);
        float toY = width / 2 + (float) Math.sin(Math.toRadians(mCurrentAngle + START_ANGLE)) * (r);

        float fromX = width / 2 + (float) Math.cos(Math.toRadians(mCurrentAngle + START_ANGLE)) * (s);
        float fromY = width / 2 + (float) Math.sin(Math.toRadians(mCurrentAngle + START_ANGLE)) * (s);
        int margin = 0;

        canvas.drawLine(fromX, fromY + margin, toX, toY, mLines);
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

    public void animate(@FloatRange(from = 0.0f, to=1.0f) float progress) {
        ValueAnimator anim = ValueAnimator.ofFloat(this.progress, progress);
        if (this.progress > progress) {
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(750);
        } else {
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(500);
        }
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BeautifulCircularProgressBar.this.setProgress((float) valueAnimator.getAnimatedValue());
            }
        });
        anim.start();
    }

    public void setTextColor(@ColorInt int color) {
        mCurrentTextPaint.setColor(color);
    }
}
