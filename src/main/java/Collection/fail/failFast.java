package Collection.fail;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * fail-fast
 * 	java.util包下集合类在用迭代器遍历一个集合对象时，遍历过程中对集合对象的结构进行了修改（增加、删除），则会抛出Concurrent Modify Exception
 * 	原因：迭代器遍历集合时，使用一个modCount变量来记录当遍历过程中集合对象结构发生了变化
 * 	     使用hashNext.next()就会检测modCount和expected值是否相等，否则抛出cme
 * 	modCount:列表在结构上被修改的次数
 */
@Slf4j
public class failFast {

    public static void main(String[] args) {
        final List<Integer> list = new ArrayList();
        list.add(1);
        Iterator<Integer> iterator = list.iterator();
        new Thread(new Runnable() {
            public void run() {
                modify(list);
            }
        }).start();

        while (iterator.hasNext()) {
            Integer next = iterator.next();
            log.info("next:[{}]", next.toString());
        }
    }

    private static void modify(List<Integer> list) {
        list.remove(list.size() - 1);
    }
}