package assignment_1.assignment_1;

// Abstract class representing a Veterinarian
abstract class Veterinarian {
    private String name;
    private boolean availability;  // Availability status of the veterinarian

    public Veterinarian(String name) {
        this.name = name;
        availability = true; // Initialize availability to true
    }

    /* Getters */
    public String getName() {
        return name; // Get the name of the veterinarian
    }

    public synchronized boolean getAvailability() {
        return availability; // Get the availability status
    }

    public synchronized void setAvailability(boolean availability) {
        this.availability = availability; // Set the availability status
    }

    // Override toString method for better readability
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle()).append(":\n\tName: ").append(this.getName()).append("\n\tAvailable: ").append(this.getAvailability());
        return sb.toString();
    }

    // Abstract method to get the title of the veterinarian
    abstract public String getTitle();
}
