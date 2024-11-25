package Main_Logic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WeightCheck {
    private float currentWeight;
    protected final float emptyPotWeight = 60;
    private final Lock lock = new ReentrantLock();
    private boolean brewing = false;
    private Thread weightThread;

    public float getCurrentWeight() {
        lock.lock();
        try {
            return currentWeight;
        } finally {
            lock.unlock();
        }
    }


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


    public void startBrewing(int flowRate, float targetWeight) {
        brewing = true;
        currentWeight = emptyPotWeight;

        weightThread = new Thread(() -> {
            while (brewing && currentWeight < targetWeight) {
                lock.lock();
                try {
                    currentWeight += flowRate;  // Simulate flow rate affecting weight
                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(1000);  // Increment weight every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            stopBrewing();  // Stop brewing when the target weight is reached
        });
        weightThread.start();
    }

    public void stopBrewing() {
        brewing = false;
        if (weightThread != null) {
            weightThread.interrupt();
        }
    }
}
