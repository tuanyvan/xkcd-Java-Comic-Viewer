package com.tuanyvan.xkcdjavacomicviewer;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class ComicTest {

    private Comic premadeComic, monthOldComic, dayOldComic;
    private LocalDate today = LocalDate.now();
    private JSONObject premadeJSON;

    @BeforeEach
    void setUp() {

        // If subtracting 1 from the month or year would cause an issue, change it to YYYY/12/12
        if (today.getMonth().getValue() == 1 || today.getDayOfMonth() == 1) {
            today = LocalDate.of(today.getYear(), 12, 12);
            System.out.println(String.format("Using an alternative date of %s for unit test.", today));
        }

        // Define a sample JSON file that will always be the same.
        premadeJSON = new JSONObject(
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
    void lessThanAYearSincePublication() {
        JSONObject plusAYear = new JSONObject(premadeJSON.toString());
        plusAYear.put("year", today.getYear());
        JSONObject plusAMonth = new JSONObject(plusAYear.toString());
        plusAMonth.put("month", today.getMonth().getValue());

        monthOldComic = new Comic(plusAYear);
        dayOldComic = new Comic(plusAMonth);

        String monthOldComicTSP = monthOldComic.getTimeSincePublication();
        Period monthOldComicTD = Period.between(monthOldComic.getPublishedDate(), today);
        String dayOldComicTSP = dayOldComic.getTimeSincePublication();
        Period dayOldComicTD = Period.between(dayOldComic.getPublishedDate(), today);

        // Month plurality check
        if (monthOldComicTD.getMonths() == 1) {
            Assertions.assertFalse(monthOldComicTSP.contains("months"));
        }
        else {
            Assertions.assertFalse(monthOldComicTSP.contains("month"));
        }

        // Day plurality check
        if (dayOldComicTD.getDays() == 1) {
            Assertions.assertFalse(dayOldComicTSP.contains("days"));
        }
        else {
            Assertions.assertFalse(dayOldComicTSP.contains("day"));
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

    @Test
    void getTitle() {
        assertEquals("Sloped Border", premadeComic.getTitle());
    }

    @Test
    void getAltText() {
        assertEquals("Oh no, there's a whole section in the treaty labeled 'curvature.'", premadeComic.getAltText());
    }

    @Test
    void getComicID() {
        assertEquals(2602, premadeComic.getComicID());
    }

    @Test
    void getImgURI() {
        assertEquals("https://imgs.xkcd.com/comics/sloped_border_2x.png", premadeComic.getImgURI());
    }
}