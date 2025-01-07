package la.arya.librarymanagement.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ApiResponse {
    private String message;
    private Object data;

    public ApiResponse(String message,Object data) {
        this.message = message;
        this.data = data;
    }

}
