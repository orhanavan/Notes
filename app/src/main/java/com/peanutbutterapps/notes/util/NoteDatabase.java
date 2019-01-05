package com.peanutbutterapps.notes.util;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.peanutbutterapps.notes.R;
import com.peanutbutterapps.notes.model.Note;

import java.util.Calendar;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();


    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note(
                    "Welcome to the Notes !",
                    "Greetings from Peanut Butter Apps. Thank you for choosing us. " +
                            "To add first note, press the + icon to the top.",
                    R.color.red, Calendar.getInstance().getTime()));

            noteDao.insert(new Note(
                    "To delete a note",
                    "Just swipe note :)",
                    R.color.yellow, Calendar.getInstance().getTime()));

            noteDao.insert(new Note(
                    "To learn more",
                    "Swipe left or click hamburger icon in the top left corner, then click Help tab.",
                    R.color.blue, Calendar.getInstance().getTime()));

            return null;
        }
    }


}
