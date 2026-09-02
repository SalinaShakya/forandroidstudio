package com.example.myview.data.local

import android.content.Context
import androidx.room.Room

//help
//import androidx.room.migration.Migration
//import androidx.sqlite.db.SupportSQLiteDatabase
object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
//                .addMigrations(MIGRATION_1_2) //this
                .fallbackToDestructiveMigration() // <--- ADD THIS LINE
                .build()


            INSTANCE = instance
            instance
        }
    }

    //help
//    private val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            // This SQL command creates the table exactly how your FavoriteEntity is defined
//            db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `price` REAL NOT NULL, `image` TEXT NOT NULL, PRIMARY KEY(`id`))")
//        }
//    }
}