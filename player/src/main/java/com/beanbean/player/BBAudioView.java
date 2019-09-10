package com.beanbean.player;

import android.content.Context;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanbean.player.utils.DateUtil;

import java.io.IOException;

/**
 * @author wugx
 * It is a small player for audio view {@link }
 */
public class BBAudioView extends RelativeLayout implements View.OnClickListener {
    private final static String TAG = BBAudioView.class.getSimpleName();
    private Context mContext;

    private ImageView iv_play_or_pause;
    private ProgressBar pro_prepareLoad, pro_progress;
    private TextView tv_current_time;

    private final static int MSG_UPDATE_SECOND_PROGRESS = 1;
    private final static int MSG_UPDATE_PROGRESS = 2;
    private boolean showSecondProgress = false;
    MediaPlayer mMediaPlayer;
    private boolean isPlaying = false;
    private boolean isPrepare = false;
    private boolean isTmpPause = false;
    UpdateMediaInfo updateMediaInfo;
    private int hasCode;

    public BBAudioView(Context context) {
        this(context, null);
    }

    public BBAudioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BBAudioView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BBAudioView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        hasCode = this.hashCode();
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.ap_layout_bbaudio, this);
        iv_play_or_pause = findViewById(R.id.iv_play_or_pause);
        pro_prepareLoad = findViewById(R.id.pro_prepareLoad);
        pro_progress = findViewById(R.id.pro_progress);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_current_time.setText(DateUtil.timestampToDate(-1));
        iv_play_or_pause.setOnClickListener(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(onPreparedListener);
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mMediaPlayer.setOnCompletionListener(onCompletionListener);
        updateMediaInfo = new UpdateMediaInfo();
        AudioPlayerHelper.getInstance().addAudioPlayListener(mAudioPlayListener);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_play_or_pause) {
            checkMediaPlayerNotNull();
            if (mMediaPlayer.isPlaying()) {
                AudioPlayerHelper.getInstance().pausePlay(hasCode);
            } else {
                AudioPlayerHelper.getInstance().startPlay(hasCode);
            }

        }
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_SECOND_PROGRESS:
                    int pro = msg.arg1;
                    pro_progress.setSecondaryProgress(pro * 10);
                    break;
                case MSG_UPDATE_PROGRESS:
                    updateView();
                    if (isPlaying) {
                        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void updateView() {
        if (!isPrepare) return;
        long currentPro = mMediaPlayer.getCurrentPosition();
        long totalTime = mMediaPlayer.getDuration();
        iv_play_or_pause.setSelected(isPlaying);
        tv_current_time.setText(DateUtil.timestampToDate(totalTime - currentPro));
        pro_progress.setProgress(getPercent(currentPro, totalTime));
    }

    private int getPercent(long currentTime, long totalTime) {
        if (currentTime <= 0 || totalTime <= 0) return 0;
        return (int) (currentTime * 1000 / totalTime);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            if (isPlaying){
                isTmpPause = true;
                pause();
            }
        }else {
            if (!isPlaying && isTmpPause){
                isTmpPause = false;
                start();
            }
        }
    }

    /**
     * 设置视频路径
     *
     * @param path
     */
    public void setDataUrl(String path) {
        invalidate();
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("视频路径不能为空！");
        }
        if (!updateMediaInfo.isCancelled() && updateMediaInfo.getStatus() == AsyncTask.Status.PENDING) {
            updateMediaInfo.execute(path);
        } else {
            updateMediaInfo = null;
            updateMediaInfo = new UpdateMediaInfo();
            updateMediaInfo.execute(path);
        }
    }

    /**
     * 设置进度条样式
     *
     * @param id
     */
    public void setProgressDraw(int id) {
        pro_progress.setProgressDrawable(getResources().getDrawable(id));
    }

    /**
     * 是否显示缓冲进度条
     *
     * @param flag
     */
    public void setShowSecondProgress(boolean flag) {
        showSecondProgress = flag;
    }

    /**
     * 播放
     */
    public void start() {
        checkMediaPlayerNotNull();
        mMediaPlayer.start();
        isPlaying = true;
        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
    }

    /**
     * 暂停
     */
    public void pause() {
        checkMediaPlayerNotNull();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        isPlaying = false;
        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        mHandler.sendEmptyMessage(MSG_UPDATE_PROGRESS);
    }

    /**
     * 重置进度为0
     */
    public void resetPos() {
        checkMediaPlayerNotNull();
        if (isPrepare) {
            mMediaPlayer.seekTo(0);
        }
    }

    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            isPrepare = true;
            pro_prepareLoad.setVisibility(GONE);
            //iv_play_or_pause.setEnabled(true);
            iv_play_or_pause.setVisibility(VISIBLE);
            long currentPro = mMediaPlayer.getCurrentPosition();
            long totalTime = mMediaPlayer.getDuration();
            tv_current_time.setText(DateUtil.timestampToDate(totalTime - currentPro));
            pro_progress.setProgress(getPercent(currentPro, totalTime));
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (showSecondProgress) {
                mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_SECOND_PROGRESS, percent, -1));
            }
        }
    };

    private AudioPlayerHelper.AudioPlayListener mAudioPlayListener = new AudioPlayerHelper.AudioPlayListener() {
        @Override
        public void onStart(int code) {
            if (code == hasCode) {
                start();
            } else {
                resetPos();
                pause();
            }
        }

        @Override
        public void onPause(int code) {
            if (code == hasCode) {
                pause();
            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            isPlaying = false;
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
            long totalTime = mMediaPlayer.getDuration();
            tv_current_time.setText(DateUtil.timestampToDate(totalTime));
            iv_play_or_pause.setSelected(false);
            pro_progress.setProgress(0);
        }
    };


    /**
     * 异步加载音频文件
     */
    class UpdateMediaInfo extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pro_prepareLoad.setVisibility(VISIBLE);
            // iv_play_or_pause.setEnabled(false);
            iv_play_or_pause.setVisibility(GONE);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            isPlaying = false;
            iv_play_or_pause.setSelected(false);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                mMediaPlayer.setDataSource(strings[0]);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void checkMediaPlayerNotNull() {
        if (mMediaPlayer == null) {
            throw new RuntimeException("视频播放器不能为NULL");
        }
    }
}
