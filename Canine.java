package assignment_1.assignment_1;

class Canine extends Veterinarian {

    public Canine(String name){

        super(name);

    } 

    

    @Override

    public String getTitle() {

        return "Canine";

    }

}