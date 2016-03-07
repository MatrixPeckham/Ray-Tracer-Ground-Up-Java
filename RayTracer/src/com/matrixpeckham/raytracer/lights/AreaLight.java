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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Class represents an area light, it samples its object for some values.
 *
 * @author William Matrix Peckham
 */
public class AreaLight extends Light {

    /**
     * object that represents this light in the scene
     */
    private GeometricObject obj = null;

    /**
     * this should be an emissive material from the object
     */
    private Material material = null;

    /**
     * sample point that was chosen last, stored between calls to the other
     * methods
     */
    private final Point3D samplePoint = new Point3D();

    /**
     * normal at the sampled light point, stored between calls to the other
     * methods.
     */
    private final Normal lightNormal = new Normal();

    /**
     * the direction vector from the hit point to the light sample point, stored
     * between calls to methods
     */
    private final Vector3D wi = new Vector3D();

    /**
     * default constructor
     */
    public AreaLight() {
        super();
    }

    /**
     * copy constructor
     *
     * @param a
     */
    public AreaLight(AreaLight a) {
        super(a);
        if (a.obj != null) {
            obj = a.obj.cloneGeometry();
        }
        if (a.material != null) {
            material = a.material.cloneMaterial();
        }
    }

    /**
     * sets the object that represents this light
     *
     * @param obj
     */
    public void setObject(GeometricObject obj) {
        this.obj = obj;
        material = obj.getMaterial();
    }

    /**
     * clone method
     *
     * @return
     */
    @Override
    public Light cloneLight() {
        return new AreaLight(this);
    }

    /**
     * get the direction from the hit point to the light at the sampled point on
     * the object. this is where we store the values for later use.
     *
     * @param sr
     * @return
     */
    @Override
    public Vector3D getDirection(ShadeRec sr) {
        //sample object and store point for later use
        samplePoint.setTo(obj.sample());
        //gets the objects normal from the sample point and stores for later use
        lightNormal.setTo(obj.getNormal(samplePoint));
        //calculates the direction from the sample point to the hit point and
        //keeps it in a member variable for later use, also returned
        wi.setTo(samplePoint.sub(sr.hitPoint));
        wi.normalize();
        return wi;
    }

    /**
     * Radiance function, differs to material.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor L(ShadeRec sr) {
        //check for back face and return black if it is
        double ndotd = lightNormal.neg().dot(wi);
        if (ndotd > 0) {
            return material.getLe(sr);
        } else {
            return Utility.BLACK;
        }
    }

    /**
     * in shadow function
     *
     * @param ray
     * @param sr
     * @return
     */
    @Override
    public boolean inShadow(Ray ray, ShadeRec sr) {

        //distance to sample point on object
        double ts = (samplePoint.sub(ray.o).dot(ray.d));

        //reference parameter
        DoubleRef t = new DoubleRef();

        //loop through objects
        int numObjects = sr.w.objects.size();
        for (int j = 0; j < numObjects; j++) {
            //we have a hit and the hit is closer than the light and the
            //the material accepts shadows
            if (sr.w.objects.get(j).shadowHit(ray, t) && t.d < ts
                    && sr.material.getShadows()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Geometric term.
     *
     * @param sr
     * @return
     */
    @Override
    public double G(ShadeRec sr) {
        //cosine term
        double ndotd = lightNormal.neg().dot(wi);
        //distance to hit point from sample point squared
        double d2 = samplePoint.distSquared(sr.hitPoint);
        return ndotd / d2;
    }

    /**
     * differed to objects pdf function, usually inverse of surface area
     *
     * @param sr
     * @return
     */
    @Override
    public double pdf(ShadeRec sr) {
        return obj.pdf(sr);
    }

    private static final Logger LOG
            = Logger.getLogger(AreaLight.class.getName());

}
