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
package com.matrixpeckham.raytracer.materials;

import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import java.util.logging.Logger;

/**
 * Sphere checkers which differs to three materials as checkers.
 *
 * @author William Matrix Peckham
 */
public class SphereMaterials extends Material {

    /**
     * Number of horizontal checkers on the sphere
     */
    private int numHorizontalCheckers = 20;

    /**
     * number of vertical checkers on the sphere
     */
    private int numVerticalCheckers = 10;

    /**
     * horizontal line width between checkers
     */
    private double horizontalLineWidth = 0;

    /**
     * vertical line width between checkers
     */
    private double verticalLineWidth = 0;

    /**
     * material for first checker
     */
    private Material color1;

    /**
     * material for second checker
     */
    private Material color2;

    /**
     * material for lines
     */
    private Material lineColor;

    /**
     * default constructor
     */
    public SphereMaterials() {
        Matte c1 = new Matte();
        c1.setCd(1);
        Matte c2 = new Matte();
        c2.setCd(0.5);
        Matte cl = new Matte();
        cl.setCd(0);
        color1 = c1;
        color2 = c2;
        lineColor = cl;

    }

    /**
     * copy constructor
     *
     * @param c
     */
    public SphereMaterials(SphereMaterials c) {
        numHorizontalCheckers = c.numHorizontalCheckers;
        numVerticalCheckers = c.numVerticalCheckers;
        horizontalLineWidth = c.horizontalLineWidth;
        verticalLineWidth = c.verticalLineWidth;
        if (c.color1 != null) {
            color1 = c.color1.cloneMaterial();
        }
        if (c.color2 != null) {
            color2 = c.color2.cloneMaterial();
        }
        if (c.lineColor != null) {
            lineColor = c.lineColor.cloneMaterial();
        }
    }

    /**
     * setter
     *
     * @param numHorizontalCheckers
     */
    public void setNumHorizontalCheckers(int numHorizontalCheckers) {
        this.numHorizontalCheckers = numHorizontalCheckers;
    }

    /**
     * setter
     *
     * @param numVerticalCheckers
     */
    public void setNumVerticalCheckers(int numVerticalCheckers) {
        this.numVerticalCheckers = numVerticalCheckers;
    }

    /**
     * setter
     *
     * @param horizontalLineWidth
     */
    public void setHorizontalLineWidth(double horizontalLineWidth) {
        this.horizontalLineWidth = horizontalLineWidth;
    }

    /**
     * setter
     *
     * @param verticalLineWidth
     */
    public void setVerticalLineWidth(double verticalLineWidth) {
        this.verticalLineWidth = verticalLineWidth;
    }

    /**
     * setter
     *
     * @param color1
     */
    public void setColor1(Material color1) {
        this.color1 = color1.cloneMaterial();
    }

    /**
     * setter
     *
     * @param color2
     */
    public void setColor2(Material color2) {
        this.color2 = color2.cloneMaterial();
    }

    /**
     * setter
     *
     * @param lineColor
     */
    public void setLineColor(Material lineColor) {
        this.lineColor = lineColor.cloneMaterial();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Material cloneMaterial() {
        return new SphereMaterials(this);
    }

    /**
     * Shade function, calculates sphere coordinates and checker calculations
     * and differs to other materials.
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor shade(ShadeRec sr) {
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;
        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        double theta = Math.acos(y);
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize = Utility.PI / numVerticalCheckers;

        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        if ((iphi + itheta) % 2 == 0) {
            if (!inOutline) {
                return color1.shade(sr);
            }
        } else {
            if (!inOutline) {
                return color2.shade(sr);
            }
        }
        return lineColor.shade(sr);
    }

    /**
     * setter
     *
     * @param d
     */
    public void setLineWidth(double d) {
        setHorizontalLineWidth(d);
        setVerticalLineWidth(d);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlat(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumlong(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumHorizontal(int i) {
        setNumHorizontalCheckers(i);
    }

    /**
     * setter
     *
     * @param i
     */
    public void setNumVertical(int i) {
        setNumVerticalCheckers(i);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setChecker1Material(Material ref) {
        setColor1(ref);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setChecker2Material(Material ref) {
        setColor2(ref);
    }

    /**
     * setter
     *
     * @param ref
     */
    public void setLineMaterial(Material ref) {
        setLineColor(ref);
    }

    /**
     * Path shade, does sphere coordinate and checker calculations and then
     * differs to other materials
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor pathShade(ShadeRec sr) {
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;
        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        double theta = Math.acos(y);
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize = Utility.PI / numVerticalCheckers;

        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        if ((iphi + itheta) % 2 == 0) {
            if (!inOutline) {
                return color1.pathShade(sr);
            }
        } else {
            if (!inOutline) {
                return color2.pathShade(sr);
            }
        }
        return lineColor.pathShade(sr);
    }

    /**
     * Global shade, does sphere coordinate and checker calculations and then
     * differs to other materials
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor globalShade(ShadeRec sr) {
        double x = sr.localHitPosition.x;
        double y = sr.localHitPosition.y;
        double z = sr.localHitPosition.z;
        //double len = Math.sqrt(x*x+y*y+z*z);
        //x/=len;
        //y/=len;
        //z/=len;
        double theta = Math.acos(y);
        double phi = Math.atan2(x, z);
        if (phi < 0) {
            phi += Utility.TWO_PI;
        }
        double phiSize = Utility.TWO_PI / numHorizontalCheckers;
        double thetaSize = Utility.PI / numVerticalCheckers;

        int iphi = (int) Math.floor(phi / phiSize);
        int itheta = (int) Math.floor(theta / thetaSize);

        double fphi = phi / phiSize - iphi;
        double ftheta = theta / thetaSize - itheta;

        double phiLineWidth = 0.5 * verticalLineWidth;
        double thetaLineWidth = 0.5 * horizontalLineWidth;

        boolean inOutline = (fphi < phiLineWidth || fphi > 1.0 - phiLineWidth)
                || (ftheta < thetaLineWidth || ftheta > 1.0 - thetaLineWidth);

        if ((iphi + itheta) % 2 == 0) {
            if (!inOutline) {
                return color1.globalShade(sr);
            }
        } else {
            if (!inOutline) {
                return color2.globalShade(sr);
            }
        }
        return lineColor.globalShade(sr);
    }

    private static final Logger LOG
            = Logger.getLogger(SphereMaterials.class.getName());

}
