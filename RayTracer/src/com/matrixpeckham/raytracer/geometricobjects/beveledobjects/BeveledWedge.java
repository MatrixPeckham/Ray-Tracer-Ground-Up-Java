/*
 * Copyright (C) 2015 William Matrix Peckham
 *
 * This program is free software; you can redistribute it &&/or
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
package com.matrixpeckham.raytracer.geometricobjects.beveledobjects;

import com.matrixpeckham.raytracer.geometricobjects.Instance;
import com.matrixpeckham.raytracer.geometricobjects.compound.*;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConcavePartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartCylinder;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.ConvexPartTorus;
import com.matrixpeckham.raytracer.geometricobjects.partobjects.PartRing;
import com.matrixpeckham.raytracer.geometricobjects.primitives.OpenCylinder;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Rectangle;
import com.matrixpeckham.raytracer.geometricobjects.primitives.Sphere;
import com.matrixpeckham.raytracer.util.BBox;
import com.matrixpeckham.raytracer.util.Normal;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.Ray;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import com.matrixpeckham.raytracer.util.Vector3D;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author William Matrix Peckham
 */
public class BeveledWedge extends Compound {
    BBox bbox = new BBox();
    public BeveledWedge() {
        this(-1, 1, 0.5, 1, 0.1, 0, 30);
    }

    public BeveledWedge(double bottom, double top, double innerRadius,
            double outerRadius, double bevelRadius) {
        this(bottom, top, innerRadius, outerRadius, bevelRadius, 0, 30);
    }

