package com.example.lab5;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable{
    public ArrayList<String> badWords = new ArrayList<>();
    public ArrayList<String> goodWords = new ArrayList<>();

    public Word() {
        badWords.add("fuck");
        badWords.add("olo");
        goodWords.add("happy");
        goodWords.add("enjoy");
        goodWords.add("life");
    }
}
