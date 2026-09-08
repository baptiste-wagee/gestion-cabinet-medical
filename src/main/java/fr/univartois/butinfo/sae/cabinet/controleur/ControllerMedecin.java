package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
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
import javafx.stage.Stage;

public class ControllerMedecin {

  private static final String FXML_AJOUTER = "/fr/univartois/butinfo/sae/cabinet/vue/AjouterMedecin-view.fxml";
  private static final String FXML_SUPPRIMER = "/fr/univartois/butinfo/sae/cabinet/vue/SupprimerMedecin-view.fxml";
  private static final String FXML_ACCUEIL = "/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml";

  private GestionMedecins gestionMedecins = GestionMedecins.getInstance();

  @FXML private ListView<Medecin> listViewMedecins;
  @FXML private Label IDMedecin;
  @FXML private Label nomMedecin;
  @FXML private Label prenomMedecin;
  @FXML private Label telephoneMedecin;
  @FXML private Label emailMedecin;
  @FXML private Label numeroOrdreMedecin;
  @FXML private Label specialiteMedecin;
  @FXML private Label tarifMedecin;
  @FXML private Button btnAccueil;
  @FXML private Button btnAjouter;
  @FXML private Button btnSupprimer;

  @FXML
  private void initialize() {
    listViewMedecins.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Medecin medecin, boolean empty) {
        super.updateItem(medecin, empty);
        setText((empty || medecin == null)
                ? null : medecin.getNom().toUpperCase() + " " + medecin.getPrenom());
      }
    });

    listViewMedecins.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> afficherDetails(nouveau));

    btnAccueil.setOnAction(e -> changerVue(FXML_ACCUEIL));

    listViewMedecins.setItems(gestionMedecins.getMedecins());
  }

  public void setGestionMedecins(GestionMedecins gestionMedecins) {
    this.gestionMedecins = gestionMedecins;
    listViewMedecins.setItems(gestionMedecins.getMedecins());
  }

  private void afficherDetails(Medecin medecin) {
    if (medecin == null) {
      IDMedecin.setText("");  nomMedecin.setText("");  prenomMedecin.setText("");
      telephoneMedecin.setText("");  emailMedecin.setText("");
      numeroOrdreMedecin.setText("");  specialiteMedecin.setText("");  tarifMedecin.setText("");
      return;
    }
    IDMedecin.setText(String.valueOf(medecin.getId()));
    nomMedecin.setText(medecin.getNom());
    prenomMedecin.setText(medecin.getPrenom());
    telephoneMedecin.setText(medecin.getTelephone() != null ? medecin.getTelephone() : "Non renseigné");
    emailMedecin.setText(medecin.getEmail() != null ? medecin.getEmail() : "Non renseigné");
    numeroOrdreMedecin.setText(medecin.getNumeroOrdre());
    specialiteMedecin.setText(medecin.getSpecialite() != null ? medecin.getSpecialite().getLibelle() : "");
    tarifMedecin.setText(medecin.getTarifHoraire() + " €/h");
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    changerVue(FXML_AJOUTER);
  }

  @FXML
  void btnSupprimer(ActionEvent event) {
    changerVue(FXML_SUPPRIMER);
  }

  private void changerVue(String cheminFxml) {
    if (gestionMedecins == null) {
      new Alert(Alert.AlertType.ERROR,
              "gestionMedecins est null : la vue qui ouvre Medecin-view doit appeler setGestionMedecins(...).")
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
      if (controleur instanceof ControllerAjouterMedecin c) {
        c.setGestionMedecins(gestionMedecins);
      } else if (controleur instanceof ControllerSupprimerMedecin c) {
        c.setGestionMedecins(gestionMedecins);
      }
      Stage stage = (Stage) listViewMedecins.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }
}