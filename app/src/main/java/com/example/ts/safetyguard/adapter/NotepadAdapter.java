package com.example.ts.safetyguard.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ts.safetyguard.R;

import java.util.List;

public class NotepadAdapter extends RecyclerView.Adapter<NotepadAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mList;
    private SharedPreferences mSharedPreferences;

    public NotepadAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext=viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notepad,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.textView_position.setText("" + i);
        mSharedPreferences = mContext.getSharedPreferences("notepad",Context.MODE_PRIVATE);
        String data = mSharedPreferences.getString("" + i ,"");
        viewHolder.textView_event.setText(data);
        i++;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_position;
        TextView textView_event;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_position = itemView.findViewById(R.id.textView_position);
            textView_event = itemView.findViewById(R.id.textView_event);
        }
    }
}
