package com.example.scream;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class policeoption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policeoption);
        ListView listView2;
            listView2=findViewById(R.id.lista1);
            final ArrayList<String> arrayList=new ArrayList<>();

            arrayList.add("Azampur Thana");
            arrayList.add("Banani Thana");
            arrayList.add("Badda Thana");
            arrayList.add("Cantonment Thana");
            arrayList.add("Chowkbazar Thana");
            arrayList.add("Dhanmondi Thana");
            arrayList.add("Gulshan Thana");
            arrayList.add("Kafrul Thana");
            arrayList.add("Khilgaon Thana");
            arrayList.add("Khilkhet Thana");
            arrayList.add("Lalbagh Thana");
            arrayList.add("Mirpur Model Thana");
            arrayList.add("Mohammadpur Thana");
            arrayList.add("Motijheel Thana");
            arrayList.add("New Market Thana");
            arrayList.add("Pallabi Thana");
            arrayList.add("Ramna Thana");
            arrayList.add("Rampura Thana");
            arrayList.add("Sabujbagh Thana");
            arrayList.add("Shah Ali Thana");
            arrayList.add("Tejgaon Thana");
            arrayList.add("Uttara Thana");

            ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

            listView2.setAdapter(arrayAdapter);

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(policeoption.this,"clicked item:"+i+" "+arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}