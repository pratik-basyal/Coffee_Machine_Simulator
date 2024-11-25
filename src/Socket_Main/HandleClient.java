package Socket_Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleClient {
    /********
     * This method handles communication with client.
     * It reads the command sent by the client (GUI), proccess it and sends back a response
     * @param socket - the socekt through which communication with client occurs
     */

    public void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            //Read command from client (GUI)
            String command = in.readLine();
            System.out.println("Received command : " + command);

            //Process the command and get a response
            String response = processCommand(command);

            //Send the response back to the client (GUI)
            out.println(response);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*******
     * This method processes the command receivedd from the client.
     * Based on teh command, it returns an appropriate response
     * @param command - command sent by the client
     * @return String - the server's response based on the command
     */
    public String processCommand (String command) {
        //Logic for processing the coffee machine commands

        return switch (command) {
            case "Machine is ON" -> "Machine is ON for TEAM 05";
            case "brewPressed" -> "Checking Water Level...";
            case "set_cup_size" -> "Cup Size set";
            case "WaterLevelLow"-> "Low ON Water !!!";
            case "WaterLevelOK" -> "Water Level is OK";
            default -> "Unknown command";
        };
    }
}
