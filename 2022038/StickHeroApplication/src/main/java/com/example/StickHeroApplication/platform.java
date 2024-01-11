package com.example.StickHeroApplication;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class platform extends Rectangle {

    private final Random random = new Random();
    private final double screenWidth = 600;
    private static final double PLATFORM_HEIGHT = 100;
    private static final double minWidth = 20;
    private static final double maxWidth = 100;
    private static final double layoutY = 300;

    public Rectangle initializePlatform() {
        Rectangle platform = new Rectangle();
        platform.setHeight(PLATFORM_HEIGHT);
        platform.setFill(Color.FIREBRICK);
        platform.setStroke(Color.YELLOW);
        platform.setLayoutY(layoutY);
        platform.setLayoutX(50);
        platform.setWidth(90);

        return platform;
    }

    public Rectangle generatePlatform() {
        Rectangle platform = new Rectangle();
        platform.setWidth(random.nextDouble() * (maxWidth - minWidth) + minWidth);
        platform.setHeight(PLATFORM_HEIGHT);
        platform.setFill(Color.FIREBRICK);
        platform.setLayoutY(layoutY);
        platform.setStroke(Color.YELLOW);
        return platform;
    }
}