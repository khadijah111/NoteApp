package com.khadijahtech.room1;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.os.AsyncTask;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public static NoteDatabase instance;

    public abstract NoteDao noteDao();

    //singleton
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //the first time DB is opened
            new PopulateDbAsync(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //Every time DB opened
        }
    };

    //thread
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private NoteDao notedao;

        public PopulateDbAsync(NoteDatabase db) {
            this.notedao = db.noteDao();
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            //INSERT SOME INITIAL NOTES FOR THE APP
            notedao.insert(new Note("note 1", "hellow 1", 1));
            notedao.insert(new Note("note 2", "hellow 2", 2));
            notedao.insert(new Note("note 3", "hellow 3", 3));
            return null;
        }
    }
}
