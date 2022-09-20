package com.example.lab5;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    protected Word words = new Word();
    protected SentenceConsumer sentence = new SentenceConsumer();
    public ArrayList<String> arr;
    public ArrayList<String> arr1;
    public SentenceConsumer sentenceCon = new SentenceConsumer();
    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
        words.badWords.add(s);
//        rabbitTemplate.convertAndSend("Direct","BadWordQueue",s);
        return words.badWords;
    }

    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
        words.goodWords.add(s);
//        rabbitTemplate.convertAndSend("Direct","GoodWordQueue",s);
        return words.goodWords;
    }

    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s) {
        words.badWords.remove(s);
//        rabbitTemplate.convertAndSend("Direct","GoodWordQueue",s);
        return words.badWords;
    }

    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET)
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s) {
        words.goodWords.remove(s);
//        rabbitTemplate.convertAndSend("Direct","GoodWordQueue",s);
        return words.goodWords;
    }

    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.GET)
    public String proofSentence(@PathVariable("sentence") String s) {
        if (words.goodWords.size() >= words.badWords.size()){
            arr = words.goodWords;
            arr1 = words.badWords;
        }
        else{
            arr = words.badWords;
            arr1 = words.goodWords;
        }
        for (int i = 0; i < arr.size(); i++) {
            if (s.contains(arr.get(i))) {
                for (int j = 0; j < arr1.size(); j++) {
                    if (s.contains(arr1.get(j))) {
                        rabbitTemplate.convertAndSend("Fanout", "", s);
                        return "Found Bad & Good Word";
                    }
                }
            }
        }
        for (int i = 0; i < words.goodWords.size(); i++) {
            if (s.contains(words.goodWords.get(i))) {
                rabbitTemplate.convertAndSend("Direct", "good", s);
                return "Found Good Word";
            }
        }
        for (int i = 0; i < words.badWords.size(); i++) {
            if (s.contains(words.badWords.get(i))) {
                rabbitTemplate.convertAndSend("Direct", "bad", s);
                return "Found Bad Word";
            }
        }
        return s;
    }
    //EX2
//    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.POST)
//    public ArrayList<String> addBadWord(@PathVariable("word") String s) {
//        words.badWords.add(s);
////        rabbitTemplate.convertAndSend("Direct","BadWordQueue",s);
//        return words.badWords;
//    }
//
//    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.POST)
//    public ArrayList<String> addGoodWord(@PathVariable("word") String s) {
//        words.goodWords.add(s);
////        rabbitTemplate.convertAndSend("Direct","GoodWordQueue",s);
//        return words.goodWords;
//    }
//
//
//    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.POST)
//    public String proofSentence(@PathVariable("sentence") String s) {
//        if (words.goodWords.size() >= words.badWords.size()){
//            arr = words.goodWords;
//            arr1 = words.badWords;
//        }
//        else{
//            arr = words.badWords;
//            arr1 = words.goodWords;
//        }
//        for (int i = 0; i < arr.size(); i++) {
//            if (s.contains(arr.get(i))) {
//                for (int j = 0; j < arr1.size(); j++) {
//                    if (s.contains(arr1.get(j))) {
//                        rabbitTemplate.convertAndSend("Fanout", "", s);
//                        return "Found Bad & Good Word";
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < words.goodWords.size(); i++) {
//            if (s.contains(words.goodWords.get(i))) {
//                rabbitTemplate.convertAndSend("Direct", "good", s);
//                return "Found Good Word";
//            }
//        }
//        for (int i = 0; i < words.badWords.size(); i++) {
//            if (s.contains(words.badWords.get(i))) {
//                rabbitTemplate.convertAndSend("Direct", "bad", s);
//                return "Found Bad Word";
//            }
//        }
//        return s;
//    }
    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence() {
        Object sen = rabbitTemplate.convertSendAndReceive( "Direct", "get", "");
        return (Sentence) sen;
    }
}
