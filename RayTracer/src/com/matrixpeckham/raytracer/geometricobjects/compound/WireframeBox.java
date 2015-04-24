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
import com.matrixpeckham.raytracer.geometricobjects.primatives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primatives.Sphere;
import com.matrixpeckham.raytracer.util.Point3D;

/**
 *
 * @author William Matrix Peckham
 */
public class WireframeBox extends Compound {

    private Point3D p0 = new Point3D();
    private Point3D p1 = new Point3D();
    private double rad = 1;

    public WireframeBox(Point3D p0, Point3D p1, double radius) {
        this.p0.setTo(p0);
        this.p1.setTo(p1);
        this.rad = radius;

        Sphere corner = new Sphere(new Point3D(0), rad);

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

        double xl = p1.x - p0.x;
        double yl = p1.y - p0.y;
        double zl = p1.z - p0.z;
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

        OpenCylinder zbar = new OpenCylinder(-(zl / 2 - rad), (zl / 2 - rad),
                rad);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.rotateY(90);
        inst.translate(p0.x + rad, p0.y + rad, 0);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.rotateY(90);
        inst.translate(p0.x + rad, p1.y - rad, 0);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.rotateY(90);
        inst.translate(p1.x - rad, p0.y + rad, 0);
        addObject(inst);
        inst = new Instance(xbar);
        inst.setTransformTexture(false);
        inst.rotateZ(90);
        inst.rotateY(90);
        inst.translate(p1.x - rad, p1.y - rad, 0);
        addObject(inst);
    }

}
