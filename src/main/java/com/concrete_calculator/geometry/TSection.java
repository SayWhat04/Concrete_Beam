package com.concrete_calculator.geometry;

public class TSection implements Section {

    private double height;
    private double width;
    private double flangeHeight;
    private double flangeWidth;
    private double webHeight = height - flangeHeight;

    public TSection(double height, double width, double flangeHeight) {
        this.height = height;
        this.width = width;
        this.flangeHeight = flangeHeight;
    }

    public double calculateArea() {
        return width * webHeight + flangeWidth * flangeHeight;
    }

    //TODO: Implement methods for calculation of moments of Inertia
    public double calculateMomentOfInertiaX() {
        return 0;
    }
    public double calculateMomentOfInertiaY() {
        return 0;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getFlangeHeight() {
        return flangeHeight;
    }

    public double getFlangeWidth() {
        return flangeWidth;
    }

}
