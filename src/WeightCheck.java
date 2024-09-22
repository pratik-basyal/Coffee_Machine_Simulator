//
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class WeightCheck {
//    private int weight = 60;
//    private boolean brewing = false;
//    private final Lock lock = new ReentrantLock();
//    private Thread brewingThread;
//
//    // Setter for the brewing state
//    public void setBrewing(boolean brewing) {
//        if (brewing && !this.brewing) {
//            this.brewing = true;
//            startBrewingThread();
//        } else if (!brewing && this.brewing) {
//            this.brewing = false;
//            stopBrewingThread();
//        }
//    }
//
//    // Start the brewing process and the thread
//    private void startBrewingThread() {
//        brewingThread = new Thread(() -> {
//            while (brewing) {
//                try {
//                    Thread.sleep(1000);  // Sleep for 1 second
//                    incrementWeight();
//                    System.out.println("Current weight: " + getWeight() + "g");
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        brewingThread.start();
//    }
//
//    // Stop the brewing process
//    private void stopBrewingThread() {
//        // Thread will stop when brewing is set to false
//        if (brewingThread != null) {
//            brewingThread.interrupt();  // Interrupt the thread if necessary
//        }
//    }
//
//    // Increment the weight by 10g
//    private void incrementWeight() {
//        lock.lock();
//        try {
//            weight += 10;
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    // Get the current weight
//    public int getWeight() {
//        lock.lock();
//        try {
//            return weight;
//        } finally {
//            lock.unlock();
//        }
//    }
//}

public class WeightCheck{

    protected float emptyPotWeight = 60;


    public float getTotalWeight(int cupSizeWeight){
        if (cupSizeWeight == 1)
        {
            return 240 + emptyPotWeight;
        }
        else if (cupSizeWeight== 2){
            return 360 + emptyPotWeight;
        }
        else{
            return 420 + emptyPotWeight;
        }

    }

}