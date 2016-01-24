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
public class WrappedFBmTexture implements Texture {
    private LatticeNoise noise=null;
    private RGBColor color=new RGBColor();
    private double minValue;
    private double maxValue;
    private double expansionNumber;
    
    public WrappedFBmTexture(){
        this(Utility.WHITE);
    }
    
    public WrappedFBmTexture(LatticeNoise noise){
        this(Utility.WHITE,0,1,2,noise);
    }
    
    public WrappedFBmTexture(RGBColor col){
        this(col,0.0,1.0);
    }
    public WrappedFBmTexture(RGBColor col, double min, double max){
        this(col,min,max,2,new LinearNoise());
    }
    public WrappedFBmTexture(RGBColor col, double min, double max, double num, LatticeNoise n){
        color.setTo(col);
        minValue=min;
        maxValue=max;
        noise=n;
        expansionNumber=num;
    }
    
    public WrappedFBmTexture(WrappedFBmTexture t){
        this.color.setTo(t.color);
        this.maxValue=t.maxValue;
        this.minValue=t.minValue;
        this.noise=t.noise.clone();
        this.expansionNumber=t.expansionNumber;
    }

    @Override
    public Texture clone() {
        return new WrappedFBmTexture(this);
    }

    
    public RGBColor getColor(ShadeRec sr){
        double value = expansionNumber * noise.valueFBM(sr.localHitPosition);
        value=value-Math.floor(value);
        value=minValue+(maxValue-minValue)*value;
        return color.mul(value);
    }

    public void setColor(double d, double d0, double d1) {
        color.setTo(d, d0, d1);
    }

    public void setExpansionNumber(double d) {
        expansionNumber=d;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
}
