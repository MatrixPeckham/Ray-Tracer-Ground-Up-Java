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
import com.matrixpeckham.raytracer.materials.Material;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.DoubleRef;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.world.World;
import java.util.ArrayList;

/**
 *
 * @author William Matrix Peckham
 */
public class Grid extends Compound{
    protected BBox bbox = new BBox();
    protected ArrayList<GeometricObject> cells = new ArrayList<>();
    protected int nx = 0;
    protected int ny = 0;
    protected int nz = 0;

    protected void deleteObjects() {
        objects.clear();
    }

    Point3D findMaxBounds() {
        BBox objectBox = new BBox();
        Point3D p0 = new Point3D(-Utility.HUGE_VALUE);
        int numObjects = objects.size();
        for (int j = 0; j < numObjects;
                j++) {
            objectBox.setTo(objects.get(j).getBoundingBox());
            if (objectBox.x0 > p0.x) {
                p0.x = objectBox.x0;
            }
            if (objectBox.y0 > p0.y) {
                p0.y = objectBox.y0;
            }
            if (objectBox.z0 > p0.z) {
                p0.z = objectBox.z0;
            }
            if (objectBox.x1 > p0.x) {
                p0.x = objectBox.x1;
            }
            if (objectBox.y1 > p0.y) {
                p0.y = objectBox.y1;
            }
            if (objectBox.z1 > p0.z) {
                p0.z = objectBox.z1;
            }
        }
        p0.x += Utility.EPSILON;
        p0.y += Utility.EPSILON;
        p0.z += Utility.EPSILON;
        return p0;
    }

    Point3D findMinBounds() {
        BBox objectBox = new BBox();
        Point3D p0 = new Point3D(Utility.HUGE_VALUE);
        int numObjects = objects.size();
        for (int j = 0; j < numObjects;
                j++) {
            objectBox.setTo(objects.get(j).getBoundingBox());
            if (objectBox.x0 < p0.x) {
                p0.x = objectBox.x0;
            }
            if (objectBox.y0 < p0.y) {
                p0.y = objectBox.y0;
            }
            if (objectBox.z0 < p0.z) {
                p0.z = objectBox.z0;
            }
            if (objectBox.x1 < p0.x) {
                p0.x = objectBox.x1;
            }
            if (objectBox.y1 < p0.y) {
                p0.y = objectBox.y1;
            }
            if (objectBox.z1 < p0.z) {
                p0.z = objectBox.z1;
            }
        }
        p0.x -= Utility.EPSILON;
        p0.y -= Utility.EPSILON;
        p0.z -= Utility.EPSILON;
        return p0;
    }

    
    @Override
    public BBox getBoundingBox() {
        return bbox;
    }

