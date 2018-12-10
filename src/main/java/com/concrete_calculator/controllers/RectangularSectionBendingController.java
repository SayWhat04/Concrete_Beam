package com.concrete_calculator.controllers;

import com.concrete_calculator.Constants;
import com.concrete_calculator.RegExps;
import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.models.ForcesSet;
import com.concrete_calculator.reinforcement.ReinforcementProperties;
import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.reinforcement.Rebar;
import com.concrete_calculator.materials.Steel;
import com.concrete_calculator.solvers.Solver;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.util.Arrays;

public class RectangularSectionBendingController {

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private ComboBox bottomMainBarsComboBox;

    @FXML
    private ComboBox topMainBarsComboBox;

    @FXML
    private ComboBox stirrupsComboBox;

    @FXML
    private TextField bottomCoverageTextField;

    @FXML
    private TextField topCoverageTextField;

    @FXML
    private ComboBox<Concrete> concreteClassComboBox;

    @FXML
    private ComboBox<Steel> steelClassComboBox;

    @FXML
    private TextField bendingMomentTextField;

    @FXML
    private TextField bottomCalculatedReinforcementTextField;

    @FXML
    private TextField bottomBarsNumberTextField;

    @FXML
    private TextField bottomActualReinforcementTextField;

    @FXML
    private TextField topCalculatedReinforcementTextField;

    @FXML
    private TextField topBarsNumberTextField;

    @FXML
    private TextField topActualReinforcementTextField;

    @FXML
    private Button calculateButton;

    @FXML
    public void initialize() {
        fillComboBoxes();
        initBindings();
        initListeners();
    }

    private void fillComboBoxes() {
        bottomMainBarsComboBox.getItems().addAll(Constants.MAIN_BARS_DIAMETERS);
        topMainBarsComboBox.getItems().addAll(Constants.MAIN_BARS_DIAMETERS);
        stirrupsComboBox.getItems().addAll(Constants.STIRRUPS_DIAMETERS);
        concreteClassComboBox.getItems().setAll(Concrete.values());
        steelClassComboBox.getItems().setAll(Steel.values());
    }

    private void initBindings() {
        calculateButton.disableProperty().bind(Bindings.isEmpty(widthTextField.textProperty())
                .or(Bindings.isEmpty(heightTextField.textProperty()))
                .or(Bindings.isEmpty(lengthTextField.textProperty()))
                .or(Bindings.isNull(bottomMainBarsComboBox.valueProperty()))
                .or(Bindings.isNull(topMainBarsComboBox.valueProperty()))
                .or(Bindings.isNull(stirrupsComboBox.valueProperty()))
                .or(Bindings.isEmpty(bottomCoverageTextField.textProperty()))
                .or(Bindings.isEmpty(topCoverageTextField.textProperty()))
                .or(Bindings.isNull(concreteClassComboBox.valueProperty()))
                .or(Bindings.isNull(steelClassComboBox.valueProperty()))
                .or(Bindings.isEmpty(bendingMomentTextField.textProperty()))
        );
    }

    private void initListeners() {
        widthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_INTEGER)) {
                widthTextField.setText(oldValue);
            }
        });
        heightTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_INTEGER)) {
                heightTextField.setText(oldValue);
            }
        });
        lengthTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_INTEGER)) {
                lengthTextField.setText(oldValue);
            }
        });
        bottomCoverageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_TWO_DIGITS_INTEGER)) {
                bottomCoverageTextField.setText(oldValue);
            }
        });
        topCoverageTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_TWO_DIGITS_INTEGER)) {
                topCoverageTextField.setText(oldValue);
            }
        });
        bendingMomentTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.ANY_DOUBLE_REGEX)) {
                bendingMomentTextField.setText(oldValue);
            }
        });
    }

    public void calculateBending() {
        Section rectangularSection = new RectangularSection(Double.parseDouble(heightTextField.getText()), Double.parseDouble(widthTextField.getText()));
        Geometry calculatedGeometry = new Geometry(rectangularSection, Double.parseDouble(lengthTextField.getText()));

        ForcesSet pureBendingForcesSet = new ForcesSet();
        pureBendingForcesSet.setBendingMoment(Double.parseDouble(bendingMomentTextField.getText()));

        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));
        double bottomReinforcementCoverage = Double.parseDouble(bottomCoverageTextField.getText());
        double topReinforcementCoverage = Double.parseDouble(topCoverageTextField.getText());
        ReinforcementProperties reinforcementProperties = new ReinforcementProperties(bottomMainBars, topMainBars, stirrups, bottomReinforcementCoverage, topReinforcementCoverage);

        String selectedConcreteClass = concreteClassComboBox.getValue().toString();
        CalculationModel pureBendingCalculationModel = new CalculationModel(calculatedGeometry, pureBendingForcesSet, reinforcementProperties, Concrete.valueOf(selectedConcreteClass));
        Solver pureBendingSolver = new Solver();

        double[] reinforcement = pureBendingSolver.calculateReinforcementPureBendingRectangularSection(pureBendingCalculationModel);

        double bottomReinforcementCrossSection = reinforcement[0];
        double topReinforcementCrossSection = reinforcement[1];
        //TEST
        System.out.println(Arrays.toString(reinforcement));

        int numberOfBottomBars = Solver.calculateNumberOfBars(bottomReinforcementCrossSection, bottomMainBars.getBarDiameter());
        int numberOfTopBars = Solver.calculateNumberOfBars(topReinforcementCrossSection, topMainBars.getBarDiameter());

        double actualBottomReinforcementCrossSection = numberOfBottomBars * (Math.PI * Math.pow(Integer.parseInt(bottomMainBarsComboBox.getValue().toString()), 2)) / (4);
        double actualTopReinforcementCrossSection = numberOfTopBars * (Math.PI * Math.pow(Integer.parseInt(topMainBarsComboBox.getValue().toString()), 2)) / (4);

        //TEST
        System.out.println("Number of Bottom Bars: " + numberOfBottomBars);
        System.out.println("Number of Top Bars: " + numberOfTopBars);

        bottomCalculatedReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(bottomReinforcementCrossSection));
        bottomBarsNumberTextField.setText(Integer.valueOf(numberOfBottomBars).toString());
        bottomActualReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(actualBottomReinforcementCrossSection));
        topCalculatedReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(topReinforcementCrossSection));
        topBarsNumberTextField.setText(Integer.valueOf(numberOfTopBars).toString());
        topActualReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(actualTopReinforcementCrossSection));
    }
}
