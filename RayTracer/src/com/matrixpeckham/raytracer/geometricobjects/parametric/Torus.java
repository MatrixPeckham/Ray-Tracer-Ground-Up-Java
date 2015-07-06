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
package com.matrixpeckham.raytracer.geometricobjects.parametric;

import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Torus extends ParametricObject {
    public Torus(){
        this(1,0.5);
    }
    public Torus(double a, double b){
        super(new TorusParametric(a,b));
    }
    protected Torus(TorusParametric par){
        super(par);
    }
    protected static class TorusParametric implements ParametricEquation {
        double a=0;
        double b=0;
        public TorusParametric(double a, double b){
            this.a=a;
            this.b=b;
        }
            @Override
            public double getMinU() {
                return 0;
            }

            @Override
            public double getMaxU() {
                return Utility.TWO_PI;
            }

            @Override
            public double getMinV() {
                return 0;
            }

            @Override
            public double getMaxV() {
                return Utility.TWO_PI;
            }

            @Override
            public double getUStep() {
                return 0.1;
            }

            @Override
            public double getVStep() {
                return 0.1;
            }

            @Override
            public Point3D getPointAt(double u, double v) {
                Point3D p=new Point3D();
                double cosu=Math.cos(u);
                double sinu=Math.sin(u);
                double cosv=Math.cos(v);
                double sinv=Math.sin(v);
                p.x=sinu*(b*cosv+a);
                p.y=b*sinv;
                p.z=cosu*(b*cosv+a);
                
                return p;
            }

            @Override
            public Normal getNormalAt(double u, double v) {
                double cosu=Math.cos(u);
                double sinu=Math.sin(u);
                double cosv=Math.cos(v);
                double sinv=Math.sin(v);
                Vector3D dv = new Vector3D();
                dv.z=cosu*b*-sinv;
                dv.y=b*cosv;
                dv.x=sinu*b*-sinv;
                Vector3D du = new Vector3D();
                du.z=-sinu*(b*cosv+a);
                du.y=0;
                du.x=cosu*(b*cosv+a);
                
                
                Normal n = new Normal(dv.cross(du));
                n.normalize();
                return n;
            }

            @Override
            public boolean isClosedU() {
                return true;
            }

            @Override
            public boolean isClosedV() {
                return true;
            }

            @Override
            public ParametricEquation.NormalType getNormalType() {
                return ParametricEquation.NormalType.REGULAR;
            }
        }    
}
