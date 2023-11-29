package com.example.noteapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.example.noteapp.Model.Folder;
import com.example.noteapp.Model.Note;

import java.util.ArrayList;
public class DatabaseForApp extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NoteApp";

    public DatabaseForApp(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        int id, String title, String content, String createTime, String statusN, String imagePath, int idFolder
        String CREATE_TABLE_FOLDER = " CREATE TABLE Folder(id INTEGER PRIMARY KEY , name TEXT , statusG TEXT )";
        String CREATE_TABLE_NOTE = "CREATE TABLE Note(id INTEGER PRIMARY KEY , title TEXT , contentN TEXT , createTime TEXT , statusN TEXT , imagePath TEXT , idFolder INTEGER)";

        db.execSQL(CREATE_TABLE_FOLDER);
        db.execSQL(CREATE_TABLE_NOTE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Folder");
        db.execSQL("DROP TABLE IF EXISTS Note");
        onCreate(db);
    }

    public long addFolder(Folder folder)  throws Exception{
        SQLiteDatabase db = null;
        long id;
        try {
            //mo ket noi
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("name", folder.getName());
            values.put("statusG",folder.getStatusG());

            // Inserting Row, tra ve id tu tang
            id = db.insert("Folder", "", values);

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            db.close();
        }
        return id;
    }
    public long addNote(Note note) throws Exception{
        SQLiteDatabase db = null;
        long id;
        try{
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("title",note.getTitle());
            values.put("contentN",note.getContent());
            values.put("createTime",note.getCreateTime());
            values.put("statusN",note.getStatusN());
            values.put("imagePath",note.getImagePath());
            values.put("idFolder",note.getIdFolder());

            id = db.insert("Note","",values);
        }
        catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        finally {
            db.close();
        }
        return id;
    }

    public ArrayList<Folder> getAllFolder() {
        ArrayList<Folder> folderList = new ArrayList<Folder>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Folder WHERE statusG = 'active'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Folder folder = new Folder();
                folder.setId(cursor.getInt(0));
                folder.setName(cursor.getString(1));
                folder.setStatusG(cursor.getString(2));

                folderList.add(folder);
            } while (cursor.moveToNext());
        }
        return folderList;
    }
    public ArrayList<Note> getAllNote(){
        ArrayList<Note> noteList = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM Note WHERE statusN = 'active'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
//                id INTEGER PRIMARY KEY , title TEXT , contentN TEXT , createTime TEXT , statusN TEXT , imagePath TEXT , idFolder INTEGER
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setCreateTime(cursor.getString(3));
                note.setStatusN(cursor.getString(4));
                note.setImagePath(cursor.getString(5));
                note.setIdFolder(cursor.getInt(6));

                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }
    public ArrayList<Note> getAllNoteDelete(){
        ArrayList<Note> noteListDelete = new ArrayList<Note>();
        String selectQuery = "SELECT  * FROM Note WHERE statusN = 'not active'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setCreateTime(cursor.getString(3));
                note.setStatusN(cursor.getString(4));
                note.setImagePath(cursor.getString(5));
                note.setIdFolder(cursor.getInt(6));

                noteListDelete.add(note);
            } while (cursor.moveToNext());
        }
        return noteListDelete;
    }
    public int updateFolder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", folder.getName());
        values.put("statusG", folder.getStatusG());

        // updating row
        return db.update("Folder", values, "id = ?",
                new String[]{String.valueOf(folder.getId())});
    }
    public int updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues ct = new ContentValues();
        ct.put("title",note.getTitle());
        ct.put("contentN",note.getContent());
        ct.put("createTime",note.getCreateTime());
        ct.put("statusN",note.getStatusN());
        ct.put("imagePath",note.getImagePath());
        ct.put("idFolder",note.getIdFolder());

        return  db.update("Note",ct,"id = ?",new String[]{String.valueOf(note.getId())});
    }
    public int getFolderCount() {
        String countQuery = "SELECT  * FROM Folder";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    public int getNoteCount(){
        String countQuery = "SELECT  * FROM Note";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cs = db.rawQuery(countQuery,null);
        cs.close();

        return cs.getCount();
    }
}
