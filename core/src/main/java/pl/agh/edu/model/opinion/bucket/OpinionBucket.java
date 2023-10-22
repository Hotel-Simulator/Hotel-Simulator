package pl.agh.edu.model.opinion.bucket;

public abstract class OpinionBucket {
	public final int weight;

	public OpinionBucket(int weight) {
		this.weight = weight;
	}

	public abstract double getValue();
}
