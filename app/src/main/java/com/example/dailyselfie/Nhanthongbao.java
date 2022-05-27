package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class Nhanthongbao extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;


    private Intent mNotificationIntent;
    private PendingIntent mPendingIntent;




    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mNotificationIntent = new Intent(context, MainActivity.class);
            mPendingIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);

            // tao thong bao builder
            Notification.Builder notificationBuilder = new Notification.Builder(context)
                //    .setTicker(" ")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setAutoCancel(true)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Đến h chụp hình")
                    .setContentIntent(mPendingIntent);


            String channelId = "ALARM";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Your alarm is here",
                    NotificationManager.IMPORTANCE_HIGH);
            // quan ly thong bao
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
//            Toast.makeText(context, "Notification", Toast.LENGTH_LONG).show();
        }
        catch (Exception exception) {
            Log.d("NOTIFICATION", exception.getMessage().toString());
        }
    }
}
