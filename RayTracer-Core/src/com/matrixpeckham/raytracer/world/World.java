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
package com.matrixpeckham.raytracer.world;

import com.matrixpeckham.raytracer.RenderListener;
import com.matrixpeckham.raytracer.RenderPixel;
import com.matrixpeckham.raytracer.cameras.Camera;
import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Light;
import com.matrixpeckham.raytracer.tracers.Tracer;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Class that represents the world to render.
 *
 * @author William Matrix Peckham
 */
public class World {

    /**
     * View Plane for the world. Starts as default.
     */
    public ViewPlane vp = new ViewPlane();

    /**
     * Color to render when nothing has been hit. Default: Black.
     */
    public RGBColor backgroundColor;

    /**
     * Tracer for the rays, determines what happens when rays hit things.
     */
    public Tracer tracer;

    /**
     * Ambient lighting for the scene.
     */
    public Light ambient;

    /**
     * Camera for the scene.
     */
    public Camera camera;

    /**
     * Lights for the scene.
     */
    public ArrayList<Light> lights = new ArrayList<>();

    /**
     * Single sphere for the initial test image from the first working example
     * from the book. used only in the first figure, and SingleSphereTracer.
     */
    public Sphere sphere = new Sphere();

    /**
     * List of objects in the scene.
     */
    public ArrayList<GeometricObject> objects = new ArrayList<>();

    /**
     * Queue for thread synchronization. Set before starting render, and used to
     * push finished pixels to the GUI.
     */
    private RenderListener paintArea = null;

    /**
     * Default constructor.
     */
    public World() {
        backgroundColor = Utility.BLACK;
        tracer = null;
        ambient = new Ambient();
    }

    public RenderListener getRenderListener() {
        return paintArea;
    }

    /**
     * Setter for the render queue.
     *
     * @param paintArea
     */
    public void setRenderListener(RenderListener paintArea) {
        this.paintArea = paintArea;
    }

    /**
     * Adds a light to the scene.
     *
     * @param light
     */
    public void addLight(Light light) {
        lights.add(light);
    }

    /**
     * set ambient light
     *
     * @param light
     */
    public void setAmbient(Light light) {
        ambient = light;
    }

    /**
     * set camera
     *
     * @param cam
     */
    public void setCamera(Camera cam) {
        camera = cam;
    }

    /**
     * Render scene function. For use without cameras, orthographic projection,
     * down the z axis. Not used, was for initial bare-bones ray tracer,
     * replaced with camera.renderScene.
     */
    public void renderScene() {
        //re-useabe color variable.
        RGBColor pixelColor = new RGBColor();
        //re-usable ray variable.
        Ray ray = new Ray();
        //resolutions and size
        int hres = vp.hRes;
        int vres = vp.vRes;
        double s = vp.s;
        double zw = 100.0f;
        //direction is always down z
        ray.d.setTo(0.0, 0.0, -1.0);
        //loop through every pixel
        for (int r = 0; r < vres; r++) {
            for (int c = 0; c < hres; c++) {
                //ray location is the center of each pixel.
                ray.o.setTo(s * (c - hres / 2.0 + 0.5), s * (r - vres / 2.0
                        + 0.5), zw);
                //set the temp color to the traced color
                pixelColor.setTo(tracer.traceRay(ray));
                //display pixel
                displayPixel(r, c, pixelColor);
            }
        }
    }

    /**
     * Sends a pixel to the GUI through the threading queue.
     *
     * @param row pixel x component
     * @param column pixel y component
     * @param rawColor color to send, may be changed if out of gamut
     */
    public void displayPixel(int row, int column, RGBColor rawColor) {
        RGBColor mappedColor;
        //fix out of gamut colors
        mappedColor = vp.toneMapper.map(rawColor);

        //gamma correction
        if (vp.gamma != 1.0) {
            mappedColor = mappedColor.powc(vp.invGamma);
        }

        //converts row/column to x,y image coordinates, flip y coordinate because
        //image has top left origin, and row/col is bottom left origin.
        int x = column;
        int y = vp.vRes - row - 1;

        //make sure we have a valid queue and send pixel.
        if (paintArea != null) {
            paintArea.newPixel(new RenderPixel(x, y,
                    (int) (mappedColor.r * 255), (int) (mappedColor.g * 255),
                    (int) (mappedColor.b * 255)));
        }
    }

    public void updateProgress(double progress) {
        paintArea.progress(progress);
    }

    public void startRender(int width, int height) {
        paintArea.renderStarting(width, height);
    }

    public void finishRender() {
        paintArea.renderFinished();
    }

    /**
     * Intersects a ray with the objects in the scene, and gets the nearest one
     * hit.
     *
     * @param ray ray to trace
     * @return ShadeRec of the nearest hit.
     */
    public ShadeRec hitObjects(Ray ray) {
        //creates a new shaderec.
        ShadeRec sr = new ShadeRec(this);
        //these hold some things temporarily for the
        //normal reference
        Normal normal = new Normal();
        //local hit position.
        Point3D localHitPoint = new Point3D();
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();
        //test the ray with all objects store values in temporary variables when they
        //are the lowest
        for (int j = 0; j < numObjects; j++) {
            if (objects.get(j).hit(ray, sr) && sr.lastT < tmin) {
                sr.hitAnObject = true;
                tmin = sr.lastT;//changes at call to hit, so we must preserve lowest
                sr.material = objects.get(j).getMaterial();
                sr.hitPoint.setTo(ray.o.add(ray.d.mul(sr.lastT)));//only calculated at this point
                normal.setTo(sr.normal);//ditto
                localHitPoint.setTo(sr.localHitPosition);//ditto
            }
        }
        //restore the saved lowest values
        if (sr.hitAnObject) {
            //sr.t=tmin;
            sr.lastT = tmin;
            sr.normal.setTo(normal);
            sr.localHitPosition.setTo(localHitPoint);
        }

        return sr;
    }

    /**
     * Simplistic hit function, no normals no local position, pretty much just
     * color.
     *
     * @param ray
     * @return
     */
    public ShadeRec hitBareBonesObjects(Ray ray) {
        ShadeRec sr = new ShadeRec(this);
        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();
        //intersect each object with ray, and keep the color from the closest.
        for (int j = 0; j < numObjects; j++) {
            GeometricObject ob = objects.get(j);
            if (ob.hit(ray, sr) && (sr.lastT < tmin)) {
                sr.hitAnObject = true;
                tmin = sr.lastT;
                sr.color.setTo(ob.getColor());
            }
        }

        return sr;
    }

    /**
     * add an object to the scene.
     *
     * @param obj
     */
    public void addObject(GeometricObject obj) {
        objects.add(obj);
    }

    private static final Logger LOG = Logger.getLogger(World.class.getName());

}
