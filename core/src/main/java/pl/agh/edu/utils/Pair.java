package pl.agh.edu.utils;

public class Pair<A, B> {
	private final A first;
	private final B second;

	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return first;
	}

	public B getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Pair<?, ?> pair = (Pair<?, ?>) o;
		if (!first.equals(pair.first))
			return false;
		return second.equals(pair.second);
	}

	@Override
	public int hashCode() {
		int result = first.hashCode();
		result = 31 * result + second.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}
}
