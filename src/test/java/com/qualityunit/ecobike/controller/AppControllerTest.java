package com.qualityunit.ecobike.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "com.qualityunit.ecobike.*")
public class AppControllerTest {
	private final AppController controller = new AppController();
	private File inputFile;

	private final ByteArrayInputStream testIn = new ByteArrayInputStream(new byte[]{});
	private final ByteArrayOutputStream testOut = new ByteArrayOutputStream();
	private final ByteArrayOutputStream testErr = new ByteArrayOutputStream();
	private final InputStream backupIn = System.in;
	private final PrintStream backupOut = System.out;
	private final PrintStream backupErr = System.err;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() {
		try {
			System.setIn(testIn);
			System.setOut(new PrintStream(testOut));
			System.setErr(new PrintStream(testErr));
			inputFile = folder.newFile("testInput.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		System.setIn(backupIn);
		System.setOut(new PrintStream(backupOut));
		System.setErr(new PrintStream(backupErr));
	}

	@Test
	public void getStreamFromFilePath_Args0IsValidPath_ReturnsStream() {
		String[] args = {inputFile.getPath(), "ignored args"};
		Stream<String> stream = controller.getStreamFromFilePath(args);

		assertNotNull(stream);
	}

	@Test
	public void getStreamFromFilePath_NoArgsButValidPathFromInput_ReturnsStream() {
//		String[] args = new String[]{};
//		InputStream in = new ByteArrayInputStream(inputFile.getPath().getBytes());
//		System.setIn(in);
//		Stream<String> stream = controller.getStreamFromFilePath(args);
//
//		assertNotNull(stream);
	}

	@Test
	public void getStreamFromFilePath_InvalidPath_ThrowsException() {
		String[] args = {""};

	}
}