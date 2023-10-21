package pl.agh.edu.management.client.report.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DateTrie {
	private final TrieNode root = new TrieNode();

	public void insert(LocalDate date, int customers) {
		TrieNode node = root;
		String[] parts = findPrefixes(date.toString());
		for (String part : parts) {
			TrieNode childNode = node.children.get(part);
			if (childNode == null) {
				childNode = new TrieNode();
				node.children.put(part, childNode);
			}
			node = childNode;
			node.clientGroupNumber += customers;
		}
	}

	public static String[] findPrefixes(String input) {
		String[] words = input.split("-");
		String[] prefixes = new String[words.length];
		for (int i = 0; i < words.length; i++) {
			if (i == 0) {
				prefixes[i] = words[i];
			} else {
				prefixes[i] = prefixes[i - 1] + "-" + words[i];
			}
		}
		return prefixes;
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
