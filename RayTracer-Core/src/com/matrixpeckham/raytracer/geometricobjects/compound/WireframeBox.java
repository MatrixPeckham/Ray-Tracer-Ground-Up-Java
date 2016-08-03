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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 * Wire frame box, made of Cylinders and spheres. does not play nice with
 * transparent materials.
 *
 * as with most compound subclasses nothing is overridden, constructor adds
 * child objects and does nothing else.
 *
 * @author William Matrix Peckham
 */
public class WireframeBox extends Compound {

    /**
     * low corner
     */
    private final Point3D p0 = new Point3D();

    /**
     * high corner
     */
    private final Point3D p1 = new Point3D();

    /**
     * radius of wire frame
     */
    private double rad = 1;

    /**
     * Constructor generates child stuff.
     *
     * @param p0
     * @param p1
     * @param radius
     */
    public WireframeBox(Point3D p0, Point3D p1, double radius) {

        //save parameters
        this.p0.setTo(p0);
        this.p1.setTo(p1);
        this.rad = radius;

        //one sphere we'll instance 8 times
        Sphere corner = new Sphere(new Point3D(0), rad);

        //one for each corner of the box we translate so that the outside
        //of the wireframe is the expected size of the box
        Instance bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p0.x + rad, p0.y + rad, p0.z + rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p0.x + rad, p0.y + rad, p1.z - rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p0.x + rad, p1.y - rad, p0.z + rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p0.x + rad, p1.y - rad, p1.z - rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p1.x - rad, p0.y + rad, p0.z + rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p1.x - rad, p0.y + rad, p1.z - rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p1.x - rad, p1.y - rad, p0.z + rad);
        addObject(bfl);
        bfl = new Instance(corner);
        bfl.setTransformTexture(false);
        bfl.translate(p1.x - rad, p1.y - rad, p1.z - rad);
        addObject(bfl);

        //extents
        double xl = p1.x - p0.x;
        double yl = p1.y - p0.y;
        double zl = p1.z - p0.z;

        //centered cylinder for x extents, we'll instance this 4 times
        OpenCylinder xbar = new OpenCylinder(-(xl / 2 - rad), (xl / 2 - rad),
                rad);
        Instance inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.translate(0, p0.y + rad, p0.z + rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.translate(0, p1.y - rad, p0.z + rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.translate(0, p0.y + rad, p1.z - rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.translate(0, p1.y - rad, p1.z - rad);
        addObject(inst);

        //centered cylinder for y extents, this will be instanced 4 times
        OpenCylinder ybar = new OpenCylinder(-(yl / 2 - rad), (yl / 2 - rad),
                rad);
        inst = new Instance(ybar);
        inst.setTransformTexture(false);
        inst.translate(p0.x + rad, 0, p0.z + rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.translate(p1.x - rad, 0, p0.z + rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.translate(p0.x + rad, 0, p1.z - rad);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.translate(p1.x - rad, 0, p1.z - rad);
        addObject(inst);

        //centered bar for the z extent, instanced 4 times
        OpenCylinder zbar = new OpenCylinder(-(zl / 2 - rad), (zl / 2 - rad),
                rad);
        inst = new Instance(zbar);
        inst.setTransformTexture(false);
        inst.rotateX(90);
        inst.translate(p0.x + rad, p0.y + rad, 0);
        addObject(inst);
        inst = new Instance(zbar);
        inst.setTransformTexture(false);
        inst.rotateX(90);
        inst.translate(p0.x + rad, p1.y - rad, 0);
        addObject(inst);
        inst = new Instance(zbar);
        inst.setTransformTexture(false);
        inst.rotateX(90);
        inst.translate(p1.x - rad, p0.y + rad, 0);
        addObject(inst);
        inst = new Instance(zbar);
        inst.setTransformTexture(false);
        inst.rotateX(90);
        inst.translate(p1.x - rad, p1.y - rad, 0);
        addObject(inst);
    }

}
