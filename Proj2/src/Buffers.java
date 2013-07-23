/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distributed;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author ishashori
 */
public class Buffers {
    public volatile Queue<String> msgBuffer;

    public Buffers()
    {
        msgBuffer = new LinkedList<String>();
    }
}
