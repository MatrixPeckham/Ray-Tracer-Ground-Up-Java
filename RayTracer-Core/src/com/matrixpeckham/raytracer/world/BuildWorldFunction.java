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
package com.matrixpeckham.raytracer.world;

/**
 * Because java lacks the ability to selectively include a function body, I use
 * this interface. In the C++ code World.build() is left undefined, and is
 * defined in many different files, only one of which is #include-ed in the main
 * method. This is not easy in java, so instead I created this interface and it
 * builds the world that is passed to it. In the main class we simply create an
 * instance of the one we wish to create. <br/> Later this will make it possible
 * to choose which world to build from within the GUI, it shouldn't be hard to
 * add.
 *
 * @author William Matrix Peckham
 */
public interface BuildWorldFunction {

    /**
     * This function will build the scene by adding objects to the passed in
     * world. It should throw runtime exceptions when things fail, but this may
     * change, if we make it more interactive.
     *
     * @param w world to build.
     */
    public void build(World w);

}
