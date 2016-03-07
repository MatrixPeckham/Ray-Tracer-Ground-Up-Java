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

import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.ViewPlane;
import com.matrixpeckham.raytracer.world.World;
import java.util.concurrent.Callable;

/**
 * Stereo Camera implementation.
 *
 * @author William Matrix Peckham
 */
public class StereoCamera extends Camera {

    /**
     * sets the left camera CANNOT BE STEREO
     *
     * @param leftCameraPtr
     */
    public final void setLeftCamera(Camera leftCameraPtr) {
        leftCamera = leftCameraPtr;
    }

    /**
     * sets the right camera CANNOT BE STEREO
     *
     * @param rightCameraPtr
     */
    public final void setRightCamera(Camera rightCameraPtr) {
        rightCamera = rightCameraPtr;
    }

    /**
     * set viewing type
     */
    public void useTransverseViewing() {
        viewingType = ViewingType.TRANSVERSE;
    }

    /**
     * set viewing type
     */
    public void useParallelViewing() {
        viewingType = ViewingType.PARALLEL;
    }

    /**
     * sets the gap between the two images
     *
     * @param i
     */
    public void setPixelGap(int i) {
        pixelGap = i;
    }

    /**
     * stereo angle of the two cameras
     *
     * @param d
     */
    public void setStereoAngle(double d) {
        beta = d;
    }

    @Override
    public void multiThreadRenderScene(World w) {
        final ViewPlane vp = new ViewPlane(w.vp);
        final int hres = vp.hRes;
        final int vres = vp.vRes;

        final double r = eye.distance(lookat);
        final double x = r * Math.tan(0.5 * beta * Utility.PI_ON_180);

        if (viewingType == ViewingType.PARALLEL) {
            Runnable left = new Runnable() {
                public void run() {
                    leftCamera.renderStereo(w, x, 0);
                }
            };
            Runnable right = new Runnable() {
                public void run() {
                    rightCamera.renderStereo(w, -x, hres + pixelGap);
                }
            };
            EXEC.submit(left);
            EXEC.submit(right);
        }
        if (viewingType == ViewingType.TRANSVERSE) {
            Runnable left = new Runnable() {
                public void run() {
                    leftCamera.renderStereo(w, -x, 0);
                }
            };
            Runnable right = new Runnable() {
                public void run() {
                }
            };
            rightCamera.renderStereo(w, x, hres + pixelGap);
            EXEC.submit(left);
            EXEC.submit(right);
        }
    }

    /**
     * enum for viewing types
     */
    public static enum ViewingType {

        /**
         *
         */
        PARALLEL,
        /**
         *
         */
        TRANSVERSE

    }

    /**
     * viewing type of camera
     */
    ViewingType viewingType = ViewingType.PARALLEL;

    /**
     * pixel gap between images
     */
    int pixelGap = 4;

    /**
     * separation between cameras
     */
    double beta = 30;

    //sub cameras
    Camera leftCamera;

    Camera rightCamera;

    /**
     * default constructor
     */
    public StereoCamera() {
    }

    /**
     * initializing constructor
     *
     * @param left
     * @param right
     */
    public StereoCamera(Camera left, Camera right) {
        this();
        setLeftCamera(left);
        setRightCamera(right);
    }

    /**
     * copy constructor
     *
     * @param o
     */
    public StereoCamera(StereoCamera o) {
        viewingType = o.viewingType;
        pixelGap = o.pixelGap;
        beta = o.beta;
        leftCamera = o.leftCamera.cloneCamera();
        rightCamera = o.rightCamera.cloneCamera();
    }

    /**
     * setup function, sets up the different camera views
     *
     * @param vp
     */
    public void setupCameras(ViewPlane vp) {
        vp.imageHeight = vp.vRes;
        vp.imageWidth = vp.hRes * 2 + pixelGap;
        double r = eye.distance(lookat);
        double x = r * Math.tan(0.5 * beta * Utility.PI_ON_180);
        leftCamera.setEye(eye.sub(u.mul(x)));
        leftCamera.setLookat(lookat.sub(u.mul(x)));
        leftCamera.computeUVW();
        rightCamera.setEye(eye.add(u.mul(x)));
        rightCamera.setLookat(lookat.add(u.mul(x)));
        rightCamera.computeUVW();

    }

    /**
     * Render function, differs to the left and right cameras
     *
     * @param w
     */
    @Override
    public void renderScene(World w) {
        ViewPlane vp = new ViewPlane(w.vp);
        int hres = vp.hRes;
        //int vres = vp.vRes;

        double r = eye.distance(lookat);
        double x = r * Math.tan(0.5 * beta * Utility.PI_ON_180);

        if (viewingType == ViewingType.PARALLEL) {
            leftCamera.renderStereo(w, x, 0);
            rightCamera.renderStereo(w, -x, hres + pixelGap);
        }
        if (viewingType == ViewingType.TRANSVERSE) {
            leftCamera.renderStereo(w, -x, 0);
            rightCamera.renderStereo(w, x, hres + pixelGap);
        }

    }

    /**
     * must override because it's abstract, but throw exception because we can't
     * have nested stereo cameras
     *
     * @param w
     * @param x
     * @param i
     */
    @Override
    public void renderStereo(World w, double x, int i) {
        throw new RuntimeException(
                "Stereo Camera should not have renderStereo call.");
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Camera cloneCamera() {
        return new StereoCamera(this);
    }

}
