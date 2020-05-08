package com.example.mansouracourseroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    PersonDatabase personDatabase;
    RecyclerView recyclerView;
    PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.main_rv);


        personDatabase = PersonDatabase.getInstance(MainActivity.this);

        getAllPersonRxJava();

    }

    private void getAllPersonByThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Person> personList = personDatabase.personDao().getAllPerson();

                for (Person person1 : personList) {
                    Log.i("MainActivity", person1.toString());
                }
                Log.i("MainActivity", "Finish");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        for (Person person1 : personList) {
//                            textView.append("\n" + person1.toString());
//                        }

                    }
                });

            }

        });
        thread.start();
    }

    @SuppressLint("CheckResult")
    private void getAllPersonRxJava() {
        Single.fromCallable(new Callable<List<Person>>() {
            @Override
            public List<Person> call() throws Exception {
                return personDatabase.personDao().getAllPerson();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<Person>>() {
                            @Override
                            public void accept(List<Person> personList) throws Exception {
//                                for (Person person : personList) {
//                                    textView.append("\n" + person.toString());
//                                }
                                personAdapter = new PersonAdapter(personList, adapterClick);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                recyclerView.setAdapter(personAdapter);
                            }
                        }
                        ,
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }

    @SuppressLint("CheckResult")
    private void getAllPersonRxJavaLamda() {
        Single.fromCallable(() -> personDatabase.personDao().getAllPerson())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        personList -> {
//                            for (Person person : personList) {
//                                textView.append("\n" + person.toString());
//                            }
                        }
                        ,
                        throwable -> Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }

    @SuppressLint("CheckResult")
    private void insertPerson(Person person) {
        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                personDatabase.personDao().insertPerson(person);
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                Toast.makeText(MainActivity.this, "Person insert", Toast.LENGTH_SHORT).show();
                            }
                        }
                        ,
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }


    PersonAdapterClick adapterClick = new PersonAdapterClick() {
        @Override
        public void onItemClick(Person person) {
            deletePerson(person);
        }
    };

    @SuppressLint("CheckResult")
    private void deletePerson(Person person) {
        Single.fromCallable((Callable<Boolean>) () -> {
            personDatabase.personDao().deletePerson(person);
            return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success ->
                        {
                            Toast.makeText(this, "Person Delete", Toast.LENGTH_SHORT).show();
                            getAllPersonRxJava();
                        }
                        ,
                        failed -> Toast.makeText(this, failed.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }
}
