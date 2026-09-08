package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerAccueil {

  private Stage stage;
  private Scene scenePrecedente;

  // Façade RDV injectée dans la vue Rendez-vous (voir note sur le singleton plus bas).
  private GestionRendezVous gestionRendezVous = GestionRendezVous.getInstance();

  @FXML private Button btnPatient;
  @FXML private Button btnMedecin;
  @FXML private Button btnMedicament;
  @FXML private Button btnOrdonnances;
  @FXML private Button btnRendezVous;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setScene(Scene scene) {
    this.scenePrecedente = scene;
  }

  @FXML
  private void ouvrirPatients(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/cabinet/vue/Patient-view.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) btnPatient.getScene().getWindow();
    stage.setScene(new Scene(root));
  }

  @FXML
  private void ouvrirMedecin(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/cabinet/vue/Medecin-view.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) btnMedecin.getScene().getWindow();
    stage.setScene(new Scene(root));
  }

  @FXML
  private void ouvrirMedicament(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/cabinet/vue/Medicament-view.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) btnMedicament.getScene().getWindow();
    stage.setScene(new Scene(root));
  }

  @FXML
  void ouvrirOrdonnances(ActionEvent event) {
    String chemin = "/fr/univartois/butinfo/sae/cabinet/vue/Ordonnance-view.fxml";
    java.net.URL url = getClass().getResource(chemin);
    if (url == null) {
      new Alert(Alert.AlertType.ERROR, "FXML introuvable : " + chemin).showAndWait();
      return;
    }
    try {
      FXMLLoader loader = new FXMLLoader(url);
      Parent root = loader.load();
      ControllerOrdonnance ctrl = loader.getController();
      ctrl.setGestionOrdonnances(GestionOrdonnances.getInstance());
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }

  @FXML
  void ouvrirRendezVous(ActionEvent event) {
    String chemin = "/fr/univartois/butinfo/sae/cabinet/vue/RendezVous-view.fxml"; // adapte au nom réel
    java.net.URL url = getClass().getResource(chemin);
    if (url == null) {
      new Alert(Alert.AlertType.ERROR, "FXML introuvable : " + chemin).showAndWait();
      return;
    }
    try {
      FXMLLoader loader = new FXMLLoader(url);
      Parent root = loader.load();

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      Scene scene = new Scene(root);

      ControllerRendezVous ctrl = loader.getController();
      ctrl.setGestionRendezVous(gestionRendezVous); // <-- l'injection qui manquait
      ctrl.setStage(stage);
      ctrl.setScene(scene);

      stage.setScene(scene);
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).showAndWait();
    }
  }
}