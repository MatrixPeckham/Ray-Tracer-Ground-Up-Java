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
 * Holds the specification for an element and implements the means to read it.
 * @author William Matrix Peckham
 */
class ElementType {
    /**
     * Property name to index
     */
    TreeMap<String, Integer> props = new TreeMap<>();
    /**
     * whether the property at the index is a list
     */
    ArrayList<Boolean> isList = new ArrayList<>();
    /**
     * Type of the property
     */
    ArrayList<Type> propType = new ArrayList<>();
    /**
     * If property at index is a list, this will hold the type that the 
     * count of the list will be
     */
    ArrayList<Type> listCountType = new ArrayList<>();
    /**
     * element type name 
     */
    String name;

    /**
     * Constructor
     * @param name 
     */
    public ElementType(String name) {
        this.name = name;
    }
    
    /**
     * This reads a single element of this element type from a stream.
     * @param in stream to read from
     * @param binary true if the file is binary, false if ascii
     * @param littleEndian flag for endianness if binary, igored otherwise
     * @return element 
     * @throws IOException required for reading from stream
     */
    PLYElement readFrom(BufferedInputStream in, boolean binary,
            boolean littleEndian) throws IOException {
        PLYElement element = new PLYElement(this);
        //for every property
        for (int i = 0; i < propType.size(); i++) {
            //if it's a list, read list count
            if (isList.get(i)) {
                int num = listCountType.get(i).
                        getAsInt(in, binary, littleEndian);
                double[] lst = new double[num];
                //then populate list type item
                for (int j = 0; j < num; j++) {
                    lst[j] = propType.get(i).getAsDouble(in, binary,
                            littleEndian);
                }
                element.setDoubleList(i, lst);
            } else {
                //not a list just populate the element with a double
                element.setDouble(i, propType.get(i).getAsDouble(in, binary,
                        littleEndian));
            }
        }
        //alleviates the problem of undeclared properties in elements.
        if(!binary) readToNewline(in);
        return element;
    }
    /**
     * reads to the next line in an ascii file. 
     * @param in
     * @throws IOException 
     */
    private void readToNewline(BufferedInputStream in) throws IOException {
        char c = (char)in.read();
        while(c!='\n'&&c!='\r'){
            c=(char)in.read();
        }
    }
    /**
     * Enum for the possible types in a ply file, knows how to read itself from
     * a stream. 
     */
    public enum Type {
        /**
         * Character/Byte
         */
        CHAR(1, "char"),
        /**
         * Character/Byte
         */
        UCHAR(1, "uchar"),
        /**
         * Short
         */
        SHORT(2, "short"),
        /**
         * Short
         */
        USHORT(2, "ushort"),
        /**
         * Int
         */
        INT(4, "int"),
        /**
         * Int
         */
        UINT(4, "uint"),
        /**
         * Float
         */
        FLOAT(4, "float"),
        /**
         * Double
         */
        DOUBLE(8, "double"),;
        /**
         * name of this type
         */
        public final String name;
        /**
         * number of bytes in the binary file
         */
        public final int bytes;
        /**
         * Constructor
         * @param bytes
         * @param name 
         */
        private Type(int bytes, String name) {
            this.bytes = bytes;
            this.name = name;
        }
        /**
         * Reads an integer from the file and returns it. only works for integer
         * types.
         * @param in stream to read from
         * @param binary if the file is binary
         * @param littleEndian endian-ness of binary file, ignored if not binary
         * @return integer that was in the file
         * @throws IOException when called on float type, or other reasons
         */
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
        /**
         * Reads an double from the file and returns it. will convert ints to double
         * @param in stream to read from
         * @param binary if the file is binary
         * @param littleEndian endian-ness of binary file, ignored if not binary
         * @return integer that was in the file
         * @throws IOException errors while reading
         */
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
