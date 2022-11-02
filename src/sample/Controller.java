package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button buttonStart;

    @FXML
    private CheckBox checkBoxIris, checkBoxSegmentChallenge, checkBoxWine;

    @FXML
    private TextField countEpochs, alpha, K, truePredicted, accuracy;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        buttonStart.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            System.out.println("Starting program...");
            textArea.clear();
            if (checkBoxIris.isSelected()){
                Iris iris = new Iris();
                iris.classificate(Integer.parseInt(countEpochs.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(K.getText()));
                truePredicted.setText(iris.getResults().get(0) + "/" + iris.getResults().get(1));
                accuracy.setText(String.valueOf(iris.getResults().get(2)));
                for (int i = 0; i < iris.getArrayList().size(); i++) {
                    textArea.setText(textArea.getText() + iris.getArrayList().get(i) + "\n");
                }
                for (int i = 0; i < iris.getRows().size(); i++) {
                    textArea.setText(textArea.getText() + iris.getRows().get(i) + "\n");
                }
            }
            else if (checkBoxSegmentChallenge.isSelected()){
                SegmentChallenge segmentChallenge = new SegmentChallenge();
                segmentChallenge.classificate(Integer.parseInt(countEpochs.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(K.getText()));
                truePredicted.setText(segmentChallenge.getResults().get(0) + "/" + segmentChallenge.getResults().get(1));
                accuracy.setText(String.valueOf(segmentChallenge.getResults().get(2)));
                for (int i = 0; i < segmentChallenge.getArrayList().size(); i++) {
                    textArea.setText(textArea.getText() + segmentChallenge.getArrayList().get(i) + "\n");
                }
                for (int i = 0; i < segmentChallenge.getRows().size(); i++) {
                    textArea.setText(textArea.getText() + segmentChallenge.getRows().get(i) + "\n");
                }
            }
            else if (checkBoxWine.isSelected()){
                Wine wine = new Wine();
                wine.classificate(Integer.parseInt(countEpochs.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(K.getText()));
                truePredicted.setText(wine.getResults().get(0) + "/" + wine.getResults().get(1));
                accuracy.setText(String.valueOf(wine.getResults().get(2)));
                for (int i = 0; i < wine.getArrayList().size(); i++) {
                    textArea.setText(textArea.getText() + wine.getArrayList().get(i) + "\n");
                }
                for (int i = 0; i < wine.getRows().size(); i++) {
                    textArea.setText(textArea.getText() + wine.getRows().get(i) + "\n");
                }
            }
            mouseEvent.consume();
        });
    }
}
