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
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Glossy Specular class, provides specular highlights without sampling and
 * glossed reflection with recursive sampling
 *
 * @author William Matrix Peckham
 */
public class GlossySpecular extends BRDF {

    /**
     * reflectance
     */
    private double ks = 0;

    /**
     * color
     */
    private final RGBColor cs = new RGBColor(1);

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
    public GlossySpecular() {
        super();
    }

    /**
     * copy construction
     *
     * @param gs
     */
    public GlossySpecular(GlossySpecular gs) {
        super(gs);
        ks = gs.ks;
        cs.setTo(gs.cs);
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
    public GlossySpecular cloneBRDF() {
        return new GlossySpecular(this);
    }

    /**
     * sampler setter
     *
     * @param s
     * @param exp
     */
    public void setSampler(Sampler s, double exp) {
        sampler = s;
        sampler.mapSamplesToHemisphere(exp);
    }

    /**
     * sampler setter
     *
     * @param num
     * @param exp
     */
    public void setSamples(int num, double exp) {
        sampler = new MultiJittered(num);
        sampler.mapSamplesToHemisphere(exp);
    }

    /**
     * f function, called from non-recursive shading functions, computes the
     * highlight at hit point viewed from wo and illuminated from wi.
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
        if (rdotwo > 0) {
            l.setTo(cs.mul(ks * Math.pow(rdotwo, exp)));
        }
        return l;
    }

    /**
     * sampling function returns color that will be multiplied by reflected ray,
     * reflected ray is sampled from the cone around the perfect reflection.
     * reflected ray stored in wi, pdf stored in reference
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
        if (sr.normal.dot(wi) < 0.0) {
            wi.setTo(u.mul(-sp.x).add(v.mul(-sp.y).add(w.mul(sp.z))));
        }
        double phong_lobe = Math.pow(r.dot(w), exp);
        pdf.d = phong_lobe * sr.normal.dot(wi);
        return cs.mul(ks * phong_lobe);
    }

    /**
     * rho, color of lit point, ambient, black
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
    public void setCs(RGBColor c) {
        cs.setTo(c);
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCs(double r, double g, double b) {
        cs.setTo(r, g, b);
    }

    /**
     * setter
     *
     * @param c
     */
    public void setCs(double c) {
        cs.setTo(c, c, c);
    }

}
