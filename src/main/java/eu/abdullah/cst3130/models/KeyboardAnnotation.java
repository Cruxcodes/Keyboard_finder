package eu.abdullah.cst3130.models;

import jakarta.persistence.*;

/**
 * Represents the Keyboard. Java annotation is used for mappings
 */

@Entity
@Table(name = "keyboard")
public class KeyboardAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "website_url")
    private String website_url;

    @Column(name = "price")
    private float price;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "model")
    private String model;


    /**
     * Empty constructor
     */
    public KeyboardAnnotation() {

    }


    /**
     * The getter and setter implementation for the keyboard table
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
