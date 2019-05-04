package gui.views.general;

import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

public class PictureView extends View {

    private WritableImage image;
    private Rectangle rect;

    private View overlayView;

    private double width;
    private double height;

    /**
     * Creates a view with the image centered and fitted, with a circular border.
     * @param image image that will be shown
     * @param diameter diameter of the circle
     */
    public PictureView(Image image,
                       double diameter) {
        if (image != null) {
            this.image = cropToSquare(image);
        }
        this.width = diameter;
        this.height = diameter;
        this.rect = new Rectangle();
        rect.setArcWidth(diameter);
        rect.setArcHeight(diameter);

        render();
    }

    /**
     * Creates a view with the image centered and fitted.
     * @param image image that will be shown
     * @param width width of the viewport
     * @param height height of the viewport
     */
    public PictureView(Image image,
                       double width,
                       double height) {
        if (image != null) {
            this.image = cropToSquare(image);
        }
        this.width = width;
        this.height = height;
        this.rect = new Rectangle();

        render();
    }

    /**
     * renders an image.
     * @param image to be rendered.
     */
    public void setPicture(Image image) {
        if (image != null) {
            this.image = cropToSquare(image);
        }

        render();
    }

    /**
     * Returns the image the PictureView contains.
     * @return image which is in the PictureView.
     */
    public Image getPicture() {
        if (image != null) {
            return image;
        } else {
            return null;
        }
    }

    /**
     * Crops picture to a square.
     * @param image original image
     * @return square result image
     */
    private WritableImage cropToSquare(Image image) {
        int origWidth = (int) image.getWidth();
        int origHeight = (int) image.getHeight();

        try {
            PixelReader reader = image.getPixelReader();
            if (origWidth > origHeight) {
                return new WritableImage(
                        reader,
                        (origWidth - origHeight) / 2,
                        0,
                        origHeight,
                        origHeight
                );
            } else {
                return new WritableImage(
                        reader,
                        0,
                        (origHeight - origWidth) / 2,
                        origWidth,
                        origWidth
                );
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Adds overlay view to the picture view.
     * @param view view that will be the overlay
     */
    public void addOverlay(View view) {

        overlayView = view;
        addSubview(overlayView);

        setInsetsFor(overlayView, 0, null, null, 0);
        setPrefHeight(height);
        setPrefWidth(width);

        setClip(rect);
        overlayView.setOpacity(0.0);
        overlayView.setCache(true);
        overlayView.setCacheHint(CacheHint.SPEED);
    }

    /**
     * Set the visibility state of the overlay image.
     * @param visible boolean indicating visibility state
     */
    public void setOverlayVisible(boolean visible) {
        overlayView.setOpacity(visible ? 1.0 : 0.0);
    }

    private void render() {
        if (image == null) {
            System.out.println("No picture for picture view provided!");
            return;
        }

        this.getChildren().clear();

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        addSubview(imageView);
        setInsetsFor(imageView, 0, null, null, 0);
        setPrefHeight(height);
        setPrefWidth(width);
        rect.setWidth(width);
        rect.setHeight(height);
        setClip(rect);

        // center image
        double setAspect = width / height;
        double imgAspect = image.getWidth() / image.getHeight();
        if (setAspect > imgAspect) {
            imageView.setFitWidth(width);
            double fittedHeight = width / image.getWidth() * image.getHeight();
            imageView.setTranslateY(
                    -(fittedHeight - height) / 2
            );
        } else {
            imageView.setFitHeight(height);
            double fittedWidth = height / image.getHeight() * image.getWidth();
            imageView.setTranslateX(
                    -(fittedWidth - width) / 2
            );
        }
    }
}
