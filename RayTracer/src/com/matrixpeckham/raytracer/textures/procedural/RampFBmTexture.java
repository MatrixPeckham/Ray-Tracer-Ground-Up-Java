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
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;

/**
 *
 * @author William Matrix Peckham
 */
public class RampFBmTexture implements Texture {
    private Image ramp;
    LatticeNoise noise;
    double perturbation;
    int hres;

    public RampFBmTexture(Image ramp, LatticeNoise noise, double perturbation,
            int hres) {
        this.ramp = ramp;
        this.noise = noise;
        this.perturbation = perturbation;
        this.hres = hres;
    }

    public RampFBmTexture(Image imagePtr1) {
        this(imagePtr1, new CubicNoise(), 2, imagePtr1.getHres());
    }

    @Override
    public Texture clone() {
        return new RampFBmTexture(ramp, noise, perturbation, hres);
    }
    
    

    @Override
    public RGBColor getColor(ShadeRec sr) {
        double n = noise.valueFBM(sr.localHitPosition);
        double y = sr.localHitPosition.y + perturbation*n;
        double u = (1.0-Math.sin(y))/2.0;
        int row=0;
        int col = (int)(u*hres-1);
        return ramp.getColor(row, col);
    }

    public void setNumOctaves(int numOctaves) {
        noise.setNumOctaves(numOctaves);
    }

    public void setLacunarity(double lacunarity) {
        noise.setLacunarity(lacunarity);
    }

    public void setGain(double gain) {
        noise.setGain(gain);
    }

    public void setPerturbation(double perturbation) {
        this.perturbation=perturbation;
    }
    
    
}
