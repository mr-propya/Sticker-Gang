package com.example.stickergang.UI;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.stickergang.Groups.CacheSQL;
import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.ImageManipulations.BaseActivity;
import com.example.stickergang.ImageManipulations.ImageEditor;
import com.example.stickergang.R;
import com.example.stickergang.WAHelpers.StickerGroup;

import butterknife.BindView;

public class MainActivity extends ActivityHelper {
    @BindView(R.id.button)
    Button button;

    @BindView(R.id.helloWorld)
    Button hello;

    @Override
    protected void viewReady(View v) {

        hello.setOnClickListener(v1->{
            startActivityForResult(new Intent(MainActivity.this, ImageEditor.class), new ActivityResultResponse() {
                @Override
                public void result(Intent data, int result) {
                    if(result== Activity.RESULT_OK)
                        showToast("Save file");
                    else
                        showToast("discarded");
                }
            });
        });


        button.setOnClickListener(v12 -> {
            startActivity(ShowAllPacks.class);
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
//                CacheSQL sql = new CacheSQL(MainActivity.this);
//                for(StickerGroup s: sql.getAllPacks(false)){
//                    Log.i("Sticker group",s.getIdentifier());
//                    Log.i("Sticker group",s.getName());
//                }

        });

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }
}
