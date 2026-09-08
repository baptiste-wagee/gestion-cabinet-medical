package fr.univartois.butinfo.sae.cabinet.controleur;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAjouterPatient {

  private static final String FXML_PATIENT = "/fr/univartois/butinfo/sae/cabinet/vue/Patient-view.fxml";
  private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private GestionPatients gestionPatients = GestionPatients.getInstance();

  @FXML private ListView<Patient> listViewPatients;
  @FXML private TextField textfieldNom;
  @FXML private TextField textfieldPrenom;
  @FXML private TextField textfieldTelephone;
  @FXML private TextField textfieldEmail;
  @FXML private TextField textfieldAdresse;
  @FXML private TextField textfieldSecu;
  @FXML private TextField textfieldDateNaissance;
  @FXML private Button btnAjouter;
  @FXML private Button btnAccueil; // bouton "Retour"

  @FXML
  public void initialize() {
    btnAccueil.setOnAction(e -> retourListePatient());
    listViewPatients.getSelectionModel().selectedItemProperty()
            .addListener((obs, ancien, nouveau) -> {
              if (nouveau == null) {
                return;
              }
              textfieldNom.setText(nouveau.getNom());
              textfieldPrenom.setText(nouveau.getPrenom());
              textfieldTelephone.setText(nouveau.getTelephone());
              textfieldEmail.setText(nouveau.getEmail());
              textfieldAdresse.setText(nouveau.getAdresse());
              textfieldSecu.setText(nouveau.getNumeroSecu());
              textfieldDateNaissance.setText(
                      nouveau.getDateNaissance() != null ? nouveau.getDateNaissance().format(FORMAT_DATE) : "");
            });
  }

  public void setGestionPatients(GestionPatients gestionPatients) {
    this.gestionPatients = gestionPatients;
    listViewPatients.setItems(gestionPatients.getPatients());
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    if (gestionPatients == null) {
      return;
    }
    String nom = textfieldNom.getText();
    String secu = textfieldSecu.getText();
    if (nom == null || nom.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le nom est obligatoire.").showAndWait();
      return;
    }
    if (secu == null || secu.isBlank()) {
      new Alert(Alert.AlertType.WARNING, "Le numéro de sécu est obligatoire.").showAndWait();
      return;
    }
    LocalDate dateNaissance = null;
    String dateTxt = textfieldDateNaissance.getText();
    if (dateTxt != null && !dateTxt.isBlank()) {
      try {
        dateNaissance = LocalDate.parse(dateTxt.trim(), FORMAT_DATE);
      } catch (DateTimeParseException e) {
        new Alert(Alert.AlertType.WARNING, "Date invalide. Format : jj/mm/aaaa").showAndWait();
        return;
      }
    }
    gestionPatients.ajouterPatient(new Patient(
            nom, textfieldPrenom.getText(), textfieldTelephone.getText(),
            textfieldEmail.getText(), textfieldAdresse.getText(), dateNaissance, secu.trim()));
    retourListePatient();
  }

  private void retourListePatient() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_PATIENT));
      Parent root = loader.load();
      ControllerPatient ctrl = loader.getController();
      ctrl.setGestionPatients(gestionPatients);
      Stage stage = (Stage) btnAccueil.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}