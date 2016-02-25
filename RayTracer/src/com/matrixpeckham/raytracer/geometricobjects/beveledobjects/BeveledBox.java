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
package com.matrixpeckham.raytracer.geometricobjects.beveledobjects;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.Compound;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Beveled box that uses full cylinders and spheres, doesn't play well with
 * transparency, but we have beveled box shell that does, so we won't bother
 * changing this to do that
 *
 * @author William Matrix Peckham
 */
public class BeveledBox extends Compound {

    /**
     * low point
     */
    private Point3D p0 = new Point3D(-1);

    /**
     * high point
     */
    private Point3D p1 = new Point3D(1);

    /**
     * bevel radius
     */
    private double rb = 0.1;

    /**
     * cached box
     */
    private BBox bBox = new BBox(p0, p1);

    /**
     * default constructor
     */
    public BeveledBox() {
        super();
        buildCompound();
    }

    /**
     * initializing constructor
     *
     * @param minCorner
     * @param maxCorner
     * @param bevelRadius
     */
    public BeveledBox(Point3D minCorner, Point3D maxCorner, double bevelRadius) {
        super();
        p0.setTo(minCorner);
        p1.setTo(maxCorner);
        rb = bevelRadius;
        bBox = new BBox(p0, p1);
        buildCompound();
    }

