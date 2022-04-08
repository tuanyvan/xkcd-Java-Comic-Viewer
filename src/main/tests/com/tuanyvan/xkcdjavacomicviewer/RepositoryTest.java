package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {

    private Repository loadedFromFile;
    private Repository defaultRepo;
    private final JSONObject testJsonData = new JSONObject("{\"0\":{\"img\":\"https://imgs.xkcd.com/comics/elevator_inspection.png\",\"month\":5,\"year\":2011,\"num\":897,\"alt\":\"Even governmental elevator inspectors get bored halfway through asking where the building office is.\",\"title\":\"Elevator Inspection\",\"day\":11},\"1\":{\"img\":\"https://imgs.xkcd.com/comics/cloud.png\",\"month\":11,\"year\":2014,\"num\":1444,\"alt\":\"Cloud computing has a ways to go.\",\"title\":\"Cloud\",\"day\":7},\"2\":{\"img\":\"https://imgs.xkcd.com/comics/efficiency.png\",\"month\":11,\"year\":2014,\"num\":1445,\"alt\":\"I need an extension for my research project because I spent all month trying to figure out whether learning Dvorak would help me type it faster.\",\"title\":\"Efficiency\",\"day\":10},\"3\":{\"img\":\"https://imgs.xkcd.com/comics/squirrel_plan.png\",\"month\":3,\"year\":2015,\"num\":1503,\"alt\":\"[Halfway to the Sun ...] Heyyyy ... what if this BALLOON is full of acorns?!\",\"title\":\"Squirrel Plan\",\"day\":25},\"4\":{\"img\":\"https://imgs.xkcd.com/comics/i_in_team.png\",\"month\":8,\"year\":2015,\"num\":1562,\"alt\":\"There's no \\\"I\\\" in \\\"VOWELS\\\".\",\"title\":\"I in Team\",\"day\":10},\"5\":{\"img\":\"https://imgs.xkcd.com/comics/frankenstein_captcha.png\",\"month\":4,\"year\":2022,\"num\":2604,\"alt\":\"The distinction between a ship and a boat is a line drawn in water.\",\"title\":\"Frankenstein Captcha\",\"day\":8}}");

    @BeforeEach
    void setUp() {
        loadedFromFile = new Repository(testJsonData);
        defaultRepo = new Repository();
    }

    @Test
    void getComics() {
        for (int i = 0; i < testJsonData.length(); i++) {
            JSONObject data = (JSONObject) testJsonData.get(Integer.toString(i));
            defaultRepo.getComics().add(new Comic(data));
        }
        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void setComics() {
        defaultRepo.setComics(loadedFromFile.getComics());
        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void getNumberOfComics() {
        int numberOfComics = testJsonData.length();
        assertEquals(numberOfComics, loadedFromFile.getNumberOfComics());
    }

    @Test
    void getSumOfComicIDs() {
        assertEquals(9455, loadedFromFile.getSumOfComicIDs());
    }

    @Test
    void getAverageOfComicIDs() {
        assertEquals(1575.83, loadedFromFile.getAverageOfComicIDs());
    }

    @Test
    void getStandardDeviationOfComicIDs() {

        assertEquals(509.56, loadedFromFile.getStandardDeviationOfComicIDs());
    }

    @Test
    void sortComics() {
        Comic firstComic = new Comic((JSONObject) testJsonData.get("0")); // ID 897
        Comic secondComic = new Comic((JSONObject) testJsonData.get("1")); // ID 1444
        Comic thirdComic = new Comic((JSONObject) testJsonData.get("2")); // ID 1445
        Comic fourthComic = new Comic((JSONObject) testJsonData.get("3")); // ID 1503
        Comic fifthComic = new Comic((JSONObject) testJsonData.get("4")); // ID 1562
        Comic sixthComic = new Comic((JSONObject) testJsonData.get("5")); // ID 2604

        // Adding elements using unintended procedure.
        defaultRepo.getComics().add(secondComic);
        defaultRepo.getComics().add(fifthComic);
        defaultRepo.getComics().add(firstComic);
        defaultRepo.getComics().add(fourthComic);
        defaultRepo.getComics().add(sixthComic);
        defaultRepo.getComics().add(thirdComic);

        // Call the sort and compare it to loadedFromFile, which is sorted.
        assertNotEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
        defaultRepo.sortComics();
        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void addToComics() {
        Comic firstComic = new Comic((JSONObject) testJsonData.get("0")); // ID 897
        Comic secondComic = new Comic((JSONObject) testJsonData.get("1")); // ID 1444
        Comic thirdComic = new Comic((JSONObject) testJsonData.get("2")); // ID 1445
        Comic fourthComic = new Comic((JSONObject) testJsonData.get("3")); // ID 1503
        Comic fifthComic = new Comic((JSONObject) testJsonData.get("4")); // ID 1562
        Comic sixthComic = new Comic((JSONObject) testJsonData.get("5")); // ID 2604

        // Again, placing comics in the wrong order, which will be sorted automatically using this function.
        defaultRepo.addToComics(secondComic);
        defaultRepo.addToComics(firstComic);
        defaultRepo.addToComics(fifthComic);
        defaultRepo.addToComics(sixthComic);
        defaultRepo.addToComics(fourthComic);
        defaultRepo.addToComics(thirdComic);

        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void removeFromComics() {

        // We will need to clone instances of the objects we are deleting since iterating through an ArrayList as we delete items causes ConcurrentModfiicationException.
        Object[] clonedData = Arrays.copyOf(loadedFromFile.getComics().toArray(), testJsonData.length());
        ArrayList<Comic> tempComics = new ArrayList<Comic>();
        for (Object comic : clonedData) {
            tempComics.add((Comic) comic);
        }

        // Remove all comics from the Repository loadedFromFile.
        for (Comic comic : tempComics) {
            loadedFromFile.removeFromComics(comic);
        }

        // It now must be equal to the default repository.
        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());

    }

    @Test
    void getJSON() {
        // The JSON from the Repository object must be the same as when it came in.
        assertEquals("{}", defaultRepo.getJSON().toString());
        assertEquals(testJsonData.toString(), loadedFromFile.getJSON().toString());
    }
}