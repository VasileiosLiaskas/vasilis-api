package vasilis.vasilis.general;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date != null) {
            // Format the date as a string and write it to the JSON output
            String formattedDate = dateFormat.format(date);
            jsonGenerator.writeString(formattedDate);
        } else {
            // Handle null date if necessary, you could write null, or skip it
            jsonGenerator.writeNull();
        }
    }
}
