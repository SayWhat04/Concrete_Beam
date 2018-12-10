package com.concrete_calculator.controllers;

import com.concrete_calculator.Constants;
import com.concrete_calculator.RegExps;
import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.RectangularSection;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;
import com.concrete_calculator.models.CalculationModel;
import com.concrete_calculator.models.ForcesSet;
import com.concrete_calculator.reinforcement.Rebar;
import com.concrete_calculator.reinforcement.ReinforcementProperties;
import com.concrete_calculator.solvers.Solver;
import com.concrete_calculator.utils.CalculationUtils;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RectangularSectionShearController {

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private TextField thetaAngleTextField;

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
    private TextField bottomBarsQuantityTextField;

    @FXML
    private TextField stirrupArmsTextField;

    @FXML
    private TextField topBarsQuantityTextField;

    @FXML
    private ComboBox<Concrete> concreteClassComboBox;

    @FXML
    private ComboBox<Steel> steelClassComboBox;

    @FXML
    private TextField axialForceTextField;

    @FXML
    private TextField shearForceTextField;

    @FXML
    private TextField bottomCalculatedReinforcementTextField;

    @FXML
    private TextField topCalculatedReinforcementTextField;

    @FXML
    private TextField stirrupsTextField;

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
                .or(Bindings.isEmpty(thetaAngleTextField.textProperty()))
                .or(Bindings.isNull(bottomMainBarsComboBox.valueProperty()))
                .or(Bindings.isEmpty(bottomBarsQuantityTextField.textProperty()))
                .or(Bindings.isNull(topMainBarsComboBox.valueProperty()))
                .or(Bindings.isEmpty(topBarsQuantityTextField.textProperty()))
                .or(Bindings.isNull(stirrupsComboBox.valueProperty()))
                .or(Bindings.isEmpty(stirrupArmsTextField.textProperty()))
                .or(Bindings.isEmpty(bottomCoverageTextField.textProperty()))
                .or(Bindings.isEmpty(topCoverageTextField.textProperty()))
                .or(Bindings.isNull(concreteClassComboBox.valueProperty()))
                .or(Bindings.isNull(steelClassComboBox.valueProperty()))
                .or(Bindings.isEmpty(axialForceTextField.textProperty()))
                .or(Bindings.isEmpty(shearForceTextField.textProperty()))
                .or(thetaAngleTextField.textProperty().greaterThan(Constants.THETA_ANGLE_MAX))
                .or(thetaAngleTextField.textProperty().lessThan(Constants.THETA_ANGLE_MIN))
                .or(thetaAngleTextField.textProperty().length().lessThan(2))
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
        thetaAngleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_DOUBLE_REGEX)) {
                thetaAngleTextField.setText(oldValue);
            }
        });
        bottomBarsQuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_TWO_DIGITS_INTEGER)) {
                bottomBarsQuantityTextField.setText(oldValue);
            }
        });
        topBarsQuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_TWO_DIGITS_INTEGER)) {
                topBarsQuantityTextField.setText(oldValue);
            }
        });
        stirrupArmsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_TWO_DIGITS_INTEGER)) {
                stirrupArmsTextField.setText(oldValue);
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
        axialForceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_DOUBLE_REGEX)) {
                axialForceTextField.setText(oldValue);
            }
        });
        shearForceTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(RegExps.POSITIVE_DOUBLE_REGEX)) {
                shearForceTextField.setText(oldValue);
            }
        });
    }

    public void calculateShear() {
        Section rectangularSection = new RectangularSection(Double.parseDouble(heightTextField.getText()), Double.parseDouble(widthTextField.getText()));
        Geometry calculatedGeometry = new Geometry(rectangularSection, Double.parseDouble(lengthTextField.getText()));

        ForcesSet shearForcesSet = new ForcesSet();
        shearForcesSet.setShearForce(Double.parseDouble(shearForceTextField.getText()));
        shearForcesSet.setAxialForce(Double.parseDouble(axialForceTextField.getText()));

        int numberOfBottomBars = Integer.parseInt(bottomBarsQuantityTextField.getText());
        int numberOfTopBars = Integer.parseInt(topBarsQuantityTextField.getText());
        int numberOfStirrupArms = Integer.parseInt(stirrupArmsTextField.getText());

        Rebar bottomMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(bottomMainBarsComboBox.getValue().toString()));
        Rebar topMainBars = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(topMainBarsComboBox.getValue().toString()));
        Rebar stirrups = new Rebar(Steel.valueOf(steelClassComboBox.getValue().toString()), Integer.parseInt(stirrupsComboBox.getValue().toString()));
        double bottomReinforcementCoverage = Double.parseDouble(bottomCoverageTextField.getText());
        double topReinforcementCoverage = Double.parseDouble(topCoverageTextField.getText());
        ReinforcementProperties reinforcementProperties = new ReinforcementProperties(bottomMainBars, topMainBars, stirrups, bottomReinforcementCoverage, topReinforcementCoverage);

        reinforcementProperties.setThetaAngleShear(Double.parseDouble(thetaAngleTextField.getText()));
        double bottomReinforcementCrossSection = CalculationUtils.calculateCrossSectionAreaOfBars(Integer.parseInt(bottomMainBarsComboBox.getValue().toString()), numberOfBottomBars);
        double topReinforcementCrossSection = CalculationUtils.calculateCrossSectionAreaOfBars(Integer.parseInt(topMainBarsComboBox.getValue().toString()), numberOfTopBars);
        double stirrupCrossSection = CalculationUtils.calculateCrossSectionAreaOfBars(Integer.parseInt(stirrupsComboBox.getValue().toString()), numberOfStirrupArms);

        reinforcementProperties.setBottomReinforcementCrossSection(bottomReinforcementCrossSection);
        reinforcementProperties.setTopReinforcementCrossSection(topReinforcementCrossSection);
        reinforcementProperties.setStirrupsCrossSection(stirrupCrossSection);

        String selectedConcreteClass = concreteClassComboBox.getValue().toString();
        CalculationModel shearCalculationModel = new CalculationModel(calculatedGeometry, shearForcesSet, reinforcementProperties, Concrete.valueOf(selectedConcreteClass));
        Solver shearSolver = new Solver();

        double shearReinforcementSpan = shearSolver.calculateReinforcementShearRectangularSection(shearCalculationModel);

        //TEST
        System.out.println("********************* " + new Object() {
        }.getClass().getEnclosingMethod().getName() + " ************************************");
        System.out.println("Shear reinforcement span: " + shearReinforcementSpan);
        System.out.println("Number of Bottom Bars: " + numberOfBottomBars);
        System.out.println("Number of Top Bars: " + numberOfTopBars);

        bottomCalculatedReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(bottomReinforcementCrossSection));
        topCalculatedReinforcementTextField.setText(Constants.twoDigitsAfterDecimal.format(topReinforcementCrossSection));
        stirrupsTextField.setText(Constants.twoDigitsAfterDecimal.format(shearReinforcementSpan));
    }
}
