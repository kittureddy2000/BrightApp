package com.brightapps.all;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by deepmetha on 8/30/16.
 */
public class AlarmRecever extends BroadcastReceiver {
    private static final String TAG = "KIT-AlarmRecever";
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String taskTitle = bundle.getString(Util.TASKTITILE);
            String taskPrority = bundle.getString(Util.TASKPRIORITY);
            int _id = bundle.getInt(Util.REC_ID);
            int reccurance_Id = bundle.getInt(Util.RECCURANCE_ID);
            Log.d(TAG, "On-Receive: Id : " + _id + " taskTitle : " + taskTitle + " Reccurance_Id : " + reccurance_Id);
            disPlayNotification(_id, taskTitle, taskPrority);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            setrepatingAlarm(reccurance_Id, pendingIntent, context);
        }

    }

    private void setrepatingAlarm(int reccurance_id, PendingIntent intent, Context context) {
        Calendar now = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (reccurance_id == 3) {

            now.add(Calendar.MONTH, 1);
            alarmManager.set(AlarmManager.RTC, now.getTimeInMillis(), intent);
        }

        if (reccurance_id == 4) {
            now.add(Calendar.YEAR, 1);
            alarmManager.set(AlarmManager.RTC, now.getTimeInMillis(), intent);

        }

    }

    public void disPlayNotification(int mNotificationId, String title, String priority) {


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// The user-visible name of the channel.
        CharSequence name = context.getString(R.string.CHANNEL_NAME);
// The user-visible description of the channel.
        String description = context.getString(R.string.CHANNEL_DESC);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(Util.CHANNEL_ID, name, importance);
// Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, Util.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                        .setContentTitle("Bright Apps Notification")
                        .setContentText(title);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }
}
