package today.devstudy.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private int status = 200;
    private String message = "ok";
    private String code = "200";
    private Object data = "no data";
}
