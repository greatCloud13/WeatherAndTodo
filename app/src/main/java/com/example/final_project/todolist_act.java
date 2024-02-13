package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class todolist_act extends AppCompatActivity {
    ListView list;
    ArrayList<String> items=new ArrayList<>();
    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);
        CustomList adapter = new CustomList(todolist_act.this);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);




        Button b = (Button) findViewById(R.id.done);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button a =(Button)findViewById(R.id.add_todo);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.add("list");
                adapter.notifyDataSetChanged();

            }
        });

        Button remove = (Button)findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.clear();
                adapter.notifyDataSetChanged();
            }
        });





    }



    public class CustomList extends ArrayAdapter<String>{
        private final Activity context;
        public CustomList(Activity context){
            super(context, R.layout.listitem, items);
            this.context = context;
        }
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.listitem, null, true);
            TextView proc = (TextView) rowView.findViewById(R.id.proceeding);
            Button btn_done = (Button) rowView.findViewById(R.id.btn_done);
            TextView name = (TextView) rowView.findViewById(R.id.name);
            EditText detail =(EditText) rowView.findViewById(R.id.detail);
            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    proc.setText("완료");

                }
            });
            SharedPreferences preferences = getSharedPreferences( "temp" , MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();



            return rowView;
        }



    }

}