package com.concrete_calculator.models;

import com.concrete_calculator.geometry.Geometry;
import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.reinforcement.ReinforcementProperties;

public class CalculationModel {
    private Geometry elementGeometry;
    private ForcesSet forcesSet;
    private ReinforcementProperties reinforcementProperties;
    private Concrete concreteType;

    public CalculationModel(Geometry elementGeometry, ForcesSet forcesSet, ReinforcementProperties reinforcementProperties, Concrete concreteType) {
        this.elementGeometry = elementGeometry;
        this.forcesSet = forcesSet;
        this.reinforcementProperties = reinforcementProperties;
        this.concreteType = concreteType;
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

    public Concrete getConcreteType() {
        return concreteType;
    }
}
