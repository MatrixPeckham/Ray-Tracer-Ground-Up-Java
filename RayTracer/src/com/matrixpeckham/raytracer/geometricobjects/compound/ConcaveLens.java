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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartSphere;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class ConcaveLens extends Compound{
    double lensRadius;
    double thickness;
    double minDistance;
    BBox bbox=new BBox();
    public ConcaveLens(double rad, double thick, double minDist) {
        super();
        lensRadius=rad;
        thickness=thick;
        minDistance=minDist;
        Point3D center;
	double theta_min, theta_max;
	
	double phi_min = 0.0;
	double phi_max = 360.0;
		
	// top concave part sphere

	double d = (thickness - minDistance) / 2.0;
	double sphere_radius = (d * d + lensRadius * lensRadius) / (2.0 * d);
	
	center =new Point3D(0.0, sphere_radius + minDistance/ 2.0, 0.0);
	theta_min = 90.0 + Math.acos(lensRadius / sphere_radius) * 180.0 / Utility.PI;  // in degrees 
	theta_max = 180.0;
	this.addObject(new ConcavePartSphere(center, sphere_radius, phi_min, phi_max, theta_min, theta_max)); 
	
	// bottom concave part sphere
	
	center =new Point3D(0.0, -(sphere_radius + minDistance / 2.0), 0.0);
	theta_min = 0.0;
	theta_max = 90.0 - Math.acos(lensRadius / sphere_radius) * 180.0 / Utility.PI;  // in degrees 
	this.addObject(new ConcavePartSphere(center, sphere_radius, phi_min, phi_max, theta_min, theta_max));
		
	// outer surface
	
	this.addObject(new ConvexPartCylinder(-thickness / 2.0, thickness / 2.0, lensRadius)); 
	
	// bounding box
	bbox.x0 = -lensRadius;
	bbox.x1 = lensRadius;
	bbox.y0 = -thickness / 2.0;
	bbox.y1 = thickness / 2.0; 
	bbox.z0 = -lensRadius;
	bbox.z1 = lensRadius; 		
    }
    public ConcaveLens(ConcaveLens c){
        super(c);
        this.bbox.setTo(c.bbox);
        lensRadius=c.lensRadius;
        thickness=c.thickness;
        minDistance=c.minDistance;
        
    }

    @Override
    public GeometricObject clone() {
        return new ConcaveLens(this);
    }

    @Override
    public BBox getBoundingBox() {
        return bbox;
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        if(bbox.hit(ray)){
            return super.hit(ray, s);
        }
        return false;
    }
    
    
    
}
