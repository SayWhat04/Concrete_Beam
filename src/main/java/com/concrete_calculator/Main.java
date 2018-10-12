package com.concrete_calculator;

import com.concrete_calculator.utils.FxmlUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    public static final String BORDER_PANE_MAIN_FXML = "/fxml/BorderPaneMain.fxml";

    public static void main(String[] args) {

        //TEST OF BENDING ALGORITHM
        /*
        double bendingMoment = 150;
        double height = 0.45;
        double width = 0.3;
        double effectiveDepth = 0.41;
        // Test of methods for calculation
        String selectedConcrete = "C25_30";
        String selectedSteel = "B_500";
        // Set rectangular section
        RectangularSection calculatedSection = new RectangularSection(height, width, Concrete.valueOf(selectedConcrete), Steel.valueOf(selectedSteel));
        calculatedSection.setEffectiveDepth(effectiveDepth);
        Solver solver = new Solver();
        double[] reinforcement = solver.calculateReinforcement(calculatedSection, bendingMoment);
        System.out.println(Arrays.toString(reinforcement));
        */

        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        //TODO: Set language for application

        Pane borderPane = FxmlUtils.fxmlLoader(BORDER_PANE_MAIN_FXML);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Concrete beam calculator");
        primaryStage.show();
    }
}
