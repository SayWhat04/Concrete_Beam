package com.concrete_calculator.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ResourceBundle;

public class FxmlUtils {

    public static Pane fxmlLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));

        try {
            return loader.load();
        } catch (IOException e) {
            //TODO: Prepare custom handling of Exceptions
            e.printStackTrace();
        }
        return null;
    }

    public static FXMLLoader getLoader(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        loader.setResources(getResourceBundle());
        return loader;
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("bundles.message");
    }
}
