package com.afei.camera2getpreview;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afei.camera2getpreview.util.Camera2Proxy;
import com.afei.camera2getpreview.util.ColorConvertUtil;
import com.afei.camera2getpreview.util.FileUtil;

import java.nio.ByteBuffer;

public class CameraFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CameraFragment";

    private ImageView mCloseIv;
    private ImageView mSwitchCameraIv;
    private ImageView mTakePictureIv;
    private Camera2View mCameraView;

    private Camera2Proxy mCameraProxy;

    private byte[] mYuvBytes;
    private boolean mIsShutter;

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            mCameraProxy.openCamera();
            mCameraProxy.setPreviewSurface(texture);
            // 根据相机预览设置View大小，避免显示变形
            Size previewSize = mCameraProxy.getPreviewSize();
            mCameraView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = reader -> {
        Image image = reader.acquireLatestImage();
        if (image == null) {
            return;
        }
        processImage(image);
        image.close();  // 一定不能忘记close
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        mCloseIv = rootView.findViewById(R.id.toolbar_close_iv);
        mSwitchCameraIv = rootView.findViewById(R.id.toolbar_switch_iv);
        mTakePictureIv = rootView.findViewById(R.id.take_picture_iv);
        mCameraView = rootView.findViewById(R.id.camera_view);
        mCameraProxy = mCameraView.getCameraProxy();

        mCloseIv.setOnClickListener(this);
        mSwitchCameraIv.setOnClickListener(this);
        mTakePictureIv.setOnClickListener(this);
        mCameraProxy.setImageAvailableListener(mOnImageAvailableListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCameraView.isAvailable()) {
            mCameraProxy.openCamera();
        } else {
            mCameraView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraProxy.releaseCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_close_iv:
                getActivity().finish();
                break;
            case R.id.toolbar_switch_iv:
                mCameraProxy.switchCamera();
                break;
            case R.id.take_picture_iv:
                mIsShutter = true;
                break;
        }
    }


    private void processImage(Image image) {
        int width = mCameraProxy.getPreviewSize().getWidth();
        int height = mCameraProxy.getPreviewSize().getHeight();
        if (mYuvBytes == null) {
            // YUV420 大小总是 width * height * 3 / 2
            mYuvBytes = new byte[width * height * 3 / 2];
        }

        ColorConvertUtil.getI420FromImage(image, mYuvBytes);

        if (mIsShutter) {
            mIsShutter = false;
            // save yuv data
            String yuvPath = FileUtil.SAVE_DIR + System.currentTimeMillis() + ".yuv";
            FileUtil.saveBytes(mYuvBytes, yuvPath);
            // save bitmap data
            String jpgPath = yuvPath.replace(".yuv", ".jpg");
            Bitmap bitmap = ColorConvertUtil.yuv420pToBitmap(mYuvBytes, width, height);
            FileUtil.saveBitmap(bitmap, jpgPath);
            bitmap.recycle();
        }
    }
}
