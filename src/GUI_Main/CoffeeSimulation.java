package GUI_Main;

import Main_Logic.Sound;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle; // Import Rectangle for heating effect
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox; // Import for VBox layout
import javafx.geometry.Pos; // Import for positioning
import javafx.scene.control.Label; // Import for labels

import java.util.Optional;

public class CoffeeSimulation {
    private AnchorPane anchorPane;
    private double cupSize; // 1.0 for small, 2.0 for large, etc.
    private Timeline timeline;
    private double pileHeight = 0; // Height of the coffee pile
    private int horizontalDropCount = 0; // Count of horizontal drops
    private double imageCenterX; // X position of the center of the image
    private double dropStartY; // Y position where drops start falling
    private Button brewButton; // Reference to the brew button
    private Button cupSizeDial;
    private Button strengthSizeDial;
    private Button onOff;
    private Button sound;
    private Label brewingLabel; // Reference to the brewing label
    public Rectangle heatingEffect; // Heating effect rectangle
    private FadeTransition fadeTransition;
    private FadeTransition soundBlinkTransition;
    private EventHandler<ActionEvent> brewButtonHandler;

    public CoffeeSimulation(AnchorPane anchorPane, double cupSize, Button brewButton,
                            Button cupSizeDial, Button strengthSizeDial, Button onOff, Button sound, Label brewingLabel, EventHandler<ActionEvent> brewButtonHandler ) {
        this.anchorPane = anchorPane;
        this.cupSize = cupSize;
        this.cupSizeDial = cupSizeDial;
        this.strengthSizeDial = strengthSizeDial;
        this.brewButton = brewButton; // Initialize brew button
        this.onOff = onOff;
        this.brewingLabel = brewingLabel; // Initialize brewing label
        this.sound = sound;
        this.brewButtonHandler = brewButtonHandler;

        // Calculate the center of the image and where drops start
        this.imageCenterX = anchorPane.getWidth() / 2; // Centered horizontally
        this.dropStartY = (anchorPane.getHeight() / 2); // Starting slightly below the image

        // Create heating effect rectangle
        heatingEffect = new Rectangle(200, 15); // Width and height of the heating effect
        heatingEffect.setFill(Color.RED); // Initial color
        heatingEffect.setOpacity(0); // Start invisible
        heatingEffect.setLayoutX(imageCenterX-150); // Center it under the cup
        heatingEffect.setLayoutY(anchorPane.getHeight()-220); // Position it at the bottom of the cup
        anchorPane.getChildren().add(heatingEffect); // Add to the pane

        initializeSimulation();
    }

