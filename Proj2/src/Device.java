/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distributed;

/**
 *
 * @author ishashori
 */
public class Device {
    
    String dev;
    String ip;
    String port;
    boolean local;
    Device(String d, String i, String p, boolean lo){
        dev = d;
        ip = i;
        port = p;
        local = lo;
    }
}
