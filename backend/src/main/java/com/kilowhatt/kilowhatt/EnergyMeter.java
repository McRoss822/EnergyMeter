package com.kilowhatt.kilowhatt;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "energies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnergyMeter {
    @Id
    private ObjectId id;
    private String value;
    private String timeStamp;

    public EnergyMeter(String value, String timeStamp){
        this.value = value;
        this.timeStamp = timeStamp;
    }

    public String getValue() {
        return this.value;
    }
}
