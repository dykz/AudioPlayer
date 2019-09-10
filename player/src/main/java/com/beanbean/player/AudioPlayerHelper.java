package com.beanbean.player;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wugx
 * This media manager is control audio view {@link BBAudioView}
 */
public class AudioPlayerHelper {
    private final static String TAG = AudioPlayerHelper.class.getSimpleName();
    private static AudioPlayerHelper INSTANCE;
    private String currentPlayingPath;
    private boolean isTmpPause = false;
    private List<AudioPlayListener> mPlayListenerList;
    public static AudioPlayerHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (AudioPlayerHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioPlayerHelper();
                }
            }
        }
        return INSTANCE;
    }

    private AudioPlayerHelper() {
        mPlayListenerList = new ArrayList<>();
    }

    public void addAudioPlayListener(AudioPlayListener listener){
        if (!mPlayListenerList.contains(listener)){
            mPlayListenerList.add(listener);
        }
    }

    public void removeAudioPlayListener(AudioPlayListener listener){
        if (mPlayListenerList.contains(listener)){
            mPlayListenerList.remove(listener);
        }
    }

    public void startPlay(int hasCode){
        if (mPlayListenerList != null){
            for (AudioPlayListener listener : mPlayListenerList){
                listener.onStart(hasCode);
            }
        }
    }

    public void pausePlay(int hasCode){
        if (mPlayListenerList != null){
            for (AudioPlayListener listener : mPlayListenerList){
                listener.onPause(hasCode);
            }
        }
    }

    public interface AudioPlayListener {
        void onStart(int hasCode);

        void onPause(int hasCode);
    }

}
