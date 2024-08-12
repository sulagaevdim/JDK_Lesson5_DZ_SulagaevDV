import org.w3c.dom.Node;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosopher extends Thread {
    private String namePhilosoph;
    private boolean isEat;
    private boolean isThink;

    private volatile LunchTable table;

    private CountDownLatch countEat;

    public Philosopher(String namePhilosoph, LunchTable table) {
        this.namePhilosoph = namePhilosoph;
        this.countEat = new CountDownLatch(3);
        this.isThink = false;
        this.isEat = false;
        this.table = table;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            while (countEat.getCount() > 0) {
                cicleEating();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEat() {
        return isEat;
    }

    public String getNamePhilosoph() {
        return namePhilosoph;
    }
    private void philosophIsEat(){
        isEat = true;
        try {
            System.out.println(this.namePhilosoph + " кушает спагетти");
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(this.namePhilosoph + " покушал");
        }
        table.setEndLaunch();
        isEat = false;
        isThink = true;
    }
    private void philosophIsThink(){
        if (isThink = true) {
            System.out.println(this.namePhilosoph + " размышляет....");
            try {
                Thread.sleep(new Random().nextInt(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        isThink = false;
    }

    private void cicleEating(){
        philosophIsThink();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        philosophIsEat();
        countEat.countDown();
        philosophIsThink();
    }
    public void go(){
        synchronized (this){
            notify();
        }
    }
}
