package com.concrete_calculator.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FxmlUtils {

    public static Pane fxmlLoader (String fxmlPath){
        FXMLLoader loader = new FXMLLoader(FxmlUtils.class.getResource(fxmlPath));
        //System.out.println(FxmlUtils.class.getResource(fxmlPath).toString());

        //TODO: Load resource Bundles

        try {
            return loader.load();
        } catch (IOException e) {
            //TODO: Prepare custom handling of Exceptions
            e.printStackTrace();
        }
        return null;
    }
}
