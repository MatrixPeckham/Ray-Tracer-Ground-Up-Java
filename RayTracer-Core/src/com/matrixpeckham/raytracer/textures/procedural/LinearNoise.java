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

import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Linear Noise, linearly interpolates the noise values.
 *
 * @author William Matrix Peckham
 */
public class LinearNoise extends LatticeNoise {

    /**
     * default
     */
    public LinearNoise() {

    }

    /**
     * pass to super constructor
     *
     * @param octaves
     * @param lacunarity
     * @param gain
     */
    public LinearNoise(int octaves, double lacunarity, double gain) {
        super(octaves, lacunarity, gain);
    }

    /**
     * copy constructor, forwards to super class
     *
     * @param n
     */
    public LinearNoise(LinearNoise n) {
        super(n);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public LatticeNoise cloneNoise() {
        return this;//new LinearNoise(this);
    }

    /**
     * set to, passes to super class
     *
     * @param n
     * @return
     */
    @Override
    public Noise setTo(Noise n) {
        return super.setTo(n); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * linear interpolation implementation for values
     *
     * @param p
     * @return
     */
    @Override
    public double valueNoise(Point3D p) {

        int ix, iy, iz;
        double fx, fy, fz;
        double[][][] d = new double[2][2][2];
        double x0, x1, x2, x3, y0, y1, z0;

        ix = FLOOR(p.x);
        fx = p.x - ix;

        iy = FLOOR(p.y);
        fy = p.y - iy;

        iz = FLOOR(p.z);
        fz = p.z - iz;

        for (int k = 0; k <= 1; k++) {
            for (int j = 0; j <= 1; j++) {
                for (int i = 0; i <= 1; i++) {
                    d[k][j][i] = valueTable[INDEX(ix + i, iy + j, iz + k)];
                }
            }
        }

        x0 = lerp(fx, d[0][0][0], d[0][0][1]);
        x1 = lerp(fx, d[0][1][0], d[0][1][1]);
        x2 = lerp(fx, d[1][0][0], d[1][0][1]);
        x3 = lerp(fx, d[1][1][0], d[1][1][1]);
        y0 = lerp(fy, x0, x1);
        y1 = lerp(fy, x2, x3);
        z0 = lerp(fz, y0, y1);

        return (z0);
    }

    /**
     * linear interpolation implementation of vector noise
     *
     * @param p
     * @return
     */
    @Override
    public Vector3D vectorNoise(Point3D p) {
        int ix, iy, iz;
        double fx, fy, fz;
        Vector3D[][][] d = new Vector3D[2][2][2];
        Vector3D x0, x1, x2, x3, y0, y1, z0;

        ix = FLOOR(p.x);
        fx = p.x - ix;

        iy = FLOOR(p.y);
        fy = p.y - iy;

        iz = FLOOR(p.z);
        fz = p.z - iz;

        for (int k = 0; k <= 1; k++) {
            for (int j = 0; j <= 1; j++) {
                for (int i = 0; i <= 1; i++) {
                    d[k][j][i] = vectorTable[INDEX(ix + i, iy + j, iz + k)];
                }
            }
        }

        x0 = lerp(fx, d[0][0][0], d[0][0][1]);
        x1 = lerp(fx, d[0][1][0], d[0][1][1]);
        x2 = lerp(fx, d[1][0][0], d[1][0][1]);
        x3 = lerp(fx, d[1][1][0], d[1][1][1]);
        y0 = lerp(fy, x0, x1);
        y1 = lerp(fy, x2, x3);
        z0 = lerp(fz, y0, y1);

        return (z0);
    }

    /**
     * lerp from a-b as f from 0 to 1
     *
     * @param f
     * @param a
     * @param b
     * @return
     */
    static double lerp(double f, double a, double b) {
        return (a + f * (b - a));
    }

    /**
     * lerp vectors a-b from f 0 to 1
     *
     * @param f
     * @param a
     * @param b
     * @return
     */
    static Vector3D lerp(double f, Vector3D a, Vector3D b) {
        return a.add(b.sub(a).mul(f));
    }

    private static final Logger LOG
            = Logger.getLogger(LinearNoise.class.getName());

}
