package com.concrete_calculator.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtils {

    private static final ResourceBundle resourceBundle = FxmlUtils.getResourceBundle();

    public static Optional<ButtonType> confirmationDialog() {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(resourceBundle.getString("exit.title"));
        confirmationDialog.setHeaderText(resourceBundle.getString("exit.header"));
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result;
    }

    public static void dialogAboutApplication() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(resourceBundle.getString("about.title"));
        informationAlert.setHeaderText(resourceBundle.getString("about.header"));
        informationAlert.setContentText(resourceBundle.getString("about.content"));
        informationAlert.showAndWait();
    }
}