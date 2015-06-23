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
public class BruteForceSolver {

    final ComplexNumber a, b, c, d, e;

    public BruteForceSolver(double a, double b, double c, double d, double e) {
        this.a = new ComplexNumber(a, 0);
        this.b = new ComplexNumber(b, 0);
        this.c = new ComplexNumber(c, 0);
        this.d = new ComplexNumber(d, 0);
        this.e = new ComplexNumber(e, 0);
    }

    public int solveQuartic(double[] ret) {
        int roots = 0;
        if (delt().real.doubleValue() > 0) {
            if (P().real.doubleValue() > 0 || D().real.doubleValue() > 0) {
                return 0;
            }
        }
        ComplexNumber nbo4a = b.negate().div(a.mult(4));
        ComplexNumber S = S();
        ComplexNumber n4s2 = S.mult(S).mult(-4);
        ComplexNumber twop = p().mult(2);
        ComplexNumber qos = q().div(S);
        ComplexNumber r12SqrtInner = n4s2.sub(twop).add(qos);
        ComplexNumber r34SqrtInner = n4s2.sub(twop).sub(qos);
        ComplexNumber root1 = nbo4a.sub(S).add(r12SqrtInner.sqrt().mult(0.5));
        if (!root1.isImaginary()) {
            ret[roots] = root1.real.doubleValue();
            roots++;
        }
        ComplexNumber root2 = nbo4a.sub(S).sub(r12SqrtInner.sqrt().mult(0.5));
        if (!root2.isImaginary()) {
            ret[roots] = root2.real.doubleValue();
            roots++;
        }
        ComplexNumber root3 = nbo4a.add(S).add(r34SqrtInner.sqrt().mult(0.5));
        if (!root3.isImaginary()) {
            ret[roots] = root3.real.doubleValue();
            roots++;
        }
        ComplexNumber root4 = nbo4a.add(S).sub(r34SqrtInner.sqrt().mult(0.5));
        if (!root4.isImaginary()) {
            ret[roots] = root4.real.doubleValue();
            roots++;
        }

        return roots;
    }
    ComplexNumber S = null;

    ComplexNumber S() {
        if (S != null) {
            return S;
        }
        ComplexNumber p = p();
        ComplexNumber n2o3p = p.mult((-2.0 / 3.0));
        ComplexNumber Q = Q();
        ComplexNumber delt0 = delt0();
        ComplexNumber d0oq = delt0.div(Q);
        ComplexNumber qpd0q = Q.add(d0oq);
        ComplexNumber oneo3a = new ComplexNumber(1, 0).div(a.mult(3));
        ComplexNumber second = oneo3a.mult(qpd0q);
        ComplexNumber sqrtInner = n2o3p.add(second);
        ComplexNumber sqrt = sqrtInner.sqrt();
        if (sqrt.abs().compareTo(new BigDecimal("0.001")) < 0) {
            Q = Q(1);
            delt0 = delt0();
            d0oq = delt0.div(Q);
            qpd0q = Q.add(d0oq);
            oneo3a = new ComplexNumber(1, 0).div(a.mult(3));
            second = oneo3a.mult(qpd0q);
            sqrtInner = n2o3p.add(second);
            sqrt = sqrtInner.sqrt();
        }
        S = sqrt.mult(0.5);
        return S;
    }
    ComplexNumber D = null;

    ComplexNumber D() {
        if (D != null) {
            return D;
        }
        ComplexNumber a3 = a.mult(a).mult(a);
        ComplexNumber a2 = a.mult(a);
        ComplexNumber c2 = c.mult(c);
        ComplexNumber b2 = b.mult(b);
        ComplexNumber b4 = b.mult(b).mult(b).mult(b);
        D = a3.mult(64).mult(e).sub(a2.mult(16).mult(c2)).add(a.mult(16).
                mult(b2).mult(c)).sub(a2.mult(16).mult(b).mult(d)).sub(b4.
                        mult(3));
        return D;
    }
    ComplexNumber P = null;

    ComplexNumber P() {
        if (P != null) {
            return P;
        }
        P = a.mult(c).mult(8).sub(b.mult(b).mult(3));
        return P;
    }
    ComplexNumber Delt0 = null;

    ComplexNumber delt0() {
        if (Delt0 == null) {
            Delt0 = c.mult(c).sub(b.mult(d).mult(3)).add(a.mult(e).mult(12));
        }
        return Delt0;
    }
    ComplexNumber Q = null;

    ComplexNumber Q() {
        if (Q == null) {
            Q = Q(0);
        }
        return Q;
    }

    ComplexNumber Q(int n) {
        ComplexNumber delt1 = delt1();
        ComplexNumber delt0 = delt0();
        ComplexNumber innersqrt = delt1.mult(delt1).sub(delt0.mult(delt0).mult(
                delt0).mult(4));
        ComplexNumber sqrtD = innersqrt.sqrt();
        ComplexNumber numer = delt1.add(sqrtD);
        ComplexNumber denom = new ComplexNumber(2, 0);
        ComplexNumber frac = numer.div(denom);
        return frac.cbrt(n);
    }

    ComplexNumber Delt = null;

    ComplexNumber delt() {
        if (Delt == null) {
            ComplexNumber de0 = delt0();
            ComplexNumber de1 = delt1();
            ComplexNumber de12 = de1.mult(de1);
            ComplexNumber de03 = de0.mult(de0).mult(de0);
            ComplexNumber det = de12.sub(de03.mult(4));
            det = det.div(-27);
            Delt = det;
        }
        return Delt;
//        return t1 - t2 - t3 + t4 - t5
        //              + t6 - t7 - t8 + t9 + t10
        //            - t11 - t12 + t13 - t14 - t15 + t16;
    }
    ComplexNumber Delt1 = null;

    ComplexNumber delt1() {
        if (Delt1 == null) {
            ComplexNumber t1 = c.mult(c.mult(c)).mult(2);;
            ComplexNumber t2 = b.mult(c.mult(d)).mult(9);
            ComplexNumber t3 = b.mult(b.mult(e)).mult(27);
            ComplexNumber t4 = a.mult(d.mult(d)).mult(27);
            ComplexNumber t5 = a.mult(c.mult(e)).mult(72);
            Delt1 = t1.sub(t2).add(t3).add(t4).sub(t5);
        }
        return Delt1;
    }
    ComplexNumber p=null;
    ComplexNumber p() {
        if(p==null){
        ComplexNumber b2 = b.mult(b);
        ComplexNumber a2 = a.mult(a);
        ComplexNumber numer = a.mult(c).mult(8).sub(b2.mult(3));
        ComplexNumber denom = a2.mult(8);
        p = numer.div(denom);
        }
        return p;
    }

    ComplexNumber q() {
        ComplexNumber a2 = a.mult(a);
        ComplexNumber b3 = b.mult(b.mult(b));
        ComplexNumber a3 = a.mult(a.mult(a));
        ComplexNumber numer = b3.sub(a.mult(b).mult(c).mult(4)).add(a2.mult(d).
                mult(8));
        ComplexNumber denom = a3.mult(8);
        ComplexNumber frac = numer.div(denom);
        return frac;
    }
}
