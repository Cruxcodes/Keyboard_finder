package eu.abdullah.cst3130.models;

import jakarta.persistence.*;

import java.io.Serializable;


/**
 * Annotation for the keyboard_details table
 */
@Entity
@Table(name = "keyboard_details")
public class KeyboardDetailsAnnotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "image")
    private String image;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "keyboard_id")
    private int keyboardId;

    @Column(name = "color")
    private String color;


    /**
     * Empty constructor
     */
    public KeyboardDetailsAnnotation() {

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getKeyboardId() {
        return keyboardId;
    }

    public void setKeyboardId(int keyboardId) {
        this.keyboardId = keyboardId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
