package com.rabbit.services;

/*import com.fasterxml.uuid.Generators;*/
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;

@Service
public class UUIDGenerator {
	private static RandomBasedGenerator uuidGen = Generators
			.randomBasedGenerator();

	public static String createAsString() {
		UUID uuid = uuidGen.generate();
		if (uuid != null) {
			return uuid.toString();
		}
		return null;
	}

	public static UUID create() {
		return uuidGen.generate();
	}
}
