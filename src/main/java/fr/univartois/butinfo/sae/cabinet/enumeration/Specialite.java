package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Énumération des spécialités médicales disponibles dans le cabinet.
 *
 * <p>Chaque spécialité possède un libellé lisible destiné à l'affichage.</p>
 */
public enum Specialite {

    /** Médecin généraliste, premier recours pour les patients. */
    GENERALISTE("Médecin généraliste"),

    /** Spécialiste du système cardiovasculaire. */
    CARDIOLOGUE("Cardiologie"),

    /** Spécialiste des maladies de la peau. */
    DERMATOLOGUE("Dermatologie"),

    /** Spécialiste de la santé féminine et de la reproduction. */
    GYNECOLOGUE("Gynécologie"),

    /** Spécialiste du système nerveux. */
    NEUROLOGUE("Neurologie"),

    /** Spécialiste de la vision et des maladies oculaires. */
    OPHTALMOLOGUE("Ophtalmologie"),

    /** Spécialiste de la santé des enfants. */
    PEDIATRE("Pédiatrie"),

    /** Spécialiste de la santé mentale. */
    PSYCHIATRE("Psychiatrie"),

    /** Spécialiste de l'imagerie médicale. */
    RADIOLOGUE("Radiologie"),

    /** Médecin intervenant dans les situations d'urgence. */
    URGENTISTE("Urgences");

    /** Libellé lisible de la spécialité, destiné à l'affichage. */
    private final String libelle;

    /**
     * Construit une spécialité avec le libellé associé.
     *
     * @param libelle le nom complet de la spécialité
     */
    Specialite(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible de la spécialité.
     *
     * @return le libellé de la spécialité
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne le libellé lisible de la spécialité.
     *
     * @return le libellé de la spécialité
     */
    @Override
    public String toString() {
        return libelle;
    }
}