package jug.junitassertj;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

public class FilesystemTest {

	
	@org.junit.Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@Test
	public void createAFileYo() throws Exception {
		File root = folder.getRoot();
		File yo = new File(root, "yo.somefile");
		yo.createNewFile();
		assertTrue(yo.exists());
	}
	
	@Test
	public void theYoFileShouldNotExistHere() throws Exception {
		File root = folder.getRoot();
		File yo = new File(root, "yo.somefile");
		assertFalse(yo.exists());
	}
	
}
