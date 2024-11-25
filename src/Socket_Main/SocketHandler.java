package Socket_Main;

import java.io.*;
import java.net.*;

public class SocketHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    //Constructor to establish the socket connection
    public SocketHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port); //Connect to the server
        this.out = new PrintWriter(socket.getOutputStream(), true); //To send the data
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    //Method to send a command to the server
    public void sendCommand(String command) {
        out.println(command);
    }

    //Method to receive the server's response
    public String receiveResponse() throws IOException {
        return in.readLine();
    }

    //Method to close the socket connection
    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }
}
