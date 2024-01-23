package com.zhenya.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DataBase dataBase; //бд
    private ListView listView; //ссылаемся на ListView
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase(this);
        listView = findViewById(R.id.task_list);

        loadAllTask();
    }

    //получаем все записи и выводим
    private void loadAllTask() {
        ArrayList<String> allTask = dataBase.getAllTask(); //набор списка
        if(arrayAdapter == null){ //при старте
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.task_list_row, R.id.text_label_low, allTask);
            listView.setAdapter(arrayAdapter);
        }else {
            arrayAdapter.clear();
            arrayAdapter.addAll(allTask);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    //переписываем меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // указываем что будем использовать свое меню вместо стандартного

        Drawable icon = menu.getItem(0).getIcon(); //обращаемся к иконке
        icon.mutate(); //чтобы могли изменять
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

        return super.onCreateOptionsMenu(menu);
    }

    //отсеживаем нажатие на кнопку меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_new_task){ // если нажали на кнопку
            final EditText userTaskField = new EditText(this); // поле ввода информации
            AlertDialog dialog = new AlertDialog.Builder(this) // создаем всплывающее окно
                    .setTitle("Добавление нового задания")
                    .setMessage("Чтобы вы хотели добавить")
                    .setView(userTaskField)
                    .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(userTaskField.getText()); //получаем текст который ввел пользователь
                            dataBase.insertData(task);// добавление записи в бд
                            loadAllTask(); //обновляем записи
                        }
                    })
                    .setNegativeButton("Ничего", null) //будет скрываться
                    .create(); //создаем диалоговое окно
            dialog.show(); //показать окно
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //кнопка удалить
    public void deleteTask(View button){
        View parent = (View)button.getParent();
        TextView textView = parent.findViewById(R.id.text_label_low);
        String task = String.valueOf(textView.getText()); //получаем текст
        dataBase.deleteData(task);//указываем текст по которому удалим запись
        loadAllTask(); //обновляем записи
    }
}