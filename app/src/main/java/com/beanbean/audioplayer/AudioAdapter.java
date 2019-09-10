package com.beanbean.audioplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanbean.player.BBAudioView;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ReportViewHolder> {
    private final static String TAG= "AudioAdapter";
    private Context mContext;
    private List<String> mList;

    public AudioAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }


    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReportViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_audio_item, viewGroup, false), viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder viewHolder, int i) {
        viewHolder.mBBAudio.setDataUrl(mList.get(i));
        viewHolder.mBBAudio.setProgressDraw(R.drawable.progress_bar_drawable);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(List<String> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(String path) {
        mList.add(path);
        notifyDataSetChanged();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        public BBAudioView mBBAudio;

        public ReportViewHolder(View itemView, Context context) {
            super(itemView);
            mBBAudio = itemView.findViewById(R.id.bbAudio);
        }
    }
}
