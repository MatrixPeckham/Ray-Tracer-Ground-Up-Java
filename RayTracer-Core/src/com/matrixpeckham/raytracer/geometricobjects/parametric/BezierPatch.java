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
package com.matrixpeckham.raytracer.geometricobjects.parametric;

import com.matrixpeckham.raytracer.util.*;

/**
 * Torus class. Is a parametric object.
 *
 * @author William Matrix Peckham
 */
public class BezierPatch extends ParametricObject {

    /**
     * default constructor
     */
    public BezierPatch() {
        this(new Point3D[]{
            new Point3D(-0.5, 0, 0 - 0.5),
            new Point3D(-0.5, 0, 0.33 - 0.5),
            new Point3D(-0.5, 0, 0.66 - 0.5),
            new Point3D(-0.5, 0, 1 - 0.5),
            new Point3D(0.33 - 0.5, 0, 0 - 0.5),
            new Point3D(0.33 - 0.5, 0, 0.33 - 0.5),
            new Point3D(0.33 - 0.5, 0, 0.66 - 0.5),
            new Point3D(0.33 - 0.5, 0, 1 - 0.5),
            new Point3D(0.66 - 0.5, 0, 0 - 0.5),
            new Point3D(0.66 - 0.5, 0, 0.33 - 0.5),
            new Point3D(0.66 - 0.5, 0, 0.66 - 0.5),
            new Point3D(0.66 - 0.5, 0, 1 - 0.5),
            new Point3D(1 - 0.5, 0, 0 - 0.5),
            new Point3D(1 - 0.5, 0, 0.33 - 0.5),
            new Point3D(1 - 0.5, 0, 0.66 - 0.5),
            new Point3D(1 - 0.5, 0, 1 - 0.5)
        });
    }

    /**
     * initializing constructor
     *
     * @param a
     * @param b
     */
    public BezierPatch(Point3D[] points) {
        super(new BezierParametric(points));
    }

    /**
     * protected constructor, this is the workhorse constructor, calls
     * superclass constructor that does the real work
     *
     * @param par
     */
    protected BezierPatch(BezierParametric par) {
        super(par);
    }

    /**
     * implementation of parametric equation for torus
     */
    protected static class BezierParametric implements ParametricEquation {

        Point3D[] points = new Point3D[16];

        /**
         * initializing constructor
         *
         * @param p
         */
        public BezierParametric(Point3D[] p) {
            if (p.length < 16) {
                throw new IllegalArgumentException("Not Enouigh Points");
            }
            for (int i = 0; i < 16; i++) {
                points[i] = new Point3D(p[i]);
            }
        }

        //bounds of u and v are both 0-2pi
        @Override
        public double getMinU() {
            return 0;
        }

        @Override
        public double getMaxU() {
            return 1;
        }

        @Override
        public double getMinV() {
            return 0;
        }

        @Override
        public double getMaxV() {
            return 1;
        }

        //steps are arbitrarily 0.1
        @Override
        public double getUStep() {
            return 0.01;
        }

        @Override
        public double getVStep() {
            return 0.01;
        }

        //most important two methods here do the actual work
        @Override
        public Point3D getPointAt(double u, double v) {
            Point3D[] Pu = new Point3D[4];
            // compute 4 control points along u direction
            for (int i = 0; i < 4; ++i) {
                Point3D[] curveP = new Point3D[4];
                curveP[0] = points[i * 4];
                curveP[1] = points[i * 4 + 1];
                curveP[2] = points[i * 4 + 2];
                curveP[3] = points[i * 4 + 3];
                Pu[i] = evalBezierCurve(curveP, u);
            }
            // compute final position on the surface using v
            return evalBezierCurve(Pu, v);
        }

        Point3D evalBezierCurve(Point3D[] P, double t) {
            double b0 = (1 - t) * (1 - t) * (1 - t);
            double b1 = 3 * t * (1 - t) * (1 - t);
            double b2 = 3 * t * t * (1 - t);
            double b3 = t * t * t;
            return P[0].mul(b0).add(
                    new Vector3D(P[1].mul(b1))
            ).add(
                    new Vector3D((P[2].mul(b2))).add(new Vector3D(P[3].
                            mul(b3)))
            );
        }

        Vector3D dUBezier(Point3D[] controlPoints, double u, double v) {
            Point3D[] P = new Point3D[4];
            Point3D[] vCurve = new Point3D[4];
            for (int i = 0; i < 4; ++i) {
                P[0] = controlPoints[i];
                P[1] = controlPoints[4 + i];
                P[2] = controlPoints[8 + i];
                P[3] = controlPoints[12 + i];
                vCurve[i] = evalBezierCurve(P, v);
            }
            double b1 = -3 * (1 - u) * (1 - u);
            double b2 = (3 * (1 - u) * (1 - u) - 6 * u * (1 - u));
            double b3 = (6 * u * (1 - u) - 3 * u * u);
            double b4 = 3 * u * u;
            Point3D p1 = vCurve[0].mul(b1);
            Point3D p2 = vCurve[1].mul(b2);
            Point3D p3 = vCurve[2].mul(b3);
            Point3D p4 = vCurve[3].mul(b4);
            return new Vector3D(p1.add(new Vector3D(p2)).add(new Vector3D(p3)).
                    add(
                            new Vector3D(p4)));
        }

        Vector3D dVBezier(Point3D[] controlPoints, double u, double v) {
            Point3D[] uCurve = new Point3D[4];
            Point3D[] P = new Point3D[4];
            for (int i = 0; i < 4; ++i) {
                P[0] = controlPoints[i * 4];
                P[1] = controlPoints[1 + i * 4];
                P[2] = controlPoints[2 + i * 4];
                P[3] = controlPoints[3 + i * 4];
                uCurve[i] = evalBezierCurve(P, u);
            }
            double b1 = -3 * (1 - v) * (1 - v);
            double b2 = (3 * (1 - v) * (1 - v) - 6 * v * (1 - v));
            double b3 = (6 * v * (1 - v) - 3 * v * v);
            double b4 = 3 * v * v;
            Point3D p1 = uCurve[0].mul(b1);
            Point3D p2 = uCurve[1].mul(b2);
            Point3D p3 = uCurve[2].mul(b3);
            Point3D p4 = uCurve[3].mul(b4);
            return new Vector3D(p1.add(new Vector3D(p2)).add(new Vector3D(p3)).
                    add(
                            new Vector3D(p4)));
        }

        @Override
        public Normal getNormalAt(double u, double v) {
            Vector3D dv = dVBezier(points, u, v);
            Vector3D du = dUBezier(points, u, v);
            Normal n = new Normal(du.cross(dv)).neg();
            n.normalize();
            return n;
        }

        //both closed
        @Override
        public boolean isClosedU() {
            return false;
        }

        @Override
        public boolean isClosedV() {
            return false;
        }

        //outward normals
        @Override
        public ParametricEquation.NormalType getNormalType() {
            return ParametricEquation.NormalType.TWO_SIDE;
        }

    }

}
