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
package com.matrixpeckham.raytracer.samplers;

import com.matrixpeckham.raytracer.util.Point2D;
import com.matrixpeckham.raytracer.util.Utility;

/**
 *
 * @author William Matrix Peckham
 */
public class PureRandom extends Sampler {

    public PureRandom(){
        super();
    }
    public PureRandom(int num){
        super(num);
        generateSamples();
    }
    public PureRandom(PureRandom u){
        super(u);
        generateSamples();
    }
    
    
    
    @Override
    public void generateSamples() {
int n = (int) Math.sqrt(numSamples);

	for (int j = 0; j < numSets; j++)
		for (int p = 0; p < n; p++)		
			for (int q = 0; q < n; q++)
				samples.add(new Point2D(Utility.randDouble(), Utility.randDouble()));
    }

    @Override
    public Sampler clone() {
        return new PureRandom(this);
    }
    
}