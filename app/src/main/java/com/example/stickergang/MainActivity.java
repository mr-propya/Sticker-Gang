package com.example.stickergang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.ImageManipulations.BaseActivity;
import com.github.gabrielbb.cutout.CutOut;

import butterknife.BindView;

public class MainActivity extends ActivityHelper {
    @BindView(R.id.button)
    Button button;

    @Override
    protected void viewReady(View v) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BaseActivity.class));
            }
        });
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_main;
    }
}
