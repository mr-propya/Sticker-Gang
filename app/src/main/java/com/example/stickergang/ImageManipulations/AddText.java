package com.example.stickergang.ImageManipulations;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stickergang.Helpers.BottomFragHelper;
import com.example.stickergang.R;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import butterknife.BindView;
import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddText extends BottomFragHelper {

    private PhotoEditor photoEditor;

    @BindView(R.id.submitText)
    Button submit;

    @BindView(R.id.addText)
    EditText text;

    @BindView(R.id.colorPicker)
    ColorPicker colorPicker;

    private int finalColorSelect = Color.BLACK;

    public AddText() {
    }

    AddText(PhotoEditor photoEditor){
        this.photoEditor = photoEditor;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_text;
    }

    @Override
    protected void layoutReady(View view) {
        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener(){
            @Override
            public void onColorSelected(int color) {
                finalColorSelect = color;
            }
        });

        submit.setOnClickListener(v->{
            photoEditor.addText(text.getText().toString(),finalColorSelect);
            dismiss();
        });
    }
}
