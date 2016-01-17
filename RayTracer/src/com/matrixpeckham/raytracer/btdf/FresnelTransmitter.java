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
public class FresnelTransmitter extends BTDF {

    private double iorIn = 1;
    private double iorOut = 1;

    public FresnelTransmitter() {
    }

    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wt) {
        Normal n = new Normal(sr.normal);
        double cosThetaI = n.dot(wo);
        double eta = iorIn / iorOut;
        if (cosThetaI < 0) {
            cosThetaI = -cosThetaI;
            n.setTo(n.neg());
            eta = 1 / eta;
        }
        double temp = 1 - (1 - cosThetaI * cosThetaI) / (eta * eta);
        double cosTheta2 = Math.sqrt(temp);
        wt.setTo(wo.neg().div(eta).sub(new Vector3D(n).mul(cosTheta2 - cosThetaI
                / eta)));
        double abs = Math.abs(sr.normal.dot(wt));
        double eta2 = eta * eta;
        double kte = fresnel(sr) / eta2;
        RGBColor mul = Utility.WHITE.mul(kte);
        return mul.div(abs);
    }

    public double fresnel(ShadeRec sr) {
        Normal normal = new Normal(sr.normal);
        double ndotd = normal.neg().dot(sr.ray.d);
        double eta;

        if (ndotd < 0.0) {
            normal.setTo(normal.neg());
            eta = iorOut / iorIn;
        } else {
            eta = iorIn / iorOut;
        }

        double cos_thiori = normal.neg().dot(sr.ray.d);
        double temp = 1.0 - (1.0 - cos_thiori * cos_thiori) / (eta * eta);
        double cos_thiort = Math.sqrt(1.0 - (1.0 - cos_thiori * cos_thiori)
                / (eta * eta));
        double r_parallel = (eta * cos_thiori - cos_thiort) / (eta * cos_thiori
                + cos_thiort);
        double r_perpendicular = (cos_thiori - eta * cos_thiort) / (cos_thiori
                + eta * cos_thiort);
        double kr = 0.5 * (r_parallel * r_parallel + r_perpendicular
                * r_perpendicular);

        return (1 - kr);
    }

    public FresnelTransmitter(FresnelTransmitter fresnelBTDF) {
        super(fresnelBTDF);
        iorIn = fresnelBTDF.iorIn;
        iorOut = fresnelBTDF.iorOut;
    }

    public boolean tir(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        double cosThetaI = sr.normal.dot(wo);
        double eta;
        double ndotd = sr.normal.neg().dot(sr.ray.d);
        if (ndotd < 0.0) {
            eta = iorOut / iorIn;
        } else {
            eta = iorIn / iorOut;
        }
        return (1 - (1 - cosThetaI * cosThetaI) / (eta * eta)) < 0;
    }

    @Override
    public BTDF clone() {
        return new FresnelTransmitter(this);
    }

    public void setIorOut(double d) {
        iorOut = d;
    }

    public void setIorIn(double d) {
        iorIn = d;
    }

}
