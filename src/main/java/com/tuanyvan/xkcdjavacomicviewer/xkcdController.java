package com.tuanyvan.xkcdjavacomicviewer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class xkcdController implements Initializable {

    @FXML
    private ImageView comicImageView;

    @FXML
    private Label comicInformationLabel;

    private int newestComicId;

    @FXML
    void generateRandomComicButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Establish a connection to the most recent comic JSON.
            HttpsURLConnection request = (HttpsURLConnection) new URL("https://xkcd.com/info.0.json").openConnection();
            request.setRequestProperty("User-Agent", "application/json");
            InputStream response = request.getInputStream();

            // Turn the response into a JSON object.
            JSONObject json = new JSONObject(new String(response.readAllBytes()));
            int newestComicId = (int) json.get("num");

            comicInformationLabel.setText("The newest comic is #" + newestComicId + ".");
            Image comic = new Image(json.get("img").toString());

            comicImageView.setImage(comic);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
