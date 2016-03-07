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
package com.matrixpeckham.raytracer.textures.image;

/**
 * Simple class for integer texture coordinates.
 *
 * @author William Matrix Peckham
 */
public class TexelCoord {

    /**
     * row, or y coord
     */
    public int row;

    /**
     * col, or x coord
     */
    public int col;

    /**
     * zero defaults
     */
    public TexelCoord() {
        row = 0;
        col = 0;
    }

    /**
     * sets the two coordinates
     *
     * @param row
     * @param col
     */
    public TexelCoord(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * java equal
     *
     * @param texelCoordinate
     */
    void setTo(TexelCoord texelCoordinate) {
        row = texelCoordinate.row;
        col = texelCoordinate.col;
    }
}
