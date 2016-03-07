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

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Perfect specular that uses a texture for reflection color. same as perfect
 * specular but differs to texture for color.
 *
 * @author William Matrix Peckham
 */
public class SV_PerfectSpecular extends BRDF {

    /**
     * reflectance
     */
    private double kr = 0;

    /**
     * color texture
     */
    private Texture cr = null;

    /**
     * default constructor
     */
    public SV_PerfectSpecular() {
        super();
    }

    /**
     * copy constructor
     *
     * @param s
     */
    public SV_PerfectSpecular(SV_PerfectSpecular s) {
        super(s);
        kr = s.kr;
        cr = s.cr.cloneTexture();
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
    public void setCr(Texture c) {
        cr = c.cloneTexture();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public SV_PerfectSpecular cloneBRDF() {
        return new SV_PerfectSpecular(this);
    }

    /**
     * f function
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
     * sample function
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
        return cr.getColor(sr).div(Math.abs(sr.normal.dot(wi))).mul(kr);
    }

    /**
     * sample f
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
        return cr.getColor(sr).mul(kr);
    }

    /**
     * rho
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
