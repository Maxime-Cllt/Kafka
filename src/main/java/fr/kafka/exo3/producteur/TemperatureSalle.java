package fr.kafka.exo3.producteur;


import java.util.ArrayList;

public class TemperatureSalle extends Thread {

    private ArrayList<Float> temperature = new ArrayList<>();
    private final String name;

    public TemperatureSalle(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    // run method
    public void run() {
        while (true) {
            try {
                temperature.add((float) (Math.random() * 30));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
