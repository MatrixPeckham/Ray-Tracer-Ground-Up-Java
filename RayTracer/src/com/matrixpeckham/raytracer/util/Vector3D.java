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
 *
 * @author William Matrix Peckham
 */
public class Vector3D {
    public double x;
    public double y;
    public double z;
    // default constructor
    public Vector3D(){
        this(0);
    }
    // constructor
    public Vector3D(double a){
        this(a,a,a);
    }
    // constructor
    public Vector3D(double _x, double _y, double _z){
        x=_x;
        y=_y;
        z=_z;
    }
    // copy constructor
    public Vector3D(Vector3D v){
        this(v.x,v.y,v.z);
    }
    // constructs a vector from a Normal
    public Vector3D(Normal n){
        this(n.x,n.y,n.z);
    }
    // constructs a vector from a point
    public Vector3D(Point3D p){
        this(p.x,p.y,p.z);
    }


    //assignment operator java substitute
    public Vector3D setTo(Vector3D other){
        x=other.x;
        y=other.y;
        z=other.z;
        return this;
    }
    
    // assign a Normal to a vector
    public Vector3D setTo(Normal other){
        x=other.x;
        y=other.y;
        z=other.z;
        return this;
    }
 
    // assign a Point3D to a vector
    public Vector3D setTo(Point3D other){
        x=other.x;
        y=other.y;
        z=other.z;
        return this;
    }
 
    // java substitute for unary minus
    public Vector3D neg () {
        return new Vector3D(-x,-y,-z);
    }
 
    // length
    public double length(){
        return Math.sqrt(lenSquared());
    }
 
    // square of the length
    public double lenSquared(){
        return x*x+y*y+z*z;
    }
 
    // multiplication by a double on the right
    public Vector3D mul(double a){
        return new Vector3D(x*a,y*a,z*a);
    }
 
    // division by a double
    public Vector3D div(double a){
        return new Vector3D(x/a,y/a,z/a);
    }
 
    // addition
    public Vector3D add(Vector3D v){
        return new Vector3D(x+v.x,y+v.y,z+v.z);
    }
 
    // compound addition
    public Vector3D addLocal(Vector3D v){
        x+=v.x;
        y+=v.y;
        z+=v.z;
        return this;
    }
 
    // subtraction
    public Vector3D sub(Vector3D v){
        return new Vector3D(x-v.x,y-v.y,z-v.z);
    }
 
    // dot product 
    public double dot(Vector3D v){
        return (x * v.x + y * v.y + z * v.z);
    }
 
    // cross product 
    public Vector3D cross(Vector3D v){
        return new Vector3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }
 
    // convert vector to a unit vector
    public void normalize(){
        double len = length();
        x/=len;
        y/=len;
        z/=len;
    } 
 
    // return a unit vector, and normalize the vector 
    public Vector3D hat(){
        normalize();
        return this;
    }
    
    public static Vector3D mul(double a, Vector3D v){
        return new Vector3D(a*v.x,a*v.y,a*v.z);
    }
    
    public static Point3D mul(Matrix mat, Vector3D v){
        return new Point3D(
                mat.m[0][0] * v.x + mat.m[0][1] * v.y + mat.m[0][2] * v.z,
                mat.m[1][0] * v.x + mat.m[1][1] * v.y + mat.m[1][2] * v.z,
		mat.m[2][0] * v.x + mat.m[2][1] * v.y + mat.m[2][2] * v.z        );
    }
    
}
