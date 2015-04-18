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
 *
 * @author William Matrix Peckham
 */
public abstract class MeshTriangle extends GeometricObject {

    public Mesh mesh = null;
    public int index0 = 0;
    public int index1 = 0;
    public int index2 = 0;
    public Normal normal = new Normal();
    public double area = 0;

    public MeshTriangle() {
    }

    public MeshTriangle(Mesh mesh, int i0, int i1, int i2) {
        super();
        this.mesh = mesh;
        index0 = i0;
        index1 = i1;
        index2 = i2;
    }

    public MeshTriangle(MeshTriangle mt) {
        super(mt);
        mesh = mt.mesh;
        index0 = mt.index0;
        index1 = mt.index1;
        index2 = mt.index2;
        normal.setTo(mt.normal);
    }

    public void computeNormal(boolean reverseNormal) {
        normal.setTo(
                mesh.vertices.get(index1).sub(mesh.vertices.get(index0)).cross(
                        mesh.vertices.get(index2).sub(mesh.vertices.get(index0)))
        );
        normal.normalize();
        if (reverseNormal) {
            normal.setTo(normal.neg());
        }
    }

    public Normal getNormal() {
        return normal;
    }

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

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
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

        double r = r = e * l - h * i;
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

    double interpolateU(double beta, double gamma) {
        return (1 - beta - gamma) * mesh.u.get(index0)
                + beta * mesh.u.get(index1)
                + gamma * mesh.u.get(index2);
    }
    double interpolateV(double beta, double gamma) {
        return (1 - beta - gamma) * mesh.v.get(index0)
                + beta * mesh.v.get(index1)
                + gamma * mesh.v.get(index2);
    }

}
