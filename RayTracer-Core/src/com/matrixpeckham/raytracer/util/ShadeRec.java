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
package com.matrixpeckham.raytracer.util;

import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.world.World;
import java.util.logging.Logger;

/**
 *
 * @author William Matrix Peckham
 */
public class ShadeRec {

    /**
     * did we hit something
     */
    public boolean hitAnObject = false;

    /**
     * world point hit
     */
    public final Point3D hitPoint = new Point3D();

    /**
     * local hit point
     */
    public final Point3D localHitPosition = new Point3D();

    /**
     * normal at hit point
     */
    public final Normal normal = new Normal();

    /**
     * color, used only in skeleton tracer
     */
    public final RGBColor color = new RGBColor(Utility.BLACK);

    /**
     * world reference
     */
    public final World w;

    /**
     * last hit point
     */
    public double lastT = Double.POSITIVE_INFINITY;

    /**
     * texture coordinate u
     */
    public double u = 0;

    /**
     * texture coordinate v
     */
    public double v = 0;

    /**
     * depth
     */
    public int depth = 0;

    /**
     * ray
     */
    public final Ray ray = new Ray();

    /**
     * material at hit point
     */
    public Material material = null;

    /**
     * Usual constructor with world.
     *
     * @param w
     */
    public ShadeRec(World w) {
        this.w = w;
    }

    /**
     * Copy constructor.
     *
     * @param r
     */
    public ShadeRec(ShadeRec r) {
        hitAnObject = r.hitAnObject;
        localHitPosition.setTo(r.localHitPosition);
        hitPoint.setTo(r.hitPoint);
        color.setTo(r.color);
        w = r.w;
        ray.setTo(r.ray);
        //t=r.t;
        lastT = r.lastT;
        depth = r.depth;
        if (r.material != null) {
            material = r.material;
        }
        normal.setTo(r.normal);
        this.u = r.u;
        this.v = r.v;
    }

    private static final Logger LOG = Logger.getLogger(ShadeRec.class.getName());

}
