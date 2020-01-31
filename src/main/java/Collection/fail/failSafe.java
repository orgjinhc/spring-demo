package Collection.fail;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * fail-safe
 * java.util.concurrent包下集合类都是安全失败,在遍历时不是直接在集合内容上访问的，而是先复制原有集合内容，在拷贝的集合上进行遍历。
 * 原理：由于迭代时是对原集合的拷贝进行遍历，所以在遍历过程中对原集合所作的修改并不能被迭代器检测到，所以不会触发Cme
 */
@Slf4j
public class failSafe {
    public static void main(String[] args) {
        final ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();
        map.put(1, 1);
        map.put(2, 1);
        map.put(3, 1);

        new Thread(new Runnable() {
            public void run() {
                modify(map);
            }
        }).start();

        Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            log.info("next:[{}]", iterator.next());
        }
    }

    private static void modify(ConcurrentMap<Integer, Integer> map) {
        map.remove(3);
    }
}
