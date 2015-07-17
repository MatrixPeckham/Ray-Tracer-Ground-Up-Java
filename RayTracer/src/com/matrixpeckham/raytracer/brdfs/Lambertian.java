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

import com.matrixpeckham.raytracer.samplers.Sampler;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
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
    private Sampler sampler;

    public Lambertian(){
        super();
        kd=0;
        cd=new RGBColor(0);
    }
    
    public Lambertian(Lambertian lamb){
        super(lamb);
        kd=lamb.kd;
        cd=new RGBColor(lamb.cd);
        if(lamb.sampler!=null){
            sampler=lamb.sampler.clone();
        }
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

    public void setSampler(Sampler clone) {
        this.sampler=clone;
        this.sampler.mapSamplesToHemisphere(1);
        
    }

    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wi, DoubleRef pdf) {
        Vector3D w = new Vector3D(sr.normal);
	Vector3D v =new Vector3D(0.0034, 1, 0.0071) .cross( w);
	v.normalize();
	Vector3D u = v .cross( w);
	
	Point3D sp = sampler.sampleHemisphere();  
	//wi = sp.x * u + sp.y * v + sp.z * w;
        wi.setTo(u.mul(sp.x).add(v.mul(sp.y).add(w.mul(sp.z))));
	wi.normalize(); 	
	
	pdf.d = sr.normal .dot( wi) * Utility.INV_PI;
	
	return (cd .mul( kd ).mul( Utility.INV_PI));     
    }
    
    
    
}
