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

import com.matrixpeckham.raytracer.samplers.MultiJittered;
import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 * Class for location based noise functions.
 *
 * @author William Matrix Peckham
 */
public abstract class LatticeNoise {

    /**
     * table size
     */
    static final int kTableSize = 256;

    /**
     * table mask.
     */
    static final int kTableMask = kTableSize - 1;

    /**
     * seed value for random generation
     */
    static final int seed_value = 253;

    /**
     * octaves for fractal sum and FBM
     */
    protected int numOctaves;

    /**
     * frequency modulator
     */
    protected double lacunarity;

    /**
     * amplitude modulator
     */
    protected double gain;

    /**
     * permutation table for scrambling indices.
     */
    static final short permutationTable[] = new short[]{
        225, 155, 210, 108, 175, 199, 221, 144, 203, 116, 70, 213, 69, 158, 33,
        252,
        5, 82, 173, 133, 222, 139, 174, 27, 9, 71, 90, 246, 75, 130, 91, 191,
        169, 138, 2, 151, 194, 235, 81, 7, 25, 113, 228, 159, 205, 253, 134, 142,
        248, 65, 224, 217, 22, 121, 229, 63, 89, 103, 96, 104, 156, 17, 201, 129,
        36, 8, 165, 110, 237, 117, 231, 56, 132, 211, 152, 20, 181, 111, 239,
        218,
        170, 163, 51, 172, 157, 47, 80, 212, 176, 250, 87, 49, 99, 242, 136, 189,
        162, 115, 44, 43, 124, 94, 150, 16, 141, 247, 32, 10, 198, 223, 255, 72,
        53, 131, 84, 57, 220, 197, 58, 50, 208, 11, 241, 28, 3, 192, 62, 202,
        18, 215, 153, 24, 76, 41, 15, 179, 39, 46, 55, 6, 128, 167, 23, 188,
        106, 34, 187, 140, 164, 73, 112, 182, 244, 195, 227, 13, 35, 77, 196,
        185,
        26, 200, 226, 119, 31, 123, 168, 125, 249, 68, 183, 230, 177, 135, 160,
        180,
        12, 1, 243, 148, 102, 166, 38, 238, 251, 37, 240, 126, 64, 74, 161, 40,
        184, 149, 171, 178, 101, 66, 29, 59, 146, 61, 254, 107, 42, 86, 154, 4,
        236, 232, 120, 21, 233, 209, 45, 98, 193, 114, 78, 19, 206, 14, 118, 127,
        48, 79, 147, 85, 30, 207, 219, 54, 88, 234, 190, 122, 95, 67, 143, 109,
        137, 214, 145, 93, 92, 100, 245, 0, 216, 186, 60, 83, 105, 97, 204, 52
    };

    /**
     * value table
     */
    protected double valueTable[] = new double[kTableSize];

    /**
     * vector table
     */
    protected Vector3D vectorTable[] = new Vector3D[kTableSize];

    /**
     * min value this noise will produce
     */
    private double fbmMin;

    /**
     * max value this noise will produce
     */
    private double fbmMax;

    /**
     * initializes value table
     *
     * @param seed
     */
    private void initValueTable(int seed) {
        Utility.setRandSeed(seed);
        for (int i = 0; i < kTableSize; i++) {
            valueTable[i] = 1 - 2 * Utility.randDouble();
        }
    }

    /**
     * fills the vector table
     *
     * @param seed
     */
    private void initVectorTable(int seed) {
        double r1, r2, x, y, z, r, phi;
        Utility.setRandSeed(seed);
        MultiJittered sample = new MultiJittered(256, 1);
        for (int j = 0; j < kTableSize; j++) {
            Point2D samplePoint = sample.sampleOneSet();
            r1 = samplePoint.x;
            r2 = samplePoint.y;
            z = 1.0 - 2.0 * r1;
            r = Math.sqrt(1.0 - z * z);
            phi = Utility.TWO_PI * r2;
            x = r * Math.cos(phi);
            y = r * Math.sin(phi);
            vectorTable[j] = new Vector3D(x, y, z).hat();
        }
    }

