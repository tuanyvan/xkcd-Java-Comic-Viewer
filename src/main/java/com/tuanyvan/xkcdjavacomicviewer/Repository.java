package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class Repository {

    private ArrayList<Comic> comics;

    enum InitializeType {
        FROM_OBJECT,
        EMPTY
    }

    /**
     * The default constructor which creates an empty ArrayList<Comic> for the comics field.
     */
    public Repository() {
        setComics(new ArrayList<>(), InitializeType.EMPTY);
    }

    /**
     * A 1-param constructor which uses a JSON object to parse a series of Comic entries.
     * @param json  A JSONObject containing the contents of the Repository class.
     */
    public Repository(JSONObject json) {
        ArrayList<Comic> jsonComics = new ArrayList<>();
        for (Iterator<String> it = json.keys(); it.hasNext(); ) {
            JSONObject entry = (JSONObject) json.get(it.next());
            // Comics validated as they are added from the JSON object.
            jsonComics.add(new Comic(entry));
        }
        setComics(jsonComics, InitializeType.FROM_OBJECT);
    }

    /**
     * @return The ArrayList<Comic> contents of the Repository's comics field.
     */
    public ArrayList<Comic> getComics() {
        return this.comics;
    }

    /**
     * Sets the comics field and sorts it.
     * @param comics    The ArrayList<Comic> to set comics to.
     */
    public void setComics(ArrayList<Comic> comics, InitializeType init) {
        if ((init == InitializeType.EMPTY && comics.size() == 0) || init == InitializeType.FROM_OBJECT) {
            this.comics = comics;
            this.sortComics();
        }
        else {
            throw new IllegalArgumentException("If initializing an empty ArrayList, please keep its size as zero.");
        }
    }

    /**
     * @return The number of items in the comics field.
     */
    public int getNumberOfComics() {
        return comics.size();
    }

    /**
     * @return The sum of all the Comic IDs in the comics field.
     */
    public int getSumOfComicIDs() {
        return comics.stream().reduce(0, (sum, currentComic) -> sum + currentComic.getComicID(), Integer::sum);
    }

    /**
     * @return The average Comic ID in the comics field.
     */
    public double getAverageOfComicIDs() {
        return Math.round((double) getSumOfComicIDs() / getNumberOfComics() * 100.0) / 100.0;
    }

    /**
     * @return The standard deviation of the Comic IDs in the comics field.
     */
    public double getStandardDeviationOfComicIDs() {
        double averageComicId = getAverageOfComicIDs();
        return
                Math.round(Math.sqrt(
                        getComics()
                                .stream()
                                .reduce(
                                    0.0,
                                    (sum, comic) -> sum + Math.pow(comic.getComicID() - averageComicId, 2),
                                    Double::sum
                                )
                        / getNumberOfComics()
                ) * 100.0) / 100.0;
    }

    /**
     * Uses a comparator to sort each Comic object by their comidID fields.
     */
    public void sortComics() {
        getComics().sort(Comparator.comparingInt(Comic::getComicID));
    }

    /**
     * Adds a comic to the comics ArrayList if it is not a duplicate of an existing comic.
     * @param comic The Comic object to add.
     */
    public void addToComics(Comic comic) {
        if (this.getComics().stream().noneMatch((c) -> c.getComicID() == comic.getComicID())) {
            this.getComics().add(comic);
            sortComics();
        }
    }

    /**
     * Removes the specified Comic object only if it exists.
     * @param comic The Comic object to remove.
     */
    public void removeFromComics(Comic comic) {
        if (this.getComics().contains(comic)) {
            this.getComics().remove(comic);
            sortComics();
        }
    }

    /**
     * @return A JSON payload containing the Repository's comic contents.
     */
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        for (int i = 0; i < getComics().size(); i++) {
            json.put(Integer.toString(i), this.getComics().get(i).getJSON());
        }
        return json;
    }

}
