package time;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.model.time.Time;

public class TimeTest {

	private Time time;

	@BeforeEach
	public void setUp() {
		time = createNewTimeInstance();
	}

	@Test
	public void testUpdateTime_NoCommands() {
		// Given
		time.start();

		// When
		time.update(time.getInterval() - 1.0F);

		// Then
		assertEquals(1.0F, time.getRemaining());
	}

	@Test
	public void testIncreaseAcceleration() {
		// When
		time.increaseAcceleration();

		// Then
		assertEquals("x2", time.getStringAcceleration());
	}

	@Test
	public void testDecreaseAccelerationMeetMinimum() {
		// When
		time.decreaseAcceleration();

		// Then
		assertEquals("x1", time.getStringAcceleration());
	}

	@Test
	public void testDecreaseAcceleration() {
		// When
		time.increaseAcceleration();

		time.decreaseAcceleration();

		// Then
		assertEquals("x1", time.getStringAcceleration());
	}

	@Test
	public void testStartAndStop() {
		// When
		time.start();
		// Then
		assertTrue(time.isRunning());

		// When
		time.stop();
		// Then
		assertFalse(time.isRunning());
	}

	@Test
	public void testToggle() {
		// Given
		time.start();

		// When
		time.toggle();
		// Then
		assertFalse(time.isRunning());

		// When
		time.toggle();
		// Then
		assertTrue(time.isRunning());
	}

	private Time createNewTimeInstance() {
		try {
			Class<?> clazz = Class.forName("pl.agh.edu.model.time.Time");
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return (Time) constructor.newInstance();
		} catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
				IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create Time instance using reflection", e);
		}
	}
}
