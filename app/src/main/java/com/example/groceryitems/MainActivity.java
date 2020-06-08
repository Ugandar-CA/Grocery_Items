package com.example.groceryitems;

import android.content.Intent;
import android.os.Bundle;

import com.example.groceryitems.data.DataBaseHandler;
import com.example.groceryitems.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemSize;
    private EditText itemColor;
    private EditText itemNote;
    private DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popUpActivity();
            }
        });

        dataBaseHandler = new DataBaseHandler(this);
        byPassActivity();

    }

    private void byPassActivity() {

        if(dataBaseHandler.getItemCount() > 0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
    }

    private void popUpActivity() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        itemName = view.findViewById(R.id.item_name);
        itemQuantity = view.findViewById(R.id.item_quantity);
        itemSize = view.findViewById(R.id.item_size);
        itemColor = view.findViewById(R.id.item_color);
        itemNote = view.findViewById(R.id.item_note);
        saveButton = view.findViewById(R.id.saveButton);

        builder.setView(view);

        dialog = builder.create(); // creating dialog object
        dialog.show(); // important step

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!itemName.getText().toString().isEmpty()){
                    saveItem(view);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this,ListActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}