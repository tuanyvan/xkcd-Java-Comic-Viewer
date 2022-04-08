package com.tuanyvan.xkcdjavacomicviewer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class StatisticsViewController {

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

    private Repository comicRepo;

    public void setCurrentRepo(Repository comicRepo) {
        this.comicRepo = comicRepo;
        updateFields();
    }

    public void updateFields() {

        ArrayList<Comic> comics = comicRepo.getComics();
        Comic highestComic = comics.stream().max(Comparator.comparingInt(Comic::getComicID)).get();
        Comic lowestComic = comics.stream().min(Comparator.comparingInt(Comic::getComicID)).get();

        highestComicIdLabel.setText(String.valueOf(highestComic.getComicID()));
        highestComicImage.setImage(new Image(highestComic.getImgURL()));
        highestComicNameLabel.setText(highestComic.toString());

        lowestComicIdLabel.setText(String.valueOf(lowestComic.getComicID()));
        lowestComicImage.setImage(new Image(lowestComic.getImgURL()));
        lowestComicNameLabel.setText(lowestComic.toString());

        longestComicTitleLabel.setText(comics.stream().max(Comparator.comparingInt(o -> o.getTitle().length())).get().toString());

        numberOfComicsLabel.setText(String.valueOf(comicRepo.getNumberOfComics()));
        sumOfComicIdsLabel.setText(String.valueOf(comicRepo.getSumOfComicIDs()));
        averageComicIdLabel.setText(String.valueOf(comicRepo.getAverageOfComicIDs()));
        stdeviationComicIdLabel.setText(String.valueOf(comicRepo.getStandardDeviationOfComicIDs()));

    }

    @FXML
    public void returnToFavoriteComics(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("xkcd-repository-view.fxml"));

        Scene repoViewScene = new Scene(loader.load());

        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();

        XKCDController XKCDController = loader.getController();
        XKCDController.setComicRepo(comicRepo);

        currentStage.setScene(repoViewScene);
        currentStage.setTitle("Repository Statistics");
        currentStage.show();
    }

}
