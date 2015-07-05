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

import com.matrixpeckham.raytracer.geometricobjects.GeometricObject;
import com.matrixpeckham.raytracer.geometricobjects.triangles.FlatMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothMeshTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.SmoothTriangle;
import com.matrixpeckham.raytracer.geometricobjects.triangles.Triangle;
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.Mesh;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.ply.PLYElement;
import com.matrixpeckham.raytracer.util.ply.PLYFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author William Matrix Peckham
 */
public class TriangleMesh extends Grid {

    enum TriangleType {

        FLAT, SMOOTH
    }
    protected Mesh mesh;
    protected boolean reverseNomral = false;

    public TriangleMesh() {
        super();
        mesh = new Mesh();
    }

    public TriangleMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public TriangleMesh(TriangleMesh m) {
        this();
    }

    public TriangleMesh clone() {
        return new TriangleMesh(this);
    }

    public void reverseNormal() {
        reverseNomral = true;
    }


    public void readFlatTriangles(File f) throws IOException {
        readPLYFile(f, TriangleType.FLAT);
    }

    public void readSmoothTriangles(File f) throws IOException {
        readPLYFile(f, TriangleType.SMOOTH);
        computeMeshNormals();
    }

    private void readPLYFile(File f, TriangleType t) throws IOException {
        PLYFile ply = new PLYFile(f);
        ArrayList<PLYElement> verts = ply.getElements("vertex");
        mesh.numVertices = verts.size();
        for (int i = 0; i < mesh.numVertices; i++) {
            mesh.vertices.add(new Point3D(verts.get(i).getDouble("x"), verts.
                    get(i).getDouble("y"), verts.get(i).getDouble("z")));
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
                FlatMeshTriangle tri = new FlatMeshTriangle(mesh, faceLst[0],
                        faceLst[1], faceLst[2]);
                tri.computeNormal(reverseNomral);
                objects.add(tri);
            } else {
                SmoothMeshTriangle tri
                        = new SmoothMeshTriangle(mesh, faceLst[0], faceLst[1],
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

    public void computeMeshNormals() {
        for (int ind = 0; ind < mesh.numVertices; ind++) {
            Normal normal = new Normal();
            for (int j = 0; j < mesh.vertexFaces.get(ind).size(); j++) {
                normal.addLocal(objects.get(mesh.vertexFaces.get(ind).get(j)).
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
    public void tessellateSmoothSphere(int horizontalSteps, int verticalSteps) {
        // define the top triangles
        int k = 1;
        for (int j = 0; j <= horizontalSteps - 1;
                j++) {
            // define vertices
            Point3D v0 = new Point3D(0, 1, 0); // top
            Point3D v1
                    = new Point3D(Math.sin(2.0 * Utility.PI * j / horizontalSteps) *
                    Math.sin(Utility.PI * k / verticalSteps),
                    // bottom left
            Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.sin(Utility.PI *
                    k / verticalSteps));
            Point3D v2
                    = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1) /
                    horizontalSteps) * Math.sin(Utility.PI * k / verticalSteps),
                    // bottom  right
            Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps) *
                    Math.sin(Utility.PI * k / verticalSteps));
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
                    = new Point3D(Math.sin(2.0 * Utility.PI * j / horizontalSteps) *
                    Math.sin(Utility.PI * k / verticalSteps),
                    // top left
            Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * j / horizontalSteps) * Math.sin(Utility.PI *
                    k / verticalSteps));
            Point3D v1 = new Point3D(0, -1, 0); // bottom
            Point3D v2
                    = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1) /
                    horizontalSteps) * Math.sin(Utility.PI * k / verticalSteps),
                    // top right
            Math.cos(Utility.PI * k / verticalSteps),
                    Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps) *
                    Math.sin(Utility.PI * k / verticalSteps));
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
                        = new Point3D(Math.sin(2.0 * Utility.PI * j /
                        horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps),
                        // bottom left, use k + 1, j
                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps));
                Point3D v1
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1) /
                        horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps),
                        // bottom  right, use k + 1, j + 1
                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps));
                Point3D v2
                        = new Point3D(Math.sin(2.0 * Utility.PI * j /
                        horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps),
                        // top left, 	use k, j
                Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps));
                SmoothTriangle trianglePtr1 = new SmoothTriangle(v0, v1, v2);
                trianglePtr1.n0.setTo(v0);
                trianglePtr1.n1.setTo(v1);
                trianglePtr1.n2.setTo(v2);
                objects.add(trianglePtr1);
                // define the second triangle
                // vertices
                v0
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1) /
                        horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps),
                        // top right, use k, j + 1
                Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps));
                v1
                        = new Point3D(Math.sin(2.0 * Utility.PI * j /
                        horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps),
                        // top left, 	use k, j
                Math.cos(Utility.PI * k / verticalSteps),
                        Math.cos(2.0 * Utility.PI * j / horizontalSteps) *
                        Math.sin(Utility.PI * k / verticalSteps));
                v2
                        = new Point3D(Math.sin(2.0 * Utility.PI * (j + 1) /
                        horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps),
                        // bottom  right, use k + 1, j + 1
                Math.cos(Utility.PI * (k + 1) / verticalSteps),
                        Math.cos(2.0 * Utility.PI * (j + 1) / horizontalSteps) *
                        Math.sin(Utility.PI * (k + 1) / verticalSteps));
                SmoothTriangle trianglePtr2 = new SmoothTriangle(v0, v1, v2);
                trianglePtr2.n0.setTo(v0);
                trianglePtr2.n1.setTo(v1);
                trianglePtr2.n2.setTo(v2);
                objects.add(trianglePtr2);
            }
        }
    }

}
