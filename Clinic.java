package assignment_1.assignment_1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

class Clinic {
    private String name;
    private final List<Pet> pets;
    final Map<String, PriorityQueue<Veterinarian>> vetMap;
    private final Object lock = new Object();
    private static final Logger logger = Logger.getLogger(Clinic.class.getName()); // Logger for monitoring

    public Clinic(String name) {
        this.name = name;
        this.vetMap = new ConcurrentHashMap<>(); // Thread-safe map for veterinarians
        this.pets = Collections.synchronizedList(new ArrayList<>()); // Synchronized list for pets
    }

    // Synchronized method to add a vet
    public void addVet(String name, String dep) {
        Veterinarian vet = createDoctor(name, dep); // Create veterinarian using the factory pattern
        if (vet != null) {
            synchronized (lock) {
                vetMap.computeIfAbsent(dep, k -> new PriorityQueue<>(Comparator.comparing(Veterinarian::getAvailability)))
                      .add(vet); // Add veterinarian to the priority queue for the specific department
                logger.info("Added veterinarian: " + vet.getName() + " to department: " + dep);
            }
        } else {
            logger.warning("Failed to add veterinarian: " + name + " due to invalid department: " + dep);
        }
    }

    // Factory method to create a veterinarian based on the department
    private Veterinarian createDoctor(String name, String dep) {
        VeterinarianFactory factory; // Declare a factory variable
        switch (dep) {
            case "avian":
                factory = new AvianFactory(); // Use AvianFactory for avian veterinarians
                break;
            case "feline":
                factory = new FelineFactory(); // Use FelineFactory for feline veterinarians
                break;
            case "canine":
                factory = new CanineFactory(); // Use CanineFactory for canine veterinarians
                break;
            default:
                logger.warning("Invalid department specified."); // Log invalid department
                return null;
        }
        return factory.createVeterinarian(name); // Use the factory to create a veterinarian
    }

    // Synchronized method to book an appointment
    public void bookAppointment(String name, int age, String vetType) {
        synchronized (lock) {
            PriorityQueue<Veterinarian> vets = vetMap.get(vetType);
            if (vets != null && !vets.isEmpty()) {
                Veterinarian vet = vets.peek(); // Get the veterinarian at the front of the queue

                if (vet != null && vet.getAvailability()) {
                    System.out.println("Appointment scheduled with " + vet.getName() + " for " + name);
                    vet.setAvailability(false); // Mark veterinarian as unavailable

                    Pet pet = new Pet(name, age, Pet.totalPets.get() + 1);
                    addPet(pet); // Add the pet to the clinic
                    vets.poll(); // Remove the vet from the queue
                } else {
                    System.out.println("No available doctor for the specified type: " + vetType + " for " + name);
                }
            } else {
                System.out.println("No doctor available for the specified type: " + vetType + " for " + name);
            }
        }
    }

    // Synchronized method to add a pet
    public void addPet(Pet pet) {
        synchronized (pets) {
            this.pets.add(pet); // Add pet to the synchronized list
        }
    }

    // Synchronized method to print the list of pets
    public void printPets() {
        System.out.println("Pets in " + this.name + ":");
        synchronized (pets) {
            for (Pet pet : pets) {
                try {
                    Thread.sleep(10); // Introduce a short delay for demonstration
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(pet);
            }
        }
    }
}
