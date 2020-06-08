package com.example.groceryitems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.groceryitems.data.DataBaseHandler;
import com.example.groceryitems.model.Item;
import com.example.groceryitems.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DataBaseHandler dataBaseHandler;
    private FloatingActionButton floatButton;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private Button saveButton;
    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemSize;
    private EditText itemColor;
    private EditText itemNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        floatButton = findViewById(R.id.floatButton);

        dataBaseHandler = new DataBaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //get data from database handler
        itemList = dataBaseHandler.getAllItem();
        recyclerViewAdapter = new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog();
            }
        });
    }

    private void popUpDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        itemName = view.findViewById(R.id.item_name);
        itemQuantity = view.findViewById(R.id.item_quantity);
        itemSize = view.findViewById(R.id.item_size);
        itemColor = view.findViewById(R.id.item_color);
        itemNote = view.findViewById(R.id.item_note);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!itemName.getText().toString().isEmpty()){
                    saveItem(view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),ListActivity.class));
                            finish();
                        }
                    },1200);
                }else {
                    Snackbar.make(view,"Add Item",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void saveItem(View view) {

        Item item = new Item();

        String newItemName = itemName.getText().toString().trim();
        String newItemQuantity = itemQuantity.getText().toString().trim();
        String newItemSize = itemSize.getText().toString().trim();
        String newItemColor = itemColor.getText().toString().trim();
        String newItemNote = itemNote.getText().toString().trim();

        item.setItemName(newItemName);
        item.setItemQuantity(newItemQuantity);
        item.setItemSize(newItemSize);
        item.setItemColor(newItemColor);
        item.setItemNote(newItemNote);

        dataBaseHandler.addItem(item);

        Snackbar.make(view,"Item saved",Snackbar.LENGTH_SHORT).show();
    }
}