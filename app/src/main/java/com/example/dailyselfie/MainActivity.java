package com.example.dailyselfie;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.Channel;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDateTime;
import java.util.Random;



public class MainActivity extends AppCompatActivity {



    private ImageView   imageView;
    private ImageButton maychupanh;
    private ImageButton btn_hengio;
    private GridView gridView;

    private ArrayList<Bitmap> images;
    private ArrayList<String> fileName;

    private static final long INTERVAL_TWO_MINUTES = 1 * 60 * 1000L;

    private Gallery_Adapter listanh;



    //saveanh
    private void saveanh(ImageView imageView){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        File dir = new File(path);

//        File file = Environment.getExternalStorageDirectory();
//        File dir  = new File(file.getAbsolutePath() + "/Pictures/Pic");
//        //dir.mkdirs();

        FileOutputStream outputStream = null;


        String filename = String.format("%d.png", System.currentTimeMillis());


        File outfile = new File(dir,filename);
        listanh.add(bitmap,outfile.getName());
        gridView.setAdapter(listanh);

        try{
            outputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // cap quyen cho may chua?
    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    //code chup hinh
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView.setImageBitmap(imageBitmap);
//                        imageBtn.setImageBitmap(imageBitmap);
                        if(isStoragePermissionGranted()){
                            // luu anh
                            saveanh(imageView);

                        }

                    }
                }
            });


    //mo may anh
    public void openCameraActivityForResult() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraActivityResultLauncher.launch(intent);
        imageView = new ImageView(this);
    }

    private void createAlarm() {
        try {
            Intent intent = new Intent(this, Nhanthongbao.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 1000L,
                    1000L,
                    pendingIntent);
        }
        catch (Exception exception) {
            Log.d("ALARM", exception.getMessage().toString());
        }
    }
    private void unsetAlarm() {
        Intent intent = new Intent(this, Nhanthongbao.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }
    //chỉnh kích thước và đưa ảnh vào bitmap
    public static Bitmap setImageFromFilePath(String imagePath, int targetW, int targetH) {
        // Nhan kich thuoc bitmap
        BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
        bmpOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, bmpOptions);
        int photoW = bmpOptions.outWidth;
        int photoH = bmpOptions.outHeight;


        int scaleFactor = Math.max(photoW / targetW, photoH / targetH);

        // dua anh vào bitmap
        bmpOptions.inJustDecodeBounds = false;
        bmpOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmpOptions);
        return bitmap;
    }

    public static Bitmap setImageFromFilePath(String imagePath) {
        return setImageFromFilePath(imagePath, 160, 120);
    }




    private void layanh(String path) {
        File dir = new File(path);
        File[] files = dir.listFiles();
//        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Bitmap imageBitmap = setImageFromFilePath(files[i].getPath());
            listanh.add(imageBitmap, files[i].getName());
            Log.d("Files", "FileName:" + files[i].getName());
        }
        gridView.setAdapter(listanh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();


        maychupanh = (ImageButton) (findViewById(R.id.btnmayanh));

        maychupanh.setOnClickListener(view -> openCameraActivityForResult());

        gridView = (GridView) (findViewById(R.id.gridView));

        listanh = new Gallery_Adapter(this, images, fileName);

        layanh(path);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, Zoom_anh.class);
                intent.putExtra("image", listanh.images.get(position));
                intent.putExtra("tenanh", listanh.fileName.get(position));
                startActivity(intent);
            }
        });

        //yeu cau quyen chup anh
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        //tao thong bao
        btn_hengio = (ImageButton)(findViewById(R.id.btngio));
        btn_hengio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlarm();
            }
        });




    }
}