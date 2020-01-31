package Collection.Array;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;

@Data
@Slf4j
public class Array implements Comparable {

    private int age;

    private int score;

    public static void main(String[] args) {
        Array array = new Array();
        array.setAge(1);
        array.setScore(20);
        Array newArray = new Array();
        newArray.setAge(2);
        newArray.setScore(19);
//        log.info("result:[{}]", array.compareTo(newArray));
//
        log.info("result:[{}]", PersonalComparator.compareByAge(array,newArray));
        log.info("result:[{}]", PersonalComparator.compareByScore(array,newArray));
    }

    public int compareTo(Object o) {
        Array array = (Array) o;
        return this.age > array.age ? 1 : this.age == array.age ? 0 : -1;
    }
}