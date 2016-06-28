package services.common;

import java.util.UUID;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;

/**
 * Standardize UUID generation.
 * 
 * @author kmtong
 *
 */
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
