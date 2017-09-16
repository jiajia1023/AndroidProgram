package com.mylibrary.zxing;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mylibrary.R;
import com.mylibrary.fragments.BaseFragment;
import com.mylibrary.utils.ScreenUtil;
import com.mylibrary.zxing.camera.CameraManager;
import com.mylibrary.zxing.decoding.CaptureActivityHandler;
import com.mylibrary.zxing.decoding.InactivityTimer;
import com.mylibrary.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class MipcaFragmentCapture extends BaseFragment implements Callback {

    ViewfinderView viewfinderView;
    SurfaceView surfaceView;

    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    //ViewPagew 顶部的指针  ViewPager的高度如果设置成match_parent 就会包含这个指针，要减去这个指针的高度
    private View indicator;

    int[] screeWH;
    private OnQRScanFinishListener onQRScanFinishListener;

    public void setOnQRScanFinishListener(OnQRScanFinishListener onQRScanFinishListener) {
        this.onQRScanFinishListener = onQRScanFinishListener;
    }

    @Override
    public int onResultLayoutResId() {
        return R.layout.activity_capture;
    }

    @Override
    public void onBindData() {
        screeWH = ScreenUtil.getScreenWH(getActivity());
        viewfinderView = (ViewfinderView) baseLayout.findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) baseLayout.findViewById(R.id.preview_view);

        init();
        onResumeWork();
    }

    private void init() {
        CameraManager.init(getActivity());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
    }


    /**
     * 如果是ViewPager 请设置指针这个View
     *
     * @param indicator
     */
    public void setIndicator(View indicator) {
        this.indicator = indicator;
    }


    protected void onResumeWork() {

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        onPauseWork();
    }

    protected void onPauseWork() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
            CameraManager.get().closeDriver();
        }
    }

    /**
     * 扫描完成后，让二维码扫描继续工作
     *
     * @param DelayedTime 延迟的时间
     */
    public void previewQRScan(int DelayedTime) {
        if (DelayedTime > 0) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, DelayedTime);
        } else {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, 1000);
        }

    }

    @Override
    public void onDestroyView() {
        inactivityTimer.shutdown();
        super.onDestroyView();
    }


    /**
     * 返回数据
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            if (this.onQRScanFinishListener != null) {
                this.onQRScanFinishListener.onScanFailed(result, barcode);
            }
        } else {
            if (this.onQRScanFinishListener != null) {
                this.onQRScanFinishListener.onScanSucceed(result, barcode);
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            int width = screeWH[0];
            int height = screeWH[1];
//            int width = getView().getMeasuredWidth();
//            int height = getView().getMeasuredHeight();
            if (indicator != null) {
                height = height - indicator.getHeight();
            }
            Point screenResolution = new Point(width, height);
            CameraManager.MAX_FRAME_WIDTH = screenResolution.x * 3 / 4;
            CameraManager.MAX_FRAME_HEIGHT = screenResolution.y / 2;
            CameraManager.get().openDriver(surfaceHolder, screenResolution);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    public interface OnQRScanFinishListener {
        void onScanSucceed(Result result, Bitmap barcode);

        void onScanFailed(Result result, Bitmap barcode);

    }

}