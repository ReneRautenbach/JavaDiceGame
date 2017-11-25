package DicePlayingArea;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class MainController implements Initializable {

    private Random rand = new Random();

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label lblDiceTotal;

    @FXML
    private Pane playingArea;

    private SimpleIntegerProperty sumOfDice = new SimpleIntegerProperty();

    final ObservableList<DieControl> diceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // bind label to total value
        // this will update the label with the calculated sum every time the sumOfDice changes
        lblDiceTotal.textProperty().bind(sumOfDice.asString());

        // This will listen for changes on the observableArrayList diceList
        // Every time a die is added to the list, the die will be added to the playingArea
        // and the sum will be adjusted. Since this caters for the wasAdded change, the oldValue is set to 0
        diceList.addListener(new ListChangeListener<DieControl>() {
            @Override
            public void onChanged(Change<? extends DieControl> change) {
                System.out.println(change);
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (DieControl die : change.getAddedSubList()) {
                            playingArea.getChildren().add( die );
                            calculateDiceSum( 0, die.getDieValue() );
                        }
                    }
                }
            }
        });
    }

    // create and returns a new die instance
    private DieControl rollDie(){
        DieControl die = new DieControl();
        die.roll();
        die.setX( rand.nextInt(450) + 1 );
        die.setY( rand.nextInt(450) + 1 );
        // add a watch on the dieValueProperty
        // this will calculate the change in sum on every change in die value
        die.dieValueProperty().addListener((observable,  oldValue,  newValue) -> {
            calculateDiceSum( (Integer)oldValue ,(Integer)newValue );
        });
        return die;
    }

    // event handler for the Add Die button click
    @FXML
    private void handleAddDieAction() {
        final DieControl die = rollDie();
        diceList.add(die);
    }

    // event handler for the Reroll All Button click
    @FXML
    private void handleRerollDieAction() {
        for (Node die : this.diceList) {
            ((DieControl) die).roll();
        }
    }

    private void calculateDiceSum(Integer prevValue, Integer newValue) {
        sumOfDice.set(sumOfDice.get() - prevValue + newValue);
    }

}
