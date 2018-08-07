package com.eamh.birdcontrol.data.dummy;

import com.eamh.birdcontrol.data.models.Bird;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DummyBird {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Bird> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Bird> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Bird item) {
        ITEMS.add(item);
        ITEM_MAP.put("" + item.get_id(), item);
    }

    public static Bird createDummyItem(int position) {
        Bird bird = new Bird();
        bird.set_id(position);
        bird.set_idBreedOrigin(position);
        bird.setPetName("Bird " + position);
        bird.setAnilla("Anilla " + position);
        bird.setImageUrl("Url " + position);
        bird.setGenre(position % 2 == 0 ? Bird.Genre.MALE : Bird.Genre.FEMALE);
        bird.setBirthDate(new Date());
        bird.setRace("Canario");
        bird.setColorMutation("Ágata rojo mosaico");
        bird.setDead(false);
        bird.setBreeding(true);
        return bird;
    }
}
