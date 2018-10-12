package com.concrete_calculator.geometry;

import com.concrete_calculator.materials.Concrete;
import com.concrete_calculator.materials.Steel;

public abstract class Section {

    abstract double calculateArea();

    abstract double calculateMomentOfInertiaX();

    abstract double calculateMomentOfInertiaY();

    abstract double getHeight();

    abstract double getWidth();

    abstract double getEffectiveDepth();

    abstract Concrete getConcreteType();

    abstract Steel getSteelType();

}
