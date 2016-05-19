/*
 * Copyright (C) 2016 William Matrix Peckham
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

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public abstract class Noise {

    /**
     * seed value for random generation
     */
    static final long seed_value = 253;

    /**
     * max value this noise will produce
     */
    protected double fbmMax;

    /**
     * min value this noise will produce
     */
    protected double fbmMin;

    /**
     * amplitude modulator
     */
    protected double gain;

    /**
     * frequency modulator
     */
    protected double lacunarity;

    /**
     * octaves for fractal sum and FBM
     */
    protected int numOctaves;

    public Noise() {
    }

    public Noise(double fbmMax, double fbmMin, double gain, double lacunarity,
            int numOctaves) {
        this.fbmMax = fbmMax;
        this.fbmMin = fbmMin;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.numOctaves = numOctaves;
    }

    public Noise(Noise n) {
        this(n.fbmMax, n.fbmMin, n.gain, n.lacunarity, n.numOctaves);
    }

    /**
     * clone method.
     *
     * @return
     */
    public abstract Noise cloneNoise();

    /**
     * computes the bounds of this noise function.
     */
    protected void computeFBMBounds() {
        if (gain == 1) {
            fbmMax = numOctaves;
        } else {
            fbmMax = (1.0 - Math.pow(gain, numOctaves)) / (1.0 - gain);
        }
        fbmMin = -fbmMax;
    }

    /**
     * sets the gain and re-computes the bounds
     *
     * @param gain
     */
    public void setGain(double gain) {
        this.gain = gain;
        computeFBMBounds();
    }

    /**
     * sets the lacunarity
     *
     * @param lacunarity
     */
    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * sets the octaves and recomutes the bounds
     *
     * @param octaves
     */
    public void setNumOctaves(int octaves) {
        numOctaves = octaves;
        computeFBMBounds();
    }

    /**
     * set to method
     *
     * @param n
     * @return
     */
    public Noise setTo(Noise n) {
        numOctaves = n.numOctaves;
        lacunarity = n.lacunarity;
        gain = n.gain;
        return this;
    }

    /**
     * samples noise at point for Fractal Brownian Motion, gain and lacunarity
     * parameterized.
     *
     * @param p
     * @return
     */
    public double valueFBM(Point3D p) {
        double amplitude = 1;
        double frequency = 1;
        double fbm = 0;
        for (int j = 0; j < numOctaves;
                j++) {
            fbm += amplitude * valueNoise(p.mul(frequency));
            amplitude *= gain;
            frequency *= lacunarity;
        }
        fbm = (fbm - fbmMin) / (fbmMax - fbmMin);
        return fbm;
    }

    /**
     * special case of FBM value noise
     *
     * @param p
     * @return
     */
    public double valueFractalSum(Point3D p) {
        double amplitude = 1.0;
        double frequency = 1.0;
        double fractalSum = 0;
        for (int j = 0; j < numOctaves;
                j++) {
            fractalSum += amplitude * valueNoise(p.mul(frequency));
            amplitude *= 0.5;
            frequency *= 2;
        }
        fractalSum = (fractalSum - fbmMin) / (fbmMax - fbmMin);
        return fractalSum;
    }

    /**
     * gets the value noise from this noise. overriding methods are for
     * different interpolations
     *
     * @param p
     * @return
     */
    public abstract double valueNoise(Point3D p);

    /**
     * same as value FBM, but the noise values are all absolute values
     *
     * @param p
     * @return
     */
    public double valueTurbulence(Point3D p) {
        double amplitude = 1.0;
        double frequency = 1.0;
        double turbulence = 0;
        for (int j = 0; j < numOctaves;
                j++) {
            turbulence += amplitude * Math.abs(valueNoise(p.mul(frequency)));
            //turbulence+=amplitude*Math.sqrt(Math.abs(valueNoise(p.mul(frequency)));
            amplitude *= 0.5;
            frequency *= 2;
        }
        turbulence /= fbmMax;
        return turbulence;
    }

    /**
     * gets a random vector from this noise as Fractal Brownian noise
     *
     * @param p
     * @return
     */
    public Vector3D vectorFBM(Point3D p) {
        double amplitude = 1;
        double frequency = 1;
        Vector3D sum = new Vector3D(0, 0, 0);
        for (int j = 0; j < numOctaves;
                j++) {
            sum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return sum;
    }

    /**
     * special case of FBM vector noise
     *
     * @param p
     * @return
     */
    public Vector3D vectorFractalSum(Point3D p) {
        double amplitude = 1.0;
        double frequency = 1.0;
        Vector3D fractalSum = new Vector3D(0, 0, 0);
        for (int j = 0; j < numOctaves;
                j++) {
            fractalSum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude *= 0.5;
            frequency *= 2;
        }
        return fractalSum;
    }

    /**
     * gats the vector noise from this noise. overriding methods are for
     * different interpolations
     *
     * @param p
     * @return
     */
    public abstract Vector3D vectorNoise(Point3D p);

}
