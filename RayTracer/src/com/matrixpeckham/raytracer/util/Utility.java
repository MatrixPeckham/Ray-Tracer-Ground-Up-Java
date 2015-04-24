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
package com.matrixpeckham.raytracer.util;

import java.util.Random;

/**
 *
 * @author William Matrix Peckham
 */
public class Utility {

    public static final double PI = Math.PI;
    public static final double TWO_PI = 2 * PI;
    public static final double PI_ON_180 = PI / 180.0;
    public static final double INV_PI = 1.0 / PI;
    public static final double INV_2_PI = 1.0 / TWO_PI;
    public static final double EPSILON = 0.0001;
    public static final double HUGE_VALUE = 1.0e10;
    public static final RGBColor BLACK = new RGBColor(0);
    public static final RGBColor WHITE = new RGBColor(1);
    public static final RGBColor RED = new RGBColor(1, 0, 0);

    public static final Random rand = new Random();

    public static final int randInt() {
        return rand.nextInt(Integer.MAX_VALUE);
    }

    public static final double randDouble() {
        return (double) randInt() / (double) Integer.MAX_VALUE;
    }

    public static final double randDouble(double l, double h) {
        double rand = randDouble();
        return rand * (h - l) + l;
    }

    public static final int randInt(int l, int h) {
        return (int) (randDouble(l, h));
    }

    public static final double clamp(double x, double min, double max) {
        return (x < min ? min : (x > max ? max : x));
    }

    public static final void setRandSeed(long seed) {
        rand.setSeed(seed);
    }

    public static final boolean isZero(double x) {
        return x > -EPSILON && x < EPSILON;
    }

    
public static final int SolveQuadric(double c[], double s[]) {
    double p, q, D;

    /* normal form: x^2 + px + q = 0 */

    p = c[ 1 ] / (2 * c[ 2 ]);
    q = c[ 0 ] / c[ 2 ];

    D = p * p - q;

    if (isZero(D)) {
	s[ 0 ] = - p;
	return 1;
    }
    else if (D > 0) {
	double sqrt_D = Math.sqrt(D);

	s[ 0 ] =   sqrt_D - p;
	s[ 1 ] = - sqrt_D - p;
	return 2;
    }
    else /* if (D < 0) */
        return 0;
}


public static final int solveCubic(double c[], double s[]) {
    int     i, num;
    double  sub;
    double  A, B, C;
    double  sq_A, p, q;
    double  cb_p, D;

    /* normal form: x^3 + Ax^2 + Bx + C = 0 */

    A = c[ 2 ] / c[ 3 ];
    B = c[ 1 ] / c[ 3 ];
    C = c[ 0 ] / c[ 3 ];

    /*  substitute x = y - A/3 to eliminate quadric term:
	x^3 +px + q = 0 */

    sq_A = A * A;
    p = 1.0/3 * (- 1.0/3 * sq_A + B);
    q = 1.0/2 * (2.0/27 * A * sq_A - 1.0/3 * A * B + C);

    /* use Cardano's formula */

    cb_p = p * p * p;
    D = q * q + cb_p;

    if (isZero(D)) {
		if (isZero(q)) { /* one triple solution */
		    s[ 0 ] = 0;
		    num = 1;
		}
	else { /* one single and one double solution */
	    double u = Math.cbrt(-q);
	    s[ 0 ] = 2 * u;
	    s[ 1 ] = - u;
	    num = 2;
	}
    }
    else if (D < 0) { /* Casus irreducibilis: three real solutions */
		double phi = 1.0/3 * Math.acos(-q / Math.sqrt(-cb_p));
		double t = 2 * Math.sqrt(-p);

		s[ 0 ] =   t * Math.cos(phi);
		s[ 1 ] = - t * Math.cos(phi + PI / 3);
		s[ 2 ] = - t * Math.cos(phi - PI / 3);
		num = 3;
    }
    else { /* one real solution */
		double sqrt_D = Math.sqrt(D);
		double u = Math.cbrt(sqrt_D - q);
		double v = - Math.cbrt(sqrt_D + q);

		s[ 0 ] = u + v;
		num = 1;
    }

    /* resubstitute */

    sub = 1.0/3 * A;

    for (i = 0; i < num; ++i)
	s[ i ] -= sub;

    return num;
}



public static final int solveQuartic(double c[], double s[]) {
    double[]  coeffs=new double[4];
    double  z, u, v, sub;
    double  A, B, C, D;
    double  sq_A, p, q, r;
    int     i, num;

    /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */

    A = c[ 3 ] / c[ 4 ];
    B = c[ 2 ] / c[ 4 ];
    C = c[ 1 ] / c[ 4 ];
    D = c[ 0 ] / c[ 4 ];

    /*  substitute x = y - A/4 to eliminate cubic term:
	x^4 + px^2 + qx + r = 0 */

    sq_A = A * A;
    p = - 3.0/8 * sq_A + B;
    q = 1.0/8 * sq_A * A - 1.0/2 * A * B + C;
    r = - 3.0/256*sq_A*sq_A + 1.0/16*sq_A*B - 1.0/4*A*C + D;

    if (isZero(r)) {
		/* no absolute term: y(y^3 + py + q) = 0 */

		coeffs[ 0 ] = q;
		coeffs[ 1 ] = p;
		coeffs[ 2 ] = 0;
		coeffs[ 3 ] = 1;

		num = solveCubic(coeffs, s);

		s[ num++ ] = 0;
    }
    else {
		/* solve the resolvent cubic ... */

		coeffs[ 0 ] = 1.0/2 * r * p - 1.0/8 * q * q;
		coeffs[ 1 ] = - r;
		coeffs[ 2 ] = - 1.0/2 * p;
		coeffs[ 3 ] = 1;
		
		int shouldBeOne = solveCubic(coeffs, s);

		/* ... and take the one real solution ... */

		z = s[ 0 ];

		/* ... to build two quadric equations */

		u = z * z - r;
		v = 2 * z - p;

		if (isZero(u))
		    u = 0;
		else if (u > 0)
		    u = Math.sqrt(u);
		else
		    return 0;

		if (isZero(v))
		    v = 0;
		else if (v > 0)
		    v = Math.sqrt(v);
		else
		    return 0;

		coeffs[ 0 ] = z - u;
		coeffs[ 1 ] = q < 0 ? -v : v;
		coeffs[ 2 ] = 1;

		num = SolveQuadric(coeffs, s);

		coeffs[ 0 ]= z + u;
		coeffs[ 1 ] = q < 0 ? v : -v;
		coeffs[ 2 ] = 1;

                int tempNum = 0;
                double[] tempSols = new double[2];
		tempNum = SolveQuadric(coeffs, tempSols);
                
                for(int in =0; in<tempNum; in++){
                    s[in+num]=tempSols[in];
                }
                num+=tempNum;
	}

    /* resubstitute */

    sub = 1.0/4 * A;

    for (i = 0; i < num; ++i)
		s[ i ] -= sub;

    return num;
}

    public static QuarticAnswer solveQuartic(double A, double B, double C,
            double D, double E) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
