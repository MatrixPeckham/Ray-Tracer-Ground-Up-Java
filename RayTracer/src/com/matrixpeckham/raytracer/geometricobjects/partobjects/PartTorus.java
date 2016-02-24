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
package com.matrixpeckham.raytracer.geometricobjects.partobjects;

import com.matrixpeckham.raytracer.geometricobjects.parametric.Torus;
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Part Torus class.
 *
 * @author William Matrix Peckham
 */
public class PartTorus extends Torus {
    //extends torus which is a parametric object so this implementation is simple

    /**
     * we extend torus's parametric function. and override its extents to be the
     * ones provided to the part torus
     */
    protected static class PartTorusParam extends Torus.TorusParametric {

        //extents

        double phiMin, phiMax, thetaMin, thetaMax;

        /**
         * torus with radii and full extents for full torus
         *
         * @param a
         * @param b
         */
        public PartTorusParam(double a, double b) {
            this(a, b, 0, Utility.TWO_PI, 0, Utility.TWO_PI);
        }

        /**
         * Constructor that takes all parameters (in degrees)
         *
         * @param a
         * @param b
         * @param phiMin
         * @param phiMax
         * @param thetaMin
         * @param thetaMax
         */
        public PartTorusParam(double a, double b, double phiMin, double phiMax,
                double thetaMin, double thetaMax) {
            super(a, b);
            this.phiMax = phiMax * Utility.PI_ON_180;
            this.phiMin = phiMin * Utility.PI_ON_180;
            this.thetaMax = thetaMax * Utility.PI_ON_180;
            this.thetaMin = thetaMin * Utility.PI_ON_180;
        }

        /**
         * required getter
         *
         * @return
         */
        @Override
        public double getMaxU() {
            return phiMax;
        }

        /**
         * required getter
         *
         * @return
         */
        @Override
        public double getMaxV() {
            return thetaMax;
        }

        /**
         * required getter
         *
         * @return
         */
        @Override
        public double getMinU() {
            return phiMin;
        }

        /**
         * required getter
         *
         * @return
         */
        @Override
        public double getMinV() {
            return thetaMin;
        }

        /**
         * required getter overrides torus default cause we're open
         *
         * @return
         */
        @Override
        public NormalType getNormalType() {
            return NormalType.TWO_SIDE;
        }

        /**
         * required getter overrides torus default
         *
         * @return
         */
        @Override
        public boolean isClosedU() {
            return false;
        }

        /**
         * required getter overrides torus default
         *
         * @return
         */
        @Override
        public boolean isClosedV() {
            return false;
        }

    }

    /**
     * default constructor
     */
    public PartTorus() {
        super(new PartTorusParam(1, 0.5));
    }

    /**
     * constructor with radii parameters
     *
     * @param a
     * @param b
     */
    public PartTorus(double a, double b) {
        super(new PartTorusParam(a, b));
    }

    /**
     * all parameter constructor (degrees)
     *
     * @param a
     * @param b
     * @param phiMin
     * @param phiMax
     * @param thetaMin
     * @param thetaMax
     */
    public PartTorus(double a, double b, double phiMin, double phiMax,
            double thetaMin, double thetaMax) {
        super(new PartTorusParam(a, b, phiMin, phiMax, thetaMin, thetaMax));
    }
}
