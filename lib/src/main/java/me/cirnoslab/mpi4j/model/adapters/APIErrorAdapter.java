package me.cirnoslab.mpi4j.model.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import me.cirnoslab.mpi4j.model.APIError;

import java.lang.reflect.Type;

public class APIErrorAdapter implements JsonDeserializer<APIError> {
    @Override
    public APIError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        int code = json.getAsInt();
        for (APIError error : APIError.values()) {
            if (error.code() == code) return error;
        }
        return null;
    }
}
