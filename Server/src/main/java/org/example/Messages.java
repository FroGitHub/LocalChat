package org.example;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Messages")
public class Messages implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    private String text_wroten;

    public Messages() {
    }

    public Messages(String author, String message) {
        this.author = author;
        this.text_wroten = message;
    }

    public Messages(Long id, String author, String message) {
        this.id = id;
        this.author = author;
        this.text_wroten = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText_wroten() {
        return text_wroten;
    }

    public void setText_wroten(String text_wroten) {
        this.text_wroten = text_wroten;
    }

    public String toString() {
        return author + " : " + text_wroten;
    }
}