    public BeveledWedge(double bottom, double top, double innerRadius,
            double outerRadius, double bevelRadius, double phiMin, double phiMax) {
        double y0=bottom;
        double y1=top;
        double r0=innerRadius;
        double r1=outerRadius;
        double rb=bevelRadius;
        double phi0=phiMin;
        double phi1=phiMax;
        /*
        OpenCylinder vertical = new OpenCylinder(bottom + bevelRadius, top
                - bevelRadius, bevelRadius);

        Instance inLowVert = new Instance(vertical);
        inLowVert.translate(0, 0, innerRadius + bevelRadius);
        inLowVert.rotateY(phiMin);
        addObject(inLowVert);
        Instance inHighVert = new Instance(vertical);
        inHighVert.translate(0, 0, innerRadius + bevelRadius);
        inHighVert.rotateY(phiMax);
        addObject(inHighVert);
        Instance outLowVert = new Instance(vertical);
        outLowVert.translate(0, 0, outerRadius - bevelRadius);
        outLowVert.rotateY(phiMin);
        addObject(outLowVert);
        Instance outHighVert = new Instance(vertical);
        outHighVert.translate(0, 0, outerRadius - bevelRadius);
        outHighVert.rotateY(phiMax);
        addObject(outHighVert);

        OpenCylinder hor = new OpenCylinder(innerRadius + bevelRadius,
                outerRadius - bevelRadius, bevelRadius);

        Instance downLowHor = new Instance(hor);
        downLowHor.rotateX(90);
        downLowHor.translate(0, bottom + bevelRadius, 0);
        downLowHor.rotateY(phiMin);
        addObject(downLowHor);
        Instance upLowHor = new Instance(hor);
        upLowHor.rotateX(90);
        upLowHor.translate(0, top - bevelRadius, 0);
        upLowHor.rotateY(phiMin);
        addObject(upLowHor);
        Instance downHighHor = new Instance(hor);
        downHighHor.rotateX(90);
        downHighHor.translate(0, bottom + bevelRadius, 0);
        downHighHor.rotateY(phiMax);
        addObject(downHighHor);
        Instance upHighHor = new Instance(hor);
        upHighHor.rotateX(90);
        upHighHor.translate(0, top - bevelRadius, 0);
        upHighHor.rotateY(phiMax);
        addObject(upHighHor);

        Sphere corner = new Sphere(new Point3D(0, 0, 0), bevelRadius);

        Instance lowInBot = new Instance(corner);
        lowInBot.translate(0, bottom + bevelRadius, innerRadius + bevelRadius);
        lowInBot.rotateY(phiMin);
        addObject(lowInBot);
        Instance lowOutBot = new Instance(corner);
        lowOutBot.translate(0, bottom + bevelRadius, outerRadius - bevelRadius);
        lowOutBot.rotateY(phiMin);
        addObject(lowOutBot);
        Instance lowInTop = new Instance(corner);
        lowInTop.translate(0, top - bevelRadius, innerRadius + bevelRadius);
        lowInTop.rotateY(phiMin);
        addObject(lowInTop);
        Instance lowOutTop = new Instance(corner);
        lowOutTop.translate(0, top - bevelRadius, outerRadius - bevelRadius);
        lowOutTop.rotateY(phiMin);
        addObject(lowOutTop);
        Instance highInBot = new Instance(corner);
        highInBot.translate(0, bottom + bevelRadius, innerRadius + bevelRadius);
        highInBot.rotateY(phiMax);
        addObject(highInBot);
        Instance highOutBot = new Instance(corner);
        highOutBot.translate(0, bottom + bevelRadius, outerRadius - bevelRadius);
        highOutBot.rotateY(phiMax);
        addObject(highOutBot);
        Instance highInTop = new Instance(corner);
        highInTop.translate(0, top - bevelRadius, innerRadius + bevelRadius);
        highInTop.rotateY(phiMax);
        addObject(highInTop);
        Instance highOutTop = new Instance(corner);
        highOutTop.translate(0, top - bevelRadius, outerRadius - bevelRadius);
        highOutTop.rotateY(phiMax);
        addObject(highOutTop);

        Rectangle rect1 = new Rectangle(new Point3D(0, bottom + bevelRadius,
                innerRadius + bevelRadius), new Vector3D(0, top - bottom - 2
                        * bevelRadius, 0), new Vector3D(0, 0, outerRadius
                        - innerRadius - 2 * bevelRadius), new Normal(-1, 0, 0));
        Rectangle rect2 = new Rectangle(new Point3D(0, bottom + bevelRadius,
                innerRadius + bevelRadius), new Vector3D(0, top - bottom - 2
                        * bevelRadius, 0), new Vector3D(0, 0, outerRadius
                        - innerRadius - 2 * bevelRadius));

        Instance lowSide = new Instance(rect1);
        lowSide.translate(-bevelRadius, 0, 0);
        lowSide.rotateY(phiMin);
        addObject(lowSide);
        Instance highSide = new Instance(rect2);
        highSide.translate(bevelRadius, 0, 0);
        highSide.rotateY(phiMax);
        addObject(highSide);

        PartRing topd = new PartRing(new Point3D(0, top, 0), innerRadius
                + bevelRadius, outerRadius - bevelRadius, phiMin, phiMax);
        PartRing bottomd = new PartRing(new Point3D(0, bottom, 0), innerRadius
                + bevelRadius, outerRadius - bevelRadius, phiMin, phiMax);
        ConcavePartCylinder innercyl = new ConcavePartCylinder(bottom
                + bevelRadius, top - bevelRadius, innerRadius, phiMin, phiMax);
        ConvexPartCylinder outercyl = new ConvexPartCylinder(bottom
                + bevelRadius, top - bevelRadius, outerRadius, phiMin, phiMax);
        PartTorus inTor = new PartTorus(innerRadius + bevelRadius, bevelRadius,
                phiMin,
                phiMax, 0, 360);
        PartTorus outTor = new PartTorus(outerRadius - bevelRadius, bevelRadius,
                phiMin,
                phiMax, 0, 360);
        addObject(topd);
        addObject(bottomd);
        addObject(innercyl);
        addObject(outercyl);
        Instance inUp = new Instance(inTor);
        inUp.translate(0, top - bevelRadius, 0);
        addObject(inUp);
        Instance inDown = new Instance(inTor);
        inDown.translate(0, bottom + bevelRadius, 0);
        addObject(inDown);
        Instance outUp = new Instance(outTor);
        outUp.translate(0, top - bevelRadius, 0);
        addObject(outUp);
        Instance outDown = new Instance(outTor);
        outDown.translate(0, bottom + bevelRadius, 0);
        addObject(outDown);
                */
	double sinPhi0 = Math.sin(phi0 * Utility.PI_ON_180);  // in radians
	double cosPhi0 = Math.cos(phi0 * Utility.PI_ON_180);  // in radians
	double sinPhi1 = Math.sin(phi1 * Utility.PI_ON_180);  // in radians
	double cosPhi1 = Math.cos(phi1 * Utility.PI_ON_180);  // in radians
	
	double sinAlpha = rb / (r0 + rb);
	double cosAlpha = Math.sqrt(r0 * r0 + 2.0 * r0 * rb) / (r0 + rb);
	double sinBeta = rb / (r1 - rb);
	double cosBeta = Math.sqrt(r1 * r1 - 2.0 * r1 * rb) / (r1 - rb);
		
	double xc1 = (r0 + rb) * (sinPhi0 * cosAlpha + cosPhi0 * sinAlpha);
	double zc1 = (r0 + rb) * (cosPhi0 * cosAlpha - sinPhi0 * sinAlpha);
	
	double xc2 = (r1 - rb) * (sinPhi0 * cosBeta + cosPhi0 * sinBeta);
	double zc2 = (r1 - rb) * (cosPhi0 * cosBeta - sinPhi0 * sinBeta);
		
	double xc3 = (r0 + rb) * (sinPhi1 * cosAlpha - cosPhi1 * sinAlpha);
	double zc3 = (r0 + rb) * (cosPhi1 * cosAlpha + sinPhi1 * sinAlpha);
	
	double xc4 = (r1 - rb) * (sinPhi1 * cosBeta - cosPhi1 * sinBeta);
	double zc4 = (r1 - rb) * (cosPhi1 * cosBeta + sinPhi1 * sinBeta);
	
	
	// corner spheres -------------------------------------------------------------------------------
		
	// bottom spheres
	
	Sphere bottomC1 = new Sphere(new Point3D(xc1, y0 + rb, zc1), rb);
	objects.add(bottomC1);
	
	Sphere bottomC2 = new Sphere(new Point3D(xc2, y0 + rb, zc2), rb);
	objects.add(bottomC2);
	
	Sphere bottomC3 = new Sphere(new Point3D(xc3, y0 + rb, zc3), rb);
	objects.add(bottomC3);
	
	Sphere bottomC4 = new Sphere(new Point3D(xc4, y0 + rb, zc4), rb);
	objects.add(bottomC4);
	
	
	// top spheres 
	
	Sphere topC1 = new Sphere(new Point3D(xc1, y1 - rb, zc1), rb);
	objects.add(topC1);
	
	Sphere topC2 = new Sphere(new Point3D(xc2, y1 - rb, zc2), rb);
	objects.add(topC2);
	
	Sphere topC3 = new Sphere(new Point3D(xc3, y1 - rb, zc3), rb);
	objects.add(topC3);
	
	Sphere topC4 = new Sphere(new Point3D(xc4, y1 - rb, zc4), rb);
	objects.add(topC4);	
	
	
	// vertical cylinders ------------------------------------------------------------------------------
	
	Instance bottomC1Cylinder = new Instance(new OpenCylinder(y0 + rb, y1 - rb, rb));
	bottomC1Cylinder.translate(xc1, 0.0, zc1);
	bottomC1Cylinder.setTransformTexture(false);
	objects.add(bottomC1Cylinder);	
	
	Instance bottomC2Cylinder = new Instance(new OpenCylinder(y0 + rb, y1 - rb, rb));
	bottomC2Cylinder.translate(xc2, 0.0, zc2);
	bottomC2Cylinder.setTransformTexture(false);
	objects.add(bottomC2Cylinder);
	
	Instance bottomC3Cylinder = new Instance(new OpenCylinder(y0 + rb, y1 - rb, rb));
	bottomC3Cylinder.translate(xc3, 0.0, zc3);
	bottomC3Cylinder.setTransformTexture(false);
	objects.add(bottomC3Cylinder);
	
	Instance bottomC4Cylinder = new Instance(new OpenCylinder(y0 + rb, y1 - rb, rb));
	bottomC4Cylinder.translate(xc4, 0.0, zc4);
	bottomC4Cylinder.setTransformTexture(false);
	objects.add(bottomC4Cylinder);
	
	
	// inner curved surface ---------------------------------------------------------------------------------
	
	// the azimuth angle range has to be specified in degrees
	
	double alpha = Math.acos(cosAlpha);  // radians
	phiMin = phi0 + alpha * 180.0 / Math.PI;
	phiMax = phi1 - alpha * 180.0 / Math.PI;
	
	ConcavePartCylinder innerCylinderPtr = new ConcavePartCylinder(y0 + rb, y1 - rb, r0, phiMin, phiMax); 
	objects.add(innerCylinderPtr);
	
	
	// outer curved surface -----------------------------------------------------------------------------------
	
	// the azimuth angle range has to be specified in degrees
	
	double beta = Math.acos(cosBeta);  // radians
	phiMin = phi0 + beta * 180.0 / Math.PI;
	phiMax = phi1 - beta * 180.0 / Math.PI;
	
	ConvexPartCylinder outerCylinderPtr = new ConvexPartCylinder(y0 + rb, y1 - rb, r1, phiMin, phiMax); 
	objects.add(outerCylinderPtr);
	
	
	// phi0 vertical rectangle
	
	double s1 = Math.sqrt(r0 * r0 + 2.0 * r0 * rb);
	double s2 = Math.sqrt(r1 * r1 - 2.0 * r1 * rb);
	Point3D p1=new Point3D(s1 * sinPhi0, y0 + rb, s1 * cosPhi0);
	Point3D p2=new Point3D(s2 * sinPhi0, y0 + rb, s2 * cosPhi0);
	Vector3D a = p2 .sub( p1);
	Vector3D b=new Vector3D(0, y1 - y0 - 2.0 * rb, 0);
	
	Rectangle phi0RectanglePtr = new Rectangle(p1, a, b); 
	objects.add(phi0RectanglePtr);
	
	
	// phi1 vertical rectangle
	
	Point3D p3=new Point3D(s1 * sinPhi1, y0 + rb, s1 * cosPhi1);
	Point3D p4=new Point3D(s2 * sinPhi1, y0 + rb, s2 * cosPhi1);
	a = p3 .sub( p4);
	
	Rectangle phi1RectanglePtr = new Rectangle(p4, a, b); 
	objects.add(phi1RectanglePtr);
	
	
	
	// the tori --------------------------------------------------------------------------------------------
	
	// inner bottom
	
	phiMin = phi0 + alpha * 180.0 / Math.PI;
	phiMax = phi1 - alpha * 180.0 / Math.PI;
	
	Instance innerBottomTorus = new Instance(new ConvexPartTorus(r0 + rb, rb, phiMin, phiMax, 0, 360));
	innerBottomTorus.translate(0.0, y0 + rb, 0.0);
	innerBottomTorus.setTransformTexture(false);
	objects.add(innerBottomTorus);
	
	
	// inner top
	
	Instance innerTopTorus = new Instance(new ConvexPartTorus(r0 + rb, rb, phiMin, phiMax, 0, 360));
	innerTopTorus.translate(0.0, y1 - rb, 0.0);
	innerTopTorus.setTransformTexture(false);
	objects.add(innerTopTorus);
	
	
	// outer bottom
	
	phiMin = phi0 + beta * 180.0 / Math.PI;
	phiMax = phi1 - beta * 180.0 / Math.PI;
	
	Instance outerBottomTorus = new Instance(new ConvexPartTorus(r1 - rb, rb, phiMin, phiMax, 0, 360));
	outerBottomTorus.translate(0.0, y0 + rb, 0.0);
	outerBottomTorus.setTransformTexture(false);
	objects.add(outerBottomTorus);
	
	
	// outer top
		
	Instance outerTopTorus = new Instance(new ConvexPartTorus(r1 - rb, rb, phiMin, phiMax, 0, 360));
	outerTopTorus.translate(0.0, y1 - rb, 0.0);
	outerTopTorus.setTransformTexture(false);
	objects.add(outerTopTorus);
	
	
	// horizontal cylinders ----------------------------------------------------------------------------------
	
	// phi0 bottom cylinder
	
	Instance phi0BottomCylinderPtr = new Instance(new OpenCylinder(0, s2 - s1, rb));
	phi0BottomCylinderPtr.rotateX(90);
	phi0BottomCylinderPtr.rotateY(phi0);
	phi0BottomCylinderPtr.translate(xc1, y0 + rb, zc1);
	phi0BottomCylinderPtr.setTransformTexture(false);
	objects.add(phi0BottomCylinderPtr);
	
	
	// phi0 top cylinder
	
	Instance phi0TopCylinderPtr = new Instance(new OpenCylinder(0, s2 - s1, rb));
	phi0TopCylinderPtr.rotateX(90);
	phi0TopCylinderPtr.rotateY(phi0);
	phi0TopCylinderPtr.translate(xc1, y1 - rb, zc1);
	phi0TopCylinderPtr.setTransformTexture(false);
	objects.add(phi0TopCylinderPtr);
	
	
	// phi1 bottom cylinder
	
	Instance phi1BottomCylinderPtr = new Instance(new OpenCylinder(0, s2 - s1, rb));
	phi1BottomCylinderPtr.rotateX(90);
	phi1BottomCylinderPtr.rotateY(phi1);
	phi1BottomCylinderPtr.translate(xc3, y0 + rb, zc3);
	phi1BottomCylinderPtr.setTransformTexture(false);
	objects.add(phi1BottomCylinderPtr);
	
	
	// phi1 top cylinder
	
	Instance phi1TopCylinderPtr = new Instance(new OpenCylinder(0, s2 - s1, rb));
	phi1TopCylinderPtr.rotateX(90);
	phi1TopCylinderPtr.rotateY(phi1);
	phi1TopCylinderPtr.translate(xc3, y1 - rb, zc3);
	phi1TopCylinderPtr.setTransformTexture(false);
	objects.add(phi1TopCylinderPtr);
	
	
	// top flat surface -----------------------------------------------------------------------------------
	
	// main part
	
	Point3D center=new Point3D(0, y1, 0);
	Normal normal=new Normal(0, 1, 0);
	double rMin = r0 + rb;
	double rMax = r1 - rb;
	phiMin = phi0 + alpha * 180.0 / Math.PI;
	phiMax = phi1 - alpha * 180.0 / Math.PI;
	
	PartRing topMainPartPtr = new PartRing(center,  rMin, rMax, phiMin, phiMax);
	objects.add(topMainPartPtr);
	
	
	// small phi0 side patch
	
	rMin = 0.0;
	rMax = s2 - s1;
	phiMin = 0.0;
	phiMax = alpha * 180.0 / Math.PI;
		
	Instance topPhi0PatchPtr = new Instance(new PartRing(center,  rMin, rMax, phiMin, phiMax));
	topPhi0PatchPtr.rotateY(phi0);
	topPhi0PatchPtr.translate(xc1, 0.0, zc1);
	topPhi0PatchPtr.setTransformTexture(false);
	objects.add(topPhi0PatchPtr);
	
	
	// small phi1 side patch
	
	phiMin = 360.0 - alpha * 180.0 / Math.PI;
	phiMax = 360.0;
	
	Instance topPhi1PatchPtr = new Instance(new PartRing(center,  rMin, rMax, phiMin, phiMax));
	topPhi1PatchPtr.rotateY(phi1);
	topPhi1PatchPtr.translate(xc3, 0.0, zc3);
	topPhi1PatchPtr.setTransformTexture(false);
	objects.add(topPhi1PatchPtr);
	

	
	// bottom flat surface ---------------------------------------------------------------------------------
	
	// main part
	
	center = new Point3D(0, y0, 0);
	normal = new Normal(0, -1, 0);
	rMin = r0 + rb;
	rMax = r1 - rb;
	phiMin = phi0 + alpha * 180.0 / Math.PI;
	phiMax = phi1 - alpha * 180.0 / Math.PI;
	
	PartRing bottomMainPartPtr = new PartRing(center,  rMin, rMax, phiMin, phiMax);
	objects.add(bottomMainPartPtr);
	
	
	// small phi0 side patch
	
	rMin = 0.0;
	rMax = s2 - s1;
	phiMin = 0.0;
	phiMax = alpha * 180.0 / Math.PI;
		
	Instance bottomPhi0PatchPtr = new Instance(new PartRing(center,  rMin, rMax, phiMin, phiMax));
	bottomPhi0PatchPtr.rotateY(phi0);
	bottomPhi0PatchPtr.translate(xc1, 0.0, zc1);
	bottomPhi0PatchPtr.setTransformTexture(false);
	objects.add(bottomPhi0PatchPtr);
	
	
	// small phi1 side patch
	
	phiMin = 360.0 - alpha * 180.0 / Math.PI;
	phiMax = 360.0;
	
	Instance bottomPhi1PatchPtr = new Instance(new PartRing(center,  rMin, rMax, phiMin, phiMax));
	bottomPhi1PatchPtr.rotateY(phi1);
	bottomPhi1PatchPtr.translate(xc3, 0.0, zc3);
	bottomPhi1PatchPtr.setTransformTexture(false);
	objects.add(bottomPhi1PatchPtr);
	
	
	
	// compute the bounding box
	
	double x[] = {xc1, xc2, xc3, xc4};
	double z[] = {zc1, zc2, zc3, zc4};
	
	
	// first, assume that the wedge is completely inside a quadrant, which will be true for most wedges
	
	// work out the maximum && minimum values
	
	double x0 = Utility.HUGE_VALUE;
	double z0 = Utility.HUGE_VALUE;
		
	for (int j = 0; j <= 3; j++)  {
		if (x[j] < x0)
			x0 = x[j];
	}
		
	for (int j = 0; j <= 3; j++) {
		if (z[j] < z0)
			z0 = z[j];
	}
	
	double x1 = -Utility.HUGE_VALUE;
	double z1 = -Utility.HUGE_VALUE;   
	
	for (int j = 0; j <= 3; j++) {
		if (x[j] > x1)
			x1 = x[j];
	}
		
	for (int j = 0; j <= 3; j++) {
		if (z[j] > z1)
			z1 = z[j];
	}
	
	// assign values to the bounding box
	
	bbox.x0 = x0 - rb;
	bbox.y0 = y0;
	bbox.z0 = z0 - rb;
	bbox.x1 = x1 + rb;
	bbox.y1 = y1;
	bbox.z1 = z1 + rb;
	
	boolean spans90 = phi0 < 90 && phi1 > 90;
	boolean spans180 = phi0 < 180 && phi1 > 180;
	boolean spans270 = phi0 < 270 && phi1 > 270;
	
	if (spans90 && spans180 && spans270) {
		bbox.x0 = -r1;
		bbox.z0 = -r1;
		bbox.x1 = r1;
		bbox.z1 = max(zc2, zc4);
	}
	else if (spans90 && spans180) {
		bbox.x0 = xc4 - rb;
		bbox.z0 = -r1;
		bbox.x1 = r1;
		bbox.z1 = zc2 + rb;
	}
	else if (spans180 && spans270) {
		bbox.x0 = -r1;
		bbox.z0 = -r1;
		bbox.x1 = xc2 + rb;
		bbox.z1 = zc4 + rb;
	}
	else if (spans90) {
		bbox.x0 = min(xc1, xc3);
		bbox.z0 = zc4 - rb;
		bbox.x1 = r1;
		bbox.z1 = zc2 + rb;
	}
	else if (spans180) {
		bbox.x0 = xc4 - rb;
		bbox.z0 = -r1;
		bbox.x1 = xc2 + rb;
		bbox.z1 = max(zc1, zc3);
	}
	else if (spans270) {
		bbox.x0 = -r1;
		bbox.z0 = zc2 - rb;
		bbox.x1 = max(xc1, xc3);
		bbox.z1 = zc4 + rb;
	}	
}


																																																														


    @Override
    public BBox getBoundingBox() {
	return (bbox);
    }



public boolean hit(Ray ray, ShadeRec sr)  {	
	if (bbox.hit(ray))
		return (super.hit(ray,  sr));
	else
		return (false);
}


}
