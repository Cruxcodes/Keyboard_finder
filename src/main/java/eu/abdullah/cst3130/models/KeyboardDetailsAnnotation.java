package eu.abdullah.cst3130.models;

import jakarta.persistence.*;


/**
 * Annotation for the keyboard_details table
 */
@Entity
@Table(name = "keyboard_details")
public class KeyboardDetailsAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "link")
    private String website_url;

    @Column(name = "price")
    private float price;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    //    @ElementCollection
//    @CollectionTable(name = "keyboard_images", joinColumns = @JoinColumn(name = "keyboard_detail_id"))
//    @Column(name = "image")
//    private List<String> images;
    @Column(name = "model")
    private String model;

    @Column(name = "brand")
    private String brand;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "keyboard_id")
    private int keyboardId;


    /**
     * Empty constructor
     */
    public KeyboardDetailsAnnotation() {

    }

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
