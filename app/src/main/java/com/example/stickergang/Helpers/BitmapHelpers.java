package com.example.stickergang.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;

public class BitmapHelpers {

    public Bitmap b;

    public BitmapHelpers(Bitmap b) {
        this.b = b;
    }
    public BitmapHelpers(Context c, Uri u) throws FileNotFoundException {
        this.b = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(u));
    }

    private boolean isEmpty(int[] a){
        for (int value : a) {
            if (value != Color.TRANSPARENT)
                return false;
        }
        return true;
    }



    public void saveImage(Context c ,String path) throws FileNotFoundException {
        if(path == null)
            path = Constants.ImageConstants.INTERMEDIATE_FILE;
        if(!path.endsWith(".png"))
            path+=".png";
        File f = new File(c.getExternalFilesDir(null),path);
        b.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(f));
    }

    public Bitmap scaleBitmapToWhatsApp(){
        int w = b.getWidth();
        int h = b.getHeight();
        int side = Math.max(w,h);

        Bitmap bitmap = Bitmap.createBitmap(side,side,b.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);

        int offset =((int)Math.abs(w-h)/2);

        if(h>w)
            canvas.drawBitmap(bitmap,new Rect(0,h,w,0),new Rect(offset,side,side-offset,0),null);
        else
            canvas.drawBitmap(bitmap,new Rect(0,h,w,0),new Rect(0,side-offset,side,offset),null);
        bitmap = Bitmap.createScaledBitmap(bitmap,502,502,true);

        BitmapHelpers helpers = new BitmapHelpers(bitmap);
        return helpers.padImage(false,10);

    }

    public Bitmap padImage(boolean inplace,int px){
        int w = b.getWidth()+px*2;
        int h = b.getHeight()+px*2;
        Bitmap bitmap = Bitmap.createBitmap(w,h,b.getConfig());
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.TRANSPARENT);
        c.drawBitmap(b,new Rect(0,b.getHeight(),b.getWidth(),0),new Rect(px,h-px,w-px,px),null);

        if(inplace)
            this.b = bitmap;

        return bitmap;
    }
    public Bitmap trim(boolean inplace) {

        int height = b.getHeight();
        int width = b.getWidth();
        int[] empty = new int[width];
        int[] buffer = new int[width];
        Arrays.fill(empty, 0);
        int top = 0;
        int left = 0;
        int bottom = height;
        int right = width;

        for (int y = 0; y < height; y++) {
            b.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                top = y;
                break;
            }
        }

        for (int y = height - 1; y > top; y--) {
            b.getPixels(buffer, 0, width, 0, y, width, 1);
            if (!Arrays.equals(empty, buffer)) {
                bottom = y;
                break;
            }
        }

        empty = new int[height];
        buffer = new int[height];
        Arrays.fill(empty, 0);

        for (int x = 0; x < width; x++) {
            b.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                left = x;
                break;
            }
        }

        for (int x = width - 1; x > left; x--) {
            b.getPixels(buffer, 0, 1, x, 0, 1, height);
            if (!Arrays.equals(empty, buffer)) {
                right = x;
                break;
            }
        }

         Bitmap bitmap = Bitmap.createBitmap(b, left, top, right - left + 1, bottom - top + 1);

        if(inplace)
            this.b = bitmap;

        return bitmap;
    }

    public static Bitmap getBitmap(Context c,String path) throws FileNotFoundException {
        if(path == null){
            path  = Constants.ImageConstants.INTERMEDIATE_FILE;
        }
        return new BitmapHelpers(c,Uri.fromFile(new File(c.getExternalFilesDir(null),path))).b;
    }

}
