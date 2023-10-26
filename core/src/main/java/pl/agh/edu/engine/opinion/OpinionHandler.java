package pl.agh.edu.engine.opinion;

import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.engine.client.Client;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private static final List<OpinionData> opinions = new ArrayList<>();
	private static final Time time = Time.getInstance();

	public static void addOpinionWithProbability(ClientGroup clientGroup, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			OpinionData opinionData = new OpinionData(RandomUtils.randomListElement(clientGroup.getMembers()).name(),time.getTime().toLocalDate(), clientGroup.opinion.getStars(), clientGroup.opinion.getComment().stream().map(LanguageString::new).toList());
			System.out.println(opinionData);
			opinions.add(opinionData);
		}

	}
}
