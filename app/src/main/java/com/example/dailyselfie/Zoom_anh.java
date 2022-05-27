package com.example.dailyselfie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class Zoom_anh extends AppCompatActivity {
    private ImageView imageView;
    private TextView imagename;
    private Button btnxoa;
    private String tenanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_anh);

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();

        imageView = (ImageView) findViewById(R.id.imageView);
        imagename = (TextView) findViewById(R.id.textView2);
        btnxoa = (Button) findViewById(R.id.btnXoa);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("image");
            tenanh = (String) extras.get("tenanh");
            imagename.setText(tenanh);
            imageView.setImageBitmap(imageBitmap);
        }

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaanh(tenanh,path);
            }
        });
    }

    public void xoaanh(String tenanh, String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            String[] listanh = dir.list();
            for (int i = 0; i < listanh.length; i++) {
                if (listanh[i].equals(tenanh)) {
                    new File(dir, listanh[i]).delete();
                    break;
                }
            }
        }
        Intent intent = new Intent(Zoom_anh.this,MainActivity.class);
        startActivity(intent);
    }
}
