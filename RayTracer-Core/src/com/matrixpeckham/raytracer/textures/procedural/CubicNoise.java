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
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import java.util.logging.Logger;

/**
 * Cubic interpolation implementation of lattice noise.
 *
 * @author William Matrix Peckham
 */
public class CubicNoise extends LatticeNoise {

    /**
     * default constructor
     */
    public CubicNoise() {

    }

    /**
     * Forward to super class
     *
     * @param octaves
     * @param lacunarity
     * @param gain
     */
    public CubicNoise(int octaves, double lacunarity, double gain) {
        super(octaves, lacunarity, gain);
    }

    /**
     * copy constructor, forwards to superclass
     *
     * @param n
     */
    public CubicNoise(CubicNoise n) {
        super(n);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public LatticeNoise cloneNoise() {
        return this;//new CubicNoise(this);
    }

    /**
     * set to method forwards to superclass
     *
     * @param n
     * @return
     */
    @Override
    public Noise setTo(Noise n) {
        return super.setTo(n); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Cubic interpolation of values
     *
     * @param p
     * @return
     */
    @Override
    public double valueNoise(Point3D p) {
        int ix, iy, iz;
        double fx, fy, fz;
        double[] xknots = new double[4], yknots = new double[4], zknots
                = new double[4];

        ix = FLOOR(p.x);
        fx = p.x - ix;

        iy = FLOOR(p.y);
        fy = p.y - iy;

        iz = FLOOR(p.z);
        fz = p.z - iz;

        for (int k = -1; k <= 2; k++) {
            for (int j = -1; j <= 2; j++) {
                for (int i = -1; i <= 2; i++) {
                    xknots[i + 1] = valueTable[INDEX(ix + i, iy + j, iz + k)];
                }
                yknots[j + 1] = four_knot_spline(fx, xknots);
            }
            zknots[k + 1] = four_knot_spline(fy, yknots);
        }

        return (Utility.clamp(four_knot_spline(fz, zknots), -1.0, 1.0));
    }

    /**
     * cubic interpolation of vectors
     *
     * @param p
     * @return
     */
    @Override
    public Vector3D vectorNoise(Point3D p) {
        int ix, iy, iz;
        double fx, fy, fz;
        Vector3D[] xknots = new Vector3D[4], yknots = new Vector3D[4], zknots
                = new Vector3D[4];

        ix = FLOOR(p.x);
        fx = p.x - ix;

        iy = FLOOR(p.y);
        fy = p.y - iy;

        iz = FLOOR(p.z);
        fz = p.z - iz;

        for (int k = -1; k <= 2; k++) {
            for (int j = -1; j <= 2; j++) {
                for (int i = -1; i <= 2; i++) {
                    xknots[i + 1] = vectorTable[INDEX(ix + i, iy + j, iz + k)];
                }
                yknots[j + 1] = four_knot_spline(fx, xknots);
            }
            zknots[k + 1] = four_knot_spline(fy, yknots);
        }

        return (four_knot_spline(fz, zknots));
    }

    /**
     * does the cubic interpolation
     *
     * @param x
     * @param knots
     * @return
     */
    double four_knot_spline(double x, double[] knots) {
        double c3 = -0.5 * knots[0] + 1.5 * knots[1] - 1.5 * knots[2] + 0.5
                * knots[3];
        double c2 = knots[0] - 2.5 * knots[1] + 2.0 * knots[2] - 0.5 * knots[3];
        double c1 = 0.5 * (-knots[0] + knots[2]);
        double c0 = knots[1];

        return (((c3 * x + c2) * x + c1) * x + c0);
    }

    /**
     * does the cubic interpolation
     *
     * @param x
     * @param knots
     * @return
     */
    Vector3D four_knot_spline(double x, Vector3D[] knots) {
        Vector3D c3 = knots[0].mul(-0.5).add(knots[1].mul(1.5)).sub(knots[2].
                mul(1.5)).add(knots[3].mul(0.5));
        Vector3D c2 = knots[0].sub(knots[1].mul(2.5)).add(knots[2].mul(2.0)).
                sub(knots[3].mul(0.5));
        Vector3D c1 = (knots[0].neg().add(knots[2])).mul(0.5);
        Vector3D c0 = knots[1];

        return (((c3.mul(x).add(c2).mul(x)).add(c1)).mul(x).add(c0));
    }

    private static final Logger LOG
            = Logger.getLogger(CubicNoise.class.getName());

}
