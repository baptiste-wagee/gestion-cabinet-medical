package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
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

public class ControllerMedicament {

  private static final String FXML_AJOUTER = "/fr/univartois/butinfo/sae/cabinet/vue/AjouterMedicament-view.fxml";
  private static final String FXML_SUPPRIMER = "/fr/univartois/butinfo/sae/cabinet/vue/SupprimerMedicament-view.fxml";
  private static final String FXML_ACCUEIL = "/fr/univartois/butinfo/sae/cabinet/vue/Accueil-view.fxml";

  private GestionMedicaments gestionMedicaments = GestionMedicaments.getInstance();

  @FXML private ListView<Medicament> listViewMedicaments;
  @FXML private Label IDMedicament;
  @FXML private Label nomMedicament;
  @FXML private Label descriptionMedicament;
  @FXML private Label prixMedicament;
  @FXML private Button btnAccueil;
  @FXML private Button btnAjouter;
  @FXML private Button btnSupprimer;

  @FXML
  private void initialize() {
    listViewMedicaments.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Medicament medicament, boolean empty) {
        super.updateItem(medicament, empty);
        setText((empty || medicament == null)
                ? null : medicament.getNom().toUpperCase() + " (" + medicament.getPrix() + " €)");
      }
    });

    listViewMedicaments.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> afficherDetails(nouveau));

    btnAccueil.setOnAction(e -> changerVue(FXML_ACCUEIL));

    listViewMedicaments.setItems(gestionMedicaments.getMedicaments());
  }

  public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
    this.gestionMedicaments = gestionMedicaments;
    listViewMedicaments.setItems(gestionMedicaments.getMedicaments());
  }

  private void afficherDetails(Medicament medicament) {
    if (medicament == null) {
      IDMedicament.setText("");  nomMedicament.setText("");
      descriptionMedicament.setText("");  prixMedicament.setText("");
      return;
    }
    IDMedicament.setText(String.valueOf(medicament.getId()));
    nomMedicament.setText(medicament.getNom());
    descriptionMedicament.setText(
            medicament.getDescription() != null ? medicament.getDescription() : "Non renseignée");
    prixMedicament.setText(medicament.getPrix() + " €");
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
    if (gestionMedicaments == null) {
      new Alert(Alert.AlertType.ERROR,
              "gestionMedicaments est null : la vue qui ouvre Medicament-view doit appeler setGestionMedicaments(...).")
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
      if (controleur instanceof ControllerAjouterMedicament c) {
        c.setGestionMedicaments(gestionMedicaments);
      }
      Stage stage = (Stage) listViewMedicaments.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }
}