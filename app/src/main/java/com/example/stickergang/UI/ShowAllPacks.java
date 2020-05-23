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
import com.example.stickergang.WAHelpers.StickerGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;

public class ShowAllPacks extends ActivityHelper {

    @BindView(R.id.allStickersRecycler)
    RecyclerView recyclerView;

    @Override
    protected void viewReady(View v) {
        CacheSQL sql = new CacheSQL(this);
        ArrayList<StickerGroup> allPacks = sql.getAllPacks(false);
        sql.syncDB(isSuccess->{log("Update "+isSuccess);});
        Collections.sort(allPacks, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        Adapter adapter = new Adapter(this,allPacks);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        log("Size of adapter "+allPacks.size());
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_show_all_packs;
    }







    class Adapter extends RecyclerView.Adapter<VH>{
        Context c;
        ArrayList<StickerGroup> groups;

        public Adapter(Context c, ArrayList<StickerGroup> groups) {
            this.c = c;
            this.groups = groups;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(c).inflate(R.layout.recycler_all_packs,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            StickerGroup stickerGroup = groups.get(position);
            holder.mainText.setText(stickerGroup.getName());
            holder.itemView.setTag(stickerGroup.getIdentifier());
            holder.itemView.setOnClickListener(v->{
                Bundle b = new Bundle();
                String tag = (String) v.getTag();
                log("Starting group "+tag);
                b.putString(Constants.SqlHelper.STICKER_PACK_IDENTIFIER,tag);
                ((ActivityHelper)c).startActivity(GroupDetails.class,b);
            });
        }

        @Override
        public int getItemCount() {
            return groups.size();
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

