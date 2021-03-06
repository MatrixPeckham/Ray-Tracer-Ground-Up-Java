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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that contains many utility functions and variables.
 *
 * @author William Matrix Peckham
 */
public class Utility {

    public static class Greek {

        public static class Capital {

            public static final char ALPHA = '\u0391';

            public static final char BETA = '\u0392';

            public static final char GAMMA = '\u0393';

            public static final char DELTA = '\u0394';

            public static final char EPSILON = '\u0395';

            public static final char ZETA = '\u0396';

            public static final char ETA = '\u0397';

            public static final char THETA = '\u0398';

            public static final char IOTA = '\u0399';

            public static final char KAPPA = '\u039a';

            public static final char LAMBDA = '\u039b';

            public static final char MU = '\u039c';

            public static final char NU = '\u039d';

            public static final char XI = '\u039e';

            public static final char OMICRON = '\u039f';

            public static final char PI = '\u03a0';

            public static final char RHO = '\u03a1';

            public static final char SIGMA = '\u03a3';

            public static final char TAU = '\u03a4';

            public static final char UPSILON = '\u03a5';

            public static final char PHI = '\u03a6';

            public static final char CHI = '\u03a7';

            public static final char PSI = '\u03a8';

            public static final char OMEGA = '\u03a9';

        }

        public static class Small {

            public static final char ALPHA = '\u03b1';

            public static final char BETA = '\u03b2';

            public static final char GAMMA = '\u03b3';

            public static final char DELTA = '\u03b4';

            public static final char EPSILON = '\u03b5';

            public static final char ZETA = '\u03b6';

            public static final char ETA = '\u03b7';

            public static final char THETA = '\u03b8';

            public static final char IOTA = '\u03b9';

            public static final char KAPPA = '\u03ba';

            public static final char LAMBDA = '\u03bb';

            public static final char MU = '\u03bc';

            public static final char NU = '\u03bd';

            public static final char XI = '\u03be';

            public static final char OMICRON = '\u03bf';

            public static final char PI = '\u03c0';

            public static final char RHO = '\u03c1';

            public static final char FINAL_SIGMA = '\u03c2';

            public static final char SIGMA = '\u03c3';

            public static final char TAU = '\u03c4';

            public static final char UPSILON = '\u03c5';

            public static final char PHI = '\u03c6';

            public static final char CHI = '\u03c7';

            public static final char PSI = '\u03c8';

            public static final char OMEGA = '\u03c9';

        }

    }

    /**
     * Defined as Math.PI for convenience
     */
    public static final double PI = Math.PI;

    /**
     * Two Pi for convenience, full circle.
     */
    public static final double TWO_PI = 2 * PI;

    /**
     * Pi over 180, for conversion to/from degrees.
     */
    public static final double PI_ON_180 = PI / 180.0;

    /**
     * Convenience inverse pi
     */
    public static final double INV_PI = 1.0 / PI;

    /**
     * convenience inverse two pi
     */
    public static final double INV_2_PI = 1.0 / TWO_PI;

    /**
     * common epsilon
     */
    public static final double EPSILON = 0.0001;

    /**
     * common too-big value.
     */
    public static final double HUGE_VALUE = 1.0e10;

    /**
     * black color
     */
    public static final RGBColor BLACK = new RGBColor(0);

    /**
     * white color
     */
    public static final RGBColor WHITE = new RGBColor(1);

    /**
     * red color
     */
    public static final RGBColor RED = new RGBColor(1, 0, 0);

    /**
     * green color
     */
    public static final RGBColor GREEN = new RGBColor(0, 1, 0);

    /**
     * blue color
     */
    public static final RGBColor BLUE = new RGBColor(0, 0, 1);

    /**
     * yellow color
     */
    public static final RGBColor YELLOW = new RGBColor(1, 1, 0);

    /**
     * random for random functions.
     */
    public static final Random rand = new Random();

    /**
     * rand int between 0 and max value
     *
     * @return
     */
    public static final int randInt() {
        return rand.nextInt(Integer.MAX_VALUE);
    }

    /**
     * rand 0-1 double
     *
     * @return
     */
    public static final double randDouble() {
        return (double) randInt() / (double) Integer.MAX_VALUE;
    }

