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
package com.matrixpeckham.raytracer.geometricobjects.compound;

import com.matrixpeckham.raytracer.geometricobjects.triangles.FlatMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.FlatUVMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.MeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothUVMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.ply.PLYElement;
import com.matrixpeckham.raytracer.util.ply.PLYFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * TriangleMesh class, for storing triangle meshes an rendering them. also has
 * the code to load them.
 *
 * Extends grid because we'll have many child triangles and they need to be
 * efficiently culled from hit tests
 *
 * @author William Matrix Peckham
 */
public class TriangleMesh extends Grid {

    /**
     * reads a ply file for flat triangle mesh with uv coordinates
     *
     * @param fileName
     * @throws IOException
     */
    public void readFlatUVTriangles(File fileName) throws IOException {
        readPLYFileUV(fileName, TriangleType.FLAT);
    }

    /**
     * reads a ply file for smooth triangle mesh with uv coordinates
     *
     * @param fileName
     * @throws IOException
     */
    public void readSmoothUvTriangles(File fileName) throws IOException {
        readPLYFileUV(fileName, TriangleType.SMOOTH);
        computeMeshNormals();
    }

    /**
     * Triangle type
     */
    enum TriangleType {

        /**
         * flat triangles, one normal per triangle
         */
        FLAT,
        /**
         * smooth triangles, interpolated individual normals
         */
        SMOOTH

    }

    /**
     * mesh reference that will hold all the vertices, normals, uvs.
     */
    protected Mesh mesh;

    /**
     * are the normals reversed
     */
    protected boolean reverseNomral = false;

    /**
     * default constructor, empty mesh
     */
    public TriangleMesh() {
        super();
        mesh = new Mesh();
    }

