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
package com.matrixpeckham.raytracer.geometricobjects.primitives;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Disk extends GeometricObject {
    private Point3D center = new Point3D();
    private Normal normal = new Normal(0,1,0);
    private double radius = 1;
    private Sampler sampler;
    double area = Utility.PI*radius*radius;
    double invArea = 1/area;
    final Vector3D up=new Vector3D(0,1,0);
    Vector3D u=new Vector3D(1,0,0);
    Vector3D v=new Vector3D(0,0,1);
    Vector3D w=new Vector3D(0,1,0);
    
    public Disk(){}
    public Disk(Point3D loc, Normal nor, double rad){
        center.setTo(loc);
        normal.setTo(nor);
        radius=rad;
        area = Utility.PI*radius*radius;
        invArea = 1/area;
    }
    public Disk(Disk d){
        center.setTo(d.center);
        normal.setTo(d.normal);
        radius=d.radius;
        area=d.area;
        invArea=d.invArea;
        sampler=d.sampler.clone();
        sampler.mapSamplesToUnitDisk();
        u.setTo(d.u);
        v.setTo(d.v);
        w.setTo(d.w);
        
    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center.setTo( center );
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    public void setNormal(Normal n){
        normal.setTo(n);
    }
    @Override
    public GeometricObject clone() {
        return new Disk(this);
    }

    @Override
    public Normal getNormal() {
        return normal;
    }

    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        double t = (center.sub(ray.o).dot(normal)/(ray.d.dot(normal)));
        if(t<Utility.EPSILON){
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        if(center.distSquared(p)<radius*radius){
            s.lastT=t;
            s.normal.setTo(normal);
            s.localHitPosition.setTo(p);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        if(!shadows)return false;
        double t = (center.sub(ray.o).dot(normal)/(ray.d.dot(normal)));
        if(t<Utility.EPSILON){
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        if(center.distSquared(p)<radius*radius){
            tr.d=t;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BBox getBoundingBox() {
        return new BBox(center.x-radius, center.x+radius, center.y-radius, center.y+radius, center.z-radius, center.z+radius);
    }

    public void setSampler(Sampler samplerPtr) {
        sampler = samplerPtr.clone();
        sampler.mapSamplesToUnitDisk();
    }
    
    public void computeUVW() {
        w.setTo(normal);
        w.normalize();
        u=up.cross(w);
        u.normalize();
        v=w.cross(u);
        
        //special cases for strait up and down view.
        if(normal.x==up.x&&normal.z==up.z&&normal.y==up.y){
            u.setTo(new Vector3D(0,0,1));
            v.setTo(new Vector3D(1,0,0));
            w.setTo(new Vector3D(0,1,0));
        }
        if(normal.x==up.x&&normal.z==up.z&&normal.y==-up.y){
            u.setTo(new Vector3D(1,0,0));
            v.setTo(new Vector3D(0,0,1));
            w.setTo(new Vector3D(0,-1,0));
        }
    }

    @Override
    public Point3D sample() {
        Point2D dp = sampler.sampleUnitDisk();
        dp=dp.mul(radius);
        Point3D rp = new Point3D(center);
        rp=rp.add(u.mul(dp.x)).add(v.mul(dp.y));
        return rp;
    }

    @Override
    public double pdf(ShadeRec sr) {
        return invArea;
    }
    
    
    
}
