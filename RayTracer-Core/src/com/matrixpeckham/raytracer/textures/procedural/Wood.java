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
package com.matrixpeckham.raytracer.textures.procedural;

import com.matrixpeckham.raytracer.textures.Texture;
import com.matrixpeckham.raytracer.util.Point3D;
import com.matrixpeckham.raytracer.util.RGBColor;
import com.matrixpeckham.raytracer.util.ShadeRec;
import com.matrixpeckham.raytracer.util.Utility;
import static com.matrixpeckham.raytracer.util.Utility.clamp;
import static com.matrixpeckham.raytracer.util.Utility.mixColor;
import static com.matrixpeckham.raytracer.util.Utility.mixDouble;
import static com.matrixpeckham.raytracer.util.Utility.smoothStep;
import com.matrixpeckham.raytracer.util.Vector3D;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.logging.Logger;

/**
 * This class defines a procedural texture based on the Larry Gritz Renderman
 * wood texture described in Apodaca and Gritz (2000). The rings here are
 * centered on the y axis, instead of the z axis as in Apodaca and Gritz. This
 * is not a complete implementation because it does not do the intrinsic
 * antialiasing that the Renderman version does. The following description of
 * the parameters is largely based on code comments in Apodaca and Gritz.
 * <pre>
 *  		lightColor: 				The light background color of the wood.
 *  		darkColor: 				The darker color of the rings and grain.
 * 		ringFrequency:				The mean radial distance berween the rings.
 * 		ringUneveness:				If this is zero, the rings are equally spaced and have equal thickness.
 * 									If this is greater than zero, the rings are unequally spaced and have different thicknesses.
 * 		ringNoise:					These two parameters just perturb the hit point. The default value of ringNoise is 0.02, and if you
 * 		ringNoiseFrequency:		make ringNoise = 1.0, the wood is destroyed. The wood still looks good with these parameters set to zero.
 * 		trunkWobble:				These two parameters perturb the trunk with a noise function so that it's not exactly along the y axis.
 * 		trunkWobbleFrequency:		trunkWobble determines the noise amplitude, and trunkWobbleFrequency determines its spatial frequency.
 * 		angularWobble:				These two parameters add noise to the rings so that they are not exactly round; angularWobble specifies
 * 		angularWobbleFrequency:	the magnitude of the noise, and	angularWobbleFrequency specifies how quckly it varies around the rings.
 * 		grainFrequency:			This seems to control the size of the grain.
 * 		grainy:						This is grain-nee, not grain "y". It should be in the range [0, 1]. It determines how the grain is weighted
 * 									in the shading. With grainy = 0 there is no grain; with grainy = 1, the grain is fully shaded.
 * 		ringy:						This is ring-ee, not ring "y". It should be in the range [0, 1]. It determines how the rings are weighted
 * 									in the shading. With ringy = 0 there are no rings; with ringy = 1, the rings are fully shaded.
 * </pre> All these parameters have default values. The easiest way to
 * experiment with different values is to use the first or second const Fructor,
 * and the access functions, which are supplied for each parameter.
 *
 * @author William Matrix Peckham
 */
public class Wood implements Texture {

    /**
     * default constructor
     */
    public Wood() {
        noisePtr = new CubicNoise(2, 4.0, 0.5); // this specifies numOctaves, lacunarity, and gain
        lightColor = Utility.WHITE;
        darkColor = Utility.BLACK;
        ringFrequency = 4.0;
        ringUneveness = 0.25;
        ringNoise = 0.02;
        ringNoiseFrequency = 1.0;
        trunkWobble = 0.15;
        trunkWobbleFrequency = 0.025;
        angularWobble = 0.5;
        angularWobbleFrequency = 1.0;
        grainFrequency = 25.0;
        grainy = 0.5;
        ringy = 0.5;
    }

    /**
     * Specifies the two colors and uses default values for everything else
     *
     * @param light
     * @param dark
     */
    public Wood(RGBColor light, RGBColor dark) {
        noisePtr = new CubicNoise(2, 4.0, 0.5); // this specifies numOctaves, lacunarity, and gain
        lightColor = light;
        darkColor = dark;
        ringFrequency = 4.0;
        ringUneveness = 0.25;
        ringNoise = 0.02;
        ringNoiseFrequency = 1.0;
        trunkWobble = 0.15;
        trunkWobbleFrequency = 0.025;
        angularWobble = 0.5;
        angularWobbleFrequency = 1.0;
        grainFrequency = 25.0;
        grainy = 0.5;
        ringy = 0.5;
    }

