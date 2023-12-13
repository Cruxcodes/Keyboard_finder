package eu.abdullah.cst3130.models;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Represents the Keyboard model
 */

@Entity
@Table(name = "keyboard")
public class KeyboardAnnotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "brand")
    private String brand;

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
