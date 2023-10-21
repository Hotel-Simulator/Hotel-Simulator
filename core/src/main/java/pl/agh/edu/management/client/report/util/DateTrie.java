package pl.agh.edu.management.client.report.util;//package pl.agh.edu.management.client.report.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DateTrie {
	private final TrieNode root = new TrieNode();

	public void insert(LocalDate date, int customers) {
		TrieNode node = root;
		String[] parts = date.toString().split("-");
		for (int i = 0; i < parts.length; i++) {
			String part = concatenateElements(parts, i);
			TrieNode childNode = node.children.get(part);
			if (childNode == null) {
				childNode = new TrieNode();
				node.children.put(part, childNode);
			}
			node = childNode;
			node.clientGroupNumber += customers;
		}
	}

	private static String concatenateElements(String[] elements, int i) {
		return Arrays.stream(elements, 0, i + 1)
				.collect(Collectors.joining("-"));
	}

	public SortedMap<LocalDate, Integer> getDailyData() {
		return root.children.entrySet().stream()
				.flatMap(entry -> entry.getValue().children.entrySet().stream())
				.flatMap(entry -> entry.getValue().children.entrySet().stream())
				.collect(Collectors.toMap(
						entry -> LocalDate.parse(entry.getKey()),
						entry -> entry.getValue().clientGroupNumber,
						(a, b) -> b,
						TreeMap::new));
	}

	public SortedMap<YearMonth, Integer> getMonthlyData() {
		return root.children.entrySet().stream()
				.flatMap(entry -> entry.getValue().children.entrySet().stream())
				.collect(Collectors.toMap(
						entry -> YearMonth.parse(entry.getKey()),
						entry -> entry.getValue().clientGroupNumber,
						(a, b) -> b,
						TreeMap::new));
	}

	public SortedMap<Year, Integer> getYearlyData() {
		return root.children.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> Year.parse(entry.getKey()),
						entry -> entry.getValue().clientGroupNumber,
						(a, b) -> b,
						TreeMap::new));
	}

	private static class TrieNode {
		SortedMap<String, TrieNode> children;
		int clientGroupNumber;

		public TrieNode() {
			this.children = new TreeMap<>();
			this.clientGroupNumber = 0;
		}
	}
}
