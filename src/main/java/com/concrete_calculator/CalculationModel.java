package com.concrete_calculator;

import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.geometry.Section;
import com.concrete_calculator.materials.Concrete;

public class CalculationModel {

    Geometry elementGeometry;
    ForcesSet forcesSet;
    ReinforcementProperties reinforcementProperties;

    public CalculationModel(Geometry elementGeometry, ForcesSet forcesSet, ReinforcementProperties reinforcementProperties) {
        this.elementGeometry = elementGeometry;
        this.forcesSet = forcesSet;
        this.reinforcementProperties = reinforcementProperties;
    }

    public Geometry getElementGeometry() {
        return elementGeometry;
    }

    public ForcesSet getForcesSet() {
        return forcesSet;
    }

    public ReinforcementProperties getReinforcementProperties() {
        return reinforcementProperties;
    }
}
