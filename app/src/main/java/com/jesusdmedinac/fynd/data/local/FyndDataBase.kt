package com.jesusdmedinac.fynd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jesusdmedinac.fynd.data.local.model.HostUser


@Database(
    entities = [HostUser::class],
    version = 1,
)
abstract class FyndDataBase : RoomDatabase() {
    abstract fun hostDao(): HostDao
}