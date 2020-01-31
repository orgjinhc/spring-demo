package Collection.Array;

import java.util.Comparator;

public class PersonalComparator implements Comparator<Array> {

    public int compare(Array o1, Array o2) {
        return o1.getAge() > o2.getAge() ? 1 : o1.getAge() == o2.getAge() ? 0 : -1;
    }

    public static int compareByAge(Array array1, Array array2) {
        return array1.getAge() > array2.getAge() ? 1 : array1.getAge() == array2.getAge() ? 0 : -1;
    }


    public static int compareByScore(Array array1, Array array2) {
        return array1.getScore() > array2.getScore() ? 1 : array1.getScore() == array2.getScore() ? 0 : -1;
    }
}
