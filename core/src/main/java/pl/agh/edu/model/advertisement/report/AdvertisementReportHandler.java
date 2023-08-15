package pl.agh.edu.model.advertisement.report;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AdvertisementReportHandler {
	private static final List<AdvertisementReportData> data = new ArrayList<>();

	public static void collectData(AdvertisementReportData advertisementReportData) {
		data.add(advertisementReportData);
	}

	public static List<AdvertisementReportData> getData() {
		return data.stream().toList();
	}

	public static List<AdvertisementReportData> getData(LocalDate startDate) {
		return data.stream()
				.dropWhile(advertisementReportData -> advertisementReportData.date().isBefore(startDate))
				.collect(Collectors.toList());
	}

	public static List<AdvertisementReportData> getData(LocalDate startDate, LocalDate endDate) {
		return data.stream()
				.dropWhile(advertisementReportData -> advertisementReportData.date().isBefore(startDate))
				.takeWhile(advertisementReportData -> advertisementReportData.date().isBefore(endDate))
				.collect(Collectors.toList());
	}

	public static LocalDate getMinDate() {
		if (data.isEmpty())
			throw new IllegalStateException("No data available!");
		return data.get(0).date();
	}

	public static LocalDate getMaxDate() {
		if (data.isEmpty())
			throw new IllegalStateException("No data available!");
		return data.get(data.size() - 1).date();
	}
}
