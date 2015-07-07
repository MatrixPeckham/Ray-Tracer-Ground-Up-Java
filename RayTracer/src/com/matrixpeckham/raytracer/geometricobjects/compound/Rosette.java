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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledWedge;

/**
 *
 * @author William Matrix Peckham
 */
public class Rosette extends Grid {

// This file contains the declaration of the class Rosette
// This is a rosette configuration of BeveledWedge objects centered on the y axis
// The parameters are:
//				number of rings
//				central hole radius
//				ring width
//				y axis extent
//				bevel radius
// The numbers of wedges in the rings are stored in an array because they 
// must divide 360 exactly
// there is no mortar between the wedges 
// the wedges are stored in a regular grid			
//-------------------------------------------------------------------------------- class Rosette
    int numWedges[] = {10, 12, 15, 18, 24, 36};  // these numbers exactly divide into 360

// ------------------------------------------------------------------------------ default constructor
    public Rosette() {
        super();
        numRings = 1;
        holeRadius = 1.0;
        ringWidth = 1.0;
        rb = 0.25;
        y0 = -1.0;
        y1 = 1.0;
        constructRosette();
    }

// ------------------------------------------------------------------------------ constructor
    public Rosette(int NumRings,
            double HoleRadius,
            double RingWidth,
            double Rb,
            double Y0,
            double Y1) {
        super();
        numRings = NumRings;
        holeRadius = HoleRadius;
        ringWidth = RingWidth;
        rb = Rb;
        y0 = Y0;
        y1 = Y1;

        constructRosette();
    }

// ------------------------------------------------------------------------------ copy constructor
    public Rosette(Rosette r) {
        super();
        numRings = r.numRings;
        holeRadius = r.holeRadius;
        ringWidth = r.ringWidth;
        rb = r.rb;
        y0 = r.y0;

        y1 = r.y1;
        constructRosette();
    }

// ------------------------------------------------------------------------------ clone
    public Rosette clone() {
        return new Rosette(this);
    }

// ------------------------------------------------------------------------------ assignment operator
    public Rosette setTo(Rosette rhs) {

        numRings = rhs.numRings;
        holeRadius = rhs.holeRadius;
        ringWidth = rhs.ringWidth;
        rb = rhs.rb;
        y0 = rhs.y0;
        y1 = rhs.y1;
        constructRosette();
        return (this);
    }

// ------------------------------------------------------------------------------ constructRosette
// this function constructs the wedges in a rosette pattern and stores them in a grid
// this is the regular version, for Figure 21.11
    void constructRosette() {

        for (int k = 0; k < numRings; k++) {
            for (int j = 0; j < numWedges[k]; j++) {
                double angleWidth = 360 / numWedges[k];  // the azimuth angle extent of each wedge
                double r0 = holeRadius + k * ringWidth;
                double r1 = holeRadius + (k + 1) * ringWidth;
                double phi0 = j * angleWidth;
                double phi1 = (j + 1) * angleWidth;

                BeveledWedge wedgePtr = new BeveledWedge(y0, y1, r0, r1, rb,
                        phi0, phi1);
                addObject(wedgePtr);
            }
        }
    }

    /*

     // ------------------------------------------------------------------------------ constructRosette
     // use this version to render Figure 21.17
 
     void
     constructRosette(void) {	
	
     for (int k = 0; k < numRings; k++) {
     for (int j = 0; j < numWedges[k]; j++) {
     double angleWidth = 360 / numWedges[k];  // the azimuth angle extent of each wedge
     double r0 = holeRadius + k * ringWidth;
     double r1 = holeRadius + (k + 1) * ringWidth;
     double angleOffset = 63 * k;			
     double phi0 = j * angleWidth + angleOffset;
     double phi1 = (j + 1) * angleWidth + angleOffset;
			
     BeveledWedge* wedgePtr = new BeveledWedge(y0, y1, r0, r1, rb, phi0 , phi1);
     addObject(wedgePtr);										
     }
     }
     }


     */
    private double numRings;		// maximum of 6
    private double holeRadius;
    private double ringWidth;
    private double rb;				// bevel radius
    private double y0, y1;			// y axis extents

}
