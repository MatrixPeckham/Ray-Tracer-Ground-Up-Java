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
 *
 * @author William Matrix Peckham
 */
public abstract class LatticeNoise {

    static final int kTableSize = 256;
    static final int kTableMask = kTableSize - 1;
    static final int seed_value = 253;

    protected int numOctaves;
    protected double lacunarity;
    protected double gain;

    static protected short permutationTable[] = new short[]{
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
    protected double valueTable[] = new double[kTableSize];
    protected Vector3D vectorTable[] = new Vector3D[kTableSize];

    private double fbmMin;
    private double fbmMax;

    private void initValueTable(int seed) {
        Utility.setRandSeed(seed);
        for(int i = 0; i<kTableSize; i++){
            valueTable[i]=1-2*Utility.randDouble();
        }
    }

    private void initVectorTable(int seed) {
        double r1,r2,x,y,z,r,phi;
        Utility.setRandSeed(seed);
        MultiJittered sample = new MultiJittered(256, 1);
        for(int j = 0; j<kTableSize; j++){
            Point2D samplePoint = sample.sampleOneSet();
            r1=samplePoint.x;
            r2=samplePoint.y;
            z=1.0-2.0*r1;
            r=Math.sqrt(1.0-z*z);
            phi = Utility.TWO_PI*r2;
            x=r*Math.cos(phi);
            y=r*Math.sin(phi);
            vectorTable[j]=new Vector3D(x,y,z).hat();
        }
    }

    private void computeFBMBounds() {
        if(gain==1){
            fbmMax=numOctaves;
        } else {
            fbmMax=(1.0-Math.pow(gain, numOctaves))/(1.0-gain);
        }
        fbmMin=-fbmMax;
    }

    public LatticeNoise() {
        numOctaves=1;
        lacunarity=2;
        gain=0.5;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    public LatticeNoise(int octaves) {
        numOctaves=octaves;
        lacunarity=2;
        gain=0.5;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    public LatticeNoise(int octaves, double lacunarity, double gain) {
        numOctaves=octaves;
        this.lacunarity=lacunarity;
        this.gain=gain;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    public LatticeNoise(LatticeNoise n) {
        numOctaves=n.numOctaves;
        lacunarity=n.lacunarity;
        gain=n.gain;
        initValueTable(seed_value);
        initVectorTable(seed_value);
        computeFBMBounds();
    }

    public LatticeNoise setTo(LatticeNoise n) {
        numOctaves=n.numOctaves;
        lacunarity=n.lacunarity;
        gain=n.gain;
        return this;
    }

    public abstract LatticeNoise clone();

    public abstract double valueNoise(Point3D p);

    public abstract Vector3D vectorNoise(Point3D p);

    public double valueFractalSum(Point3D p) {
        double amplitude=1.0;
        double frequency=1.0;
        double fractalSum=0;
        for(int j=0; j<numOctaves; j++){
            fractalSum+=amplitude*valueNoise(p.mul(frequency));
            amplitude*=0.5;
            frequency*=2;
        }
        fractalSum=(fractalSum-fbmMin)/(fbmMax-fbmMin);
        return fractalSum;
    }

    public Vector3D vectorFractalSum(Point3D p) {
        double amplitude=1.0;
        double frequency=1.0;
        Vector3D fractalSum=new Vector3D(0,0,0);
        for(int j=0; j<numOctaves; j++){
            fractalSum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude*=0.5;
            frequency*=2;
        }
        return fractalSum;
    }
    
    public double valueTurbulence(Point3D p){
        double amplitude=1.0;
        double frequency=1.0;
        double turbulence=0;
        for(int j=0; j<numOctaves; j++){
            turbulence+=amplitude*Math.abs(valueNoise(p.mul(frequency)));
            //turbulence+=amplitude*Math.sqrt(Math.abs(valueNoise(p.mul(frequency)));
            amplitude*=0.5;
            frequency*=2;
        }
        turbulence/=fbmMax;
        return turbulence;
    }

    public double valueFBM(Point3D p) {
        double amplitude=1;
        double frequency=1;
        double fbm=0;
        for(int j=0; j<numOctaves; j++){
            fbm+=amplitude*valueNoise(p.mul(frequency));
            amplitude*=gain;
            frequency*=lacunarity;
        }
        fbm=(fbm-fbmMin)/(fbmMax-fbmMin);
        return fbm;
    }

    public Vector3D vectorFBM(Point3D p) {
        double amplitude=1;
        double frequency=1;
        Vector3D sum = new Vector3D(0, 0, 0);
        for(int j=0; j<numOctaves; j++){
            sum.addLocal(vectorNoise(p.mul(frequency)).mul(amplitude));
            amplitude*=gain;
            frequency*=lacunarity;
        }
        return sum;
    }

    public void setNumOctaves(int octaves) {
        numOctaves=octaves;
        computeFBMBounds();
    }

    public void setLacunarity(double lacunarity) {
        this.lacunarity=lacunarity;
    }

    public void setGain(double gain) {
        this.gain=gain;
        computeFBMBounds();
    }

    static int PERM(int x) {
        return permutationTable[(x) & kTableMask];
    }

    static int INDEX(int ix, int iy, int iz) {
        return PERM((ix) + PERM((iy) + PERM(iz)));
    }

    static int FLOOR(double x) {
        return ((int) Math.floor(x));
    }

}
