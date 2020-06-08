package com.example.groceryitems.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.groceryitems.R;
import com.example.groceryitems.data.DataBaseHandler;
import com.example.groceryitems.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Item> itemList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public RecyclerViewAdapter(Context context , List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,null,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Item item = itemList.get(position);
            holder.itemName.setText(MessageFormat.format("Item: {0}", item.getItemName()));
            holder.itemQuantity.setText(MessageFormat.format("Quantity: {0}", item.getItemQuantity()));
            holder.itemSize.setText(MessageFormat.format("Size: {0}", item.getItemSize()));
            holder.itemColor.setText(MessageFormat.format("Color: {0}", item.getItemColor()));
            holder.itemNote.setText(MessageFormat.format("Note: {0}", item.getItemNote()));
//            holder.itemDate.setText(MessageFormat.format("Added on: {0}", item.getItemAddDate()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemSize;
        public TextView itemColor;
        public TextView itemNote;
        public TextView itemDate;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemSize = itemView.findViewById(R.id.itemSize);
            itemColor = itemView.findViewById(R.id.itemColor);
            itemNote = itemView.findViewById(R.id.itemNote);
            itemDate = itemView.findViewById(R.id.itemDate);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Item item = itemList.get(position);
            switch (view.getId()){
                case R.id.editButton:  editItem(item);
                    break;

                case R.id.deleteButton:  deleteItem(item.getId());
                    break;
            }
        }

        private void editItem(final Item newItem) {

            builder = new AlertDialog.Builder(context);
            final View view = LayoutInflater.from(context).inflate(R.layout.popup,null);

            final EditText itemName;
            final EditText itemQuantity;
            final EditText itemSize;
            final EditText itemColor;
            final EditText itemNote;
            Button saveButton;
            TextView title;

            itemName = view.findViewById(R.id.item_name);
            itemQuantity = view.findViewById(R.id.item_quantity);
            itemSize = view.findViewById(R.id.item_size);
            itemColor = view.findViewById(R.id.item_color);
            itemNote = view.findViewById(R.id.item_note);
            saveButton = view.findViewById(R.id.saveButton);
            title = view.findViewById(R.id.heading);

            title.setText(R.string.edit);
            saveButton.setText(R.string.update);

            itemName.setText(newItem.getItemName());
            itemQuantity.setText(newItem.getItemQuantity());
            itemSize.setText(newItem.getItemSize());
            itemColor.setText(newItem.getItemColor());
            itemNote.setText(newItem.getItemNote());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(context);

                    newItem.setItemName(itemName.getText().toString());
                    newItem.setItemQuantity(itemQuantity.getText().toString());
                    newItem.setItemSize(itemSize.getText().toString());
                    newItem.setItemColor(itemColor.getText().toString());
                    newItem.setItemNote(itemNote.getText().toString());

                    if(!itemName.getText().toString().isEmpty()){
                        dataBaseHandler.updateItem(newItem);
                        notifyItemChanged(getAdapterPosition(),newItem);
                    }else{
                        Snackbar.make(view,"Empty Field",Snackbar.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                }
            });
        }

        private void deleteItem(final int id){
            builder = new AlertDialog.Builder(context);
            View layout = LayoutInflater.from(context).inflate(R.layout.confirmation,null);
            Button noButton = layout.findViewById(R.id.noButton);
            Button yesButton = layout.findViewById(R.id.yesButton);
            builder.setView(layout);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
                    dataBaseHandler.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

    }
}
