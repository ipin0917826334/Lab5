package com.example.lab5;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@PageTitle("MyView2")
@Route(value = "index2")
public class MyView2 extends HorizontalLayout {
    private TextField addWord, addSence;
    private TextArea showGoodSence, showBadSence;
    private Button btnBadWord, btnGoodWord, btnAddSentence, btnShowSentence;
    protected Word words = new Word();
    public MyView2(){
        addWord = new TextField("Add Word");
        addSence = new TextField("Add Sentence");
        showGoodSence = new TextArea("Good Sentences");
        showBadSence = new TextArea("Bad Sentences");
        btnGoodWord = new Button("Add Good Word");
        btnBadWord = new Button("Add Bad Word");
        btnAddSentence = new Button("Add Sentence");
        btnShowSentence = new Button("Show Sentence");
        ComboBox<String> goodWordBox = new ComboBox<>("Good Words");
        ComboBox<String> badWordBox = new ComboBox<>("Bad Words");
        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();
//        System.out.println(words.goodWords);
        goodWordBox.setItems(words.goodWords);
        badWordBox.setItems(words.badWords);

        v1.setAlignItems(FlexComponent.Alignment.STRETCH);
        v2.setAlignItems(FlexComponent.Alignment.STRETCH);

        v1.add(addWord, btnGoodWord, btnBadWord, goodWordBox, badWordBox);
        v2.add(addSence, btnAddSentence, showGoodSence, showBadSence, btnShowSentence);
        add(v1,v2);
        btnGoodWord.addClickListener(event -> {
            String word = addWord.getValue();
            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGood/"+word)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            ArrayList<String> temp = new ArrayList<>();

            for(Object text : out){
                temp.add((String) text); //add ที return มา
            }
            goodWordBox.setItems(out); //set item ออกไป
            Notification notification = Notification.show("Insert " + word + " to Good Word Lists Complete");
        });
        btnBadWord.addClickListener(event -> {
            String word = addWord.getValue();
            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBad/"+word)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();

            ArrayList<String> temp = new ArrayList<>();

            for(Object text : out){
                temp.add((String) text); //add ที return มา
            }
            badWordBox.setItems(out); //set item ออกไป
            Notification notification = Notification.show("Insert " + word + " to Bad Word Lists Complete");

        });
        btnAddSentence.addClickListener(event -> {
            String sentence = addSence.getValue();
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proof/"+sentence)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Notification notification = Notification.show(out);
        });
        btnShowSentence.addClickListener(event -> {
            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
//            System.out.println(out.badSentences);
            showBadSence.setValue(String.valueOf(out.badSentences));
            showGoodSence.setValue(String.valueOf(out.goodSentences));
        });
    }
}
