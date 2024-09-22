import java.util.LinkedList;
import java.util.Queue;

public class AlertController {
    /**
     * Things that AlertController needs to get
     * TempInfo --> Temperature of water --> Temperature of Plate
     * When brew is ready
     * Water level info
     *
     */

    Queue<String> messages = new LinkedList<>();

    public void displayMessage(String message){
        messages.add(message);
        System.out.println(messages.remove());
    }

}
