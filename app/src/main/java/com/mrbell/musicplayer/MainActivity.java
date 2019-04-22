package com.mrbell.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int mypermission=1;
    List<Modeldata> list;
    RecyclerView recyclerView;
    String[] item;
    ArrayList<File> mysong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.rvlist);

        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.
                READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},mypermission);

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},mypermission);

            }
        }
        else {
            mysong=findsong(Environment.getExternalStorageDirectory());
            item=new String[mysong.size()];
            list=new ArrayList<>();
            for(int i=0;i<mysong.size();i++){

                item[i]=mysong.get(i).getName().toString().replace("mp3","");
                Toast.makeText(this, item[i], Toast.LENGTH_SHORT).show();
                Modeldata modeldata = new Modeldata(item);
                list.add(modeldata);

            }



            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            RecyclerView.LayoutManager rLlaoutmanager = layoutManager;
            recyclerView.setLayoutManager(rLlaoutmanager);
            Customadapter adapter = new Customadapter(MainActivity.this,item,mysong);
            recyclerView.setAdapter(adapter);

        }

    }

    private  ArrayList<File> findsong(File root) {
        ArrayList<File> mpfile = new ArrayList<>();

        File[] allfile= root.listFiles();

        for(File singlefile: allfile){
            if(singlefile.isDirectory() && !singlefile.isHidden()){

                    mpfile.addAll(findsong(singlefile));

            }else{
                if(singlefile.getName().endsWith(".mp3")){
                    mpfile.add(singlefile);
                }
            }
        }
        return mpfile;
    }


}
