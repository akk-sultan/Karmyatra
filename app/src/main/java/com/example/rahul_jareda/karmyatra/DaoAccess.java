package com.example.rahul_jareda.karmyatra;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface DaoAccess {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert (Movements movements);

    @Query("SELECT * FROM movements")
    public Movements[] loadAllMovements();

    @Delete
    void delete (Movements movements);
}
