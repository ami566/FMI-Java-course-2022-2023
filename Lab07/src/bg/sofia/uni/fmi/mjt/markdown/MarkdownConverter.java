package bg.sofia.uni.fmi.mjt.markdown;

import bg.sofia.uni.fmi.mjt.markdown.tags.Tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Pair {
    public String currentConvertedLine;
    public int counter;

    Pair(String s, int c) {
        currentConvertedLine = s;
        counter = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return counter == pair.counter && Objects.equals(currentConvertedLine, pair.currentConvertedLine);
    }
}

public class MarkdownConverter implements MarkdownConverterAPI {

    private List<String> list;
    // private String currentConvertedLine;
    private final String hashSign = "#";
    private final String backtick = "`";
    private final String star = "*";

    private final int three = 3;
    private final int four = 4;
    private final int five = 5;
    private final int six = 6;

    private void initializeList() {
        if (list != null) {
            list.clear();
        }
        list = new ArrayList<>();
        list.add(Tag.HTML_TAG.text);
        list.add(Tag.BODY_TAG.text);
    }

    private void addEndTagLinesToList() {
        list.add(Tag.BODY_CLOSING_TAG.text);
        list.add(Tag.HTML_CLOSING_TAG.text);
    }

    public boolean checkForSpecialSymbol(char a) {
        return checkForStarSymbol(a) || checkForHashSymbol(a) || checkForBacktickSymbol(a);
    }

    private boolean checkForStarSymbol(char a) {
        return a == star.charAt(0);
    }

    private boolean checkForHashSymbol(char a) {
        return a == hashSign.charAt(0);
    }

    private boolean checkForBacktickSymbol(char a) {
        return a == backtick.charAt(0);
    }

    private Pair writeTextBetweenTags(String str, Pair p) {
        while (p.counter < str.length() && !checkForSpecialSymbol(str.charAt(p.counter))) {
            p.currentConvertedLine += str.charAt(p.counter++);
        }
        return p;
    }

    public Pair writeTagsWithText(Tag openTag, Tag endTag, String str, Pair p) {
        p.currentConvertedLine += openTag.text;
        p = writeTextBetweenTags(str, p);
        p.currentConvertedLine += endTag.text;
        p.counter--;
        return p;
    }

    public Pair writeTaggedText(String str, Pair p) {
        String symbols = "";

        while (checkForSpecialSymbol(str.charAt(p.counter))) {
            symbols += str.charAt(p.counter++);
        }

        // p.counter--;
        if (checkForBacktickSymbol(symbols.charAt(0))) {
            p = writeTagsWithText(Tag.CODE_TAG, Tag.CODE_CLOSING_TAG, str, p);
        } else if (checkForStarSymbol(symbols.charAt(0))) {
            if (symbols.length() == 1) {
                p = writeTagsWithText(Tag.EM_TAG, Tag.EM_CLOSING_TAG, str, p);
            } else {
                p = writeTagsWithText(Tag.BOLD_TAG, Tag.BOLD_CLOSING_TAG, str, p);
            }
        } else {
            p.counter++;
            p = switch (symbols.length()) {
                case 1 -> writeTagsWithText(Tag.H1_TAG, Tag.H1_CLOSING_TAG, str, p);
                case 2 -> writeTagsWithText(Tag.H2_TAG, Tag.H2_CLOSING_TAG, str, p);
                case three -> writeTagsWithText(Tag.H3_TAG, Tag.H3_CLOSING_TAG, str, p);
                case four -> writeTagsWithText(Tag.H4_TAG, Tag.H4_CLOSING_TAG, str, p);
                case five -> writeTagsWithText(Tag.H5_TAG, Tag.H5_CLOSING_TAG, str, p);
                case six -> writeTagsWithText(Tag.H6_TAG, Tag.H6_CLOSING_TAG, str, p);
                default -> null;
            };
            return p;
        }
        p.counter += symbols.length(); // to count the closing symbols: '`' and '*'
        return p;
    }

    public String convertLineToHtml(String str) {
        // String currentConvertedLine = "";
        Pair p = new Pair("", 0);

        //int counter = 0;
        char currChar;
        while (p.counter < str.length()) {
            currChar = str.charAt(p.counter);
            if (checkForSpecialSymbol(currChar)) {
                p = writeTaggedText(str, p);
                // continue;
            } else {
                p.currentConvertedLine += currChar;
            }
            p.counter++;
        }

        return p.currentConvertedLine;
    }

    @Override
    public void convertMarkdown(Reader source, Writer output) {

        initializeList();

        try (var in = new BufferedReader(source)) {
            String line;
            while ((line = in.readLine()) != null) {
                list.add(convertLineToHtml(line));
            }
            addEndTagLinesToList();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }

        try (var out = new BufferedWriter(output)) {
            for (String s : list) {
                out.write(s + System.lineSeparator());
            }
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }


    @Override
    public void convertMarkdown(Path from, Path to) {
        initializeList();
        try (var in = Files.newBufferedReader(from)) {
            String line;
            while ((line = in.readLine()) != null) {
                list.add(convertLineToHtml(line));
            }
            addEndTagLinesToList();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while reading from a file", e);
        }

        try (var out = Files.newBufferedWriter(to)) {
            for (String s : list) {
                out.write(s + System.lineSeparator());
            }
            out.flush();
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }

    }

    @Override
    public void convertAllMarkdownFiles(Path sourceDir, Path targetDir) {

        File sourceF = new File(sourceDir.toUri());
        File targetF = new File(targetDir.toUri());

        for (File f : sourceF.listFiles()) {

            if (f.getPath().contains(".md")) {
                Path output = Path.of(targetDir + File.separator +
                        f.getName().substring(0, f.getName().length() - 2) + "html");
                convertMarkdown(f.toPath(), output);
            }
        }
    }
}
