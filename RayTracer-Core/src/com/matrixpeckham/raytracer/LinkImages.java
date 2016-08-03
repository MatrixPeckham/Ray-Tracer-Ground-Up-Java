/*
 * Copyright (C) 2016 William Matrix Peckham
 *
 * This program is free software; you can redistribute it and\\or
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
package com.matrixpeckham.raytracer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author William Matrix Peckham
 */
public class LinkImages {

    public static void main(String[] args) throws FileNotFoundException {
        File output = new File("link-image.txt");
        PrintWriter out = new PrintWriter(output);
        String baseLocation
                = "C:\\Users\\Owner\\Documents\\GitHub\\RayTracerWiki\\Ray-Tracer-Ground-Up-Java.wiki\\images\\Samples";
        String rel
                = "C:\\Users\\Owner\\Documents\\GitHub\\RayTracerWiki\\Ray-Tracer-Ground-Up-Java.wiki\\";
        File initFolder = new File(baseLocation);
        recurse(initFolder, baseLocation, rel, out, 0);
        out.flush();
        out.close();
    }

    private static void recurse(File initFolder, String baseLocation, String rel,
            PrintWriter out, int level) {
        String levName = initFolder.getAbsolutePath().replace(baseLocation, "");
        for (int i = 0; i < level; i++) {
            out.append("    ");
        }
        out.append("-");
        if (initFolder.isDirectory()) {
            String[] levs = levName.split("\\\\");
            out.append(levName.replace("\\", " "));
            for (File f : initFolder.listFiles()) {
                out.append("\n");
                recurse(f, baseLocation + levs[0] + "\\", rel, out, level + 1);
            }
        } else {
            out.append("![");
            out.append(initFolder.getAbsolutePath().replace(baseLocation, "").
                    replace("\\", " "));
            out.append("](");
            out.append("./");
            out.append(initFolder.getAbsolutePath().replace(rel, "").replace(
                    "\\", "/"));
            out.append("}");
        }
    }
}
