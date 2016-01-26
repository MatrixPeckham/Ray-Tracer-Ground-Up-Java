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
import com.matrixpeckham.raytracer.util.Utility;

/**
 * Class for texturing a generic rectangle with a checker
 *
 * @author William Matrix Peckham
 */
public class RectangleChecker implements Texture {

    /**
     * number of checkers in the x direction
     */
    private int numXCheckers = 20;

    /**
     * number of checkers in the z diretion
     */
    private int numZCheckers = 10;

    /**
     * horizontal line width
     */
    private double horizontalLineWidth = 0;

    /**
     * vertical line width
     */
    private double verticalLineWidth = 0;

    /**
     * first color
     */
    private RGBColor color1 = new RGBColor(1);

    /**
     * second color
     */
    private RGBColor color2 = new RGBColor(0.5);

    /**
     * line color
     */
    private RGBColor lineColor = new RGBColor(0);

    /**
     * default constructor.
     */
    public RectangleChecker() {
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public RectangleChecker(RectangleChecker c) {
        numXCheckers = c.numXCheckers;
        numZCheckers = c.numZCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        lineColor.setTo(c.lineColor);
    }

    /**
     * setter
     *
     * @param numXCheckers
     */
    public void setNumXCheckers(int numXCheckers) {
        this.numXCheckers = numXCheckers;
    }

    /**
     * setter
     *
     * @param numZCheckers
     */
    public void setNumZCheckers(int numZCheckers) {
        this.numZCheckers = numZCheckers;
    }

    /**
     * setter
     *
     * @param horizontalLineWidth
     */
    public void setXLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    /**
     * setter
     *
     * @param verticalLineWidth
     */
    public void setZLineWidth(double verticalLineWidth) {
        this.verticalLineWidth = verticalLineWidth;
    }

    /**
     * setter
     *
     * @param color1
     */
    public void setColor1(RGBColor color1) {
        this.color1 = color1;
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setColor2(RGBColor color2) {
        this.color2 = color2;
    }

    /**
     * setter
     *
     * @param lineColor
     */
    public void setLineColor(RGBColor lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture clone() {
        return new RectangleChecker(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr) {

        //hit point
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;

        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        //normalize hit point from -1-1 to 0-1
        double theta = x / 2 + .5;//Math.acos(y);
        double phi = z / 2 + .5;//Math.atan2(x,z);

        //checker sizes
        double phiSize = 1.0 / numXCheckers;//Utility.TWO_PI / numXCheckers;
        double thetaSize = 1.0 / numZCheckers;//Utility.PI / numZCheckers;

        //checker calculation
        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        //line calculations
        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        //are we in the line
        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        //return proper color
        if ((iphi + itheta) % 2 == 0) {
            if (!inOutline) {
                return color1;
            }
        } else {
            if (!inOutline) {
                return color2;
            }
        }
        return lineColor;
    }

    /**
     * setter
     *
     * @param d
     */
    public void setLineWidth(double d) {
        setXLineWidth(d);
        setZLineWidth(d);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlat(int i) {
        setNumXCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlong(int i) {
        setNumZCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumX(int i) {
        setNumXCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumVertical(int i) {
        setNumZCheckers(i);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setVerticalLineWidth(double d) {
        setZLineWidth(d);
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor1(double d, double d0, double d1) {
        setColor1(new RGBColor(d, d0, d1));
    }

    /**
     * setter
     *
     * @param d
     * @param d0
     * @param d1
     */
    public void setColor2(double d, double d0, double d1) {
        setColor2(new RGBColor(d, d0, d1));
    }

}
