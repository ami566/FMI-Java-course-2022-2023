package bg.sofia.uni.fmi.mjt.markdown;

import bg.sofia.uni.fmi.mjt.markdown.tags.Tag;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MarkdownConverterTest {

    @TempDir
    static Path tempDir;
    @TempDir
    static Path tempDir2;

    MarkdownConverter c = new MarkdownConverter();
    Path path1, path2, path3;
    File f1, f2, f3;

    @BeforeEach
    public void setUp() {
        path1 = tempDir.resolve("testFile1.md");
        path2 = tempDir.resolve("testFile2.md");
        path3 = tempDir.resolve("testFile3.html");

        f1 = path1.toFile();
        f2 = path2.toFile();
        f3 = path3.toFile();
    }

    @Test
    void testWriteTagsWithTextProperCode() {
        MarkdownConverter c = new MarkdownConverter();
        Pair p = new Pair("<code>.close()</code>", 7);
        assertEquals(p, c.writeTagsWithText(Tag.CODE_TAG, Tag.CODE_CLOSING_TAG, ".close()`", new Pair("", 0)),
                "Tags and text do not match");
    }

    @Test
    void testWriteTaggedTextBold() {
        MarkdownConverter c = new MarkdownConverter();
        Pair p = new Pair("<strong>bold text</strong>", 12);
        assertEquals(p, c.writeTaggedText("**bold text**", new Pair("", 0)),
                "Tags and text do not match");
    }

    @Test
    void testConvertLineToHtmlCode() {
        MarkdownConverter c = new MarkdownConverter();

        assertEquals("A proper <code>code</code> content", c.convertLineToHtml("A proper `code` content"),
                "Wrong conversion");
    }

    @Test
    void testConvertLineToHtmlHeaderOne() {

        assertEquals("<h1>Heading level 1</h1>", c.convertLineToHtml("# Heading level 1"),
                "Wrong conversion");
    }

    @Test
    void testConvertLineToHtmlBoldAndEm() {

        assertEquals("I love <em>your</em> eyes", c.convertLineToHtml("I love *your* eyes"),
                "Wrong conversion");
    }

    @Test
    void testCheckForUniqueSymbolWithSharpSign() {
        MarkdownConverter c = new MarkdownConverter();
        assertTrue(c.checkForSpecialSymbol('#'), "# is a unique symbol");
    }

    @Test
    void testConvertMarkdownPathToPathCorrect() {
        String s = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H4_TAG.text + "title" + Tag.H4_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();
        try {
            FileWriter fileWriter = new FileWriter(f1);
            fileWriter.append("#### title");
            fileWriter.flush();
            fileWriter.close();

            c.convertMarkdown(path1, path3);
            String res = Files.readString(path3);
            assertEquals(s, res, "File path to path conversion does not work ");
        } catch (IOException e) {
        }
    }

    @Test
    void testConvertMarkdownReaderToWriterCorrect() {
        String s = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H3_TAG.text + "title" + Tag.H3_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();

        Writer writer = new StringWriter();
        Reader reader = new StringReader("### title");

        c.convertMarkdown(reader, writer);
        assertEquals(s, writer.toString(), "File path to path conversion does not work ");

    }

    @Test
    void testConvertMarkdownDirToSameDir() {
        String s = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H5_TAG.text + "title" + Tag.H5_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();

        String s2 = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H6_TAG.text + "title" + Tag.H6_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();
        try {
            FileWriter fileWriter = new FileWriter(f1);
            fileWriter.append("##### title");
            fileWriter.flush();
            fileWriter.close();

            FileWriter fileWriter1 = new FileWriter(f2);
            fileWriter1.append("###### title");
            fileWriter1.flush();
            fileWriter1.close();

            c.convertAllMarkdownFiles(tempDir, tempDir);

            String res1 = Files.readString(Path.of(tempDir + File.separator + "testFile1.html"));
            String res2 = Files.readString(Path.of(tempDir + File.separator + "testFile2.html"));
            assertEquals(s, res1, "File path to path conversion does not work ");
            assertEquals(s2, res2, "File path to path conversion does not work ");
        } catch (IOException e) {
        }
    }

    @Test
    void testConvertMarkdownDirToDiffDir() {
        String s = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H1_TAG.text + "title" + Tag.H1_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();

        String s2 = Tag.HTML_TAG.text + System.lineSeparator() + Tag.BODY_TAG.text + System.lineSeparator() +
                Tag.H2_TAG.text + "title" + Tag.H2_CLOSING_TAG.text + System.lineSeparator() + Tag.BODY_CLOSING_TAG.text + System.lineSeparator() +
                Tag.HTML_CLOSING_TAG.text + System.lineSeparator();
        try {
            FileWriter fileWriter = new FileWriter(f1);
            fileWriter.append("# title");
            fileWriter.flush();
            fileWriter.close();

            FileWriter fileWriter1 = new FileWriter(f2);
            fileWriter1.append("## title");
            fileWriter1.flush();
            fileWriter1.close();

            c.convertAllMarkdownFiles(tempDir, tempDir2);

            String res1 = Files.readString(Path.of(tempDir2 + File.separator + "testFile1.html"));
            String res2 = Files.readString(Path.of(tempDir2 + File.separator + "testFile2.html"));
            assertEquals(s, res1, "File path to path conversion does not work ");
            assertEquals(s2, res2, "File path to path conversion does not work ");
        } catch (IOException e) {
        }

    }
}
