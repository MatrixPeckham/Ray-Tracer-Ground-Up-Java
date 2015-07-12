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

import com.matrixpeckham.raytracer.brdfs.GlossySpecular;
import com.matrixpeckham.raytracer.brdfs.Lambertian;
import com.matrixpeckham.raytracer.brdfs.SV_GlossySpecular;
import com.matrixpeckham.raytracer.brdfs.SV_Lambertian;
import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class SV_Phong extends Material {

    protected SV_Lambertian ambientBRDF;
    protected SV_Lambertian diffuseBRDF;
    protected SV_GlossySpecular specularBRDF;

    public SV_Phong() {
        ambientBRDF = new SV_Lambertian();
        diffuseBRDF = new SV_Lambertian();
        specularBRDF = new SV_GlossySpecular();
    }

    public SV_Phong(SV_Phong p) {
        super(p);
        ambientBRDF = p.ambientBRDF.clone();
        diffuseBRDF = p.diffuseBRDF.clone();
        specularBRDF = p.specularBRDF.clone();
    }

    @Override
    public Material clone() {
        return new SV_Phong(this);
    }

    @Override
    public RGBColor shade(ShadeRec sr) {
        Vector3D wo = sr.ray.d.neg();
        RGBColor L = ambientBRDF.rho(sr, wo).mul(sr.w.ambient.L(sr));
        int numLights = sr.w.lights.size();
        for (int j = 0; j < numLights; j++) {
            Vector3D wi = sr.w.lights.get(j).getDirection(sr);
            double ndotwi = sr.normal.dot(wi);
            if(ndotwi > 0.0){
                boolean inShadow = false;
                if(sr.w.lights.get(j).castsShadows()){
                    Ray shadowRay=new Ray(sr.hitPoint,wi);
                    inShadow = sr.w.lights.get(j).inShadow(shadowRay,sr);
                }
                if (!inShadow) {
                    L.addLocal(diffuseBRDF.f(sr, wo, wi).add(specularBRDF.f(sr, wo,
                            wi)).mul(sr.w.lights.get(j).
                                    L(sr)).mul(sr.w.lights.get(j).G(sr)*ndotwi/sr.w.lights.get(j).pdf(sr)));
                }
            }
        }
        return L;
    }

    public void setKa(double ka) {
        ambientBRDF.setKa(ka);
    }

    public void setKd(double kd) {
        diffuseBRDF.setKd(kd);
    }

    public void setKs(double ks) {
        specularBRDF.setKs(ks);
    }
    public void setCs(Texture cs) {
        specularBRDF.setCs(cs);
    }

    public void setExp(double exp) {
        specularBRDF.setExp(exp);
    }

    
    public void setCd(Texture brown) {
        ambientBRDF.setCd(brown);
        diffuseBRDF.setCd(brown);
    }

}
