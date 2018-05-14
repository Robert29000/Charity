package com.example.melikyan.charity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by melikyan on 02.05.2018.
 */

public class OnMoneyChangedService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String uid=intent.getStringExtra("UID");
        FirebaseThread thread=new FirebaseThread(uid);
        thread.start();
        return START_REDELIVER_INTENT;
    }

    private class FirebaseThread extends Thread{
        String uid;

        FirebaseThread(String uid){
            this.uid=uid;
        }

        @Override
        public void run() {
            ChildEventListener childEventListener=new ChildEventListener() {
                int moneyin;
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("EASY",dataSnapshot.toString());
                    moneyin=(int)dataSnapshot.getValue();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.d("EASY",dataSnapshot.toString());
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_logo_charity1).setContentTitle("Charity").setContentText("Вам зачислено"+" "+
                                    ((int)dataSnapshot.getValue()-moneyin));
                    Notification notification=builder.build();
                    NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notification);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference data= FirebaseDatabase.getInstance().getReference().child("Annoucments").child(uid).child("moneyincome");
            data.addChildEventListener(childEventListener);
        }
    }
}
