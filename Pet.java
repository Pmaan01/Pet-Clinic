package assignment_1.assignment_1;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class Pet {
    private String name;
    private volatile int age;
    private int code;
    public static AtomicInteger totalPets = new AtomicInteger(0); // Atomic counter for total pets

    

    public Pet(String name, int age, int code) {
        this.name = name;
        this.age = age;
        this.code = code;
        totalPets.incrementAndGet(); // Atomically incrementing the counter
        RandomSleep();
 
    }

    public String getName() {
        return name;
    }

    public void setAge(int age)
    {
    	this.age = age;
    }
    public int getAge() {
        return age;
    }

    public int getCode() {
        return code;
    }

    public static int getTotalPets() {
        return totalPets.get(); // Return the current count of pets
    }

    public String toString() {
        return ("Pet:-\n\tName: " + name + "\n\tAge: " + age);
    }

    public static void RandomSleep() {
        Random random = new Random();
        int sleepTime = random.nextInt(30) + 1; // Random sleep time between 1 and 30 milliseconds
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

