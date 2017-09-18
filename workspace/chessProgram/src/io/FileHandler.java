package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;

/**
 * Handles interactions with files
 * @author matthewslesinski
 */
public class FileHandler {

	/** The default directory to save files in or retrieve files from */
	private static final File DEFAULT_DIRECTORY = new File(".");
	
	/**
	 * Retrieves the lines of strings of a file
	 * @param filepath The path to the file
	 * @return The list of strings representing the lines
	 */
	public static List<String> getLinesFromFile(String filepath) {
		File file = openFile(filepath);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			return reader.lines().collect(Collectors.toList());
		} catch (IOException e) {
			throw new BadArgumentException(filepath, File.class, "Can't read this file");
		}
	}
	
	/**
	 * Writes a string to a file
	 * @param sequence The string to write
	 * @param filepath The path to the file
	 */
	public static void writeToFile(String sequence, String filepath) {
		File file = openFile(filepath);
		try (OutputStream out = new FileOutputStream(file)) {
		} catch (IOException e) {
			throw new BadArgumentException(filepath, File.class, "Can't write to this file");
		}
	}
	
	/**
	 * Opens a file
	 * @param filepath The path to the file
	 * @return The {@code File}
	 */
	private static File openFile(String filepath) {
		return new File(DEFAULT_DIRECTORY, filepath);
	}
}
