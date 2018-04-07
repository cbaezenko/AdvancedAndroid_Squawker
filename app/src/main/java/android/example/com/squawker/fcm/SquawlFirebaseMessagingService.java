package android.example.com.squawker.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawlFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();

        Log.d(TAG, "data is "+data);

        if (data.size() > 0) {

            sendNotification(data);
            addToScreen(data);
        }
    }

    private void sendNotification(Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        String message = data.get("message");

        if(message.length() > 30){
            message = message.substring(0, 30) +"\u2026";
        }

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_duck)
                .setContentTitle("My notification")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0, mBuilder.build());
    }

    private void addToScreen(final Map <String, String> data){

        final String test = data.get("test");
        final String author = data.get("author");
        final String authorKey = data.get("authorKey");
        final String message = data.get("message");
        final String date = data.get("date");

        AsyncTask<Void, Void, Void> insertSquawkTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(SquawkContract.COLUMN_AUTHOR, author);
                newMessage.put(SquawkContract.COLUMN_DATE, date);
                newMessage.put(SquawkContract.COLUMN_MESSAGE, message);
                newMessage.put(SquawkContract.COLUMN_AUTHOR_KEY, authorKey);
                getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, newMessage);
                return null;
            }
        };
        insertSquawkTask.execute();

    }
}
