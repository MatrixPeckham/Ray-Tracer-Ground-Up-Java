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

import com.matrixpeckham.raytracer.textures.ConstantColor;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class NestedNoisesTexture implements Texture {
    private LatticeNoise noise=null;
    private RGBColor color1=new RGBColor();
    private Texture color2=null;
    private double minValue;
    private double maxValue;
    private double expansionNumber;
    
    public NestedNoisesTexture(){
        this(Utility.WHITE,new ConstantColor(Utility.BLACK));
    }
    public NestedNoisesTexture(RGBColor col,Texture col2){
        this(col,col2,0.0,1.0);
    }
    public NestedNoisesTexture(RGBColor col,Texture col2, double min, double max){
        this(col,col2,min,max,2,new LinearNoise());
    }
    public NestedNoisesTexture(RGBColor col,Texture col2, double min, double max, double num, LatticeNoise n){
        color1.setTo(col);
        color2=col2.clone();
        minValue=min;
        maxValue=max;
        noise=n;
        expansionNumber=num;
    }
    
    public NestedNoisesTexture(NestedNoisesTexture t){
        this.color1.setTo(t.color1);
        this.color2=t.color2.clone();
        this.maxValue=t.maxValue;
        this.minValue=t.minValue;
        this.noise=t.noise.clone();
        this.expansionNumber=t.expansionNumber;
    }

    public NestedNoisesTexture(CubicNoise noisePtr) {
        this(Utility.WHITE, new ConstantColor(Utility.BLACK), 0, 1, 2, noisePtr);
    }

    @Override
    public Texture clone() {
        return new NestedNoisesTexture(this);
    }

    
    @Override
    public RGBColor getColor(ShadeRec sr){
        double n = expansionNumber * noise.valueFBM(sr.localHitPosition);
        double value=n-Math.floor(n);
        value=minValue+(maxValue-minValue)*value;
        if(n<1){
            return(color1.mul(value));
        } else {
            return color2.getColor(sr).mul(value);
        }
    }

    public void setColor(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    public void setTexture(Texture c2) {
        color2=c2.clone();
    }

    public void setExpansionNumber(double d) {
        expansionNumber=d;
    }
    public void setWrapFactor(double d) {
        expansionNumber=d;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
}
