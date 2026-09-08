package fr.univartois.butinfo.sae.cabinet.controleur;

import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSupprimerOrdonnance {

  private static final String FXML_ORDONNANCE = "/fr/univartois/butinfo/sae/cabinet/vue/Ordonnance-view.fxml";

  private GestionOrdonnances gestionOrdonnances = GestionOrdonnances.getInstance();

  @FXML private TextField textFieldEnlever;
  @FXML private Button retour;
  @FXML private Button valideEnlever;

  @FXML
  private void initialize() {
    valideEnlever.setOnAction(e -> supprimer());
    retour.setOnAction(e -> retourListeOrdonnance());
  }

  public void setGestionOrdonnances(GestionOrdonnances gestionOrdonnances) {
    this.gestionOrdonnances = gestionOrdonnances;
  }

  private void supprimer() {
    String saisie = (textFieldEnlever.getText() == null) ? "" : textFieldEnlever.getText().trim();
    if (saisie.isEmpty() || gestionOrdonnances == null) {
      return;
    }
    int id;
    try {
      id = Integer.parseInt(saisie);
    } catch (NumberFormatException e) {
      new Alert(Alert.AlertType.WARNING, "L'ID doit être un nombre entier.").showAndWait();
      return;
    }
    if (gestionOrdonnances.rechercherParId(id) == null) {
      new Alert(Alert.AlertType.WARNING, "Aucune ordonnance avec l'ID " + id).showAndWait();
      return;
    }
    gestionOrdonnances.supprimerOrdonnance(id);
    retourListeOrdonnance();
  }

  private void retourListeOrdonnance() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ORDONNANCE));
      Parent root = loader.load();
      ControllerOrdonnance ctrl = loader.getController();
      ctrl.setGestionOrdonnances(gestionOrdonnances);
      Stage stage = (Stage) retour.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}