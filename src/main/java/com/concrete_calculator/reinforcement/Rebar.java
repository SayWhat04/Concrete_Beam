package com.concrete_calculator.reinforcement;

import com.concrete_calculator.materials.Steel;

public class Rebar {

    private Steel steelType;
    private int barDiameter;

    public Rebar(Steel steelType, int barDiameter) {
        this.steelType = steelType;
        this.barDiameter = barDiameter;
    }

    public Steel getSteelType() {
        return steelType;
    }

    public int getBarDiameter() {
        return barDiameter;
    }
}
