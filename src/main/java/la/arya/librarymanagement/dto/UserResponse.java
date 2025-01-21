package la.arya.librarymanagement.dto;

import lombok.Data;

@Data
public class UserResponse {

    private String hashId;

    private String firstName;

    private String lastName;

    private String email;
}
