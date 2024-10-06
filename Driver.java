package assignment_1.assignment_1;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Driver {

    private static final Logger logger = Logger.getLogger(Driver.class.getName());

    public static void main(String[] args) {
        // Log starting point
        logger.info("Starting the Driver application...");

        // 1. Show how creating pets at the same time can lead to incorrect counting
        demonstrateConcurrentPetCreation();

        // 2. Show that adding pets and vets at the same time can cause duplicates
        demonstrateClinicOperations();

        // 3. Show that booking appointments at the same time can lead to double bookings
        demonstrateConcurrentBooking();

        // 4. Show what happens when one thread updates a pet's age while another reads it
        demonstrateConcurrentPetAgeAccess();
        
        // 5. Show that printing pets while modifying the list can lead to exceptions
        demonstrateConcurrentPrintingPets();
    }

    // 1. Example of creating pets at the same time
    private static void demonstrateConcurrentPetCreation() {
        Runnable petCreationTask = () -> {
            new Pet("Pet", 3, Pet.getTotalPets() + 1);
            logger.info("Created a new pet.");
        };

        // Create threads to simulate creating pets at the same time
        Thread thread1 = new Thread(petCreationTask);
        Thread thread2 = new Thread(petCreationTask);

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to finish
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.severe("Thread interrupted during pet creation: " + e.getMessage());
        }

        // Show the total number of pets created
        logger.info("Total pets created: " + Pet.getTotalPets());
    }
    
    private static void demonstrateClinicOperations() {
        Clinic clinic = new Clinic("Medical Clinic");
        logger.info("Demonstrating clinic operations...");

        // Create a runnable to add pets
        Runnable addPetsTask = () -> {
            for (int j = 0; j < 10; j++) {
                Pet pet = new Pet("Pet" + j, 5, 1);
                clinic.addPet(pet);
              
            }
        };

        // Create a runnable to add vets
        Runnable addVetsTask = () -> {
            for (int j = 0; j < 10; j++) {
                clinic.addVet("Vet" + j, "canine");
          
            }
        };

        // Create and start threads for adding pets and vets
        Thread petThread = new Thread(addPetsTask);
        Thread vetThread = new Thread(addVetsTask);
        
        petThread.start();
        vetThread.start();

        // Wait for both threads to finish
        try {
            petThread.join();
            vetThread.join();
        } catch (InterruptedException e) {
            logger.severe("Thread interrupted during clinic operations: " + e.getMessage());
        }

        // Print the final pets and vets
        logger.info("Final Pets:");
        clinic.printPets();
        logger.info("Final Vets:");
        for (Entry<String, PriorityQueue<Veterinarian>> entry : clinic.vetMap.entrySet()) {
            logger.info("Department: " + entry.getKey());
            for (Veterinarian vet : entry.getValue()) {
                logger.info("Vet: " + vet);
            }
        }
    }

    // 3. Example of booking appointments at the same time
    private static void demonstrateConcurrentBooking() {
        Clinic clinic = new Clinic("Medical Clinic");

        // Add a veterinarian
        clinic.addVet("John Watson", "canine");
        logger.info("Added vet: John Watson");

        // Task to book an appointment
        Runnable bookingTask = () -> {
            Random rand = new Random();
            String petName = "Pet " + rand.nextInt(0, 6); // Create a unique pet name for each thread
            clinic.bookAppointment(petName, 3, "canine");
            logger.info("Booked appointment for: " + petName);
        };

        // Create multiple threads to simulate booking at the same time
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(bookingTask);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                logger.severe("Thread interrupted during booking: " + e.getMessage());
            }
        }
    }

    // 4. Example of one thread updating a pet's age while another reads it
    private static void demonstrateConcurrentPetAgeAccess() {
        Pet myPet = new Pet("Buddy", 5, 1);
        logger.info("Created a pet named Buddy with age: " + myPet.getAge());

        // Task to update the pet's age
        Runnable ageUpdateTask = () -> {
            Random rand = new Random();
            try {
                // Wait for a random time before updating
                Thread.sleep(rand.nextInt(20)); 
                int newAge = myPet.getAge() + 1; // Increase the age
                myPet.setAge(newAge);
                logger.info(Thread.currentThread().getName() + " updated age to: " + newAge);
            } catch (InterruptedException e) {
                logger.severe("Thread interrupted during age update: " + e.getMessage());
            }
        };

        // Task to read the pet's age
        Runnable ageReadTask = () -> {
            try {
                // Wait a bit to read age while another thread might be updating it
                Thread.sleep(20); 
                logger.info(Thread.currentThread().getName() + " read age: " + myPet.getAge());
            } catch (InterruptedException e) {
                logger.severe("Thread interrupted during age read: " + e.getMessage());
            }
        };

        // Create threads for updating and reading age
        Thread updaterThread = new Thread(ageUpdateTask, "UpdaterThread");
        Thread readerThread = new Thread(ageReadTask, "ReaderThread");

        // Start both threads
        updaterThread.start();
        readerThread.start();

        // Wait for both threads to finish
        try {
            updaterThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            logger.severe("Thread interrupted while waiting for updater and reader: " + e.getMessage());
        }
    }
    
    // 5. Example of printing pets while modifying the list causing ConcurrentModificationException
    private static void demonstrateConcurrentPrintingPets() {
        Clinic clinic = new Clinic("Medical Clinic");
        clinic.addPet(new Pet("Buddy", 5, 1));
        clinic.addPet(new Pet("Max", 2, 2));
        logger.info("Added pets: Buddy and Max");

        // Task to print pets
        Runnable printTask = () -> {
            clinic.printPets(); // Attempt to print the list of pets
            logger.info("Printed pets.");
        };

        // Task to modify the pet list
        Runnable modifyTask = () -> {
            clinic.addPet(new Pet("Bella", 4, 3)); // Add a new pet
            logger.info(Thread.currentThread().getName() + " added a pet: Bella.");
        };

        // Create threads for printing and modifying pets
        Thread printerThread = new Thread(printTask, "PrinterThread");
        Thread modifierThread = new Thread(modifyTask, "ModifierThread");

        // Start both threads
        printerThread.start();
        modifierThread.start();

        // Wait for both threads to finish
        try {
            printerThread.join();
            modifierThread.join();
        } catch (InterruptedException e) {
            logger.severe("Thread interrupted during printing and modifying pets: " + e.getMessage());
        }
    }
}
