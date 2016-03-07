/*
 * Copyright (C) 2015 William Matrix Peckham
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.matrixpeckham.raytracer.tracers;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.World;
import java.util.logging.Logger;

/**
 * Simplest tracer, hit tests the single sphere and returns red if it hits.
 *
 * @author William Matrix Peckham
 */
public class SingleSphere extends Tracer {

    /**
     * default constructor
     */
    public SingleSphere() {
    }

    /**
     * World setting constructor
     *
     * @param w
     */
    public SingleSphere(World w) {
        super(w);
    }

    /**
     * Simplest possible implementation of traceRay function. returns one color
     * if ray hits the only object and the other color if it doesn't.
     *
     * @param ray
     * @return
     */
    @Override
    public RGBColor traceRay(Ray ray) {
        ShadeRec sr = new ShadeRec(world);
        if (world.sphere.hit(ray, sr)) {
            return Utility.RED;
        }
        return Utility.BLACK;
    }

    /**
     * Simplest possible implementation of traceRay function. returns one color
     * if ray hits the only object and the other color if it doesn't.
     *
     * @param ray
     * @return
     */
    @Override
    public RGBColor traceRay(Ray ray, int depth) {
        ShadeRec sr = new ShadeRec(world);
        if (world.sphere.hit(ray, sr)) {
            return Utility.RED;
        }
        return Utility.BLACK;
    }

    private static final Logger LOG
            = Logger.getLogger(SingleSphere.class.getName());

}
