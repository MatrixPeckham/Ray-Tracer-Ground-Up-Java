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
package com.matrixpeckham.raytracer.samplers;

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Multijittered samples.
 *
 * @author William Matrix Peckham
 */
public class MultiJittered extends Sampler {

    /**
     * default
     */
    public MultiJittered() {
        super();
    }

    /**
     * number of samples settable
     *
     * @param num
     */
    public MultiJittered(int num) {
        super(num);
        generateSamples();
    }

    /**
     * num is sample number and m is sets.
     *
     * @param num
     * @param m
     */
    public MultiJittered(int num, int m) {
        super(num, m);
        generateSamples();
    }

    /**
     * copy constructor
     *
     * @param u
     */
    public MultiJittered(MultiJittered u) {
        super(u);
        //generateSamples();
    }

    /**
     * generate samples that will be multijittered
     */
    @Override
    public void generateSamples() {
        // numSamples needs to be a perfect square

        int n = (int) Math.sqrt((double) numSamples);
        double subcell_width = 1.0 / ((double) numSamples);
        for (int j = 0; j < numSamples * numSets; j++) {
            samples.add(new Point2D());
        }
        // distribute points in the initial patterns
        for (int p = 0; p < numSets; p++) {
            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    double rand = Utility.randDouble(0,
                            subcell_width);
                    samples.get(j * n + i + p * numSamples).x = (i * n + j)
                            * subcell_width + rand;
                    rand = Utility.randDouble(0,
                            subcell_width);
                    samples.get(j * n + i + p * numSamples).y = (j * n + i)
                            * subcell_width + rand;
                }
            }
        }

        // shuffle x coordinates
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int k = Utility.randInt(j, n - 1);
                    double t = samples.get(i * n + j + p * numSamples).x;
                    samples.get(i * n + j + p * numSamples).x = samples.get(i
                            * n + k + p * numSamples).x;
                    samples.get(i * n + k + p * numSamples).x = t;
                }
            }
        }

        // shuffle y coordinates
        for (int p = 0; p < numSets; p++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int k = Utility.randInt(j, n - 1);
                    double t = samples.get(j * n + i + p * numSamples).y;
                    samples.get(j * n + i + p * numSamples).y = samples.get(k
                            * n + i + p * numSamples).y;
                    samples.get(k * n + i + p * numSamples).y = t;
                }
            }
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Sampler protclone() {
        return new MultiJittered(this);
    }

}