    private void initializeSimulation() {
        int durationInSeconds;
        // Duration of the simulation based on cup size
        if(cupSize==1.0){
            durationInSeconds = 10;
        }
        else if(cupSize==1.5){
            durationInSeconds = 20;
        }
        else{
            durationInSeconds = 25;
        }

        // Timeline to create repetitive drops
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            createCoffeeDrops();
        }));

        timeline.setCycleCount(durationInSeconds); // Drop every second
        timeline.setOnFinished(event -> onSimulationComplete()); // Call when animation finishes
    }

    private void createCoffeeDrops() {
        for (int i = 0; i < 10; i++) { // Change to 10 drops
            // Create a coffee drop
            Circle coffeeDrop = new Circle(5, Color.WHITE);
            coffeeDrop.setOpacity(0.7); // Fully visible

            // Position the drop at the center of the image (top of the carafe)
            coffeeDrop.setLayoutX(imageCenterX - 50);
            coffeeDrop.setLayoutY(dropStartY);

            // Add the drop to the pane
            anchorPane.getChildren().add(coffeeDrop);

            // Calculate the final position of the drop in the pile (over the cup)
            double endY = anchorPane.getHeight() - 50 - pileHeight; // Adjust to be above the cup
            double endX = imageCenterX - 40 + (horizontalDropCount + i) * 10; // Spread horizontally

            // Create a path transition to animate the drop falling
            Line path = new Line(coffeeDrop.getLayoutX(), coffeeDrop.getLayoutY(), endX, endY);
            PathTransition transition = new PathTransition();
            transition.setNode(coffeeDrop);
            transition.setPath(path);
            transition.setDuration(Duration.seconds(1.5)); // Duration of the drop

            transition.setOnFinished(event -> {
                // Ensure the drop stays in place after reaching the pile
                coffeeDrop.setTranslateX(endX - coffeeDrop.getLayoutX() - 100);
                coffeeDrop.setTranslateY(endY - coffeeDrop.getLayoutY() - 220);
            });

            transition.play();
        }

        // Adjust the pile height and horizontal drop count after each set of drops
        horizontalDropCount += 8; // Increment by 10 drops
        if (horizontalDropCount >= 15) {
            horizontalDropCount = 0;
            pileHeight += 4; // Increment pile height
        }
    }

    private void onSimulationComplete() {
        brewingLabel.setText("Brewing Complete"); // Update label text
        brewButton.setStyle("-fx-background-radius: 50; -fx-background-color:grey;"); // Change button color back to green

        // Show heating effect after brewing
        showHeatingEffect();
        startSoundButtonBlink(); // Start blinking the sound button
        Sound sound = new Sound();
        sound.playSound("/Users/aashutoshdahal/Desktop/CS460/CoffeMachineSimulator/sources/second-hand-149907.wav");
        askUserForCoffee();
    }

    private void askUserForCoffee() {
        // Create a new stage (window) for user input
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Coffee Ready");
        dialogStage.initModality(Modality.APPLICATION_MODAL); // Block events from other windows

        // Create buttons for Yes and No
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        // Create a button bar to hold the buttons
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(yesButton, noButton);

        // Create a layout for the dialog
        VBox layout = new VBox(10); // Vertical box with spacing
        layout.getChildren().addAll(new Label("Do you want to grab your coffee?"), buttonBar);
        layout.setAlignment(Pos.CENTER); // Center align the content

        // Set up the scene
        Scene scene = new Scene(layout, 300, 150);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false); // Make it non-resizable
        dialogStage.show(); // Show the stage

        // Button actions
        yesButton.setOnAction(event -> {
            resetCoffeeMachine(); // Reset if the user wants coffee
            dialogStage.close(); // Close the dialog
        });

        noButton.setOnAction(event -> {
            startAutoShutOffTimer(); // Start auto shut-off timer
            dialogStage.close(); // Close the dialog
        });
    }

    private void resetCoffeeMachine() {
        anchorPane.getChildren().removeIf(node -> node instanceof Circle); // Remove all Circle nodes (coffee drops)
        brewingLabel.setText("Team 05"); // Reset label to initial state
        brewButton.setStyle("-fx-background-radius: 50; -fx-background-color: green;"); // Reset button color
        heatingEffect.setOpacity(0); // Hide heating effect
        stopSoundButtonBlink(); // Stop the sound button blinking
        pileHeight = 0; // Reset pile height
        horizontalDropCount = 0; // Reset horizontal drop count
        brewButton.setOnAction(brewButtonHandler);

        // Stop any ongoing animations
        if (timeline != null) {
            timeline.stop();
        }
        if (fadeTransition != null) {
            fadeTransition.stop();
        }
        if (soundBlinkTransition != null) {
            soundBlinkTransition.stop();
        }
        // Reset any other necessary states or UI components
    }

    private void startAutoShutOffTimer() {
        // Start a timer for 2 hours (7200 seconds)
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> shutOffMachine());
        pause.play();
    }

    private void shutOffMachine() {
        System.out.println("shutting down");
        brewButton.setStyle("-fx-background-radius: 50; -fx-background-color:grey;"); // Change button color to grey
        cupSizeDial.setStyle("-fx-background-radius: 50; -fx-background-color:grey;");
        strengthSizeDial.setStyle("-fx-background-radius: 50; -fx-background-color:grey;");
        onOff.setStyle("-fx-background-radius: 50; -fx-background-color:grey;");
        cupSizeDial.setOnAction(null);
        strengthSizeDial.setOnAction(null);
        brewingLabel.setText("");
        fadeTransition.stop();
        heatingEffect.setOpacity(0);
        stopSoundButtonBlink(); // Stop the sound button blinking
    }

    private void showHeatingEffect() {
        heatingEffect.setOpacity(0.7); // Make the heating effect visible

        // Animate the heating effect
        fadeTransition = new FadeTransition(Duration.seconds(10), heatingEffect);
        fadeTransition.setFromValue(0.5); // Start from fully opaque
        fadeTransition.setToValue(0.1); // Fade to semi-transparent
        fadeTransition.setCycleCount(Animation.INDEFINITE); // Repeat indefinitely
        fadeTransition.setAutoReverse(true); // Reverse the fade effect
        fadeTransition.play(); // Start the animation
    }

    private void startSoundButtonBlink() {
        // Create a new FadeTransition for the sound button
        FadeTransition soundButtonBlink = new FadeTransition(Duration.seconds(0.5), sound);
        soundButtonBlink.setFromValue(1.0);
        soundButtonBlink.setToValue(0.1);
        soundButtonBlink.setCycleCount(Animation.INDEFINITE);
        soundButtonBlink.setAutoReverse(true);

        // Add a listener to change the color while blinking
        soundButtonBlink.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.lessThan(soundButtonBlink.getCycleDuration().divide(2))) {
                sound.setStyle("-fx-background-color: green;"); // Set to green
            } else {
                sound.setStyle("-fx-background-color: transparent;"); // Reset to default (or original color)
            }
        });

        soundButtonBlink.play();
    }

    public void stopSoundButtonBlink() {
        if (soundBlinkTransition != null) {
            soundBlinkTransition.stop();
            sound.setOpacity(1.0); // Reset to fully opaque
        }
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}
