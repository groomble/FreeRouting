/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * PrintableShape.java
 *
 * Created on 5. Januar 2005, 08:02
 */
package net.freerouting.freeroute.board;

import java.util.Locale;
import net.freerouting.freeroute.MainApp;
import net.freerouting.freeroute.geometry.planar.FloatPoint;

/**
 * Shape class used for printing a geometry.planar.Shape after transforming it
 * to user coordinates.
 *
 * @author Alfons Wirtz
 */
public abstract class PrintableShape {

    protected PrintableShape() {
    }

    /**
     * Returns text information about the PrintableShape.
     */
    @Override
    public abstract String toString();

    static class Circle extends PrintableShape {

        public final FloatPoint center;
        public final double radius;

        /**
         * Creates a Circle from the input coordinates.
         */
        public Circle(FloatPoint p_center, double p_radius) {
            center = p_center;
            radius = p_radius;
        }

        @Override
        public String toString() {
            Locale locale = MainApp.get_locale();
            java.util.ResourceBundle resources
                    = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", locale);
            StringBuilder sb = new StringBuilder();
            sb.append(resources.getString("circle")).append(": ");
            if (center.x != 0 || center.y != 0) {
                sb.append(resources.getString("center")).append(" =").append(center.to_string(locale));
            }
            java.text.NumberFormat nf = java.text.NumberFormat.getInstance(locale);
            nf.setMaximumFractionDigits(4);
            String radius_string = resources.getString("radius") + " = " + nf.format((float) radius);
            sb.append(radius_string);
            return sb.toString();
        }
    }

    static class Rectangle extends PrintableShape {

        public final FloatPoint lower_left;
        public final FloatPoint upper_right;

        public Rectangle(FloatPoint p_lower_left, FloatPoint p_upper_right) {
            lower_left = p_lower_left;
            upper_right = p_upper_right;
        }

        @Override
        public String toString() {
            Locale locale = MainApp.get_locale();
            java.util.ResourceBundle resources
                    = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", locale);
            StringBuilder sb = new StringBuilder();
            sb.append(resources.getString("rectangle")).append(": ").append(resources.getString("lower_left")).append(" = ").append(lower_left.to_string(locale)).append(", ").append(resources.getString("upper_right")).append(" = ").append(upper_right.to_string(locale));
            return sb.toString();
        }
    }

    static class Polygon extends PrintableShape {

        public final FloatPoint[] corner_arr;

        public Polygon(FloatPoint[] p_corners) {
            corner_arr = p_corners;
        }

        @Override
        public String toString() {
            Locale locale = MainApp.get_locale();
            java.util.ResourceBundle resources
                    = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.board.resources.ObjectInfoPanel", locale);
            StringBuilder sb = new StringBuilder();
            sb.append(resources.getString("polygon")).append(": ");
            for (int i = 0; i < corner_arr.length; ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(corner_arr[i].to_string(locale));
            }
            return sb.toString();
        }

    }
}
