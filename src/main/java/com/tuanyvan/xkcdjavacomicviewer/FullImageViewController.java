package com.tuanyvan.xkcdjavacomicviewer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class FullImageViewController {

    @FXML
    private AnchorPane anchorPane;

//    @FXML
//    private ImageView imagePreview;

    @FXML
    private Text altTextBox;

    public void setPreview(String imageURL, String altText) {
        anchorPane.setBackground(new Background(
                new BackgroundImage(
                        new Image(imageURL),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT
                )
        ));

//        imagePreview.setImage(new Image(imageURL));

        altTextBox.setText(altText);

    }
}
