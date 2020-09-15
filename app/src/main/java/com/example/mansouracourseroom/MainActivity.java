package com.example.mansouracourseroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    EditText editTextName, editTextSpec;
    Button buttonAdd;
    RecyclerView recyclerView;
    PersonDatabase personDatabase;
    PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.main_name_et);
        editTextSpec = findViewById(R.id.main_spec_et);
        buttonAdd = findViewById(R.id.main_add_btn);
        recyclerView = findViewById(R.id.main_result_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        personDatabase = PersonDatabase.getInstance(MainActivity.this);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String spec = editTextSpec.getText().toString();

                Person person = new Person(name, spec);

                personDatabase.personDao().insertPerson(person);

                final List<Person> personList = personDatabase.personDao().getAllPerson();

                personAdapter = new PersonAdapter(personList);

                recyclerView.setAdapter(personAdapter);
//                for (Person person1 : personList) {
//                    textViewResults.append("\n" + person1.toString());
//                }

            }
        });



    }


}
