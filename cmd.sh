#!/bin/bash

# Chemin vers les fichiers JAR
jar1="backend/target/backend-1.0-SNAPSHOT.jar"
jar2="microservice/target/microservice-1.0-SNAPSHOT.jar"

# Chemin vers le frontend
dossier_npm="frontend"

# Lancement des deux JARs
java -jar "$jar1" &
java -jar "$jar2" &

# Changement de répertoire vers le frontend
cd "$dossier_npm" || exit

# Lancement de npm run serve
npm run serve