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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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

    private int newestComicId = 3;
    private Comic currentComic;
    private Repository comicRepo;

    /**
     * Defines instructions for updateComicRepo() and determines if it needs to write to a .json file.
     */
    enum FileInstructions {
        WRITE,
        BYPASS
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Get the latest comic using the default JSON endpoint.
        try {
            processRequest("https://xkcd.com/info.0.json");
            newestComicId = currentComic.getComicID(); // Track the upper bound for the random comic generator.
        }
        catch (IOException | NullPointerException e) {
            try {
                createConnectionWarningPane();
            } catch (IOException ex) {
                System.out.println("The file, warning-screen-view.fxml, could not be loaded. Please check the integrity of the program's files.");
            }
        }

        // If comic-repo.json exists, use that to fill the contents in the comicListView.
        File json = new File("comic-repo.json");
        if (json.isFile()) {

            ArrayList<Comic> comics = new ArrayList<>();

            try {
                JSONObject jsonData =
                        new JSONObject(
                            new String(
                                Files.readAllBytes(
                                        Path.of(String.valueOf(json))
                                )
                            )
                        );
                comicRepo = new Repository(jsonData);
                updateComicRepo(FileInstructions.BYPASS);
            } catch (IOException e) {
                comicRepo = new Repository();
                System.out.println("The file, comic-repo.json, could not be read. Please check the administrative rights of this program.");
            }
        }
        // Otherwise, instantiate an empty Repository.
        else {
            comicRepo = new Repository();
        }

    }

    /**
     * Uses the various functions to fetch a comic's JSON and set the appropriate labels.
     * @param url   The URL pointing to the comic's JSON endpoint.
     */
    private void processRequest(String url) throws IOException {
        setControllerLabels(Objects.requireNonNull(makeComicRequest(url)));
    }

    /**
     * Generates a random comic from IDs 1 to the second-latest comic.
     * @throws IOException  If the user's network refuses or fails to connect to the JSON endpoint.
     */
    @FXML
    private void generateRandomComicButton(ActionEvent event) throws IOException {
        // Try generating a random comic, catch any network-related errors.
        try {
            Random rng = new Random();
            processRequest(
                String.format("https://xkcd.com/%d/info.0.json",
                        rng.nextInt(1, newestComicId) // Exclude the latest comic from RNG.
                )
            );
        }
        catch (UnknownHostException | NoRouteToHostException routeError) {
            createConnectionWarningPane();
        }
    }

    /**
     * Used by the comicURL Hyperlink to open the comic in the user's default browser.
     */
    @FXML
    private void openInBrowser(ActionEvent event) throws IOException {
        Desktop.getDesktop().browse(URI.create(comicURL.getText()));
    }

    /**
     * Add the comic to the Repository object and write to a .json file.
     */
    @FXML
    private void addComic(ActionEvent event) {
        comicRepo.addToComics(currentComic);
        updateComicRepo(FileInstructions.WRITE);
    }

    /**
     * Delete the currently selected comic from the Repository object and write to a .json file.
     */
    @FXML
    private void deleteComic(ActionEvent event) {
        comicRepo.removeFromComics(comicListView.getSelectionModel().getSelectedItem());
        updateComicRepo(FileInstructions.WRITE);
    }

    /**
     * Updates the comicListView to the altered comicRepo ArrayList.
     * @param instruction   When passed as FileInstructions.WRITE, writes to a file called comic-repo.json.
     */
    private void updateComicRepo(FileInstructions instruction) {
        try {
            comicListView.setItems(FXCollections.observableArrayList(comicRepo.getComics()));
            // If the enum FileInstructions.WRITE was passed, then write to comic-repo.json.
            if (instruction == FileInstructions.WRITE) {
                FileWriter comicRepoFile = new FileWriter("comic-repo.json");
                comicRepoFile.write(comicRepo.getJSON().toString());
                comicRepoFile.close();
            }

        }
        catch (IOException ioe) {
            System.out.println("The program could not write to comic-repo.json. Please check the administrative rights for this program.");
        }
    }

    @FXML
    private void openDetailedStatisticsScene(ActionEvent event) throws IOException {
        // Do not allow operation to go through if there are no comics in comicRepo.
        if (comicRepo.getComics().size() != 0) {
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
    }

    /**
     * Searches a comic by ID based on user input.
     */
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

    /**
     * Makes a request to the specified URL.
     * @param url   The JSON endpoint to interface with.
     * @return  The JSON object returned from the url endpoint.
     * @throws IOException  If the user fails/refuses to connect to the URL endpoint.
     */
    private Comic makeComicRequest(String url) throws IOException {

        // Establish a connection to the most recent comic JSON.
        HttpsURLConnection request = (HttpsURLConnection) new URL(url).openConnection();
        request.setRequestProperty("User-Agent", "application/json");
        InputStream response = request.getInputStream();

        // Turn the response into a JSON object.
        JSONObject json = new JSONObject(new String(response.readAllBytes()));
        return new Comic(json);

    }

    /**
     * Sets the comic information labels and sets the currentComic.
     * @param comic The Comic object to set the information labels to.
     */
    private void setControllerLabels(Comic comic) {
        currentComic = comic;
        comicIdLabel.setText("#" + comic.getComicID());
        titleLabel.setText(comic.getTitle());
        altLabel.setText(comic.getAltText());
        dateCreatedLabel.setText(
                String.format(
                        "%s (%s)",
                        // Get the date in a localized format, such as "Friday, April 8th, 2022".
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

        Stage newWindow = new Stage();
        newWindow.setTitle(String.format("%s - %s", comicIdLabel.getText(), titleLabel.getText()));
        FullImageViewController fivc = loader.getController();
        newWindow.setScene(fullImageView);
        fivc.setPreview(comicImageView.getImage().getUrl(), altLabel.getText());
        newWindow.show();
    }

    private void createConnectionWarningPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("warning-screen-view.fxml"));
        Scene warningPane = new Scene(loader.load());
        Stage newWindow = new Stage();
        newWindow.setScene(warningPane);
        newWindow.setTitle("500 - Internet Not Found");
        newWindow.show();
    }

    /**
     * Used by StatisticsViewController to pass the Repository object back to this controller.
     * @param comicRepo The Repository object being passed back.
     */
    public void setComicRepo(Repository comicRepo) {
        this.comicRepo.setComics(comicRepo.getComics());
        updateComicRepo(FileInstructions.BYPASS);
    }

    /**
     * Changes the current comic and information labels based on a selection in the comicListView.
     */
    @FXML
    public void renderSelectedComic(MouseEvent event) {
        if (comicListView.getSelectionModel().getSelectedItem() != null) {
            Comic selectedComic = comicListView.getSelectionModel().getSelectedItem();
            setControllerLabels(selectedComic);
        }
    }

}
