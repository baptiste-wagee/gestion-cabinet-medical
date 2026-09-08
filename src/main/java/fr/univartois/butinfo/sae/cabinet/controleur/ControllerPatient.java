package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerPatient {

  private static final String FXML_AJOUTER = "/fr/univartois/butinfo/sae/cabinet/vue/AjouterPatient.fxml";
  private static final String FXML_SUPPRIMER = "/fr/univartois/butinfo/sae/cabinet/vue/SupprimerPatient-view.fxml";
  private static final String FXML_ACCUEIL = "/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml";

  private GestionPatients gestionPatients = GestionPatients.getInstance();

  @FXML private ListView<Patient> listViewPatients;
  @FXML private TextField rechercherPatient;
  @FXML private Label IDpatient;
  @FXML private Label nomPatient;
  @FXML private Label prenomPatient;
  @FXML private Label telephonePatient;
  @FXML private Label emailPatient;
  @FXML private Label adressePatient;
  @FXML private Button btnAccueil;
  @FXML private Button btnAjouter;
  @FXML private Button btnSupprimer;

  @FXML
  private void initialize() {
    listViewPatients.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Patient patient, boolean empty) {
        super.updateItem(patient, empty);
        setText((empty || patient == null)
                ? null : patient.getNom().toUpperCase() + " " + patient.getPrenom());
      }
    });

    listViewPatients.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> afficherDetails(nouveau));

    btnAccueil.setOnAction(e -> changerVue(FXML_ACCUEIL));

    listViewPatients.setItems(gestionPatients.getPatients());

    // --- RECHERCHE : filtre la liste au fur et à mesure de la saisie ---
    rechercherPatient.textProperty().addListener((obs, ancien, nouveau) -> {
      if (nouveau == null || nouveau.isBlank()) {
        listViewPatients.setItems(gestionPatients.getPatients()); // liste complète
      } else {
        listViewPatients.setItems(
                FXCollections.observableArrayList(gestionPatients.rechercherParNom(nouveau)));
      }
    });
  }

  public void setGestionPatients(GestionPatients gestionPatients) {
    this.gestionPatients = gestionPatients;
    listViewPatients.setItems(gestionPatients.getPatients());
  }

  private void afficherDetails(Patient patient) {
    if (patient == null) {
      IDpatient.setText("");  nomPatient.setText("");  prenomPatient.setText("");
      telephonePatient.setText("");  emailPatient.setText("");  adressePatient.setText("");
      return;
    }
    IDpatient.setText(String.valueOf(patient.getId()));
    nomPatient.setText(patient.getNom());
    prenomPatient.setText(patient.getPrenom());
    telephonePatient.setText(patient.getTelephone() != null ? patient.getTelephone() : "Non renseigné");
    emailPatient.setText(patient.getEmail() != null ? patient.getEmail() : "Non renseigné");
    adressePatient.setText(patient.getAdresse() != null ? patient.getAdresse() : "Non renseignée");
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    changerVue(FXML_AJOUTER);
  }

  @FXML
  void btnSupprimer(ActionEvent event) {
    changerVue(FXML_SUPPRIMER);
  }

  /** Charge un FXML, lui passe la façade, et remplace la scène de la fenêtre courante. */
  private void changerVue(String cheminFxml) {
    if (gestionPatients == null) {
      new Alert(Alert.AlertType.ERROR,
              "gestionPatients est null : l'accueil qui ouvre Patient-view doit appeler setGestionPatients(...).")
              .showAndWait();
      return;
    }
    java.net.URL url = getClass().getResource(cheminFxml);
    if (url == null) {
      new Alert(Alert.AlertType.ERROR, "FXML introuvable : " + cheminFxml).showAndWait();
      return;
    }
    try {
      FXMLLoader loader = new FXMLLoader(url);
      Parent root = loader.load();
      Object controleur = loader.getController();
      if (controleur instanceof ControllerAjouterPatient c) {
        c.setGestionPatients(gestionPatients);
      }
      Stage stage = (Stage) listViewPatients.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }
}