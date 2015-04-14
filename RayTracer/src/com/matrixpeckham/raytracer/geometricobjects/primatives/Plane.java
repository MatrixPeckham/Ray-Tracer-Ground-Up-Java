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
package com.matrixpeckham.raytracer.geometricobjects.primatives;

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Plane extends GeometricObject {
    
    private Point3D a;
    private Normal n; 
    private static final double EPSILON=0.001;
    
    public Plane(){
        super();
        a=new Point3D(0);
        n=new Normal(0,1,0);
    }
    
    public Plane(Point3D point, Normal normal){
        super();
        a=new Point3D(point);
        n=new Normal(normal);
        n.normalize();
    }
    
    public Plane(Plane p){
        super(p);
        a=new Point3D(p.a);
        n=new Normal(p.n);
    }
    
    
    @Override
    public GeometricObject clone() {
        return new Plane(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        double t = a.sub(ray.o).dot(new Vector3D(n))/(ray.d.dot(new Vector3D(n)));
        if(t>EPSILON){
            s.lastT=t;
            s.normal.setTo(n);
            s.localHitPosition=ray.o.add(ray.d.mul(t));
            return true;
        }
        return false;
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        if(!shadows) return false;
        t.d = a.sub(ray.o).dot(new Vector3D(n))/(ray.d.dot(new Vector3D(n)));
        if(t.d>EPSILON){
            return true;
        }
        return false;
    }

}
