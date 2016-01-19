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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class to load a ply file.
 *
 * @author William Matrix Peckham
 */
public class PLYFile {

    /**
     * reads a line from a stream
     * @param in
     * @return
     * @throws IOException 
     */
    static String readLine(BufferedInputStream in) throws IOException {
        //builds a string
        StringBuilder s = new StringBuilder();
        char c = (char) in.read();
        //append to the string
        //carriege return linefeed or NONChar (EOF)
        while ("\r\n\uFFFF".indexOf(c) == -1) {
            s.append(c);
            //because (char)-1 doesn't work well.
            int temp = in.read();
            if(temp==-1) break;
            c = (char) temp;
        }
        return s.toString();
    }

    /**
     * is the file binary
     */
    boolean binary = false;
    /**
     * if the file is binary which endian-ness is it
     */
    boolean littleEndian = false;
    /**
     * Element names to types
     */
    TreeMap<String, ElementType> types = new TreeMap<>();
    /**
     * Names for indices. 
     */
    ArrayList<String> names = new ArrayList<>();
    /**
     * Element names to list of elements.
     */
    TreeMap<String, ArrayList<PLYElement>> elms = new TreeMap<>();
    /**
     * Element counts for indices. 
     */
    ArrayList<Integer> elementCounts = new ArrayList<>();
    /**
     * default constructor
     */
    public PLYFile() {
    }
    /**
     * Constructor to directly read file.
     * @param f file to read.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public PLYFile(File f) throws FileNotFoundException, IOException {
        readPLYFile(f);
    }

    /**
     * Reads a word from an ascii file, a word is non-whitespace characters 
     * surrounded by whitespace characters.
     * @param in
     * @return
     * @throws IOException 
     */
    protected static String readWord(BufferedInputStream in) throws IOException {
        //builds a string
        StringBuilder s = new StringBuilder();
        char c = (char) in.read();
        //skip whitespace
        while (" \t\r\n\uFFFF".indexOf(c) != -1) {
            int temp = in.read();
            if(temp==-1) break;
            c = (char) temp;
        }
        //append to the string
        while (" \t\r\n\uFFFF".indexOf(c) == -1) {
            s.append(c);
            int temp = in.read();
            if(temp==-1) break;
            c = (char) temp;
        }
        //if the word is comment we need to skip the rest of the line and re-call
        if (s.toString().equals("comment")) {
            while ("\r\n\uFFFF".indexOf(c) == -1) {
            int temp = in.read();
            if(temp==-1) break;
            c = (char) temp;
//              s.append(c);
            }
            //c=(char)in.read();
            return readWord(in);
        } else {
            return s.toString().trim();
        }
    }
    /**
     * Reads a file.
     * @param f
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void readPLYFile(File f) throws FileNotFoundException, IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        readHeader(in);
        //for all the names of elements
        for (int i = 0; i < names.size(); i++) {
            //get the type of element for this name
            ElementType t = types.get(names.get(i));
            //for all expected elements in this type
            for (int j = 0; j < elementCounts.get(i); j++) {
                //get the list for this element name or make it if we haven't yet
                ArrayList<PLYElement> es = elms.get(names.get(i));
                if (es == null) {
                    es = new ArrayList<>();
                    elms.put(names.get(i), es);
                }
                es.add(t.readFrom(in, binary, littleEndian));
            }
        }
    }
    /**
     * Reads the header of a file.
     * @param in
     * @throws IOException 
     */
    private void readHeader(BufferedInputStream in) throws IOException {
        //reads the magic number
        String ply = readWord(in);
        if (!ply.equals("ply")) {
            throw new IOException("File is not PLY or is corrupted");
        }
        //read format string
        String format = readWord(in);
        if (!format.equals("format")) {
            throw new IOException("Ply formating error");
        }
        String endian = readWord(in);
        switch (endian) {
            case "ascii":
                binary = false;
                break;
            case "binary_little_endian":
                binary = true;
                littleEndian = true;
                break;
            case "binary_big_endian":
                binary = true;
                littleEndian = false;
                break;
            default:
                throw new IOException("UNKNOWN PLY TYPE " + endian);
        }
        //skip
        readWord(in);
        
        String first = readWord(in);
        //until we reach the end of the header
        int elementsEncountered = 0;
        while (!first.equals("end_header")) {
            //first should be element
            if (!first.equals("element")) {
                throw new IOException("Poorly formatted PLY");
            }
            //build element type
            String elementName = readWord(in);
            ElementType t = new ElementType(elementName);
            //number of expected elements
            int count = Integer.parseInt(readWord(in));
            elementCounts.add(count);
            first = readWord(in);
            int props = 0;
            //read each property of this element
            while (first.equals("property")) {
                String propType = readWord(in);
                ElementType.Type propT = null;
                if (!propType.equals("list")) {
                    propT = ElementType.Type.valueOf(propType.toUpperCase());
                }
                if (propT == null) {
                    if (propType.equals("list")) {
                        t.isList.add(true);
                        t.listCountType.add(ElementType.Type.valueOf(
                                readWord(in).toUpperCase()));
                        t.propType.add(ElementType.Type.valueOf(readWord(in).
                                toUpperCase()));
                        t.props.put(readWord(in), props);
                        props++;
                    } else {
                        throw new IOException("Unknown property type");
                    }
                } else {
                    t.isList.add(false);
                    t.listCountType.add(null);
                    t.propType.add(propT);
                    t.props.put(readWord(in), props);
                    props++;
                }
                first = readWord(in);
            }
            types.put(elementName, t);
            names.add(elementName);
            elementsEncountered++;

        }
    }
    /**
     * get elements by name.
     * @param name
     * @return 
     */
    public ArrayList<PLYElement> getElements(String name) {
        return elms.get(name);
    }
}
