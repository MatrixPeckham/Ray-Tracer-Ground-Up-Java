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

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class DiskChecker implements Texture{
    private int numAngularCheckers=20;
    private int numRadialCheckers=10;
    private double horizontalLineWidth=0;
    private double verticleLineWidth=0;
    private RGBColor color1=new RGBColor(1);
    private RGBColor color2=new RGBColor(0.5);
    private RGBColor lineColor=new RGBColor(0);

    public DiskChecker() {
    }

    public DiskChecker(DiskChecker c) {
        numAngularCheckers=c.numAngularCheckers;
        numRadialCheckers=c.numRadialCheckers;
        horizontalLineWidth=c.horizontalLineWidth;
        verticleLineWidth=c.verticleLineWidth;
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        lineColor.setTo(c.lineColor);
    }

    public void setNumAngularCheckers(int numAngularCheckers) {
        this.numAngularCheckers = numAngularCheckers;
    }

    public void setNumRadialCheckers(int numRadialCheckers) {
        this.numRadialCheckers = numRadialCheckers;
    }

    public void setAngularLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    public void setRadialLineWidth(double verticleLineWidth) {
        this.verticleLineWidth = verticleLineWidth;
    }

    public void setColor1(RGBColor color1) {
        this.color1 = color1;
    }

    public void setColor2(RGBColor color2) {
        this.color2 = color2;
    }

    public void setLineColor(RGBColor lineColor) {
        this.lineColor = lineColor;
    }
    
    
    
    
    @Override
    public Texture clone() {
        return new DiskChecker(this);
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
        double theta = Math.sqrt(x*x+z*z);//Math.acos(y);
        double phi = Math.atan2(x,z);
        if(phi<0)
            phi+=Utility.TWO_PI;
        double phiSize = Utility.TWO_PI / numAngularCheckers;
        double thetaSize= 1.0/numRadialCheckers;//Utility.PI / numRadialCheckers;
        
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
                return color1;
            }
        }else{
            if(!inOutline){
                return color2;
            }
        }
        return lineColor;
    }

    public void setLineWidth(double d) {
        setAngularLineWidth(d);
        setRadialLineWidth(d);
    }

    public void setNumlat(int i) {
        setNumAngularCheckers(i);
    }

    public void setNumlong(int i) {
        setNumRadialCheckers(i);
    }

    public void setNumAngular(int i) {
        setNumAngularCheckers(i);
    }

    public void setNumVertical(int i) {
        setNumRadialCheckers(i);
    }

    public void setVerticalLineWidth(double d) {
        setRadialLineWidth(d);
    }

    public void setColor1(double d, double d0, double d1) {
        setColor1(new RGBColor(d, d0, d1));
    }

    public void setColor2(double d, double d0, double d1) {
        setColor2(new RGBColor(d, d0, d1));
    }
    
}
