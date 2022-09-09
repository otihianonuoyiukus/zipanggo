package com.example.zipanggotest;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KanjiDataXMLParser {
    private int jlptLevel;
    private List<String> kanjiCharList;
    private String category;

    public KanjiDataXMLParser(int jlptLevel) {
        this.jlptLevel = jlptLevel;
    }

    public KanjiDataXMLParser(int jlptLevel, String category) {
        this.jlptLevel = jlptLevel;
        this.category = category;
    }

    public KanjiDataXMLParser(List<String> kanjiCharList) {
        this.kanjiCharList = kanjiCharList;
    }

    public List<Kanji> kanjiReadFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        List<Kanji> kanji = new ArrayList<>();
        parser.next();
        parser.next();
        parser.require(XmlPullParser.START_TAG, null, "han");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            //
            if (name.equals("kanji")) {
                String jlptLevelAtt = parser.getAttributeValue(null, "jlpt");
                // Checks for the attribute of the entry tag
                //
                if(jlptLevel != Integer.parseInt(jlptLevelAtt)) {
                    skip(parser);
                    continue;
                }
                kanji.add(kanjiReadEntry(parser));
            } else {
                skip(parser);
            }
        }
        return kanji;
    }

    public List<Kanji> kanjiReadFeedwCategory(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        List<Kanji> kanji = new ArrayList<>();
        parser.next();
        parser.next();
        parser.require(XmlPullParser.START_TAG, null, "han");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            //
            if (name.equals("kanji")) {
                String jlptLevelAtt = parser.getAttributeValue(null, "jlpt");
                String categoryAtt = parser.getAttributeValue(null, "category");
                // Checks for the attribute of the entry tag
                //
                if(jlptLevel != Integer.parseInt(jlptLevelAtt) ||
                        !category.equalsIgnoreCase(categoryAtt))
                {
                    skip(parser);
                    continue;
                }
                kanji.add(kanjiReadEntry(parser));
            } else {
                skip(parser);
            }
        }
        return kanji;
    }

    public List<Kanji> kanjiReadFeedwCharacter(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        List<Kanji> kanji = new ArrayList<>();
        parser.next();
        parser.next();
        parser.require(XmlPullParser.START_TAG, null, "han");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            //
            if (name.equals("kanji")) {
                String characterStrAtt = parser.getAttributeValue(null, "character");
                // Checks for the attribute of the entry tag
                //
                if(!kanjiCharList.contains(characterStrAtt)) {
                    skip(parser);
                    continue;
                }
                kanji.add(kanjiReadEntry(parser));
            } else {
                skip(parser);
            }
        }
        return kanji;
    }

    // Parses the contents of a kanji. If it encounters a tag such as character, transliteration,
    // and etc., hands them off to their respective "read" methods for processing. Otherwise, skips the tag.
    //
    private Kanji kanjiReadEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "kanji");
        int strokeCount = 0;
        String character = "", kunyomi = "", onyomi = "", radical = "", part = "",
                variant = "", mnemonicImg = "", mnemonicTxt = "";
        List<String> meaningList = new ArrayList<>(5);
        List<String> strokeOrderList = new ArrayList<>(7);
        List<List<String>> vocabularyList = new ArrayList<>(5);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            String name = parser.getName();
            switch (name) {
                case "character":
                    character = readMulti(parser, "character");
                    if (character == null) { return new Kanji(); }
                    break;
                case "stroke_count":
                    try {
                        strokeCount = Integer.parseInt(readMulti(parser, "stroke_count"));
                    } catch (NumberFormatException e) {
                        Log.e(
                                "NumberFormatException",
                                "There's a problem with the number format here!"
                        );
                    }
                    break;
                case "meaning":
                    meaningList = kanjiReadMeaning(parser);
                    break;
                case "vocabulary":
                    vocabularyList = kanjiReadVocabulary(parser);
                    break;
                case "kunyomi":
                    kunyomi = kanjiReadReading(parser, "kunyomi");
                    break;
                case "onyomi":
                    onyomi = kanjiReadReading(parser, "onyomi");
                    break;
                case "radical":
                    radical = kanjiReadExtra(parser, "radical");
                    break;
                case "part":
                    part = kanjiReadExtra(parser, "part");
                    break;
                case "variant":
                    variant = kanjiReadExtra(parser, "variant");
                    break;
                case "mnemonic_img":
                    mnemonicImg = readMulti(parser, "mnemonic_img");
                    break;
                case "mnemonic_txt":
                    mnemonicTxt = readMulti(parser, "mnemonic_txt");
                    break;
                case "stroke_img":
                    strokeOrderList = kanjiReadStrokeOrder(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Kanji(
                jlptLevel,
                character,
                strokeCount,
                meaningList,
                vocabularyList,
                kunyomi,
                onyomi,
                radical,
                part,
                variant,
                mnemonicImg,
                mnemonicTxt,
                strokeOrderList
        );
    }

    private String readMulti(XmlPullParser parser, String name)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, name);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, name);
        return title;
    }

    private List<String> kanjiReadMeaning(XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "meaning");

        List<String> wordList = new ArrayList<>(3);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("word")) {
                wordList.add(readText(parser));
            } else { skip(parser); }
        }
        return wordList;
    }

    private List<List<String>> kanjiReadVocabulary(XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "vocabulary");

        List<List<String>> vocabularyList = new ArrayList<>(5);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("word")) {
                parser.require(XmlPullParser.START_TAG, null, "word");
                List<String> wordList = new ArrayList<>(3);
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
                    name = parser.getName();
                    if (name.equalsIgnoreCase("kanji_word") ||
                            name.equalsIgnoreCase("hiragana") ||
                            name.equalsIgnoreCase("fil"))
                    {
                        wordList.add(readText(parser));
                    } else { skip(parser); }
                }
                vocabularyList.add(wordList);
            } else { skip(parser); }
        }
        return vocabularyList;
    }

    private String kanjiReadReading(XmlPullParser parser, String tagName)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        String reading = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equalsIgnoreCase("reading")) {
                reading = readText(parser);
            } else { skip(parser); }
        }
        return reading;
    }

    private String kanjiReadExtra(XmlPullParser parser, String tagName)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, tagName);

        String extra = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equalsIgnoreCase(tagName + "_character")) {
                extra = readText(parser);
            } else { skip(parser); }
        }
        return extra;
    }

    private List<String> kanjiReadStrokeOrder(XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "stroke_img");

        List<String> strokeOrderList = new ArrayList<>(7);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equalsIgnoreCase("img")) {
                strokeOrderList.add(readText(parser));
            } else { skip(parser); }
        }
        return strokeOrderList;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
