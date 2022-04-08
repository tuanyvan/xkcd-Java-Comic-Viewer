package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class Repository {

    private ArrayList<Comic> comics;

    public Repository(ArrayList<Comic> comics) {
        setComics(comics);
    }

    public ArrayList<Comic> getComics() {
        return this.comics;
    }

    public void setComics(ArrayList<Comic> comics) {
        this.comics = comics;
        this.sortComics();
    }

    public int getNumberOfComics() {
        return comics.size();
    }

    public int getSumOfComicIDs() {
        return comics.stream().reduce(0, (sum, currentComic) -> sum + currentComic.getComicID(), Integer::sum);
    }

    public double getAverageOfComicIDs() {
        return Math.round((double) getSumOfComicIDs() / getNumberOfComics() * 100.0) / 100.0;
    }

    public double getStandardDeviationOfComicIDs() {
        double averageComicId = getAverageOfComicIDs();
        return
                Math.round(
                    Math.sqrt(
                        comics.stream().reduce(
                                0.0,
                                (sum, currentComic) -> sum + Math.pow(currentComic.getComicID() + averageComicId, 2),
                                Double::sum)
                                / getNumberOfComics()
                    ) * 100.0) / 100.0;
    }

    public void sortComics() {
        getComics().sort(Comparator.comparingInt(Comic::getComicID));
    }

    public void addToComics(Comic comic) {
        if (this.getComics().stream().noneMatch((c) -> c.getComicID() == comic.getComicID())) {
            this.getComics().add(comic);
            sortComics();
        }
    }

    public void removeFromComics(Comic comic) {
        if (this.getComics().contains(comic)) {
            this.getComics().remove(comic);
            sortComics();
        }
    }

    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        for (int i = 0; i < getComics().size(); i++) {
            json.put(Integer.toString(i), this.getComics().get(i).getJSON());
        }
        return json;
    }

}
