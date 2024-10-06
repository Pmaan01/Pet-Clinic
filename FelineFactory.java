package assignment_1.assignment_1;

// Factory for creating Feline veterinarians
public class FelineFactory implements VeterinarianFactory {
    @Override
    public Veterinarian createVeterinarian(String name) {
        return new Feline(name); // Create an instance of Feline veterinarian
    }
}
