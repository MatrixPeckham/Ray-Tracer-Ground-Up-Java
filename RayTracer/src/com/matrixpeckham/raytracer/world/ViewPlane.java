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
package com.matrixpeckham.raytracer.world;

import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.samplers.Regular;
import com.matrixpeckham.raytracer.samplers.Sampler;

/**
 * Class that represents the viewport for the image. 
 * @author William Matrix Peckham
 */
public class ViewPlane {
    /**
     * Horizontal resolution.
     */
    public int hRes;
    /**
     * Vertical resolution. 
     */
    public int vRes;
    /**
     * Pixel size.
     */
    public double s;
    /**
     * samples per pixel
     */
    public int numSamples;
    /**
     * Sampler for pixel location.
     */
    public Sampler sampler = null;
    /**
     * Gamma correction.
     */
    public double gamma;
    /**
     * Inverse of gamma correction, computed once to avoid many divisions. 
     */
    public double invGamma;
    /**
     * Flag to show out of gamut colors as a solid known color. If true shows as red
     * otherwise normalizes color, third option of clamping color is not implemented,
     * but would be trivial to add. 
     */
    public boolean showOutOfGamut;//TODO: implement color clamping as well.
    /**
     * Maximum recursion depth for rays.
     */
    public int maxDepth=1;
    public Integer imageHeight=null;
    public Integer imageWidth=null;

    /**
     * Default constructor. 
     */
    public ViewPlane() {
        hRes=400;
        vRes=400;
        s=1;
        numSamples=1;
        gamma=1;
        invGamma=1;
        showOutOfGamut=false;
    }

    /**
     * Copy constructor.
     * @param vp 
     */
    public ViewPlane(ViewPlane vp) {
        hRes=vp.hRes;
        vRes=vp.vRes;
        s=vp.s;
        numSamples=vp.numSamples;
        sampler = vp.sampler.clone();
        gamma=vp.gamma;
        invGamma=vp.invGamma;
        showOutOfGamut=vp.showOutOfGamut;
    }

    /**
     * Java replacement for overridden = operator. 
     * @param vp
     * @return this reference
     */
    public ViewPlane setTo(ViewPlane vp) {
        hRes=vp.hRes;
        vRes=vp.vRes;
        s=vp.s;
        numSamples=vp.numSamples;
        gamma=vp.gamma;
        invGamma=vp.invGamma;
        showOutOfGamut=vp.showOutOfGamut;
        return this;
    }

    /**
     * Setter
     * @param h_res 
     */
    public void setHres(int h_res){hRes=h_res;}
    /**
     * Setter
     * @param v_res 
     */
    public void setVres(int v_res){vRes=v_res;}
    /**
     * Setter
     * @param size 
     */
    public void setPixelSize(double size){
        s=size;
    }
    /**
     * Sets gamma and computes invGamma
     * @param g 
     */
    public void setGamma(double g){
        gamma=g;
        invGamma=1.0f/gamma;
    }
    /**
     * Setter
     * @param show 
     */
    public void setGamutDisplay(boolean show){
        showOutOfGamut=show;
    }
    
    /**
     * Sets the number of samples, if n>1 creates a new multi-jittered sampler
     * with that number of samples. Otherwise creates a regular sampler with one 
     * sample.  
     * @param n 
     */
    public void setSamples(int n){
        numSamples=n;
        if(numSamples>1){
            sampler=new MultiJittered(numSamples);
        } else {
            sampler=new Regular(1);
        }
    }
    /**
     * Sets the sampler for the viewport, also sets the number of samples as 
     * the number of samples in the sampler
     * @param s 
     */
    public void setSampler(Sampler s){
        sampler = s;
        numSamples=sampler.getNumSamples();
    }
    /**
     * Setter.
     * @param i 
     */
    public void setMaxDepth(int i) {
        maxDepth=i;
    }
}
