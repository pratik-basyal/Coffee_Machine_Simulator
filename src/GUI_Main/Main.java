package GUI_Main;

import Main_Logic.*;

import Socket_Main.SocketHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;
import java.io.*;
import java.net.*;

public class Main extends Application {
    private boolean isOn = false; // Coffee machine state
    private int cupSizeIndex = -1; // Index for cup size dial
    private int coffeeStrengthIndex = -1; // Index for coffee strength dial

    private int defaultWaterLevel = 500;

    private int previousWaterLevel=0;
    private final String[] cupSizes = {"Small", "Medium", "Large"};
    private final String[] coffeeStrengths = {"Weak", "Medium", "Strong"};
    private CoffeeSimulation coffeeSimulation; // Add this line at the beginning of Main class
    // Declare event handlers as class members
    private EventHandler<ActionEvent> brewButtonHandler;
    private EventHandler<ActionEvent> cupSizeDialHandler;
    private EventHandler<ActionEvent> coffeeStrengthDialHandler;

    private Label brewingLabel; // to display on GUI

    private SocketHandler socketHandler;

    private CupSizeDial cup = new CupSizeDial();

    private BrewStrengthDial brewStrength = new BrewStrengthDial();

//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;


    @Override
    public void start(Stage primaryStage) {
        //initializing socket connection to the CoffeeController server
        //initializeSocketConnection();
        try {
            socketHandler = new SocketHandler("localhost", 12345); //Connect to CoffeeMachineController server
        }

        catch (Exception e) {
            System.out.println("Error connecting to CoffeeMachineController : " + e.getMessage());
        }

        // Load the image
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream
                ("/img.png")));
