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
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Noise bump map texture.
 *
 * @author William Matrix Peckham
 */
public class FBMBump implements Texture {

    /**
     * noise value to use
     */
    private LatticeNoise noise = null;
    /**
     * perturbation value (scales noise vectors)
     */
    double perturbation = 1;

    /**
     * default constructor doesn't initialize anything.
     */
    public FBMBump() {
    }

    /**
     * initialize all fields.
     *
     * @param numOctaves
     * @param lacunarity
     * @param gain
     * @param perturbationAmount
     */
    public FBMBump(int numOctaves, double lacunarity, double gain,
            double perturbationAmount) {
        noise = new CubicNoise(numOctaves, lacunarity, gain);
        perturbation = perturbationAmount;
    }

    /**
     * initialize noise
     *
     * @param n
     */
    public FBMBump(LatticeNoise n) {
        noise = n.clone();
    }

    /**
     * copy constructor
     *
     * @param n
     */
    public FBMBump(FBMBump n) {
        noise = n.noise.clone();
        perturbation = n.perturbation;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture clone() {
        return new FBMBump(this);
    }

    /**
     * sample texture. Even though this function should return a vector it
     * returns a color as per the Texture.getColor() contract. this color is NOT
     * in the 0-1 range that regular colors will be. It will instead represent
     * an x,y,z vector with values -perturbation-perturbation as r,g,b.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        Vector3D v = noise.vectorFBM(sr.localHitPosition);
        v.normalize();
        v = v.mul(perturbation);
        return new RGBColor(v.x, v.y, v.z);
    }
}