    /**
     *
     * @param mesh
     */
    public TriangleMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    /**
     * copy constructor
     *
     * @param m
     */
    public TriangleMesh(TriangleMesh m) {
        super(m);
        this.mesh = m.mesh;
        reverseNomral = m.reverseNomral;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public TriangleMesh cloneGeometry() {
        return new TriangleMesh(this);
    }

    /**
     * sets flag to reverse normals to true
     */
    public void reverseNormal() {
        reverseNomral = true;
    }

    /**
     * reads a file for a flat triangle mesh
     *
     * @param f
     * @throws IOException
     */
    public void readFlatTriangles(File f) throws IOException {
        readPLYFile(f, TriangleType.FLAT);
    }

    /**
     * reads a file for a smooth triangle mesh
     *
     * @param f
     * @throws IOException
     */
    public void readSmoothTriangles(File f) throws IOException {
        readPLYFile(f, TriangleType.SMOOTH);
        computeMeshNormals();
    }

    /**
     * reads a PLY file and creates the correct triangle type for the type input
     *
     * @param f
     * @param t
     * @throws IOException
     */
    private void readPLYFile(File f, TriangleType t) throws IOException {

        //create a plyfile object from the file.
        PLYFile ply = new PLYFile(f);

        //add all the ply file's vertices to the mesh
        ArrayList<PLYElement> verts = ply.getElements("vertex");
        mesh.numVertices = verts.size();
        for (int i = 0; i < mesh.numVertices; i++) {
            mesh.vertices.add(new Point3D(verts.get(i).getDouble("x"), verts.
                    get(i).getDouble("y"), verts.get(i).getDouble("z")));
        }

        //get the faces from the ply file, will be a list of lists of indices
        ArrayList<PLYElement> faces = ply.getElements("face");
        //store triangle count
        mesh.numTriangles = faces.size();
        //make sure we have empty lists for each vertex to store triangle connectivity, used in smooth triangles
        for (int i = 0; i < mesh.numVertices; i++) {
            mesh.vertexFaces.add(new ArrayList<>());
        }
        int count = 0;
        for (int i = 0; i < mesh.numTriangles; i++) {
            //gets the integer array of indices for each face
            int[] faceLst = faces.get(i).getIntList("vertex_indices");

            if (t == TriangleType.FLAT) {
                //flat triangles mean it's easy, make a traignle from the three indices compute its normal and add to grid
                FlatMeshTriangle tri = new FlatMeshTriangle(mesh, faceLst[0],
                        faceLst[1], faceLst[2]);
                tri.computeNormal(reverseNomral);
                objects.add(tri);
            } else {

                //start by making a smooth triangle from the three indices, compute its normal and add to grid
                SmoothMeshTriangle tri
                        = new SmoothMeshTriangle(mesh, faceLst[0], faceLst[1],
                                faceLst[2]);
                tri.computeNormal(reverseNomral);
                objects.add(tri);
                //add connectivity information
                mesh.vertexFaces.get(faceLst[0]).add(count);
                mesh.vertexFaces.get(faceLst[1]).add(count);
                mesh.vertexFaces.get(faceLst[2]).add(count);
                count++;
            }
        }
    }

    /**
     * Reads a ply file with per vertex uv coordinates with the specified
     * triangle type
     *
     * @param f
     * @param t
     * @throws IOException
     */
    private void readPLYFileUV(File f, TriangleType t) throws IOException {
        //method works the same way as the non-uv enabled one the only difference is in the vertex part
        PLYFile ply = new PLYFile(f);
        ArrayList<PLYElement> verts = ply.getElements("vertex");
        mesh.numVertices = verts.size();
        for (int i = 0; i < mesh.numVertices; i++) {
            mesh.vertices.add(new Point3D(verts.get(i).getDouble("x"), verts.
                    get(i).getDouble("y"), verts.get(i).getDouble("z")));
            //these are the only lines added to the other method, they get the uv coordinates and put them in the mesh
            mesh.u.add(verts.get(i).getDouble("u"));
            mesh.v.add(verts.get(i).getDouble("v"));
        }
        ArrayList<PLYElement> faces = ply.getElements("face");
        mesh.numTriangles = faces.size();
        for (int i = 0; i < mesh.numVertices; i++) {
            mesh.vertexFaces.add(new ArrayList<>());
        }
        int count = 0;
        for (int i = 0; i < mesh.numTriangles; i++) {
            int[] faceLst = faces.get(i).getIntList("vertex_indices");

            if (t == TriangleType.FLAT) {
                FlatUVMeshTriangle tri
                        = new FlatUVMeshTriangle(mesh, faceLst[0],
                                faceLst[1], faceLst[2]);
                tri.computeNormal(reverseNomral);
                objects.add(tri);
            } else {
                SmoothUVMeshTriangle tri
                        = new SmoothUVMeshTriangle(mesh, faceLst[0], faceLst[1],
                                faceLst[2]);
                tri.computeNormal(reverseNomral);
                objects.add(tri);
                mesh.vertexFaces.get(faceLst[0]).add(count);
                mesh.vertexFaces.get(faceLst[1]).add(count);
                mesh.vertexFaces.get(faceLst[2]).add(count);
                count++;
            }
        }
    }

    /**
     * computes the normals for the mesh from the stored adjacency information
     */
    public void computeMeshNormals() {
        for (int ind = 0; ind < mesh.numVertices; ind++) {
            Normal normal = new Normal();
            for (int j = 0; j < mesh.vertexFaces.get(ind).size(); j++) {
                normal.addLocal(((MeshTriangle) objects.get(mesh.vertexFaces.
                        get(ind).get(j))).
                        getNormal());
            }
            if (normal.x == 0 && normal.y == 0 && normal.z == 0) {
                normal.y = 1;
            } else {
                normal.normalize();
            }
            mesh.normals.add(normal);
        }
    }

    /**
     *
     * @param horizontalSteps
     * @param verticalSteps
     */
    public void tessellateFlatSphere(int horizontalSteps, int verticalSteps) {

        // define the top triangles which all touch the north pole
        int k = 1;

        for (int j = 0; j <= horizontalSteps - 1; j++) {
            // define vertices

            Point3D v0 = new Point3D(0, 1, 0);																		// top (north pole)

            Point3D v1 = new Point3D(Math.sin(2.0 * Utility.PI * j
                    / horizontalSteps) * Math.
                    sin(Utility.PI * k / verticalSteps), // bottom left
                    Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.sin(
                            Utility.PI * k / verticalSteps));

            Point3D v2 = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                    / horizontalSteps) * Math.
                    sin(Utility.PI * k / verticalSteps), // bottom  right
                    Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps)
                    * Math.sin(Utility.PI * k / verticalSteps));

