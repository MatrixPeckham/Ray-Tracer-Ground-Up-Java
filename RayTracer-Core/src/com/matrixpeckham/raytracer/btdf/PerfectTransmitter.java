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
package com.matrixpeckham.raytracer.btdf;

import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class PerfectTransmitter extends BTDF {

    /**
     * transparency multiplier
     */
    private double kt;

    /**
     * index of refraction
     */
    private double ior;

    /**
     * default constructor
     */
    public PerfectTransmitter() {
        kt = 0;
        ior = 1;
    }

    /**
     * copy constructor
     *
     * @param t
     */
    public PerfectTransmitter(PerfectTransmitter t) {
        super(t);
        kt = t.kt;
        ior = t.ior;
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public PerfectTransmitter cloneBTDF() {
        return new PerfectTransmitter(this);
    }

    /**
     * sets ior
     *
     * @param ior
     */
    public void setIor(double ior) {
        this.ior = ior;
    }

    /**
     * set multiplier
     *
     * @param kt
     */
    public void setKt(double kt) {
        this.kt = kt;
    }

    /**
     * copy method
     *
     * @param o
     * @return
     */
    public BTDF setTo(PerfectTransmitter o) {
        super.setTo(o);
        kt = o.kt;
        ior = o.ior;
        return this;
    }

    /**
     * total internal reflection test
     *
     * @param sr
     * @return
     */
    public boolean tir(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        double cosThetaI = sr.normal.dot(wo);
        double eta = ior;
        if (cosThetaI < 0) {
            eta = 1 / eta;
        }
        return (1 - (1 - cosThetaI * cosThetaI) / (eta * eta)) < 0;
    }

    /**
     * never used
     *
     * @param sr
     * @param wo
     * @param wi
     * @return
     */
    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return new RGBColor(Utility.BLACK);
    }

    /**
     * sample f, gets the color of the object. sets wt to the direction of
     * transmission.
     *
     * @param sr
     * @param wo
     * @param wt
     * @return
     */
    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wt) {
        //clone the normal
        Normal n = new Normal(sr.normal);
        //gets the cos of the angle
        double cosThetaI = n.dot(wo);
        //use the index of refraction
        double eta = ior;
        //if ray is leaving material, we negate the angle, normal, and invert the ior
        if (cosThetaI < 0) {
            cosThetaI = -cosThetaI;
            n.setTo(n.neg());
            eta = 1 / eta;
        }
        //compute the angle of refraction
        double temp = 1 - (1 - cosThetaI * cosThetaI) / (eta * eta);
        double cosTheta2 = Math.sqrt(temp);
        //use it to generate transmitted ray
        wt.setTo(wo.neg().div(eta).sub(new Vector3D(n).mul(cosTheta2 - cosThetaI
                / eta)));
        //figure out the amount of contribution of the transmission.
        double abs = Math.abs(sr.normal.dot(wt));
        double eta2 = eta * eta;
        double kte = kt / eta2;
        RGBColor mul = Utility.WHITE.mul(kte);
        return mul.div(abs);
    }

    /**
     * rho, not used
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
