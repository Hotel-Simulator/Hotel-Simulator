package pl.agh.edu.model;

import java.time.Month;
import java.time.MonthDay;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;

public class HotelPopularityFunction {

	public static Map<MonthDay, Double> getSeasonalMultipliers(HotelType hotelType) {
		PolynomialSplineFunction splineFunction = getSplineFunction(hotelType);
		return getDailyMultiplierFromSpline(splineFunction);
	}

	private static Map<MonthDay, Double> getDailyMultiplierFromSpline(PolynomialSplineFunction splineFunction) {
		return Stream.of(Month.values())
				.flatMap(month -> IntStream.range(1, month.maxLength() + 1).mapToObj(day -> MonthDay.of(month, day)))
				.collect(Collectors.toMap(
						Function.identity(),
						monthDay -> splineFunction.value(monthDay.getMonth().getValue() + (monthDay.getDayOfMonth() - 1.) / monthDay.getMonth().maxLength()),
						(a, b) -> b,
						HashMap::new));
	}

	private static PolynomialSplineFunction getSplineFunction(HotelType hotelType) {
		Map<Integer, Double> monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(hotelType);

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
		return interpolator.interpolate(x, y);
	}
}
