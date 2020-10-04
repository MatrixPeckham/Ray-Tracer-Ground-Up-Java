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

import com.matrixpeckham.raytracer.geometricobjects.csg.CSGShadeRec;
import com.matrixpeckham.raytracer.util.*;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Smooth triangle for meshes.
 *
 * @author William Matrix Peckham
 */
public class SmoothMeshTriangle extends MeshTriangle {

    /**
     * default constructor
     */
    public SmoothMeshTriangle() {
    }

    /**
     * initializing constructor
     *
     * @param mesh
     * @param i0
     * @param i1
     * @param i2
     */
    public SmoothMeshTriangle(Mesh mesh, int i0, int i1, int i2) {
        super(mesh, i0, i1, i2);
    }

    /**
     * copy constructor
     *
     * @param mt
     */
    public SmoothMeshTriangle(SmoothMeshTriangle mt) {
        super(mt);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public SmoothMeshTriangle cloneGeometry() {
        return new SmoothMeshTriangle(this);
    }

    /**
     * interpolates normal based on barycentric coordinates
     *
     * @param beta
     * @param gamma
     *
     * @return
     */
    public Normal interpolateNormal(double beta, double gamma) {
        Normal n = new Normal(
                mesh.normals.get(index0).mul(1 - beta - gamma).add(
                        mesh.normals.get(index1).mul(beta).add(
                                mesh.normals.get(index2).mul(gamma)
                        )
                )
        );
        n.normalize();
        return n;
    }

    /**
     * hit function, works just like Triangle.hit(), but looks up vertices with
     * indices first. also interpolates normals
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
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

        sr.lastT = t;
        sr.normal.setTo(interpolateNormal(beta, gamma)); // for smooth shading
        sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));

        return (true);
    }

    @Override
    public boolean hit(Ray ray, ArrayList<CSGShadeRec> hits, ShadeRec sr1) {
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

        CSGShadeRec sr = new CSGShadeRec(sr1);
        sr.lastT = t;
        sr.normal.setTo(interpolateNormal(beta, gamma)); // for smooth shading
        sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
        hits.add(sr);
        return (true);
    }

    private static final Logger LOG
            = Logger.getLogger(SmoothMeshTriangle.class.getName());

}
