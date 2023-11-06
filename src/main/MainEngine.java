/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import calc.Calculator;
import data.Datamanager;

/**
 *
 * @author arnoud
 */
public class MainEngine 
    {
    private final Datamanager datamanager;
    
    private MainWindow mainwindow;
    
    public MainEngine()
        {
        datamanager = new Datamanager();        
            
        Calculator calulator = new Calculator(datamanager);
        calulator.calc();
        
        
        java.awt.EventQueue.invokeLater(() ->   {
                                                mainwindow = new MainWindow(datamanager);
                                                mainwindow.setVisible(true);
                                                });
        }
    
   //=====================================================================
    
    public void start() 
        {


        }
    
    //=====================================================================

    }
