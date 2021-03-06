package com.qualityunit.ecobike.controller;

import com.qualityunit.ecobike.view.Menu;
import com.qualityunit.ecobike.view.UserInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.out;

public class AppController {
	private final UserInput userInput;
	private Path inputFilePath;
	private static final String FILE_PATH_PROMPT = "Please enter the input file path:";
	private static final String OPEN_ERROR_MSG = "Can't open a file at this path: '%s'";

	public AppController(UserInput userInput) {
		this.userInput = userInput;
	}

	/*
	 * Tries to get an input file path from command line arguments. If there is none or
	 * it's invalid then get the path from user's input. Loop until the stream is obtained from file.
	 */
	public Stream<String> getStreamFromFilePath(String[] args) {
		String pathStr = (args.length > 0) ? args[0] : userInput.getLine(FILE_PATH_PROMPT);
		Stream<String> stream = null;
		do {
			try {
				inputFilePath = Paths.get(pathStr).toRealPath();
				stream = Files.lines(inputFilePath);
			} catch (InvalidPathException | IOException | SecurityException e) {
				err.println(format(OPEN_ERROR_MSG, pathStr));
				pathStr = userInput.getLine(FILE_PATH_PROMPT);
			}
		} while (stream == null);
		out.println("Reading the file: " + inputFilePath);
		return stream;
	}

	public void runMenu() {
		Menu menu = new Menu(inputFilePath, userInput);
		while (true) {
			menu.getCommandFromUser().execute();
		}
	}
}
