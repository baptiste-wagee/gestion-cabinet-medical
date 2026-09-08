package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Représente un médicament disponible dans le système médical.
 * <p>
 * Un médicament est caractérisé par un identifiant unique, un nom,
 * une description et un prix. L'identifiant est généré automatiquement
 * lors de la création de l'objet.
 * </p>
 */
public class Medicament {

  /**
   * Identifiant unique du médicament.
   */
  private final int id;

  /**
   * Nom commercial du médicament.
   */
  private String nom;

  /**
   * Description du médicament.
   */
  private String description;

  /**
   * Prix du médicament.
   */
  private double prix;

  /**
   * Compteur utilisé pour générer automatiquement les identifiants.
   */
  private static int compteurId = 1;

  /**
   * Construit un médicament avec son nom, sa description et son prix.
   *
   * @param nom Le nom du médicament.
   * @param description La description du médicament.
   * @param prix Le prix du médicament.
   */
  public Medicament(String nom, String description, double prix) {
    this.id = compteurId++;
    this.nom = nom;
    this.description = description;
    this.prix = prix;
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne l'identifiant du médicament.
   *
   * @return L'identifiant unique du médicament.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le nom du médicament.
   *
   * @return Le nom du médicament.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Modifie le nom du médicament.
   *
   * @param nom Le nouveau nom du médicament.
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Retourne la description du médicament.
   *
   * @return La description du médicament.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Modifie la description du médicament.
   *
   * @param description La nouvelle description du médicament.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Retourne le prix du médicament.
   *
   * @return Le prix du médicament.
   */
  public double getPrix() {
    return prix;
  }

  /**
   * Modifie le prix du médicament.
   *
   * @param prix Le nouveau prix du médicament.
   */
  public void setPrix(double prix) {
    this.prix = prix;
  }

  /**
   * Vérifie si un objet est égal à ce médicament.
   * <p>
   * Deux médicaments sont considérés comme égaux s'ils possèdent
   * le même nom (sans tenir compte de la casse) et le même prix.
   * </p>
   *
   * @param obj L'objet à comparer.
   *
   * @return true si les deux médicaments sont égaux, false sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Medicament autre = (Medicament) obj;
    return this.nom.equalsIgnoreCase(autre.nom) &&
            this.prix == autre.prix;
  }

  /**
   * Calcule le code de hachage du médicament.
   *
   * @return Le code de hachage basé sur le nom et le prix.
   */
  @Override
  public int hashCode() {
    int result = nom.toLowerCase().hashCode();
    result = 31 * result + Double.hashCode(prix);
    return result;
  }

  /**
   * Retourne une représentation textuelle du médicament.
   *
   * @return Une chaîne contenant l'identifiant, le nom,
   *         le prix et la description du médicament.
   */
  @Override
  public String toString() {
    return "Médicament { [ID=" + id + "] " + nom +
            " | Prix: " + prix +
            " | " + description + " }";
  }
}