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

/**
 *
 * @author William Matrix Peckham
 */
public class ComplexNumber {

    public static final int DIV_PRECISION = 50;
    public static final BigDecimal ROOT_PRECISION = new BigDecimal(
            "0.0000000001");
    public final BigDecimal real;
    public final BigDecimal imaginary;
    private BigDecimal abs = null;

    public ComplexNumber(double r, double i) {
        real = new BigDecimal(r + "");
        imaginary = new BigDecimal(i + "");
    }

    public ComplexNumber(BigDecimal r, BigDecimal i) {
        real = r;
        imaginary = i;
    }

    public ComplexNumber negate() {
        return new ComplexNumber(real.negate(), imaginary.negate());
    }

    public ComplexNumber mult(double d) {
        return new ComplexNumber(real.multiply(new BigDecimal(d + "")),
                imaginary.multiply(new BigDecimal(d + "")));
    }

    public ComplexNumber mult(BigDecimal d) {
        return new ComplexNumber(real.multiply(d), imaginary.multiply(d));
    }

    public ComplexNumber mult(ComplexNumber o) {
        BigDecimal rr = real.multiply(o.real);
        BigDecimal ri = real.multiply(o.imaginary);
        BigDecimal ir = imaginary.multiply(o.real);
        BigDecimal ii = imaginary.multiply(o.imaginary).negate();
        BigDecimal r = rr.add(ii);
        BigDecimal i = ri.add(ir);
        return new ComplexNumber(r, i);
    }

    public ComplexNumber add(ComplexNumber o) {
        return new ComplexNumber(real.add(o.real), imaginary.add(o.imaginary));
    }

    public ComplexNumber sub(ComplexNumber o) {
        return new ComplexNumber(real.subtract(o.real), imaginary.subtract(
                o.imaginary));
    }

    public ComplexNumber congigate() {
        return new ComplexNumber(real, imaginary.negate());
    }

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

    public BigDecimal abs() {
        if (abs == null) {
            abs = takeRoot(2, real.multiply(real).add(imaginary.multiply(
                    imaginary)));
        }
        return abs;
        //return Math.sqrt(real*real+imaginary*imaginary);
    }

    public BigDecimal takeRoot(int root, BigDecimal n) {
        return takeRoot(root, n, ROOT_PRECISION);
    }

    public static BigDecimal takeRoot(int root, BigDecimal n,
            BigDecimal maxError) {
        int MAXITER = 5000;

        // Specify the starting value in the search for the cube root.
        BigDecimal x;
        x = new BigDecimal("1");

        BigDecimal prevX = null;

        BigDecimal rootBD = new BigDecimal(root);
        // Search for the cube root via the Newton-Raphson loop. Output each successive iteration's value.
        for (int i = 0; i < MAXITER; ++i) {
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
    
    private ComplexNumber sqrt = null;
    
    public ComplexNumber sqrt() {
        if(sqrt==null){
            if (real.compareTo(BigDecimal.ZERO) >= 0 || isImaginary()) {
                ComplexNumber r = new ComplexNumber(abs(), BigDecimal.ZERO);
                ComplexNumber numer = add(r);
                BigDecimal denom = numer.abs();
                sqrt = numer.mult(takeRoot(2, r.real).divide(denom, DIV_PRECISION,
                    BigDecimal.ROUND_HALF_UP));
            } else {
                sqrt = new ComplexNumber(BigDecimal.ZERO, takeRoot(2, real.negate()));
            }
        }
        return sqrt;
    }

    public boolean isImaginary() {
        return imaginary.abs().compareTo(new BigDecimal("0.0001")) > 0;
    }

    public ComplexNumber div(double dd) {
        BigDecimal d = new BigDecimal(dd + "");
        return new ComplexNumber(real.divide(d, DIV_PRECISION,
                BigDecimal.ROUND_HALF_UP), imaginary.divide(d, DIV_PRECISION,
                        BigDecimal.ROUND_HALF_UP));
    }

    private ComplexNumber cbrt=null;
    public ComplexNumber cbrt() {
        if(cbrt==null){
            cbrt=cbrt(0);
        }
        return cbrt;
    }

    public static BigDecimal sin(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.sin(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal cos(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.cos(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal tan(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.tan(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal asin(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.asin(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal acos(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.acos(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal atan(BigDecimal bd) {
        double d = bd.doubleValue();
        BigDecimal ret = new BigDecimal(Math.atan(d) + "");
        return ret.setScale(bd.scale(), BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal atan2(BigDecimal bd1, BigDecimal bd2) {
        double d1 = bd1.doubleValue();
        double d2 = bd2.doubleValue();
        BigDecimal ret = new BigDecimal(Math.atan2(d1, d2) + "");
        return ret.setScale(bd1.scale() > bd2.scale() ? bd1.scale() : bd2
                .scale(), BigDecimal.ROUND_HALF_EVEN);
    }

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

    @Override
    public String toString() {
        char add = '+';
        if (imaginary.compareTo(BigDecimal.ZERO) < 0) {
            add = '-';
        }
        return real + "" + add + "" + imaginary.abs() + "i";
    }
}
