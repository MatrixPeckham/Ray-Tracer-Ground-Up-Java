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
import com.matrixpeckham.raytracer.textures.image.Image;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class WrappedRamp implements Texture {
    private LatticeNoise noise=null;
    private Image ramp=null;
    private double minValue;
    private double maxValue;
    private double perturbation=0;
    private double expansionNumber;
    
    public WrappedRamp(){
    }
    
    public WrappedRamp(LatticeNoise noise){
    }
    
    public WrappedRamp(Image col){
        this(col,0.0,1.0);
    }
    public WrappedRamp(Image col, double min, double max){
        this(col,min,max,2,new LinearNoise());
    }
    public WrappedRamp(Image col, double min, double max, double num, LatticeNoise n){
        ramp=col;
        minValue=min;
        maxValue=max;
        noise=n;
        expansionNumber=num;
    }
    
    public WrappedRamp(WrappedRamp t){
        this.ramp=t.ramp;
        this.maxValue=t.maxValue;
        this.minValue=t.minValue;
        this.noise=t.noise.clone();
        this.expansionNumber=t.expansionNumber;
    }

    @Override
    public Texture clone() {
        return new WrappedRamp(this);
    }

    
    @Override
    public RGBColor getColor(ShadeRec sr){
        double value = expansionNumber * noise.valueFBM(sr.localHitPosition);
        value=value-Math.floor(value);
        value=minValue+(maxValue-minValue)*value;
        return ramp.getColor(0, (int)(value*(ramp.getHres()-1)));
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
    
    public void setNoise(LatticeNoise noise){
        this.noise=noise.clone();
    }

    public void setPerturbation(double perturbation) {
        this.perturbation = perturbation;
    }

    public void setWrapNumber(double d) {
        expansionNumber=d;
    }
    
}
