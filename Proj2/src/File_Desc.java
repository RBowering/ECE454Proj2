/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distributed;

import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author ishashori
 */
public class File_Desc {
    String name;
    String loc;
    FileReader fileIn;
    FileWriter fileOut;
    int fIn = 0;
    int fOut = 0;
    File_Desc(String na, String lo){
        name = na;
        loc = lo;
    }
    
    
    
}
