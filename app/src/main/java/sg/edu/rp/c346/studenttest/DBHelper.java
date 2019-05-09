package sg.edu.rp.c346.studenttest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "stu.db";

    private static final String TABLE_STUDENT = "student";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_GRADE = "gpa";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_STUDENT + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_GRADE + " TEXT )";
        db.execSQL(createTable);
        Log.i("info", "created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    public void insertStudent(String name, String grade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_GRADE, grade);
        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }

    public ArrayList<String> getStudentContent() {
        //create arraylist that holds string objects
        ArrayList<String> tasks = new ArrayList<String>();
        //select all tasks' description
        String selectQuery = "SELECT " + COLUMN_NAME + COLUMN_GRADE + " FROM " + TABLE_STUDENT;

        //Get instance of database to read
        SQLiteDatabase db = this.getReadableDatabase();
        //run SL and get back Cursor object
        Cursor cursor = db.rawQuery(selectQuery, null);

        //moveToFirst() moves to first row
        if (cursor.moveToFirst()) {
            //loop while moveToNest() points to next row and return truel moveToNest() returns false when no more next row to move to
            do {
                //add task content to arrayList, 0 in getString(0) return data in the first position.
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        //close connection
        cursor.close();
        db.close();

        return tasks;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> tasks = new ArrayList<Student>();
        String selectQuery = "SELECT " + COLUMN_ID + ", "
                + COLUMN_NAME + ", "
                + COLUMN_GRADE
                + " FROM " + TABLE_STUDENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                double grade = cursor.getDouble(2);
                Student obj = new Student(id, name, grade);
                tasks.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
