package com.example.rahul_jareda.karmyatra;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;

import static com.example.rahul_jareda.karmyatra.MainActivity.movementDatabase;

public class SyncWorker extends Worker {
    @Override
    public Worker.Result doWork(){

        final Boolean insertDbRequired = false;
        Movements movement[] = movementDatabase.daoAccess().loadAllMovements();
        int size = movement.length;
        //Log.i("size:", "" + size);
        int i;
        for (i = 0; i < size; i++) {
            insertDbOrServer.insertDbOrServer_method(movement[i], insertDbRequired, getApplicationContext());
        }
        //Log.w("Worker",": Worker worked");
        return Result.SUCCESS;
    }
}
