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
public class Rectangle extends GeometricObject{
    private Point3D p0 = new Point3D(-1,0,-1);
    private Vector3D a = new Vector3D(0,0,2);
    private Vector3D b = new Vector3D(2,0,0);
    private double aLenSquared = 4;
    private double bLenSquared = 4;
    private Normal normal = new Normal(0,1,0);
    private double area = 4;
    private double invArea = 0.25;
    private Sampler sampler = null;

    public Rectangle(){super();}
    public Rectangle(Point3D p, Vector3D a, Vector3D b){
        p0.setTo(p);
        this.a.setTo(a);
        this.b.setTo(b);
        aLenSquared=a.lenSquared();
        bLenSquared=b.lenSquared();
        area=a.length()*b.length();
        invArea=1/area;
        sampler=null;
        normal=new Normal(a.cross(b));
        normal.normalize();
    }
    public Rectangle(Point3D p, Vector3D a, Vector3D b, Normal n){
        super();
        p0.setTo(p);
        this.a.setTo(a);
        this.b.setTo(b);
        area=a.length()*b.length();
        aLenSquared=a.lenSquared();
        bLenSquared=b.lenSquared();
        invArea=1/area;
        sampler=null;
        normal.setTo(n);
        normal.normalize();
    }
    
    public Rectangle(Rectangle r){
        super(r);
        p0.setTo(r.p0);
        this.a.setTo(r.a);
        this.b.setTo(r.b);
        aLenSquared=r.aLenSquared;
        bLenSquared=r.bLenSquared;
        area=r.area;
        invArea=r.invArea;
        sampler=null;
        normal.setTo(r.normal);
        normal.normalize();
        if(r.sampler!=null){
            sampler=r.sampler.clone();
        }
    }

    @Override
    public BBox getBoundingBox() {
	double delta = 0.0001; 

	return(new BBox(Math.min(p0.x, p0.x + a.x + b.x) - delta, Math.max(p0.x, p0.x + a.x + b.x) + delta,
				Math.min(p0.y, p0.y + a.y + b.y) - delta, Math.max(p0.y, p0.y + a.y + b.y) + delta, 
				Math.min(p0.z, p0.z + a.z + b.z) - delta, Math.max(p0.z, p0.z + a.z + b.z) + delta));
    }
    
    
    
    @Override
    public GeometricObject clone() {
        return new Rectangle(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        double t = p0.sub(ray.o).dot(normal) / ray.d.dot(normal);
        if(t<=Utility.EPSILON){
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        Vector3D d = p.sub(p0);
        
        double ddota = d.dot(a);
        
        if(ddota<0 || ddota > aLenSquared){
            return false;
        }

        double ddotb = d.dot(b);
        
        if(ddotb<0 || ddotb > bLenSquared){
            return false;
        }
        
        s.lastT=t;
        s.normal.setTo(normal);
        s.localHitPosition.setTo(p);
        return true;
}

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        if(!shadows)return false;
        double t = p0.sub(ray.o).dot(normal) / ray.d.dot(normal);
        if(t<=Utility.EPSILON){
            return false;
        }
        Point3D p = ray.o.add(ray.d.mul(t));
        Vector3D d = p.sub(p0);
        
        double ddota = d.dot(a);
        
        if(ddota<0 || ddota > aLenSquared){
            return false;
        }

        double ddotb = d.dot(b);
        
        if(ddotb<0 || ddotb > bLenSquared){
            return false;
        }
        
        tr.d=t;
        return true;
    }
    
    public void setSampler(Sampler s){
        sampler=s;
    }
    
    @Override
    public Point3D sample(){
        Point2D samplePoint = sampler.sampleUnitSquare();
        return p0.add(a.mul(samplePoint.x).add(b.mul(samplePoint.y)));
    }

    @Override
    public Normal getNormal(Point3D p) {
        return normal;
    }
    
    
    
    @Override
    public double pdf(ShadeRec s){
        return invArea;
    }
    
}
