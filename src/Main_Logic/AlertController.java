package Main_Logic;

import Socket_Main.SocketHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class AlertController {

    SocketHandler socketHandler;


//    public AlertController(SocketHandler socketHandler) {
//        this.socketHandler = socketHandler;
//    }
    /**
     * Things that AlertController needs to get
     * TempInfo --> Temperature of water --> Temperature of Plate
     * When brew is ready
     * Water level info
     *
     */

    Queue<String> messages = new LinkedList<>();

    public AlertController(SocketHandler socketHandler) throws IOException {
        this.socketHandler = socketHandler;
    }

    public void displayMessage(String message){
        messages.add(message);
//        System.out.println(messages.remove());
        socketHandler.sendCommand(message);
        System.out.println("Check " + message);
    }
}