    /**
     * rand double l-h
     *
     * @param l
     * @param h
     *
     * @return
     */
    public static final double randDouble(double l, double h) {
        double randd = randDouble();
        return randd * (h - l) + l;
    }

    /**
     * rand int between l=h
     *
     * @param l
     * @param h
     *
     * @return
     */
    public static final int randInt(int l, int h) {
        return (int) (randDouble(l, h));
    }

    /**
     * clamp function.
     *
     * @param x   value to clamp
     * @param min min value
     * @param max max value
     *
     * @return min if x&lt;min max if x&gt;max otherwise x
     */
    public static final double clamp(double x, double min, double max) {
        return (x < min ? min : (x > max ? max : x));
    }

    /**
     * sets the random seed
     *
     * @param seed
     */
    public static final void setRandSeed(long seed) {
        rand.setSeed(seed);
    }

    /**
     * returns true if x could be considered 0
     *
     * @param x
     *
     * @return
     */
    public static final boolean isZero(double x) {
        return x > -EPSILON && x < EPSILON;
    }

    /**
     * Solve quadratic equation
     *
     * @param c
     * @param s
     *
     * @return
     */
    public static final int solveQuadric(double c[], double s[]) {
        double p, q, D;

        /* normal form: x^2 + px + q = 0 */
        p = c[1] / (2 * c[2]);
        q = c[0] / c[2];

        D = p * p - q;

        if (isZero(D)) {
            s[0] = -p;
            return 1;
        } else if (D > 0) {
            double sqrt_D = Math.sqrt(D);

            s[0] = sqrt_D - p;
            s[1] = -sqrt_D - p;
            return 2;
        } else /* if (D < 0) */ {
            return 0;
        }
    }

    /**
     * solve a cubic
     *
     * @param c
     * @param s
     *
     * @return
     */
    public static final int solveCubic(double c[], double s[]) {
        int i, num;
        double sub;
        double A, B, C;
        double sq_A, p, q;
        double cb_p, D;

        /* normal form: x^3 + Ax^2 + Bx + C = 0 */
        A = c[2] / c[3];
        B = c[1] / c[3];
        C = c[0] / c[3];

        /* substitute x = y - A/3 to eliminate quadric term:
         * x^3 +px + q = 0 */
        sq_A = A * A;
        p = 1.0 / 3 * (-1.0 / 3 * sq_A + B);
        q = 1.0 / 2 * (2.0 / 27 * A * sq_A - 1.0 / 3 * A * B + C);

        /* use Cardano's formula */
        cb_p = p * p * p;
        D = q * q + cb_p;

        if (isZero(D)) {
            if (isZero(q)) {
                /* one triple solution */

                s[0] = 0;
                num = 1;
            } else {
                /* one single and one double solution */

                double u = Math.cbrt(-q);
                s[0] = 2 * u;
                s[1] = -u;
                num = 2;
            }
        } else if (D < 0) {
            /* Casus irreducibilis: three real solutions */

            double phi = 1.0 / 3 * Math.acos(-q / Math.sqrt(-cb_p));
            double t = 2 * Math.sqrt(-p);

            s[0] = t * Math.cos(phi);
            s[1] = -t * Math.cos(phi + PI / 3);
            s[2] = -t * Math.cos(phi - PI / 3);
            num = 3;
        } else {
            /* one real solution */

            double sqrt_D = Math.sqrt(D);
            double u = Math.cbrt(sqrt_D - q);
            double v = -Math.cbrt(sqrt_D + q);

            s[0] = u + v;
            num = 1;
        }

        /* resubstitute */
        sub = 1.0 / 3 * A;

        for (i = 0; i < num; ++i) {
            s[i] -= sub;
        }

        return num;
    }

