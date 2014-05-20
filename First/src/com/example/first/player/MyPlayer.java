package com.example.first.player;

import com.example.first.R;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyPlayer extends Activity implements OnCompletionListener, OnInfoListener{
	 private AudioManager mAudioManager; 
	 private VideoView mVideoView;  
	 private MediaController mMediaController; 
	 private String mPath1="mms://officetv.bupt.edu.cn/CCTV-1"; 
	 //private String mPath="http://61.155.129.3/sohu/10//165/256/a2ZjNCBRh44eTndsvsuqa2.mp4?key=9E8r9wTELhzppRoUuMVph8qlOKSWYu_pJg6xpg..";
	 private String mPath="http://live.3gv.ifeng.com/live/hongkong.m3u8";
	 private String mTitle; 
	 private LinearLayout mLoadingView;
	 private TextView mLoadingText;
	    /** 声音 */  
	    private int mMaxVolume;  
	    /** 当前声音 */  
	    private int mVolume = -1;  
	    /** 当前亮度 */  
	    private float mBrightness = -1f;  
	    /** 当前缩放模式 */  
	    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;  
	    private GestureDetector mGestureDetector;  
	  @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        if (!LibsChecker.checkVitamioLibs(this))  
	            return;  
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.myplayer_layout);  
	        mVideoView = (VideoView) findViewById(R.id.myplayer_videoView);  
	        mLoadingView=(LinearLayout) findViewById(R.id.myplayer_progress);
	        mLoadingText=(TextView) findViewById(R.id.myplayer_progressBar_text);
	        mPath=getIntent().getStringExtra("address");
	        mTitle=getIntent().getStringExtra("name");
	        // ~~~ 绑定事件  
	        mVideoView.setOnCompletionListener(this);  
	        mVideoView.setOnInfoListener(this);  
	        
	        
	        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);  
	        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
	        
	        
	        if (mPath.startsWith("http:")){  
	            mVideoView.setVideoURI(Uri.parse(mPath));  
	        }  
	        else{  
	            mVideoView.setVideoPath(mPath);  
	        } 
	        
	        mMediaController = new MediaController(this);  
	        mMediaController.setFileName(mTitle);  
	        mVideoView.setMediaController(mMediaController);
	        mMediaController.setMediaPlayer(mVideoView);  
	        mVideoView.requestFocus();  
	        mVideoView.start();
	        
	        mGestureDetector = new GestureDetector(this, new MyGestureListener());  
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        
	        }
	  
	  
	  
	  
	  /** 是否自动恢复播放，用于自动暂停，恢复播放 */  
	    private boolean needResume;  
	  @Override  
	    public boolean onInfo(MediaPlayer arg0, int arg1, int down_rate) {  
	        switch (arg1) {  
	        case MediaPlayer.MEDIA_INFO_BUFFERING_START:  
	            //�?��缓存，暂停播�?  
	            if (isPlaying()) {  
	                stopPlayer();  
	                needResume = true;  
	            }  
	            mLoadingView.setVisibility(View.VISIBLE);  
	            break;  
	        case MediaPlayer.MEDIA_INFO_BUFFERING_END:  
	            //缓存完成，继续播�?  
	            if (needResume)  
	                startPlayer();  
	            mLoadingView.setVisibility(View.GONE);  
	            break;  
	        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:  
	            //显示 下载速度  
	            Log.e("test","download rate:" + down_rate);  
	           mLoadingText.setText("正在缓冲..."+"缓冲完成"+String.valueOf(down_rate));  
//	          mLoadingPerce.setText("正在缓冲�?.."+"缓冲完成�?+down_rate);  
	            //mListener.onDownloadRateChanged(arg2);  
	            break;  
	        }  
	        return true;  
	    }




	    @Override  
	    public void onCompletion(MediaPlayer player) {  
	        Log.e("播放完成", "播放完成");  
	    }  
	    private boolean isPlaying() {  
	        return mVideoView != null && mVideoView.isPlaying();  
	    }  
	    private void stopPlayer() {  
	        if (mVideoView != null)  
	            mVideoView.pause();  
	    }  
	  
	    private void startPlayer() {  
	        if (mVideoView != null)  
	            mVideoView.start();  
	    }  
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        if (mVideoView != null)  
	            mVideoView.stopPlayback();  
	    }  
	    
	    
	    @Override  
	    public boolean onTouchEvent(MotionEvent event) {  
	        if (mGestureDetector.onTouchEvent(event))  
	            return true;  
	  
	        // 处理手势结束  
	        switch (event.getAction() & MotionEvent.ACTION_MASK) {  
	        case MotionEvent.ACTION_UP:  
	            endGesture();  
	            break;  
	        }  
	  
	        return super.onTouchEvent(event);  
	    }  
	  
	    /** 手势结束 */  
	    private void endGesture() {  
	        mVolume = -1;  
	        mBrightness = -1f;  
	  
	        // 隐藏  
	        mDismissHandler.removeMessages(0);  
	        mDismissHandler.sendEmptyMessageDelayed(0, 500);  
	    }  
	  
	    private class MyGestureListener extends SimpleOnGestureListener {  
	  
	        /** 双击 */  
	        @Override  
	        public boolean onDoubleTap(MotionEvent e) {  
	            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)  
	                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;  
	            else  
	                mLayout++;  
	            if (mVideoView != null)  
	                mVideoView.setVideoLayout(mLayout, 0);  
	            return true;  
	        }  
	  
	        /** 滑动 */  
	        @SuppressWarnings("deprecation")  
	        @Override  
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
	            float mOldX = e1.getX(), mOldY = e1.getY();  
	            int y = (int) e2.getRawY();  
	            Display disp = getWindowManager().getDefaultDisplay();  
	            int windowWidth = disp.getWidth();  
	            int windowHeight = disp.getHeight();  
	  
	            if (mOldX > windowWidth * 4.0 / 5)// 右边滑动  
	                onVolumeSlide((mOldY - y) / windowHeight);  
	            else if (mOldX < windowWidth / 5.0)// 左边滑动  
	                onBrightnessSlide((mOldY - y) / windowHeight);  
	  
	            return super.onScroll(e1, e2, distanceX, distanceY);  
	        }  
	    }  
	  
	    /** 定时隐藏 */  
	    private Handler mDismissHandler = new Handler() {  
	        @Override  
	        public void handleMessage(Message msg) {  
	     /*       mVolumeBrightnessLayout.setVisibility(View.GONE); */ 
	        }  
	    };  
	  
	    /** 
	     * 滑动改变声音大小 
	     *  
	     * @param percent 
	     */  
	    private void onVolumeSlide(float percent) {  
	        if (mVolume == -1) {  
	            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);  
	            if (mVolume < 0)  
	                mVolume = 0;  
	  
	            // 显示  
/*	            mOperationBg.setImageResource(R.drawable.video_volumn_bg);  
	            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);  */
	        }  
	  
	        int index = (int) (percent * mMaxVolume) + mVolume;  
	        if (index > mMaxVolume)  
	            index = mMaxVolume;  
	        else if (index < 0)  
	            index = 0;  
	  
	        // 变更声音  
	        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);  
	  
	        // 变更进度�?  
