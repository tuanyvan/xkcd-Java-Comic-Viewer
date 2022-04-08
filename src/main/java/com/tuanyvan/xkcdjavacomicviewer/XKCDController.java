package com.tuanyvan.xkcdjavacomicviewer;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.fxml.FXMLLoader;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

public class XKCDController implements Initializable {

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
    private Label searchComicLabel;

    @FXML
    private Hyperlink comicURL;

    @FXML
    private TextField idInputField;

    @FXML
    private ListView<Comic> comicListView;

    private int newestComicId;
    private Comic currentComic;
    private Repository comicRepo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            processRequest("https://xkcd.com/info.0.json");
            newestComicId = currentComic.getComicID(); // Track the upper bound for the random comic generator.
        } catch (IOException e) {
            // TODO: Add warning screen to connect to Internet.
        }

        comicRepo = new Repository(new ArrayList<Comic>(), Repository.File.NOT_USED);
        updateComicListView();

    }

    private void processRequest(String url) throws IOException {
        setControllerLabels(makeComicRequest(url));
    }

    @FXML
    private void generateRandomComicButton(ActionEvent event) throws IOException {
        Random rng = new Random();
        processRequest(String.format("https://xkcd.com/%d/info.0.json", rng.nextInt(1, newestComicId))); // Exclude the latest comic from RNG
    }

    @FXML
    private void openInBrowser(ActionEvent event) throws IOException {
        Desktop.getDesktop().browse(URI.create(comicURL.getText()));
    }

    @FXML
    private void addComic(ActionEvent event) {
        comicRepo.addToComics(currentComic);
        updateComicListView();
    }

    @FXML
    private void deleteComic(ActionEvent event) {
        comicRepo.removeFromComics(comicListView.getSelectionModel().getSelectedItem());
        updateComicListView();
    }

    private void updateComicListView() {
        comicListView.setItems(FXCollections.observableArrayList(comicRepo.getComics()));
    }

    @FXML
    private void openDetailedStatisticsScene(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("repo-statistics-view.fxml"));

        Scene statViewScene = new Scene(loader.load());

        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        StatisticsViewController statViewController = loader.getController();
        statViewController.setCurrentRepo(comicRepo);

        currentStage.setScene(statViewScene);
        currentStage.setTitle("Repository Statistics");
        currentStage.show();

    }

    @FXML
    private void handleComicSearch(ActionEvent event) {
        try {
            String id = idInputField.getText();
            searchComicLabel.setText("Search Comic By ID");
            processRequest(String.format("https://xkcd.com/%d/info.0.json", Integer.parseInt(id)));
        }
        catch (NumberFormatException nfe) {
            searchComicLabel.setText("Please input a valid\nnumber.");
        }
        catch (IOException ioe) {
            searchComicLabel.setText("That comic ID is invalid.");
        }
        finally {
            idInputField.clear();
        }
    }

    private Comic makeComicRequest(String url) throws IOException {
        try {
            // Establish a connection to the most recent comic JSON.
            HttpsURLConnection request = (HttpsURLConnection) new URL(url).openConnection();
            request.setRequestProperty("User-Agent", "application/json");
            InputStream response = request.getInputStream();

            // Turn the response into a JSON object.
            JSONObject json = new JSONObject(new String(response.readAllBytes()));
            currentComic = new Comic(json);
            return currentComic;
        }
        catch (UnknownHostException nhe) {
            // TODO: Add warning screen to connect to Internet.
        }

        // If the request could not be made, return null.
        return null;
    }

    private void setControllerLabels(Comic comic) {
        comicIdLabel.setText("#" + comic.getComicID());
        titleLabel.setText(comic.getTitle());
        altLabel.setText(comic.getAltText());
        dateCreatedLabel.setText(
                String.format(
                        "%s (%s)",
                        comic.getPublishedDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)),
                        comic.getTimeSincePublication()
                )
        );
        comicImageView.setImage(new Image(comic.getImgURL()));
        comicURL.setText(String.format("https://xkcd.com/%d", comic.getComicID()));
    }

    @FXML
    private void createLargePreviewPane(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("full-image-view.fxml"));
        Scene fullImageView = new Scene(loader.load(), comicImageView.getImage().getWidth() * 2, comicImageView.getImage().getHeight() * 2);

        Stage newWindow = new Stage(StageStyle.DECORATED);
        newWindow.setTitle(String.format("%s - %s", comicIdLabel.getText(), titleLabel.getText()));
        FullImageViewController fivc = loader.getController();
        newWindow.show();
        newWindow.setScene(fullImageView);
        fivc.setPreview(comicImageView.getImage().getUrl(), altLabel.getText());
    }

}
