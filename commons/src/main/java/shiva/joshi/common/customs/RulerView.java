package shiva.joshi.common.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.text.DecimalFormat;
import java.util.List;

import shiva.joshi.common.R;


/**
 * The type Ruler view.
 */
public class RulerView extends View implements GestureDetector.OnGestureListener {

    private Paint mRulerPaint;

    private TextPaint mTextPaint;

    private int mSelectedIndex = 0;

    private int mHighlightColor = Color.RED;

    private int mBigStepsLineColor = Color.BLUE;

    private int mTextColor = Color.BLACK;

    private int mRulerColor = Color.BLACK;


    private int mRulerHeight;

    private int mRulerWidth;

    private List<String> mTextList;

    private float mMaxOverScrollDistance;

    private int mViewScopeSize;

    private float mContentLength;

    private boolean mFling = false;

    private float mMaxValue, mMinValue;

    private float mIntervalValue = 1f;

    private float mIntervalDistance = 0f;


    private int mRulerCount;

    private float mTextSize;

    private float mRulerLineWidth;


    private float mRulerLineHeight;

    private int mRetainLength = 0;

    private boolean mIsDivideByTen = true;

    private float mDivideByTenHeight;

    private float mDivideByTenWidth;


    private boolean mIsDivideByFive = false;

    private float mDivideByFiveHeight;

    private float mDivideByFiveWidth;


    private float mTextBaseLineDistance;

    private int mOrientation = 0;

    private OnValueChangeListener onValueChangeListener;

    private final String HEIGHT_UNIT = " CM";
    private final String WEIGHT_UNIT = " Kg";
    /**
     * The constant HORIZONTAL.
     */
    public static final int HORIZONTAL = 0;
    /**
     * The constant VERTICAL.
     */
    public static final int VERTICAL = 1;
    private Scroller mScroller;

    private GestureDetectorCompat mGestureDetectorCompat;

    /**
     * The interface On value change listener.
     */
    public interface OnValueChangeListener {
        /**
         * On change.
         *
         * @param view  the view
         * @param value the value
         */
        void onChange(RulerView view, float value);
    }

    /**
     * Instantiates a new Ruler view.
     *
     * @param context the context
     */
    public RulerView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Ruler view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();


        mRulerLineWidth = dm.density * 2;
        mDivideByFiveWidth = dm.density * 3;
        mDivideByTenWidth = dm.density * 4;

        mRulerLineHeight = dm.density * 15;
        mDivideByFiveHeight = dm.density * 20;
        mDivideByTenHeight = dm.density * 30;

        mIntervalDistance = dm.density * 8;
        mTextSize = dm.scaledDensity * 15;


        TypedArray typedArray = attrs == null ? null : getContext()
                .obtainStyledAttributes(attrs, R.styleable.customRulerView);
        if (typedArray != null) {
            mOrientation = typedArray.getInt(R.styleable.customRulerView_rulerOrientation, HORIZONTAL);
            mHighlightColor = typedArray
                    .getColor(R.styleable.customRulerView_rulerHighlightColor,
                            mHighlightColor);

            mBigStepsLineColor = typedArray
                    .getColor(R.styleable.customRulerView_rulerBigStepsLineColor,
                            mBigStepsLineColor);

            mTextColor = typedArray.getColor(
                    R.styleable.customRulerView_rulerTextColor, mTextColor);
            mRulerColor = typedArray.getColor(R.styleable.customRulerView_rulerColor,
                    mRulerColor);
            mIntervalValue = typedArray
                    .getFloat(R.styleable.customRulerView_rulerIntervalValue,
                            mIntervalValue);
            mMaxValue = typedArray
                    .getFloat(R.styleable.customRulerView_rulerMaxValue,
                            mMaxValue);
            mMinValue = typedArray
                    .getFloat(R.styleable.customRulerView_rulerMinValue, mMinValue);
            mTextSize = typedArray.getDimension(
                    R.styleable.customRulerView_rulerTextSize,
                    mTextSize);
            mRulerLineWidth = typedArray.getDimension(
                    R.styleable.customRulerView_rulerLineWidth, mRulerLineWidth);
            mIntervalDistance = typedArray.getDimension(R.styleable.customRulerView_rulerIntervalDistance, mIntervalDistance);
            mRetainLength = typedArray.getInteger(R.styleable.customRulerView_rulerRetainLength, 0);


            mRulerLineHeight = typedArray.getDimension(
                    R.styleable.customRulerView_rulerLineHeight, mRulerLineHeight);

            mIsDivideByFive = typedArray.getBoolean(R.styleable.customRulerView_rulerIsDivideByFive, mIsDivideByFive);
            mDivideByFiveHeight = typedArray.getDimension(
                    R.styleable.customRulerView_rulerDivideByFiveHeight, mDivideByFiveHeight);
            mDivideByFiveWidth = typedArray.getDimension(
                    R.styleable.customRulerView_rulerDivideByFiveWidth, mDivideByFiveWidth);

            mIsDivideByTen = typedArray.getBoolean(R.styleable.customRulerView_rulerIsDivideByTen, mIsDivideByTen);
            mDivideByTenHeight = typedArray.getDimension(
                    R.styleable.customRulerView_rulerDivideByTenHeight, mDivideByTenHeight);
            mDivideByTenWidth = typedArray.getDimension(R.styleable.customRulerView_rulerDivideByTenWidth, mDivideByTenWidth);

            mTextBaseLineDistance = typedArray.getDimension(
                    R.styleable.customRulerView_rulerTextBaseLineDistance,
                    mTextBaseLineDistance);
        }
        typedArray.recycle();
        checkRulerLineParam();
        calculateTotal();

