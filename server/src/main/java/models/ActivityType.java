package models;

/**
 * Object containing information about an activity type
 * e.g. 'travelled 15km with the train'
 */
public class ActivityType {

    private int id;
    private int points;

    private String name;
    private String description;

    private ActivityCategory category;
    private String subcategoryName;

    /**
     * Encapsulate default constructor to enforce passing all attributes.
     */
    private ActivityType() {}

    /**
     * Creates an activity type without a subcategory.
     * @param id the id of the activity type
     * @param name the name of the activity
     * @param points the green points of the activity
     * @param category the category of the activity
     * @param description the description of the activity
     */
    public ActivityType(int id, String name, int points,
                        ActivityCategory category, String description) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.category = category;
        this.subcategoryName = null;
        this.description = description;
    }

    /**
     * Creates an activity type with a subcategory.
     * @param id the id of the activity type
     * @param name the name of the activity
     * @param points the green points of the activity
     * @param category the category of the activity
     * @param subcategoryName optional string describing subcategory
     * @param description the description of the activity
     */
    public ActivityType(int id, String name, int points,
                        ActivityCategory category,
                        String subcategoryName, String description
    ) {
        this(id, name, points, category, description);
        this.subcategoryName = subcategoryName;
    }

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public ActivityCategory getCategory() {
        return category;
    }

    public void setCategory(ActivityCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }
}
