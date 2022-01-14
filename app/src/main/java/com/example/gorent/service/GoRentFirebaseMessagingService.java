package com.example.gorent.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.gorent.R;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.AuthenticationRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GoRentFirebaseMessagingService extends FirebaseMessagingService {

    private AuthenticationRepository authenticationRepository;

    private final List<Disposable> disposables = new ArrayList<>();

    private static final String TAG = "MESSAGING_SERVICE";

    private static final String CHANNEL_ID = "GORENT_CHANNEL_ID";

    private static final String CHANNEL_NAME = "GORENT_CHANNEL";

    private static final int NOTIFICATION_ID = 12;

    private NotificationManager notificationManager;

    public GoRentFirebaseMessagingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.authenticationRepository = RepositoryManager.getInstance(getApplicationContext()).getAuthenticationRepository();
        this.notificationManager = getSystemService(NotificationManager.class);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("REFRESH FCM TOKEN", token);
        disposables.add(authenticationRepository.refreshFCMToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.d("TOKEN Refreshed", token);
                }, error -> {
                    error.printStackTrace();
                    Log.e("TOKEN Refresh Error", error.getMessage());
                }));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message Payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_bedroom)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSilent(false);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disposables.forEach(Disposable::dispose);
        return super.onUnbind(intent);
    }


    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}