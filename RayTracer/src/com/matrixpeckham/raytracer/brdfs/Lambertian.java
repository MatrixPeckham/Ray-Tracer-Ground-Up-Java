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
package com.matrixpeckham.raytracer.brdfs;

import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Lambertian extends BRDF{
    private double kd;
    private RGBColor cd;

    public Lambertian(){
        super();
        kd=0;
        cd=new RGBColor(0);
    }
    
    public Lambertian(Lambertian lamb){
        super(lamb);
        kd=lamb.kd;
        cd=new RGBColor(lamb.cd);
    }
    
    public Lambertian clone(){
        return new Lambertian(this);
    }

    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return cd.mul(kd).mul(Utility.INV_PI);
    }

    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return cd.mul(kd);
    }
    
    public void setKd(double kd) {
        this.kd = kd;
    }
    public void setKa(double kd) {
        this.kd = kd;
    }

    public void setCd(RGBColor cd) {
        this.cd.setTo(cd);
    }
    public void setCd(double r, double g, double b) {
        this.cd.setTo(r, g, b);
    }
    public void setCd(double c) {
        this.cd.setTo(c, c, c);
    }
    
}
