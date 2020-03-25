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

    public Bitmap trimBitmap(boolean replaceInplace){
        int w = b.getWidth();
        int h = b.getHeight();

        int left = 0;
        int top = h-1;
        int right = w-1;
        int bottom = 0;


//        get the lower bound
        for(int i=0;i<h;i++){
            int[] pixels = new int[w];
            b.getPixels(pixels,0,0,0,i,w,1);
            if(isEmpty(pixels))
                bottom=i;
            else
                break;
        }

//        get the upper bound
        for(int i=h-1;i>=0;i--){
            int[] pixels = new int[w];
            b.getPixels(pixels,0,0,0,i,w,1);
            if(isEmpty(pixels))
                top=i;
            else
                break;
        }


//        get the left bound
        for(int i=0;i<w;i++){
            int[] pixels = new int[h];
            b.getPixels(pixels,0,0,i,0,1,h);
            if(isEmpty(pixels))
                left=i;
            else
                break;
        }

//        get the right bound
        for(int i=w-1;i>=0;i--){
            int[] pixels = new int[h];
            b.getPixels(pixels,0,0,i,0,1,h);
            if(isEmpty(pixels))
                right=i;
            else
                break;
        }

        w = right-left;
        h = top-bottom;
        Bitmap bitmap = Bitmap.createBitmap(w,h,b.getConfig());
        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.TRANSPARENT);
        c.drawBitmap(b,new Rect(left,top,right,bottom),new Rect(0,h,w,0),null);

        if(replaceInplace)
            this.b = bitmap;

        return bitmap;

    }

    public void saveImage(Context c ,String path) throws FileNotFoundException {
        if(path == null)
            path = "temporary.png";
        if(!path.endsWith(".png"))
            path+=".png";
        File f = new File(c.getExternalFilesDir(null),path);
        b.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(f));
    }


}
