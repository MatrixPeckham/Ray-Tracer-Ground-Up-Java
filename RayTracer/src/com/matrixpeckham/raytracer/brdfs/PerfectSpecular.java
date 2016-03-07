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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Mirror perfect specular.
 *
 * @author William Matrix Peckham
 */
public class PerfectSpecular extends BRDF {

    /**
     * reflectance
     */
    private double kr = 0;

    /**
     * color
     */
    private final RGBColor cr = new RGBColor(1);

    /**
     * default constructor
     */
    public PerfectSpecular() {
        super();
    }

    /**
     * copy constructor
     *
     * @param s
     */
    public PerfectSpecular(PerfectSpecular s) {
        super(s);
        kr = s.kr;
        cr.setTo(s.cr);
    }

    /**
     * setter
     *
     * @param k
     */
    public void setKr(double k) {
        kr = k;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCr(RGBColor c) {
        cr.setTo(c);
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCr(double r, double g, double b) {
        cr.setTo(r, g, b);
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCr(double c) {
        cr.setTo(c, c, c);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public PerfectSpecular cloneBRDF() {
        return new PerfectSpecular(this);
    }

    /**
     * f function, returns black, without a recursive ray it doesn't have a
     * color
     *
     * @param sr
     * @param wo
     * @param wi
     * @return
     */
    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return Utility.BLACK;
    }

    /**
     * sample f, returns color, stores reflected direction in wi
     *
     * @param sr
     * @param wo
     * @param wi
     * @return
     */
    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi) {
        double ndotwo = sr.normal.dot(wo);
        wi.setTo(wo.neg().add(new Vector3D(sr.normal.mul(2 * ndotwo))));
        return cr.div(Math.abs(sr.normal.dot(wi))).mul(kr);
    }

    /**
     * sample f, returns color, stores reflected direction in wi, and pdf if
     * reference
     *
     * @param sr
     * @param wo
     * @param wi
     * @param pdf
     * @return
     */
    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi, DoubleRef pdf) {
        double ndotwo = sr.normal.dot(wo);
        wi.setTo(wo.neg().add(new Vector3D(sr.normal.mul(2 * ndotwo))));
        pdf.d = Math.abs(sr.normal.dot(wi));
        return cr.mul(kr);
    }

    /**
     * rho, black
     *
     * @param sr
     * @param wo
     * @return
     */
    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return Utility.BLACK;
    }

}
