package com.example.services_demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;
import android.os.Handler;
import android.content.Context;
import android.util.Log;
import android.media.MediaPlayer;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleService extends Service {

    public Context context = this;
    public android.os.Handler handler = new Handler();
    public static Runnable runnable = null;
    MediaPlayer objPlayer;

    final static String MY_ACTION = "MY_ACTION";

    int _currentValue = 0;

    public SimpleService() {
    }

    public static String helloFromService() {
        return "Hello from Service";
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;
    }

    // public void startNotificationListener() {
    //     //start's a new thread
    //     new Thread(new Runnable() {
    //         @Override
    //         public void run() {
    //             //fetching notifications from server
    //             //if there is notifications then call this method
    //             ShowNotification();
    //         }
    //     }).start();
    // }

    public void ShowNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getBaseContext(), "com.example.services_demo/service")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("title")
                .setContentText("content")
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .build();
        notificationManager.notify(0, notification);
        //the notification is not showing

    }

    @Override
    public void onCreate() {
        Log.e(">>>>>>>>", "Service created!");

        objPlayer = MediaPlayer.create(this, R.raw.tuk);
        // objPlayer.setLooping(true);

        runnable = new Runnable() {
            public void run() {
                Log.d(">>>>>>>>", "Service is still running!");
                objPlayer.start();
//                ShowNotification();
                notifyMe();
                Toast.makeText(context, "Service is still running!!!", Toast.LENGTH_SHORT).show();
                handler.postDelayed(runnable, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        // objPlayer.start();
        // Log.d(">>>>>>>>", "Media Player started!");
        // if(objPlayer.isLooping() != true){
        //     Log.d(">>>>>>>>", "Problem in Playing Audio");
        // }
        // return 1;


        saveText();
        // objPlayer.start();

        // MyThread myThread = new MyThread();
        // myThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    void saveText() {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();

        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd : hh.mm.ss");
        String date = format.format(todaysdate);
//        System.out.println(date);
        ed.putString(date, "play start");
        ed.commit();
//        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    // public void onStop(){
    //     objPlayer.stop();
    //     objPlayer.release();
    //     }

    //     public void onPause(){
    //     objPlayer.stop();
    //     objPlayer.release();
    //     }
    //     public void onDestroy(){
    //     objPlayer.stop();
    //     objPlayer.release();
    //     }

    public int getValue() {
        return _currentValue;
    }

    public class MyThread extends Thread {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            for (int i = 0; i < 1; i++) {
                try {
                    Thread.sleep(500);
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);

                    intent.putExtra("DATAPASSED", i);
                    _currentValue = i;

                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            stopSelf();
        }
    }
    private void notifyMe2() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.launch_background)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.launch_background, "Call", pIntent)
                .addAction(R.drawable.launch_background, "More", pIntent)
                .addAction(R.drawable.launch_background, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    private void notifyMe() {
        // The id of the channel.
        String CHANNEL_ID = "com.example.services_demo/service";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(SimpleService.this,CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //******
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setAction("SELECT_NOTIFICATION");
        Class mainActivityClass = getMainActivityClass(context);
        intent.setClass(context, mainActivityClass);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //******


        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NotificationManager.class);

        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        mNotificationManager.createNotificationChannel(channel);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(0, mBuilder.build());
    }
}
