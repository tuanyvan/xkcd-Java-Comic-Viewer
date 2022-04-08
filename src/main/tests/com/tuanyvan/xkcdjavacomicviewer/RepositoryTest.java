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
    private final File testJson = new File("test-repo.json");
    private final JSONObject testJsonData = new JSONObject("{\"0\":{\"img\":\"https://imgs.xkcd.com/comics/efficiency.png\",\"month\":11,\"year\":2014,\"num\":1445,\"alt\":\"I need an extension for my research project because I spent all month trying to figure out whether learning Dvorak would help me type it faster.\",\"title\":\"Efficiency\",\"day\":10},\"1\":{\"img\":\"https://imgs.xkcd.com/comics/i_in_team.png\",\"month\":8,\"year\":2015,\"num\":1562,\"alt\":\"There's no \\\"I\\\" in \\\"VOWELS\\\".\",\"title\":\"I in Team\",\"day\":10},\"2\":{\"img\":\"https://imgs.xkcd.com/comics/google_trends_maps.png\",\"month\":3,\"year\":2019,\"num\":2126,\"alt\":\"It's early 2020. The entire country is gripped with Marco fever except for Alaska, which is freaking out. You're frantically studying up on etiquette and/or sexting.\",\"title\":\"Google Trends Maps\",\"day\":20},\"3\":{\"img\":\"https://imgs.xkcd.com/comics/childhood_toys.png\",\"month\":4,\"year\":2022,\"num\":2603,\"alt\":\"The rope keeps breaking, I'm covered in bruises and scrapes, and I've barely reached the end of my driveway, but I don't care--I'm determined to become the first person to commute to work by tetherball.\",\"title\":\"Childhood Toys\",\"day\":6}}");

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
        assertEquals(7736, loadedFromFile.getSumOfComicIDs());
    }

    @Test
    void getAverageOfComicIDs() {
        assertEquals(1934.0, loadedFromFile.getAverageOfComicIDs());
    }

    @Test
    void getStandardDeviationOfComicIDs() {

        assertEquals(464.20, loadedFromFile.getStandardDeviationOfComicIDs());
    }

    @Test
    void sortComics() {
        Comic firstComic = new Comic((JSONObject) testJsonData.get("0")); // ID 1445
        Comic secondComic = new Comic((JSONObject) testJsonData.get("1")); // ID 1562
        Comic thirdComic = new Comic((JSONObject) testJsonData.get("2")); // ID 2126
        Comic fourthComic = new Comic((JSONObject) testJsonData.get("3")); // ID 2603

        // Adding elements using unintended procedure.
        defaultRepo.getComics().add(secondComic);
        defaultRepo.getComics().add(firstComic);
        defaultRepo.getComics().add(fourthComic);
        defaultRepo.getComics().add(thirdComic);

        // Call the sort and compare it to loadedFromFile, which is sorted.
        assertNotEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
        defaultRepo.sortComics();
        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void addToComics() {
        Comic firstComic = new Comic((JSONObject) testJsonData.get("0")); // ID 1445
        Comic secondComic = new Comic((JSONObject) testJsonData.get("1")); // ID 1562
        Comic thirdComic = new Comic((JSONObject) testJsonData.get("2")); // ID 2126
        Comic fourthComic = new Comic((JSONObject) testJsonData.get("3")); // ID 2603

        // Again, placing comics in the wrong order, which will be sorted out in this function.
        defaultRepo.addToComics(secondComic);
        defaultRepo.addToComics(firstComic);
        defaultRepo.addToComics(fourthComic);
        defaultRepo.addToComics(thirdComic);

        assertEquals(defaultRepo.getComics().toString(), loadedFromFile.getComics().toString());
    }

    @Test
    void removeFromComics() {

        // We will need to clone instances of the objects we are deleting since iterating through an ArrayList as we delete items causes ConcurrentModfiicationException.
        Object[] clonedData = Arrays.copyOf(loadedFromFile.getComics().toArray(), 4);
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