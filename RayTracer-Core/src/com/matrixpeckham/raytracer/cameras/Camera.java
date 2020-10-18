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

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import com.matrixpeckham.raytracer.world.World;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Base class for cameras, holds all the stuff that all cameras have in common.
 *
 * @author William Matrix Peckham
 */
public abstract class Camera {

    /**
     * Number of threads to use
     */
    public static final int POOL_SIZE = 10;

    /**
     * thread pool executor for multithreading.
     */
    protected static final ScheduledThreadPoolExecutor EXEC
            = new ScheduledThreadPoolExecutor(POOL_SIZE);

    public static void exit() {
        EXEC.shutdown();
    }

    /**
     * Eye Point of the camera, center of ortho view, or focal point of
     * perspective.
     */
    protected final Point3D eye;

    /**
     * Point for the camera to look at.
     */
    protected final Point3D lookat;

    /**
     * Roll angle along look direction.
     */
    protected double rollAngle;

    /**
     * Three unit vectors for every camera.
     */
    protected final Vector3D u;

    /**
     *
     */
    protected final Vector3D v;

    /**
     *
     */
    protected final Vector3D w;

    /**
     * Up vector for the camera
     */
    protected final Vector3D up;

    /**
     * Exposure time.
     */
    protected double exposureTime = 1;

    /**
     * Sets defaults.
     */
    public Camera() {
        eye = new Point3D(0, 0, 500);
        lookat = new Point3D(0);
        rollAngle = 0;
        up = new Vector3D(0, 1, 0);
        u = new Vector3D(1, 0, 0);
        v = new Vector3D(0, 1, 0);
        w = new Vector3D(0, 0, 1);
        exposureTime = 1;
    }

    /**
     * copies other camera.
     *
     * @param c
     */
    public Camera(Camera c) {
        eye = new Point3D(c.eye);
        lookat = new Point3D(c.lookat);
        rollAngle = c.rollAngle;
        up = new Vector3D(c.up);
        u = new Vector3D(c.u);
        v = new Vector3D(c.v);
        w = new Vector3D(c.w);
        exposureTime = c.exposureTime;
    }

    /**
     * Computes basis vectors from look at and up.
     */
    public void computeUVW() {
        w.setTo(eye.sub(lookat));
        w.normalize();
        up.setTo(Vector3D.rotateAAroundB(up, w, rollAngle));
        u.setTo(up.cross(w));
        u.normalize();
        v.setTo(w.cross(u));

        //special cases for strait up and down view.
        if (eye.x == lookat.x && eye.z == lookat.z && eye.y > lookat.y) {
            w.setTo(new Vector3D(0, 1, 0));
            u.
                    setTo(Vector3D.rotateAAroundB(new Vector3D(0, 0, 1), w,
                            rollAngle));
            v.
                    setTo(Vector3D.rotateAAroundB(new Vector3D(1, 0, 0), w,
                            rollAngle));
        }
        if (eye.x == lookat.x && eye.z == lookat.z && eye.y < lookat.y) {
            w.setTo(new Vector3D(0, -1, 0));
            u.
                    setTo(Vector3D.rotateAAroundB(new Vector3D(0, 0, 1), w,
                            rollAngle));
            v.
                    setTo(Vector3D.rotateAAroundB(new Vector3D(1, 0, 0), w,
                            rollAngle));
        }
    }

    /**
     * Setter
     *
     * @param p
     */
    public void setEye(Point3D p) {
        eye.setTo(p);
    }

    /**
     * setter
     *
     * @param x
     * @param y
     * @param z
     */
    public void setEye(double x, double y, double z) {
        eye.x = x;
        eye.y = y;
        eye.z = z;
    }

    /**
     * setter
     *
     * @param p
     */
    public void setLookat(Point3D p) {
        lookat.setTo(p);
    }

    /**
     * setter
     *
     * @param x
     * @param y
     * @param z
     */
    public void setLookat(double x, double y, double z) {
        lookat.x = x;
        lookat.y = y;
        lookat.z = z;
    }

    /**
     * Sets the look at vector to point in the direction of view direction.
     *
     * @param vd view direction
     */
    public void setViewDirection(Vector3D vd) {
        lookat.setTo(eye.add(vd));
    }

    /**
     * Sets the look at vector to point in the direction specified by the three
     * parameters.
     *
     * @param x
     * @param y
     * @param z
     */
    public void setViewDirection(double x, double y, double z) {
        lookat.setTo(eye.add(new Vector3D(x, y, z)));
    }

    /**
     * setter
     *
     * @param p
     */
    public void setUp(Vector3D p) {
        up.setTo(p);
    }

    /**
     * setter
     *
     * @param x
     * @param y
     * @param z
     */
    public void setUp(double x, double y, double z) {
        up.x = x;
        up.y = y;
        up.z = z;
    }

    /**
     * setter
     *
     * @param r
     */
    public void setRoll(double r) {
        rollAngle = r;
    }

    /**
     * setter
     *
     * @param exp
     */
    public void setExposureTime(double exp) {
        exposureTime = exp;
    }

    /**
     * renders a scene.
     *
     * @param w
     */
    public void renderScene(World w) {
        renderScene(w, 0, 0, 0);
    }

    public abstract void renderScene(World w, int frameNumber,
            double timeElapsed, double deltaTime);

    /**
     * renders a scene.
     *
     * @param w
     */
    public abstract void multiThreadRenderScene(World w, int frameNumber,
            double timeElapsed, double deltaTime);

    public void multiThreadRenderScene(World w) {
        multiThreadRenderScene(w, 0, 0, 0);
    }

    /**
     * clone method.
     *
     * @return
     */
    public abstract Camera cloneCamera();

    /**
     * Render stereo, x is an offset for frustum, i is a raster offset.
     *
     * @param w
     * @param x
     * @param i
     */
    public abstract void renderStereo(World w, double x, int i, int frameNumber,
            double elapsedTime, double deltaTime);

    public void renderStereo(World w, double x, int i) {
        renderStereo(w, x, i, 0, 0, 0);
    }

    /**
     * sets the up vector
     *
     * @param i
     * @param i0
     * @param i1
     */
    public void setUpVector(int i, int i0, int i1) {
        up.setTo(i, i0, i1);
    }

}
