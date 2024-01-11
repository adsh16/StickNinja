package com.example.StickHeroApplication;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class gameplayController {
    private Character hero = new Character(0);

    private Cherry cherry = new Cherry();
    public Label gameOverLabel;
    @FXML
    private Pane gameScreen;

    @FXML
    private Label score;
    @FXML
    private ImageView stickHero;

    private boolean isGrowing = false;
    private boolean istranslating = false;
    private Line stick;
    private double angle = 270.0;
    private double stickLength = 0.0;
    private final double fixedCharDist = 55.0;
    private final double minDist = 30.0;
    private boolean isCharacterFacingRight = true;
    private final double maxDist = 150.0;
    private ArrayList<Rectangle> platforms = new ArrayList<>();
    private Rectangle prevPlatform;
    private Rectangle currPlatform;
    private com.example.StickHeroApplication.soundManager soundManager = new soundManager();

    private AnimationTimer stickExtension;

    private Timeline timeline;

    private platform platform = new platform();

    Random random = new Random();

    @FXML
    public void initialize() {
        soundManager.playgameplayBGM();
        Rectangle initPlatform = platform.initializePlatform();
        gameScreen.getChildren().add(initPlatform);
        prevPlatform = initPlatform;
        currPlatform = initPlatform;
        platforms.add(initPlatform);
        startGeneratingPlatforms();
        gameScreen.setOnMousePressed(event -> {
            if (!istranslating && !isGrowing) {
                isGrowing = true;
                handleMousePressed();
            }
        });
        gameScreen.setOnMouseReleased(event -> {
            if (isGrowing) {
                isGrowing = false;
                handleMouseReleased();
            }
        });
    }

    private void startGeneratingPlatforms() {
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(0.1),
                event -> generatePlatform()
        );

        timeline = new javafx.animation.Timeline(keyFrame);
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void stopGeneratingPlatforms() {
        //stop the platform generator
        timeline.stop();
    }

    private void generatePlatform() {
        Rectangle platform = this.platform.generatePlatform();
        double minLayoutX = prevPlatform.getLayoutX() + prevPlatform.getWidth() + minDist;
        double maxLayoutX = prevPlatform.getLayoutX() + prevPlatform.getWidth() + maxDist;
        double layoutX = random.nextDouble() * (maxLayoutX - minLayoutX) + minLayoutX;
        platform.setLayoutX(layoutX);
        gameScreen.getChildren().add(platform);
        platforms.add(platform);
        prevPlatform = platform;
    }
    public void handleMousePressed() {
        double pivotX = currPlatform.getLayoutX() + currPlatform.getWidth();
        double pivotY = currPlatform.getLayoutY();
        stick = new Line(pivotX, pivotY, pivotX, pivotY);
        stick.setStroke(Color.WHITE);
        stick.setStrokeWidth(4);
        stick.setStroke(Color.WHITE);
        gameScreen.getChildren().add(stick);
        soundManager.playStickGrowSound();

        final long[] startTime = { System.nanoTime() };

        stickExtension = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double elapsedTime = (now - startTime[0]) / 1e5;
                if (elapsedTime > 0) {
                    stickLength += 2;
                    stick.setEndY(stick.getStartY() - stickLength);
                    startTime[0] = System.nanoTime();
                }
            }
        };
        // Toggle character orientation when the stick is growing
        isCharacterFacingRight = !isCharacterFacingRight;
        stickExtension.start();
    }

    public void handleMouseReleased() {
        //stop the animation timer
        stickExtension.stop();
        rotateStick();
    }

    @FXML
    private void rotateStick() {
        soundManager.stopPlaySound();

        double targetAngle = 90; // Adjust the final angle of rotation
        double pivotX = stick.getStartX();
        double pivotY = stick.getStartY();
        double duration = 1000; // Adjust the duration of the rotation

        Rotate rotate = new Rotate(0, pivotX, pivotY);
        stick.getTransforms().add(rotate);

        Timeline rotationTimeline = new Timeline(
                new KeyFrame(Duration.millis(duration), new KeyValue(rotate.angleProperty(), targetAngle))
        );

        // Add an onFinished event handler
        rotationTimeline.setOnFinished(e ->checkIfStickIsLongEnough());
        rotationTimeline.play();
    }


    private void checkIfStickIsLongEnough() {
        //check if stick is long enough to reach the next platform
        //if it is, move the character to the next platform
        //if it is not, end the game
        double currPlatformEndX = currPlatform.getLayoutX() + currPlatform.getWidth();
        double nextPlatformStartX = platforms.get(platforms.indexOf(currPlatform) + 1).getLayoutX();
        double nextPlatformEndX = nextPlatformStartX + platforms.get(platforms.indexOf(currPlatform) + 1).getWidth();
        if (stickLength >= nextPlatformStartX - currPlatformEndX && stickLength <= nextPlatformEndX - currPlatformEndX) {
            moveCharacterToNextPlatform();
            soundManager.playStickFallSound();
        } else {
            endGame();
        }
    }
    private void moveCharacterToNextPlatform() {
        istranslating = true;
        Rectangle previousPlatform = currPlatform;
        int nextPlatformIndex = platforms.indexOf(currPlatform) + 1;
        currPlatform = platforms.get(nextPlatformIndex);
        //Animate the character to move to the next platform
        double characterX = stickHero.getLayoutX();
        double characterEndX = currPlatform.getLayoutX() + currPlatform.getWidth() - fixedCharDist;
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(700), stickHero);
        translateTransition.setToX(characterEndX - characterX);
        translateTransition.play();
        translateTransition.setOnFinished(event -> {
            istranslating = false;
            if (score.getText().equals("00")){
                score.setText("01");
            } //check if score is less than or equal to 09
            else if (Integer.parseInt(score.getText()) <= 9) {
                int currScore = Integer.parseInt(score.getText());
                currScore = currScore + 1;
                score.setText(String.format("%02d", currScore)); // 2 digits
            } else{
                int currScore = Integer.parseInt(score.getText());
                currScore++;
                score.setText(currScore + "");
            }
            gameScreen.getChildren().remove(stick);
            stickLength = 0.0;
            angle = 270.0;
            double shiftAmount = currPlatform.getLayoutX() - previousPlatform.getLayoutX();
            stickHero.setLayoutX(stickHero.getLayoutX() - shiftAmount);
            for (Rectangle platform : platforms) {
                platform.setLayoutX(platform.getLayoutX() - shiftAmount);
            }
        });
    }

    private void endGame() {
        stopGeneratingPlatforms();
        //move player till end of stick and then fall
        double characterX = stickHero.getLayoutX();
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), stickHero);
        tt.setToX(stick.getEndX() - characterX - 30);
        tt.play();
        tt.setOnFinished(event -> {
            gameScreen.getChildren().remove(stick);
            stickLength = 0.0;
            angle = 270;
            playerFalls();
            displayGameOverMessage();
        });
    }
    double double_zero = 0.0;
    double getDouble_one= 1.0;
    private void displayGameOverMessage() {
        soundManager.playGameOverSound();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), gameOverLabel);
        fadeTransition.setFromValue(double_zero);
        fadeTransition.setToValue(getDouble_one);
        fadeTransition.play();

        PauseTransition pause = new PauseTransition(Duration.seconds(2)); // Adjust the duration as needed
        pause.setOnFinished(event -> switchToMainScreen());
        pause.play();
    }


    private void switchToMainScreen() {
        try {
            // Load the main screen from FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("homescreen.fxml")));

            // Get the current stage
            Stage stage = (Stage) gameScreen.getScene().getWindow();

            // Set the main screen scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playerFalls() {
        double characterY = stickHero.getLayoutY();

        // Create a translation transition for falling
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), stickHero);
        translateTransition.setToY(characterY + 250);

        // Create a rotation transition for rotating while falling
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(500), stickHero);
        rotateTransition.setByAngle(360); // You can adjust the rotation angle as needed

        // Play both translation and rotation transitions in parallel
        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, rotateTransition);
        parallelTransition.play();

        parallelTransition.setOnFinished(event -> {
            soundManager.playPlayerFallSound();
        });
    }
}
