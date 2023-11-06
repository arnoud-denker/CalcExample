/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calc.helpers;

import java.util.List;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

/**
 *
 * @author arnoud
 */
public class ExponantialHelperFunction 
    {
    private final List<Double> x_list;
    private final List<Double> y_list;
    
    public ExponantialHelperFunction(List<Double> _x_list, List<Double> _y_list)
        {
        x_list = _x_list;
        y_list = _y_list;
        }

//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    /**
     * return target data as double array by target data
     * @return target	double arrya of target data
     */
    public double[] calculateTarget() 
        {
        double[] target = new double[y_list.size()];
        for (int i = 0; i < y_list.size(); i++) 
            {
            target[i] = y_list.get(i);
            }
        return target;
        }
    
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
    @SuppressWarnings("Convert2Lambda")
    public MultivariateJacobianFunction retMVJF_expo() 
        {
	return new MultivariateJacobianFunction() 
            {
            @Override
            public Pair<RealVector, RealMatrix> value(RealVector point) 
                {
                double a = point.getEntry(0);
                double b = point.getEntry(1);
                RealVector value = new ArrayRealVector( x_list.size() );
                RealMatrix jacobian = new Array2DRowRealMatrix(x_list.size() , 2);

                for (int i = 0; i < x_list.size() ; i++) 
                    {
                    double expectedY = a * Math.exp(b * x_list.get(i) );
                    value.setEntry(i, expectedY);
                    jacobian.setEntry(i, 0, Math.exp(b * x_list.get(i) )); // Partial derivative with respect to 'a'
                    jacobian.setEntry(i, 1, a * x_list.get(i) * Math.exp(b * x_list.get(i) )); // Partial derivative with respect to 'b'
                    }
                
                return new Pair<>(value, jacobian);
                }
            };		
        }
    
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
    
    }
