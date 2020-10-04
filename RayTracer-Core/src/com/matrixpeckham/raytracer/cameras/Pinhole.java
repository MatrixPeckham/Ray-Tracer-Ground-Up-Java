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

import static java.util.logging.Level.SEVERE;

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pinhole perspective camera.
 *
 * @author William Matrix Peckham
 */
public class Pinhole extends Camera {

    private double d;//view plane dist

    private double zoom;//zoom factor

    /**
     * Default constructor.
     */
    public Pinhole() {
        super();
        d = 500;
        zoom = 1.0f;
    }

    /**
     * Copy constructor.
     *
     * @param c
     */
    public Pinhole(Pinhole c) {
        super(c);
        d = c.d;
        zoom = c.zoom;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Camera cloneCamera() {
        return new Pinhole(this);
    }

    /**
     * Get ray direction for point. simply unit direction from eye point to the
     * point on the view plane.
     *
     * @param p
     *
     * @return
     */
    public Vector3D getDirection(Point2D p) {
        Vector3D dir = (u.mul(p.x)).add(v.mul(p.y)).sub(w.mul(d));
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
        //clone the viewport, we'll manipulate it later
        ViewPlane vp = new ViewPlane(w.vp);
        //ray
        Ray ray = new Ray();
        //depth
        int depth = 0;
        //pixel point
        Point2D pp = new Point2D();
        //change the pixel size for the zoom
        vp.s /= zoom;
        //the origin of the ray will always be the eye point.
        ray.o.setTo(eye);
        int pixRendered = 0;
        double pixToRender = vp.vRes * vp.hRes;
        w.startRender(vp.hRes, vp.hRes);

        //loop through all pixels
        for (int r = 0; r < vp.vRes; r++) {
            for (int c = 0; c < vp.hRes; c++) {
                //reset color
                L.setTo(0, 0, 0);
                //for all samples
                for (int p = 0; p < vp.numSamples; p++) {
                    //get sample point on pixel.
                    Point2D sp = vp.sampler.sampleUnitSquare();
                    pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x);
                    pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                    //compute direction
                    ray.d.setTo(getDirection(pp));
                    //add color
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize color and expose
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                //display
                w.displayPixel(r, c, L);
                pixRendered++;
            }
            w.updateProgress(pixRendered / pixToRender);
        }
        w.finishRender();

    }

    /**
     * sets the view distance
     *
     * @param d
     */
    public void setViewDistance(double d) {
        this.d = d;
    }

    /**
     * sets the zoom
     *
     * @param zoom
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
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
        //clone the viewport, we'll manipulate it later
        ViewPlane vp = new ViewPlane(w.vp);
        //ray
        Ray ray = new Ray();
        //depth
        int depth = 0;
        //pixel point
        Point2D pp = new Point2D();
        //change the pixel size for the zoom
        vp.s /= zoom;
        //the origin of the ray will always be the eye point.
        ray.o.setTo(eye);
        int pixRendered = 0;
        double pixToRender = vp.vRes * vp.hRes;

        //loop through all pixels
        for (int r = 0; r < vp.vRes; r++) {
            for (int c = 0; c < vp.hRes; c++) {
                //reset color
                L.setTo(0, 0, 0);
                //for all samples
                for (int p = 0; p < vp.numSamples; p++) {
                    //get sample point on pixel.
                    Point2D sp = vp.sampler.sampleUnitSquare();
                    //get point for pixel, offset for stereo
                    pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x) + x;
                    pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                    //compute direction
                    ray.d.setTo(getDirection(pp));
                    //add color
                    L.addLocal(w.tracer.traceRay(ray, depth));
                }
                //normalize color and expose
                L.divLocal(vp.numSamples);
                L.mulLocal(exposureTime);
                //display, offset for stereo
                w.displayPixel(r, c + i, L);
                pixRendered++;
            }
            w.updateProgress(pixRendered / pixToRender);
        }

    }

    @Override
    public void multiThreadRenderScene(World w) {
        //clone the viewport, we'll manipulate it later
        final ViewPlane vp = new ViewPlane(w.vp);
        //change the pixel size for the zoom
        vp.s /= zoom;
        w.startRender(vp.vRes, vp.hRes);
        CountDownLatch cdl = new CountDownLatch(vp.vRes * vp.hRes);

        //loop through all pixels
        for (int ri = 0; ri < vp.vRes; ri++) {
            for (int ci = 0; ci < vp.hRes; ci++) {
                final int r = ri;
                final int c = ci;
                Runnable pix = new Runnable() {

                    public void run() {
                        try {
                            //color
                            RGBColor L = new RGBColor();
                            //ray
                            Ray ray = new Ray();
                            //depth
                            int depth = 0;
                            //pixel point
                            Point2D pp = new Point2D();
                            //the origin of the ray will always be the eye point.
                            ray.o.setTo(eye);
                            //reset color
                            L.setTo(0, 0, 0);
                            //for all samples
                            for (int p = 0; p < vp.numSamples; p++) {
                                //get sample point on pixel.
                                Point2D sp = vp.sampler.sampleUnitSquare();
                                pp.x = vp.s * (c - 0.5f * vp.hRes + sp.x);
                                pp.y = vp.s * (r - 0.5f * vp.vRes + sp.y);
                                //compute direction
                                ray.d.setTo(getDirection(pp));
                                //add color
                                L.addLocal(w.tracer.traceRay(ray, depth));
                            }
                            //normalize color and expose
                            L.divLocal(vp.numSamples);
                            L.mulLocal(exposureTime);
                            //display
                            w.displayPixel(r, c, L);
                            w.updateProgress(((double) cdl.getCount())
                                    / ((double) (vp.hRes * vp.vRes)));
                            cdl.countDown();
                        } catch (Throwable t) {
                            Logger.getGlobal().log(SEVERE, "ERROR RENDERING: ",
                                    t);
                        }
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

}
