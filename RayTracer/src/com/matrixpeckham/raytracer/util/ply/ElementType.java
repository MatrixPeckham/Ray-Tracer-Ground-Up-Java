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
package com.matrixpeckham.raytracer.util.ply;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author William Matrix Peckham
 */
class ElementType {

    TreeMap<String, Integer> props = new TreeMap<>();
    ArrayList<Boolean> isList = new ArrayList<>();
    ArrayList<Type> propType = new ArrayList<>();
    ArrayList<Type> listCountType = new ArrayList<>();
    String name;

    public ElementType(String name) {
        this.name = name;
    }

    PLYElement readFrom(BufferedInputStream in, boolean binary,
            boolean littleEndian) throws IOException {
        PLYElement element = new PLYElement(this);
        for (int i = 0; i < propType.size(); i++) {
            if (isList.get(i)) {
                int num = listCountType.get(i).
                        getAsInt(in, binary, littleEndian);
                double[] lst = new double[num];
                for (int j = 0; j < num; j++) {
                    lst[j] = propType.get(i).getAsDouble(in, binary,
                            littleEndian);
                }
                element.setDoubleList(i, lst);
            } else {
                element.setDouble(i, propType.get(i).getAsDouble(in, binary,
                        littleEndian));
            }
        }
        if(!binary) readToNewline(in);
        return element;
    }

    private void readToNewline(BufferedInputStream in) throws IOException {
        char c = (char)in.read();
        while(c!='\n'&&c!='\r'){
            c=(char)in.read();
        }
    }

    public enum Type {

        CHAR(1, "char"),
        UCHAR(1, "uchar"),
        SHORT(2, "short"),
        USHORT(2, "ushort"),
        INT(4, "int"),
        UINT(4, "uint"),
        FLOAT(4, "float"),
        DOUBLE(8, "double"),;
        public final String name;
        public final int bytes;

        private Type(int bytes, String name) {
            this.bytes = bytes;
            this.name = name;
        }

        private int getAsInt(BufferedInputStream in, boolean binary,
                boolean littleEndian) throws IOException {
            switch (this) {
                case DOUBLE:
                case FLOAT:
                    throw new IOException(
                            "Cannot read int value for float type. Error in reading file likely cause.");
                case SHORT:
                    if (binary) {
                        byte[] ba = new byte[bytes];
                        int ct = in.read(ba);
                        if (ct != bytes) {
                            throw new IOException("End of file wile parsing");
                        }
                        short ret = 0;
                        if (!littleEndian) {
                            for (int i = 0; i < bytes; i++) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        } else {
                            for (int i = bytes - 1; i >= 0; i--) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        }
                        return ret;
                    } else {
                        String s = PLYFile.readWord(in);
                        return Integer.parseInt(s);
                    }
                case CHAR:
                    if (binary) {
                        byte[] ba = new byte[bytes];
                        int ct = in.read(ba);
                        if (ct != bytes) {
                            throw new IOException("End of file wile parsing");
                        }
                        char ret = 0;
                        if (!littleEndian) {
                            for (int i = 0; i < bytes; i++) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        } else {
                            for (int i = bytes - 1; i >= 0; i--) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        }
                        return ret;
                    } else {
                        String s = PLYFile.readWord(in);
                        return Integer.parseInt(s);
                    }
                default:
                    if (binary) {
                        byte[] ba = new byte[bytes];
                        int ct = in.read(ba);
                        if (ct != bytes) {
                            throw new IOException("End of file wile parsing");
                        }
                        int ret = 0;
                        if (!littleEndian) {
                            for (int i = 0; i < bytes; i++) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        } else {
                            for (int i = bytes - 1; i >= 0; i--) {
                                ret <<= 8;
                                ret |= (ba[i] & 0xFF);
                            }
                        }
                        return ret;
                    } else {
                        String s = PLYFile.readWord(in);
                        return Integer.parseInt(s);
                    }
            }
        }

        private double getAsDouble(BufferedInputStream in, boolean binary,
                boolean littleEndian) throws IOException {
            if (!binary) {
                return Double.parseDouble(PLYFile.readWord(in));
            }
            switch (this) {
                case FLOAT: {
                    int ret = 0;
                    byte[] ba = new byte[4];
                    int n = in.read(ba);
                    if (n != 4) {
                        throw new IOException("End of file while reading");
                    }
                    if (!littleEndian) {
                        for (int i = 0; i < bytes; i++) {
                            ret <<= 8;
                            ret |= (ba[i] & 0xFF);
                        }
                    } else {
                        for (int i = bytes - 1; i >= 0; i--) {
                            ret <<= 8;
                            ret |= (ba[i] & 0xFF);
                        }
                    }
                    return Float.intBitsToFloat(ret);
                }
                case DOUBLE:
                    long ret = 0;
                    byte[] ba = new byte[8];
                    int n = in.read(ba);
                    if (n != 8) {
                        throw new IOException("End of file while reading");
                    }
                    if (!littleEndian) {
                        for (int i = 0; i < bytes; i++) {
                            ret <<= 8;
                            ret |= (ba[i] & 0xFF);
                        }
                    } else {
                        for (int i = bytes - 1; i >= 0; i--) {
                            ret <<= 8;
                            ret |= (ba[i] & 0xFF);
                        }
                    }
                    return Double.longBitsToDouble(ret);
                default:
                    return getAsInt(in, binary, littleEndian);
            }
        }

    }
}
