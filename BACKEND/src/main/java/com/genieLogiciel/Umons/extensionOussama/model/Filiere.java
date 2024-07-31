package com.genieLogiciel.Umons.extensionOussama.model;

import jakarta.persistence.Entity;
import lombok.Getter;


@Getter
public enum Filiere {
        INFORMATIQUE("Informatique"),
        MATHEMATIQUES("Mathématiques"),
        PHYSIQUE("Physique"),
        CHIMIE("Chimie"),
        BIOLOGIE("Biologie"),
        ECONOMIE("Économie"),
        GESTION("Gestion"),
        LANGUES("Langues"),
        LITTERATURE("Littérature"),
        HISTOIRE("Histoire"),
        GEOGRAPHIE("Géographie"),
        PHILOSOPHIE("Philosophie"),
        ART("Art"),
        MUSIQUE("Musique"),
        SCIENCES_POLITIQUES("Sciences Politiques"),
        DROIT("Droit"),
        MEDICINE("Médecine"),
        ARCHITECTURE("Architecture"),
        INGÉNIERIE("Ingénierie"),
        SCIENCES_ENVIRONNEMENT("Sciences de l'Environnement");

        private final String nom;

        Filiere(String nom) {
            this.nom = nom;
        }


}
