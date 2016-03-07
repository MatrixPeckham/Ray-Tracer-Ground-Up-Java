/*
 * Copyright (C) 2016 William Matrix Peckham
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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.brdfs.FresnelReflector;
import com.matrixpeckham.raytracer.btdf.FresnelTransmitter;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Realistic transparency with color filtering.
 *
 * @author William Matrix Peckham
 */
public class Dielectric extends Phong {

    /**
     * inside color
     */
    private final RGBColor cfIn = new RGBColor(1);

    /**
     * outside color
     */
    private final RGBColor cfOut = new RGBColor(1);

    /**
     * reflective BRDF
     */
    FresnelReflector fresnelBRDF;

    /**
     * BTDF for transparency
     */
    FresnelTransmitter fresnelBTDF;

    /**
     * default constructor
     */
    public Dielectric() {
        fresnelBRDF = new FresnelReflector();
        fresnelBTDF = new FresnelTransmitter();
    }

    /**
     * copy Constructor
     *
     * @param cp
     */
    public Dielectric(Dielectric cp) {
        super(cp);
        fresnelBRDF = new FresnelReflector(cp.fresnelBRDF);
        fresnelBTDF = new FresnelTransmitter(cp.fresnelBTDF);
        cfIn.setTo(cp.cfIn);
        cfOut.setTo(cp.cfOut);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material cloneMaterial() {
        return new Dielectric(this);
    }

    /**
     * shade function
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        RGBColor L = super.shade(sr);//get phong shade, for specular reflections

        //sample reflected ray
        final Vector3D wi = new Vector3D();

        //ray from camera
        Vector3D wo = new Vector3D(sr.ray.d.neg());

        //sample reflection
        RGBColor fr = fresnelBRDF.sampleF(sr, wo, wi);

        //ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //reflection color
        final RGBColor Lr = new RGBColor();

        //transmission color
        final RGBColor Lt = new RGBColor();

        //cosine
        double ndotwi = sr.normal.dot(wi);

        //intersection ray parameter
        DoubleRef t = new DoubleRef(Utility.HUGE_VALUE);

        if (fresnelBTDF.tir(sr)) {//total internal reflection
            //trace only one reflected ray, filter with proper color
            if (ndotwi < 0) {
                //inside
                Lr.setTo(sr.w.tracer.traceRay(reflectedRay, t, sr.depth + 1));
                L.addLocal(cfIn.powc(t.d).mul(Lr));
            } else {
                //reflected outside
                Lr.setTo(sr.w.tracer.traceRay(reflectedRay, t, sr.depth + 1));
                L.addLocal(cfOut.powc(t.d).mul(Lr));
            }
        } else {
            //refracted ray
            Vector3D wt = new Vector3D();

            //sample transmission
            RGBColor ft = fresnelBTDF.sampleF(sr, wo, wt);

            //ray
            Ray transmittedRay = new Ray(sr.hitPoint, wt);

            //cosine term
            double ndotwt = sr.normal.dot(wt);

            if (ndotwi < 0) {
                //reflect inside
                Lr.setTo(sr.w.tracer.traceRay(reflectedRay, t, sr.depth + 1).
                        mul(Math.abs(ndotwi)).mul(fr));
                L.addLocal(Lr.mul(cfIn.powc(t.d)));
                //transmit outside
                Lt.setTo(sr.w.tracer.traceRay(transmittedRay, t, sr.depth + 1).
                        mul(Math.abs(ndotwt)).mul(ft));
                L.addLocal(Lt.mul(cfOut.powc(t.d)));
            } else {
                //reflect outside
                Lr.setTo(sr.w.tracer.traceRay(reflectedRay, t, sr.depth + 1).
                        mul(Math.abs(ndotwi)).mul(fr));
                L.addLocal(Lr.mul(cfOut.powc(t.d)));
                //transmit inside
                Lt.setTo(sr.w.tracer.traceRay(transmittedRay, t, sr.depth + 1).
                        mul(Math.abs(ndotwt)).mul(ft));
                L.addLocal(Lt.mul(cfIn.powc(t.d)));
            }

        }

        return L;
    }

    /**
     * sets the interior index of refraction
     *
     * @param d
     */
    public void setIorIn(double d) {
        fresnelBRDF.setIorIn(d);
        fresnelBTDF.setIorIn(d);
    }

    /**
     * sets the exterior index of refraction
     *
     * @param d
     */
    public void setIorOut(double d) {
        fresnelBRDF.setIorOut(d);
        fresnelBTDF.setIorOut(d);
    }

    /**
     * sets the interior color
     *
     * @param c
     */
    public void setCfIn(RGBColor c) {
        cfIn.setTo(c);
    }

    /**
     * sets the exterior color
     *
     * @param c
     */
    public void setCfOut(RGBColor c) {
        cfOut.setTo(c);
    }

    /**
     * sets the diffuse color that the specular shading will use
     *
     * @param brown
     */
    @Override
    public void setCd(RGBColor brown) {
        super.setCd(brown); //To change body of generated methods, choose Tools | Templates.
        //cfIn.setTo(brown);
    }

    /**
     * sets the interior color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCfIn(double r, double g, double b) {
        setCfIn(new RGBColor(r, g, b));
    }

    /**
     * sets the interior color gray
     *
     * @param r
     */
    public void setCfIn(double r) {
        setCfIn(new RGBColor(r, r, r));
    }

    /**
     * sets the exterior color
     *
     * @param r
     * @param g
     * @param b
     */
    public void setCfOut(double r, double g, double b) {
        setCfOut(new RGBColor(r, g, b));
    }

    /**
     * sets the exterior color gray
     *
     * @param r
     */
    public void setCfOut(double r) {
        setCfOut(new RGBColor(r, r, r));
    }

    //TODO: this class does not yet implement path/global tracing
    private static final Logger LOG
            = Logger.getLogger(Dielectric.class.getName());

}
