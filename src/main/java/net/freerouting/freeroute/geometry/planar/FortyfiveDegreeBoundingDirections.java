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
 */
package net.freerouting.freeroute.geometry.planar;

/**
 *
 * Implements the abstract class ShapeBoundingDirections as the 8 directions,
 * which are multiples of 45 degree. The class is a singleton with the only
 * instanciation INSTANCE.
 *
 * @author Alfons Wirtz
 */
public class FortyfiveDegreeBoundingDirections implements ShapeBoundingDirections {

    /**
     * the one and only instantiation
     */
    public static final FortyfiveDegreeBoundingDirections INSTANCE = new FortyfiveDegreeBoundingDirections();

    /**
     * prevent instantiation
     */
    private FortyfiveDegreeBoundingDirections() {
    }

    @Override
    public int count() {
        return 8;
    }

    @Override
    public RegularTileShape bounds(ConvexShape p_shape) {
        return p_shape.bounding_shape(this);
    }

    @Override
    public RegularTileShape bounds(IntBox p_box) {
        return p_box.to_IntOctagon();
    }

    @Override
    public RegularTileShape bounds(IntOctagon p_oct) {
        return p_oct;
    }

    @Override
    public RegularTileShape bounds(Simplex p_simplex) {
        return p_simplex.bounding_octagon();
    }

    @Override
    public RegularTileShape bounds(Circle p_circle) {
        return p_circle.bounding_octagon();
    }

    @Override
    public RegularTileShape bounds(PolygonShape p_polygon) {
        return p_polygon.bounding_octagon();
    }

}
