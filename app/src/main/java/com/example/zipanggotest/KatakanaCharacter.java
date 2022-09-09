package com.example.zipanggotest;

import java.util.List;

public class KatakanaCharacter extends Kana {

    public KatakanaCharacter() { }

    public KatakanaCharacter(String character,
                             List<String> transliteration,
                             int strokeCount,
                             List<List<String>> wordList,
                             String mnemonicImg,
                             String mnemonicTxt,
                             List<String> strokeOrder,
                             String audio,
                             SoundType soundType)
    {
        super(
                character,
                transliteration,
                strokeCount,
                wordList,
                mnemonicImg,
                mnemonicTxt,
                strokeOrder,
                audio,
                soundType
        );
    }
}
