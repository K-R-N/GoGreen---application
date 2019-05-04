package gui.helpers;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Makes it possible for a Node to influence the main window drag.
 * @author Jules van der Toorn
 */
public class DraggableNode {

    private DraggableNode() {}

    /**
     * Enables influencing window for node by setting listeners to a mouse event on it.
     * @param node the node that will get the listener added to it
     */
    public static void enableDrag(Stage stage, Node node) {
        if (stage == null) {
            System.out.println("No stage initialized for DraggableNode class!");
            return;
        }

        final Delta dragDelta = new Delta();

        node.setOnMousePressed(mouseEvent -> {
            // record a delta distance for the drag and drop operation.
            dragDelta.dx = stage.getX() - mouseEvent.getScreenX();
            dragDelta.dy = stage.getY() - mouseEvent.getScreenY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() + dragDelta.dx);
            stage.setY(mouseEvent.getScreenY() + dragDelta.dy);
        });
    }

    /**
     * Small struct to hold Delta coordinates.
     */
    public static class Delta {
        double dx;
        double dy;
    }
}


