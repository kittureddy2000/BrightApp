package com.brightapps.all;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.brightapps.all.mDatabase.MainDbAdapter;
import com.brightapps.all.mDatabase.TodoObject;

/**
 * Created by kyadamakanti on 1/18/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "KIT-BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            //TODO  : Restart Alarms using the background service

            // It is better to reset alarms using Background IntentService
          /*  Intent i = new Intent(context, BootService.class);
            ComponentName service = context.startService(i);

            if (null == service) {
                // something really wrong here
                Log.e(TAG, "Could not start service ");
            }
            else {
                Log.e(TAG, "Successfully started service ");
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }

            public class BootService extends IntentService {

                public BootService() {
                    super("BootService");
                }

                @Override
                protected void onHandleIntent(Intent intent) {

                    // Your code to reset alarms.
                    // All of these will be done in Background and will not affect
                    // on phone's performance

                }
            }       */
            restartAlarms(context,intent);
        }
    }

    private void restartAlarms(Context context, Intent intent) {
        MainDbAdapter myDB = new MainDbAdapter(context);
        myDB.open();
        Cursor cursor = myDB.getAllRows();

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_TITLE));
            String dateTime = cursor.getString(cursor.getColumnIndex(MainDbAdapter.KEY_REMINDER_DATE));
            int reccurance_id =  cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_RECURRENCE));
            int row_Id = cursor.getInt(cursor.getColumnIndex(MainDbAdapter.KEY_ROWID));
            Log.d(TAG, "BootReceiver: Title :" + title);
            Log.d(TAG, "BootReceiver: Reccurance : " + reccurance_id);
            Log.d(TAG, "BootReceiver: Time " + dateTime);

            Util.scheduleNotification(context,dateTime, title, "1", row_Id, 0, reccurance_id);

        }
    }
}

