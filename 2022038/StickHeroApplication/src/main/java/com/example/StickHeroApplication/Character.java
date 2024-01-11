package com.example.StickHeroApplication;

import javafx.scene.image.ImageView;

public class Character {
    private int cherries_collected;
    private ImageView imageView;

    // Constructor
    public Character(int cherriesCollected) {
        cherries_collected = cherriesCollected;
    }

    public int getCherries_collected() {
        return cherries_collected;
    }

    public void setCherries_collected(int cherries_collected) {
        this.cherries_collected = cherries_collected;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
