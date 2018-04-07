package android.example.com.squawker.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebase";

    @Override
    public void onTokenRefresh(){
        //Get updated InstanceID token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "the token is "+refreshedToken);

        //If you want to send messages to this application instance or
        //manage this apps subscriptions on the server side, send the Instance ID
        //token to your app server
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token THe new token.
     */
    private void sendRegistrationToServer(String token){
        //This method is blank, but if you were to build a server that stores users token
        //information, this is where you'd send the token to the server.
    }
}
