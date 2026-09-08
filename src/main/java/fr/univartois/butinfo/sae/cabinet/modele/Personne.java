package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Représente une personne du cabinet médical.
 * <p>
 * Cette classe abstraite constitue la superclasse commune aux patients
 * et aux médecins. Elle regroupe les informations générales d'une personne
 * telles que son identité et ses coordonnées.
 * </p>
 */
public abstract class Personne {

    /**
     * Identifiant unique de la personne.
     */
    private final int id;

    /**
     * Nom de famille de la personne.
     */
    protected String nom;

    /**
     * Prénom de la personne.
     */
    protected String prenom;

    /**
     * Numéro de téléphone de la personne.
     */
    protected String telephone;

    /**
     * Adresse électronique de la personne.
     */
    protected String email;

    /**
     * Compteur utilisé pour générer automatiquement les identifiants.
     */
    private static int compteurId = 1;

    /**
     * Construit une personne avec ses informations principales.
     *
     * @param nom Le nom de la personne.
     * @param prenom Le prénom de la personne.
     * @param telephone Le numéro de téléphone de la personne.
     * @param email L'adresse électronique de la personne.
     */
    public Personne(String nom, String prenom, String telephone, String email) {
        this.id = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    /**
     * Retourne l'identifiant de la personne.
     *
     * @return L'identifiant unique.
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de la personne.
     *
     * @return Le nom de la personne.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de la personne.
     *
     * @param nom Le nouveau nom.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prénom de la personne.
     *
     * @return Le prénom de la personne.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Modifie le prénom de la personne.
     *
     * @param prenom Le nouveau prénom.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le numéro de téléphone de la personne.
     *
     * @return Le numéro de téléphone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Modifie le numéro de téléphone de la personne.
     *
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Retourne l'adresse électronique de la personne.
     *
     * @return L'adresse électronique.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Modifie l'adresse électronique de la personne.
     *
     * @param email La nouvelle adresse électronique.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le nom complet de la personne.
     *
     * @return Le prénom suivi du nom.
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /**
     * Vérifie si un objet est égal à cette personne.
     * Deux personnes sont considérées comme égales lorsqu'elles
     * possèdent le même identifiant.
     *
     * @param obj L'objet à comparer.
     *
     * @return true si les deux objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Personne autre = (Personne) obj;
        return this.id == autre.id;
    }

    /**
     * Calcule le code de hachage de la personne.
     *
     * @return Le code de hachage basé sur l'identifiant.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Retourne une représentation textuelle de la personne.
     *
     * @return Une chaîne contenant l'identifiant,
     *         le nom et les coordonnées de la personne.
     */
    @Override
    public String toString() {
        return "[ID=" + id + "] " + prenom + " " + nom +
                " | Tél: " + telephone +
                " | Email: " + email;
    }
}