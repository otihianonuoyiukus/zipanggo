package com.example.zipanggotest;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KanaDataXMLParser {
    private final String systemType;
    private List<String> soundTypeArr;

    public KanaDataXMLParser(String systemType, String[] soundTypeArr) {
        this.systemType = systemType;
        this.soundTypeArr = Arrays.asList(soundTypeArr);
    }

    public List<Kana> kanaReadFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        List<Kana> kana = new ArrayList<>();
        parser.next();
        parser.next();
        parser.require(XmlPullParser.START_TAG, null, "kana");
        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            //
            if (name.equals(systemType)) {
                String soundTypeAtt = parser.getAttributeValue(null, "soundType");
                // Checks for the attribute of the entry tag
                //
                if(!soundTypeArr.contains(soundTypeAtt)) {
                    skip(parser);
                    continue;
                }
                kana.add(kanaReadEntry(parser, systemType));
            } else {
                skip(parser);
            }
        }
        return kana;
    }

    // Parses the contents of a kana. If it encounters a tag such as character, transliteration,
    // and etc., hands them off to their respective "read" methods for processing. Otherwise, skips the tag.
    //
    private Kana kanaReadEntry(XmlPullParser parser, String systemType)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, systemType);
        List<String> transliteration = new ArrayList<>(1);
        List<String> strokeOrder = new ArrayList<>(5);
        List<List<String>> wordList = new ArrayList<>(5);
        String character = null,
                mnemonicImg = null,
                mnemonicTxt = null,
                audio = null;
        int strokeCount = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            String name = parser.getName();
            switch (name) {
                case "character":
                    character = readMulti(parser, "character");
                    if (character == null) { return new Kana(); }
                    break;
                case "transliteration":
                    transliteration.add(readMulti(parser, "transliteration"));
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
                case "word":
                    wordList.add(kanaReadWord(parser));
                    break;
                case "mnemonic_img":
                    mnemonicImg = readMulti(parser, "mnemonic_img");
                    break;
                case "mnemonic_txt":
                    mnemonicTxt = readMulti(parser, "mnemonic_txt");
                    break;
                case "stroke_img":
                    strokeOrder.add(readMulti(parser, "stroke_img"));
                    break;
                case "audio":
                    audio = readMulti(parser, "audio");
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Kana(
                character,
                transliteration,
                strokeCount,
                wordList,
                mnemonicImg,
                mnemonicTxt,
                strokeOrder,
                audio,
                Kana.SoundType.GOJUUON
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

    private List<String> kanaReadWord(XmlPullParser parser)
            throws IOException, XmlPullParserException
    {
        parser.require(XmlPullParser.START_TAG, null, "word");

        List<String> wordList = new ArrayList<>(3);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("jp") || name.equals("rom") || name.equals("fil")) {
                wordList.add(readText(parser));
            } else { skip(parser); }
        }
        return wordList;
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
