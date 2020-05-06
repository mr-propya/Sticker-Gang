package com.example.stickergang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.stickergang.Groups.CacheSQL;
import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.ImageManipulations.BaseActivity;
import com.example.stickergang.WAHelpers.AddStickerHelpers;
import com.example.stickergang.WAHelpers.ImageAssistants;
import com.example.stickergang.WAHelpers.StickerGroup;
import com.github.gabrielbb.cutout.CutOut;

import java.io.IOException;

import butterknife.BindView;

public class MainActivity extends ActivityHelper {
    @BindView(R.id.button)
    Button button;

    @Override
    protected void viewReady(View v) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, BaseActivity.class));
//                ImageAssistants imageAssistants = new ImageAssistants(MainActivity.this);
//                try {
////                    imageAssistants.getImageList(null);
//                } catch (Exception e) {
//                    showToast(e.getLocalizedMessage());
//                    e.printStackTrace();
//                }
//                AddStickerHelpers helpers = new AddStickerHelpers(MainActivity.this);
//                helpers.registerStickerPack("abc_identifier",BuildConfig.CONTENT_PROVIDER_AUTHORITY,
//                        "This is my sticker pack");
                CacheSQL sql = new CacheSQL(MainActivity.this);
                for(StickerGroup s: sql.getAllPacks(false)){
                    Log.i("Sticker group",s.getIdentifier());
                }
            }
        });

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }
}
