package pl.agh.edu.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.utils.Pair;

public class HotelPopularityFunction {

	public static HashMap<Pair<Integer, Integer>, Double> getSeasonalMultipliers(HotelType hotelType) {
		Map<Integer, Double> monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(hotelType);
		HashMap<Pair<Integer, Integer>, Double> multipliers = new HashMap<>();

		double[] x = new double[14];
		double[] y = new double[14];

		x[0] = 0.0;
		y[0] = monthClientsMultiplier.get(12);

		IntStream.range(1, 13).forEach(i -> {
			x[i] = i;
			y[i] = monthClientsMultiplier.get(i);
		});

		x[13] = 13.0;
		y[13] = monthClientsMultiplier.get(1);

		SplineInterpolator interpolator = new SplineInterpolator();
		PolynomialSplineFunction splineFunction = interpolator.interpolate(x, y);

		IntStream.range(0, 31).forEach(i -> {
			for (Integer key : Arrays.asList(1, 3, 5, 7, 8, 10, 12)) {
				multipliers.put(new Pair<>(key, i + 1), splineFunction.value(key + i / 31.0));
			}
			if (i < 30) {
				for (Integer key : Arrays.asList(4, 6, 9, 11)) {
					multipliers.put(new Pair<>(key, i + 1), splineFunction.value(key + i / 30.0));
				}
			}
			if (i < 28)
				multipliers.put(new Pair<>(2, i + 1), splineFunction.value(2 + 1 / 28.0));
		});
		multipliers.put(new Pair<>(2, 29), splineFunction.value(2 + 27 / 28.0));

		return multipliers;
	}
}
