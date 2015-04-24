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
 *
 * @author William Matrix Peckham
 */
public class PLYFile {
    boolean binary = false;
    boolean littleEndian = false;
    
    TreeMap<String, ElementType> types = new TreeMap<>();
    ArrayList<String> names = new ArrayList<>();
    TreeMap<String, ArrayList<PLYElement>> elms = new TreeMap<>();
    ArrayList<Integer> elementCounts = new ArrayList<>();
    
    public PLYFile(){}
    
    public PLYFile(File f) throws FileNotFoundException, IOException{
        readPLYFile(f);
    }
    
    
    
    
    protected static String readWord(BufferedInputStream in) throws IOException{
        StringBuilder s = new StringBuilder();
        char c =(char)in.read();
        while(" \t\r\n".indexOf(c)!=-1){
            c=(char)in.read();
        }
        while(" \t\r\n".indexOf(c)==-1){
            s.append(c);
            c=(char)in.read();
        }
        if(s.toString().equals("comment")){
            while("\r\n".indexOf(c)==-1){
                c=(char)in.read();
//              s.append(c);
            }
            //c=(char)in.read();
            return readWord(in);
        } else {
            return s.toString().trim();
        }
    }

    private void readPLYFile(File f) throws FileNotFoundException, IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
        readHeader(in);
        for(int i = 0; i<names.size(); i++){
            ElementType t = types.get(names.get(i));
            for(int j = 0; j<elementCounts.get(i); j++){
                ArrayList<PLYElement> es = elms.get(names.get(i));
                if(es==null){
                    es=new ArrayList<>();
                    elms.put(names.get(i), es);
                }
                es.add(t.readFrom(in,binary,littleEndian));
            }
        }
    }

    private void readHeader(BufferedInputStream in) throws IOException {
        String ply = readWord(in);
        if(!ply.equals("ply")){
            throw new IOException("File is not PLY or is corrupted");
        }
        String format = readWord(in);
        if(!format.equals("format")){
            throw new IOException("Ply formating error");
        }
        String endian = readWord(in);
        switch (endian) {
            case "ascii":
                binary=false;
                break;
            case "binary_little_endian":
                binary=true;
                littleEndian=true;
                break;
            case "binary_big_endian":
                binary=true;
                littleEndian=false;
                break;
            default:
                throw new IOException("UNKNOWN PLY TYPE " + endian);
        }
        readWord(in);
        String first = readWord(in);
        //until we reach the end of the header
        int elementsEncountered = 0;
        while(!first.equals("end_header")){
            //first should be element
            if(!first.equals("element")){
                throw new IOException("Poorly formatted PLY");
            }
            String elementName = readWord(in);
            ElementType t = new ElementType(elementName);
            int count = Integer.parseInt(readWord(in));
            elementCounts.add(count);
            first=readWord(in);
            int props = 0;
            while(first.equals("property")){
                String propType = readWord(in);
                ElementType.Type propT = null;
                if(!propType.equals("list")){
                    propT=ElementType.Type.valueOf(propType.toUpperCase());
                }
                if(propT == null){
                    if(propType.equals("list")){
                        t.isList.add(true);
                        t.listCountType.add(ElementType.Type.valueOf(readWord(in).toUpperCase()));
                        t.propType.add(ElementType.Type.valueOf(readWord(in).toUpperCase()));
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
                first=readWord(in);
            }
            types.put(elementName, t);
            names.add(elementName);
            elementsEncountered++;
            
        }
    }

    public ArrayList<PLYElement> getElements(String name) {
        return elms.get(name);
    }
}
