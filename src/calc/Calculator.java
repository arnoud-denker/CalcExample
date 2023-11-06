package calc;

import calc.helpers.ExponantialHelperFunction;
import calc.helpers.QuadraticParabolicFunction;
import data.Datamanager;
import data.TimeValue;
import helpers.CommonUtilities;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

public class Calculator 
    {
    private final Datamanager datamanager;
    
    private final ArrayList <TimeValue> afterEventData;
    public Calculator(Datamanager _datamanager)
        {
        datamanager = _datamanager;
        afterEventData = datamanager.get_after_array();
        }
    
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
    public void calc() 
        {           
        map_parabol(10);
//      map_exponential(10);
//      map_natural_logarithm(10);
        }
    
//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
    private void map_parabol(int _nr_off_ticks_after_to_check )     // Ensure the window size is odd and centered around the peak index
        {
        List<Double> x = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        for (int i = 0; i < _nr_off_ticks_after_to_check; i++)
            {
            TimeValue eod = afterEventData.get(i);
            x.add(eod.date);
            yValues.add(eod.value);
            }
        
        QuadraticParabolicFunction qf = new QuadraticParabolicFunction(x,yValues);
 
        LeastSquaresBuilder lsb = new LeastSquaresBuilder();    // prepare construction of LeastSquresProblem by builder
        lsb.model(qf.retMVF_parabole(), qf.retMMF_parabole());  // set model function and its jacobian
        double[] newTarget = qf.calculateTarget();
    
        lsb.target(newTarget);//set target data
        double[] newStart = {1,1,1};
        
        lsb.start(newStart);        // set initial parameters     
        lsb.maxEvaluations(100);    // set upper limit of evaluation time       
        lsb.maxIterations(1000);    // set upper limit of iteration time
               
        LevenbergMarquardtOptimizer lmo = new LevenbergMarquardtOptimizer();//construct LevenbergMarquardtOptimizer 

        try
            {       
            LeastSquaresOptimizer.Optimum lsoo = lmo.optimize(lsb.build());//do LevenbergMarquardt optimization

            final double[] optimalValues = lsoo.getPoint().toArray();	//get optimized parameters		

            double a = optimalValues[0]; // Coefficient for x^2 term
            double b = optimalValues[1]; // Coefficient for x term
            double c = optimalValues[2]; // Constant term
            
            System.out.println("A: " + a);
            System.out.println("B: " + b);
            System.out.println("C: " + c);
            
            List<Double> predictedValues = new ArrayList<>();

            for (int i = 0; i < _nr_off_ticks_after_to_check; i++) 
                {
                TimeValue eod = afterEventData.get(i);    
                double date = eod.date;  

                double calcedValue = (a * date * date) + (b * date) + c;
                datamanager.add_to_calced_parabol_list(date, calcedValue);

                predictedValues.add(calcedValue);
                }

            double meanY = CommonUtilities.calculateMean(yValues);                  // Calculate the mean of the observed values
            double sst = CommonUtilities.calculateSST(yValues, meanY);              // Calculate the total sum of squares (SST) 
            double sse = CommonUtilities.calculateSSE(yValues, predictedValues);    // Calculate the residual sum of squares (SSE)
            double rSquared = 1 - (sse / sst);                                      // Calculate R-squared (coefficient of determination)
   
            System.out.println("R-squared value: " + rSquared);// Print the R-squared value   
            System.out.println("Stop para");
            } 
        catch (Exception e) 
            {
            System.out.println(e.toString());
            }
        }

//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    //y = A + Be(cx)
//    //y=a⋅exp(b⋅t)+c
    private void map_exponential(int _nr_off_ticks_after_to_check )     // Ensure the window size is odd and centered around the peak index
        {
        List<Double> x = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < _nr_off_ticks_after_to_check; i++)
            {
            TimeValue eod = afterEventData.get(i);
            x.add(eod.date);
            yValues.add(eod.value);
            
            obs.add(eod.date, eod.value);
            }
        
        ExponantialHelperFunction qf = new ExponantialHelperFunction(x,yValues);
 
        LeastSquaresProblem problem = new LeastSquaresBuilder()
                .model(qf.retMVJF_expo())
                .target(qf.calculateTarget())
                .maxEvaluations(100)    // set upper limit of evaluation time       
                .maxIterations(1000)    // set upper limit of iteration time
                //.weight(obs.toListOfWeights())
                .start(new double[]{1.0, 0.1}) // Initial parameter guesses
                .build();


        LeastSquaresOptimizer optimizer = new LevenbergMarquardtOptimizer();
        Optimum optimum = optimizer.optimize(problem);

        double fittedA = optimum.getPoint().getEntry(0);
        double fittedB = optimum.getPoint().getEntry(1);
        
        System.out.println("A: " + fittedA);
        System.out.println("B: " + fittedB);
        }

//  ======================================================= 
//  ======================================================= 
//  ======================================================= 
    
//    private void map_natural_logarithm(int _nr_off_ticks_after_to_check )     // Ensure the window size is odd and centered around the peak index
//        {

//                
//        WeightedObservedPoints after_points = new WeightedObservedPoints();       
//        
//        for (int i = 0; i < _nr_off_ticks_after_to_check; i++)
//            {
//            Entity_EOD eod = ticklist_afterEvent.get(i);
//            after_points.add(eod.datetime, eod.lasttick_close);
//            }
//        // Fit a logarithmic curve (y = a * ln(x) + b) to the data points
//        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1); // Logarithmic curve (1st degree)
//        double[] coefficients = fitter.fit(after_points.toList());
//
//        // Check if the fit is good enough (e.g., based on the threshold)
//        double a = coefficients[0]; // Coefficient for a * ln(x)
//        double b = coefficients[1]; // Constant term
//
//        for (int i = 0; i < _nr_off_ticks_after_to_check; i++) 
//            {
//            Entity_EOD eod = ticklist_afterEvent.get(i);    
//            double date = eod.datetime;  
//                
//            double calcedValue = (a *  Math.log(date)) + b;
//            _quantify_object.add_to_ticklist_calced_naturalLogarithm(date, calcedValue);
//            }
//        
//        // You can define your criteria for determining if it's a natural logarithm function here.
//        // For example, you can check if 'a' is not zero and 'b' is close to the expected constant.
//
//        //System.out.println("NaturalLogarithm: " + Math.abs(a) + " " + Math.abs(b));
//        
//        //return (Math.abs(a) > threshold) && (Math.abs(b) < threshold); // Adjust the threshold as needed
     //   }


 //  ======================================================= 
//  ======================================================= 
//  ======================================================= 
  
    }
