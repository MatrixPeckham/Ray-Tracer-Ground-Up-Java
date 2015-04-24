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
package com.matrixpeckham.raytracer.cameras;

import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;

/**
 *
 * @author William Matrix Peckham
 */
public class Spherical extends Camera {

    private double psiMax = 90;
    private double lambdaMax = 90;

    public Spherical() {

    }

    public Spherical(Spherical aThis) {
        super(aThis);
        psiMax = aThis.psiMax;
        lambdaMax = aThis.lambdaMax;
    }

    @Override
    public void renderScene(World w) {
        RGBColor L = new RGBColor();
        ViewPlane vp = new ViewPlane(w.vp);
        int hres = vp.hRes;
        int vres = vp.vRes;
        double s = vp.s;
        Ray ray = new Ray();
        int depth = 0;
        Point2D sp = new Point2D(); 					// sample point in [0, 1] X [0, 1]
        Point2D pp = new Point2D();						// sample point on the pixel
        DoubleRef r_squared = new DoubleRef();				// sum of squares of normalised device coordinates

        ray.o.setTo(eye);

        for (int r = 0; r < vres; r++) // up
        {
            for (int c = 0; c < hres; c++) {	// across 					
                L.setTo(Utility.BLACK);

                for (int j = 0; j < vp.numSamples; j++) {
                    sp.setTo(vp.sampler.sampleUnitSquare());
                    pp.x = s * (c - 0.5 * hres + sp.x);
                    pp.y = s * (r - 0.5 * vres + sp.y);
                    ray.d.setTo(rayDirection(pp, hres, vres, s, r_squared));

                    //if (r_squared.d <= 1.0)
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }

                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
    }

    @Override
    public Camera clone() {
        return new Spherical(this);
    }

    private Vector3D rayDirection(Point2D pp, int hres, int vres, double s,
            DoubleRef r_squared) {
        Point2D pn = new Point2D(2.0 / (s * hres) * pp.x, 2.0 / (s * vres)
                * pp.y);
        double lambda = pn.x*lambdaMax*Utility.PI_ON_180;
        double psi = pn.y*psiMax*Utility.PI_ON_180;
        double phi = Utility.PI-lambda;
        double theta = 0.5 * Utility.PI-psi;
        
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double sinTheta=Math.sin(theta);
        double cosTheta=Math.cos(theta);
        
        Vector3D dir = (u.mul(sinTheta * sinPhi)
                .add(v.mul(cosTheta)))
                .add(w.mul(cosPhi*sinTheta));
        return dir;
    }

    public void setVerticalFov(double d) {
        psiMax = d / 2;
    }

    public void setHorizontalFov(double d) {
        lambdaMax = d / 2;
    }

}
