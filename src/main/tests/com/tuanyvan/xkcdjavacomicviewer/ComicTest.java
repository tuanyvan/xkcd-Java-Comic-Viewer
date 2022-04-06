package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.accessibility.AccessibleStateSet;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class ComicTest {

    private Comic premadeComic;
    LocalDate today = LocalDate.now();

    // Define a sample JSON file that will always be the same.
    JSONObject premadeJSON = new JSONObject(
            "{" +
                    "\"img\": \"https://imgs.xkcd.com/comics/sloped_border_2x.png\"," +
                    "\"num\": 2602," +
                    String.format("\"year\": %d,", today.getYear() - 1) +
                    String.format("\"month\": %d,", today.getMonth().getValue() - 1) +
                    String.format("\"day\": %d,", today.getDayOfMonth() - 1) +
                    "\"alt\": \"Oh no, there's a whole section in the treaty labeled 'curvature.'\"," +
                    "\"title\": \"Sloped Border\"" +
                  "}"
    );

    @BeforeEach
    void setUp() {
        premadeComic = new Comic(premadeJSON);
    }

    @Test
    void getPublishedDate() {
        assertEquals(
                LocalDate.of(
                        today.getYear() - 1,
                        today.getMonth().getValue() - 1,
                        today.getDayOfMonth() - 1
                ),
                premadeComic.getPublishedDate()
        );
    }

    @Test
    void getTimeSincePublication() {
        String premadeTSP = premadeComic.getTimeSincePublication();
        Period premadeTimeDiff = Period.between(premadeComic.getPublishedDate(), today);

        // Year plurality check
        if (premadeTimeDiff.getYears() == 1) {
            Assertions.assertFalse(premadeTSP.contains("years"));
        }
        else {
            Assertions.assertFalse(premadeTSP.contains("year"));
        }

        // Month plurality check
        if (premadeTimeDiff.getMonths() == 1) {
            Assertions.assertFalse(premadeTSP.contains("months"));
        }
        else {
            Assertions.assertFalse(premadeTSP.contains("month"));
        }

        // Day plurality check
        if (premadeTimeDiff.getDays() == 1) {
            Assertions.assertFalse(premadeTSP.contains("days"));
        }
        else {
            Assertions.assertFalse(premadeTSP.contains("day"));
        }

    }

    @Test
    void setInavlidImgURI() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> { premadeComic.setImgURI("http:/excellenturl.com"); }
        );

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> { premadeComic.setImgURI("https://imgs.xkcd.com/comics/linguistics_degree"); }
        );

        Assertions.assertDoesNotThrow(
                () -> premadeComic.setImgURI("https://imgs.xkcd.com/comics/linguistics_degree.png")
        );
    }

}