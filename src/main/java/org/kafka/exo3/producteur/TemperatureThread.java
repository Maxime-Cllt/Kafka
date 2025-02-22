package org.kafka.exo3.producteur;

public class TemperatureThread {

    public static void main(String[] args) {
        final String nomBatiment = "Batiment " + Thread.currentThread().getId();
        System.out.println("Démarrage du producteur pour le batiment " + nomBatiment);
        TemperatureProducteur producteur = new TemperatureProducteur(nomBatiment);
        producteur.sendTemperature();
    }

}
