package model.opinion.bucket;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.agh.edu.model.opinion.bucket.EmployeesSatisfactionOpinionBucket;

public class EmployeesSatisfactionOpinionBucketTest {
	private EmployeesSatisfactionOpinionBucket employeesSatisfaction;

	@BeforeEach
	public void setUp() {
		employeesSatisfaction = new EmployeesSatisfactionOpinionBucket(3);
	}

	@Test
	public void getValueTest() {
		// Given
		employeesSatisfaction.addSatisfaction(new BigDecimal("4.5"));
		employeesSatisfaction.addSatisfaction(new BigDecimal("3.5"));
		employeesSatisfaction.addSatisfaction(new BigDecimal("5.0"));

		// When
		double expectedValue = (4.5 + 3.5 + 5.0) / 3;
		double actualValue = employeesSatisfaction.getValue();

		// Then
		assertEquals(expectedValue, actualValue, 0.001);
	}

	@Test
	public void testGetValueEmptyBucket() {
		double actualValue = employeesSatisfaction.getValue();

		assertEquals(0.0, actualValue, 0.001);
	}
}
