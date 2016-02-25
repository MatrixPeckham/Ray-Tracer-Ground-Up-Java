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
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartSphere;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Beveled box that works with transparency.
 *
 * @author William Matrix Peckham
 */
public class BeveledBoxShell extends Compound {

    /**
     * low point of box
     */
    private Point3D p0 = new Point3D(-1);
    /**
     * high point of box
     */
    private Point3D p1 = new Point3D(1);
    /**
     * bevel radius
     */
    private double rb = 0.1;
    //bounding box
    private BBox bBox = new BBox(p0, p1);

    /**
     * default constructor
     */
    public BeveledBoxShell() {
        super();
        buildCompound();
    }

    /**
     * initializer constructor
     *
     * @param minCorner
     * @param maxCorner
     * @param bevelRadius
     */
    public BeveledBoxShell(Point3D minCorner, Point3D maxCorner,
            double bevelRadius) {
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
    public BeveledBoxShell(BeveledBoxShell bb) {
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
        return new BeveledBoxShell(this);
    }

    /**
     * returns cached box
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        return bBox;
    }

    /**
     * shadow hit, overridden to use cached bounds
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
     * hit function overridden to use cached bounding box
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
        double startCyl = 0;
        double endCyl = 90;
        Instance top_front_edge = new Instance(new ConvexPartCylinder(-(p1.x
                - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb, startCyl, endCyl));	// top front edge
        top_front_edge.rotateZ(90);
        top_front_edge.translate((p0.x + p1.x) / 2, p1.y - rb, p1.z - rb);
        top_front_edge.setTransformTexture(false);
        objects.add(top_front_edge);

        // top back (-ve z)
        Instance top_back_edge = new Instance(new ConvexPartCylinder(-(p1.x
                - p0.x - 2
                * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb, startCyl + 90, endCyl
                + 90));	// top back edge
        top_back_edge.rotateZ(90);
        top_back_edge.translate((p0.x + p1.x) / 2, p1.y - rb, p0.z + rb);
        top_back_edge.setTransformTexture(false);
        objects.add(top_back_edge);

        // top right (+ve x)
        Instance top_right_edge = new Instance(new ConvexPartCylinder(-(p1.z
                - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb, startCyl + 90,
                endCyl + 90)); // top right edge
        top_right_edge.rotateX(90);
        top_right_edge.translate(p1.x - rb, p1.y - rb, (p0.z + p1.z) / 2);
        top_right_edge.setTransformTexture(false);
        objects.add(top_right_edge);

        // top left (-ve x)
        Instance top_left_edge = new Instance(new ConvexPartCylinder(-(p1.z
                - p0.z - 2
                * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb, startCyl + 180,
                endCyl + 180)); // top left edge
        top_left_edge.rotateX(90);
        top_left_edge.translate(p0.x + rb, p1.y - rb, (p0.z + p1.z) / 2);
        top_left_edge.setTransformTexture(false);
        objects.add(top_left_edge);

        // bottom edges  (-ve y)
        // bottom front  (+ve z)
        Instance bottom_front_edge = new Instance(new ConvexPartCylinder(-(p1.x
                - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb, startCyl + 270,
                endCyl + 270));	// bottom fromt edge
        bottom_front_edge.rotateZ(90);
        bottom_front_edge.translate((p0.x + p1.x) / 2, p0.y + rb, p1.z - rb);
        bottom_front_edge.setTransformTexture(false);
        objects.add(bottom_front_edge);

        // bottom back  (-ve z)
        Instance bottom_back_edge = new Instance(new ConvexPartCylinder(-(p1.x
                - p0.x
                - 2 * rb) / 2, (p1.x - p0.x - 2 * rb) / 2, rb, startCyl + 180,
                endCyl + 180));	// bottom back edge
        bottom_back_edge.rotateZ(90);
        bottom_back_edge.translate((p0.x + p1.x) / 2, p0.y + rb, p0.z + rb);
        bottom_back_edge.setTransformTexture(false);
        objects.add(bottom_back_edge);

        // bottom right (-ve x, -ve y)
        Instance bottom_right_edge = new Instance(new ConvexPartCylinder(-(p1.z
                - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb, startCyl, endCyl)); // bottom right edge
        bottom_right_edge.rotateX(90);
        bottom_right_edge.translate(p1.x - rb, p0.y + rb, (p0.z + p1.z) / 2);
        bottom_right_edge.setTransformTexture(false);
        objects.add(bottom_right_edge);

        // bottom left (-ve x, -ve y)
        Instance bottom_left_edge = new Instance(new ConvexPartCylinder(-(p1.z
                - p0.z
                - 2 * rb) / 2, (p1.z - p0.z - 2 * rb) / 2, rb, startCyl + 270,
                endCyl + 270)); // bottom left edge
        bottom_left_edge.rotateX(90);
        bottom_left_edge.translate(p0.x + rb, p0.y + rb, (p0.z + p1.z) / 2);
        bottom_left_edge.setTransformTexture(false);
        objects.add(bottom_left_edge);

        // vertical edges
        // vertical right front  (+ve x, +ve z)
        Instance vertical_right_front_edge = new Instance(
                new ConvexPartCylinder(p0.y
                        + rb, p1.y - rb, rb, startCyl, endCyl));
        vertical_right_front_edge.translate(p1.x - rb, 0, p1.z - rb);
        vertical_right_front_edge.setTransformTexture(false);
        objects.add(vertical_right_front_edge);

        // vertical left front  (-ve x, +ve z)
        Instance vertical_left_front_edge = new Instance(new ConvexPartCylinder(
                p0.y
                + rb, p1.y - rb, rb, startCyl + 270, endCyl + 270));
        vertical_left_front_edge.translate(p0.x + rb, 0, p1.z - rb);
        vertical_left_front_edge.setTransformTexture(false);
        objects.add(vertical_left_front_edge);

        // vertical left back  (-ve x, -ve z)
        Instance vertical_left_back_edge = new Instance(new ConvexPartCylinder(
                p0.y
                + rb, p1.y - rb, rb, startCyl + 180, endCyl + 180));
        vertical_left_back_edge.translate(p0.x + rb, 0, p0.z + rb);
        vertical_left_back_edge.setTransformTexture(false);
        objects.add(vertical_left_back_edge);

        // vertical right back  (+ve x, -ve z)
        Instance vertical_right_back_edge = new Instance(new ConvexPartCylinder(
                p0.y
                + rb, p1.y - rb, rb, startCyl + 90, endCyl + 90));
        vertical_right_back_edge.translate(p1.x - rb, 0, p0.z + rb);
        vertical_right_back_edge.setTransformTexture(false);
        objects.add(vertical_right_back_edge);

        // corner spheres
        // top right front
        ConvexPartSphere top_right_front_corner = new ConvexPartSphere(
                new Point3D(p1.x - rb, p1.y
                        - rb, p1.z - rb), rb, 0, 90, 0, 90);
        objects.add(top_right_front_corner);

        // top left front  (-ve x)
        ConvexPartSphere top_left_front_corner = new ConvexPartSphere(
                new Point3D(p0.x + rb, p1.y
                        - rb, p1.z - rb), rb, 270, 360, 0, 90);
        objects.add(top_left_front_corner);

        // top left back
        ConvexPartSphere top_left_back_corner = new ConvexPartSphere(
                new Point3D(p0.x + rb, p1.y
                        - rb, p0.z + rb), rb, 180, 270, 0, 90);
        objects.add(top_left_back_corner);

        // top right back
        ConvexPartSphere top_right_back_corner = new ConvexPartSphere(
                new Point3D(p1.x - rb, p1.y
                        - rb, p0.z + rb), rb, 90, 180, 0, 90);
        objects.add(top_right_back_corner);

        // bottom right front
        ConvexPartSphere bottom_right_front_corner = new ConvexPartSphere(
                new Point3D(p1.x - rb,
                        p0.y + rb, p1.z - rb), rb, 0, 90, 90, 180);
        objects.add(bottom_right_front_corner);

        // bottom left front
        ConvexPartSphere bottom_left_front_corner = new ConvexPartSphere(
                new Point3D(p0.x + rb, p0.y
                        + rb, p1.z - rb), rb, 270, 360, 90, 180);
        objects.add(bottom_left_front_corner);

        // bottom left back
        ConvexPartSphere bottom_left_back_corner = new ConvexPartSphere(
                new Point3D(p0.x + rb, p0.y
                        + rb, p0.z + rb), rb, 180, 270, 90, 180);
        objects.add(bottom_left_back_corner);

        // bottom right back
        ConvexPartSphere bottom_right_back_corner = new ConvexPartSphere(
                new Point3D(p1.x - rb, p0.y
                        + rb, p0.z + rb), rb, 90, 180, 90, 180);
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
