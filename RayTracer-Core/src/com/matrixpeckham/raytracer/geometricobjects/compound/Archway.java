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

import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledBox;
import com.matrixpeckham.raytracer.geometricobjects.beveledobjects.BeveledWedge;
import com.matrixpeckham.raytracer.util.Point3D;
import java.util.logging.Logger;

/**
 * Archway This is a door with a semi-circular arch at the top The door sides
 * are beveled boxes, and the arch is beveled wedges An un-transformed archway
 * is parallel to the (x, z) plane with its minimum (x, y, z) corner at the
 * origin The parameters are: width - x dimension, height - y dimension, depth -
 * z dimension, columnWidth - width of the side columns and archway, numBlocks -
 * number of blocks in the side columns, numWedges - number of wedges in the
 * archway, rb - common bevel radius of the block and wedges
 *
 * @author William Matrix Peckham
 */
public class Archway extends Grid {

    /**
     * initializing constructor
     *
     * @param Width
     * @param Height
     * @param Depth
     * @param ColumnWidth
     * @param NumBlocks
     * @param NumWedges
     * @param Rb
     */
    public Archway(double Width,
            double Height,
            double Depth,
            double ColumnWidth,
            int NumBlocks,
            int NumWedges,
            double Rb) {
        super();
        width = Width;
        height = Height;
        depth = Depth;
        columnWidth = ColumnWidth;
        numBlocks = NumBlocks;
        numWedges = NumWedges;
        rb = Rb;
        constructArchway();
    }

    /**
     * copy constructor
     *
     * @param aw
     */
    public Archway(Archway aw) {
        width = aw.width;
        height = aw.height;
        depth = aw.depth;
        columnWidth = aw.columnWidth;
        numBlocks = aw.numBlocks;
        numWedges = aw.numWedges;
        rb = aw.rb;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Archway cloneGeometry() {
        return (new Archway(this));
    }

    /**
     * constructs the archway
     */
    public final void constructArchway() {

        double blockHeight = (height - width / 2.0) / numBlocks;

        // build left column
        for (int j = 0; j < numBlocks; j++) {
            Point3D p0 = new Point3D(-(height - width / 2.0) + j * blockHeight,
                    0.0, -width / 2.0);
            Point3D p1 = new Point3D(-(height - width / 2.0) + (j + 1)
                    * blockHeight, depth, -width / 2.0 + columnWidth);
            BeveledBox blockPtr = new BeveledBox(p0, p1, rb);
            addObject(blockPtr);
        }

        // build right column
        for (int j = 0; j < numBlocks; j++) {
            Point3D p0 = new Point3D(-(height - width / 2.0) + j * blockHeight,
                    0.0, width / 2.0 - columnWidth);
            Point3D p1 = new Point3D(-(height - width / 2.0) + (j + 1)
                    * blockHeight, depth, width / 2.0);
            BeveledBox blockPtr = new BeveledBox(p0, p1, rb);
            addObject(blockPtr);
        }

        // build curved arch
        for (int j = 0; j < numWedges; j++) {
            double angleWidth = 180d / numWedges;  // the azimuth angle extent of each wedge
            double r0 = width / 2.0 - columnWidth;
            double r1 = width / 2.0;
            double phi0 = j * angleWidth;
            double phi1 = (j + 1) * angleWidth;

            BeveledWedge wedgePtr = new BeveledWedge(0.0, depth, r0, r1, rb,
                    phi0, phi1);
            addObject(wedgePtr);
        }
    }

    private final double width;

    private final double height;

    private final double depth;

    private final double columnWidth;

    private final int numBlocks;

    private final int numWedges;

    private final double rb;

    private static final Logger LOG = Logger.getLogger(Archway.class.getName());

}
