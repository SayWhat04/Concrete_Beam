package com.concrete_calculator.geometry;

public abstract class Section {

    public abstract double calculateArea();

    public abstract double calculateMomentOfInertiaX();

    public abstract double calculateMomentOfInertiaY();

    public abstract double getHeight();

    public abstract double getWidth();

    public abstract double getFlangeHeight();

    public abstract double getFlangeWidth();
}