    /**
     * solve a quartic
     *
     * @param c
     * @param s
     *
     * @return
     */
    public static final int solveQuartic(double c[], double s[]) {
        double[] coeffs = new double[4];
        double z, u, v, sub;
        double A, B, C, D;
        double sq_A, p, q, r;
        int i, num;

        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */
        A = c[3] / c[4];
        B = c[2] / c[4];
        C = c[1] / c[4];
        D = c[0] / c[4];

        /* substitute x = y - A/4 to eliminate cubic term:
         * x^4 + px^2 + qx + r = 0 */
        sq_A = A * A;
        p = -3.0 / 8 * sq_A + B;
        q = 1.0 / 8 * sq_A * A - 1.0 / 2 * A * B + C;
        r = -3.0 / 256 * sq_A * sq_A + 1.0 / 16 * sq_A * B - 1.0 / 4 * A * C + D;

        if (isZero(r)) {
            /* no absolute term: y(y^3 + py + q) = 0 */

            coeffs[0] = q;
            coeffs[1] = p;
            coeffs[2] = 0;
            coeffs[3] = 1;

            num = solveCubic(coeffs, s);

            s[num++] = 0;
        } else {
            /* solve the resolvent cubic ... */

            coeffs[0] = 1.0 / 2 * r * p - 1.0 / 8 * q * q;
            coeffs[1] = -r;
            coeffs[2] = -1.0 / 2 * p;
            coeffs[3] = 1;

            int shouldBeOne = solveCubic(coeffs, s);

            if (shouldBeOne != 0) {
                LOG.log(Level.WARNING,
                        "Quartic equation got unusual result from solveCubic.");
            }

            /* ... and take the one real solution ... */
            z = s[0];

            /* ... to build two quadric equations */
            u = z * z - r;
            v = 2 * z - p;

            if (isZero(u)) {
                u = 0;
            } else if (u > 0) {
                u = Math.sqrt(u);
            } else {
                return 0;
            }

            if (isZero(v)) {
                v = 0;
            } else if (v > 0) {
                v = Math.sqrt(v);
            } else {
                return 0;
            }

            coeffs[0] = z - u;
            coeffs[1] = q < 0 ? -v : v;
            coeffs[2] = 1;

            num = solveQuadric(coeffs, s);

            coeffs[0] = z + u;
            coeffs[1] = q < 0 ? v : -v;
            coeffs[2] = 1;

            int tempNum;
            double[] tempSols = new double[2];
            tempNum = solveQuadric(coeffs, tempSols);

            System.arraycopy(tempSols, 0, s, num, tempNum);
            num += tempNum;
        }

        /* resubstitute */
        sub = 1.0 / 4 * A;

        for (i = 0; i < num; ++i) {
            s[i] -= sub;
        }

        return num;
    }

    /**
     * Modulus double operator. a%b
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static double mod(double a, double b) {
        int n = (int) (a / b);

        a -= n * b;
        if (a < 0.0) {
            a += b;
        }

        return (a);
    }

    /**
     * Smooth pulse 0 x for 0-e1 and e3-1 one for e2-e3 lerped between.
     *
     * @param e0
     * @param e1
     * @param e2
     * @param e3
     * @param x
     *
     * @return
     */
    public static double smoothPulse(double e0, double e1, double e2, double e3,
            double x) {
        return (smoothStep(e0, e1, x) - smoothStep(e2, e3, x));
    }

    /**
     * same as pulse, but repeated for period
     *
     * @param e0
     * @param e1
     * @param e2
     * @param e3
     * @param period
     * @param x
     *
     * @return
     */
    public static double smoothPulseTrain(double e0, double e1, double e2,
            double e3, double period, double x) {
        return (smoothPulse(e0, e1, e2, e3, mod(x, period)));
    }

    /**
     * Steps from as x ranges [0-1] returns 0 for x [0-a] 1 for x [b-inf] lerp
     * from 0-1 for x [a-b].
     *
     * @param a
     * @param b
     * @param x
     *
     * @return
     */
    public static double smoothStep(double a, double b, double x) {
        if (x < a) {
            return (0.0);
        }

        if (x >= b) {
            return (1.0);
        }

        double y = (x - a) / (b - a);  // normalise to [0, 1]

        return (y * y * (3.0 - 2.0 * y));
    }

    /**
     * lerps color
     *
     * @param c0
     * @param c1
     * @param f
     *
     * @return
     */
    public static RGBColor mixColor(RGBColor c0, RGBColor c1, double f) {
        return c0.mul(1 - f).add(c1.mul(f));
    }

    /**
     * lerps double
     *
     * @param a
     * @param b
     * @param f
     *
     * @return
     */
    public static double mixDouble(double a, double b, double f) {
        return ((1.0 - f) * a + f * b);
    }

    private static final Logger LOG = Logger.getLogger(Utility.class.getName());

}
