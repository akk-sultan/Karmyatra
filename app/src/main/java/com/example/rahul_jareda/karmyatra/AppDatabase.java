package com.example.rahul_jareda.karmyatra;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Movements.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract DaoAccess daoAccess();
}

