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
import java.util.logging.Logger;

/**
 * Checker for generic disk
 *
 * @author William Matrix Peckham
 */
public class DiskChecker implements Texture {

    /**
     * Number of checkers around the disk
     */
    private int numAngularCheckers = 20;

    /**
     * Number of checkers on the radius.
     */
    private int numRadialCheckers = 10;

    /**
     * line width for rings
     */
    private double horizontalLineWidth = 0;

    /**
     * line width for spokes
     */
    private double verticalLineWidth = 0;

    /**
     * first color
     */
    private final RGBColor color1 = new RGBColor(1);

    /**
     * second color
     */
    private final RGBColor color2 = new RGBColor(0.5);

    /**
     * line color
     */
    private final RGBColor lineColor = new RGBColor(0);

    /**
     * gray and black no lines.
     */
    public DiskChecker() {
    }

    /**
     * copy constructor
     *
     * @param c
     */
    public DiskChecker(DiskChecker c) {
        numAngularCheckers = c.numAngularCheckers;
        numRadialCheckers = c.numRadialCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        color1.setTo(c.color1);
        color2.setTo(c.color2);
        lineColor.setTo(c.lineColor);
    }

    /**
     * setter
     *
     * @param numAngularCheckers
     */
    public void setNumAngularCheckers(int numAngularCheckers) {
        this.numAngularCheckers = numAngularCheckers;
    }

    /**
     * setter
     *
     * @param numRadialCheckers
     */
    public void setNumRadialCheckers(int numRadialCheckers) {
        this.numRadialCheckers = numRadialCheckers;
    }

    /**
     * setter
     *
     * @param horizontalLineWidth
     */
    public void setAngularLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    /**
     * setter
     *
     * @param verticalLineWidth
     */
    public void setRadialLineWidth(double verticalLineWidth) {
        this.verticalLineWidth = verticalLineWidth;
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
     * setter
     *
     * @param color2
     */
    public void setColor2(RGBColor color2) {
        this.color2.setTo(color2);
    }

    /**
     * setter
     *
     * @param lineColor
     */
    public void setLineColor(RGBColor lineColor) {
        this.lineColor.setTo(lineColor);
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new DiskChecker(this);
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
        double z = sr.localHitPosition.z;

        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        //calculate angle and radius distance
        double theta = Math.sqrt(x * x + z * z);//Math.acos(y);
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }

        //calculate checkers
        double phiSize = Utility.TWO_PI / numAngularCheckers;
        double thetaSize = 1.0 / numRadialCheckers;//Utility.PI / numRadialCheckers;

        //calculate checker index
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

        //get color
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
        setAngularLineWidth(d);
        setRadialLineWidth(d);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlat(int i) {
        setNumAngularCheckers(i);
    }

    /**
     *
     * @param i
     */
    public void setNumlong(int i) {
        setNumRadialCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumAngular(int i) {
        setNumAngularCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumVertical(int i) {
        setNumRadialCheckers(i);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setVerticalLineWidth(double d) {
        setRadialLineWidth(d);
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

    private static final Logger LOG
            = Logger.getLogger(DiskChecker.class.getName());

}
