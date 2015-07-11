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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class SphereMaterials extends Material {
    private int numHorizontalCheckers=20;
    private int numVerticleCheckers=10;
    private double horizontalLineWidth=0;
    private double verticleLineWidth=0;
    private Material color1;
    private Material color2;
    private Material lineColor;

    public SphereMaterials() {
        Matte c1=new Matte();
        c1.setCd(1);
        Matte c2=new Matte();
        c2.setCd(0.5);
        Matte cl=new Matte();
        cl.setCd(0);
        color1=c1;
        color2=c2;
        lineColor=cl;

    }

    public SphereMaterials(SphereMaterials c) {
        numHorizontalCheckers=c.numHorizontalCheckers;
        numVerticleCheckers=c.numVerticleCheckers;
        horizontalLineWidth=c.horizontalLineWidth;
        verticleLineWidth=c.verticleLineWidth;
        if(c.color1!=null)color1=c.color1.clone();
        if(c.color2!=null)color2=c.color2.clone();
        if(c.lineColor!=null)lineColor=c.lineColor.clone();
    }

    public void setNumHorizontalCheckers(int numHorizontalCheckers) {
        this.numHorizontalCheckers = numHorizontalCheckers;
    }

    public void setNumVerticleCheckers(int numVerticleCheckers) {
        this.numVerticleCheckers = numVerticleCheckers;
    }

    public void setHorizontalLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    public void setVerticleLineWidth(double verticleLineWidth) {
        this.verticleLineWidth = verticleLineWidth;
    }

    public void setColor1(Material color1) {
        this.color1 = color1.clone();
    }

    public void setColor2(Material color2) {
        this.color2 = color2.clone();
    }

    public void setLineColor(Material lineColor) {
        this.lineColor = lineColor.clone();
    }
    
    
    
    
    @Override
    public Material clone() {
        return new SphereMaterials(this);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;
        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        double theta = Math.acos(y);
        double phi = Math.atan2(x,z);
        if(phi<0)
            phi+=Utility.TWO_PI;
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize= Utility.PI / numVerticleCheckers;
        
        int iphi=(int)Math.floor(phi/phiSize);
        int itheta=(int)Math.floor(theta/thetaSize);
        
        double fphi = phi/phiSize-iphi;
        double ftheta=theta/thetaSize-itheta;
        
        double phiLineWidth = 0.5*verticleLineWidth;
        double thetaLineWidth = 0.5*horizontalLineWidth;
        
        boolean inOutline = (fphi<phiLineWidth ||fphi>1.0-phiLineWidth)||
                (ftheta<thetaLineWidth||ftheta>1.0-thetaLineWidth);
        
        if((iphi+itheta)%2==0){
            if(!inOutline){
                return color1.shade(sr);
            }
        }else{
            if(!inOutline){
                return color2.shade(sr);
            }
        }
        return lineColor.shade(sr);
    }

    public void setLineWidth(double d) {
        setHorizontalLineWidth(d);
        setVerticleLineWidth(d);
    }

    public void setNumlat(int i) {
        setNumHorizontalCheckers(i);
    }

    public void setNumlong(int i) {
        setNumVerticleCheckers(i);
    }

    public void setNumHorizontal(int i) {
        setNumHorizontalCheckers(i);
    }

    public void setNumVertical(int i) {
        setNumVerticleCheckers(i);
    }

    public void setVerticalLineWidth(double d) {
        setVerticleLineWidth(d);
    }

    public void setChecker1Material(Material ref) {
        setColor1(ref);
    }

    public void setChecker2Material(Material ref) {
        setColor2(ref);
    }

    public void setLineMaterial(Material ref) {
        setLineColor(ref);
    }


    
}
