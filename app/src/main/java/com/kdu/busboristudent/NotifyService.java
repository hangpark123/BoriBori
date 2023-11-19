package com.kdu.busboristudent;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.kdu.busbori.R;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class NotifyService extends Service {
    private Thread scanthread;
    boolean isrunning = true;
    private SharedPreferences sharedPreferences;
    private Set<String> notifyList;
    private String destination;
    private String type;
    private String time;
    private String state;
    private String Run;
    private NotificationManager mNotificationManager;
    private NotificationChannel channel;
    private NotificationCompat.Builder notification;
    private Intent Intent;
    private PendingIntent pendingIntent;
    private Set<String> sentNotifications = new HashSet<>();
    private Set<String> RunNotifications = new HashSet<>();
    @Override
    public void onCreate(){
        super.onCreate();
        Intent = new Intent(getApplicationContext(), MainActivity.class);
        pendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("Student_channel", "버스 운행 알림",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
            mNotificationManager.createNotificationChannel(channel);
        }
        BasicAWSCredentials credentials = new BasicAWSCredentials("AKIATR4KVIIS74DNZLGD", "KAjjuxbXJE6n1lyeYTuBfikvjtUq8BkQhQ6BUQOB");
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        sharedPreferences = getSharedPreferences("notify", Context.MODE_PRIVATE);
        notifyList = sharedPreferences.getStringSet("notify", new LinkedHashSet<>());
        scanToDynamoDB(client);
    }

    @SuppressLint("NotificationId0")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification
                = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentText("경동셔틀 실행 중~");

        mNotificationManager.notify(1, notification.build());
        startForeground(1, notification.build());
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isrunning = false;
        scanthread.interrupt();
        sentNotifications.clear();
        stopForeground(true);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("NotificationId0")
    private void scanToDynamoDB(AmazonDynamoDBClient client){
        scanthread = new Thread(() -> {
            while (isrunning) {
                ScanRequest request = new ScanRequest()
                        .withTableName("Borigps");
                ScanResult result = client.scan(request);
                List<Map<String, AttributeValue>> items = result.getItems();
                if (!(scanthread.isInterrupted())) {
                    for (Map<String, AttributeValue> item : items) {
                        String settime = "null";
                        destination = item.get("destination") != null ? Objects.requireNonNull(item.get("destination")).getS() : null;
                        type = item.get("type") != null ? Objects.requireNonNull(item.get("type")).getS() : null;
                        time = item.get("time") != null ? Objects.requireNonNull(item.get("time")).getS() : null;
                        state = item.get("State") != null ? Objects.requireNonNull(item.get("State")).getS() : null;
                        Run = item.get("Run") != null ? Objects.requireNonNull(item.get("Run")).getS() : null;

                        String[] parts = time.split(" ");
                        if (parts.length > 1) {
                            settime = parts[0];
                        }
                        String combine = destination + type + settime;
                        if (notifyList.contains(combine)) {
                            String notificationKey = destination + type + settime + state;
                            String RunnotificationKey = destination + type + settime + Run;
                            if (Run.equals("운행 중")) {
                                if (!RunNotifications.contains(RunnotificationKey)) {
                                    RunNotifications.add(RunnotificationKey);
                                    notification
                                            = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentIntent(pendingIntent)
                                            .setContentTitle(destination + "[" + type + "] " + time)
                                            .setContentText("셔틀버스가 운행을 시작했습니다.");
                                    mNotificationManager.notify(1, notification.build());
                                }
                                if (!sentNotifications.contains(notificationKey)) {
                                    sentNotifications.add(notificationKey);
                                    switch (state) {
                                        case "1":
                                            notification
                                                    = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle(destination + "[" + type + "] " + time)
                                                    .setContentText("셔틀버스가 " + destination + "에 도착했습니다.");

                                            mNotificationManager.notify(2, notification.build());
                                            break;
                                        case "2":
                                            notification
                                                    = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle(destination + "[" + type + "] " + time)
                                                    .setContentText("셔틀버스가 " + destination + "에서 출발했습니다.");

                                            mNotificationManager.notify(3, notification.build());
                                            break;
                                        case "3":
                                            notification
                                                    = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle(destination + "[" + type + "] " + time)
                                                    .setContentText("셔틀버스가 학교에 도착했습니다.");

                                            mNotificationManager.notify(4, notification.build());
                                            break;
                                        case "4":
                                            notification
                                                    = new NotificationCompat.Builder(getApplicationContext(), "Student_channel")
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentIntent(pendingIntent)
                                                    .setContentTitle(destination + "[" + type + "] " + time)
                                                    .setContentText("셔틀버스가 학교에서 출발했습니다.");

                                            mNotificationManager.notify(5, notification.build());
                                            break;
                                    }
                                }
                            } else {
                                sentNotifications.clear();
                                RunNotifications.clear();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isrunning = false;
                    if (scanthread.isInterrupted()){
                        isrunning = true;
                        scanthread.start();
                    }
                }
            }
        });
        scanthread.start();
    }
}