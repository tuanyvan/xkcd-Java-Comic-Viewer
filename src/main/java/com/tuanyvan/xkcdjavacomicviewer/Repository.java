package com.tuanyvan.xkcdjavacomicviewer;

import java.util.ArrayList;

public class Repository {

    enum File {
        NOT_USED,
        READ
    }

    private ArrayList<Comic> comics;

    public Repository(ArrayList<Comic> comics, File status) {
        setComics(comics, status);
    }

    public ArrayList<Comic> getComics() {
        return this.comics;
    }

    public void setComics(ArrayList<Comic> comics, File status) {
        if(comics.size() != 0 || status == File.NOT_USED) {
            this.comics = comics;
        }
        else {
            throw new IllegalArgumentException("The only time `comics` can be set to an empty list is when no output file could be read.");
        }
    }

    public int getNumberOfComics() {
        return comics.size();
    }

    public int getSumOfComicIDs() {
        return comics.stream().reduce(0, (sum, currentComic) -> sum + currentComic.getComicID(), Integer::sum);
    }

    public double getAverageOfComicIDs() {
        return (double) getSumOfComicIDs() / getNumberOfComics();
    }

    public double getStandardDeviationOfComicIDs() {
        double averageComicId = getAverageOfComicIDs();
        return comics.stream().reduce(0.0, (sum, currentComic) -> sum + Math.pow(currentComic.getComicID() + averageComicId, 2), Double::sum) / getNumberOfComics();
    }

}