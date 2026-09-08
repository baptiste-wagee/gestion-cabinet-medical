package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSupprimerMedecin {

  private static final String FXML_MEDECIN = "/fr/univartois/butinfo/sae/cabinet/vue/Medecin-view.fxml";

  private GestionMedecins gestionMedecins = GestionMedecins.getInstance();

  @FXML private TextField textFieldEnlever;
  @FXML private Button retour;
  @FXML private Button valideEnlever;

  @FXML
  private void initialize() {
    valideEnlever.setOnAction(e -> supprimer());
    retour.setOnAction(e -> retourListeMedecin());
  }

  public void setGestionMedecins(GestionMedecins gestionMedecins) {
    this.gestionMedecins = gestionMedecins;
  }

  private void supprimer() {
    String saisie = (textFieldEnlever.getText() == null) ? "" : textFieldEnlever.getText().trim();
    if (saisie.isEmpty() || gestionMedecins == null) {
      return;
    }
    int id;
    try {
      id = Integer.parseInt(saisie);
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "L'ID doit être un nombre entier.").showAndWait();
      return;
    }
    if (gestionMedecins.rechercherParId(id) == null) {
      new Alert(Alert.AlertType.WARNING, "Aucun médecin avec l'ID " + id).showAndWait();
      return;
    }
    gestionMedecins.supprimerMedecin(id);
    retourListeMedecin();
  }

  private void retourListeMedecin() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_MEDECIN));
      Parent root = loader.load();
      ControllerMedecin ctrl = loader.getController();
      ctrl.setGestionMedecins(gestionMedecins);
      Stage stage = (Stage) retour.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}