//        double imageWidth = image.getWidth();
//        double imageHeight = image.getHeight();

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);

        // Bind the ImageView's width and height to the Scene's width and height
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());
        imageView.setPreserveRatio(true);

        // Create a circular button
        Button circularButton = new Button("Brew");
        circularButton.setShape(new Circle(10));
        circularButton.setMinSize(40, 45);
        circularButton.setMaxSize(50, 60);
        circularButton.setStyle("-fx-background-radius: 50; -fx-background-color:grey;");

        // Create On/Off button
        Button onOffButton = new Button("");
        onOffButton.setShape(new Circle(10));
        onOffButton.setMinSize(40, 40);
        onOffButton.setMaxSize(45, 45);
        onOffButton.setStyle("-fx-background-radius: 20; -fx-background-color:grey;");

        // Create a label to display brewing status
        brewingLabel = new Label("");
        brewingLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        brewingLabel.setLayoutX(250);
        brewingLabel.setLayoutY(170);

        // Create dial buttons for cup size and coffee strength
        Button cupSizeDial = new Button("Cup Size");
        cupSizeDial.setShape(new Circle(10));
        cupSizeDial.setMinSize(80, 80);
        cupSizeDial.setMaxSize(80, 80);
        cupSizeDial.setStyle("-fx-background-radius: 40; -fx-background-color:grey;");

        Button speaker = new Button("");
        speaker.setShape(new Circle(10));
        speaker.setMinSize(10, 10);
        speaker.setMaxSize(10, 10);
        speaker.setStyle("-fx-background-radius: 20; -fx-background-color:grey;");

        Button coffeeStrengthDial = new Button("Strength");
        coffeeStrengthDial.setShape(new Circle(10));
        coffeeStrengthDial.setMinSize(80, 80);
        coffeeStrengthDial.setMaxSize(80, 80);
        coffeeStrengthDial.setStyle("-fx-background-radius: 40; -fx-background-color:grey;");

        // Create an AnchorPane to position the buttons and label
        AnchorPane anchorPane = new AnchorPane(imageView, onOffButton, cupSizeDial, coffeeStrengthDial, brewingLabel);

        // Position the brew button
        AnchorPane.setTopAnchor(circularButton, 180.0);
        AnchorPane.setLeftAnchor(circularButton, 495.0);
        anchorPane.getChildren().add(circularButton);

        // Position the on/off button
        AnchorPane.setTopAnchor(onOffButton, 635.0);
        AnchorPane.setLeftAnchor(onOffButton, 203.0);

        // Position the dial buttons
        AnchorPane.setTopAnchor(cupSizeDial, 302.0);
        AnchorPane.setLeftAnchor(cupSizeDial, 505.0);

        AnchorPane.setTopAnchor(coffeeStrengthDial, 485.0);
        AnchorPane.setLeftAnchor(coffeeStrengthDial, 505.0);

        AnchorPane.setTopAnchor(speaker, 179.0);
        AnchorPane.setLeftAnchor(speaker, 562.0);

        anchorPane.getChildren().add(speaker);

        brewButtonHandler = event1 -> {
            brewingLabel.setText("");

                //Send "brew" command to the CoffeeController server when the button is clicked
                BrewPressed brewPressed = new BrewPressed();
                brewPressed.pressButton();
                CoffeeController coffeeController;
                try {
                    coffeeController = new CoffeeController(cup.getSelectedSize(),brewStrength.getSelectedStrength(),
                            brewPressed.isBrewPressed(), socketHandler, defaultWaterLevel- previousWaterLevel);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //socketHandler.sendCommand("brewPressed");
                coffeeController.start();


            Thread responseListenerThread = new Thread(() -> {
                try {
                    String response;
                    while (true) {
                        response = socketHandler.receiveResponse();
                        if (response != null) {
                            // Update the JavaFX UI with the response
                            final String finalResponse = response;
                            Platform.runLater(() -> {
                                brewingLabel.setText(finalResponse);
                                System.out.println("CHECKING.............."+finalResponse);
                                // Start simulation if brewing has started
                                if (finalResponse.equalsIgnoreCase("Brewing....")) {

                                    coffeeSimulation = new CoffeeSimulation(anchorPane, getCupSize(), circularButton,
                                            cupSizeDial, coffeeStrengthDial, onOffButton, speaker, brewingLabel, brewButtonHandler);
                                    coffeeSimulation.start();
                                    circularButton.setOnAction(null);
                                }

                                // Re-enable buttons if brewing is complete
                                if (finalResponse.equalsIgnoreCase("Brewing complete")) {
                                    circularButton.setOnAction(brewButtonHandler);
                                    cupSizeDial.setOnAction(cupSizeDialHandler);
                                    coffeeStrengthDial.setOnAction(coffeeStrengthDialHandler);
                                    coffeeSimulation.stopSoundButtonBlink();
                                    if (cup.getSelectedSize() == 1)
                                        previousWaterLevel += 300;
                                    else if (cup.getSelectedSize()==2)
                                        previousWaterLevel += 420;
                                    else
                                        previousWaterLevel += 480;
                                }

                            });

                            // Break out if the brewing is complete
                            if (response.equalsIgnoreCase("Brewing complete")) {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error receiving response: " + e.getMessage());
                }
            });

            // Start the response listener thread to continuously receive updates from the server
            responseListenerThread.start();
        };

        cupSizeDialHandler = event2 -> {
            cupSizeIndex = (cupSizeIndex + 1) % cupSizes.length;
            cupSizeDial.setText(cupSizes[cupSizeIndex]);
            cupSizeDial.setRotate(cupSizeDial.getRotate() + 120); // Rotate 120 degrees
            //System.out.println("Cup Size: " + cupSizes[cupSizeIndex]);
            //cup = new CupSizeDial();
            cup.rotateDial(cupSizeIndex + 1);
        };

        coffeeStrengthDialHandler = event3 -> {
            coffeeStrengthIndex = (coffeeStrengthIndex + 1) % coffeeStrengths.length;
            coffeeStrengthDial.setText(coffeeStrengths[coffeeStrengthIndex]);
            coffeeStrengthDial.setRotate(coffeeStrengthDial.getRotate() + 120); // Rotate 120 degrees
            //System.out.println("Coffee Strength: " + coffeeStrengths[coffeeStrengthIndex]);
            //brewStrength = new BrewStrengthDial();
            brewStrength.rotateDial(coffeeStrengthIndex + 1);
        };
        // Create the scene with the layout pane
        Scene scene = new Scene(anchorPane, 800, 800);

        // Set up the stage
        primaryStage.setTitle("KAPS Coffee Maker Simulation TEAM 05");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add event handler for the on/off button
        onOffButton.setOnAction(event -> {
            isOn = !isOn;
            if (isOn) {
                brewingLabel.setText("TEAM 05");
                onOffButton.setStyle("-fx-background-radius: 50; -fx-background-color:#e8825b;");
                circularButton.setStyle("-fx-background-radius: 50; -fx-background-color:green;");
                cupSizeDial.setStyle("-fx-background-radius: 40; -fx-background-color:green;");
                coffeeStrengthDial.setStyle("-fx-background-radius: 40; -fx-background-color:green;");
                // Add event handler for the brew button
                circularButton.setOnAction(brewButtonHandler);
                // Add event handler for the cup size dial
                cupSizeDial.setOnAction(cupSizeDialHandler);

                // Add event handler for the coffee strength dial
                coffeeStrengthDial.setOnAction(coffeeStrengthDialHandler);
            }
            else {
                onOffButton.setStyle("-fx-background-radius: 50; -fx-background-color:grey;");
                circularButton.setStyle("-fx-background-radius: 50; -fx-background-color:grey");
                cupSizeDial.setStyle("-fx-background-radius: 40; -fx-background-color:grey;");
                coffeeStrengthDial.setStyle("-fx-background-radius: 40; -fx-background-color:grey;");
                circularButton.setOnAction(null);
                cupSizeDial.setOnAction(null);
                coffeeStrengthDial.setOnAction(null);
                brewingLabel.setText("");
                coffeeSimulation.stop();
                coffeeSimulation.heatingEffect.setOpacity(0);
            }
        });

    }

    private double getCupSize() {
        switch (cupSizes[cupSizeIndex]) {
            case "Medium":
                return 1.5;
            case "Large":
                return 2.0;
            case "Small":
            default:
                return 1.0;
        }
    }


//    /*******
//     * Initializes the socket connection to the CoffeeMachineCOntroller server
//     */
//    private void initializeSocketConnection() {
//        try {
//            socket = new Socket("localhost", 12345); //Connect to CoffeeMachineController server
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        }
//
//        catch (Exception e) {
//            System.out.println("Error connecting to CoffeeMachineController : " + e.getMessage());
//        }
//    }

//    /*********
//     * Sends command to the CoffeMachineController server and process the response
//     * @param command - The command to send (e.g., "bew", "set_cup_size", etc)
//     */
//    private void sendCommandToServer (String command) {
//        try {
//            //send command to server
//            out.println(command);
//
//            //get server's response and update the GUI
//            String response = in.readLine();
//            System.out.println("Response from server : " + response);
//
//            //Updating GUI based on the response form the server
//            brewingLabel.setText(response);
//        }
//        catch (Exception e) {
//            System.out.println("Error communicating with CoffeeMachineController : " + e.getMessage());
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}