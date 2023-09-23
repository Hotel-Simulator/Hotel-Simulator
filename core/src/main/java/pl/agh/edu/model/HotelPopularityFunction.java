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
				.flatMap(HotelPopularityFunction::generateMonthDays)
				.collect(Collectors.toMap(
						Function.identity(),
						monthDay -> calculateMultiplier(splineFunction, monthDay),
						(a, b) -> b,
						HashMap::new));
	}

	private static Stream<MonthDay> generateMonthDays(Month month) {
		return IntStream.range(1, month.maxLength() + 1).mapToObj(day -> MonthDay.of(month, day));
	}

	private static double calculateMultiplier(PolynomialSplineFunction splineFunction, MonthDay monthDay) {
		return splineFunction.value(calculateDayFraction(monthDay));
	}

	private static double calculateDayFraction(MonthDay monthDay) {
		int monthValue = monthDay.getMonth().getValue();
		int day = monthDay.getDayOfMonth();
		int monthLength = monthDay.getMonth().maxLength();
		return monthValue + (day - 1.) / monthLength;
	}

	private static PolynomialSplineFunction getSplineFunction(HotelType hotelType) {
		Map<Integer, Double> monthClientsMultiplier = JSONHotelScenariosDataLoader.vacationPopularity.get(hotelType);

		double[] x = generateXValues();
		double[] y = generateYValues(monthClientsMultiplier);

		y[0] = monthClientsMultiplier.get(12);

		IntStream.range(1, 13).forEach(i -> {
			y[i] = monthClientsMultiplier.get(i);
		});

		y[13] = monthClientsMultiplier.get(1);

		return createSplineFunction(x, y);
	}

	private static double[] generateXValues() {
		return IntStream.rangeClosed(0, 13)
				.asDoubleStream()
				.toArray();
	}

	private static double[] generateYValues(Map<Integer, Double> monthClientsMultiplier) {
		return IntStream.rangeClosed(0, 13)
				.mapToDouble(i -> calculateYValue(monthClientsMultiplier, i))
				.toArray();
	}

	private static double calculateYValue(Map<Integer, Double> monthClientsMultiplier, int i) {
		if (i == 0)
			return monthClientsMultiplier.getOrDefault(12, 0.0);
		int month = (i % 13) + 1;
		return monthClientsMultiplier.getOrDefault(month, 0.0);
	}

	private static PolynomialSplineFunction createSplineFunction(double[] x, double[] y) {
		SplineInterpolator interpolator = new SplineInterpolator();
		return interpolator.interpolate(x, y);
	}
}
