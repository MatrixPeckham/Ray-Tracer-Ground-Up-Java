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
package com.matrixpeckham.raytracer.geometricobjects;

import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Matrix;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;

/**
 *
 * @author William Matrix Peckham
 */
public class Instance extends GeometricObject {

    private GeometricObject object;
    private Matrix invMatrix;
    private Matrix forwardMatrix;
    private BBox bbox;
    private boolean transformTexture;

    public Instance() {
        super();
        object = null;
        invMatrix = new Matrix();
        bbox = new BBox();
        transformTexture = true;
        forwardMatrix = new Matrix();
    }

    public Instance(GeometricObject obj) {
        super();
        object = obj;
        invMatrix = new Matrix();
        forwardMatrix = new Matrix();
        bbox = new BBox();
        transformTexture = true;
    }

    public Instance(Instance i) {
        super(i);
        invMatrix.setTo(i.invMatrix);
        transformTexture = i.transformTexture;
        if (i.object != null) {
            object = i.object.clone();
        }
        forwardMatrix.setTo(i.forwardMatrix);
        bbox = new BBox(i.bbox);
    }

    public void setObject(GeometricObject obj) {
        object = obj;
    }

    public void computeBoundingBox() {
        BBox objBBox = object.getBoundingBox();

        // Now apply the affine transformations to the box.
        // We must apply the transformations to all 8 vertices of the orginal box
        // and then work out the new minimum and maximum values
        // Construct the eight vertices as 3D points:
        Point3D[] v = new Point3D[8];
        for (int i = 0; i < 8; i++) {
            v[i] = new Point3D();
        }

        v[0].x = objBBox.x0;
        v[0].y = objBBox.y0;
        v[0].z = objBBox.z0;
        v[1].x = objBBox.x1;
        v[1].y = objBBox.y0;
        v[1].z = objBBox.z0;
        v[2].x = objBBox.x1;
        v[2].y = objBBox.y1;
        v[2].z = objBBox.z0;
        v[3].x = objBBox.x0;
        v[3].y = objBBox.y1;
        v[3].z = objBBox.z0;

        v[4].x = objBBox.x0;
        v[4].y = objBBox.y0;
        v[4].z = objBBox.z1;
        v[5].x = objBBox.x1;
        v[5].y = objBBox.y0;
        v[5].z = objBBox.z1;
        v[6].x = objBBox.x1;
        v[6].y = objBBox.y1;
        v[6].z = objBBox.z1;
        v[7].x = objBBox.x0;
        v[7].y = objBBox.y1;
        v[7].z = objBBox.z1;

        // Transform these using the forward matrix
        v[0] = Point3D.mul(forwardMatrix, v[0]);
        v[1] = Point3D.mul(forwardMatrix, v[1]);
        v[2] = Point3D.mul(forwardMatrix, v[2]);
        v[3] = Point3D.mul(forwardMatrix, v[3]);
        v[4] = Point3D.mul(forwardMatrix, v[4]);
        v[5] = Point3D.mul(forwardMatrix, v[5]);
        v[6] = Point3D.mul(forwardMatrix, v[6]);
        v[7] = Point3D.mul(forwardMatrix, v[7]);

        // Compute the minimum values
        double x0 = Utility.HUGE_VALUE;
        double y0 = Utility.HUGE_VALUE;
        double z0 = Utility.HUGE_VALUE;

        for (int j = 0; j <= 7; j++) {
            if (v[j].x < x0) {
                x0 = v[j].x;
            }
        }

        for (int j = 0; j <= 7; j++) {
            if (v[j].y < y0) {
                y0 = v[j].y;
            }
        }

        for (int j = 0; j <= 7; j++) {
            if (v[j].z < z0) {
                z0 = v[j].z;
            }
        }

        // Compute the minimum values
        double x1 = -Utility.HUGE_VALUE;
        double y1 = -Utility.HUGE_VALUE;
        double z1 = -Utility.HUGE_VALUE;

        for (int j = 0; j <= 7; j++) {
            if (v[j].x > x1) {
                x1 = v[j].x;
            }
        }

        for (int j = 0; j <= 7; j++) {
            if (v[j].y > y1) {
                y1 = v[j].y;
            }
        }

        for (int j = 0; j <= 7; j++) {
            if (v[j].z > z1) {
                z1 = v[j].z;
            }
        }

        // Assign values to the bounding box
        bbox.x0 = x0;
        bbox.y0 = y0;
        bbox.z0 = z0;
        bbox.x1 = x1;
        bbox.y1 = y1;
        bbox.z1 = z1;
    }

    @Override
    public BBox getBoundingBox() {
        return bbox;
    }

