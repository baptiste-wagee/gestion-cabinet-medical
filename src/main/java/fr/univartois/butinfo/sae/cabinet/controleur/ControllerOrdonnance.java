package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
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

public class ControllerOrdonnance {

  private static final String FXML_AJOUTER = "/fr/univartois/butinfo/sae/cabinet/vue/AjouterOrdonnance-view.fxml";
  private static final String FXML_SUPPRIMER = "/fr/univartois/butinfo/sae/cabinet/vue/SupprimerOrdonnance-view.fxml";
  private static final String FXML_ACCUEIL = "/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml";

  private GestionOrdonnances gestionOrdonnances = GestionOrdonnances.getInstance();

  @FXML private ListView<Ordonnance> listViewOrdonnances;
  @FXML private Label IDOrdonnance;
  @FXML private Label medecinOrdonnance;
  @FXML private Label patientOrdonnance;
  @FXML private Label dateEmissionOrdonnance;
  @FXML private Label dateValiditeOrdonnance;
  @FXML private Label statutOrdonnance;
  @FXML private Label nbPrescriptionsOrdonnance;
  @FXML private Label prixTotalOrdonnance;
  @FXML private Label remarquesOrdonnance;
  @FXML private Button btnAccueil;
  @FXML private Button btnAjouter;
  @FXML private Button btnSupprimer;

  @FXML
  private void initialize() {
    listViewOrdonnances.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Ordonnance ordo, boolean empty) {
        super.updateItem(ordo, empty);
        setText((empty || ordo == null)
                ? null : "N°" + ordo.getId() + " - " + ordo.getPatient().getNomComplet());
      }
    });

    listViewOrdonnances.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> afficherDetails(nouveau));

    btnAccueil.setOnAction(e -> changerVue(FXML_ACCUEIL));

    listViewOrdonnances.setItems(gestionOrdonnances.getOrdonnances());
  }

  public void setGestionOrdonnances(GestionOrdonnances gestionOrdonnances) {
    this.gestionOrdonnances = gestionOrdonnances;
    listViewOrdonnances.setItems(gestionOrdonnances.getOrdonnances());
  }

  private void afficherDetails(Ordonnance ordo) {
    if (ordo == null) {
      IDOrdonnance.setText("");  medecinOrdonnance.setText("");  patientOrdonnance.setText("");
      dateEmissionOrdonnance.setText("");  dateValiditeOrdonnance.setText("");
      statutOrdonnance.setText("");  nbPrescriptionsOrdonnance.setText("");
      prixTotalOrdonnance.setText("");  remarquesOrdonnance.setText("");
      return;
    }
    IDOrdonnance.setText(String.valueOf(ordo.getId()));
    medecinOrdonnance.setText("Dr " + ordo.getMedecin().getNomComplet());
    patientOrdonnance.setText(ordo.getPatient().getNomComplet());
    dateEmissionOrdonnance.setText(String.valueOf(ordo.getDateEmission()));
    dateValiditeOrdonnance.setText(String.valueOf(ordo.getDateValidite()));
    statutOrdonnance.setText(ordo.estValide() ? "Valide" : "Expirée");
    nbPrescriptionsOrdonnance.setText(String.valueOf(ordo.getPrescriptions().size()));
    prixTotalOrdonnance.setText(String.format("%.2f €", ordo.getPrixTotal()));
    remarquesOrdonnance.setText(
            (ordo.getRemarques() == null || ordo.getRemarques().isEmpty()) ? "Aucune" : ordo.getRemarques());
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
    if (gestionOrdonnances == null) {
      new Alert(Alert.AlertType.ERROR,
              "gestionOrdonnances est null : la vue qui ouvre Ordonnance-view doit appeler setGestionOrdonnances(...).")
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
      if (controleur instanceof ControllerAjouterOrdonnance c) {
        c.setGestionOrdonnances(gestionOrdonnances);
      } else if (controleur instanceof ControllerSupprimerOrdonnance c) {
        c.setGestionOrdonnances(gestionOrdonnances);
      }
      Stage stage = (Stage) listViewOrdonnances.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }
}