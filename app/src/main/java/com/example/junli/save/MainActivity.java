package com.example.junli.save;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText etContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etContent = findViewById(R.id.etContent);
    }

    //清楚文本输入框内容
    protected void onContentClear(View view){
        etContent.setText("");
    }

    //保存到SharedPreferences
    protected  void onSaveToSharedPreferences(View view){
        SharedPreferences sp = getSharedPreferences("my_preferences",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("content",etContent.getText().toString());
        editor.commit();
    }

    //从SharedPreference中读取
    protected void onLoadFromSharedPreferences(View view){
        SharedPreferences sp = getSharedPreferences("my_preferences",0);
        String content = sp.getString("content","默认值");
        etContent.setText(content);
    }

    //保存到内部文件
    protected  void onSaveToInternalFile(View view){
        FileOutputStream fos = null;
        String content = etContent.getText().toString();
        try{
            fos = openFileOutput("my_dataFile", Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    //从内部文件读取
    protected void onLoadFromInternalFile(View view) {
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try {
            fis = openFileInput("my_dataFile");
            int len = fis.read(buffer);
            fis.close();

            String content = new String(buffer, 0, len);
            etContent.setText(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        //检测内部存储是否可写
        private boolean isExternaStorageWritable(){
            String state = Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equals(state)){
                return true;
            }
            return false;
        }
        //保存到外部文件
    protected void onSaveToExternalFile(View view){
        if(!isExternaStorageWritable()) return;

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString());

        if(!file.mkdirs()){
            Log.e("DEMO","Directory not created!");
        }

        FileOutputStream fos = null;
        String content = etContent.getText().toString();
        try{
            fos = new FileOutputStream(file+"/test.txt");
            fos.write(content.getBytes());
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //从外部文件读取
    protected  void onLoadFromExternalFile(View view){
        if(!isExternaStorageWritable()) return;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString());

        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try{
            fis = new FileInputStream(file+"/test.txt");
            int len = fis.read(buffer);
            fis.close();

            String content = new String(buffer,0,len);
            etContent.setText(content);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