    // ---------------------------------------------------------------- hit
    // The following grid traversal code is based on the pseudo-code in Shirley (2000)
    // The first part is the same as the code in BBox::hit
    @Override
    public boolean hit(Ray ray, ShadeRec sr) {
        double ox = ray.o.x;
        double oy = ray.o.y;
        double oz = ray.o.z;
        double dx = ray.d.x;
        double dy = ray.d.y;
        double dz = ray.d.z;
        double x0 = bbox.x0;
        double y0 = bbox.y0;
        double z0 = bbox.z0;
        double x1 = bbox.x1;
        double y1 = bbox.y1;
        double z1 = bbox.z1;
        double txMin;
        double tyMin;
        double tzMin;
        double txMax;
        double tyMax;
        double tzMax;
        // the following code includes modifications from Shirley and Morley (2003)
        double a = 1.0 / dx;
        if (a >= 0) {
            txMin = (x0 - ox) * a;
            txMax = (x1 - ox) * a;
        } else {
            txMin = (x1 - ox) * a;
            txMax = (x0 - ox) * a;
        }
        double b = 1.0 / dy;
        if (b >= 0) {
            tyMin = (y0 - oy) * b;
            tyMax = (y1 - oy) * b;
        } else {
            tyMin = (y1 - oy) * b;
            tyMax = (y0 - oy) * b;
        }
        double c = 1.0 / dz;
        if (c >= 0) {
            tzMin = (z0 - oz) * c;
            tzMax = (z1 - oz) * c;
        } else {
            tzMin = (z1 - oz) * c;
            tzMax = (z0 - oz) * c;
        }
        double t0;
        double t1;
        if (txMin > tyMin) {
            t0 = txMin;
        } else {
            t0 = tyMin;
        }
        if (tzMin > t0) {
            t0 = tzMin;
        }
        if (txMax < tyMax) {
            t1 = txMax;
        } else {
            t1 = tyMax;
        }
        if (tzMax < t1) {
            t1 = tzMax;
        }
        if (t0 > t1) {
            return false;
        }
        // initial cell coordinates
        int ix;
        int iy;
        int iz;
        if (bbox.inside(ray.o)) {
            // does the ray start inside the grid?
            ix = (int) Utility.clamp((ox - x0) * nx / (x1 - x0), 0, nx - 1);
            iy = (int) Utility.clamp((oy - y0) * ny / (y1 - y0), 0, ny - 1);
            iz = (int) Utility.clamp((oz - z0) * nz / (z1 - z0), 0, nz - 1);
        } else {
            Point3D p = ray.o.add(ray.d.mul(t0)); // initial hit point with grid's bounding box
            ix = (int) Utility.clamp((p.x - x0) * nx / (x1 - x0), 0, nx - 1);
            iy = (int) Utility.clamp((p.y - y0) * ny / (y1 - y0), 0, ny - 1);
            iz = (int) Utility.clamp((p.z - z0) * nz / (z1 - z0), 0, nz - 1);
        }
        // ray parameter increments per cell in the x, y, and z directions
        double dtx = (txMax - txMin) / nx;
        double dty = (tyMax - tyMin) / ny;
        double dtz = (tzMax - tzMin) / nz;
        double txNext;
        double tyNext;
        double tzNext;
        int ixStep;
        int iyStep;
        int izStep;
        int ixStop;
        int iyStop;
        int izStop;
        if (dx > 0) {
            txNext = txMin + (ix + 1) * dtx;
            ixStep = +1;
            ixStop = nx;
        } else {
            txNext = txMin + (nx - ix) * dtx;
            ixStep = -1;
            ixStop = -1;
        }
        if (dx == 0.0) {
            txNext = Utility.HUGE_VALUE;
            ixStep = -1;
            ixStop = -1;
        }
        if (dy > 0) {
            tyNext = tyMin + (iy + 1) * dty;
            iyStep = +1;
            iyStop = ny;
        } else {
            tyNext = tyMin + (ny - iy) * dty;
            iyStep = -1;
            iyStop = -1;
        }
        if (dy == 0.0) {
            tyNext = Utility.HUGE_VALUE;
            iyStep = -1;
            iyStop = -1;
        }
        if (dz > 0) {
            tzNext = tzMin + (iz + 1) * dtz;
            izStep = +1;
            izStop = nz;
        } else {
            tzNext = tzMin + (nz - iz) * dtz;
            izStep = -1;
            izStop = -1;
        }
        if (dz == 0.0) {
            tzNext = Utility.HUGE_VALUE;
            izStep = -1;
            izStop = -1;
        }
        // traverse the grid
        while (true) {
            GeometricObject objectPtr = cells.get(ix + nx * iy + nx * ny * iz);
            if (txNext < tyNext && txNext < tzNext) {
                if (objectPtr != null && objectPtr.hit(ray, sr) &&
                        sr.lastT < txNext) {
                    material = objectPtr.getMaterial();
                    return true;
                }
                txNext += dtx;
                ix += ixStep;
                if (ix == ixStop) {
                    return false;
                }
            } else {
                if (tyNext < tzNext) {
                    if (objectPtr != null && objectPtr.hit(ray, sr) &&
                            sr.lastT < tyNext) {
                        material = objectPtr.getMaterial();
                        return true;
                    }
                    tyNext += dty;
                    iy += iyStep;
                    if (iy == iyStop) {
                        return false;
                    }
                } else {
                    if (objectPtr != null && objectPtr.hit(ray, sr) &&
                            sr.lastT < tzNext) {
                        material = objectPtr.getMaterial();
                        return true;
                    }
                    tzNext += dtz;
                    iz += izStep;
                    if (iz == izStop) {
                        return false;
                    }
                }
            }
        }
    } // end of hit

    public double pdf(ShadeRec sr) {
        return 1;
    }

