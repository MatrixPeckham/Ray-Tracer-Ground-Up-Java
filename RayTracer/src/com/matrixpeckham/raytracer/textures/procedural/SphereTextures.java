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
package com.matrixpeckham.raytracer.textures.procedural;

import com.matrixpeckham.raytracer.materials.*;
import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class SphereTextures implements Texture {
    private int numHorizontalCheckers=20;
    private int numVerticleCheckers=10;
    private double horizontalLineWidth=0;
    private double verticleLineWidth=0;
    private Texture color1;
    private Texture color2;
    private Texture lineColor;

    public SphereTextures() {
        color1=new ConstantColor();
        color2=new ConstantColor();
        lineColor=new ConstantColor();

    }

    public SphereTextures(SphereTextures c) {
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

    public void setTexture1(Texture color1) {
        this.color1 = color1.clone();
    }

    public void setTexture2(Texture color2) {
        this.color2 = color2.clone();
    }

    public void setLineColor(Texture lineColor) {
        this.lineColor = lineColor.clone();
    }
    
    
    
    
    @Override
    public Texture clone() {
        return new SphereTextures(this);
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

    public void setChecker1Texture(Texture ref) {
        setTexture1(ref);
    }

    public void setChecker2Texture(Texture ref) {
        setTexture1(ref);
    }

    public void setLineTexture(Texture ref) {
        setLineColor(ref);
    }


    @Override
    public RGBColor getColor(ShadeRec sr) {
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
        
        if(!((iphi+itheta)%2==0)){
            if(!inOutline){
                return color1.getColor(sr);
            }
        }else{
            if(!inOutline){
                return color2.getColor(sr);
            }
        }
        return lineColor.getColor(sr);
    }


    
}