    /**
     * Allows you to specify everything To use this ructor you will have to
     * define a noise object in the build function or use a call to a noise
     * constructor as the first argument.
     *
     * @param NoisePtr
     * @param LightColor
     * @param DarkColor
     * @param RingFrequency
     * @param RingUneveness
     * @param RingNoise
     * @param RingNoiseFrequency
     * @param TrunkWobble
     * @param TrunkWobbleFrequency
     * @param AngularWobble
     * @param AngularWobbleFrequency
     * @param GrainFrequency
     * @param Grainy
     * @param Ringy
     */
    public Wood(LatticeNoise NoisePtr,
            RGBColor LightColor,
            RGBColor DarkColor,
            double RingFrequency,
            double RingUneveness,
            double RingNoise,
            double RingNoiseFrequency,
            double TrunkWobble,
            double TrunkWobbleFrequency,
            double AngularWobble,
            double AngularWobbleFrequency,
            double GrainFrequency,
            double Grainy,
            double Ringy
    ) {
        noisePtr = NoisePtr;
        lightColor = LightColor;
        darkColor = DarkColor;
        ringFrequency = RingFrequency;
        ringUneveness = RingUneveness;
        ringNoise = RingNoise;
        ringNoiseFrequency = RingNoiseFrequency;
        trunkWobble = TrunkWobble;
        trunkWobbleFrequency = TrunkWobbleFrequency;
        angularWobble = AngularWobble;
        angularWobbleFrequency = AngularWobbleFrequency;
        grainFrequency = GrainFrequency;
        grainy = Grainy;
        ringy = Ringy;
    }

    /**
     * copy constructor
     *
     * @param wood
     */
    public Wood(Wood wood
    ) {
        lightColor = wood.lightColor;
        darkColor = wood.darkColor;
        ringFrequency = wood.ringFrequency;
        ringUneveness = wood.ringUneveness;
        ringNoise = wood.ringNoise;
        ringNoiseFrequency = wood.ringNoiseFrequency;
        trunkWobble = wood.trunkWobble;
        trunkWobbleFrequency = wood.trunkWobbleFrequency;
        angularWobble = wood.angularWobble;
        angularWobbleFrequency = wood.angularWobbleFrequency;
        grainFrequency = wood.grainFrequency;
        grainy = wood.grainy;
        ringy = wood.ringy;
        if (wood.noisePtr != null) {
            noisePtr = wood.noisePtr.cloneNoise();
        } else {
            noisePtr = null;
        }
    }

