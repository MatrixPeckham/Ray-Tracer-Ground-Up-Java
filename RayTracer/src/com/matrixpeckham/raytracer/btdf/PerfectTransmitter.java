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
package com.matrixpeckham.raytracer.btdf;

import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class PerfectTransmitter extends BTDF{
    private double kt;
    private double ior;

    public PerfectTransmitter() {
        kt=0;
        ior=1;
    }
    public PerfectTransmitter(PerfectTransmitter t){
        super(t);
        kt=t.kt;
        ior=t.ior;
    }

    @Override
    public PerfectTransmitter clone() {
        return new PerfectTransmitter(this);
    }

    public void setIor(double ior) {
        this.ior = ior;
    }

    public void setKt(double kt) {
        this.kt = kt;
    }

    public BTDF setTo(PerfectTransmitter o) {
        super.setTo(o);
        kt=o.kt;
        ior=o.ior;
        return this;
    }
    
    public boolean tir(ShadeRec sr){
        Vector3D wo = sr.ray.d.neg();
        double cosThetaI=sr.normal.dot(wo);
        double eta = ior;
        if(cosThetaI<0){
            eta=1/eta;
        }
        return (1-(1-cosThetaI*cosThetaI)/(eta*eta))<0;
    }

    @Override
    public RGBColor f(ShadeRec sr, Vector3D wo, Vector3D wi) {
        return new RGBColor(Utility.BLACK);
    }

    @Override
    public RGBColor sampleF(ShadeRec sr, Vector3D wo, Vector3D wt) {
        Normal n = new Normal(sr.normal);
        double cosThetaI = n.dot(wo);
        double eta = ior;
        if(cosThetaI<0){
            cosThetaI=-cosThetaI;
            n.setTo(n.neg());
            eta=1/eta;
        }
        double temp = 1-(1-cosThetaI*cosThetaI)/(eta*eta);
        double cosTheta2=Math.sqrt(temp);
        wt.setTo(wo.neg().div(eta).sub(new Vector3D(n).mul(cosTheta2-cosThetaI/eta)));
        double abs = Math.abs(sr.normal.dot(wt));
        double eta2 = eta*eta;
        double kte = kt/eta2;
        RGBColor mul = Utility.WHITE.mul(kte);
        return mul.div(abs);
    }

    @Override
    public RGBColor rho(ShadeRec sr, Vector3D wo) {
        return Utility.BLACK;
    }
    
    
    
    
}
