/*
 * Copyright (C) 2021 matri
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
package com.matrixpeckham.raytracer.build;

import com.matrixpeckham.parse.parse.Parser;
import com.matrixpeckham.parse.parse.tokens.Token;
import com.matrixpeckham.parse.utensil.NullCloneable;
import com.matrixpeckham.parse.utensil.TypeOrType;
import com.matrixpeckham.raytracer.world.BuildWorldFunction;
import com.matrixpeckham.raytracer.world.World;
import java.util.Map;

/**
 * Build world function implementation class that parses a file.
 * <p>
 * File Specification can be found on the
 * <a href="https://github.com/MatrixPeckham/Ray-Tracer-Ground-Up-Java/wiki">Github
 * wiki</a>
 *
 *
 * @author matri
 *
 */
public class LoadWorldFromFile implements BuildWorldFunction {

    Parser<Token, Map<String, TypeOrType<Double, Map<String, Double>>>, NullCloneable> parser;

    @Override
    public void build(final World w) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
