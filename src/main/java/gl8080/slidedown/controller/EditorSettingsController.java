package gl8080.slidedown.controller;

import gl8080.slidedown.model.InvalidInputException;
import gl8080.slidedown.model.settings.Settings;
import gl8080.slidedown.model.settings.SettingsTransaction;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditorSettingsController implements Initializable {
    
    public static void showStage(Stage stage) {
        FXMLLoader loader = new FXMLLoader(EditorSettingsController.class.getResource("/fxml/editor_settings.fxml"));
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Parent root = loader.getRoot();

        Scene scene = new Scene(root);

        Stage dialog = new Stage(StageStyle.UTILITY);

        EditorSettingsController controller = loader.getController();
        controller.setStage(dialog);

        dialog.setTitle("Editor Settings");
        dialog.setScene(scene);
        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
    
    @FXML
    private ComboBox<String> fontFamilyComboBox;
    @FXML
    private TextField fontSizeTextField;
    
    private Stage stage;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList(Font.getFontNames());
        this.fontFamilyComboBox.setItems(list);
        
        Settings settings = Settings.instance;
        
        this.fontSizeTextField.setText(String.valueOf(settings.getFontSize()));
        this.fontFamilyComboBox.getSelectionModel().select(settings.getFontFamily());
    }
    
    @FXML
    public void onOkAction() {
        this.onApplyAction();
        this.stage.hide();
    }
    
    @FXML
    public void onApplyAction() {
        try {
            SettingsTransaction.with(tx -> {
                tx.setFontFamily(this.fontFamilyComboBox.getValue());
                tx.setFontSize(Integer.parseInt(this.fontSizeTextField.getText()));
            });
        } catch (NumberFormatException e) {
            DialogController.showErrorDialog(this.stage, "フォントサイズは数値で指定してください。");
        } catch (InvalidInputException ex) {
            DialogController.showErrorDialog(this.stage, ex.getMessage());
        }
    }
    
    @FXML
    public void onCancelAction() {
        this.stage.hide();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
