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
            new Point3D(0, 0, 0),
            new Point3D(0, 0, 0.33),
            new Point3D(0, 0, 0.66),
            new Point3D(0, 0, 1),
            new Point3D(0.33, 0, 0),
            new Point3D(0.33, 0, 0.33),
            new Point3D(0.33, 0, 0.66),
            new Point3D(0.33, 0, 1),
            new Point3D(0.66, 0, 0),
            new Point3D(0.66, 0, 0.33),
            new Point3D(0.66, 0, 0.66),
            new Point3D(0.66, 0, 1),
            new Point3D(1, 0, 0),
            new Point3D(1, 0, 0.33),
            new Point3D(1, 0, 0.66),
            new Point3D(1, 0, 1)
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
            return 1.0 / 20.0;
        }

        @Override
        public double getVStep() {
            return 1.0 / 20.0;
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
            if (vCurve[0].equals(vCurve[1]) && vCurve[2].equals(vCurve[1])
                    && vCurve[2].equals(vCurve[3])) {
                for (int i = 0; i < 4; i++) {
                    //noting
                }
            }
            Point3D p1 = vCurve[0].mul(b1);
            Point3D p2 = vCurve[1].mul(b2);
            Point3D p3 = vCurve[2].mul(b3);
            Point3D p4 = vCurve[3].mul(b4);
            Vector3D du = new Vector3D(p1.add(new Vector3D(p2)).add(
                    new Vector3D(p3)).
                    add(
                            new Vector3D(p4)));
            if (du.lenSquared() == 0.0) {
                //du.y = 1;
                //du.x = -1;
                //du.z = 1;
            }
            return du;
        }
//dP(t) / dt =  -3(1-t)^2 * P0 + 3(1-t)^2 * P1 - 6t(1-t) * P1 - 3t^2 * P2 + 6t(1-t) * P2 + 3t^2 * P3

        Vector3D dVBezier(Point3D[] controlPoints, double u, double v) {
            Point3D[] uCurve = new Point3D[4];
            Point3D[] P = new Point3D[4];
            for (int i = 0; i < 4; ++i) {
                P[0] = controlPoints[i * 4];
                P[1] = controlPoints[4 * i + 1];
                P[2] = controlPoints[4 * i + 2];
                P[3] = controlPoints[4 * i + 3];
                uCurve[i] = evalBezierCurve(P, u);
            }
            double b1 = -3 * (1 - v) * (1 - v);
            double b2 = (3 * (1 - v) * (1 - v) - 6 * v * (1 - v));
            double b3 = (6 * v * (1 - v) - 3 * v * v);
            double b4 = 3 * v * v;
            if (uCurve[0].equals(uCurve[1]) && uCurve[2].equals(uCurve[1])
                    && uCurve[2].equals(uCurve[3])) {
                //noting
            }
            Point3D p1 = uCurve[0].mul(b1);
            Point3D p2 = uCurve[1].mul(b2);
            Point3D p3 = uCurve[2].mul(b3);
            Point3D p4 = uCurve[3].mul(b4);
            Vector3D dv = new Vector3D(p1.add(new Vector3D(p2)).add(
                    new Vector3D(p3)).
                    add(
                            new Vector3D(p4)));
            if (dv.lenSquared() == 0.0) {
                //dv.y = -1;
                //dv.x = 1;
                //dv.z = -1;
            }
            return dv;
        }

        @Override
        public Normal getNormalAt(double u, double v) {
            Vector3D dv = dVBezier(points, u, v);
            Vector3D du = dUBezier(points, u, v);
            Normal n = new Normal(du.cross(dv));
            if (Double.isNaN(n.x)) {
                n.x = 0;
            }
            if (Double.isNaN(n.y)) {
                n.y = 0;
            }
            if (Double.isNaN(n.z)) {
                n.z = 0;
            }
            if (n.x == 0 && n.y == 0 && n.z == 0) {
                n.z = 1;
            }
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
            return ParametricEquation.NormalType.REGULAR;
        }

    }

}
