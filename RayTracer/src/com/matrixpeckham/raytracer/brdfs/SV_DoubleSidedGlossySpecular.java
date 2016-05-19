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

import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Glossy Specular that uses a texture for color. same as glossy specular, but
 * differs to texture for color
 *
 * @author William Matrix Peckham
 */
public class SV_DoubleSidedGlossySpecular extends BRDF {

    /**
     * multiplier
     */
    private double ks = 0;

    /**
     * color texture
     */
    private Texture cs = null;

    /**
     * exponent
     */
    private double exp = 2;

    /**
     * sampler
     */
    private Sampler sampler = null;

    /**
     * default constructor
     */
    public SV_DoubleSidedGlossySpecular() {
        super();
    }

    /**
     * copy constructor
     *
     * @param gs
     */
    public SV_DoubleSidedGlossySpecular(SV_DoubleSidedGlossySpecular gs) {
        super(gs);
        ks = gs.ks;
        if (gs.cs != null) {
            cs = gs.cs.cloneTexture();
        }
        exp = gs.exp;
        if (gs.sampler != null) {
            sampler = gs.sampler.cloneSampler();
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public SV_DoubleSidedGlossySpecular cloneBRDF() {
        return new SV_DoubleSidedGlossySpecular(this);
    }

    /**
     * setter
     *
     * @param s
     * @param exp
     */
    public void setSampler(Sampler s, double exp) {
        sampler = s;
        sampler.mapSamplesToHemisphere(exp);
    }

    /**
     * setter
     *
     * @param num
     * @param exp
     */
    public void setSamples(int num, double exp) {
        sampler = new MultiJittered(num);
        sampler.mapSamplesToHemisphere(exp);
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
        RGBColor l = new RGBColor();
        double ndotwi = sr.normal.dot(wi);
        Vector3D r = wi.neg().add(new Vector3D(sr.normal.mul(2 * ndotwi)));
        double rdotwo = r.dot(wo);
        l.setTo(cs.getColor(sr).mul(ks * Math.pow(rdotwo, exp)));
        return l;
    }

    /**
     * sample f function
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
        Vector3D r = wo.neg().add(new Vector3D(sr.normal.mul(2 * ndotwo)));
        Vector3D w = new Vector3D(r);
        Vector3D u = new Vector3D(0.00424, 1, 0.00764).cross(w);
        u.normalize();
        Vector3D v = u.cross(w);

        Point3D sp = sampler.sampleHemisphere();
        wi.setTo(u.mul(sp.x).add(v.mul(sp.y).add(w.mul(sp.z))));
        wi.setTo(u.mul(-sp.x).add(v.mul(-sp.y).add(w.mul(sp.z))));
        double phong_lobe = Math.pow(r.dot(w), exp);
        pdf.d = phong_lobe * sr.normal.dot(wi);
        return cs.getColor(sr).mul(ks * phong_lobe);
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

    /**
     * setter
     *
     * @param k
     */
    public void setKs(double k) {
        ks = k;
    }

    /**
     * setter
     *
     * @param e
     */
    public void setExp(double e) {
        exp = e;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCs(Texture c) {
        cs = c.cloneTexture();
    }

    private static final Logger LOG
            = Logger.getLogger(SV_DoubleSidedGlossySpecular.class.getName());

}
