package pl.agh.edu.utils;

public class HotelPopularityPlotter {
    public static void main(String[] args) {

        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0};
        double[] y = {2.0, 3.0, 1.0, 5.0, 4.0, 6.0, 8.0, 7.0, 10.0, 9.0, 11.0, 12.0};

        SplineInterpolator interpolator = new SplineInterpolator();
        PolynomialSplineFunction splineFunction = interpolator.interpolate(x, y);

        int sth = 8;
        int numPoints = 12 * sth;
        XYSeries series = new XYSeries("Data Points");

        for (int i = 0; i < numPoints-8; i++) {
            double xi = 1.0 + i / (double) sth; // Calculate the x-value
            double yi = splineFunction.value(xi); // Calculate the y-value
            series.add(xi, yi);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Spline Plot",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);

        JFrame frame = new JFrame("Spline Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

    // Function to get popularity for any day of the year
    static double popularityFunction(int t) {
        // Example: Hotel popularity data for 12 months
        double[] months = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
        double[] popularity = { 50, 60, 40, 80, 10, 20, 30, 40, 50, 100, 90, 70 };

        // Create a spline interpolator
        SplineInterpolator interpolator = new SplineInterpolator();

        // Fit the interpolator to the data points
        PolynomialSplineFunction splineFunction = interpolator.interpolate(months, popularity);

        // Create a function that returns popularity for any day of the year
        double minMonth = 1;
        double maxMonth = 12;
        double daysPerYear = 365.0; // Adjust for leap years if needed

        // Normalize the input to be within [0, 12)
        double month = (t % daysPerYear) / (daysPerYear / 12.0);
//
//		// Ensure the month is within [1, 12] to match the data
//		if (month < minMonth) {
//			month += 12;
//		}

        return splineFunction.value(t/31 + 1);
    };
}
