package com.peanutbutterapps.notes.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.peanutbutterapps.notes.util.TimestampConverter;

import java.util.Date;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int key;

    private String title;

    private String description;

    private int color;

    @ColumnInfo(name = "create_edit_date")
    @TypeConverters({TimestampConverter.class})
    private Date creteEditDate;

    public Note(String title, String description, int color, Date creteEditDate) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.creteEditDate = creteEditDate;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getColor() {
        return color;
    }

    public Date getCreteEditDate() {
        return creteEditDate;
    }
}
