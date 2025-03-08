package fr.kafka.exo3.producteur;

public class TemperatureThread {

    public static void main(String[] args) {
        final int nbThreads = 20;

        // Création de plusieurs threads pour simuler plusieurs producteurs de température
        for (int i = 0; i < nbThreads; i++) {
            Thread thread = new Thread(() -> {
                final String nomBatiment = "Batiment " + Thread.currentThread().getId();
                System.out.println("Démarrage du producteur pour le batiment " + nomBatiment);
                TemperatureProducteur producteur = new TemperatureProducteur(nomBatiment);
                producteur.sendTemperature();
            });

            // Délai aléatoire avant de démarrer le thread (simuler un flux de données non constant)
            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            thread.start();
        }
    }
}
