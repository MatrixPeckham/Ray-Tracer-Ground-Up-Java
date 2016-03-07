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
import java.util.logging.Logger;

/**
 * Generates Hammersley samples. Not random follows a mathematical pattern,
 *
 * @author William Matrix Peckham
 */
public class Hammersley extends Sampler {

    /**
     * default
     */
    public Hammersley() {
        super();
    }

    /**
     * set number of samples
     *
     * @param num
     */
    public Hammersley(int num) {
        super(num);
        generateSamples();
    }

    /**
     * copy constructor
     *
     * @param u
     */
    public Hammersley(Hammersley u) {
        super(u);
        //generateSamples();
    }

    /**
     * generates hammersley samples
     */
    @Override
    public final void generateSamples() {

        for (int p = 0; p < numSets; p++) {
            for (int j = 0; j < numSamples; j++) {
                samples.add(new Point2D(j / (double) numSamples, phi(j)));
            }
        }
    }

    /**
     * clone
     *
     * @return
     */
    @Override
<<<<<<< HEAD
    public Sampler protclone() {
=======
    public Sampler cloneSampler() {
>>>>>>> refs/remotes/origin/master
        return new Hammersley(this);
    }

    private double phi(int j) {
        double x = 0;
        double f = 0.5;
        while (j != 0) {
            x += f * (j % 2);
            j /= 2;
            f *= 0.5;
        }
        return x;
    }

    private static final Logger LOG
            = Logger.getLogger(Hammersley.class.getName());

}
