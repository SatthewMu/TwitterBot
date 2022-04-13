package org.cis120;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Tests for TwitterBot class */
public class TwitterBotTest {

    /*
     * This tests whether your TwitterBot class itself is written correctly
     *
     * This test operates very similarly to our MarkovChain tests in its use of
     * `fixDistribution`, so make sure you know how to test MarkovChain before
     * testing this!
     */
    @Test
    public void simpleTwitterBotTest() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", "comes", "from", "data", "with", "no", "duplicate", "words", ".",
                        "the", "end", "should", "come", "."
                )
        );
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);
        t.fixDistribution(desiredTweet);

        String expected = "this comes from data with no duplicate words. the end should come.";
        String actual = TweetParser.replacePunctuation(t.generateTweet(12));
        assertEquals(expected, actual);
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void singleWordTwitterBotTest() {
        String words = "0, hi";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);

        assertEquals("hi. hi. hi.", TweetParser.replacePunctuation(t.generateTweet(3)));
    }

    @Test
    public void testWriteStringsToFile() throws IOException {
        String path = "files/test.csv";
        File f = new File(path);
        f.createNewFile();
        List<String> strings = new ArrayList<>(
                Arrays.asList(
                        "a", "b", "c", "d"
                )
        );
        BufferedReader br = new BufferedReader((new FileReader(path)));
        TwitterBot t = new TwitterBot(br, 1);
        t.writeStringsToFile(strings, path, false);
        FileLineIterator fl = new FileLineIterator(new BufferedReader(new FileReader(f)));
        assertEquals("a", fl.next());
        assertEquals("b", fl.next());
        assertEquals("c", fl.next());
        assertEquals("d", fl.next());
    }

    @Test
    public void testAppendStringsToFile() throws IOException {
        String path = "files/test1.csv";
        File f = new File(path);
        f.createNewFile();
        List<String> desiredStrings = new ArrayList<>(
                Arrays.asList(
                        "c", "d"
                )
        );
        FileWriter fw = new FileWriter(f);
        fw.write("a\nb\n");
        fw.close();
        BufferedReader br = new BufferedReader((new FileReader(path)));
        TwitterBot t = new TwitterBot(br, 1);
        t.writeStringsToFile(desiredStrings, path, true);
        FileLineIterator fl = new FileLineIterator(new BufferedReader(new FileReader(f)));
        assertEquals("a", fl.next());
        assertEquals("b", fl.next());
        assertEquals("c", fl.next());
        assertEquals("d", fl.next());
    }

    @Test
    public void testNegativeWords() {
        List<String> desiredTweet = new ArrayList<>(
                Arrays.asList(
                        "this", "comes", "from", "data", "with", "no", "duplicate", "words", ".",
                        "the", "end", "should", "come", "."
                )
        );
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);
        t.fixDistribution(desiredTweet);

        assertThrows(IllegalArgumentException.class, () ->
                TweetParser.replacePunctuation(t.generateTweet(-1)));
    }

    @Test
    public void testNoSentence() {
        String words = "";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        TwitterBot t = new TwitterBot(br, 1);

        assertEquals("", TweetParser.replacePunctuation(t.generateTweet(1)));
    }

}
