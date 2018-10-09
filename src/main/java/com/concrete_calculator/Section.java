package com.concrete_calculator;

public interface Section {

    double calculateArea();

    double calculateMomentOfInertiaX();

    double calculateMomentOfInertiaY();

    double getHeight();

    double getWidth();

    double getEffectiveDepth();

    Concrete getConcreteType();

    Steel getSteelType();

}
