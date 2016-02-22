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
package com.matrixpeckham.raytracer.geometricobjects.triangles;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Class that represents a triangle that is part of a mesh, saves on memory by
 * sharing vertices, normals, and uv coordinates for all vertices. Implements
 * all the methods of geometric object except hit(). Hit() itself is almost
 * nearly identical between implementations, the smooth triangles use
 * interpolate normals instead of using a single one, and the uv ones add uv
 * coordinates to the ShadeRec. unfortunately these two things are not easy to
 * add outside of the hit function.
 *
 * @author William Matrix Peckham
 */
public abstract class MeshTriangle extends GeometricObject {

    /**
     * reference to a mesh that contains the information for the triangle
     */
    public Mesh mesh = null;

    /**
     * mesh indices
     */
    public int index0 = 0;
    public int index1 = 0;
    public int index2 = 0;

    /**
     * normal of triangle plane
     */
    public Normal normal = new Normal();

    /**
     * area of triangle, not used yet, could be used for making meshes area
     * lights, but that would be non-trivial
     */
    public double area = 0;

    /**
     * default constructor
     */
    public MeshTriangle() {
    }

    /**
     * constructor initializes indices and mesh
     *
     * @param mesh
     * @param i0
     * @param i1
     * @param i2
     */
    public MeshTriangle(Mesh mesh, int i0, int i1, int i2) {
        super();
        this.mesh = mesh;
        index0 = i0;
        index1 = i1;
        index2 = i2;
    }

    /**
     * copy constructor
     *
     * @param mt
     */
    public MeshTriangle(MeshTriangle mt) {
        super(mt);
        mesh = mt.mesh;
        index0 = mt.index0;
        index1 = mt.index1;
        index2 = mt.index2;
        normal.setTo(mt.normal);
    }

    /**
     * computes the single plane normal of the triangle, used during loading
     * mesh, as flat normal for triangles and for creating per-vertex normals
     * for smooth meshes
     *
     * @param reverseNormal
     */
    public void computeNormal(boolean reverseNormal) {
        //cross product of vertex position differences
        normal.setTo(
                mesh.vertices.get(index1).sub(mesh.vertices.get(index0)).cross(
                        mesh.vertices.get(index2).sub(mesh.vertices.get(index0)))
        );
        //normalize and reverse if requested
        normal.normalize();
        if (reverseNormal) {
            normal.setTo(normal.neg());
        }
    }

    /**
     * returns the plane normal. used only in triangle mesh creation
     *
     * @return
     */
    public Normal getNormal() {
        return normal;
    }

    /**
     * bounding box method, calculates the bounding box. we get the coordinates
     * from the mesh by using the image
     *
     * @return
     */
    @Override
    public BBox getBoundingBox() {
        double delta = 0.0001;  // to avoid degenerate bounding boxes

        Point3D v1 = new Point3D(mesh.vertices.get(index0));
        Point3D v2 = new Point3D(mesh.vertices.get(index1));
        Point3D v3 = new Point3D(mesh.vertices.get(index2));

        return (new BBox(Math.min(Math.min(v1.x, v2.x), v3.x) - delta, Math.max(
                Math.max(v1.x, v2.x), v3.x) + delta,
                Math.min(Math.min(v1.y, v2.y), v3.y) - delta, Math.max(Math.max(
                                v1.y, v2.y), v3.y) + delta,
                Math.min(Math.min(v1.z, v2.z), v3.z) - delta, Math.max(Math.max(
                                v1.z, v2.z), v3.z) + delta));
    }

    /**
     * shadow hit, works like Triangle.hit(), but we have to get the vertices
     * from the indexes
     *
     * @param ray
     * @param tr
     * @return
     */
    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        //early bailout that all implementations have
        if (!shadows) {
            return false;
        }
        Point3D v0 = new Point3D(mesh.vertices.get(index0));
        Point3D v1 = new Point3D(mesh.vertices.get(index1));
        Point3D v2 = new Point3D(mesh.vertices.get(index2));

        double a = v0.x - v1.x, b = v0.x - v2.x, c = ray.d.x, d = v0.x - ray.o.x;
        double e = v0.y - v1.y, f = v0.y - v2.y, g = ray.d.y, h = v0.y - ray.o.y;
        double i = v0.z - v1.z, j = v0.z - v2.z, k = ray.d.z, l = v0.z - ray.o.z;

        double m = f * k - g * j, n = h * k - g * l, p = f * l - h * j;
        double q = g * i - e * k, s = e * j - f * i;

        double inv_denom = 1.0 / (a * m + b * q + c * s);

        double e1 = d * m - b * n - c * p;
        double beta = e1 * inv_denom;

        if (beta < 0.0) {
            return (false);
        }

        double r = e * l - h * i;
        double e2 = a * n + d * q + c * r;
        double gamma = e2 * inv_denom;

        if (gamma < 0.0) {
            return (false);
        }

        if (beta + gamma > 1.0) {
            return (false);
        }

        double e3 = a * p - b * r + d * s;
        double t = e3 * inv_denom;

        if (t < Utility.EPSILON) {
            return (false);
        }

        tr.d = t;

        return (true);
    }

    /**
     * interpolates u coordinate of the mesh based on the barycentric
     * coordinates
     *
     * @param beta
     * @param gamma
     * @return
     */
    double interpolateU(double beta, double gamma) {
        return (1 - beta - gamma) * mesh.u.get(index0)
                + beta * mesh.u.get(index1)
                + gamma * mesh.u.get(index2);
    }

    /**
     * interpolates v coordinate of the mesh based on the barycentric
     * coordinates
     *
     * @param beta
     * @param gamma
     * @return
     */
    double interpolateV(double beta, double gamma) {
        return (1 - beta - gamma) * mesh.v.get(index0)
                + beta * mesh.v.get(index1)
                + gamma * mesh.v.get(index2);
    }

}
