package task.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorStatistics implements Serializable {
    private String name;
    private BigInteger recipesNumber;
 }
