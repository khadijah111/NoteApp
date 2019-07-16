package com.khadijahtech.room1;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    //here the Entity is a table in DB

    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String mTitle;
    private String mDescription;
    private int mPriority;

    public Note(String title, String description, int priority) {
        mTitle = title;
        mDescription = description;
        mPriority = priority;
    }

    public void setId(int id)
    {
        this.Id = id;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getPriority() {
        return mPriority;
    }


}
