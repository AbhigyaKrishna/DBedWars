package org.zibble.dbedwars.api.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JSONBuilder implements Cloneable {

    Json json;

    public JSONBuilder() {
        json = Json.getNew();
    }

    public JSONBuilder(Json json) {
        this.json = json;
    }

    public JSONBuilder(JsonObject json) {
        this.json = Json.of(json);
    }

    public JSONBuilder add(String key, Object value) {
        if (json.has(key))
            throw new IllegalArgumentException("field" + key + "is already present");

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
        } else if (value instanceof Json) {
            json.add(key, ((Json) value).getHandle());
        } else {
            json.addProperty(key, value.toString());
        }
        return this;
    }

    public JSONBuilder addIfNotExists(String key, Object value) {
        if (json.has(key))
            return this;

        return this.add(key, value);
    }

    public JSONBuilder set(String key, Object value, Class<?> clazz) {
        if (!json.has(key))
            throw new IllegalArgumentException("No field found with" + key + "found");

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

    public JSONBuilder setIfExists(String key, Object value, Class<?> clazz) {
        if (!json.has(key)) return this;

        return this.set(key, value, clazz);
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

    public JSONBuilder remove(String key) {
        if (!json.has(key))
            throw new IllegalArgumentException("No field found with" + key + "key");

        json.remove(key);
        return this;
    }

    public JSONBuilder removeIfExists(String key) {
        if (!json.has(key)) return this;

        return this.remove(key);
    }

    public String toString() {
        return this.json.toString();
    }

    public Json toJson() {
        return json;
    }

    @Override
    public JSONBuilder clone() {
        return new JSONBuilder(this.json);
    }
}
