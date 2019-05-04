package models;

import javafx.scene.image.Image;

/**
 * Object containing information about an activity category
 * e.g. 'Food category'
 */
public class ActivityCategory {

    private int categoryId;
    private String name;
    private String desc;

    /**
     * Stores the 40px image that's shown on the 'add activity' page.
     */
    private String imagePath;
    private Image image;

    /**
     * Encapsulate default constructor to enforce passing all attributes.
     */
    private ActivityCategory() {}

    /**
     * Creates an activity group object.
     * @param id id of the activity group
     * @param name short name of the activity group
     * @param desc description of the activity group
     */
    public ActivityCategory(int id, String name, String desc, String imagePath) {
        this.categoryId = id;
        this.name = name;
        this.desc = desc;
        this.imagePath = imagePath;
        this.image = null;
    }

    public int getId() {
        return categoryId;
    }

    public void setId(int id) {
        this.categoryId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage( Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