        mGestureDetectorCompat = new GestureDetectorCompat(getContext(), this);
        mScroller = new Scroller(getContext());

        mRulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRulerPaint.setStrokeWidth(mRulerLineWidth);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);

        setSelectedIndex(mRulerCount / 2);
    }

    private void checkRulerLineParam() {
        float[] heights = new float[]{mRulerLineHeight, mDivideByFiveHeight, mDivideByTenHeight};
        float[] weights = new float[]{mRulerLineWidth, mDivideByFiveWidth, mDivideByTenWidth};

        for (int i = 0; i < heights.length; i++) {
            float heightTemp;
            float weightTemp;
            for (int j = 0; j < heights.length - i - 1; j++) {
                if (heights[j] > heights[j + 1]) {
                    heightTemp = heights[j];
                    heights[j] = heights[j + 1];
                    heights[j + 1] = heightTemp;
                }
                if (weights[j] > weights[j + 1]) {
                    weightTemp = weights[j];
                    weights[j] = weights[j + 1];
                    weights[j + 1] = weightTemp;
                }
            }
        }
        mRulerLineHeight = heights[0];
        mDivideByFiveHeight = heights[1];
        mDivideByTenHeight = heights[2];

        mRulerLineWidth = weights[0];
        mDivideByFiveWidth = weights[1];
        mDivideByTenWidth = weights[2];

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }


    private int measureWidth(int widthMeasureSpec) {

        int measureMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = MeasureSpec.getSize(widthMeasureSpec);


        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize;
                break;
            default:
                break;
        }
        return result;
    }


    private int measureHeight(int heightMeasure) {
        int measureMode = MeasureSpec.getMode(heightMeasure);
        int measureSize = MeasureSpec.getSize(heightMeasure);
        int result;
        if (mOrientation == HORIZONTAL) {
            result = (int) (mTextSize) * 4;
        } else {
            result = getSuggestedMinimumHeight();
        }
        switch (measureMode) {

            case MeasureSpec.EXACTLY:
                result = Math.max(result, measureSize);
                break;

            case MeasureSpec.AT_MOST:
                result = Math.min(result, measureSize);
                break;
            default:
                break;
        }
        return result;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w != oldw || h != oldh) {

            if (mOrientation == HORIZONTAL) {
                mRulerHeight = h;
                mMaxOverScrollDistance = w / 2.f;
            } else {
                mRulerWidth = w;
                mMaxOverScrollDistance = h / 2.f;
            }

            mContentLength = ((mMaxValue - mMinValue) / mIntervalValue)
                    * mIntervalDistance;
            mViewScopeSize = (int) Math.ceil(mMaxOverScrollDistance
                    / mIntervalDistance);
        }


    }

    private Rect mRect;
    private DecimalFormat mDecimalFormat;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRect == null) {
            mRect = new Rect();
        }
        if (mDecimalFormat == null) {
            mDecimalFormat = new DecimalFormat("##0");
        }

        int start = mSelectedIndex - mViewScopeSize;
        int end = mSelectedIndex + mViewScopeSize;
        if (mSelectedIndex == mMaxValue) {
            end += mViewScopeSize;
        } else if (mSelectedIndex == mMinValue) {
            start -= mViewScopeSize;
        }


        if (mDivideByTenWidth >= mIntervalDistance) {
            mRulerLineWidth = mIntervalDistance / 6;
            mDivideByFiveWidth = mIntervalDistance / 3;
            mDivideByTenWidth = mIntervalDistance / 2;
        }

        mRulerPaint.setStrokeCap(Paint.Cap.ROUND);

        if (mOrientation == HORIZONTAL) {
            float x = start * mIntervalDistance;

            float markHeight = mRulerHeight - mTextSize;

            if (mDivideByTenHeight + mTextBaseLineDistance > markHeight) {
                mRulerLineHeight = markHeight / 2;
                mDivideByFiveHeight = markHeight * 3 / 4;
                mDivideByTenHeight = markHeight;
                mTextBaseLineDistance = 0;
            }


            for (int i = start; i < end; i++) {
                if (mRulerCount > 0 && i >= 0 && i < mRulerCount) {

                    int remainderBy2 = i % 2;
                    int remainderBy5 = i % 5;

                  /*  if (i == mSelectedIndex) {
                        mRulerPaint.setColor(mHighlightColor);
                    } else {*/
                        mRulerPaint.setColor(mRulerColor);
                   // }

                    //Tenth line
                    if (mIsDivideByTen && remainderBy2 == 0 && remainderBy5 == 0) {
                        mRulerPaint.setColor(mBigStepsLineColor);
                        mRulerPaint.setStrokeWidth(mDivideByTenWidth);
                        canvas.drawLine(x,canvas.getHeight()-mDivideByTenHeight, x,canvas.getHeight(),
                                mRulerPaint);
                    } else if (mIsDivideByFive && remainderBy2 != 0 && remainderBy5 == 0) {
                        mRulerPaint.setStrokeWidth(mDivideByFiveWidth);
                        canvas.drawLine(x,canvas.getHeight()-mDivideByFiveHeight, x,canvas.getHeight(), mRulerPaint);
                    } else {
                        mRulerPaint.setStrokeWidth(mRulerLineWidth);
                        canvas.drawLine(x,canvas.getHeight()-mRulerLineHeight, x, canvas.getHeight(),
                                mRulerPaint);
                    }

                    mTextPaint.setColor(mTextColor);

                    /*if (mSelectedIndex == i) {
                        mTextPaint.setColor(mHighlightColor);
                    }*/
                    if (i % 10 == 0) {
                        String text = null;
                        if (mTextList != null && mTextList.size() > 0) {
                            int index = i / 10;
                            if (index < mTextList.size()) {
                                text = mTextList.get(index)+WEIGHT_UNIT;
                            } else {
                                text = "";
                            }

                        } else {
                            text = mDecimalFormat.format(i * mIntervalValue + mMinValue)+WEIGHT_UNIT;
                        }
                        mTextPaint.getTextBounds(text, 0, text.length(), mRect);

                        // mTextBaseLineDistance
                        canvas.drawText(text, 0, text.length(), x,canvas.getHeight()- mDivideByTenHeight - mRect.height() , mTextPaint);
                    }
                }
                x += mIntervalDistance;
            }
        } else {
            float y = start * mIntervalDistance;

            float markWidth = mRulerWidth - mTextSize;

            if (mDivideByTenHeight + mTextBaseLineDistance > markWidth) {
                mRulerLineHeight = markWidth / 2;
                mDivideByFiveHeight = markWidth * 3 / 4;
                mDivideByTenHeight = markWidth;
                mTextBaseLineDistance = 0;
            }


            for (int i = start; i < end; i++) {
                if (mRulerCount > 0 && i >= 0 && i < mRulerCount) {

                    int remainderBy2 = i % 2;
                    int remainderBy5 = i % 5;

                    if (i == mSelectedIndex) {
                        mRulerPaint.setColor(mHighlightColor);
                    } else {
                        mRulerPaint.setColor(mRulerColor);
                    }

                    //Tenth line
                    if (mIsDivideByTen && remainderBy2 == 0 && remainderBy5 == 0) {

                        mRulerPaint.setColor(mBigStepsLineColor);
                        mRulerPaint.setStrokeWidth(mDivideByTenWidth);
                        canvas.drawLine(canvas.getWidth() - mDivideByTenHeight, y, canvas.getWidth(), y,
                                mRulerPaint);
                    } else if (mIsDivideByFive && remainderBy2 != 0 && remainderBy5 == 0) {
                        mRulerPaint.setStrokeWidth(mDivideByFiveWidth);
                        canvas.drawLine(canvas.getWidth() - mDivideByFiveHeight, y, canvas.getWidth(), y, mRulerPaint);
                    } else {
                        //mRulerLineHeight
                        mRulerPaint.setStrokeWidth(mRulerLineWidth);
                      canvas.drawLine(canvas.getWidth() - mRulerLineHeight, y, canvas.getWidth(), y, mRulerPaint);
                    }
                    // Longer Line for selected height
                    if (i == mSelectedIndex) {
                        mRulerPaint.setColor(mHighlightColor);
                        //canvas.drawLine(0, y, canvas.getWidth(), y, mRulerPaint);
                    }


                    mTextPaint.setColor(mTextColor);

                   /* if (mSelectedIndex == i) {
                        mTextPaint.setColor(mHighlightColor);
                    }*/
                    if (i % 10 == 0) {
                        String text = null;
                        if (mTextList != null && mTextList.size() > 0) {
                            int index = i / 10;
                            if (index < mTextList.size()) {
                                text = mTextList.get(index) + HEIGHT_UNIT;
                            } else {
                                text = "";
                            }

                        } else {

                            text = mDecimalFormat.format(mMaxValue - i * mIntervalValue) + HEIGHT_UNIT;
                        }
                        mTextPaint.getTextBounds(text, 0, text.length(), mRect);

                        canvas.drawText(text, 0, text.length(), mDivideByTenHeight + mTextBaseLineDistance, y + mRect.height() + mDivideByTenWidth, mTextPaint);
                    }
                }
                y += mIntervalDistance;
            }
        }
    }

    private String format(float value) {
        switch (mRetainLength) {
            case 0:
                return new DecimalFormat("##0").format(value);
            case 1:
                return new DecimalFormat("##0.0").format(value);
            case 2:
                return new DecimalFormat("##0.00").format(value);
            case 3:
                return new DecimalFormat("##0.000").format(value);
            default:
                return new DecimalFormat("##0.0").format(value);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean resolve = mGestureDetectorCompat.onTouchEvent(event);
        if (!mFling && MotionEvent.ACTION_UP == event.getAction()) {
            adjustPosition();
            resolve = true;
        }
        return resolve || super.onTouchEvent(event);
    }


    private void adjustPosition() {
        int scroll;
        if (mOrientation == HORIZONTAL) {
            scroll = getScrollX();
        } else {
            scroll = getScrollY();
        }
        float distance = mSelectedIndex * mIntervalDistance - scroll
                - mMaxOverScrollDistance;
        if (distance == 0) {
            return;
        }
        if (mOrientation == HORIZONTAL) {
            mScroller.startScroll(scroll, 0, (int) distance, 0);
        } else {
            mScroller.startScroll(0, scroll, 0, (int) distance);
        }
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            onValueChange();
            invalidate();
        } else {

            if (mFling) {
                mFling = false;
                adjustPosition();
            }
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(false);
        }
        mFling = false;
        if (null != getParent()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        float scroll = 0;
        float distance = 0;
        if (mOrientation == HORIZONTAL) {
            scroll = getScrollX();
            distance = distanceX;
        } else {
            scroll = getScrollY();
            distance = distanceY;
        }


        if (scroll + distance <= -mMaxOverScrollDistance) {
            distance = -(int) (scroll + mMaxOverScrollDistance);
        } else if (scroll + distance >= mContentLength - mMaxOverScrollDistance) {
            distance = (int) (mContentLength - mMaxOverScrollDistance - scroll);
        }

        if (distance == 0) {
            return true;
        }
        if (mOrientation == HORIZONTAL) {
            scrollBy((int) distance, 0);

        } else {
            scrollBy(0, (int) distance);
        }
        onValueChange();
        return true;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        float scroll = 0;
        float velocity = 0;
        if (mOrientation == HORIZONTAL) {
            scroll = getScrollX();
            velocity = velocityX;
        } else {
            scroll = getScrollY();
            velocity = velocityY;
        }

        if (scroll < -mMaxOverScrollDistance
                || scroll > mContentLength - mMaxOverScrollDistance) {
            return false;
        }
        mFling = true;
        fling((int) -velocity / 2);
        return true;
    }

    private void fling(int velocity) {
        if (mOrientation == HORIZONTAL) {
            mScroller.fling(getScrollX(), 0, velocity, 0, (int) -mMaxOverScrollDistance, (int) (mContentLength - mMaxOverScrollDistance), 0, 0);
        } else {
            mScroller.fling(0, getScrollY(), 0, velocity, 0, 0, (int) -mMaxOverScrollDistance, (int) (mContentLength - mMaxOverScrollDistance));

        }

        ViewCompat.postInvalidateOnAnimation(this);
    }

    //Value change listener
    private void onValueChange() {
        int offset = 0;

        if (mOrientation == HORIZONTAL) {
            offset = (int) (getScrollX() + mMaxOverScrollDistance);
        } else {
            offset = (int) (getScrollY() + mMaxOverScrollDistance);
        }
        int tempIndex = Math.round(offset / mIntervalDistance);
        tempIndex = clampSelectedIndex(tempIndex);
        mSelectedIndex = tempIndex;
        if (onValueChangeListener != null) {
            String str = null;
            if (mOrientation == HORIZONTAL) {

                str = format(mSelectedIndex * mIntervalValue
                        + mMinValue);
            } else {
                str = format(mMaxValue - mSelectedIndex * mIntervalValue
                );
            }
            float mValue = Float.parseFloat(str);
            onValueChangeListener.onChange(this, mValue);
        }
    }


    private int clampSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0) {
            selectedIndex = 0;
        } else if (selectedIndex > mRulerCount) {
            selectedIndex = mRulerCount - 1;
        }
        return selectedIndex;
    }

    private void setSelectedIndex(int selectedIndex) {
        this.mSelectedIndex = clampSelectedIndex(selectedIndex);
        post(new Runnable() {
            @Override
            public void run() {
                int position = (int) (mSelectedIndex * mIntervalDistance - mMaxOverScrollDistance);
                if (mOrientation == HORIZONTAL) {
                    scrollTo(position, 0);
                } else {
                    scrollTo(0, position);
                }
                onValueChange();
                invalidate();

            }
        });
    }


    private void calculateTotal() {
        mRulerCount = (int) ((mMaxValue - mMinValue) / mIntervalValue) + 1;
    }


    /**
     * Sets selected value.
     *
     * @param selectedValue the selected value
     */
    public void setSelectedValue(float selectedValue) {
        if (selectedValue < mMinValue) {
            selectedValue = mMinValue;
        } else if (selectedValue > mMaxValue) {
            selectedValue = mMaxValue;
        }
        int index = Math.round(((selectedValue - mMinValue) / mIntervalValue));

        if (mOrientation == VERTICAL) {
            index = mRulerCount - index - 1;
        }
        setSelectedIndex(index);
    }


    /**
     * Sets min value.
     *
     * @param mMinValue the m min value
     */
    public void setMinValue(float mMinValue) {
        this.mMinValue = mMinValue;
        calculateTotal();
        invalidate();
    }

    /**
     * Sets interval value.
     *
     * @param mIntervalValue the m interval value
     */
    public void setIntervalValue(float mIntervalValue) {
        this.mIntervalValue = mIntervalValue;
        calculateTotal();
        invalidate();
    }

    /**
     * Sets interval dis.
     *
     * @param mIntervalDis the m interval dis
     */
    public void setIntervalDis(float mIntervalDis) {
        this.mIntervalDistance = mIntervalDis;
    }

    /**
     * Sets highlight color.
     *
     * @param mHighlightColor the m highlight color
     */
    public void setHighlightColor(int mHighlightColor) {
        this.mHighlightColor = mHighlightColor;
    }


    /**
     * Sets mark text color.
     *
     * @param mMarkTextColor the m mark text color
     */
    public void setMarkTextColor(int mMarkTextColor) {
        this.mTextColor = mMarkTextColor;
    }


    /**
     * Sets mark color.
     *
     * @param mMarkColor the m mark color
     */
    public void setMarkColor(int mMarkColor) {
        this.mRulerColor = mMarkColor;
    }


    /**
     * Sets text list.
     *
     * @param mTextList the m text list
     */
    public void setTextList(List<String> mTextList) {
        this.mTextList = mTextList;
    }


    /**
     * Sets max value.
     *
     * @param mMaxValue the m max value
     */
    public void setMaxValue(float mMaxValue) {
        this.mMaxValue = mMaxValue;
        calculateTotal();
        invalidate();
    }

    /**
     * Sets retain length.
     *
     * @param retainLength the retain length
     */
    public void setRetainLength(int retainLength) {

        if (retainLength < 1) {
            retainLength = 1;
        } else if (retainLength > 3) {
            retainLength = 3;
        }
        this.mRetainLength = retainLength;
        invalidate();
    }

    /**
     * Sets on value change listener.
     *
     * @param onValueChangeListener the on value change listener
     */
    public void setOnValueChangeListener(
            OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

}