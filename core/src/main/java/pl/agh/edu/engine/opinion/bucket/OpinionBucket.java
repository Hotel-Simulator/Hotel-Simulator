package pl.agh.edu.engine.opinion.bucket;

import java.util.Optional;

public abstract class OpinionBucket {
	public final int weight;

	public OpinionBucket(int weight) {
		this.weight = weight;
	}

	public abstract double getValue();

	public abstract Optional<String> getComment();
}
