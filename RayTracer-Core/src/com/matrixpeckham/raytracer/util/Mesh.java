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
package com.matrixpeckham.raytracer.util;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Mesh class for holding data for rendering a mesh.
 *
 * @author William Matrix Peckham
 */
public class Mesh {

    /**
     * Vertices that make up this mesh
     */
    public final ArrayList<Point3D> vertices = new ArrayList<>();

    /**
     * normals at the vertices.
     */
    public final ArrayList<Normal> normals = new ArrayList<>();

    /**
     * u texture coordinates at the vertices.
     */
    public final ArrayList<Double> u = new ArrayList<>();

    /**
     * v texture coordinates at the vertices.
     */
    public final ArrayList<Double> v = new ArrayList<>();

    /**
     * index lists
     */
    public final ArrayList<ArrayList<Integer>> vertexFaces = new ArrayList<>();

    /**
     * number of vertices.
     */
    public int numVertices = 0;

    /**
     * number of faces.
     */
    public int numTriangles = 0;

    /**
     * default constructor
     */
    public Mesh() {
    }

    /**
     * copy constructor.
     *
     * @param m
     */
    public Mesh(Mesh m) {
        vertices.addAll(m.vertices);
        normals.addAll(m.normals);
        u.addAll(m.u);
        v.addAll(m.v);
        numVertices = m.numVertices;
        numTriangles = m.numTriangles;
    }

    private static final Logger LOG = Logger.getLogger(Mesh.class.getName());

}
