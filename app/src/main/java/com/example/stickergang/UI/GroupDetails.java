package com.example.stickergang.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stickergang.Groups.CacheSQL;
import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.Helpers.Constants;
import com.example.stickergang.R;
import com.example.stickergang.WAHelpers.Sticker;
import com.example.stickergang.WAHelpers.StickerGroup;

import java.util.ArrayList;

import butterknife.BindView;

public class GroupDetails extends ActivityHelper {

    String group;

    @BindView(R.id.allStickersRecycler)
    RecyclerView recyclerView;


    @Override
    protected void viewReady(View v) {
        group = this.extras.getString(Constants.SqlHelper.STICKER_PACK_IDENTIFIER);
        log("Got group "+group);
        CacheSQL sql = new CacheSQL(this);
        ArrayList<Sticker> allStickers = sql.getAllStickers(group);
        Adapter adapter = new Adapter(this,allStickers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_show_all_packs;
    }
    class Adapter extends RecyclerView.Adapter<GroupDetails.VH>{
        Context c;
        ArrayList<Sticker> stickers;

        public Adapter(Context c, ArrayList<Sticker> stickers) {
            this.c = c;
            this.stickers = stickers;
        }

        @NonNull
        @Override
        public GroupDetails.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GroupDetails.VH(LayoutInflater.from(c).inflate(R.layout.recycler_all_packs,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull GroupDetails.VH holder, int position) {
            Sticker sticker = stickers.get(position);
            holder.mainText.setText(sticker.getName());
        }

        @Override
        public int getItemCount() {
            return stickers.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder{

        TextView mainText;

        VH(@NonNull View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.adapterMainText);
        }
    }


}
