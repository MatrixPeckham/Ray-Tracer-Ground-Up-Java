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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Jittered directional light for getting soft shadows. For reproducible results
 * call Utility.setSeed() if you use this class.
 *
 * @author William Matrix Peckham
 */
public class JitteredDirectional extends Directional {

    /**
     * jitter amount
     */
    double r = 1;

    /**
     * default constructor
     */
    public JitteredDirectional() {
        super();
        dir.normalize();
    }

    /**
     * copy constructor
     *
     * @param dl
     */
    public JitteredDirectional(JitteredDirectional dl) {
        super(dl);
        dir.normalize();
        this.r = dl.r;
    }

    /**
     * setter, makes sure normalization occurs
     *
     * @param d
     */
    @Override
    public void setDirection(Vector3D d) {
        super.setDirection(d);
        dir.normalize();
    }

    /**
     * set direction
     *
     * @param dx
     * @param dy
     * @param dz
     */
    @Override
    public void setDirection(double dx, double dy, double dz) {
        super.setDirection(dx, dy, dz);
        dir.normalize();
    }

    /**
     * get direction randomly jitters the direction, to be reproduced we need to
     * set the seed of the Utility class if we use this class
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        Vector3D ndir = new Vector3D();
        ndir.x = dir.x + r * (2 * Utility.randDouble() - 1);
        ndir.y = dir.y + r * (2 * Utility.randDouble() - 1);
        ndir.z = dir.z + r * (2 * Utility.randDouble() - 1);
        ndir.normalize();
        return ndir;
    }

    /**
     * sets the jitter amount.
     *
     * @param d
     */
    public void setJitterAmount(double d) {
        r = d;
    }

    private static final Logger LOG
            = Logger.getLogger(JitteredDirectional.class.getName());

}
