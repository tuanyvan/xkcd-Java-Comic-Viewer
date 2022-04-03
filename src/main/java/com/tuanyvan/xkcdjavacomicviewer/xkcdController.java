package com.tuanyvan.xkcdjavacomicviewer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

import javafx.scene.text.TextFlow;
import org.json.JSONObject;

public class xkcdController implements Initializable {

    @FXML
    private Label altLabel;

    @FXML
    private ImageView comicImageView;

    @FXML
    private Label comicIdLabel;

    @FXML
    private Label dateCreatedLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Hyperlink comicURL;

    private int newestComicId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Comic newestComic = makeComicRequest("https://xkcd.com/info.0.json");
            newestComicId = newestComic.getComicID(); // Track the upper bound for the random comic generator
            setControllerLabels(newestComic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void generateRandomComicButton(ActionEvent event) throws IOException {
        Random rng = new Random();
        Comic comic = makeComicRequest(String.format("https://xkcd.com/%d/info.0.json", rng.nextInt(1, newestComicId)));
        setControllerLabels(comic);
    }

    @FXML
    void openInBrowser(ActionEvent event) throws IOException {
        Desktop.getDesktop().browse(URI.create(comicURL.getText()));
    }

    @FXML
    void openDetailedStatisticsScene(ActionEvent event) {

    }

    private Comic makeComicRequest(String url) throws IOException {
        // Establish a connection to the most recent comic JSON.
        HttpsURLConnection request = (HttpsURLConnection) new URL(url).openConnection();
        request.setRequestProperty("User-Agent", "application/json");
        InputStream response = request.getInputStream();

        // Turn the response into a JSON object.
        JSONObject json = new JSONObject(new String(response.readAllBytes()));

        return new Comic(json);
    }

    private void setControllerLabels(Comic comic) {
        comicIdLabel.setText("#" + comic.getComicID());
        titleLabel.setText(comic.getTitle());
        altLabel.setText(comic.getAltText());
        dateCreatedLabel.setText(
                String.format(
                        "%s (%s)",
                        comic.getPublishedDate().toString(),
                        comic.getTimeSincePublication()
                )
        );
        comicImageView.setImage(new Image(comic.getImgURI()));
        comicURL.setText(String.format("https://xkcd.com/%d", comic.getComicID()));
        comicURL.setVisited(false);
    }

}
