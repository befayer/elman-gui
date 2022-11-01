package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button buttonStart;

    @FXML
    private CheckBox checkBoxIris, checkBoxSegmentChallenge;

    @FXML
    private TextField countEpochs, alpha, K, truePredicted, falsePredicted, accuracy;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonStart.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            System.out.println("Starting program...");

            if (checkBoxIris.isSelected()){
                Iris iris = new Iris();
                iris.classificate(Integer.parseInt(countEpochs.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(K.getText()));
                truePredicted.setText(iris.getResults().get(0) + "/" + iris.getResults().get(1));
                accuracy.setText(String.valueOf(iris.getResults().get(2)));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < iris.getRows().size(); i++) {
                    textArea.setText(textArea.getText() + iris.getRows().get(i) + "\n");
                }
            }
            else if (checkBoxSegmentChallenge.isSelected()){
                SegmentChallenge segmentChallenge = new SegmentChallenge();
                segmentChallenge.classificate(Integer.parseInt(countEpochs.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(K.getText()));
                truePredicted.setText(segmentChallenge.getResults().get(0) + "/" + segmentChallenge.getResults().get(1));
                accuracy.setText(String.valueOf(segmentChallenge.getResults().get(2)));
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < segmentChallenge.getRows().size(); i++) {
                    textArea.setText(textArea.getText() + segmentChallenge.getRows().get(i) + "\n");
                }
            }
            mouseEvent.consume();
        });
    }
}
