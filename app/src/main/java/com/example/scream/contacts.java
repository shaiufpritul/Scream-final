package com.example.scream;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class contacts extends AppCompatActivity {

    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listView1=findViewById(R.id.lista);
        final ArrayList<String> arrayList=new ArrayList<>();

        arrayList.add("Mike Shinoda                                                                                           01711234688");
        arrayList.add("Brett Lee                                                                                              01321820939");
        arrayList.add("Shanon Bruen                                                                                           01786435684");
        arrayList.add("Granville Leannon                                                                                      01578465874");
        arrayList.add("Emelie Nader                                                                                           01675438743");
        arrayList.add("Ryan Rogers                                                                                            01845745754");
        arrayList.add("Amanda Morgan                                                                                          01747854445");
        arrayList.add("Ethan Hunt                                                                                             01256747844");
        arrayList.add("Abbas Ali                                                                                              02880747434");


        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listView1.setAdapter(arrayAdapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(contacts.this,"clicked item:"+i+" "+arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