            Triangle trianglePtr = new Triangle(v0, v1, v2);
            objects.add(trianglePtr);
        }

        // define the bottom triangles which all touch the south pole
        k = verticalSteps - 1;

        for (int j = 0; j <= horizontalSteps - 1; j++) {
            // define vertices

            Point3D v0 = new Point3D(Math.sin(2.0 * Utility.PI * j
                    / horizontalSteps) * Math.
                    sin(Utility.PI * k / verticalSteps), // top left
                    Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.sin(
                            Utility.PI * k / verticalSteps));

            Point3D v1 = new Point3D(0, -1, 0);																		// bottom (south pole)

            Point3D v2 = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                    / horizontalSteps) * Math.
                    sin(Utility.PI * k / verticalSteps), // top right
                    Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps)
                    * Math.sin(Utility.PI * k / verticalSteps));

            Triangle trianglePtr = new Triangle(v0, v1, v2);
            objects.add(trianglePtr);
        }

        //  define the other triangles
        for (k = 1; k <= verticalSteps - 2; k++) {
            for (int j = 0; j <= horizontalSteps - 1; j++) {
			// define the first triangle

                // vertices
                Point3D v0 = new Point3D(Math.sin(2.0 * Utility.PI * j
                        / horizontalSteps) * Math.sin(Utility.PI * (k + 1)
                                / verticalSteps), // bottom left, use k + 1, j
                        Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.
                        sin(Utility.PI * (k + 1) / verticalSteps));

                Point3D v1 = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                        / horizontalSteps) * Math.sin(Utility.PI * (k + 1)
                                / verticalSteps), // bottom  right, use k + 1, j + 1
                        Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps)
                        * Math.sin(Utility.PI * (k + 1) / verticalSteps));

                Point3D v2 = new Point3D(Math.sin(2.0 * Utility.PI * j
                        / horizontalSteps) * Math.sin(Utility.PI * k
                                / verticalSteps), // top left, 	use k, j
                        Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.
                        sin(Utility.PI * k / verticalSteps));

                Triangle trianglePtr1 = new Triangle(v0, v1, v2);
                objects.add(trianglePtr1);

                // define the second triangle
                // vertices
                v0 = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                        / horizontalSteps) * Math.sin(Utility.PI * k
                                / verticalSteps), // top right, use k, j + 1
                        Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps)
                        * Math.sin(Utility.PI * k / verticalSteps));

                v1 = new Point3D(Math.
                        sin(2.0 * Utility.PI * j / horizontalSteps) * Math.sin(
                                Utility.PI * k / verticalSteps), // top left, 	use k, j
                        Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.
                        sin(Utility.PI * k / verticalSteps));

                v2 = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                        / horizontalSteps) * Math.sin(Utility.PI * (k + 1)
                                / verticalSteps), // bottom  right, use k + 1, j + 1
                        Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps)
                        * Math.sin(Utility.PI * (k + 1) / verticalSteps));

                Triangle trianglePtr2 = new Triangle(v0, v1, v2);
                objects.add(trianglePtr2);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------  tesselateSmoothSphere
    // tesselate a unit sphere into smooth triangles that are stored directly in the grid
    /**
     *
     * @param horizontalSteps
     * @param verticalSteps
     */
    public void tessellateSmoothSphere(int horizontalSteps, int verticalSteps) {
        // define the top triangles
        int k = 1;
        for (int j = 0; j <= horizontalSteps - 1;
                j++) {
            // define vertices
            Point3D v0 = new Point3D(0, 1, 0); // top
            Point3D v1
                    = new Point3D(Math.sin(2.0 * Utility.PI * j
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps),
                            // bottom left
                            Math.cos(Utility.PI * k / verticalSteps),
                            Math.cos(2.0 * Utility.PI * j / horizontalSteps)
                            * Math.sin(Utility.PI * k / verticalSteps));
            Point3D v2
                    = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps),
                            // bottom  right
                            Math.cos(Utility.PI * k / verticalSteps),
                            Math.cos(2.0 * Utility.PI * (j + 1)
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps));
            SmoothTriangle trianglePtr = new SmoothTriangle(v0, v1, v2);
            trianglePtr.n0.setTo(v0);
            trianglePtr.n1.setTo(v1);
            trianglePtr.n2.setTo(v2);
            objects.add(trianglePtr);
        }
        // define the bottom triangles
        k = verticalSteps - 1;
        for (int j = 0; j <= horizontalSteps - 1;
                j++) {
            // define vertices
            Point3D v0
                    = new Point3D(Math.sin(2.0 * Utility.PI * j
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps),
                            // top left
                            Math.cos(Utility.PI * k / verticalSteps),
                            Math.cos(2.0 * Utility.PI * j / horizontalSteps)
                            * Math.sin(Utility.PI * k / verticalSteps));
            Point3D v1 = new Point3D(0, -1, 0); // bottom
            Point3D v2
                    = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps),
                            // top right
                            Math.cos(Utility.PI * k / verticalSteps),
                            Math.cos(2.0 * Utility.PI * (j + 1)
                                    / horizontalSteps) * Math.sin(Utility.PI * k
                                    / verticalSteps));
            SmoothTriangle trianglePtr = new SmoothTriangle(v0, v1, v2);
            trianglePtr.n0.setTo(v0);
            trianglePtr.n1.setTo(v1);
            trianglePtr.n2.setTo(v2);
            objects.add(trianglePtr);
        }
        //  define the other triangles
        for (k = 1; k <= verticalSteps - 2;
                k++) {
            for (int j = 0; j <= horizontalSteps - 1;
                    j++) {
                // define the first triangle
                // vertices
                Point3D v0
                        = new Point3D(Math.sin(2.0 * Utility.PI * j
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * (k + 1) / verticalSteps),
                                // bottom left, use k + 1, j
                                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                                Math.cos(2.0 * Utility.PI * j / horizontalSteps)
                                * Math.sin(Utility.PI * (k + 1) / verticalSteps));
                Point3D v1
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * (k + 1) / verticalSteps),
                                // bottom  right, use k + 1, j + 1
                                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                                Math.cos(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * (k + 1) / verticalSteps));
                Point3D v2
                        = new Point3D(Math.sin(2.0 * Utility.PI * j
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * k / verticalSteps),
                                // top left, 	use k, j
                                Math.cos(Utility.PI * k / verticalSteps),
                                Math.cos(2.0 * Utility.PI * j / horizontalSteps)
                                * Math.sin(Utility.PI * k / verticalSteps));
                SmoothTriangle trianglePtr1 = new SmoothTriangle(v0, v1, v2);
                trianglePtr1.n0.setTo(v0);
                trianglePtr1.n1.setTo(v1);
                trianglePtr1.n2.setTo(v2);
                objects.add(trianglePtr1);
                // define the second triangle
                // vertices
                v0
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * k / verticalSteps),
                                // top right, use k, j + 1
                                Math.cos(Utility.PI * k / verticalSteps),
                                Math.cos(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * k / verticalSteps));
                v1
                        = new Point3D(Math.sin(2.0 * Utility.PI * j
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * k / verticalSteps),
                                // top left, 	use k, j
                                Math.cos(Utility.PI * k / verticalSteps),
                                Math.cos(2.0 * Utility.PI * j / horizontalSteps)
                                * Math.sin(Utility.PI * k / verticalSteps));
                v2
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * (k + 1) / verticalSteps),
                                // bottom  right, use k + 1, j + 1
                                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                                Math.cos(2.0 * Utility.PI * (j + 1)
                                        / horizontalSteps) * Math.sin(Utility.PI
                                        * (k + 1) / verticalSteps));
                SmoothTriangle trianglePtr2 = new SmoothTriangle(v0, v1, v2);
                trianglePtr2.n0.setTo(v0);
                trianglePtr2.n1.setTo(v1);
                trianglePtr2.n2.setTo(v2);
                objects.add(trianglePtr2);
            }
        }
    }

    private static final Logger LOG
            = Logger.getLogger(TriangleMesh.class.getName());

}
