package eu.abdullah.cst3130.models;


import jakarta.persistence.*;

/**
 * This is the annotation for the comparison table
 */
@Entity
@Table(name = "comparison_table")
public class ComparisonAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "link")
    private String link;

    @Column(name = "price")
    private float price;

    @Column(name = "keyboard_details_id")
    private int keyboardId;

    /**
     * Empty constructor
     */
    public ComparisonAnnotation() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getKeyboardId() {
        return keyboardId;
    }

    public void setKeyboardId(int keyboardId) {
        this.keyboardId = keyboardId;
    }

}