    /**
     * java equals override
     *
     * @param rhs
     * @return
     */
    public Wood setTo(Wood rhs) {
        if (this == rhs) {
            return this;
        }
        if (rhs.noisePtr != null) {
            noisePtr = rhs.noisePtr.cloneNoise();
        }

        lightColor = rhs.lightColor;
        darkColor = rhs.darkColor;
        ringFrequency = rhs.ringFrequency;
        ringUneveness = rhs.ringUneveness;
        ringNoise = rhs.ringNoise;
        ringNoiseFrequency = rhs.ringNoiseFrequency;
        trunkWobble = rhs.trunkWobble;
        trunkWobbleFrequency = rhs.trunkWobbleFrequency;
        angularWobble = rhs.angularWobble;
        angularWobbleFrequency = rhs.angularWobbleFrequency;
        grainFrequency = rhs.grainFrequency;
        grainy = rhs.grainy;
        ringy = rhs.ringy;

        return this;
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Texture cloneTexture() {
        return new Wood(this);
    }

    /**
     * sample texture
     *
     * @param sr
     * @return
     */
    @Override
    public RGBColor getColor(ShadeRec sr
    ) {
        Point3D hitPoint = sr.localHitPosition;

        // perturb the hit point
        Vector3D offset = noisePtr.vectorFBM(hitPoint.mul(ringNoiseFrequency));

        Point3D ringPoint = hitPoint.add(offset.mul(ringNoise));

        // perturb the trunk so that it's quite along the z axis
        Vector3D tempVec = noisePtr.vectorNoise(new Point3D(0, 0,
                hitPoint.y * trunkWobbleFrequency)).mul(trunkWobble);
        ringPoint.x += tempVec.x;
        ringPoint.z += tempVec.z;

        // distance from the y axis
        double r = sqrt(ringPoint.x * ringPoint.x + ringPoint.z
                * ringPoint.z) * ringFrequency;

        // perturb r so that the rings aren't quite round
        Point3D tempVec2 = new Point3D();
        tempVec2.x = angularWobbleFrequency * ringPoint.x;
        tempVec2.y = angularWobbleFrequency * ringPoint.y * 0.1;
        tempVec2.z = angularWobbleFrequency * ringPoint.z;

        double deltaR = angularWobble * Utility.smoothStep(0.0, 5.0, r)
                * noisePtr.
                valueNoise(tempVec2);
        r += deltaR;

        // add some noise so that the rings are not equally spaced and have different thicknesses
        r += ringUneveness * noisePtr.valueNoise(new Point3D(r));

        double inRing = Utility.smoothPulseTrain(0.1, 0.55, 0.7, 0.95, 1.0, r);

        // the grain
        Point3D grainPoint = new Point3D();
        grainPoint.x = hitPoint.x * grainFrequency;
        grainPoint.y = hitPoint.y * grainFrequency * 0.05;
        grainPoint.z = hitPoint.z * grainFrequency;

        double dpgrain = 0.2;
        double grain = 0.0;
        double amplitude = 1.0;

        for (int i = 0; i < 2; i++) {
            double grainValid = 1.0 - smoothStep(0.2, 0.6, dpgrain);
            if (grainValid > 0.0) {
                double g = grainValid * noisePtr.valueNoise(grainPoint);
                g *= (0.3 + 0.7 * inRing);
                g = pow(clamp(0.8 - g, 0.0, 1.0), 2.0);
                g = grainy * smoothStep(0.5, 1.0, g);
                if (i == 0) {
                    inRing *= (1.0 - 0.4 * grainValid);
                }
                grain = amplitude * max(grain, g);
            }
            grainPoint = grainPoint.mul(2);
            dpgrain *= 2.0;
            amplitude *= 0.5;
        }

        double finalValue = mixDouble(inRing * ringy, 1.0, grain);

        return (mixColor(lightColor, darkColor, finalValue));
    }

    /**
     * setter
     *
     * @param NoisePtr
     */
    public void setNoise(LatticeNoise NoisePtr
    ) {
        noisePtr = NoisePtr;
    }

    /**
     * setter
     *
     * @param NumOctaves
     */
    public void setNumOctaves(int NumOctaves
    ) {
        noisePtr.setNumOctaves(NumOctaves);
    }

    /**
     * setter
     *
     * @param Lacunarity
     */
    public void setLacunarity(double Lacunarity
    ) {
        noisePtr.setLacunarity(Lacunarity);
    }

    /**
     *
     * @param Gain
     */
    public void setGain(double Gain
    ) {
        noisePtr.setGain(Gain);
    }

    /**
     * setter
     *
     * @param c
     */
    public void setLightColor(RGBColor c
    ) {
        lightColor = c;
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setLightColor(double r, double g, double b
    ) {
        lightColor.r = r;
        lightColor.b = b;
        lightColor.g = g;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setLightColor(double c
    ) {
        lightColor.r = lightColor.b = lightColor.g = c;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setDarkColor(RGBColor c
    ) {
        darkColor = c;
    }

    /**
     * setter
     *
     * @param r
     * @param g
     * @param b
     */
    public void setDarkColor(double r, double g, double b
    ) {
        darkColor.r = r;
        darkColor.b = b;
        darkColor.g = g;
    }

    /**
     * setter
     *
     * @param c
     */
    public void setDarkColor(double c
    ) {
        darkColor.r = darkColor.b = darkColor.g = c;
    }

    /**
     * setter
     *
     * @param RingFrequency
     */
    public void setRingFrequency(double RingFrequency
    ) {
        ringFrequency = RingFrequency;
    }

    /**
     * setter
     *
     * @param RingUneveness
     */
    public void setRingUneveness(double RingUneveness
    ) {
        ringUneveness = RingUneveness;
    }

    /**
     * setter
     *
     * @param RingNoise
     */
    public void setRingNoise(double RingNoise
    ) {
        ringNoise = RingNoise;
    }

    /**
     * setter
     *
     * @param RingNoiseFrequency
     */
    public void setRingNoiseFrequency(double RingNoiseFrequency
    ) {
        ringNoiseFrequency = RingNoiseFrequency;
    }

    /**
     * setter
     *
     * @param TrunkWobble
     */
    public void setTrunkWobble(double TrunkWobble
    ) {
        trunkWobble = TrunkWobble;
    }

    /**
     * setter
     *
     * @param TrunkWobbleFrequency
     */
    public void setTrunkWobbleFrequency(double TrunkWobbleFrequency
    ) {
        trunkWobbleFrequency = TrunkWobbleFrequency;
    }

    /**
     * setter
     *
     * @param AngularWobble
     */
    public void setAngularWobble(double AngularWobble
    ) {
        angularWobble = AngularWobble;
    }

    /**
     * setter
     *
     * @param AngularWobbleFrequency
     */
    public void setAngularWobbleFrequency(double AngularWobbleFrequency
    ) {
        angularWobbleFrequency = AngularWobbleFrequency;
    }

    /**
     * setter
     *
     * @param GrainFrequency
     */
    public void setGrainFrequency(double GrainFrequency
    ) {
        grainFrequency = GrainFrequency;
    }

    /**
     * setter
     *
     * @param Grainy
     */
    public void setGrainy(double Grainy
    ) {
        grainy = Grainy;
    }

    /**
     * setter
     *
     * @param Ringy
     */
    public void setRingy(double Ringy) {
        ringy = Ringy;
    }

    //see class comment
    Noise noisePtr;

    RGBColor lightColor;

    RGBColor darkColor;

    double ringFrequency;

    double ringUneveness;

    double ringNoise;

    double ringNoiseFrequency;

    double trunkWobble;

    double trunkWobbleFrequency;

    double angularWobble;

    double angularWobbleFrequency;

    double grainFrequency;

    double grainy;

    double ringy;

    private static final Logger LOG = Logger.getLogger(Wood.class.getName());

}
