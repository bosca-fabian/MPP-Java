package ProiectMPP.Model.Validators;

public class ChildValidator implements IChildValidator{

    @Override
    public void validate(String firstName, String lastName, String age) throws ValidationException {
        if(firstName.matches(".*[0-9].*"))
            throw new ValidationException("First name cannot contain numbers");
        if(lastName.matches(".*[0-9].*"))
            throw new ValidationException("Last name cannot contain numbers");
        if(age.matches(".*[a-zA-Z].*"))
            throw new ValidationException("Age cannot contain letters");
    }
}
