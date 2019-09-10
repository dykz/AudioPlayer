package com.beanbean.audioplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private RecyclerView recyclerView;
    private AudioAdapter mAudioAdapter;
    private String[] dataArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        initView();
        //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        mAudioAdapter = new AudioAdapter(mContext, new ArrayList<String>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAudioAdapter);

        dataArr = new String[]{"http://192.168.31.120/files/music/fcml_lj_sl.mp3"
                , "http://192.168.31.120/files/music/hktk_beyond.mp3"
                , "http://192.168.31.120/files/music/632_audio.amr"
                , "http://192.168.31.120/files/music/rc1.wav"
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (mAudioAdapter != null) {
                mAudioAdapter.addData(organizeData());
            }
        }
    }

    private List<String> organizeData() {
        List<String> list = new ArrayList<>();
        list.add("http://192.168.31.120/files/music/fcml_lj_sl.mp3");
        list.add("http://192.168.31.120/files/music/rc1.wav");
        list.add("http://192.168.31.120/files/music/hktk_beyond.mp3");
        list.add("http://192.168.31.120/files/music/632_audio.amr");
        list.add("http://192.168.31.120/files/music/fcml_lj_sl.mp3");
        list.add("http://192.168.31.120/files/music/rc1.wav");
        list.add("http://192.168.31.120/files/music/rc1.wav");
        list.add("http://192.168.31.120/files/music/hktk_beyond.mp3");
        list.add("http://192.168.31.120/files/music/632_audio.amr");
        return list;
    }

    private int index = 0;
    public void addDatas(View view) {
        if (mAudioAdapter != null) {
            //mAudioAdapter.addData(organizeData());
        }
        Log.i(TAG, "addDatas: "+dataArr[index]);
        mAudioAdapter.addData(dataArr[index]);
        if (index == 3){
            index = 0;
        }
        index++;
    }
}
