/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controller.StartGUIController;
import View.StartGUIFrame;
import java.awt.Dimension;

/**
 * Created by osboxes on 31/01/16.
 */
public class Main {
    
    public static void main(String args[]){
        
        final Dimension prefferedSize = new Dimension(830, 680);
        
        StartGUIFrame startGUIFrame = new StartGUIFrame(prefferedSize);
        StartGUIController controller = new StartGUIController(startGUIFrame);
    }
    
}
