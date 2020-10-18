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

import com.matrixpeckham.raytracer.util.*;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Orthographic camera.
 *
 * @author William Matrix Peckham
 */
public class QuadOrthographic extends Camera {

    /**
     * Default.
     */
    public QuadOrthographic() {
        super();
    }

    /**
     * pixel gap between images
     */
    int pixelGap = 4;

    /**
     * Copy Constructor
     *
     * @param c
     */
    public QuadOrthographic(QuadOrthographic c) {
        super(c);
        pixelGap = c.pixelGap;
    }

    /**
     * Clone
     *
     * @return
     */
    @Override
    public Camera cloneCamera() {
        return new QuadOrthographic(this);
    }

    /**
     * Gets the direction vector for traced rays, always the same for ortho.
     *
     * @param p point on the 2d plane
     *
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
    public void renderScene(World w, int frameNumber, double elapsedTime,
            double deltaTime) {
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
        int pixRendered = 0;
        double pixToRender = vp.vRes * vp.hRes;
        w.startRender(vp.hRes, vp.hRes);
        for (int r = 0; r < vp.vRes; r++) {
            for (int c = 0; c < vp.hRes; c++) {
                //color
                RGBColor L1 = new RGBColor();
                RGBColor L2 = new RGBColor();
                RGBColor L3 = new RGBColor();
                RGBColor L4 = new RGBColor();
                //initialize color
                L1.setTo(0, 0, 0);
                L2.setTo(0, 0, 0);
                L3.setTo(0, 0, 0);
                L4.setTo(0, 0, 0);
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
                    L1.addLocal(w.tracer.traceRay(ray, depth));
                    //get ray direction
                    ray.d.setTo(new Vector3D(0, -1, 0));
                    //set ray origin, eyepoint + pixel location
                    ray.o.setTo((new Point3D(0, 100, 0)).add(
                            (new Vector3D(1, 0, 0)).mul(pp.x).add(
                                    (new Vector3D(0, 0, 1)).mul(pp.y))));
                    //sum up samples.
                    L2.addLocal(w.tracer.traceRay(ray, depth));
                    //get ray direction
                    ray.d.setTo(new Vector3D(-1, 0, 0));
                    //set ray origin, eyepoint + pixel location
                    ray.o.setTo((new Point3D(100, 0, 0)).add(
                            (new Vector3D(0, 1, 0)).mul(pp.x).add(
                                    (new Vector3D(0, 0, 1)).mul(pp.y))));
                    //sum up samples.
                    L3.addLocal(w.tracer.traceRay(ray, depth));
                    //get ray direction
                    ray.d.setTo(new Vector3D(0, 0, -1));
                    //set ray origin, eyepoint + pixel location
                    ray.o.setTo((new Point3D(0, 0, 100)).add(
                            (new Vector3D(1, 0, 0)).mul(pp.x).add(
                                    (new Vector3D(0, 1, 0)).mul(pp.y))));
                    //sum up samples.
                    L4.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize and expose pixel
                L1.divLocal(vp.numSamples);
                L1.mulLocal(exposureTime);
                //normalize and expose pixel
                L2.divLocal(vp.numSamples);
                L2.mulLocal(exposureTime);
                //normalize and expose pixel
                L3.divLocal(vp.numSamples);
                L3.mulLocal(exposureTime);
                //normalize and expose pixel
                L4.divLocal(vp.numSamples);
                L4.mulLocal(exposureTime);
                //display
                w.displayPixel(r, c, L3);
                //display
                w.displayPixel(r + vp.hRes + pixelGap, c, L1);
                //display
                w.displayPixel(r, c + vp.vRes + pixelGap, L2);
                //display
                w.displayPixel(r + vp.hRes + pixelGap, c + vp.vRes
                        + pixelGap, L4);
                pixRendered++;
            }
            w.updateProgress(pixRendered / pixToRender);
        }
        w.finishRender();

    }

    /**
     * setup function, sets up the different camera views
     *
     * @param vp
     */
    public void setupCameras(ViewPlane vp) {
        vp.imageHeight = vp.vRes * 2 + pixelGap;
        vp.imageWidth = vp.hRes * 2 + pixelGap;
    }

