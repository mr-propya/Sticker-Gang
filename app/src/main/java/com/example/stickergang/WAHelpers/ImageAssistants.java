package com.example.stickergang.WAHelpers;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageAssistants {

    Context c;
    String externalPath;

    public ImageAssistants(Context c,String externalPath){
        this.c = c;
        this.externalPath = externalPath;
    }
    public ImageAssistants(Context c){
        this.c = c;
        this.externalPath = "ImageAssist";
    }

    public void saveImage() throws IOException {
        File f = c.getExternalFilesDir(externalPath);
        File fNew = new File(f,"abc.txt");
        fNew.createNewFile();
        byte[] data1={1,1,0,0};
        if(fNew.exists())
        {
            OutputStream fo = new FileOutputStream(fNew);
            fo.write(data1);
            fo.close();
        }
    }

    public ArrayList<String> getStickerPackList(){
        return new ArrayList<>();
    }

    public ArrayList<String> getImageList(String subFolder){
        ArrayList<String> images = new ArrayList<>();
        File f = c.getExternalFilesDir(externalPath);
        if(subFolder !=null)
            f = new File(f,subFolder);
        Log.i("Filename root",f.getName());
        Log.i("Filename root",f.getAbsolutePath());
        if(f.exists()){
            String[] list = f.list();
            if(list!=null){
                for(String s:list){
                    if(s.endsWith(".webp"))
                        images.add(s);
                }
            }
        }
        return images;
    }

    File getFile(String stickerPack,String fileName){
        File externalFilesDir = c.getExternalFilesDir(externalPath);
        return new File(externalFilesDir,fileName);
    }


}
