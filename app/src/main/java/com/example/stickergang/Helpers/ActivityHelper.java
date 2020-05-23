package com.example.stickergang.Helpers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class ActivityHelper extends AppCompatActivity {
    int viewId;
    View rootView;
    Toast t;
    int requestIDs = 2000;
    protected Bundle extras;
    HashMap<Integer,ActivityResultResponse> resultResponseHashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewId = getRootView();
        rootView = LayoutInflater.from(this).inflate(viewId,null,false);
        setContentView(rootView);
        ButterKnife.bind(this);
        try {
            extras = getIntent().getBundleExtra("bundle");
        }catch (Exception e){
            Log.i("Bundle","No bundle attached");
            e.printStackTrace();
        }
        viewReady(rootView);
    }




    public void startActivity(Class c){
        startActivity(new Intent(this,c));
    }

    public void startActivity(Class c,Bundle b){
        Intent intent = new Intent(this, c);
        intent.putExtra("bundle",b);
        startActivity(intent);
    }

    public void showToast(String text){
        if(t!=null)
            t.cancel();
        t = Toast.makeText(this,text,Toast.LENGTH_LONG);
        t.show();
    }

    protected void log(String text){
        Log.v(this.getLocalClassName(),text);
    }

    public void startActivityForResult(Intent intent,ActivityResultResponse callBack) {
        int requestCode = requestIDs++;
        resultResponseHashMap.put(requestCode,callBack);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultResponseHashMap.containsKey(requestCode))
            if(resultResponseHashMap.get(requestCode)!=null)
                resultResponseHashMap.get(requestCode).result(data,resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    public interface ActivityResultResponse{
        public void result(Intent data,int result);
    }

    protected abstract void viewReady(View v);
    protected abstract int getRootView();
}