    /**
     * Render scene.
     *
     * @param w
     */
    @Override
    public void multiThreadRenderScene(final World w, int frameNumber,
            double elapsedTime, double deltaTime) {
        //copy of view plane.
        final ViewPlane vp = new ViewPlane(w.vp);
        w.startRender(vp.vRes, vp.hRes);
        CountDownLatch cdl = new CountDownLatch(vp.vRes * vp.hRes);
        //loop through all pixels
        for (int ri = 0; ri < vp.vRes; ri++) {
            for (int ci = 0; ci < vp.hRes; ci++) {
                final int r = ri;
                final int c = ci;
                Runnable pix = new Runnable() {

                    public void run() {
                        //color
                        RGBColor L1 = new RGBColor();
                        RGBColor L2 = new RGBColor();
                        RGBColor L3 = new RGBColor();
                        RGBColor L4 = new RGBColor();
                        //ray
                        Ray ray = new Ray();
                        //depth
                        int depth = 0;
                        //pixel point
                        Point2D pp = new Point2D();
                        //normalized sample point
                        Point2D sp = new Point2D();
                        //initialize color
                        L1.setTo(0, 0, 0);
                        L2.setTo(0, 0, 0);
                        L3.setTo(0, 0, 0);
                        L4.setTo(0, 0, 0);
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
                            L1.addLocal(w.tracer.traceRay(ray, depth));
                            //get ray direction
                            ray.d.setTo(new Vector3D(0, -1, 0));
                            //set ray origin, eyepoint + pixel location
                            ray.o.setTo((new Point3D(0, 100, 0)).add(
                                    (new Vector3D(1, 0, 0)).mul(pp.x).add(
                                            (new Vector3D(0, 0, 1)).mul(pp.y))));
                            //sum up samples.
                            L2.addLocal(w.tracer.traceRay(ray, depth));
                            //get ray direction
                            ray.d.setTo(new Vector3D(-1, 0, 0));
                            //set ray origin, eyepoint + pixel location
                            ray.o.setTo((new Point3D(100, 0, 0)).add(
                                    (new Vector3D(0, 1, 0)).mul(pp.x).add(
                                            (new Vector3D(0, 0, 1)).mul(pp.y))));
                            //sum up samples.
                            L3.addLocal(w.tracer.traceRay(ray, depth));
                            //get ray direction
                            ray.d.setTo(new Vector3D(0, 0, -1));
                            //set ray origin, eyepoint + pixel location
                            ray.o.setTo((new Point3D(0, 0, 100)).add(
                                    (new Vector3D(1, 0, 0)).mul(pp.x).add(
                                            (new Vector3D(0, 1, 0)).mul(pp.y))));
                            //sum up samples.
                            L4.addLocal(w.tracer.traceRay(ray, depth));
                        }
                        //normalize and expose pixel
                        L1.divLocal(vp.numSamples);
                        L1.mulLocal(exposureTime);
                        //normalize and expose pixel
                        L2.divLocal(vp.numSamples);
                        L2.mulLocal(exposureTime);
                        //normalize and expose pixel
                        L3.divLocal(vp.numSamples);
                        L3.mulLocal(exposureTime);
                        //normalize and expose pixel
                        L4.divLocal(vp.numSamples);
                        L4.mulLocal(exposureTime);
                        //display
                        w.displayPixel(r, c, L3);
                        //display
                        w.displayPixel(r + vp.hRes + pixelGap, c, L1);
                        //display
                        w.displayPixel(r, c + vp.vRes + pixelGap, L2);
                        //display
                        w.displayPixel(r + vp.hRes + pixelGap, c + vp.vRes
                                + pixelGap, L4);
                        w.updateProgress(((double) cdl.getCount())
                                / ((double) (vp.hRes * vp.vRes)));
                        cdl.countDown();
                    }

                };
                EXEC.submit(pix);
            }
        }
        try {
            cdl.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(Pinhole.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        w.finishRender();

    }

    /**
     * render stereo function
     *
     * @param w
     * @param x
     * @param i
     */
    @Override
    public void renderStereo(World w, double x, int i, int frameNumber,
            double elapsedTime, double deltaTime) {
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
        int pixRendered = 0;
        double pixToRender = vp.vRes * vp.hRes;
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
                pixRendered++;
            }
            w.updateProgress(pixRendered / pixToRender);
        }

    }

}
