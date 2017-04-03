package seedu.typed.commons.util;

//@@author A0143853A
/**
 * Stores three items.
 */

public class TripleUtil<F, S, T> {
    private F first;
    private S second;
    private T third;

    public TripleUtil(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof TripleUtil) {
            @SuppressWarnings("unchecked")
            TripleUtil<F, S, T> triple = (TripleUtil<F, S, T>) obj;
            return first.equals(triple.getFirst())
                   && second.equals(triple.getSecond())
                   && third.equals(triple.getThird());
        } else {
            return false;
        }
    }
}
