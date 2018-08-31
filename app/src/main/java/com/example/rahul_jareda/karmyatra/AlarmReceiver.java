package com.example.rahul_jareda.karmyatra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.example.rahul_jareda.karmyatra.MainActivity.movementDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        final Boolean insertDbRequired = false;
        //Log.i("Alarm", "Fired");
        final Context onReceiveContext = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Movements movement[] = movementDatabase.daoAccess().loadAllMovements();
                int size = movement.length;
                Log.i("size:", "" + size);
                int i;
                for (i = 0; i < size; i++) {
                    insertDbOrServer.insertDbOrServer_method(movement[i], insertDbRequired,onReceiveContext);
                }
            }
        }).start();
    }
}
