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
public class TurbulenceTexture implements Texture {
    private LatticeNoise noise=null;
    private RGBColor color=new RGBColor();
    private double minValue;
    private double maxValue;
    
    public TurbulenceTexture(){
        this(Utility.WHITE);
    }
    public TurbulenceTexture(RGBColor col){
        this(col,0.0,1.0);
    }
    public TurbulenceTexture(LatticeNoise n){
        this(Utility.WHITE,0,1,n);
    }
    public TurbulenceTexture(RGBColor col, double min, double max){
        this(col,min,max,new LinearNoise());
    }
    public TurbulenceTexture(RGBColor col, double min, double max, LatticeNoise n){
        color.setTo(col);
        minValue=min;
        maxValue=max;
        noise=n;
    }
    
    public TurbulenceTexture(TurbulenceTexture t){
        this.color.setTo(t.color);
        this.maxValue=t.maxValue;
        this.minValue=t.minValue;
        this.noise=t.noise.clone();
    }

    @Override
    public Texture clone() {
        return new TurbulenceTexture(this);
    }

    
    @Override
    public RGBColor getColor(ShadeRec sr){
        double value = noise.valueTurbulence(sr.localHitPosition);
        value=minValue+(maxValue-minValue)*value;
        return color.mul(value);
    }

    public void setColor(RGBColor color) {
        this.color.setTo(color);
    }
    public void setColor(double r, double g, double b) {
        this.color.setTo(r,g,b);
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    
}
