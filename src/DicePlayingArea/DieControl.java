package DicePlayingArea;

import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DieControl extends ImageView {

  //  public Integer value;
    private boolean dragging = false;

    private final IntegerProperty dieValue = new SimpleIntegerProperty();

    Random rand = new Random();

    public DieControl() {
        super();
        roll();

        this.addEventFilter(MouseEvent.ANY,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(final MouseEvent event) {
                        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                            dragging = false;
                        } else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
                            dragging = true;
                        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                                setX(event.getX());
                                setY(event.getY());
                        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                            if (!dragging) {
                                roll();
                            }
                        }
                    }
                });
    }


    public void roll(){
        this.setDieValue( rand.nextInt(6) + 1 );
        loadImage();
    }

    public IntegerProperty dieValueProperty() {
        return dieValue;
    }

    public final Integer getDieValue() {
        return dieValueProperty().get();
    }

    public final void setDieValue(Integer value) {
        dieValueProperty().set(value);
    }


    private void loadImage() {
        String imageUrl="";

        try {
            imageUrl = "../images/side" + this.getDieValue() + ".png";
            Image image = new Image( getClass().getResource( imageUrl ).toExternalForm());
            setImage(image);
        } catch (Exception e){
            System.out.println(" EXCEPTION PIC" + imageUrl + " : " + e.getMessage());
        }
    }

}
