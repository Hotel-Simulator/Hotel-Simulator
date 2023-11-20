package pl.agh.edu.engine.client.report.util;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.Pair;

public class DateTrie {
	private final TrieNode root;

	public static void kryoRegister() {
		KryoConfig.kryo.register(DateTrie.class, new Serializer<DateTrie>() {
			@Override
			public void write(Kryo kryo, Output output, DateTrie object) {
				kryo.writeObject(output, object.getData(), KryoConfig.listSerializer(Pair.class));
			}

			@Override
			public DateTrie read(Kryo kryo, Input input, Class<? extends DateTrie> type) {
				return new DateTrie(kryo.readObject(input, List.class, KryoConfig.listSerializer(Pair.class)));
			}
		});
	}

	public DateTrie() {
		this.root = new TrieNode();
	}

	private DateTrie(List<Pair<LocalDate, Integer>> data) {
		this.root = new TrieNode();
		data.forEach(pair -> insert(pair.first(), pair.second()));
	}

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

	private String[] findPrefixes(String input) {
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

	private List<Pair<LocalDate, Integer>> getData() {
		return getDailyData().entrySet().stream()
				.map(entry -> Pair.of(entry.getKey(), entry.getValue()))
				.toList();
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
		private final SortedMap<String, TrieNode> children;
		private int clientGroupNumber;

		public TrieNode() {
			this.children = new TreeMap<>();
			this.clientGroupNumber = 0;
		}
	}
}