    public void setupCells() {
        Point3D p0 = findMinBounds();
        Point3D p1 = findMaxBounds();
        bbox.x0 = p0.x;
        bbox.y0 = p0.y;
        bbox.z0 = p0.z;
        bbox.x1 = p1.x;
        bbox.y1 = p1.y;
        bbox.z1 = p1.z;
        // compute the number of grid cells in the x, y, and z directions
        int numObjects = objects.size();
        // dimensions of the grid in the x, y, and z directions
        double wx = p1.x - p0.x;
        double wy = p1.y - p0.y;
        double wz = p1.z - p0.z;
        double multiplier = 2.0; // multiplyer scales the number of grid cells relative to the number of objects
        double s = Math.pow(wx * wy * wz / numObjects, 0.3333333);
        nx = (int) (multiplier * wx / s + 1);
        ny = (int) (multiplier * wy / s + 1);
        nz = (int) (multiplier * wz / s + 1);
        // set up the array of grid cells with null pointers
        int numCells = nx * ny * nz;
        cells.ensureCapacity(numObjects);
        for (int j = 0; j < numCells;
                j++) {
            cells.add(null);
        }
        // set up a temporary array to hold the number of objects stored in each cell
        ArrayList<Integer> counts = new ArrayList<>();
        counts.ensureCapacity(numCells);
        for (int j = 0; j < numCells;
                j++) {
            counts.add(0);
        }
        // put the objects into the cells
        BBox objBBox; // object's bounding box
        int index; // cell's array index
        for (int j = 0; j < numObjects;
                j++) {
            objBBox = objects.get(j).getBoundingBox();
            // compute the cell indices at the corners of the bounding box of the object
            int ixmin
                    = (int) Utility.clamp((objBBox.x0 - p0.x) * nx /
                    (p1.x - p0.x), 0, nx - 1);
            int iymin
                    = (int) Utility.clamp((objBBox.y0 - p0.y) * ny /
                    (p1.y - p0.y), 0, ny - 1);
            int izmin
                    = (int) Utility.clamp((objBBox.z0 - p0.z) * nz /
                    (p1.z - p0.z), 0, nz - 1);
            int ixmax
                    = (int) Utility.clamp((objBBox.x1 - p0.x) * nx /
                    (p1.x - p0.x), 0, nx - 1);
            int iymax
                    = (int) Utility.clamp((objBBox.y1 - p0.y) * ny /
                    (p1.y - p0.y), 0, ny - 1);
            int izmax
                    = (int) Utility.clamp((objBBox.z1 - p0.z) * nz /
                    (p1.z - p0.z), 0, nz - 1);
            // add the object to the cells
            for (int iz = izmin; iz <= izmax;
                    iz++) // cells in z direction
            {
                for (int iy = iymin; iy <= iymax;
                        iy++) // cells in y direction
                {
                    for (int ix = ixmin; ix <= ixmax;
                            ix++) {
                        // cells in x direction
                        index = ix + nx * iy + nx * ny * iz;
                        if (counts.get(index) == 0) {
                            cells.set(index, objects.get(j));
                            counts.set(index, counts.get(index)+1); // now = 1
                        } else {
                            if (counts.get(index) == 1) {
                                Compound compoundPtr = new Compound(); // construct a compound object
                                compoundPtr.addObject(cells.get(index)); // add object already in cell
                                compoundPtr.addObject(objects.get(j)); // add the new object
                                cells.set(index, compoundPtr); // store compound in current cell
                                counts.set(index, counts.get(index) + 1); // now = 2
                            } else {
                                // counts.get(index) > 1
                                ((Compound) cells.get(index)).addObject(objects.get(j)); // just add current object
                                counts.set(index, counts.get(index) + 1); // for statistics only
                            }
                        }
                    }
                }
            }
        } // end of for (int j = 0; j < numObjects; j++)
        // erase the Compound::vector that stores the object pointers, but don't delete the objects
        
//IF WE DON'T DO THIS WE CAN CALL setMaterial() AFTER setupCells()
//this allows us to sub-class grid and call setupCells within a constructor after 
//generating a mesh, but still allow the user of the class to setup a material.
//objects.clear();

        
        // display some statistics on counts
        // this is useful for finding out how many cells have no objects, one object, etc
        // comment this out if you don't want to use it
        int numZeroes = 0;
        int numOnes = 0;
        int numTwos = 0;
        int numThrees = 0;
        int numGreater = 0;
        for (int j = 0; j < numCells;
                j++) {
            if (counts.get(j) == 0) {
                numZeroes += 1;
            }
            if (counts.get(j) == 1) {
                numOnes += 1;
            }
            if (counts.get(j) == 2) {
                numTwos += 1;
            }
            if (counts.get(j) == 3) {
                numThrees += 1;
            }
            if (counts.get(j) > 3) {
                numGreater += 1;
            }
        }
    }

    @Override
    public boolean shadowHit(Ray ray, DoubleRef t) {
        if (!shadows) {
            return false;
        }
        ShadeRec sr = new ShadeRec((World)null);
/*        double tmin = Utility.HUGE_VALUE;
        int numObjects = objects.size();
        for (int j = 0; j < numObjects;
                j++) {
            if (objects.get(j).shadowHit(ray, t) && t.d < tmin) {
                hit = true;
            }
        }
        if (hit) {
            t.d = tmin;
        }
        return hit;*/
        boolean hit = hit(ray,sr);
        t.d=sr.lastT;
        return hit;
    }

    public void storeMaterial(Material mat, int index) {
        objects.get(index).setMaterial(mat);
    }
    
}
