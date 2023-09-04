package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.model.time.Time;

public class ClientGeneratorTest {

	@Mock
	private Time time;

	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
		time = Time.getInstance();
	}

	@BeforeEach
	public void setUp() throws ReflectiveOperationException {
		changeJSONPath();
	}

	@Test
	public void testGenerateArrivalsForDay() {
		// Create an instance of ClientGenerator
		assertDoesNotThrow(() -> {
			ClientGenerator clientGenerator = ClientGenerator.getInstance();
		});

	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}

}
