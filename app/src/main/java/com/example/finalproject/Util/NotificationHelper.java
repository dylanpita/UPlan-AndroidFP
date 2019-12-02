package com.example.finalproject.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

import com.example.finalproject.Data.DatabaseHandler;
import com.example.finalproject.MainActivity;
import com.example.finalproject.Model.Event;
import com.example.finalproject.Model.Task;
import com.example.finalproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationHelper {

    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    public NotificationHelper(Context context) {
        mContext = context;
    }

    public void createNotification()
    {

        Intent intent = new Intent(mContext , MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);

        final DatabaseHandler db = new DatabaseHandler(mContext);

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        List<Event> eventList = db.getDayEvents(date);
        List<Task> taskList = db.getDayTasks(date);

        if (eventList.size() > 0 && taskList.size() > 0) {
            String [] startParts = eventList.get(0).getStart_date().split(" ");
            String [] endParts = eventList.get(0).getEnd_date().split(" ");
            String [] dueParts = taskList.get(0).getEnd_date().split(" ");
            mBuilder.setSmallIcon(R.drawable.calendar);
            mBuilder.setContentTitle("UPlan Reminder!")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Events Today:")
                            .addLine(" • "+eventList.get(0).getName()+ " at "+startParts[1]+" till "+endParts[1])
                            .addLine("Tasks due Today:")
                            .addLine(" • "+taskList.get(0).getName()+" at "+dueParts[1])
                            .addLine("...and more, check your app!"))
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);
        }
        else if (eventList.size() == 0 && taskList.size() > 0) {
            String [] dueParts = taskList.get(0).getEnd_date().split(" ");
            mBuilder.setSmallIcon(R.drawable.calendar);
            mBuilder.setContentTitle("UPlan Reminder!")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Tasks due Today:")
                            .addLine(" • "+taskList.get(0).getName()+" at "+dueParts[1])
                            .addLine("...and more, check your app!"))
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);
        }
        else if (eventList.size() > 0 && taskList.size() == 0) {
            String [] startParts = eventList.get(0).getStart_date().split(" ");
            String [] endParts = eventList.get(0).getEnd_date().split(" ");
            mBuilder.setSmallIcon(R.drawable.calendar);
            mBuilder.setContentTitle("UPlan Reminder!")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Events Today:")
                            .addLine(" • "+eventList.get(0).getName()+ "at "+startParts[1]+" till "+endParts[1])
                            .addLine("...and more, check your app!"))
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);
        }
        else {
            mBuilder.setSmallIcon(R.drawable.calendar);
            mBuilder.setContentTitle("UPlan Reminder!")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Nothing today?")
                            .addLine("Try planning things with the app!"))
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}
