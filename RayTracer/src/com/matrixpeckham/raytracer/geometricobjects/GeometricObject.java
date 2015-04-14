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

import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 *
 * @author William Matrix Peckham
 */
public abstract class GeometricObject {

    protected RGBColor color = new RGBColor();
    
    protected Material material = null;
    
    protected boolean shadows = true;
    
    // default ructor
    public GeometricObject() {
    }

    // copy ructor
    public GeometricObject(GeometricObject object) {
    }

    // virtual copy ructor
    public abstract GeometricObject clone();

    public abstract boolean hit(Ray ray, ShadeRec s);

		// the following three functions are only required for Chapter 3
    public void setColor(RGBColor c) {
        color.setTo(c);
    }

    public void setColor(double r, double g, double b) {
        color.setTo(r, g, b);
    }

    public Material getMaterial(){
        return material;
    }
    
    public void setMaterial(Material mat){
        material=mat;
    }
    
    public RGBColor getColor() {
        return color;
    }
    
    public Normal getNormal(){
        return new Normal();
    }
    
    public Normal getNormal(Point3D p){
        return new Normal();
    }
    
    public double pdf(ShadeRec sr){
        return 0;
    }
    
    public Point3D sample(){
        return new Point3D(0);
    }
    
    public void addObject(GeometricObject obj){}
    
    public BBox getBoundingBox(){
        return new BBox();
    }
    
    public void setBouningBox(){}

    public abstract boolean shadowHit(Ray ray, DoubleRef t);
    
    public void setShadows(boolean b){
        shadows=b;
    }
    
    public boolean castsShadows(){
        return shadows;
    }
    
}
