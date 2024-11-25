package Main_Logic;

import Socket_Main.HandleClient;
import Socket_Main.SocketHandler;

import java.io.*;
import java.net.*;


/************
 * This class acts as my main server. This will also create a socket to listen from Client
 * Server will then process incoming requests from GUI
 */
public class CoffeeController extends Thread {
    private SocketHandler socketHandler;
    public UserInput userInput;
    public ValveController valveController = new ValveController();

    public WeightCheck weightCheck = new WeightCheck();

    public TemperatureInfo temperatureInfo = new TemperatureInfo();

    public AlertController alertController;

    public WaterInfo waterInfo = new WaterInfo();

    public BrewController brewController;

    public HandleClient clientHandle = new HandleClient();

    private int cupSize;
    private int brewStrength;
    private boolean isBrewPressed;

    public CoffeeController(int cupSize, int brewStrength, boolean isBrewPressed, SocketHandler socketHandler, int waterLevel) throws IOException {
        this.cupSize = cupSize;
        this.socketHandler = socketHandler;
        this.brewStrength = brewStrength;
        this.isBrewPressed = isBrewPressed;
        alertController = new AlertController(socketHandler);
        userInput = new UserInput(cupSize, brewStrength, isBrewPressed);
        brewController = new BrewController(valveController, weightCheck, temperatureInfo,
                alertController, waterInfo, userInput, waterLevel);
    }

    @Override
    public void run() {
        System.out.println("Cup Size : " + cupSize + "Brew Strength : " + brewStrength);

        if (isBrewPressed) {
            alertController.displayMessage("brewPressed");
            //start brewing
            brewController.startBrewing(cupSize, brewStrength);
        }
    }

    /********
     * This method is the entry point of CoffeeMachineController server.
     * It creates a ServerSocket that listens for client connections.
     * Once a client connects, it will handle the client's request.
     * @param args
     */

    public static void main(String[] args) {
        // Initialize the socket connection

        //Thread controllerThread = new Thread(coffeeController);
        //controllerThread.start();

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("CoffeeMachineController is running....");
            while (true) {
                // Accept client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // Handle client commands in a separate thread
                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                        String command;
                        // Keep handling client commands in a loop
                        while ((command = in.readLine()) != null) {
                            System.out.println("Received command: " + command);
                            String response = "";

                            switch (command) {

                                case "WaterLevelLow": {
                                    response = "Low ON Water !!!";
                                    break;
                                }

                                case "coffeCupSizeNotSelected" : {
                                    response = "Cup/Strength ????";
                                    break;
                                }

                                case "WaterLevelOK": {
                                    response = "Water Level is OK";
                                    out.println(response);
                                    out.flush();
                                    Thread.sleep(2000);
                                    response = "Heating Water...";
                                    break;
                                }

                                case "brewPressed": {
                                    response = "Checking Water Level...";
                                    Thread.sleep(1000);  // Simulate delay for brewing steps
                                    break;
                                }

                                case "pouringCoffee" : {
                                    response = "BREWING....";
                                    break;
                                }

                                default: {
                                    response = "Brewing complete";
                                }
                            }
                            // Send the response to the client
                            out.println(response);
                            out.flush();
                        }
                        System.out.println("Client disconnected");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();  // Start a new thread to handle each client connection
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
