package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Énumération des statuts possibles d'un rendez-vous médical.
 *
 * <p>Chaque statut possède un libellé lisible destiné à l'affichage.</p>
 */
public enum StatutRendezVous {

    /** Le rendez-vous a été créé mais pas encore confirmé. */
    PLANIFIE("Planifié"),

    /** Le rendez-vous a été confirmé par le patient ou le cabinet. */
    CONFIRME("Confirmé"),

    /** Le rendez-vous a été annulé. */
    ANNULE("Annulé"),

    /** Le rendez-vous a eu lieu et est terminé. */
    TERMINE("Terminé");

    /** Libellé lisible du statut, destiné à l'affichage. */
    private final String libelle;

    /**
     * Construit un statut avec le libellé associé.
     *
     * @param libelle le libellé lisible du statut
     */
    StatutRendezVous(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du statut.
     *
     * @return le libellé du statut
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne le libellé lisible du statut.
     *
     * @return le libellé du statut
     */
    @Override
    public String toString() {
        return libelle;
    }
}