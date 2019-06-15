package com.afei.camera2getpreview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.afei.camera2getpreview.util.Camera2Proxy;

public class Camera2View extends TextureView {

    private static final String TAG = "Camera2View";
    private Camera2Proxy mCameraProxy;
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public Camera2View(Context context) {
        this(context, null);
    }

    public Camera2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera2View(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Camera2View(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mCameraProxy = new Camera2Proxy((Activity) context);
    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    public Camera2Proxy getCameraProxy() {
        return mCameraProxy;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

}
