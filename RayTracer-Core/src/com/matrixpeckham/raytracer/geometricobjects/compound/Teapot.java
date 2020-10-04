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

import com.matrixpeckham.raytracer.geometricobjects.parametric.BezierPatch;
import com.matrixpeckham.raytracer.util.Point3D;
import java.util.logging.Logger;

/**
 * Rosette. The parameters are: number of rings, central hole radius, ring
 * width, y axis extent, bevel radius
 *
 * @author William Matrix Peckham
 */
public class Teapot extends Grid {

    static int[][] indices = new int[][]{
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16},
        {4, 17, 18, 19, 8, 20, 21, 22, 12, 23, 24, 25, 16, 26, 27, 28},
        {19, 29, 30, 31, 22, 32, 33, 34, 25, 35, 36, 37, 28, 38, 39, 40},
        {31, 41, 42, 1, 34, 43, 44, 5, 37, 45, 46, 9, 40, 47, 48, 13},
        {13, 14, 15, 16, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60},
        {16, 26, 27, 28, 52, 61, 62, 63, 56, 64, 65, 66, 60, 67, 68, 69},
        {28, 38, 39, 40, 63, 70, 71, 72, 66, 73, 74, 75, 69, 76, 77, 78},
        {40, 47, 48, 13, 72, 79, 80, 49, 75, 81, 82, 53, 78, 83, 84, 57},
        {57, 58, 59, 60, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96},
        {60, 67, 68, 69, 88, 97, 98, 99, 92, 100, 101, 102, 96, 103, 104, 105},
        {69, 76, 77, 78, 99, 106, 107, 108, 102, 109, 110, 111, 105, 112, 113,
            114},
        {78, 83, 84, 57, 108, 115, 116, 85, 111, 117, 118, 89, 114, 119, 120, 93},
        {121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134,
            135, 136},
        {124, 137, 138, 121, 128, 139, 140, 125, 132, 141, 142, 129, 136, 143,
            144, 133},
        {133, 134, 135, 136, 145, 146, 147, 148, 149, 150, 151, 152, 69, 153,
            154, 155},
        {136, 143, 144, 133, 148, 156, 157, 145, 152, 158, 159, 149, 155, 160,
            161, 69},
        {162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175,
            176, 177},
        {165, 178, 179, 162, 169, 180, 181, 166, 173, 182, 183, 170, 177, 184,
            185, 174},
        {174, 175, 176, 177, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195,
            196, 197},
        {177, 184, 185, 174, 189, 198, 199, 186, 193, 200, 201, 190, 197, 202,
            203, 194},
        {204, 204, 204, 204, 207, 208, 209, 210, 211, 211, 211, 211, 212, 213,
            214, 215},
        {204, 204, 204, 204, 210, 217, 218, 219, 211, 211, 211, 211, 215, 220,
            221, 222},
        {204, 204, 204, 204, 219, 224, 225, 226, 211, 211, 211, 211, 222, 227,
            228, 229},
        {204, 204, 204, 204, 226, 230, 231, 207, 211, 211, 211, 211, 229, 232,
            233, 212},
        {212, 213, 214, 215, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243,
            244, 245},
        {215, 220, 221, 222, 237, 246, 247, 248, 241, 249, 250, 251, 245, 252,
            253, 254},
        {222, 227, 228, 229, 248, 255, 256, 257, 251, 258, 259, 260, 254, 261,
            262, 263},
        {229, 232, 233, 212, 257, 264, 265, 234, 260, 266, 267, 238, 263, 268,
            269, 242},
        {270, 270, 270, 270, 279, 280, 281, 282, 275, 276, 277, 278, 271, 272,
            273, 274},
        {270, 270, 270, 270, 282, 289, 290, 291, 278, 286, 287, 288, 274, 283,
            284, 285},
        {270, 270, 270, 270, 291, 298, 299, 300, 288, 295, 296, 297, 285, 292,
            293, 294},
        {270, 270, 270, 270, 300, 305, 306, 279, 297, 303, 304, 275, 294, 301,
            302, 271}
    };

    static {
        for (int i = 0; i < indices.length; i++) {
            for (int j = 0; j < indices[i].length; j++) {
                indices[i][j]--;
            }
        }
    }

    static Point3D[] verts = new Point3D[]{
        new Point3D(1.4, 0.0, 2.4
        ),
        new Point3D(1.4, -0.784, 2.4
        ),
        new Point3D(0.784, -1.4, 2.4
        ),
        new Point3D(0.0, -1.4, 2.4
        ),
        new Point3D(1.3375, 0.0, 2.53125
        ),
        new Point3D(1.3375, -0.749, 2.53125
        ),
        new Point3D(0.749, -1.3375, 2.53125
        ),
        new Point3D(0.0, -1.3375, 2.53125
        ),
        new Point3D(1.4375, 0.0, 2.53125
        ),
        new Point3D(1.4375, -0.805, 2.53125
        ),
        new Point3D(0.805, -1.4375, 2.53125
        ),
        new Point3D(0.0, -1.4375, 2.53125
        ),
        new Point3D(1.5, 0.0, 2.4
        ),
        new Point3D(1.5, -0.84, 2.4
        ),
        new Point3D(0.84, -1.5, 2.4
        ),
        new Point3D(0.0, -1.5, 2.4
        ),
        new Point3D(-0.784, -1.4, 2.4
        ),
        new Point3D(-1.4, -0.784, 2.4
        ),
        new Point3D(-1.4, 0.0, 2.4
        ),
        new Point3D(-0.749, -1.3375, 2.53125
        ),
        new Point3D(-1.3375, -0.749, 2.53125
        ),
        new Point3D(-1.3375, 0.0, 2.53125
        ),
        new Point3D(-0.805, -1.4375, 2.53125
        ),
        new Point3D(-1.4375, -0.805, 2.53125
        ),
        new Point3D(-1.4375, 0.0, 2.53125
        ),
        new Point3D(-0.84, -1.5, 2.4
        ),
        new Point3D(-1.5, -0.84, 2.4
        ),
        new Point3D(-1.5, 0.0, 2.4
        ),
        new Point3D(-1.4, 0.784, 2.4
        ),
        new Point3D(-0.784, 1.4, 2.4
        ),
        new Point3D(0.0, 1.4, 2.4
        ),
        new Point3D(-1.3375, 0.749, 2.53125
        ),
        new Point3D(-0.749, 1.3375, 2.53125
        ),
        new Point3D(0.0, 1.3375, 2.53125
        ),
        new Point3D(-1.4375, 0.805, 2.53125
        ),
        new Point3D(-0.805, 1.4375, 2.53125
        ),
        new Point3D(0.0, 1.4375, 2.53125
        ),
        new Point3D(-1.5, 0.84, 2.4
        ),
        new Point3D(-0.84, 1.5, 2.4
        ),
        new Point3D(0.0, 1.5, 2.4
        ),
        new Point3D(0.784, 1.4, 2.4
        ),
        new Point3D(1.4, 0.784, 2.4
        ),
        new Point3D(0.749, 1.3375, 2.53125
        ),
        new Point3D(1.3375, 0.749, 2.53125
        ),
        new Point3D(0.805, 1.4375, 2.53125
        ),
        new Point3D(1.4375, 0.805, 2.53125
        ),
        new Point3D(0.84, 1.5, 2.4
        ),
        new Point3D(1.5, 0.84, 2.4
        ),
        new Point3D(1.75, 0.0, 1.875
        ),
        new Point3D(1.75, -0.98, 1.875
        ),
        new Point3D(0.98, -1.75, 1.875
        ),
        new Point3D(0.0, -1.75, 1.875
        ),
        new Point3D(2.0, 0.0, 1.35
        ),
        new Point3D(2.0, -1.12, 1.35
        ),
        new Point3D(1.12, -2.0, 1.35
        ),
        new Point3D(0.0, -2.0, 1.35
        ),
        new Point3D(2.0, 0.0, 0.9
        ),
        new Point3D(2.0, -1.12, 0.9
        ),
        new Point3D(1.12, -2.0, 0.9
        ),
        new Point3D(0.0, -2.0, 0.9
        ),
        new Point3D(-0.98, -1.75, 1.875
        ),
        new Point3D(-1.75, -0.98, 1.875
        ),
        new Point3D(-1.75, 0.0, 1.875
        ),
        new Point3D(-1.12, -2.0, 1.35
        ),
        new Point3D(-2.0, -1.12, 1.35
        ),
        new Point3D(-2.0, 0.0, 1.35
        ),
        new Point3D(-1.12, -2.0, 0.9
        ),
        new Point3D(-2.0, -1.12, 0.9
        ),
        new Point3D(-2.0, 0.0, 0.9
        ),
        new Point3D(-1.75, 0.98, 1.875
        ),
        new Point3D(-0.98, 1.75, 1.875
        ),
        new Point3D(0.0, 1.75, 1.875
        ),
        new Point3D(-2.0, 1.12, 1.35
        ),
        new Point3D(-1.12, 2.0, 1.35
        ),
        new Point3D(0.0, 2.0, 1.35
        ),
        new Point3D(-2.0, 1.12, 0.9
        ),
        new Point3D(-1.12, 2.0, 0.9
        ),
        new Point3D(0.0, 2.0, 0.9
        ),
        new Point3D(0.98, 1.75, 1.875
        ),
        new Point3D(1.75, 0.98, 1.875
        ),
        new Point3D(1.12, 2.0, 1.35
        ),
        new Point3D(2.0, 1.12, 1.35
        ),
        new Point3D(1.12, 2.0, 0.9
        ),
        new Point3D(2.0, 1.12, 0.9
        ),
        new Point3D(2.0, 0.0, 0.45
        ),
        new Point3D(2.0, -1.12, 0.45
        ),
        new Point3D(1.12, -2.0, 0.45
        ),
        new Point3D(0.0, -2.0, 0.45
        ),
        new Point3D(1.5, 0.0, 0.225
        ),
        new Point3D(1.5, -0.84, 0.225
        ),
        new Point3D(0.84, -1.5, 0.225
        ),
        new Point3D(0.0, -1.5, 0.225
        ),
        new Point3D(1.5, 0.0, 0.15
        ),
        new Point3D(1.5, -0.84, 0.15
        ),
        new Point3D(0.84, -1.5, 0.15
        ),
        new Point3D(0.0, -1.5, 0.15
        ),
        new Point3D(-1.12, -2.0, 0.45
        ),
        new Point3D(-2.0, -1.12, 0.45
        ),
        new Point3D(-2.0, 0.0, 0.45
        ),
        new Point3D(-0.84, -1.5, 0.225
        ),
        new Point3D(-1.5, -0.84, 0.225
        ),
        new Point3D(-1.5, 0.0, 0.225
        ),
        new Point3D(-0.84, -1.5, 0.15
        ),
        new Point3D(-1.5, -0.84, 0.15
        ),
        new Point3D(-1.5, 0.0, 0.15
        ),
        new Point3D(-2.0, 1.12, 0.45
        ),
        new Point3D(-1.12, 2.0, 0.45
        ),
        new Point3D(0.0, 2.0, 0.45
        ),
        new Point3D(-1.5, 0.84, 0.225
        ),
        new Point3D(-0.84, 1.5, 0.225
        ),
        new Point3D(0.0, 1.5, 0.225
        ),
        new Point3D(-1.5, 0.84, 0.15
        ),
        new Point3D(-0.84, 1.5, 0.15
        ),
        new Point3D(0.0, 1.5, 0.15
        ),
        new Point3D(1.12, 2.0, 0.45
        ),
        new Point3D(2.0, 1.12, 0.45
        ),
        new Point3D(0.84, 1.5, 0.225
        ),
        new Point3D(1.5, 0.84, 0.225
        ),
        new Point3D(0.84, 1.5, 0.15
        ),
        new Point3D(1.5, 0.84, 0.15
        ),
        new Point3D(-1.6, 0.0, 2.025
        ),
        new Point3D(-1.6, -0.3, 2.025
        ),
        new Point3D(-1.5, -0.3, 2.25
        ),
        new Point3D(-1.5, 0.0, 2.25
        ),
        new Point3D(-2.3, 0.0, 2.025
        ),
        new Point3D(-2.3, -0.3, 2.025
        ),
        new Point3D(-2.5, -0.3, 2.25
        ),
        new Point3D(-2.5, 0.0, 2.25
        ),
        new Point3D(-2.7, 0.0, 2.025
        ),
        new Point3D(-2.7, -0.3, 2.025
        ),
        new Point3D(-3.0, -0.3, 2.25
        ),
        new Point3D(-3.0, 0.0, 2.25
        ),
        new Point3D(-2.7, 0.0, 1.8
        ),
        new Point3D(-2.7, -0.3, 1.8
        ),
        new Point3D(-3.0, -0.3, 1.8
        ),
        new Point3D(-3.0, 0.0, 1.8
        ),
        new Point3D(-1.5, 0.3, 2.25
        ),
        new Point3D(-1.6, 0.3, 2.025
        ),
        new Point3D(-2.5, 0.3, 2.25
        ),
        new Point3D(-2.3, 0.3, 2.025
        ),
        new Point3D(-3.0, 0.3, 2.25
        ),
        new Point3D(-2.7, 0.3, 2.025
        ),
        new Point3D(-3.0, 0.3, 1.8
        ),
        new Point3D(-2.7, 0.3, 1.8
        ),
        new Point3D(-2.7, 0.0, 1.575
        ),
        new Point3D(-2.7, -0.3, 1.575
        ),
        new Point3D(-3.0, -0.3, 1.35
        ),
        new Point3D(-3.0, 0.0, 1.35
        ),
        new Point3D(-2.5, 0.0, 1.125
        ),
        new Point3D(-2.5, -0.3, 1.125
        ),
        new Point3D(-2.65, -0.3, 0.9375
        ),
        new Point3D(-2.65, 0.0, 0.9375
        ),
        new Point3D(-2.0, -0.3, 0.9
        ),
        new Point3D(-1.9, -0.3, 0.6
        ),
        new Point3D(-1.9, 0.0, 0.6
        ),
        new Point3D(-3.0, 0.3, 1.35
        ),
        new Point3D(-2.7, 0.3, 1.575
        ),
        new Point3D(-2.65, 0.3, 0.9375
        ),
        new Point3D(-2.5, 0.3, 1.125
        ),
        new Point3D(-1.9, 0.3, 0.6
        ),
        new Point3D(-2.0, 0.3, 0.9
        ),
        new Point3D(1.7, 0.0, 1.425
        ),
        new Point3D(1.7, -0.66, 1.425
        ),
        new Point3D(1.7, -0.66, 0.6
        ),
        new Point3D(1.7, 0.0, 0.6
        ),
        new Point3D(2.6, 0.0, 1.425
        ),
        new Point3D(2.6, -0.66, 1.425
        ),
        new Point3D(3.1, -0.66, 0.825
        ),
        new Point3D(3.1, 0.0, 0.825
        ),
        new Point3D(2.3, 0.0, 2.1
        ),
        new Point3D(2.3, -0.25, 2.1
        ),
        new Point3D(2.4, -0.25, 2.025
        ),
        new Point3D(2.4, 0.0, 2.025
        ),
        new Point3D(2.7, 0.0, 2.4
        ),
        new Point3D(2.7, -0.25, 2.4
        ),
        new Point3D(3.3, -0.25, 2.4
        ),
        new Point3D(3.3, 0.0, 2.4
        ),
        new Point3D(1.7, 0.66, 0.6
        ),
        new Point3D(1.7, 0.66, 1.425
        ),
        new Point3D(3.1, 0.66, 0.825
        ),
        new Point3D(2.6, 0.66, 1.425
        ),
        new Point3D(2.4, 0.25, 2.025
        ),
        new Point3D(2.3, 0.25, 2.1
        ),
        new Point3D(3.3, 0.25, 2.4
        ),
        new Point3D(2.7, 0.25, 2.4
        ),
        new Point3D(2.8, 0.0, 2.475
        ),
        new Point3D(2.8, -0.25, 2.475
        ),
        new Point3D(3.525, -0.25, 2.49375
        ),
        new Point3D(3.525, 0.0, 2.49375
        ),
        new Point3D(2.9, 0.0, 2.475
        ),
        new Point3D(2.9, -0.15, 2.475
        ),
        new Point3D(3.45, -0.15, 2.5125
        ),
        new Point3D(3.45, 0.0, 2.5125
        ),
        new Point3D(2.8, 0.0, 2.4
        ),
        new Point3D(2.8, -0.15, 2.4
        ),
        new Point3D(3.2, -0.15, 2.4
        ),
        new Point3D(3.2, 0.0, 2.4
        ),
        new Point3D(3.525, 0.25, 2.49375
        ),
        new Point3D(2.8, 0.25, 2.475
        ),
        new Point3D(3.45, 0.15, 2.5125
        ),
        new Point3D(2.9, 0.15, 2.475
        ),
        new Point3D(3.2, 0.15, 2.4
        ),
        new Point3D(2.8, 0.15, 2.4
        ),
        new Point3D(0.0, 0.0, 3.15
        ),
        new Point3D(0.0, -0.002, 3.15
        ),
        new Point3D(0.002, 0.0, 3.15
        ),
        new Point3D(0.8, 0.0, 3.15
        ),
        new Point3D(0.8, -0.45, 3.15
        ),
        new Point3D(0.45, -0.8, 3.15
        ),
        new Point3D(0.0, -0.8, 3.15
        ),
        new Point3D(0.0, 0.0, 2.85
        ),
        new Point3D(0.2, 0.0, 2.7
        ),
        new Point3D(0.2, -0.112, 2.7
        ),
        new Point3D(0.112, -0.2, 2.7
        ),
        new Point3D(0.0, -0.2, 2.7
        ),
        new Point3D(-0.002, 0.0, 3.15
        ),
        new Point3D(-0.45, -0.8, 3.15
        ),
        new Point3D(-0.8, -0.45, 3.15
        ),
        new Point3D(-0.8, 0.0, 3.15
        ),
        new Point3D(-0.112, -0.2, 2.7
        ),
        new Point3D(-0.2, -0.112, 2.7
        ),
        new Point3D(-0.2, 0.0, 2.7
        ),
        new Point3D(0.0, 0.002, 3.15
        ),
        new Point3D(-0.8, 0.45, 3.15
        ),
        new Point3D(-0.45, 0.8, 3.15
        ),
        new Point3D(0.0, 0.8, 3.15
        ),
        new Point3D(-0.2, 0.112, 2.7
        ),
        new Point3D(-0.112, 0.2, 2.7
        ),
        new Point3D(0.0, 0.2, 2.7
        ),
        new Point3D(0.45, 0.8, 3.15
        ),
        new Point3D(0.8, 0.45, 3.15
        ),
        new Point3D(0.112, 0.2, 2.7
        ),
        new Point3D(0.2, 0.112, 2.7
        ),
        new Point3D(0.4, 0.0, 2.55
        ),
        new Point3D(0.4, -0.224, 2.55
        ),
        new Point3D(0.224, -0.4, 2.55
        ),
        new Point3D(0.0, -0.4, 2.55
        ),
        new Point3D(1.3, 0.0, 2.55
        ),
        new Point3D(1.3, -0.728, 2.55
        ),
        new Point3D(0.728, -1.3, 2.55
        ),
        new Point3D(0.0, -1.3, 2.55
        ),
        new Point3D(1.3, 0.0, 2.4
        ),
        new Point3D(1.3, -0.728, 2.4
        ),
        new Point3D(0.728, -1.3, 2.4
        ),
        new Point3D(0.0, -1.3, 2.4
        ),
        new Point3D(-0.224, -0.4, 2.55
        ),
        new Point3D(-0.4, -0.224, 2.55
        ),
        new Point3D(-0.4, 0.0, 2.55
        ),
        new Point3D(-0.728, -1.3, 2.55
        ),
        new Point3D(-1.3, -0.728, 2.55
        ),
        new Point3D(-1.3, 0.0, 2.55
        ),
        new Point3D(-0.728, -1.3, 2.4
        ),
        new Point3D(-1.3, -0.728, 2.4
        ),
        new Point3D(-1.3, 0.0, 2.4
        ),
        new Point3D(-0.4, 0.224, 2.55
        ),
        new Point3D(-0.224, 0.4, 2.55
        ),
        new Point3D(0.0, 0.4, 2.55
        ),
        new Point3D(-1.3, 0.728, 2.55
        ),
        new Point3D(-0.728, 1.3, 2.55
        ),
        new Point3D(0.0, 1.3, 2.55
        ),
        new Point3D(-1.3, 0.728, 2.4
        ),
        new Point3D(-0.728, 1.3, 2.4
        ),
        new Point3D(0.0, 1.3, 2.4
        ),
        new Point3D(0.224, 0.4, 2.55
        ),
        new Point3D(0.4, 0.224, 2.55
        ),
        new Point3D(0.728, 1.3, 2.55
        ),
        new Point3D(1.3, 0.728, 2.55
        ),
        new Point3D(0.728, 1.3, 2.4
        ),
        new Point3D(1.3, 0.728, 2.4
        ),
        new Point3D(0.0, 0.0, 0.0
        ),
        new Point3D(1.5, 0.0, 0.15
        ),
        new Point3D(1.5, 0.84, 0.15
        ),
        new Point3D(0.84, 1.5, 0.15
        ),
        new Point3D(0.0, 1.5, 0.15
        ),
        new Point3D(1.5, 0.0, 0.075
        ),
        new Point3D(1.5, 0.84, 0.075
        ),
        new Point3D(0.84, 1.5, 0.075
        ),
        new Point3D(0.0, 1.5, 0.075
        ),
        new Point3D(1.425, 0.0, 0.0
        ),
        new Point3D(1.425, 0.798, 0.0
        ),
        new Point3D(0.798, 1.425, 0.0
        ),
        new Point3D(0.0, 1.425, 0.0
        ),
        new Point3D(-0.84, 1.5, 0.15
        ),
        new Point3D(-1.5, 0.84, 0.15
        ),
        new Point3D(-1.5, 0.0, 0.15
        ),
        new Point3D(-0.84, 1.5, 0.075
        ),
        new Point3D(-1.5, 0.84, 0.075
        ),
        new Point3D(-1.5, 0.0, 0.075
        ),
        new Point3D(-0.798, 1.425, 0.0
        ),
        new Point3D(-1.425, 0.798, 0.0
        ),
        new Point3D(-1.425, 0.0, 0.0
        ),
        new Point3D(-1.5, -0.84, 0.15
        ),
        new Point3D(-0.84, -1.5, 0.15
        ),
        new Point3D(0.0, -1.5, 0.15
        ),
        new Point3D(-1.5, -0.84, 0.075
        ),
        new Point3D(-0.84, -1.5, 0.075
        ),
        new Point3D(0.0, -1.5, 0.075
        ),
        new Point3D(-1.425, -0.798, 0.0
        ),
        new Point3D(-0.798, -1.425, 0.0
        ),
        new Point3D(0.0, -1.425, 0.0
        ),
        new Point3D(0.84, -1.5, 0.15
        ),
        new Point3D(1.5, -0.84, 0.15
        ),
        new Point3D(0.84, -1.5, 0.075
        ),
        new Point3D(1.5, -0.84, 0.075
        ),
        new Point3D(0.798, -1.425, 0.0
        ),
        new Point3D(1.425, -0.798, 0.0
        )
    };

    /**
     * default constructor
     */
    public Teapot() {
        super();
        constructTeapot();
    }

    /**
     * copy constructor
     *
     * @param r
     */
    public Teapot(Teapot r) {
        super();
        constructTeapot();
    }

    /**
     * clone
     *
     * @return
     */
    @Override
    public Teapot cloneGeometry() {
        return new Teapot(this);
    }

    /**
     * assignment
     *
     * @param rhs
     *
     * @return
     */
    public Teapot setTo(Teapot rhs) {

        return (this);
    }

    /**
     * this function constructs the wedges in a rosette pattern and stores them
     * in a grid this is the regular version, for Figure 21.11
     */
    final void constructTeapot() {

        for (int i = 0; i < 4; i++) {
            BezierPatch p1 = new BezierPatch(new Point3D[]{
                verts[indices[i][0]],
                verts[indices[i][1]],
                verts[indices[i][2]],
                verts[indices[i][3]],
                verts[indices[i][4]],
                verts[indices[i][5]],
                verts[indices[i][6]],
                verts[indices[i][7]],
                verts[indices[i][8]],
                verts[indices[i][9]],
                verts[indices[i][10]],
                verts[indices[i][11]],
                verts[indices[i][12]],
                verts[indices[i][13]],
                verts[indices[i][14]],
                verts[indices[i][15]]
            });
            addObject(p1);
        }

    }

    private static final Logger LOG = Logger.getLogger(Teapot.class.getName());

}
