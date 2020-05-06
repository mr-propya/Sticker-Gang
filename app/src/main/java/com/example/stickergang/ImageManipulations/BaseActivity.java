package com.example.stickergang.ImageManipulations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.stickergang.Helpers.ActivityHelper;
import com.example.stickergang.Helpers.BitmapHelpers;
import com.example.stickergang.Helpers.Constants;
import com.example.stickergang.R;
import com.github.gabrielbb.cutout.CutOut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import io.grpc.Context;

public class BaseActivity extends ActivityHelper {


    @BindView(R.id.tryNewImage)
    Button pickImage;

    @BindView(R.id.eraseImage)
    Button eraseImage;

    @BindView(R.id.editImage)
    Button editImage;

    @BindView(R.id.imagePreview)
    ImageView previewImage;


    static Uri uri = null;
    static final int REQUEST_IMAGE = 547;

    @Override
    protected int getRootView() {
        return R.layout.activity_base;
    }

    @Override
    protected void viewReady(View v) {
        pickImage.setOnClickListener(v1 -> getImage());
        eraseImage.setOnClickListener(v1 -> eraseImage());
        editImage.setOnClickListener(v1 -> startActivity(new Intent(BaseActivity.this,ImageEditActivity.class)));
        updatePreview();
    }

    void getImage(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/**");
        startActivityForResult(i,REQUEST_IMAGE);
    }

    void eraseImage(){
        CutOut.activity().src(uri).noCrop().start(this);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (reqCode){
                case REQUEST_IMAGE:
                    uri = data.getData();
                    saveInterMediate();
                    break;
                case CutOut.CUTOUT_ACTIVITY_REQUEST_CODE:
                    uri = CutOut.getUri(data);
                    try {
                        BitmapHelpers helpers = new BitmapHelpers(this,uri);
                        helpers.trim(true);
                        previewImage.setImageBitmap(helpers.b);
                        helpers.saveImage(this,null);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        showToast("error cropp");
                    }
//                    saveInterMediate();
                    break;
            }
//            updatePreview();
        }else{
            showToast("Some error occurred");
        }
    }

    private void padImage(Bitmap bitmap) {
        try {
            int height = (int) ((bitmap.getHeight()*Constants.ImageConstants.IMAGE_PADDING));
            int width = (int) ((bitmap.getWidth()*Constants.ImageConstants.IMAGE_PADDING));
            height-=bitmap.getHeight();
            width-=bitmap.getWidth();
            height/=2;
            width/=2;
            Log.d("height pad",String.valueOf(height));
            Bitmap padded = Bitmap.createBitmap((int)(bitmap.getWidth()+width*2),(int)(bitmap.getHeight()+height*2),bitmap.getConfig());

            Canvas canvas = new Canvas(padded);
            canvas.drawARGB(255,255,255,255);
            canvas.drawBitmap(padded,width,height,null);

            File f = new File(getExternalFilesDir(null), Constants.ImageConstants.INTERMEDIATE_FILE);
            showToast(f.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(f);

            padded.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("error in padding");
        }


    }

    void saveInterMediate(){
        ProgressDialog dialog = ProgressDialog.show(this, "Processing image", "Please Wait", true, false);

        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            if (imageStream != null) {
                imageStream.close();
            }
            selectedImage = trimBitmap(selectedImage);
            File f = new File(getExternalFilesDir(null), Constants.ImageConstants.INTERMEDIATE_FILE);
            showToast(f.getAbsolutePath());
            FileOutputStream outputStream = new FileOutputStream(f);
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        }catch (Exception e){
            log("Error saving file");
        }
        dialog.dismiss();


    }
    void updatePreview(){
        try {
            File f = new File(getExternalFilesDir(null),Constants.ImageConstants.INTERMEDIATE_FILE);
            if(!f.exists()){
                return;
            }
            uri = Uri.fromFile(f);
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            if (imageStream != null) {
                imageStream.close();
            }
            previewImage.setImageBitmap(selectedImage);

        } catch (IOException e) {
            e.printStackTrace();
            showToast("error saving");
        }

    }

    Bitmap trimBitmap(Bitmap b){
//        trip the white spaces and center align the pic

        return b;

    }

}
