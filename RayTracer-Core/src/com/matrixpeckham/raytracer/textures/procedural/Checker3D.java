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

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import java.util.logging.Logger;

/**
 * 3D checker class. 3D checkers don't have lines between them
 *
 * @author William Matrix Peckham
 */
public class Checker3D implements Texture {

    /**
     * size of checker
     */
    private double size = 1;

    /**
     * color 1
     */
    private final RGBColor color1 = new RGBColor();

    /**
     * color 2
     */
    private final RGBColor color2 = new RGBColor(1);

    /**
     * default black/white size 1 checkers
     */
    public Checker3D() {
    }

    ;

    /**
     * copy constructor
     * @param c
     */
    public Checker3D(Checker3D c) {
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        size = c.size;
    }

    /**
     * getter
     *
     * @return
     */
    public double getSize() {
        return size;
    }

    /**
     * setter
     *
     * @param size
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * getter
     *
     * @return
     */
    public RGBColor getColor1() {
        return color1;
    }

    /**
     * setter
     *
     * @param color1
     */
    public void setColor1(RGBColor color1) {
        this.color1.setTo(color1);
    }

    /**
     * getter
     *
     * @return
     */
    public RGBColor getColor2() {
        return color2;
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setColor2(RGBColor color2) {
        this.color2.setTo(color2);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new Checker3D(this);
    }

    /**
     * gets color of texture at point
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {
        //offset position so that generic objects don't have roundoff noise
        double eps = -0.000187453738;
        double x = sr.localHitPosition.x + eps;
        double y = sr.localHitPosition.y + eps;
        double z = sr.localHitPosition.z + eps;
        //checker formula
        if (((int) Math.floor(x / size) + (int) Math.floor(y / size)
                + (int) Math.floor(z / size)) % 2 == 0) {
            return color1;
        }
        return color2;
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        color1.setTo(d, d0, d1);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor2(double d, double d0, double d1) {
        color2.setTo(d, d0, d1);
    }

    /**
     * gray setter
     *
     * @param d
     */
    public void setColor1(double d) {
        color1.setTo(d);
    }

    /**
     * gray setter
     *
     * @param d
     */
    public void setColor2(double d) {
        color2.setTo(d);
    }

    private static final Logger LOG
            = Logger.getLogger(Checker3D.class.getName());

}
