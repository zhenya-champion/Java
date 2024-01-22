package com.zhenya.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    //создание бд
    private static final String db_name = "task_manager";
    private static final int db_version = 1;

    private static final String db_table = "task";
    private static final String db_column = "taskName";

    public DataBase(@Nullable Context context) {
        super(context, db_name, null, db_version);
    }

    //создание таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL);", db_table, db_column);
        db.execSQL(query);
    }
    //изменение бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXIST %s", db_table); // удаляем таблицу
        db.execSQL(query);
        onCreate(db); //заново создаем таблицу
    }

    //задача которую хотим добавить
    public void insertData(String task){
        SQLiteDatabase db = this.getWritableDatabase(); //для редактирования бд
        ContentValues values = new ContentValues(); //указание значение данных
        values.put(db_column, task); // что передаем
        db.insertWithOnConflict(db_table, null, values, SQLiteDatabase.CONFLICT_REPLACE); //помещаем
    }

    //задача которую хотим удалить
    public void deleteData(String taskName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(db_table, db_column + " = ?", new String[] {taskName}); // удаляем то что заносили
        db.close();
    }

    //получить данные из бд
    public ArrayList<String> getAllTask(){
        ArrayList<String> allTask = new ArrayList<>(); //создаем список
        SQLiteDatabase db = this.getReadableDatabase(); //подключаемся к бд только чтение
        Cursor cursor = db.query(db_table, new String[]{db_column}, null, null, null, null, null); //получаем записи из таблицы
        while (cursor.moveToNext()){ // перебираем все записи
            int index = cursor.getColumnIndex(db_column); //берем индекс записи
            allTask.add(cursor.getString(index)); //по индексу берем значение
        }
        cursor.close();
        db.close();
        return allTask;
    }
}
