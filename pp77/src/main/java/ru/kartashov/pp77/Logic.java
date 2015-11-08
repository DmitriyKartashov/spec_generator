package ru.kartashov.pp77;

import ru.kartashov.objects.Posting;

/**
 * @author Dmitrii Kartashov
 */
public class Logic {
    public static final Posting ABC = new Posting();

    public void updatePosting(Posting posting)
    {
        posting.setB3Indicator(2, ABC);
    }
}
