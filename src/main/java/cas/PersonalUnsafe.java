package cas;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sun.java2d.UnixSurfaceManagerFactory;
import sun.misc.Unsafe;

import java.lang.reflect.Field;


/**
 *
 */
@Slf4j
public class PersonalUnsafe {

    private static int changeValue = 88;

    private static long ageOffsetByCustom = 00;

    private static final String AGE = "age";

    public static void main(String[] args) throws Exception {
        //  通过反射实例化Unsafe
        Field f = Unsafe.class.getDeclaredField("theUnsafe");

        //  设置暴力访问
        f.setAccessible(true);

        //  获取私有方法
        Unsafe unsafe = (Unsafe) f.get(null);

        //实例化Person
        long ageOffsetByActual = 0;
        Person person = (Person) unsafe.allocateInstance(Person.class);
        person.setAge(40);
        person.setName("zhangsan");
        for (Field field : Person.class.getDeclaredFields()) {
            if (field.getName().equals("age")) {
                ageOffsetByActual = unsafe.objectFieldOffset(field);
            }
            log.info("{}:对应的内存偏移地址:{}", field.getName(), unsafe.objectFieldOffset(field));
        }

        //  通过内存偏移地址修改age的值
        //  错误的内存地址
        log.info("custom offset result:{}", unsafe.compareAndSwapInt(person, ageOffsetByCustom, person.getAge(), changeValue));

        //  内存中真正存的值和逻辑上的原值不相等（大部分cas失败的情况都是这种情况）
        int illegalAgeExpectValue = person.getAge();
        person.setAge(100);
        log.info("custom expect result:{}", unsafe.compareAndSwapInt(person, ageOffsetByActual, illegalAgeExpectValue, changeValue));

        /**
         * ageOffsetByActual:内存中真正存的值
         * 40:逻辑上的原值
         * 100:要写入的新值
         * 通过循环检查内存中的值是不是原来的值，以此来判断是不是正有其他线程在改变它
         *
         */
        log.info("actual result:{}", unsafe.compareAndSwapInt(person, ageOffsetByActual, person.getAge(), changeValue));
        log.info("age修改后的值:{},地址:{}", person.getAge(), unsafe.objectFieldOffset(Person.class.getDeclaredField("age")));
    }
}

@Data
class Person {

    private int age;

    private String name;

    private Person() {
    }
}