    public void setTransformTexture(boolean tr) {
        transformTexture = tr;
    }

    @Override
    public GeometricObject clone() {
        return new Instance(this);
    }

    @Override
    public boolean hit(Ray ray, ShadeRec s) {
        Ray invRay = new Ray(ray);
        invRay.o.setTo(Point3D.mul(invMatrix, invRay.o));
        invRay.d.setTo(Vector3D.mul(invMatrix, invRay.d));

        if (object.hit(invRay, s)) {
            s.normal.setTo(Normal.mul(invMatrix, s.normal));
            s.normal.normalize();
            if (object.getMaterial() != null) {
                material = object.getMaterial();
            }
            if (!transformTexture) {
                s.localHitPosition.setTo(ray.o.add(ray.d.mul(s.lastT)));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef tr) {
        Ray invRay = new Ray(ray);
        invRay.o.setTo(Point3D.mul(invMatrix, invRay.o));
        invRay.d.setTo(Vector3D.mul(invMatrix, invRay.d));

        return object.shadowHit(invRay, tr);
    }

    public void scale(Vector3D s) {

        Matrix invScalingMatrix = new Matrix();			// temporary inverse scaling matrix

        invScalingMatrix.m[0][0] = 1.0 / s.x;
        invScalingMatrix.m[1][1] = 1.0 / s.y;
        invScalingMatrix.m[2][2] = 1.0 / s.z;

        invMatrix = invMatrix.mul(invScalingMatrix);

        Matrix scaling_matrix = new Matrix();				// temporary scaling matrix

        scaling_matrix.m[0][0] = s.x;
        scaling_matrix.m[1][1] = s.y;
        scaling_matrix.m[2][2] = s.z;

        forwardMatrix = scaling_matrix.mul(forwardMatrix);
    }

//-------------------------------------------------------------------------------- scale
    public void scale(double a, double b, double c) {

        Matrix invScalingMatrix = new Matrix();					// temporary inverse scaling matrix

        invScalingMatrix.m[0][0] = 1.0 / a;
        invScalingMatrix.m[1][1] = 1.0 / b;
        invScalingMatrix.m[2][2] = 1.0 / c;

        invMatrix = invMatrix.mul(invScalingMatrix);

        Matrix scaling_matrix = new Matrix();						// temporary scaling matrix

        scaling_matrix.m[0][0] = a;
        scaling_matrix.m[1][1] = b;
        scaling_matrix.m[2][2] = c;

        forwardMatrix = scaling_matrix.mul(forwardMatrix);
    }

//-------------------------------------------------------------------------------- translate
    public void translate(Vector3D trans) {

        Matrix inv_translation_matrix=new Matrix();				// temporary inverse translation matrix	

        inv_translation_matrix.m[0][3] = -trans.x;
        inv_translation_matrix.m[1][3] = -trans.y;
        inv_translation_matrix.m[2][3] = -trans.z;

        invMatrix = invMatrix .mul(inv_translation_matrix);

        Matrix translation_matrix=new Matrix();					// temporary translation matrix	

        translation_matrix.m[0][3] = trans.x;
        translation_matrix.m[1][3] = trans.y;
        translation_matrix.m[2][3] = trans.z;

        forwardMatrix = translation_matrix .mul( forwardMatrix);
    }

//-------------------------------------------------------------------------------- translate
    public void translate(double dx, double dy, double dz) {

        Matrix inv_translation_matrix=new Matrix();				// temporary inverse translation matrix	

        inv_translation_matrix.m[0][3] = -dx;
        inv_translation_matrix.m[1][3] = -dy;
        inv_translation_matrix.m[2][3] = -dz;

        invMatrix = invMatrix .mul (inv_translation_matrix);

        Matrix translation_matrix=new Matrix();					// temporary translation matrix	

        translation_matrix.m[0][3] = dx;
        translation_matrix.m[1][3] = dy;
        translation_matrix.m[2][3] = dz;

        forwardMatrix = translation_matrix .mul(forwardMatrix);
    }

//-------------------------------------------------------------------------------- rotate_x
    public void rotateX(double theta) {
        
        double sin_theta = Math.sin(theta * Utility.PI_ON_180);
        double cos_theta = Math.cos(theta * Utility.PI_ON_180);

        Matrix inv_x_rotation_matrix=new Matrix();					// temporary inverse rotation matrix about x axis

        inv_x_rotation_matrix.m[1][1] = cos_theta;
        inv_x_rotation_matrix.m[1][2] = sin_theta;
        inv_x_rotation_matrix.m[2][1] = -sin_theta;
        inv_x_rotation_matrix.m[2][2] = cos_theta;

        invMatrix = invMatrix .mul(inv_x_rotation_matrix);

        Matrix x_rotation_matrix=new Matrix();						// temporary rotation matrix about x axis

        x_rotation_matrix.m[1][1] = cos_theta;
        x_rotation_matrix.m[1][2] = -sin_theta;
        x_rotation_matrix.m[2][1] = sin_theta;
        x_rotation_matrix.m[2][2] = cos_theta;

        forwardMatrix = x_rotation_matrix .mul( forwardMatrix);
    }

//-------------------------------------------------------------------------------- rotate_y
    public void rotateY(double theta) {

        double sin_theta = Math.sin(theta * Utility.PI / 180.0);
        double cos_theta = Math.cos(theta * Utility.PI / 180.0);

        Matrix inv_y_rotation_matrix=new Matrix();					// temporary inverse rotation matrix about y axis

        inv_y_rotation_matrix.m[0][0] = cos_theta;
        inv_y_rotation_matrix.m[0][2] = -sin_theta;
        inv_y_rotation_matrix.m[2][0] = sin_theta;
        inv_y_rotation_matrix.m[2][2] = cos_theta;

        invMatrix = invMatrix .mul(inv_y_rotation_matrix);

        Matrix y_rotation_matrix=new Matrix();						// temporary rotation matrix about x axis

        y_rotation_matrix.m[0][0] = cos_theta;
        y_rotation_matrix.m[0][2] = sin_theta;
        y_rotation_matrix.m[2][0] = -sin_theta;
        y_rotation_matrix.m[2][2] = cos_theta;

        forwardMatrix = y_rotation_matrix .mul(forwardMatrix);
    }

//-------------------------------------------------------------------------------- rotate_z
    public void rotateZ(double theta) {
        double sin_theta = Math.sin(theta * Utility.PI / 180.0);
        double cos_theta = Math.cos(theta * Utility.PI / 180.0);

        Matrix inv_z_rotation_matrix=new Matrix();					// temporary inverse rotation matrix about y axis	

        inv_z_rotation_matrix.m[0][0] = cos_theta;
        inv_z_rotation_matrix.m[0][1] = sin_theta;
        inv_z_rotation_matrix.m[1][0] = -sin_theta;
        inv_z_rotation_matrix.m[1][1] = cos_theta;

        invMatrix = invMatrix .mul( inv_z_rotation_matrix);

        Matrix z_rotation_matrix=new Matrix();						// temporary rotation matrix about y axis

        z_rotation_matrix.m[0][0] = cos_theta;
        z_rotation_matrix.m[0][1] = -sin_theta;
        z_rotation_matrix.m[1][0] = sin_theta;
        z_rotation_matrix.m[1][1] = cos_theta;

        forwardMatrix = z_rotation_matrix .mul(forwardMatrix);
    }

//-------------------------------------------------------------------------------- shear
    public void shear(Matrix s) {
	
	Matrix inverse_shearing_matrix=new Matrix();    // inverse shear matrix

        // discriminant
        double d = 1.0 - s.m[1][0] * s.m[0][1] - s.m[2][0] * s.m[0][2]
                - s.m[2][1] * s.m[1][2]
                + s.m[1][0] * s.m[2][1] * s.m[0][2] + s.m[2][0] * s.m[0][1]
                * s.m[2][1];

        // diagonals
        inverse_shearing_matrix.m[0][0] = 1.0 - s.m[2][1] * s.m[1][2];
        inverse_shearing_matrix.m[1][1] = 1.0 - s.m[2][0] * s.m[0][2];
        inverse_shearing_matrix.m[2][2] = 1.0 - s.m[1][0] * s.m[0][1];
        inverse_shearing_matrix.m[3][3] = d;

        // first row
        inverse_shearing_matrix.m[0][1] = -s.m[1][0] + s.m[2][0] * s.m[1][2];
        inverse_shearing_matrix.m[0][2] = -s.m[2][0] + s.m[1][0] * s.m[2][1];

        // second row
        inverse_shearing_matrix.m[1][0] = -s.m[0][1] + s.m[2][1] * s.m[0][2];
        inverse_shearing_matrix.m[1][2] = -s.m[2][1] + s.m[2][0] * s.m[0][1];

        // third row
        inverse_shearing_matrix.m[2][0] = -s.m[0][2] + s.m[0][1] * s.m[1][2];
        inverse_shearing_matrix.m[2][1] = -s.m[1][2] + s.m[1][0] * s.m[0][2];

        // divide by discriminant
        inverse_shearing_matrix = inverse_shearing_matrix .div( d);

        invMatrix = invMatrix .mul( inverse_shearing_matrix);

        forwardMatrix = s .mul( forwardMatrix);
    }

}