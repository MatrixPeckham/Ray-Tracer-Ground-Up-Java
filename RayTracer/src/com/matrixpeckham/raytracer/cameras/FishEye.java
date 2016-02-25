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
 * Fish eye camera.
 *
 * @author William Matrix Peckham
 */
public class FishEye extends Camera {

    private double psiMax = 90;//view angle
    private boolean rectangular = false;//circle or square

    /**
     * default constructor
     */
    public FishEye() {

    }

    /**
     * copy constructor
     *
     * @param aThis
     */
    public FishEye(FishEye aThis) {
        super(aThis);
        psiMax = aThis.psiMax;
    }

    /**
     * render scene function
     *
     * @param w
     */
    @Override
    public void renderScene(World w) {
        //we map points on image to angles and project them
        RGBColor L = new RGBColor();
        ViewPlane vp = new ViewPlane(w.vp);
        int hres = vp.hRes;
        int vres = vp.vRes;
        double s = vp.s;
        Ray ray = new Ray();
        int depth = 0;
        Point2D sp = new Point2D();// sample point in [0, 1] X [0, 1]
        Point2D pp = new Point2D();// sample point on the pixel
        DoubleRef r_squared = new DoubleRef();// sum of squares of normalised device coordinates

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

                    if (rectangular || r_squared.d <= 1.0) {
                        L.addLocal(w.tracer.traceRay(ray, depth));
                    }
                }

                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c, L);
            }
        }
    }

    /**
     * stereo render function
     *
     * @param w
     * @param x
     * @param i
     */
    @Override
    public void renderStereo(World w, double x, int i) {
        RGBColor L = new RGBColor();
        ViewPlane vp = new ViewPlane(w.vp);
        int hres = vp.hRes;
        int vres = vp.vRes;
        double s = vp.s;
        Ray ray = new Ray();
        int depth = 0;
        Point2D sp = new Point2D();// sample point in [0, 1] X [0, 1]
        Point2D pp = new Point2D();// sample point on the pixel
        DoubleRef r_squared = new DoubleRef();// sum of squares of normalised device coordinates

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

                    if (rectangular || r_squared.d <= 1.0) {
                        L.addLocal(w.tracer.traceRay(ray, depth));
                    }
                }

                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                w.displayPixel(r, c + i, L);
            }
        }
    }

    /**
     * clone function
     *
     * @return
     */
    @Override
    public Camera clone() {
        return new FishEye(this);
    }

    /**
     * gets the ray direction for the given parameters
     *
     * @param pp
     * @param hres
     * @param vres
     * @param s
     * @param r_squared
     * @return
     */
    private Vector3D rayDirection(Point2D pp, int hres, int vres, double s,
            DoubleRef r_squared) {
        Point2D pn = new Point2D(2.0 / (s * hres) * pp.x, 2.0 / (s * vres)
                * pp.y);
        r_squared.d = pn.x * pn.x + pn.y * pn.y;
        if (rectangular || r_squared.d <= 1.0) {
            double r = Math.sqrt(r_squared.d);
            double psi = r * psiMax * Utility.PI_ON_180;
            double sinPsi = Math.sin(psi);
            double cosPsi = Math.cos(psi);
            double sinAlpha = pn.y / r;
            double cosAlpha = pn.x / r;
            Vector3D dir = (u.mul(sinPsi * cosAlpha)
                    .add(v.mul(sinPsi * sinAlpha)))
                    .sub(w.mul(cosPsi));
            return dir;
        } else {
            return new Vector3D(0);
        }
    }

    /**
     * sets field of view
     *
     * @param d
     */
    public void setFov(double d) {
        psiMax = d / 2;
    }

    /**
     * sets rectangular field
     *
     * @param b
     */
    public void setRectangular(boolean b) {
        rectangular = b;
    }

}
