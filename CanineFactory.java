package assignment_1.assignment_1;

// Factory for creating Canine veterinarians
public class CanineFactory implements VeterinarianFactory {
    @Override
    public Veterinarian createVeterinarian(String name) {
        return new Canine(name); // Create an instance of Canine veterinarian
    }
}
