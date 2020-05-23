package com.example.stickergang.ImageManipulations;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.Helpers.Constants;
import com.example.stickergang.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ImageEditActivity extends ActivityHelper {

    @BindView(R.id.photoEdit)
    PhotoEditorView photoEditorView;

    @BindView(R.id.uselessButton)
    Button useless;

    @Override
    protected void viewReady(View v) {
        File f = new File(getExternalFilesDir(null), Constants.ImageConstants.INTERMEDIATE_FILE);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            photoEditorView.getSource().setImageBitmap(bitmap);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        useless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoEditor mPhotoEditor = new PhotoEditor.Builder(ImageEditActivity.this, photoEditorView)
                        .setPinchTextScalable(true)
                        .build();

                mPhotoEditor.saveAsFile(f.getAbsolutePath(), new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        showToast("Success saved");
                        setResult(RESULT_OK);
                        finishActivity(50);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast("Error saving");
                        log(exception.getLocalizedMessage());
                    }
                });
            }
        });


    }

    @Override
    protected int getRootView() {
        return R.layout.activity_image_edit;
    }
}
