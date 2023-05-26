package ProiectMPP.Model.Validators;

/**
 * Interface of the validator for any given datatype
 */
public interface IChildValidator {
    void validate(String firstName, String lastName, String age) throws ValidationException;
}
