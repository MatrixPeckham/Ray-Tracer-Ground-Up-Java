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
package com.matrixpeckham.raytracer.geometricobjects;

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Sphere extends GeometricObject {
    private Point3D center;
    private double radius;
    private static final double EPSILON=0.001;
    
    public Sphere(){
        super();
        center=new Point3D(0);
        radius=1;
    }
    
    public Sphere(Point3D c, double r){
        super();
        center=new Point3D(c);
        radius=r;
    }
    
    public Sphere(Sphere sphere){
        super(sphere);
        center=new Point3D(sphere.center);
        radius=sphere.radius;
    }
    
    @Override
    public GeometricObject clone() {
        return new Sphere(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        double t;
        Vector3D temp = ray.o.sub(center);
        double a = ray.d.dot(ray.d);
        double b = 2.0 * temp.dot(ray.d);
        double c = temp.dot(temp)-radius*radius;
        double disc = b*b-4.0*a*c;
        if(disc<0){
            return false;
        } else {
            double e = Math.sqrt(disc);
            double denom = 2.0*a;
            t=(-b-a)/denom;
            if(t>EPSILON){
                sr.lastT=t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
            t=(-b+a)/denom;
            if(t>EPSILON){
                sr.lastT=t;
                sr.normal.setTo(temp.add(ray.d.mul(t)).div(radius));
                sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
                return true;
            }
        }
        return false;
    }

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center.setTo(center);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    
}
