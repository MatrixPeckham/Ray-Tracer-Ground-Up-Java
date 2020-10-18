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

import com.matrixpeckham.raytracer.geometricobjects.compound.TriangleMesh;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothMeshTriangle;
import com.matrixpeckham.raytracer.util.*;
import java.util.logging.Logger;

/**
 * ParametricObject. represents any object that can be defined by a parametric
 * equation of two variables. implemented this in order render tori when I was
 * unable to get the quartic solver to work properly. extends TriangleMesh
 * because it is one
 *
 * @author William Matrix Peckham
 */
public class ParametricObject extends TriangleMesh {

    /**
     * Class that represents the parametric equation. an implementation of this
     * class is passed to each parametric object
     */
    public interface ParametricEquation {

        /**
         * Normal Type, this is used as a way to to control how normals are
         * represented. getNormal(u,v) should ALWAYS return outward facing
         * normal. using this enum we choose whether that normal is reversed
         * when the ray hits the opposite side.
         */
        public enum NormalType {

            /**
             * returns the outward facing normal from hit function
             */
            REGULAR,
            /**
             * returns the inward facing normal from hit function
             */
            REVERSE,
            /**
             * returns the outward normal if the ray hits outside and the inward
             * normal if the ray hits inside from hit function
             */
            TWO_SIDE

        }

        /**
         * Minimum U to use when sampling this equation
         *
         * @return
         */
        public double getMinU();

        /**
         * Maximum U to use when sampling this equation
         *
         * @return
         */
        public double getMaxU();

        /**
         * minimum v to use when sampling this equation
         *
         * @return
         */
        public double getMinV();

        /**
         * maximum v to use when sampling this equation
         *
         * @return
         */
        public double getMaxV();

        /**
         * Amount to add to each U between sampling the function.
         *
         * @return
         */
        public double getUStep();

        /**
         * amount to add to each V between sampling the function.
         *
         * @return
         */
        public double getVStep();

        /**
         * Sample a point of this equation, given u and v coordinates this
         * should return the point on the surface at those parameters.
         *
         * @param u
         * @param v
         *
         * @return
         */
        public Point3D getPointAt(double u, double v);

        /**
         * Sample a normal of this equation, given u, and v, coordinates this
         * should return the normal on the surface at those parameters, should
         * return the outward facing normal.
         *
         * @param u
         * @param v
         *
         * @return
         */
        public Normal getNormalAt(double u, double v);

        /**
         * should return true if the first and last U coordinate should be the
         * same point
         *
         * @return
         */
        public boolean isClosedU();

        /**
         * should return true if the first and last V coordinate should be the
         * same point
         *
         * @return
         */
        public boolean isClosedV();

        /**
         * the type of normals that we should use for this equation
         *
         * @return
         */
        public NormalType getNormalType();

    }

    /**
     * Normal type to use in the hit function.
     */
    ParametricEquation.NormalType normType;

    /**
     * Constructor that takes an equation. Only constructor.
     *
     * @param p
     */
    public ParametricObject(ParametricEquation p) {
        super();//initializes the triangle mesh to an empty one

        //convienence variables
        double uMin = p.getMinU();
        double uMax = p.getMaxU();
        double vMin = p.getMinV();
        double vMax = p.getMaxV();
        double uStep = p.getUStep();
        double vStep = p.getVStep();
        boolean closedU = p.isClosedU();
        boolean closedV = p.isClosedV();

        normType = p.getNormalType();//store for later

        //u and v counts
        int numU = 0;
        int numV = 0;

        //we loop through u and v coordinates and create a regular grid of points.
        //double counter
        double u;
        for (u = uMin; u <= uMax; u += uStep) {
            numV = 0;
            double v;
            for (v = vMin; v <= vMax; v += vStep) {
                Point3D vert = p.getPointAt(u, v);
                Normal normal = p.getNormalAt(u, v);
                if (Double.isNaN(normal.x) || Double.isNaN(normal.y) || Double.
                        isNaN(normal.z)) {
                    int breakable = 1 + 1;
                }
                mesh.vertices.add(vert);
                mesh.normals.add(normal);
                numV++;
            }
            //this is after the loop, if the loop variable didn't stop
            //at vMax, it jumps past it, we check those and add a point for vMax
            if (v != vMax && v > vMax) {
                mesh.vertices.add(p.getPointAt(u, vMax));
                mesh.normals.add(p.getNormalAt(u, vMax));
                numV++;
            }
            numU++;
        }
        //this is after the loop, if the loop variable didn't stop at uMax,
        //it jumps past it, we check those and go through another v loop to add
        if (u != uMax && u > uMax) {
            double v;
            for (v = vMin; v <= vMax; v += vStep) {
                mesh.vertices.add(p.getPointAt(uMax, v));
                mesh.normals.add(p.getNormalAt(uMax, v));
                //numV++;
            }
            if (v != vMax && v > vMax) {
                mesh.vertices.add(p.getPointAt(uMax, vMax));
                mesh.normals.add(p.getNormalAt(uMax, vMax));
//                numV++;
            }
            numU++;
        }

        //TODO: I should add the ability to make it flat shaded too
        //loop through grid, two triangles per grid, adds to super object list
        for (int i = 0; i < numU - 1; i++) {
            for (int j = 0; j < numV - 1; j++) {
                int bl = i * numV + j;
                int br = i * numV + j + 1;
                int tl = (i + 1) * numV + j;
                int tr = (i + 1) * numV + j + 1;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        //if it should be closed stich the coordinates together
        //probably results in degenerate triangles, but
        //better to have and not need than need and not have
        if (closedV) {
            for (int i = 0; i < numU - 1; i++) {
                int bl = i * numV + numV - 1;
                int br = i * numV;
                int tl = (i + 1) * numV + numV - 1;
                int tr = (i + 1) * numV;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        if (closedU) {
            for (int j = 0; j < numU - 1; j++) {
                int bl = (numU - 1) * numV + j;
                int br = (numU - 1) * numV + j + 1;
                int tl = j;
                int tr = j + 1;
                SmoothMeshTriangle b = new SmoothMeshTriangle(mesh, bl, br, tl);
                objects.add(b);
                SmoothMeshTriangle t = new SmoothMeshTriangle(mesh, tl, br, tr);
                objects.add(t);
            }
        }
        //call super method to generate cells
        setupCells();
    }

    /**
     * hit function.
     *
     * @param ray
     * @param sr
     *
     * @return
     */
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        //calls the triangle mesh hit function.
        boolean hit = super.hit(ray, sr);
        //if we hit the mesh, we hit the object and need to augment the normal
        if (hit) {
            switch (normType) {
                case REGULAR://normal is correct
                    break;
                case REVERSE:
                    sr.normal.setTo(sr.normal.neg());//reverse the normal
                    break;
                case TWO_SIDE:
                    if (ray.d.neg().dot(new Vector3D(sr.normal)) < 0.0) {
                        //reverse only if we hit the back
                        sr.normal.setTo(sr.normal.neg());
                    }
            }
        }
        return hit;
    }

    private static final Logger LOG
            = Logger.getLogger(ParametricObject.class.getName());

}