/*	        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();  
	        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;  
	        mOperationPercent.setLayoutParams(lp); */ 
	    }  
	  
	    /** 
	     * 滑动改变亮度 
	     *  
	     * @param percent 
	     */  
	    private void onBrightnessSlide(float percent) {  
	        if (mBrightness < 0) {  
	            mBrightness = getWindow().getAttributes().screenBrightness;  
	            if (mBrightness <= 0.00f)  
	                mBrightness = 0.50f;  
	            if (mBrightness < 0.01f)  
	                mBrightness = 0.01f;  
	  
	            // 显示  
/*	            mOperationBg.setImageResource(R.drawable.video_brightness_bg);  
	            mVolumeBrightnessLayout.setVisibility(View.VISIBLE); */ 
	        }  
	        WindowManager.LayoutParams lpa = getWindow().getAttributes();  
	        lpa.screenBrightness = mBrightness + percent;  
	        if (lpa.screenBrightness > 1.0f)  
	            lpa.screenBrightness = 1.0f;  
	        else if (lpa.screenBrightness < 0.01f)  
	            lpa.screenBrightness = 0.01f;  
	        getWindow().setAttributes(lpa);  
	  
/*	        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();  
	        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);  
	        mOperationPercent.setLayoutParams(lp);  */
	    }  
	  
	    @Override  
	    public void onConfigurationChanged(Configuration newConfig) {  
	        if (mVideoView != null)  
	            mVideoView.setVideoLayout(mLayout, 0);  
	        super.onConfigurationChanged(newConfig);  
	    }  
}
