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

import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * Complex number class for the brute force solver of quartics. it's this class
 * and its use of BigDecimal that makes the solver so slow.
 *
 * @author William Matrix Peckham
 */
public class ComplexNumber {

    /**
     * Precision used for all div calls, needed because of the exception thrown
     * when infinite precision is used and div causes repeating decimals
     */
    public static final int DIV_PRECISION = 50;

    /**
     * Resolution that we will try to get the square root to.
     */
    public static final BigDecimal ROOT_PRECISION = new BigDecimal(
            "0.0000001");

    /**
     * Maximum number of iterations through the root finding algorithm.
     */
    public static final int MAX_ROOT_ITERS = 100;

    /**
     * real component
     */
    public final BigDecimal real;

    /**
     * imaginary component
     */
    public final BigDecimal imaginary;

    /**
     * cache the absolute value, lazy initialized. found to be a bottleneck
     */
    private BigDecimal abs = null;

    /**
     * Initialize with doubles
     *
     * @param r
     * @param i
     */
    public ComplexNumber(double r, double i) {
        real = new BigDecimal(r + "");
        imaginary = new BigDecimal(i + "");
    }

    /**
     * Initialize with big decimals
     *
     * @param r
     * @param i
     */
    public ComplexNumber(BigDecimal r, BigDecimal i) {
        real = r;
        imaginary = i;
    }

    /**
     * returns the negative of this number
     *
     * @return
     */
    public ComplexNumber negate() {
        return new ComplexNumber(real.negate(), imaginary.negate());
    }

    /**
     * Multiply by a scalar.
     *
     * @param d
     * @return
     */
    public ComplexNumber mult(double d) {
        return new ComplexNumber(real.multiply(new BigDecimal(d + "")),
                imaginary.multiply(new BigDecimal(d + "")));
    }

    /**
     * Multiply by a scalar.
     *
     * @param d
     * @return
     */
    public ComplexNumber mult(BigDecimal d) {
        return new ComplexNumber(real.multiply(d), imaginary.multiply(d));
    }

    /**
     * Multiply by another complex number.
     *
     * @param o
     * @return
     */
    public ComplexNumber mult(ComplexNumber o) {
        BigDecimal rr = real.multiply(o.real);
        BigDecimal ri = real.multiply(o.imaginary);
        BigDecimal ir = imaginary.multiply(o.real);
        BigDecimal ii = imaginary.multiply(o.imaginary).negate();
        BigDecimal r = rr.add(ii);
        BigDecimal i = ri.add(ir);
        return new ComplexNumber(r, i);
    }

    /**
     * add two complex numbers together
     *
     * @param o
     * @return
     */
    public ComplexNumber add(ComplexNumber o) {
        return new ComplexNumber(real.add(o.real), imaginary.add(o.imaginary));
    }

    /**
     * subtract two real numbers
     *
     * @param o
     * @return
     */
    public ComplexNumber sub(ComplexNumber o) {
        return new ComplexNumber(real.subtract(o.real), imaginary.subtract(
                o.imaginary));
    }

    /**
     * Gets the congigate.
     *
     * @return
     */
    public ComplexNumber congigate() {
        return new ComplexNumber(real, imaginary.negate());
    }

    /**
     * divide two complex numbers.
     *
     * @param o
     * @return
     */
    public ComplexNumber div(ComplexNumber o) {
        BigDecimal x = real;
        BigDecimal y = imaginary;
        BigDecimal u = o.real;
        BigDecimal v = o.imaginary;
        BigDecimal denom = u.multiply(u).add(v.multiply(v));
        BigDecimal rnumer = x.multiply(u).add(y.multiply(v));
        BigDecimal inumer = x.negate().multiply(v).add(y.multiply(u));
        return new ComplexNumber(rnumer.divide(denom, DIV_PRECISION,
                BigDecimal.ROUND_HALF_UP), inumer.divide(denom, DIV_PRECISION,
                        BigDecimal.ROUND_HALF_UP));
    }

    /**
     * Returns the absolute value of this complex number. caches it for later.
     *
     * @return
     */
    public BigDecimal abs() {
        if (abs == null) {
            abs = takeRoot(2, real.multiply(real).add(imaginary.multiply(
                    imaginary)));
        }
        return abs;
        //return Math.sqrt(real*real+imaginary*imaginary);
    }

    /**
     * Takes the root-th root of n. convenience for
     * takeRoot(root,n,ROOT_PRECISION)
     *
     * @param root
     * @param n
     * @return
     */
    public BigDecimal takeRoot(int root, BigDecimal n) {
        return takeRoot(root, n, ROOT_PRECISION);
    }

