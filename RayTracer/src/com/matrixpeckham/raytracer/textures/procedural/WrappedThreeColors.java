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
public class WrappedThreeColors implements Texture {
    private LatticeNoise noise=null;
    private RGBColor color1=new RGBColor();
    private RGBColor color2=new RGBColor();
    private RGBColor color3=new RGBColor();
    private double thresh1=1.35;
    private double thresh2=2;
    private double minValue;
    private double maxValue;
    private double expansionNumber;
    
    public WrappedThreeColors(){
        this(Utility.WHITE,Utility.BLACK);
    }
    public WrappedThreeColors(RGBColor col,RGBColor col2){
        this(col,col2,0.0,1.0);
    }
    public WrappedThreeColors(RGBColor col,RGBColor col2, double min, double max){
        this(col,col2,min,max,2,new LinearNoise());
    }
    public WrappedThreeColors(RGBColor col,RGBColor col2, double min, double max, double num, LatticeNoise n){
        color1.setTo(col);
        color2.setTo(col2);
        minValue=min;
        maxValue=max;
        noise=n;
        expansionNumber=num;
    }
    
    public WrappedThreeColors(WrappedThreeColors t){
        this.color1.setTo(t.color1);
        this.color2.setTo(t.color2);
        this.color3.setTo(t.color3);
        this.maxValue=t.maxValue;
        this.minValue=t.minValue;
        this.noise=t.noise.clone();
        this.expansionNumber=t.expansionNumber;
    }

    public WrappedThreeColors(CubicNoise noisePtr) {
        this(Utility.WHITE, Utility.BLACK, 0, 1, 2, noisePtr);
    }

    @Override
    public Texture clone() {
        return new WrappedThreeColors(this);
    }

    
    @Override
    public RGBColor getColor(ShadeRec sr){
        double n = expansionNumber * noise.valueFBM(sr.localHitPosition);
        double value=n-Math.floor(n);
        value=minValue+(maxValue-minValue)*value;
        if (n < thresh1) {
		return color1.mul(value);  
	} else if (n < thresh2) {
		return color2.mul(value);  
	} else {
		return color3.mul(value);   
	}
    }

    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    public void setColor2(double d, double d0, double d1) {
        color2.setTo(d, d0, d1);
    }
    public void setColor3(double d, double d0, double d1) {
        color3.setTo(d, d0, d1);
    }

    public void setExpansionNumber(double d) {
        expansionNumber=d;
    }
}
