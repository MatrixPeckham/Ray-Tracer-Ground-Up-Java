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
package com.matrixpeckham.raytracer.lights;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Ambient extends Light {

    private double ls;
    private RGBColor color;
    
    public Ambient(){
        super();
        ls=1;
        color=new RGBColor(1);
    }
    
    public Ambient(Ambient a){
        super(a);
        ls=a.ls;
        color.setTo(a.color);
    }
    
    
    public void scaleRadiance(double b){
        ls=b;
    }
    
    public void setColor(double c){
        color.setTo(c, c, c);
    }
    
    public void setColor(RGBColor c){
        color.setTo(c);
    }
    
    public void setColor(double r, double g, double b){
        color.r=r;
        color.g=g;
        color.b=b;
    }
    
    @Override
    public Light clone() {
        return new Ambient(this);
    }

    @Override
    public Vector3D getDirection(ShadeRec sr) {
        return new Vector3D(0);
    }

    @Override
    public RGBColor L(ShadeRec sr) {
        return color.mul(ls);
    }
    
    
    
}
