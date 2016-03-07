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
import java.util.logging.Logger;

/**
 * Texture for looping the ramp through the y coordinate, and perturb it with
 * the noise value.
 *
 * @author William Matrix Peckham
 */
public class RampFBmTexture implements Texture {

    /**
     * ramp image
     */
    private Image ramp;

    /**
     * noise
     */
    LatticeNoise noise;

    /**
     * perturbation value
     */
    double perturbation;

    /**
     * Makes a new texture with the provided values
     *
     * @param ramp
     * @param noise
     * @param perturbation
     */
    public RampFBmTexture(Image ramp, LatticeNoise noise, double perturbation) {
        this.ramp = ramp;
        this.noise = noise;
        this.perturbation = perturbation;
    }

    /**
     * Makes a new texture with the provided values.
     *
     * @param imagePtr
     * @param octaves
     * @param fbmamount
     */
    public RampFBmTexture(Image imagePtr, int octaves, double fbmamount) {
        this(imagePtr, new CubicNoise(octaves, 2, 0.5), fbmamount);
    }

    /**
     * Makes a new texture with the ramp
     *
     * @param imagePtr1
     */
    public RampFBmTexture(Image imagePtr1) {
        this(imagePtr1, new CubicNoise(), 2);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new RampFBmTexture(ramp, noise, perturbation);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //sample noise
        double n = noise.valueFBM(sr.localHitPosition);
        //perturb y coordinate of hit point
        double y = sr.localHitPosition.y + perturbation * n;
        //normalize sine wave of y coordinate
        double u = (1.0 + Math.sin(y)) / 2.0;
        //color ramp always zero row
        int row = 0;
        //sample the image
        int col = (int) (u * ramp.getHres() - 1);
        return ramp.getColor(row, col);
    }

    /**
     * setter
     *
     * @param numOctaves
     */
    public void setNumOctaves(int numOctaves) {
        noise.setNumOctaves(numOctaves);
    }

    /**
     * setter
     *
     * @param lacunarity
     */
    public void setLacunarity(double lacunarity) {
        noise.setLacunarity(lacunarity);
    }

    /**
     * setter
     *
     * @param gain
     */
    public void setGain(double gain) {
        noise.setGain(gain);
    }

    /**
     * setter
     *
     * @param perturbation
     */
    public void setPerturbation(double perturbation) {
        this.perturbation = perturbation;
    }

    /**
     * setter
     *
     * @param noise
     */
    public void setNoise(LatticeNoise noise) {
        this.noise = noise;
    }

    private static final Logger LOG
            = Logger.getLogger(RampFBmTexture.class.getName());

}
