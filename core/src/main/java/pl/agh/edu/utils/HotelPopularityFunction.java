package pl.agh.edu.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;

public class HotelPopularityFunction {

	public static HashMap<Integer, HashMap<Integer, Double>> getSeasonalMultipliers(HotelType hotelType) {
		Map<Integer, Double> monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(hotelType);
		HashMap<Integer, HashMap<Integer, Double>> multipliers = new HashMap<>();
		for (int i = 1; i < 13; i++) {
			multipliers.put(i, new HashMap<>());
		}

		double[] x = new double[14];
		double[] y = new double[14];

		x[0] = 0.0;
		y[0] = monthClientsMultiplier.get(12);

		for (int i = 1; i < 13; i++) {
			x[i] = i;
			y[i] = monthClientsMultiplier.get(i);
		}

		x[13] = 13.0;
		y[13] = monthClientsMultiplier.get(1);

		SplineInterpolator interpolator = new SplineInterpolator();
		PolynomialSplineFunction splineFunction = interpolator.interpolate(x, y);

		for (int i = 0; i < 31; i++) {
			for (Integer key : Arrays.asList(1, 3, 5, 7, 8, 10, 12)) {
				multipliers.get(key).put(i + 1, splineFunction.value(key + i / 31.0));
			}
			if (i < 30) {
				for (Integer key : Arrays.asList(4, 6, 9, 11)) {
					multipliers.get(key).put(i + 1, splineFunction.value(key + i / 30.0));
				}
			}
			if (i < 28)
				multipliers.get(2).put(i + 1, splineFunction.value(2 + 1 / 28.));
		}
		multipliers.get(2).put(29, splineFunction.value(2 + 27 / 28.));

		return multipliers;
	}
}