    /**
     * computes the bounds of this noise function.
     */
    private void computeFBMBounds() {
        if (gain == 1) {
            fbmMax = numOctaves;
        } else {
            fbmMax = (1.0 - Math.pow(gain, numOctaves)) / (1.0 - gain);
        }
        fbmMin = -fbmMax;
    }

    /**
     * default constructor
     */
    public LatticeNoise() {
        numOctaves = 1;
        lacunarity = 2;
        gain = 0.5;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    /**
     * constructor sets octaves.
     *
     * @param octaves
     */
    public LatticeNoise(int octaves) {
        numOctaves = octaves;
        lacunarity = 2;
        gain = 0.5;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    /**
     * constructor for specifying values
     *
     * @param octaves
     * @param lacunarity
     * @param gain
     */
    public LatticeNoise(int octaves, double lacunarity, double gain) {
        numOctaves = octaves;
        this.lacunarity = lacunarity;
        this.gain = gain;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    /**
     * copy constructor
     *
     * @param n
     */
    public LatticeNoise(LatticeNoise n) {
        numOctaves = n.numOctaves;
        lacunarity = n.lacunarity;
        gain = n.gain;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    /**
     * set to method
     *
     * @param n
     * @return
     */
    public LatticeNoise setTo(LatticeNoise n) {
        numOctaves = n.numOctaves;
        lacunarity = n.lacunarity;
        gain = n.gain;
        return this;
    }

    /**
     * clone method.
     *
     * @return
     */
    public abstract LatticeNoise cloneNoise();

    /**
     * gets the value noise from this noise. overriding methods are for
     * different interpolations
     *
     * @param p
     * @return
     */
    public abstract double valueNoise(Point3D p);

    /**
     * gats the vector noise from this noise. overriding methods are for
     * different interpolations
     *
     * @param p
     * @return
     */
    public abstract Vector3D vectorNoise(Point3D p);

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
        for (int j = 0; j < numOctaves; j++) {
            fractalSum += amplitude * valueNoise(p.mul(frequency));
            amplitude *= 0.5;
            frequency *= 2;
        }
        fractalSum = (fractalSum - fbmMin) / (fbmMax - fbmMin);
        return fractalSum;
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
        for (int j = 0; j < numOctaves; j++) {
            fractalSum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude *= 0.5;
            frequency *= 2;
        }
        return fractalSum;
    }

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
        for (int j = 0; j < numOctaves; j++) {
            turbulence += amplitude * Math.abs(valueNoise(p.mul(frequency)));
            //turbulence+=amplitude*Math.sqrt(Math.abs(valueNoise(p.mul(frequency)));
            amplitude *= 0.5;
            frequency *= 2;
        }
        turbulence /= fbmMax;
        return turbulence;
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
        for (int j = 0; j < numOctaves; j++) {
            fbm += amplitude * valueNoise(p.mul(frequency));
            amplitude *= gain;
            frequency *= lacunarity;
        }
        fbm = (fbm - fbmMin) / (fbmMax - fbmMin);
        return fbm;
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
        for (int j = 0; j < numOctaves; j++) {
            sum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude *= gain;
            frequency *= lacunarity;
        }
        return sum;
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
     * sets the lacunarity
     *
     * @param lacunarity
     */
    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
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
     * Uses the permutation table to scramble the input
     *
     * @param x
     * @return
     */
    static int PERM(int x) {
        return permutationTable[(x) & kTableMask];
    }

    /**
     * Gets the index of the permutation table from the integer coordinates
     *
     * @param ix
     * @param iy
     * @param iz
     * @return
     */
    static int INDEX(int ix, int iy, int iz) {
        return PERM((ix) + PERM((iy) + PERM(iz)));
    }

    /**
     * Floors the parameter directly to an integer
     *
     * @param x
     * @return
     */
    static int FLOOR(double x) {
        return ((int) Math.floor(x));
    }

}
