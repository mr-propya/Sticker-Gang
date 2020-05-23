package com.example.stickergang.ImageManipulations;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.stickergang.Helpers.BottomFragHelper;
import com.example.stickergang.R;

import butterknife.BindView;
import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawBrushSettings extends BottomFragHelper {

    private PhotoEditor photoEditor;

    @BindView(R.id.brushModeSwitch)
    Switch editingEnabled;

    @BindView(R.id.innerLinear)
    LinearLayout innerSettings;

    @BindView(R.id.eraseModeSwitch)
    Switch eraseEnabled;

    @BindView(R.id.opacitySlide)
    SeekBar opacity;

    @BindView(R.id.sizeSlide)
    SeekBar size;

    boolean isEraser = false;

    public DrawBrushSettings() {
        // Required empty public constructor
    }

    DrawBrushSettings(PhotoEditor photoEditor){
        this.photoEditor = photoEditor;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_draw_brush_settings;
    }


    @Override
    protected void layoutReady(View view) {
        Boolean brushDrawableMode = photoEditor.getBrushDrawableMode();
        enableEditing(brushDrawableMode);

        if(isEraser)
            photoEditor.brushEraser();
        eraseEnabled.setChecked(isEraser);

        int progress = (int)(photoEditor.getBrushSize())+1;
        if(isEraser)
            progress = (int)(photoEditor.getEraserSize()+1);

        size.setProgress(progress);

        editingEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> enableEditing(isChecked));
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if (isEraser) {
                        photoEditor.setBrushEraserSize((float)(progress+1));
                    } else {
                        photoEditor.setBrushSize((float)(progress+1));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        eraseEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isEraser = isChecked;
            layoutReady(view);
        });

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(isEraser)
            photoEditor.brushEraser();
    }

    private void enableEditing(boolean isEnabled){
        editingEnabled.setChecked(isEnabled);
        for(int i=0;i<innerSettings.getChildCount();i++)
            innerSettings.getChildAt(i).setEnabled(isEnabled);
        photoEditor.setBrushDrawingMode(isEnabled);
    }

}
