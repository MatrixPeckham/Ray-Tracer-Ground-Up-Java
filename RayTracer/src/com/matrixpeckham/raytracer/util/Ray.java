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
package com.matrixpeckham.raytracer.util;

/**
 * Ray class.
 * @author William Matrix Peckham
 */
public class Ray {
    /**
     * origin
     */
    public final Point3D o;
    /**
     * direction
     */ 
    public final Vector3D d;
    /**
     * for debugging
     * @return 
     */
    @Override
    public String toString(){
        return "("+o+","+d+")Ray";
    }
    /**
     * Defualt constructor, ray at origin pointing in positive z direction.
     */
    public Ray(){
        o=new Point3D(0);
        d=new Vector3D(0,0,1);
    }
    /**
     * initialize ray
     * @param p origin
     * @param v direction
     */
    public Ray(Point3D p, Vector3D v){
        o=new Point3D(p);
        d=new Vector3D(v);
    }
    /**
     * copy constructor
     * @param r 
     */
    public Ray(Ray r){
        this(r.o,r.d);
    }
    /**
     * equals replacement
     * @param r
     * @return 
     */
    public Ray setTo(Ray r){
        o.setTo(r.o);
        d.setTo(r.d);
        return this;
    }
}
