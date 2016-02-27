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

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;

/**
 * Orthographic camera.
 *
 * @author William Matrix Peckham
 */
public class Orthographic extends Camera {

    /**
     * Default.
     */
    public Orthographic() {
        super();
    }

    /**
     * Copy Constructor
     *
     * @param c
     */
    public Orthographic(Orthographic c) {
        super(c);
    }

    /**
     * Clone
     *
     * @return
     */
    @Override
    public Camera clone() {
        return new Orthographic(this);
    }

    /**
     * Gets the direction vector for traced rays, always the same for ortho.
     *
     * @param p point on the 2d plane
     * @return
     */
    public Vector3D getDirection(Point2D p) {
        Vector3D dir = (lookat.sub(eye));
        dir.normalize();
        return dir;
    }

    /**
     * Render scene.
     *
     * @param w
     */
    @Override
    public void renderScene(World w) {
        //color
        RGBColor L = new RGBColor();
        //copy of view plane.
        ViewPlane vp = new ViewPlane(w.vp);
        //ray
        Ray ray = new Ray();
        //depth
        int depth = 0;
        //pixel point
        Point2D pp = new Point2D();
        //normalized sample point
        Point2D sp = new Point2D();
        //loop through all pixels
        for (int r = 0; r < vp.vRes; r++) {
            for (int c = 0; c < vp.hRes; c++) {
                //this is a bad idea
                //initialize color
                L.setTo(0, 0, 0);
                //for all samples in point
                for (int p = 0; p < vp.sampler.getNumSamples(); p++) {
                    //sample point
                    sp.setTo(vp.sampler.sampleUnitSquare());
                    //convert normalized sample point to a point somewhere in the pixel
                    pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x);
                    pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                    //get ray direction
                    ray.d.setTo(getDirection(pp));
                    //set ray origin, eyepoint + pixel location
                    ray.o.setTo(eye.add(u.mul(pp.x).add(v.mul(pp.y))));
                    //sum up samples. 
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize and expose pixel
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                //display
                w.displayPixel(r, c, L);
            }
        }

    }

    /**
     * Render scene.
     *
     * @param w
     */
    @Override
    public void multiThreadRenderScene(final World w) {
        //copy of view plane.
        final ViewPlane vp = new ViewPlane(w.vp);
        //loop through all pixels
        for (int ri = 0; ri < vp.vRes; ri++) {
            for (int ci = 0; ci < vp.hRes; ci++) {
                final int r = ri;
                final int c = ci;
                Runnable pix = new Runnable() {
                    public void run() {
                        //color
                        RGBColor L = new RGBColor();
                        //ray
                        Ray ray = new Ray();
                        //depth
                        int depth = 0;
                        //pixel point
                        Point2D pp = new Point2D();
                        //normalized sample point
                        Point2D sp = new Point2D();
                        //initialize color
                        L.setTo(0, 0, 0);
                        //for all samples in point
                        for (int p = 0; p < vp.sampler.getNumSamples(); p++) {
                            //sample point
                            sp.setTo(vp.sampler.sampleUnitSquare());
                            //convert normalized sample point to a point somewhere in the pixel
                            pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x);
                            pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                            //get ray direction
                            ray.d.setTo(getDirection(pp));
                            //set ray origin, eyepoint + pixel location
                            ray.o.setTo(eye.add(u.mul(pp.x).add(v.mul(pp.y))));
                            //sum up samples. 
                            L.addLocal(w.tracer.traceRay(ray, depth));
                        }
                        //normalize and expose pixel
                        L.divLocal(vp.numSamples);
                        L.mulLocal(exposureTime);
                        //display
                        w.displayPixel(r, c, L);
                    }
                };
                EXEC.submit(pix);
            }
        }

    }

    /**
     * render stereo function
     *
     * @param w
     * @param x
     * @param i
     */
    @Override
    public void renderStereo(World w, double x, int i) {
        //color
        RGBColor L = new RGBColor();
        //copy of view plane.
        ViewPlane vp = new ViewPlane(w.vp);
        //ray
        Ray ray = new Ray();
        //depth
        int depth = 0;
        //pixel point
        Point2D pp = new Point2D();
        //normalized sample point
        Point2D sp = new Point2D();
        //loop through all pixels
        for (int r = 0; r < vp.vRes; r++) {
            for (int c = 0; c < vp.hRes; c++) {
                //initialize color
                L.setTo(0, 0, 0);
                //for all samples in point
                for (int p = 0; p < vp.sampler.getNumSamples(); p++) {
                    //sample point
                    sp.setTo(vp.sampler.sampleUnitSquare());
                    //convert normalized sample point to a point somewhere in the pixel offset for stereo
                    pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x) + x;
                    pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                    //get ray direction
                    ray.d.setTo(getDirection(pp));
                    //set ray origin, eyepoint + pixel location
                    ray.o.setTo(eye.add(u.mul(pp.x).add(v.mul(pp.y))));
                    //sum up samples. 
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize and expose pixel
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                //display, offset for stereo
                w.displayPixel(r, c + i, L);
            }
        }

    }

}
