package com.example.groceryitems.model;

public class Item {

    private int id;
    private String itemName ;
    private String itemQuantity;
    private String itemSize ;
    private String itemColor;
    private String itemNote;
    private String itemAddDate;

    public Item(){

    }

    public Item(String itemName, String itemQuantity, String itemSize, String itemColor, String itemNote, String itemAddDate) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemSize = itemSize;
        this.itemColor = itemColor;
        this.itemNote = itemNote;
        this.itemAddDate = itemAddDate;
    }

    public Item(int id, String itemName, String itemQuantity, String itemSize, String itemColor, String itemNote, String itemAddDate) {
        this.id = id;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemSize = itemSize;
        this.itemColor = itemColor;
        this.itemNote = itemNote;
        this.itemAddDate = itemAddDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public String getItemAddDate() {
        return itemAddDate;
    }

    public void setItemAddDate(String itemAddDate) {
        this.itemAddDate = itemAddDate;
    }
}
