package calc.helpers;

import java.util.List;

import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;

//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 

public class NatualLogaritmeFunction 
    {	
    // Member variables
    private final List<Double> x_list;
    private final List<Double> y_list;

    /**
     * Constructor of QuadraticFunction
     * @param _x_list	input data
     * @param _y_list	target data
     */
    public NatualLogaritmeFunction(List<Double> _x_list, List<Double> _y_list) 
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
    /**
     * Define model function and return values
     * @return	return the values of model function by input data
     */
    @SuppressWarnings("Convert2Lambda")
    public MultivariateVectorFunction retMVF() 
        {
	return new MultivariateVectorFunction() 
            {
            @Override
            public double[] value(double[] variables) throws IllegalArgumentException 
                {
		double[] values = new double[x_list.size()];
		for (int i = 0; i < values.length; ++i) 
                    {
		    values[i] = (variables[0] * x_list.get(i) + variables[1]) * x_list.get(i) + variables[2];
		    }
		return values;
		}			
            };
        }
    
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
    /**
     * Return the jacobian of the model function
     * @return	return the jacobian
     */
    public MultivariateMatrixFunction retMMF() 
        {
    	return new MultivariateMatrixFunction() 
            {
            @Override
            public double[][] value(double[] point) throws IllegalArgumentException 
                {
		// TODO Auto-generated method stub
                return jacobian(point);
		}
            
//  ======================================================= 

            /**
             * calculate and retjacobian
             * @param	variables	parameters of model function
             * @return	jacobian	jacobian of the model function
             */
            private double[][] jacobian(double[] variables) 
                {
                double[][] jacobian = new double[x_list.size()][3];
                for (int i = 0; i < jacobian.length; ++i) 
                    {
                    jacobian[i][0] = x_list.get(i) * x_list.get(i);
                    jacobian[i][1] = x_list.get(i);
                    jacobian[i][2] = 1.0;
                    }
                return jacobian;
                }	
            };
        }
//  ======================================================= 
//  ======================================================= 
//  =======================================================     
}