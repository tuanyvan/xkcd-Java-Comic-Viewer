package com.tuanyvan.xkcdjavacomicviewer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class FullImageViewController {

    @FXML
    private ImageView imagePreview;

    @FXML
    private Text altTextBox;

    public void setPreview(String imageURL, String altText) {

        imagePreview.setImage(new Image(imageURL));
        altTextBox.setText(altText);

    }
}
