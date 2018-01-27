package com.brightapps.all;

/**
 * Created by kyadamakanti on 12/25/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.brightapps.all.mDatabase.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by kyadamakanti on 1/6/2015.
 */
public class Util {
    private static final String TAG = "KIT-Util";

    static final String NOTIFY_ID = "NOTIFY_ID";
    static final String REC_ID = "REC_ID";
    static int TASK_ACTIVE = 1;
    public static int TASK_COMPLETE = 2;
    public static int TASK_ARCHIEVE = 3;
    public static String MARK_COMPLETE = "COMPLETE";
    public static String SNOOZE = "SNOOZE";
    static int SNOOZE_AMOUNT = 15;
    static String CATEGORY_NAME = "STANDARD";
    static String TASKTITILE = "TASKTITILE";
    static String TASKDESC = "TASKDESC";
    static String RECCURANCE_ID = "RECCURANCE_ID";
    static String TASKRECCURANCE = "TASKRECCURANCE";
    static String TASKDATE = "TASKDATE";
    static String TASKPOSITION = "TASKPOSITION";
    static String TASKPRIORITY = "TASKPRIORITY";
    static String NULL_TEXT = "";
    static String CHANNEL_ID = "BRIGHAPPS_01";


    private static Random randomGenerator;

    Util() {
        randomGenerator = new Random();

    }

    public static int getRandomInt() {
        return randomGenerator.nextInt(100000);
    }

    public static String getCurrentTimeString() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        return sdf.format(cal.getTime());
    }

    static DateTime getTimeValues(String dateTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
        Calendar calendar = Calendar.getInstance();
        String lDateTime;
        Date date ;

        Log.d(TAG, "getTimeValues: Datetime Passed " + dateTime);

        if (dateTime != null && !dateTime.isEmpty()) {
            lDateTime = dateTime;
        } else {
            lDateTime = sdf.format(new Date());
        }

        try {
            Log.d(TAG, "getTimeValues: Datetime Passed " + lDateTime);

            date = sdf.parse(lDateTime);
            calendar.setTime(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        int year;
        int month;
        int day;
        int hour;
        int minute;
        int amPM;
        String amPMString = "AM";

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        amPM = calendar.get(Calendar.AM_PM);

        if (amPM == Calendar.PM) {
            amPMString = "PM";
        }

        Log.d(TAG, "getTimeValues year :" + year + " month: " + month + " day: " + day + " hour: " + hour + " minute: " + minute + " amPM: " + amPM);

        return new DateTime(year, month, day, hour, minute, amPMString);
    }


    static long getTimeinMilliSeconds(String dateTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

        Date date = null;
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Log.d(TAG, "getTimeinMilliSeconds Time String : " + dateTime);

        Log.d(TAG, "getTimeinMilliSeconds Current Time  : " + calendar.getTime());
        Log.d(TAG, "getTimeinMilliSeconds Current Milli Second : " + calendar.getTimeInMillis());


        calendar.setTime(date);
        Log.d(TAG, "getTimeinMilliSeconds Alarm Time  : " + calendar.getTime());
        Log.d(TAG, "getTimeinMilliSeconds Alarm Milli Second : " + calendar.getTimeInMillis());

        return calendar.getTimeInMillis();
    }

    public static int getReccuranceId(String s) {
        int reccuranceId = 0;
        switch (s) {
            case "Daily":
                reccuranceId = 1;
                break;
            case "Weekly":
                reccuranceId = 2;
                break;
            case "Monthly":
                reccuranceId = 3;
                break;
            case "Yearly":
                reccuranceId = 4;
                break;
        }
        return reccuranceId;
    }
    private  static long getReccuranceAlarmId(int reccuranceId){

        long reccuranceAlarmID = 0;
        switch (reccuranceId) {
            case 1:
                reccuranceAlarmID = AlarmManager.INTERVAL_DAY;
                break;
            case 2:
                reccuranceAlarmID = 7*AlarmManager.INTERVAL_DAY;
                break;
            case 3:
                reccuranceAlarmID = 3;
                break;
            case 4:
                reccuranceAlarmID = 4;
                break;


        }
        return  reccuranceAlarmID;
    }
    public static  void scheduleNotification(Context context,String time, String TaskTitle, String TaskPrority, int rec_id, int remove, int reccurance_id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmRecever.class);
        myIntent.putExtra(Util.TASKTITILE, TaskTitle);
        myIntent.putExtra(Util.TASKPRIORITY, TaskPrority);
        myIntent.putExtra(Util.REC_ID, rec_id);
        myIntent.putExtra(Util.RECCURANCE_ID, reccurance_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                rec_id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (remove == 1) {
            alarmManager.cancel(pendingIntent);
        } else {
            switch (reccurance_id) {
                case 1:
                    Log.d(TAG, "scheduleNotification: Setting Alarm for Day");
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Util.getTimeinMilliSeconds(time),
                            AlarmManager.INTERVAL_DAY, pendingIntent);

                    break;
                case 2:
                    Log.d(TAG, "scheduleNotification: Setting Alarm for Week");

                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, Util.getTimeinMilliSeconds(time),
                            7 * AlarmManager.INTERVAL_DAY, pendingIntent);
                    break;
                default:

                    Log.d(TAG, "scheduleNotification : Setting One Time Alarm Alarm at : " + Util.getTimeinMilliSeconds(time));
                    alarmManager.set(AlarmManager.RTC, Util.getTimeinMilliSeconds(time), pendingIntent);
                    break;

            }
        }


    }

}
