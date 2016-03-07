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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Fresnel reflector BRDF, uses the fresnel equation for reflection.
 *
 * @author William Matrix Peckham
 */
public class FresnelReflector extends BRDF {

    /**
     * index of refraction interior
     */
    private double iorIn = 1;

    /**
     * index of refraction exterior
     */
    private double iorOut = 1;

    /**
     * default constructor
     */
    public FresnelReflector() {
    }

    /**
     * copy constructor
     *
     * @param fresnelBRDF
     */
    public FresnelReflector(FresnelReflector fresnelBRDF) {
        super(fresnelBRDF);
        iorIn = fresnelBRDF.iorIn;
        iorOut = fresnelBRDF.iorOut;
    }

    /**
     * Samples distribution, returns color and reflection direction.
     *
     * @param sr
     * @param wo
     * @param wr
     * @return
     */
    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wr) {
        double ndotwo = sr.normal.dot(wo);
        wr.setTo(wo.neg().add(sr.normal.mul(ndotwo * 2)));
        return Utility.WHITE.mul(fresnel(sr) / Math.abs(sr.normal.dot(wr)));
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public BRDF cloneBRDF() {
        return new FresnelReflector(this);
    }

    /**
     * fresnel function, calculates reflectance
     *
     * @param sr
     * @return
     */
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
        //double temp = 1.0 - (1.0 - cos_thiori * cos_thiori) / (eta * eta);
        double cos_thiort = Math.sqrt(1.0 - (1.0 - cos_thiori * cos_thiori)
                / (eta * eta));
        double r_parallel = (eta * cos_thiori - cos_thiort) / (eta * cos_thiori
                + cos_thiort);
        double r_perpendicular = (cos_thiori - eta * cos_thiort) / (cos_thiori
                + eta * cos_thiort);
        double kr = 0.5 * (r_parallel * r_parallel + r_perpendicular
                * r_perpendicular);

        return (kr);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setIorIn(double d) {
        iorIn = d;
    }

    /**
     * setter
     *
     * @param d
     */
    public void setIorOut(double d) {
        iorOut = d;
    }

    private static final Logger LOG
            = Logger.getLogger(FresnelReflector.class.getName());

}
