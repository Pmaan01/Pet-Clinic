package assignment_1.assignment_1;

// Factory for creating Avian veterinarians
public class AvianFactory implements VeterinarianFactory {
    @Override
    public Veterinarian createVeterinarian(String name) {
        return new Avian(name); // Create an instance of Avian veterinarian
    }
}
