# Kafka Java Project - Gestion et Analyse des Données Massives

Lien du
rapport : [Google Docs](https://docs.google.com/document/d/1lQLYvurFBKFSZf75EUDY6nXk9LKN0_AtvOI5bWYghkI/edit?usp=sharing)

## Introduction

Ce projet s'inscrit dans le cadre du **Master 2 Bases de Données et Intelligence Artificielle**, dans le module de *
*Gestion et analyse des Données Massives**. Il vise à expérimenter l'utilisation d'**Apache Kafka** pour la gestion de
flux de données en temps réel et à comparer ses fonctionnalités avec **JMS**.

## 📌 Objectifs du projet

1. **Exercice 2 : Clients Kafka en Java**
    - Implémenter un **producteur Kafka** en Java.
    - Mettre en place un **consommateur Kafka** et tester la réception des messages.
    - Expérimenter la **répartition des messages** sur plusieurs partitions et groupes de consommateurs.
    - Comparer les mécanismes de consommation de Kafka avec ceux de **JMS**.

2. **Exercice 3 : Stream Processing avec Kafka**
    - Construire une **architecture Kafka Streams** pour traiter des flux de données issues de capteurs de température.
    - Développer un **producteur multi-source** qui envoie périodiquement des relevés de température.
    - Mettre en place un **traitement en temps réel** pour calculer des moyennes et générer des alertes en cas de
      dépassement de seuils.

---

## 🏗️ Architecture du projet

Le projet repose sur plusieurs composants :

- **Zookeeper** : Coordonne le cluster Kafka et gère les métadonnées.
- **Kafka Brokers** : Stockent et distribuent les messages aux consommateurs.
- **Producteurs Java** : Envoient des messages à Kafka.
- **Consommateurs Java** : Récupèrent et traitent les messages depuis Kafka.
- **Kafka Streams** : Traite les flux en temps réel et génère des analyses.

## 🛠️ Installation et Exécution

### 📌 Prérequis

- **Java 17+**
- **Docker** et **Docker Compose**

### 🔹 Installation

1. Cloner le dépôt GitHub :

```bash
git clone https://github.com/Maxime-Cllt/Kafka.git
cd Kafka
```

2. Lancer les services Kafka et Zookeeper :

```bash
docker-compose up
```

3. Compiler et exécuter les classes Java :

```bash
gradlew clean build
gradlew <nom de la classe>
```

## 📊 Résultats attendus

- Vérification de la bonne transmission des messages entre producteurs et consommateurs.
- Mesure des performances en fonction du nombre de partitions et de consommateurs.
- Analyse du traitement en temps réel via Kafka Streams.
