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
import java.util.logging.Logger;

/**
 * Generates an NRooks distribution of samples.
 *
 * @author William Matrix Peckham
 */
public class NRooks extends Sampler {

    /**
     * default
     */
    public NRooks() {
        super();
    }

    /**
     * num samples sample
     *
     * @param num
     */
    public NRooks(int num) {
        super(num);
        generateSamples();
    }

    /**
     * copy constructor
     *
     * @param u
     */
    public NRooks(NRooks u) {
        super(u);
        //generateSamples();
    }

    /**
     * generate the NRooks samples
     */
    @Override
    public final void generateSamples() {
        for (int p = 0; p < numSets; p++) {
            for (int j = 0; j < numSamples; j++) {
                Point2D sp
                        = new Point2D((j + Utility.randDouble()) / numSamples,
                                (j + Utility.randDouble()) / numSamples);
                samples.add(sp);
            }
        }

        shuffleXCoordinates();
        shuffleYCoordinates();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Sampler protclone() {
        return new NRooks(this);
    }

    private static final Logger LOG = Logger.getLogger(NRooks.class.getName());

}
