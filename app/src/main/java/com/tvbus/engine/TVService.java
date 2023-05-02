package com.tvbus.engine;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.brazvip.fivetv.R;

public class TVService extends Service {
    public static final String TAG = "TVBusService";
    public static boolean bInited = false;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* renamed from: b */
    private void showNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "P2Five";
            String appName = getString(R.string.app_name);
            String content = getString(R.string.service_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, appName,
                                                NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                                                .createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                       .setContentTitle(appName).setContentText(content).build();
            startForeground(1, notification);
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        showNotification();
        Thread thread = new Thread(new TVServer());
        thread.setName("tvcore");
        thread.start();
    }

    @Override // android.app.Service
    public void onDestroy() {
        try {
            TVCore.getInstance().quit();
        } catch (Exception ignored) { }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        else
            stopForeground(true);
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY; //2
    }

    private class TVServer implements Runnable {
        public static final String TAG = "TVBusServer";
        public TVCore tvcore = TVCore.getInstance();

        public TVServer() {
        }

        @Override // java.lang.Runnable
        public void run() {
            this.tvcore.setPlayPort(8902);
            this.tvcore.setServPort(0);
            this.tvcore.setRunningMode(TVCore.RM_MASTER);
            int result = this.tvcore.init(getApplicationContext());
            TVService.bInited = true;
            if (result == 0) {
                this.tvcore.run();
            }
        }
    }
}
