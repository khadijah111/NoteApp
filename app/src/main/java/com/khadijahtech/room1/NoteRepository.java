package com.khadijahtech.room1;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public void insert(Note note) {

        new InsertNoteAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(mNoteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllNoteAsyncTask(mNoteDao).execute();
    }

    public LiveData<List<Note>> getmAllNotes() {
        return mAllNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
       //need note dao because we need DB operations
        private NoteDao noteDao1;

        private InsertNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao1 = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            this.noteDao1.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        //need note dao because we need DB operations
        private NoteDao noteDao1;

        private DeleteNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao1 = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            this.noteDao1.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        //need note dao because we need DB operations
        private NoteDao noteDao1;

        private DeleteAllNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao1 = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            this.noteDao1.deleteAllNotes();
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        //need note dao because we need DB operations
        private NoteDao noteDao1;

        private UpdateNoteAsyncTask(NoteDao noteDao)
        {
            this.noteDao1 = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            this.noteDao1.update(notes[0]);
            return null;
        }
    }
    // Other data sources
    /** Single source of truth
     * When we have another sources of data (persistent, model, web service, cache, etc...)
     * we should save them into room database. And we must read only from one source which is
     * our Room database with the help from LiveData.
     *
     * More info at (good examples fetching from API):
     * https://developer.android.com/jetpack/docs/guide#truth
     *
     * And, if we have two LiveData and we want to marge them we should use MediatorLiveData.
     */
}
