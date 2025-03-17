package vasilis.vasilis.general;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String dateStr = jsonParser.getText();

        try {
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            return null; // Handle error (you can throw an exception if you want)
        }
    }
}
