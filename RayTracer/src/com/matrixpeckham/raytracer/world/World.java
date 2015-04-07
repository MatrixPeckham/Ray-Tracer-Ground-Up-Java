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

import com.matrixpeckham.raytracer.RenderPixel;
import com.matrixpeckham.raytracer.cameras.Camera;
import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.Sphere;
import com.matrixpeckham.raytracer.lights.Ambient;
import com.matrixpeckham.raytracer.lights.Light;
import com.matrixpeckham.raytracer.tracers.Tracer;
import com.matrixpeckham.raytracer.util.Constants;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author William Matrix Peckham
 */
public class World {

    public ViewPlane vp=new ViewPlane();
    public RGBColor backgroundColor;
    public Tracer tracer;
    public Light ambient;
    public Camera camera;
    public ArrayList<Light> lights = new ArrayList<>();
    public Sphere sphere=new Sphere();
    public ArrayList<GeometricObject> objects=new ArrayList<>();
    private BlockingQueue<RenderPixel> paintArea = null;
    

    public World() {
        backgroundColor = Constants.BLACK;
        tracer = null;
        ambient=new Ambient();
    }
    
    public void setQueue(BlockingQueue paintArea){
        this.paintArea=paintArea;
    }
    
    public void addLight(Light light){
        lights.add(light);
    }
    
    public void setAmbient(Light light){
        ambient.setTo(light);
    }
    
    public void setCamera(Camera cam){
        camera = cam;
    }
    
    public void renderScene() {
        RGBColor pixelColor = new RGBColor();
        Ray ray = new Ray();
        int hres = vp.hRes;
        int vres = vp.vRes;
        double s = vp.s;
        double zw = 100.0f;
        ray.d.setTo(0.0, 0.0, -1.0);
        for (int r = 0; r < vres; r++) {
            for (int c = 0; c < hres; c++) {
                ray.o.setTo(s * (c - hres / 2.0 + 0.5), s * (r - vres / 2.0
                        + 0.5), zw);
                pixelColor.setTo(tracer.traceRay(ray));
                displayPixel(r, c, pixelColor);
            }
        }
    }

    public RGBColor maxToOne(RGBColor c) {
        double maxVal = Math.max(c.r, Math.max(c.g, c.b));
        if (maxVal > 1) {
            return c.div(maxVal);
        }
        return c;
    }

    public RGBColor clampToColor(RGBColor rawColor) {
        RGBColor c = new RGBColor(rawColor);
        if (c.r > 1 || c.g > 1 || c.b > 1) {
            c.r = 1;
            c.g = 0;
            c.b = 0;
        }
        return c;
    }

    public void displayPixel(int row, int column, RGBColor rawColor) {
        RGBColor mappedColor;
        if (vp.showOutOfGamut) {
            mappedColor = clampToColor(rawColor);
        } else {
            mappedColor = maxToOne(rawColor);
        }

        if (vp.gamma != 1.0) {
            mappedColor = mappedColor.powc(vp.invGamma);
        }

        int x = column;
        int y = vp.vRes - row - 1;

        if(paintArea!=null){
            paintArea.offer(new RenderPixel(x, y, (int)(mappedColor.r*255), (int)(mappedColor.g*255), (int)(mappedColor.b*255)));
        }
    }
    
    public ShadeRec hitObjects(Ray ray){
        ShadeRec sr=new ShadeRec(this);
        Normal normal = new Normal();
        Point3D localHitPoint = new Point3D();
        double tmin = Constants.HUGE_VALUE;
        int numObjects = objects.size();
        
        for(int j = 0; j<numObjects; j++){
            if(objects.get(j).hit(ray, sr)&&sr.lastT<tmin){
                sr.hitAnObject=true;
                tmin=sr.lastT;
                sr.material = objects.get(j).getMaterial();
                sr.hitPoint = ray.o.add(ray.d.mul(sr.lastT));
                normal.setTo(sr.normal);
                localHitPoint=sr.localHitPosition;
            }
        }
        if(sr.hitAnObject){
            sr.t=tmin;
            sr.normal.setTo(normal);
            sr.localHitPosition.setTo(localHitPoint);
        }
        
        return sr;
    }
    
    public ShadeRec hitBareBonesObjects(Ray ray){
        ShadeRec sr = new ShadeRec(this);
        double tmin = Constants.HUGE_VALUE;
        int numObjects = objects.size();
        
        for(int j=0; j<numObjects; j++){
            GeometricObject ob = objects.get(j);
            if(ob.hit(ray,sr)&&(sr.lastT<tmin)){
                sr.hitAnObject=true;
                tmin=sr.lastT;
                sr.color=ob.getColor();
            }
        }
        
        return sr;
    }

    public void addObject(GeometricObject obj) {
        objects.add(obj);
    }

}
