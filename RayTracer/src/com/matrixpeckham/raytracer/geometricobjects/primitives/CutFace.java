/*
 * Copyright (C) 2016 William Matrix Peckham
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
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class CutFace extends GeometricObject {

    private double size;
    private double radius;
    
    public CutFace(){
        size=1;
        size=0.5;
    }

    public CutFace(double size, double radius) {
        this.size = size;
        this.radius = radius;
    }
    
    public CutFace(CutFace c){
        this.size=c.size;
        this.radius=c.radius;
    }
    
    @Override
    public GeometricObject clone() {
        return new CutFace(this);
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    
    

    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
double t = -ray.o.y / ray.d.y;            
														
	if (t > Utility.EPSILON) {
		double xi = ray.o.x + t * ray.d.x; 
		double zi = ray.o.z + t * ray.d.z;
		double d = xi * xi + zi * zi;
		double size_on_two = 0.5 * size;
				
		if ((-size_on_two <= xi && xi <= size_on_two) && (-size_on_two <= zi && zi <= size_on_two)  // inside square
				&& 	d >= radius * radius)															// outside circle
		{	
			sr.lastT 				= t;
			sr.normal.setTo(0.0, 1.0, 0.0);
			sr.localHitPosition.setTo(ray.o.add(ray.d.mul(t)));
			
			return (true);
		}	
		else
			return (false);
	}
	
	return (false);    
    }
    
    

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        double t = -ray.o.y / ray.d.y;            
														
	if (t > Utility.EPSILON) {
		double xi = ray.o.x + t * ray.d.x; 
		double zi = ray.o.z + t * ray.d.z;
		double d = xi * xi + zi * zi;
		double size_on_two = 0.5 * size;
				
		if ((-size_on_two <= xi && xi <= size_on_two) && (-size_on_two <= zi && zi <= size_on_two)  // inside square
				&& 	d >= radius * radius)															// outside circle
		{	
			tr.d 				= t;
			return (true);
		}	
		else
			return (false);
	}
	
	return (false);    
    }
    
}
