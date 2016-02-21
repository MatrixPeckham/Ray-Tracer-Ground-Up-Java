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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.brdfs.PerfectSpecular;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Reflective class, for perfectly reflective surfaces
 *
 * @author William Matrix Peckham
 */
public class Reflective extends Phong {

    /**
     * perfect reflecting BRDF
     */
    private PerfectSpecular perfectBRDF;

    /**
     * default constructor
     */
    public Reflective() {
        super();
        perfectBRDF = new PerfectSpecular();
    }

    /**
     * copy constructor
     *
     * @param r
     */
    public Reflective(Reflective r) {
        super(r);
        perfectBRDF = r.perfectBRDF.clone();
    }

    /**
     * clone
     */
    @Override
    public Material clone() {
        return new Reflective(this);
    }

    /**
     * set reflector color
     *
     * @param c
     */
    public void setCr(RGBColor c) {
        perfectBRDF.setCr(c);
    }

    /**
     * sets reflectivity
     *
     * @param k
     */
    public void setKr(double k) {
        perfectBRDF.setKr(k);
    }

    /**
     * Shade function for reflective
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {

        //phong 
        RGBColor L = super.shade(sr);

        //camera direction
        Vector3D wo = sr.ray.d.neg();

        //reflected direction
        Vector3D wi = new Vector3D();

        //sample
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);
        //ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //recurse and calculate color
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1).mul(
                sr.normal.dot(wi))));

        return L;
    }

    /**
     * path shade function
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        //color
        RGBColor L = new RGBColor();

        //from camera
        Vector3D wo = sr.ray.d.neg();

        //reflected direction
        Vector3D wi = new Vector3D();

        //sample
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);

        //ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //recurse and calculate color
        L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1).mul(
                sr.normal.dot(wi))));

        return L;
    }

    /**
     * global shade
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        //color
        RGBColor L = new RGBColor();

        //camera direction
        Vector3D wo = sr.ray.d.neg();

        //reflected direction
        Vector3D wi = new Vector3D();

        //sample
        RGBColor fr = perfectBRDF.sampleF(sr, wo, wi);

        //ray
        Ray reflectedRay = new Ray(sr.hitPoint, wi);

        //area light hack
        if (sr.depth == 0) {
            L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 2).
                    mul(sr.normal.dot(wi))));
        } else {
            L.addLocal(fr.mul(sr.w.tracer.traceRay(reflectedRay, sr.depth + 1).
                    mul(sr.normal.dot(wi))));
        }

        return L;
    }

    public void setCr(double d, double d0, double d1) {
        setCr(new RGBColor(d, d0, d1));
    }

}