    /*    public static BigDecimal takeRoot(int root, BigDecimal n,
     BigDecimal maxError) {
     int MAXITER = MAX_ROOT_ITERS;

     // Specify the starting value in the search for the cube root.
     BigDecimal x;
     x = new BigDecimal("1");

     BigDecimal prevX = null;

     BigDecimal rootBD = new BigDecimal(root);
     // Search for the cube root via the Newton-Raphson loop. Output each successive iteration's value.
     int i=0;
     for (i = 0; i < MAXITER; ++i) {
     x = x.subtract(x.pow(root)
     .subtract(n)
     .divide(rootBD.multiply(x.pow(root - 1)), DIV_PRECISION,
     BigDecimal.ROUND_HALF_UP));
     if (prevX != null && prevX.subtract(x).abs().compareTo(maxError) < 0) {
     break;
     }
     prevX = x;
     }

     return x;
     }
     */
    /**
     * Takes the root-th root of a to at least eps precision.
     *
     * @param root
     * @param a
     * @param eps
     * @return
     */
    public static BigDecimal takeRoot(int root, BigDecimal a, BigDecimal eps) {
        BigDecimal nth = BigDecimal.ONE.divide(new BigDecimal(root + ""),
                DIV_PRECISION,
                BigDecimal.ROUND_HALF_UP);
        BigDecimal x = BigDecimal.ONE;
        int i;
        for (i = 0; i < MAX_ROOT_ITERS; i++) {
            BigDecimal err = nth.multiply(a.divide(x.pow(root - 1),
                    DIV_PRECISION, BigDecimal.ROUND_HALF_UP).subtract(x));
            if (err.abs().compareTo(eps) < 0) {
                return x;
            }
            x = x.add(err);
        }
        return x;

    }

    /**
     * cache for sqrt of this number
     */
    private ComplexNumber sqrt = null;

    /**
     * Sqrt of a complex number, caches for later.
     *
     * @return
     */
    public ComplexNumber sqrt() {
        if (sqrt == null) {
            //general case, works as long as this does't represent a real negative
            if (real.compareTo(BigDecimal.ZERO) >= 0 || isImaginary()) {
                ComplexNumber r = new ComplexNumber(abs(), BigDecimal.ZERO);
                ComplexNumber numer = add(r);
                BigDecimal denom = numer.abs();
                sqrt = numer.mult(takeRoot(2, r.real).divide(denom,
                        DIV_PRECISION,
                        BigDecimal.ROUND_HALF_UP));
            } else {
                //real negative case
                sqrt = new ComplexNumber(BigDecimal.ZERO, takeRoot(2, real.
                        negate()));
            }
        }
        return sqrt;
    }

    /**
     * Returns true if the imaginary component is ~0
     *
     * @return
     */
    public boolean isImaginary() {
        return imaginary.abs().compareTo(new BigDecimal("0.0001")) > 0;
    }

    /**
     * Divides by scalar.
     *
     * @param dd
     * @return
     */
    public ComplexNumber div(double dd) {
        BigDecimal d = new BigDecimal(dd + "");
        return new ComplexNumber(real.divide(d, DIV_PRECISION,
                BigDecimal.ROUND_HALF_UP), imaginary.divide(d, DIV_PRECISION,
                        BigDecimal.ROUND_HALF_UP));
    }

    /**
     * cache cube root, probably not needed.
     */
    private ComplexNumber cbrt = null;

    /**
     * Calculate primary cube root.
     *
     * @return
     */
    public ComplexNumber cbrt() {
        if (cbrt == null) {
            cbrt = cbrt(0);
        }
        return cbrt;
    }

    /**
     * Sin method for big decimal.
     *
     * @param bd
     * @return
     */
    public static BigDecimal sin(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.sin(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * cos method for big decimal.
     *
     * @param bd
     * @return
     */
    public static BigDecimal cos(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.cos(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * tan method for big decimal.
     *
     * @param bd
     * @return
     */
    public static BigDecimal tan(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.tan(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * asin for big decimal
     *
     * @param bd
     * @return
     */
    public static BigDecimal asin(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.asin(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * acos for big decimal
     *
     * @param bd
     * @return
     */
    public static BigDecimal acos(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.acos(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * atan for big decimal
     *
     * @param bd
     * @return
     */
    public static BigDecimal atan(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.atan(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * atan2 for big decimals
     *
     * @param bd1
     * @param bd2
     * @return
     */
    public static BigDecimal atan2(BigDecimal bd1, BigDecimal bd2) {
        double d1 = bd1.doubleValue();
        double d2 = bd2.doubleValue();
        BigDecimal ret = new BigDecimal(Math.atan2(d1, d2) + "");
        return ret.setScale(bd1.scale() > bd2.scale() ? bd1.scale() : bd2
                .scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * calculates cube root for this number there are three possible roots inum
     * chooses which.
     *
     * @param inum
     * @return
     */
    public ComplexNumber cbrt(int inum) {
        BigDecimal num = new BigDecimal(inum);
        BigDecimal pi = new BigDecimal(Math.PI + "");
        BigDecimal angle = atan2(imaginary, real);
        BigDecimal len = abs();
        BigDecimal r = takeRoot(3, len);
        BigDecimal nangle = new BigDecimal("2").multiply(pi).multiply(num.
                divide(new BigDecimal("3.0"), DIV_PRECISION,
                        BigDecimal.ROUND_HALF_UP)).add(angle.divide(
                                new BigDecimal("3.0"), DIV_PRECISION,
                                BigDecimal.ROUND_HALF_UP));
        return new ComplexNumber(r.multiply(cos(nangle)), r.
                multiply(sin(nangle)));
    }

    /**
     * for debugging.
     *
     * @return
     */
    @Override
    public String toString() {
        char add = '+';
        if (imaginary.compareTo(BigDecimal.ZERO) < 0) {
            add = '-';
        }
        return real + "" + add + "" + imaginary.abs() + "i";
    }

    private static final Logger LOG
            = Logger.getLogger(ComplexNumber.class.getName());

}
