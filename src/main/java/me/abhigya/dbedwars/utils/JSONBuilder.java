package me.abhigya.dbedwars.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.Abhigya.core.util.json.Json;
import org.jetbrains.annotations.NotNull;

public class JSONBuilder implements Cloneable {

    Json json;

    public JSONBuilder() {
        json = Json.getNew();
    }

    public JSONBuilder(Json json) {
        this.json = json;
    }

    public JSONBuilder add(String key, Object value) {

        if (json.has(key)) throw new IllegalArgumentException("field" + key + "is already present");

        return adder(key, value);
    }

    public JSONBuilder addIfNotExists(String key, Object value) {
        if (json.has(key)) return this;

        return adder(key, value);
    }

    public JSONBuilder edit(String key, Object value, Class<?> clazz) {

        if (!json.has(key))
            throw new IllegalArgumentException("No field found with" + key + "found");

        return editor(key, value, clazz);
    }

    public JSONBuilder editIfExists(String key, Object value, Class<?> clazz) {
        if (!json.has(key)) return this;

        return editor(key, value, clazz);
    }

    public JSONBuilder addNewList(String key, JsonObject... objects) {

        if (json.has(key)) throw new IllegalArgumentException("field" + key + "already exists");

        JsonArray array = new JsonArray();
        for (JsonObject object : objects) {
            array.add(object);
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewListIfNotExists(String key, JsonObject... objects) {

        if (json.has(key)) return this;

        JsonArray array = new JsonArray();
        for (JsonObject object : objects) {
            array.add(object);
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder clearList(String key) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");
        json.remove(key);
        json.add(key, new JsonArray());
        return this;
    }

    public JSONBuilder clearListIfExists(String key) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;
        json.remove(key);
        json.add(key, new JsonArray());
        return this;
    }

    public JSONBuilder addNewList(String key, String... strings) {
        if (json.has(key)) throw new IllegalArgumentException("field" + key + "already exists");
        JsonArray array = new JsonArray();
        for (String s : strings) {
            array.add(new JsonPrimitive(s));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewListIfNotExists(String key, String... strings) {
        if (json.has(key)) return this;
        JsonArray array = new JsonArray();
        for (String s : strings) {
            array.add(new JsonPrimitive(s));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewList(String key, Number... numbers) {
        if (json.has(key)) throw new IllegalArgumentException("field" + key + "already exists");
        JsonArray array = new JsonArray();
        for (Number number : numbers) {
            array.add(new JsonPrimitive(number));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewListIfNotExists(String key, Number... numbers) {
        if (json.has(key)) return this;
        JsonArray array = new JsonArray();
        for (Number number : numbers) {
            array.add(new JsonPrimitive(number));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewList(String key, Boolean... booleans) {
        if (json.has(key)) throw new IllegalArgumentException("field" + key + "already exists");
        JsonArray array = new JsonArray();
        for (Boolean bool : booleans) {
            array.add(new JsonPrimitive(bool));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewListIfNotExist(String key, Boolean... booleans) {
        if (json.has(key)) return this;
        JsonArray array = new JsonArray();
        for (Boolean bool : booleans) {
            array.add(new JsonPrimitive(bool));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewList(String key, Character... characters) {
        if (json.has(key)) throw new IllegalArgumentException("field" + key + "already exists");
        JsonArray array = new JsonArray();
        for (Character character : characters) {
            array.add(new JsonPrimitive(character));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addNewListIfNotExists(String key, Character... characters) {
        if (json.has(key)) return this;
        JsonArray array = new JsonArray();
        for (Character character : characters) {
            array.add(new JsonPrimitive(character));
        }
        json.add(key, array);
        return this;
    }

    public JSONBuilder addToExistingList(String key, JsonObject... objects) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");

        for (JsonObject object : objects) {
            json.getAsJsonArray(key).add(object);
        }

        return this;
    }

    public JSONBuilder addToExistingListIfExists(String key, JsonObject... objects) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;

        for (JsonObject object : objects) {
            json.getAsJsonArray(key).add(object);
        }

        return this;
    }

    public JSONBuilder addToExistingList(String key, String... strings) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");

        for (String s : strings) {
            json.getAsJsonArray(key).add(new JsonPrimitive(s));
        }

        return this;
    }

    public JSONBuilder addToExistingListIfExists(String key, String... strings) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;

        for (String s : strings) {
            json.getAsJsonArray(key).add(new JsonPrimitive(s));
        }

        return this;
    }

    public JSONBuilder addToExistingList(String key, Number... numbers) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");

        for (Number number : numbers) {
            json.getAsJsonArray(key).add(new JsonPrimitive(number));
        }

        return this;
    }

    public JSONBuilder addToExistingListIfExists(String key, Number... numbers) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;

        for (Number number : numbers) {
            json.getAsJsonArray(key).add(new JsonPrimitive(number));
        }

        return this;
    }

    public JSONBuilder addToExistingList(String key, Boolean... bools) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");

        for (Boolean bool : bools) {
            json.getAsJsonArray(key).add(new JsonPrimitive(bool));
        }

        return this;
    }

    public JSONBuilder addToExistingListIfExists(String key, Boolean... bools) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;

        for (Boolean bool : bools) {
            json.getAsJsonArray(key).add(new JsonPrimitive(bool));
        }

        return this;
    }

    public JSONBuilder addToExistingList(String key, Character... chars) {
        if (!json.has(key)) throw new IllegalArgumentException("field" + key + "does not exist");
        if (!json.get(key).isJsonArray())
            throw new IllegalArgumentException("field" + key + "is not an array");

        for (Character c : chars) {
            json.getAsJsonArray(key).add(new JsonPrimitive(c));
        }

        return this;
    }

    public JSONBuilder addToExistingListIfExists(String key, Character... chars) {
        if (!json.has(key)) return this;
        if (!json.get(key).isJsonArray()) return this;

        for (Character c : chars) {
            json.getAsJsonArray(key).add(new JsonPrimitive(c));
        }

        return this;
    }

    public JSONBuilder remove(String key, Class<?> clazz) {
        if (!json.has(key)) throw new IllegalArgumentException("No field found with" + key + "key");

        return remover(key, clazz);
    }

    public JSONBuilder removeIfExists(String key, Class<?> clazz) {
        if (!json.has(key)) return this;

        return remover(key, clazz);
    }

    @NotNull
    private JSONBuilder remover(String key, Class<?> clazz) {
        if (clazz.equals(String.class)) {
            json.remove(key);
        } else if (clazz.equals(Number.class)) {
            json.remove(key);
        } else if (clazz.equals(Boolean.class)) {
            json.remove(key);
        } else if (clazz.equals(Character.class)) {
            json.remove(key);
        } else if (clazz.equals(JsonElement.class)) {
            json.remove(key);
        }

        return this;
    }

    @NotNull
    private JSONBuilder editor(String key, Object value, Class<?> clazz) {
        if (clazz.equals(String.class)) {
            json.remove(key);
            json.addProperty(key, (String) value);
        } else if (clazz.equals(Number.class)) {
            json.remove(key);
            json.addProperty(key, (Number) value);
        } else if (clazz.equals(Boolean.class)) {
            json.remove(key);
            json.addProperty(key, (Boolean) value);
        } else if (clazz.equals(Character.class)) {
            json.remove(key);
            json.addProperty(key, (Character) value);
        } else if (clazz.equals(JsonElement.class)) {
            json.remove(key);
            json.add(key, (JsonElement) value);
        }

        return this;
    }

    @NotNull
    private JSONBuilder adder(String key, Object value) {
        if (value instanceof String) {
            json.addProperty(key, (String) value);
        } else if (value instanceof Number) {
            json.addProperty(key, (Number) value);
        } else if (value instanceof Boolean) {
            json.addProperty(key, (Boolean) value);
        } else if (value instanceof Character) {
            json.addProperty(key, (Character) value);
        } else if (value instanceof JsonElement) {
            json.add(key, (JsonElement) value);
        } else {
            json.addProperty(key, value.toString());
        }
        return this;
    }

    public String toString() {
        return json.toString();
    }

    public Json getRawJson() {
        return json;
    }

    @Override
    public JSONBuilder clone() {
        try {
            return (JSONBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
