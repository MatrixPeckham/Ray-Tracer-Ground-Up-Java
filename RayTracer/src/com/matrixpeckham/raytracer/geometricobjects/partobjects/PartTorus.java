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
 *
 * @author William Matrix Peckham
 */
public class PartTorus extends Torus {
    protected static class PartTorusParam extends Torus.TorusParametric {
        double phiMin,phiMax,thetaMin,thetaMax;
        public PartTorusParam(double a, double b){
            this(a,b,0,Utility.TWO_PI,0,Utility.TWO_PI);
        }
        public PartTorusParam(double a, double b, double phiMin, double phiMax, double thetaMin, double thetaMax) {
            super(a, b);
            this.phiMax=phiMax*Utility.PI_ON_180;
            this.phiMin=phiMin*Utility.PI_ON_180;
            this.thetaMax=thetaMax*Utility.PI_ON_180;
            this.thetaMin=thetaMin*Utility.PI_ON_180;
        }

        @Override
        public double getMaxU() {
            return phiMax;
        }

        @Override
        public double getMaxV() {
            return thetaMax;
        }

        @Override
        public double getMinU() {
            return phiMin;
        }

        @Override
        public double getMinV() {
            return thetaMin;
        }

        @Override
        public NormalType getNormalType() {
            return NormalType.TWO_SIDE;
        }

        @Override
        public boolean isClosedU() {
            return false;
        }

        @Override
        public boolean isClosedV() {
            return false;
        }
        
    }

    public PartTorus() {
        super(new PartTorusParam(1, 0.5));
    }

    public PartTorus(double a, double b) {
        super(new PartTorusParam(a, b));
    }
    
    public PartTorus(double a, double b, double phiMin, double phiMax, double thetaMin, double thetaMax){
        super(new PartTorusParam(a, b, phiMin, phiMax, thetaMin, thetaMax));
    }
}
