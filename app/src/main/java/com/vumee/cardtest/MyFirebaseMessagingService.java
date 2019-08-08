package com.vumee.cardtest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "";

public static String url2="";

    public static String getUrl() {

        return String.valueOf(url2);
    }

    public static void setUrl(String url) {
        MyFirebaseMessagingService.url2 = url;
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage = data.getString("extra_information");
                MyFirebaseMessagingService.setUrl(jsonMessage);
                Log.d(TAG, "onMessageReceived: \n" +
                        "Extra Information: " + jsonMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            String link = remoteMessage.getNotification().getTag();
            String click_action = remoteMessage.getNotification().getClickAction();
            sendNotification(link, title, body, remoteMessage, click_action);
        }
    }
    //This method is only generating push notification
//It is same as we did in earlier posts
    private void sendNotification(String link, String title, String body, RemoteMessage remoteMessage, String click_action) {
        Intent intent;
        if (click_action.startsWith("https")){
            intent = new Intent(getApplicationContext(), notification_lp.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.splash_logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText((remoteMessage.getNotification().getBody()+(remoteMessage.getNotification().getTag()))).setAutoCancel(true).setContentIntent(pendingIntent);
            MyFirebaseMessagingService.setUrl(remoteMessage.getNotification().getTag());
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
        }

    }

}
