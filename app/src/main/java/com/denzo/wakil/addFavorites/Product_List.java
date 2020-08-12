package com.denzo.wakil.addFavorites;

public class Product_List {
    private int id;
    private String name;
    private String image;

    public Product_List(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
