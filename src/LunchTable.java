import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class LunchTable extends Thread {
    private LinkedList<Philosopher> philosophers;
    private CountDownLatch startLaunch;
    private CountDownLatch endLaunch;

    public LunchTable(int numberOfPhilosophers) {
        philosophers = new LinkedList<>();
        startLaunch = new CountDownLatch(numberOfPhilosophers);
        endLaunch = new CountDownLatch(numberOfPhilosophers * 3);
        addPhilosophers();
    }
    @Override
    public void run() {
        try {
            startLaunch.await();
            System.out.println("____________________________________");
            System.out.println("Спагетти поданы, приятного аппетита!");
            System.out.println("____________________________________");
            while (endLaunch.getCount() > 0) {
                for (int i = 0; i < philosophers.size(); i++) {
                    if (neighborsAndMeNotEating(i)) {
                        philosophers.get(i).go();
                    }
                }
            }
            endLaunch.await();
            System.out.println("________________________________________________________________");
            System.out.println("Все господа оттрапезничали, теперь можно спокойно и думы думать!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void addPhilosophers() {
        philosophers.add(new Philosopher("Дмитрий Викторович", this));
        philosophers.add(new Philosopher("Александр Геннадьевич", this));
        philosophers.add(new Philosopher("Петр Иванович", this));
        philosophers.add(new Philosopher("Сергей Петрович", this));
        philosophers.add(new Philosopher("Виктор Витальевич", this));
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
            try {
                Thread.sleep(new Random().nextInt(200, 800));
                System.out.println(philosopher.getNamePhilosoph() + " присел за обеденный стол");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            startLaunch.countDown();
        }
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean neighborsAndMeNotEating(int i) {
        if (!philosophers.get(i).isEat()) {
            if (i == 0) {
                if (!philosophers.get(philosophers.size() - 1).isEat()) {
                    if (!philosophers.get(1).isEat()) return true;
                }
            } else if (i == philosophers.size() - 1) {
                if (!philosophers.get(philosophers.size() - 2).isEat()) {
                    if (!philosophers.get(0).isEat()) return true;
                }
            } else if (!philosophers.get(i - 1).isEat()) {
                if (!philosophers.get(i + 1).isEat()) return true;
            }
        }
        return false;
    }

    public CountDownLatch getEndLaunch() {
        return endLaunch;
    }

    public void setEndLaunch() {
        endLaunch.countDown();
    }
}
