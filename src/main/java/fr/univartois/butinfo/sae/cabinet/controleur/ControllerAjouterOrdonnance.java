package fr.univartois.butinfo.sae.cabinet.controleur;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerAjouterOrdonnance {

  private static final String FXML_ORDONNANCE = "/fr/univartois/butinfo/sae/cabinet/vue/Ordonnance-view.fxml";
  private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private GestionOrdonnances gestionOrdonnances = GestionOrdonnances.getInstance();
  private final GestionMedecins gestionMedecins = GestionMedecins.getInstance();
  private final GestionPatients gestionPatients = GestionPatients.getInstance();

  @FXML private ListView<Ordonnance> listViewOrdonnances;
  @FXML private ComboBox<Medecin> comboMedecin;
  @FXML private ComboBox<Patient> comboPatient;
  @FXML private TextField textfieldDateValidite;
  @FXML private TextArea textfieldRemarques;
  @FXML private Button btnAjouter;
  @FXML private Button btnAccueil; // bouton "Retour"

  @FXML
  public void initialize() {
    btnAccueil.setOnAction(e -> retourListeOrdonnance());

    comboMedecin.setItems(gestionMedecins.getMedecins());
    comboMedecin.setCellFactory(lv -> creerCelluleMedecin());
    comboMedecin.setButtonCell(creerCelluleMedecin());

    comboPatient.setItems(gestionPatients.getPatients());
    comboPatient.setCellFactory(lv -> creerCellulePatient());
    comboPatient.setButtonCell(creerCellulePatient());

    listViewOrdonnances.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(Ordonnance ordo, boolean empty) {
        super.updateItem(ordo, empty);
        setText((empty || ordo == null)
                ? null : "N°" + ordo.getId() + " - " + ordo.getPatient().getNomComplet());
      }
    });
  }

  private ListCell<Medecin> creerCelluleMedecin() {
    return new ListCell<>() {
      @Override
      protected void updateItem(Medecin m, boolean empty) {
        super.updateItem(m, empty);
        setText((empty || m == null) ? null : "Dr " + m.getNomComplet());
      }
    };
  }

  private ListCell<Patient> creerCellulePatient() {
    return new ListCell<>() {
      @Override
      protected void updateItem(Patient p, boolean empty) {
        super.updateItem(p, empty);
        setText((empty || p == null) ? null : p.getNomComplet());
      }
    };
  }

  public void setGestionOrdonnances(GestionOrdonnances gestionOrdonnances) {
    this.gestionOrdonnances = gestionOrdonnances;
    listViewOrdonnances.setItems(gestionOrdonnances.getOrdonnances());
  }

  @FXML
  void btnAjouter(ActionEvent event) {
    if (gestionOrdonnances == null) {
      return;
    }
    Medecin medecin = comboMedecin.getValue();
    Patient patient = comboPatient.getValue();
    if (medecin == null) {
      new Alert(Alert.AlertType.WARNING, "Le médecin est obligatoire.").showAndWait();
      return;
    }
    if (patient == null) {
      new Alert(Alert.AlertType.WARNING, "Le patient est obligatoire.").showAndWait();
      return;
    }
    // Date de validité : vide => +3 mois (comportement par défaut de la classe Ordonnance)
    LocalDate dateValidite = LocalDate.now().plusMonths(3);
    String dateTxt = textfieldDateValidite.getText();
    if (dateTxt != null && !dateTxt.isBlank()) {
      try {
        dateValidite = LocalDate.parse(dateTxt.trim(), FORMAT_DATE);
      } catch (DateTimeParseException e) {
        new Alert(Alert.AlertType.WARNING, "Date invalide. Format : jj/mm/aaaa").showAndWait();
        return;
      }
    }
    String remarques = textfieldRemarques.getText() == null ? "" : textfieldRemarques.getText();
    gestionOrdonnances.creerOrdonnance(medecin, patient, dateValidite, remarques);
    retourListeOrdonnance();
  }

  private void retourListeOrdonnance() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_ORDONNANCE));
      Parent root = loader.load();
      ControllerOrdonnance ctrl = loader.getController();
      ctrl.setGestionOrdonnances(gestionOrdonnances);
      Stage stage = (Stage) btnAccueil.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (Exception e) {
      new Alert(Alert.AlertType.ERROR, "Erreur retour : " + e.getMessage()).showAndWait();
    }
  }
}