    /**
     * copy constructor
     *
     * @param bb
     */
    public BeveledBox(BeveledBox bb) {
        super(bb);
        p0.setTo(bb.p0);
        p1.setTo(bb.p1);
        rb = bb.rb;
        bBox = new BBox(bb.bBox);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public GeometricObject clone() {
        return new BeveledBox(this);
    }

    /**
     * bounding box
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return bBox;
    }

    /**
     * shadow hit, overridden to use cached box
     *
     * @param ray
     * @param t
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        if (!shadows) {
            return false;
        }
        if (bBox.hit(ray)) {
            return super.shadowHit(ray, t);
        } else {
            return false;
        }
    }

    /**
     * override hit function to use cached box
     *
     * @param ray
     * @param s
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        if (bBox.hit(ray)) {
            return super.hit(ray, s);
        } else {
            return false;
        }
    }

    /**
     * builds the components
     */
    private void buildCompound() {
        Instance top_front_edge = new Instance(new OpenCylinder(-(p1.x - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb));	// top front edge
        top_front_edge.rotateZ(90);
        top_front_edge.translate((p0.x + p1.x) / 2, p1.y - rb, p1.z - rb);
        top_front_edge.setTransformTexture(false);
        objects.add(top_front_edge);

        // top back (-ve z)
        Instance top_back_edge = new Instance(new OpenCylinder(-(p1.x - p0.x - 2
                * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb));	// top back edge
        top_back_edge.rotateZ(90);
        top_back_edge.translate((p0.x + p1.x) / 2, p1.y - rb, p0.z + rb);
        top_back_edge.setTransformTexture(false);
        objects.add(top_back_edge);

        // top right (+ve x)
        Instance top_right_edge = new Instance(new OpenCylinder(-(p1.z - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb)); // top right edge
        top_right_edge.rotateX(90);
        top_right_edge.translate(p1.x - rb, p1.y - rb, (p0.z + p1.z) / 2);
        top_right_edge.setTransformTexture(false);
        objects.add(top_right_edge);

        // top left (-ve x)
        Instance top_left_edge = new Instance(new OpenCylinder(-(p1.z - p0.z - 2
                * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb)); // top left edge
        top_left_edge.rotateX(90);
        top_left_edge.translate(p0.x + rb, p1.y - rb, (p0.z + p1.z) / 2);
        top_left_edge.setTransformTexture(false);
        objects.add(top_left_edge);

	// bottom edges  (-ve y)
        // bottom front  (+ve z)
        Instance bottom_front_edge = new Instance(new OpenCylinder(-(p1.x - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb));	// bottom fromt edge
        bottom_front_edge.rotateZ(90);
        bottom_front_edge.translate((p0.x + p1.x) / 2, p0.y + rb, p1.z - rb);
        bottom_front_edge.setTransformTexture(false);
        objects.add(bottom_front_edge);

        // bottom back  (-ve z)
        Instance bottom_back_edge = new Instance(new OpenCylinder(-(p1.x - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb));	// bottom back edge
        bottom_back_edge.rotateZ(90);
        bottom_back_edge.translate((p0.x + p1.x) / 2, p0.y + rb, p0.z + rb);
        bottom_back_edge.setTransformTexture(false);
        objects.add(bottom_back_edge);

        // bottom right (-ve x, -ve y)
        Instance bottom_right_edge = new Instance(new OpenCylinder(-(p1.z - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb)); // bottom right edge
        bottom_right_edge.rotateX(90);
        bottom_right_edge.translate(p1.x - rb, p0.y + rb, (p0.z + p1.z) / 2);
        bottom_right_edge.setTransformTexture(false);
        objects.add(bottom_right_edge);

        // bottom left (-ve x, -ve y)
        Instance bottom_left_edge = new Instance(new OpenCylinder(-(p1.z - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb)); // bottom left edge
        bottom_left_edge.rotateX(90);
        bottom_left_edge.translate(p0.x + rb, p0.y + rb, (p0.z + p1.z) / 2);
        bottom_left_edge.setTransformTexture(false);
        objects.add(bottom_left_edge);

	// vertical edges
        // vertical right front  (+ve x, +ve z)
        Instance vertical_right_front_edge = new Instance(new OpenCylinder(p0.y
                + rb, p1.y - rb, rb));
        vertical_right_front_edge.translate(p1.x - rb, 0, p1.z - rb);
        vertical_right_front_edge.setTransformTexture(false);
        objects.add(vertical_right_front_edge);

        // vertical left front  (-ve x, +ve z)
        Instance vertical_left_front_edge = new Instance(new OpenCylinder(p0.y
                + rb, p1.y - rb, rb));
        vertical_left_front_edge.translate(p0.x + rb, 0, p1.z - rb);
        vertical_left_front_edge.setTransformTexture(false);
        objects.add(vertical_left_front_edge);

        // vertical left back  (-ve x, -ve z)
        Instance vertical_left_back_edge = new Instance(new OpenCylinder(p0.y
                + rb, p1.y - rb, rb));
        vertical_left_back_edge.translate(p0.x + rb, 0, p0.z + rb);
        vertical_left_back_edge.setTransformTexture(false);
        objects.add(vertical_left_back_edge);

        // vertical right back  (+ve x, -ve z)
        Instance vertical_right_back_edge = new Instance(new OpenCylinder(p0.y
                + rb, p1.y - rb, rb));
        vertical_right_back_edge.translate(p1.x - rb, 0, p0.z + rb);
        vertical_right_back_edge.setTransformTexture(false);
        objects.add(vertical_right_back_edge);

	// corner spheres
        // top right front
        Sphere top_right_front_corner = new Sphere(new Point3D(p1.x - rb, p1.y
                - rb, p1.z - rb), rb);
        objects.add(top_right_front_corner);

        // top left front  (-ve x)
        Sphere top_left_front_corner = new Sphere(new Point3D(p0.x + rb, p1.y
                - rb, p1.z - rb), rb);
        objects.add(top_left_front_corner);

        // top left back
        Sphere top_left_back_corner = new Sphere(new Point3D(p0.x + rb, p1.y
                - rb, p0.z + rb), rb);
        objects.add(top_left_back_corner);

        // top right back
        Sphere top_right_back_corner = new Sphere(new Point3D(p1.x - rb, p1.y
                - rb, p0.z + rb), rb);
        objects.add(top_right_back_corner);

        // bottom right front
        Sphere bottom_right_front_corner = new Sphere(new Point3D(p1.x - rb,
                p0.y + rb, p1.z - rb), rb);
        objects.add(bottom_right_front_corner);

        // bottom left front
        Sphere bottom_left_front_corner = new Sphere(new Point3D(p0.x + rb, p0.y
                + rb, p1.z - rb), rb);
        objects.add(bottom_left_front_corner);

        // bottom left back
        Sphere bottom_left_back_corner = new Sphere(new Point3D(p0.x + rb, p0.y
                + rb, p0.z + rb), rb);
        objects.add(bottom_left_back_corner);

        // bottom right back
        Sphere bottom_right_back_corner = new Sphere(new Point3D(p1.x - rb, p0.y
                + rb, p0.z + rb), rb);
        objects.add(bottom_right_back_corner);

        // the faces
        // bottom face: -ve y
        Rectangle bottom_face = new Rectangle(new Point3D(p0.x + rb, p0.y, p0.z
                + rb),
                new Vector3D(0, 0, (p1.z - rb) - (p0.z + rb)),
                new Vector3D((p1.x - rb) - (p0.x + rb), 0, 0),
                new Normal(0, -1, 0));
        objects.add(bottom_face);

        // bottom face: +ve y
        Rectangle top_face = new Rectangle(new Point3D(p0.x + rb, p1.y, p0.z
                + rb),
                new Vector3D(0, 0, (p1.z - rb) - (p0.z + rb)),
                new Vector3D((p1.x - rb) - (p0.x + rb), 0, 0),
                new Normal(0, 1, 0));
        objects.add(top_face);

        // back face: -ve z
        Rectangle back_face = new Rectangle(new Point3D(p0.x + rb, p0.y + rb,
                p0.z),
                new Vector3D((p1.x - rb) - (p0.x + rb), 0, 0),
                new Vector3D(0, (p1.y - rb) - (p0.y + rb), 0),
                new Normal(0, 0, -1));
        objects.add(back_face);

        // front face: +ve z
        Rectangle front_face = new Rectangle(new Point3D(p0.x + rb, p0.y + rb,
                p1.z),
                new Vector3D((p1.x - rb) - (p0.x + rb), 0, 0),
                new Vector3D(0, (p1.y - rb) - (p0.y + rb), 0),
                new Normal(0, 0, 1));
        objects.add(front_face);

        // left face: -ve x
        Rectangle left_face = new Rectangle(new Point3D(p0.x, p0.y + rb, p0.z
                + rb),
                new Vector3D(0, 0, (p1.z - rb) - (p0.z + rb)),
                new Vector3D(0, (p1.y - rb) - (p0.y + rb), 0),
                new Normal(-1, 0, 0));
        objects.add(left_face);

        // right face: +ve x
        Rectangle right_face = new Rectangle(new Point3D(p1.x, p0.y + rb, p0.z
                + rb),
                new Vector3D(0, 0, (p1.z - rb) - (p0.z + rb)),
                new Vector3D(0, (p1.y - rb) - (p0.y + rb), 0),
                new Normal(1, 0, 0));
        objects.add(right_face);

    }
}
