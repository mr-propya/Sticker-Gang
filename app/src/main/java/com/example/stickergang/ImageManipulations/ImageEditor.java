package com.example.stickergang.ImageManipulations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.Helpers.BitmapHelpers;
import com.example.stickergang.Helpers.Constants;
import com.example.stickergang.R;
import com.example.stickergang.UI.CurvedBottomNavigationView;
import com.github.gabrielbb.cutout.CutOut;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class ImageEditor extends ActivityHelper {

    @BindView(R.id.photoEditor)
    PhotoEditorView photoEditorView;

    @BindView(R.id.linearControls)
    LinearLayout layout;

    @BindView(R.id.photoHolder)
    FrameLayout holder;

    @BindView(R.id.bottomNav)
    CurvedBottomNavigationView bottomNavigationView;

    @BindView(R.id.centerFab)
    FloatingActionButton fab;

    PhotoEditor photoEditor;

    @BindView(R.id.refSize)
    View refView;

    void setBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemReselectedListener(menuItem -> {
                    switch (menuItem.getItemId()){
                        case R.id.undoBottom:
                            photoEditor.undo();
                            break;
                        case R.id.redoBottom:
                            photoEditor.redo();
                            break;
                        case R.id.middleBottom:
                            handleEditMenu();
                            break;
                    }
                    menuItem.setChecked(false);
                    menuItem.setChecked(false);
                    menuItem.setChecked(false);
                }
        );

    }

    private void handleEditMenu() {
        saveImage(true,false);
    }

    @Override
    protected void viewReady(View v) {
        photoEditor = new PhotoEditor.Builder(this, this.photoEditorView).setPinchTextScalable(true).build();
        setBottomNavigationView();
        try {
            Bitmap bitmap = BitmapHelpers.getBitmap(this, null);
            bitmap.getWidth();
            setImageBitmap(bitmap);
        } catch (Exception e) {
            pickImage();
        }
        fab.setOnClickListener(v1->handleEditMenu());
        fab.setTag(false);
    }

    void pickImage(){

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/**");
        startActivityForResult(i, (data, result) -> {
            if(result != Activity.RESULT_OK || data == null)
                return;
            setImageBitmap(data.getData());

        });

    }

    void setImageBitmap(Bitmap bitmap){
        PhotoEditorView photoEditorViewNew = new PhotoEditorView(this);
        photoEditorViewNew.setLayoutParams(photoEditorView.getLayoutParams());

        holder.removeView(photoEditorView);
        holder.addView(photoEditorViewNew);

        photoEditorView = photoEditorViewNew;

        photoEditor = new PhotoEditor.Builder(this, this.photoEditorView).setPinchTextScalable(true).build();
        this.photoEditorView.getSource().setImageBitmap(bitmap);

    }
    void setImageBitmap(Uri uri){
        if(uri == null){
            return;
        }
        try {
            BitmapHelpers bitmapHelpers = new BitmapHelpers(this,uri);
            setImageBitmap(bitmapHelpers.b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void cutOutImage(){
        saveImage(false,true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveImage(false,false);
    }

    void saveImage(boolean exit, boolean cutOut){
        Uri uri = Uri.fromFile(new File(getExternalFilesDir(null), Constants.ImageConstants.INTERMEDIATE_FILE));
        photoEditor.saveAsFile(uri.getPath(), new PhotoEditor.OnSaveListener() {

            @Override
            public void onSuccess(@NonNull String imagePath) {
                log("File saved to intermediate");
                if(exit){
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                if(cutOut){
                    CutOut.activity().noCrop().src(uri).start(ImageEditor.this);
                }

            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                log(exception.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CutOut.CUTOUT_ACTIVITY_REQUEST_CODE){
            if(resultCode==Activity.RESULT_OK){
                setImageBitmap(CutOut.getUri(data));
            }else {
                showToast("Error cutting out image");
            }
        }
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_image_editor;
    }
}
