package com.tuanyvan.xkcdjavacomicviewer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsViewController implements Initializable {

    @FXML
    private Label averageComicIdLabel;

    @FXML
    private Label highestComicIdLabel;

    @FXML
    private ImageView highestComicImage;

    @FXML
    private Label highestComicNameLabel;

    @FXML
    private Label longestComicTitleLabel;

    @FXML
    private Label lowestComicIdLabel;

    @FXML
    private ImageView lowestComicImage;

    @FXML
    private Label lowestComicNameLabel;

    @FXML
    private Label numberOfComicsLabel;

    @FXML
    private Label stdeviationComicIdLabel;

    @FXML
    private Label sumOfComicIdsLabel;

    private Repository currentRepo;

    public void setCurrentRepo(Repository currentRepo) {
        this.currentRepo = currentRepo;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
