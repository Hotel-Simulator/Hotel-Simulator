package pl.agh.edu.model;

import java.time.MonthDay;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;

public class HotelPopularityFunction {

	public static HashMap<MonthDay, Double> getSeasonalMultipliers(HotelType hotelType) {
		Map<Integer, Double> monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(hotelType);
		HashMap<MonthDay, Double> multipliers = new HashMap<>();

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
			Stream.of(1, 3, 5, 7, 8, 10, 12).forEach(key ->
				multipliers.put(MonthDay.of(key, i + 1), splineFunction.value(key + i / 31.0))
			);
			if (i < 30) {
				Stream.of(4, 6, 9, 11).forEach(key ->
					multipliers.put(MonthDay.of(key, i + 1), splineFunction.value(key + i / 30.0))
				);
			}
			if (i < 28)
				multipliers.put(MonthDay.of(2, i + 1), splineFunction.value(2 + 1 / 28.0));
		});
		multipliers.put(MonthDay.of(2, 29), splineFunction.value(2 + 27 / 28.0));

		return multipliers;
	}
}
