package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Représente une ligne de prescription dans une ordonnance médicale.
 * <p>
 * Une ligne de prescription associe un médicament à des informations
 * précises concernant son administration, telles que la quantité prescrite,
 * la posologie, la durée du traitement et les éventuelles instructions
 * particulières destinées au patient.
 * </p>
 */
public class LignePrescription {

  /**
   * Médicament prescrit.
   */
  private final Medicament medicament;

  /**
   * Quantité de médicament prescrite.
   */
  private int quantite;

  /**
   * Posologie à suivre pour le traitement.
   * Exemple : "1 comprimé matin et soir".
   */
  private String posologie;

  /**
   * Durée du traitement exprimée en jours.
   */
  private int dureeTraitement;

  /**
   * Consignes particulières associées à la prescription.
   */
  private String instructions;

  /**
   * Constructeur complet d'une ligne de prescription.
   *
   * @param medicament       Le médicament prescrit.
   * @param quantite         La quantité prescrite.
   * @param posologie        La posologie à respecter.
   * @param dureeTraitement  La durée du traitement en jours.
   * @param instructions     Les instructions particulières à suivre.
   */
  public LignePrescription(Medicament medicament, int quantite, String posologie,
                           int dureeTraitement, String instructions) {
    this.medicament = medicament;
    this.quantite = quantite;
    this.posologie = posologie;
    this.dureeTraitement = dureeTraitement;
    this.instructions = instructions;
  }

  /**
   * Construit une ligne de prescription avec une quantité par défaut de 1
   * et sans instruction particulière.
   *
   * @param medicament      Le médicament prescrit.
   * @param posologie       La posologie à respecter.
   * @param dureeTraitement La durée du traitement en jours.
   */
  public LignePrescription(Medicament medicament, String posologie, int dureeTraitement) {
    this(medicament, 1, posologie, dureeTraitement, "Aucune instruction particulière");
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne le médicament associé à cette prescription.
   *
   * @return Le médicament prescrit.
   */
  public Medicament getMedicament() {
    return medicament;
  }

  /**
   * Retourne la posologie du traitement.
   *
   * @return La posologie à suivre.
   */
  public String getPosologie() {
    return posologie;
  }

  /**
   * Modifie la posologie du traitement.
   *
   * @param posologie La nouvelle posologie.
   */
  public void setPosologie(String posologie) {
    this.posologie = posologie;
  }

  /**
   * Retourne la durée du traitement.
   *
   * @return La durée du traitement en jours.
   */
  public int getDureeTraitement() {
    return dureeTraitement;
  }

  /**
   * Modifie la durée du traitement.
   *
   * @param dureeTraitement La nouvelle durée du traitement en jours.
   */
  public void setDureeTraitement(int dureeTraitement) {
    this.dureeTraitement = dureeTraitement;
  }

  /**
   * Retourne les instructions particulières associées à la prescription.
   *
   * @return Les instructions du traitement.
   */
  public String getInstructions() {
    return instructions;
  }

  /**
   * Modifie les instructions particulières du traitement.
   *
   * @param instructions Les nouvelles instructions.
   */
  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  /**
   * Retourne la quantité prescrite.
   *
   * @return La quantité du médicament prescrite.
   */
  public int getQuantite() {
    return quantite;
  }

  /**
   * Modifie la quantité prescrite.
   *
   * @param quantite La nouvelle quantité prescrite.
   */
  public void setQuantite(int quantite) {
    this.quantite = quantite;
  }

  /**
   * Retourne une représentation textuelle de la ligne de prescription.
   *
   * @return Une chaîne de caractères décrivant le médicament,
   *         la quantité, la posologie, la durée du traitement
   *         et les instructions associées.
   */
  @Override
  public String toString() {
    return "  → " + medicament.getNom() +
            " | Quantité: " + quantite +
            " | Posologie: " + posologie +
            " | Durée: " + dureeTraitement + " jours" +
            " | Instructions: " + instructions;
  }
}