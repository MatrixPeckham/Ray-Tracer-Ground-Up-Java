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
package com.matrixpeckham.raytracer.geometricobjects.primatives;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.BruteForceSolver;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.QuarticAnswer;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Solvers;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class Torus extends GeometricObject {

    private double a;
    private double b;
    private BBox bbox;

    public Torus() {
        this(2, 0.5);
    }

    public Torus(double a, double b) {
        super();
        this.a = a;
        this.b = b;
        bbox = new BBox(-a - b, a + b, -b, b, -a - b, a + b);
    }

    public Torus(Torus t) {
        super(t);
        this.a = t.a;
        this.b = t.b;
        bbox = new BBox(-a - b, a + b, -b, b, -a - b, a + b);
    }

    @Override
    public GeometricObject clone() {
        return new Torus(this);
    }

    public Normal computeNormal(Point3D p) {
        Normal normal = new Normal();
        double param_squared = a * a + b * b;

        double x = p.x;
        double y = p.y;
        double z = p.z;
        double sum_squared = x * x + y * y + z * z;

        normal.x = 4.0 * x * (sum_squared - param_squared);
        normal.y = 4.0 * y * (sum_squared - param_squared + 2.0 * a * a);
        normal.z = 4.0 * z * (sum_squared - param_squared);
        normal.normalize();

        return (normal);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        if (!bbox.hit(ray)) {
            return (false);
        }

        double x1 = ray.o.x;
        double y1 = ray.o.y;
        double z1 = ray.o.z;
        double d1 = ray.d.x;
        double d2 = ray.d.y;
        double d3 = ray.d.z;

        double[] coeffs = new double[5];	// coefficient array for the quartic equation
        double[] roots = new double[4];	// solution array for the quartic equation

        // define the coefficients of the quartic equation
        double sum_d_sqrd = d1 * d1 + d2 * d2 + d3 * d3;
        double e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
        double f = x1 * d1 + y1 * d2 + z1 * d3;
        double four_a_sqrd = 4.0 * a * a;

        double E = e * e - four_a_sqrd * (b * b - y1 * y1); 	// constant term
        double D = 4.0 * f * e + 2.0 * four_a_sqrd * y1 * d2;
        double C = 2.0 * sum_d_sqrd * e + 4.0 * f * f + four_a_sqrd * d2 * d2;
        double B = 4.0 * sum_d_sqrd * f;
        double A = sum_d_sqrd * sum_d_sqrd; // coefficient of t^4
        coeffs[0] = E;
        coeffs[1] = D;
        coeffs[2] = C;
        coeffs[3] = B;
        coeffs[4] = A;

        boolean intersected = false;
        

        // find roots of the quartic equation
        //int num_real_roots = Utility.solveQuartic(coeffs, roots);
        //QuarticAnswer qa = Utility.solveQuartic(A, B,C,D,E);
        BruteForceSolver solver = new BruteForceSolver(A, B, C, D, E);
        int num_real_roots = solver.solveQuartic(roots);

        double t = Utility.HUGE_VALUE;

        //        if (qa.numRealRoots == 0) // ray misses the torus
        if (num_real_roots == 0) {
            return (false);
        }

        // find the smallest root greater than kEpsilon, if any
        // the roots array is not sorted
        //        for (int j = 0; j < qa.numRealRoots; j++) {
        for (int j = 0; j < num_real_roots; j++) {
            if (roots[j] > Utility.EPSILON) {
                intersected = true;
                if (roots[j] < t) {
                    t = roots[j];
                }
            }
        }

        if (!intersected) {
            return (false);
        }

        s.lastT = t;
        s.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
        s.normal.setTo(computeNormal(s.localHitPosition));

        return (true);
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        if (!shadows) {
            return false;
        }
        if (!bbox.hit(ray)) {
            return (false);
        }

        double x1 = ray.o.x;
        double y1 = ray.o.y;
        double z1 = ray.o.z;
        double d1 = ray.d.x;
        double d2 = ray.d.y;
        double d3 = ray.d.z;

        double[] coeffs = new double[5];	// coefficient array for the quartic equation
        double[] roots = new double[4];	// solution array for the quartic equation

        // define the coefficients of the quartic equation
        double sum_d_sqrd = d1 * d1 + d2 * d2 + d3 * d3;
        double e = x1 * x1 + y1 * y1 + z1 * z1 - a * a - b * b;
        double f = x1 * d1 + y1 * d2 + z1 * d3;
        double four_a_sqrd = 4.0 * a * a;

        coeffs[0] = e * e - four_a_sqrd * (b * b - y1 * y1); 	// constant term
        coeffs[1] = 4.0 * f * e + 2.0 * four_a_sqrd * y1 * d2;
        coeffs[2] = 2.0 * sum_d_sqrd * e + 4.0 * f * f + four_a_sqrd * d2 * d2;
        coeffs[3] = 4.0 * sum_d_sqrd * f;
        coeffs[4] = sum_d_sqrd * sum_d_sqrd;  					// coefficient of t^4
        double E = e * e - four_a_sqrd * (b * b - y1 * y1); 	// constant term
        double D = 4.0 * f * e + 2.0 * four_a_sqrd * y1 * d2;
        double C = 2.0 * sum_d_sqrd * e + 4.0 * f * f + four_a_sqrd * d2 * d2;
        double B = 4.0 * sum_d_sqrd * f;
        double A = sum_d_sqrd * sum_d_sqrd;  					// coefficient of t^4

        // find roots of the quartic equation
        BruteForceSolver solver = new BruteForceSolver(A, B, C, D, E);
        int num_real_roots = solver.solveQuartic(roots);
        boolean intersected = false;
        double t = Utility.HUGE_VALUE;
        /*double[] roots = Solvers.solveQuartic(A, B, C, D, E);
         for (int i = 0; i < roots.length; i++) {
         if (roots[i] > 0 & roots[i] < t) {
         t = roots[i];
         intersected = true;
         }
         }*/
        if (num_real_roots == 0) // ray misses the torus
        {
            return (false);
        }

         // find the smallest root greater than kEpsilon, if any
        // the roots array is not sorted
        for (int j = 0; j < num_real_roots; j++) {
            if (roots[j] > Utility.EPSILON) {
                intersected = true;
                if (roots[j] < t) {
                    t = roots[j];
                }
            }
        }

        if (!intersected) {
            return (false);
        }

        tr.d = t;

        return (true);
    }

}
