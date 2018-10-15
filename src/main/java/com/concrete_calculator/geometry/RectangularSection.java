package com.concrete_calculator.geometry;

public class RectangularSection implements Section {
    private double height;
    private double width;

    public RectangularSection(double height, double width) {
        this.height = height;
        this.width = width;
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
}
