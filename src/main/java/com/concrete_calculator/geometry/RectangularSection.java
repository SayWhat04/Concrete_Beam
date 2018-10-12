package com.concrete_calculator.geometry;

import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;

public class RectangularSection extends Section {
    private double height;
    private double width;

    private double effectiveDepth; // TODO: Add calculation of this.

    private Concrete concreteType;
    private Steel steelType;

    public RectangularSection(double height, double width, Concrete concreteType, Steel steelType) {
        this.height = height;
        this.width = width;
        this.concreteType = concreteType;
        this.steelType = steelType;
    }

    public double calculateArea() {
        return width * height;
    }

    public double calculateMomentOfInertiaX() {
        return (width * Math.pow(height, 3)) / (12);
    }

    public double calculateMomentOfInertiaY() {
        return (height * Math.pow(width, 3)) / (12);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getEffectiveDepth() {
        return effectiveDepth;
    }


    public Concrete getConcreteType() {
        return concreteType;
    }


    public Steel getSteelType() {
        return steelType;
    }


    public void setEffectiveDepth(double effectiveDepth) {
        this.effectiveDepth = effectiveDepth;
    }
}
