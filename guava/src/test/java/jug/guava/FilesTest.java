package jug.guava;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class FilesTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void shouldWriteAndCopyTextFile() throws Exception {
		final String content = "yo";
		File file = folder.newFile("someFile.txt");
		File other = folder.newFile("someOther.txt");
		// write content
		PrintWriter writer = new PrintWriter(file);
		writer.println(content);
		writer.close();
		// copy file
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(other);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, length);
		}
		fis.close();
		fos.close();
		// test
		assertThat(other).hasContent(content);
	}

	@Test
	public void shouldWriteAndCopyTextFile_guava() throws Exception {
		final String content = "yo";
		File file = folder.newFile("someFile.txt");
		File other = folder.newFile("someOther.txt");
		// write content
		Files.write(content, file, Charsets.UTF_8);
		// copy file
		Files.copy(file, other);
		// test
		assertThat(other).hasContent(content);
	}
}
