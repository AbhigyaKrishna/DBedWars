package org.zibble.dbedwars.utils.json;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.pepedevs.radium.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.util.Map;
import java.util.Set;

/** Represents a utility class for handling Json. */
public final class Json {

    private final JsonObject handle;
    private final Json root;
    private final JsonOptions options;

    /**
     * Constructs a new instance of {@link Json}.
     *
     * <p>
     *
     * @param handle {@link JsonObject} to handle
     * @param root Root Json Object
     */
    private Json(JsonObject handle, Json root) {
        this.handle = handle;
        this.root = root;
        this.options = root.getOptions();
    }

    /**
     * Constructs a new instance of {@link Json}.
     *
     * <p>
     *
     * @param handle {@link JsonObject} to handle
     */
    private Json(JsonObject handle) {
        this.handle = handle;
        this.root = this;
        this.options = new JsonOptions();
    }

    /**
     * Loads a File to Json Object.
     *
     * <p>
     *
     * @param json_file File to parse to Json
     * @param check_encrypted Whether to check encryption
     * @return This class instance
     * @throws MalformedJsonException if the json file is corrupted or cannot be parsed
     */
    public static Json load(File json_file, boolean check_encrypted) throws MalformedJsonException {
        try {
            StringBuilder contents = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(json_file));
            reader.lines()
                    .forEach(
                            line -> {
                                contents.append(line + StringUtils.LINE_SEPARATOR);
                            });

            reader.close();
            return loadFromString(contents.toString(), check_encrypted);
        } catch (IOException e) {
            throw new MalformedJsonException(e);
        }
    }

    /**
     * Loads a File to Json Object.
     *
     * <p>
     *
     * @param json_file File to parse to Json
     * @return This class instance
     * @throws MalformedJsonException if the json file is corrupted or cannot be parsed
     */
    public static Json load(File json_file) throws MalformedJsonException {
        return load(json_file, true);
    }

    /**
     * Loads a String to Json Object.
     *
     * <p>
     *
     * @param contents String to parse to Json
     * @param check_encrypted Whether to check encryption
     * @return This class instance
     */
    public static Json loadFromString(String contents, boolean check_encrypted) {
        if (check_encrypted) {
            if (StringEscapeUtils.escapeJava(StringUtils.deleteWhitespace(contents))
                    .equals(StringUtils.deleteWhitespace(contents))) { // if encrypted
                contents = new String(Base64.decodeBase64(contents)); // decode!
            }
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(contents);
        if (!element.isJsonObject()) {
            throw new JsonSyntaxException("Illegal syntax!");
        }
        return new Json(element.getAsJsonObject());
    }

    /**
     * Loads a String to Json Object.
     *
     * <p>
     *
     * @param contents String to parse to Json
     * @return This class instance
     */
    public static Json loadFromString(String contents) {
        return loadFromString(contents, true);
    }

    /**
     * Creates a new instance with a empty Json Object.
     *
     * <p>
     *
     * @return This class instance
     */
    public static Json getNew() {
        return new Json(new JsonObject());
    }

    /**
     * Returns the {@link JsonObject} handle.
     *
     * <p>
     *
     * @return The handle {@link JsonObject}
     */
    public JsonObject getHandle() {
        return handle;
    }

    /**
     * Returns the {@link Json} root.
     *
     * <p>
     *
     * @return The root {@link Json}
     */
    public Json getRoot() {
        return root;
    }

    /**
     * Returns the {@link JsonOptions} for this Json Object.
     *
     * <p>
     *
     * @return {@link JsonOptions}
     */
    public JsonOptions getOptions() {
        return options;
    }

    /**
     * Creates a new Json Object with this instance as the root.
     *
     * <p>
     *
     * @param name Key of the Json Object
     * @return The new Json Object
     */
    public Json createObject(String name) {
        JsonElement present = handle.get(name);
        if (present != null) {
            if (!present.isJsonObject()) {
                return null;
            }
        } else {
            handle.add(name, (present = new JsonObject()));
        }
        return new Json(present.getAsJsonObject(), this);
    }

    /**
     * Adds a new Json Element to the Json Object.
     *
     * <p>
     *
     * @param property Key of the element
     * @param value Value of the element
     */
    public void add(String property, JsonElement value) {
        getHandle().add(property, value);
    }

    /**
     * Remove the given Json Element from the Json Object.
     *
     * <p>
     *
     * @param property Key of the element
     * @return Json Element removed
     */
    public JsonElement remove(String property) {
        return getHandle().remove(property);
    }

    /**
     * Adds a property to the Json Object.
     *
     * <p>
     *
     * @param property Property key
     * @param value String Value of the property
     */
    public void addProperty(String property, String value) {
        getHandle().addProperty(property, value);
    }

    /**
     * Adds a property to the Json Object.
     *
     * <p>
     *
     * @param property Property key
     * @param value Number Value of the property
     */
    public void addProperty(String property, Number value) {
        getHandle().addProperty(property, value);
    }

    /**
     * Adds a property to the Json Object.
     *
     * <p>
     *
     * @param property Property key
     * @param value Boolean Value of the property
     */
    public void addProperty(String property, Boolean value) {
        getHandle().addProperty(property, value);
    }

    /**
     * Adds a property to the Json Object.
     *
     * <p>
     *
     * @param property Property key
     * @param value Character Value of the property
     */
    public void addProperty(String property, Character value) {
        getHandle().addProperty(property, value);
    }

    /**
     * Returns the entry set of the Json Object.
     *
     * <p>
     *
     * @return Entry set of the Json Object
     */
    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return getHandle().entrySet();
    }

    /**
     * Checks whether the Json Object has the element member.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return true if present, false otherwise
     */
    public boolean has(String memberName) {
        return getHandle().has(memberName);
    }

    /**
     * Returns the element member of Json Object.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return {@link JsonElement} for the given key
     */
    public JsonElement get(String memberName) {
        return getHandle().get(memberName);
    }

    /**
     * Returns the element member of Json Object as Json Primitive.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return {@link JsonPrimitive} for the given key
     */
    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return getHandle().getAsJsonPrimitive(memberName);
    }

    /**
     * Returns the element member of Json Object as Json Array.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return {@link JsonArray} for the given key
     */
    public JsonArray getAsJsonArray(String memberName) {
        return getHandle().getAsJsonArray(memberName);
    }

    /**
     * Returns the element member of Json Object as Json Object.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return {@link JsonObject} for the given key
     */
    public JsonObject getAsJsonObject(String memberName) {
        return getHandle().getAsJsonObject(memberName);
    }

    /**
     * Returns the element member of Json Object as Json.
     *
     * <p>
     *
     * @param memberName Key of the element
     * @return {@link Json} for the given key
     */
    public Json getAsJson(String memberName) {
        return new Json(getAsJsonObject(memberName), this);
    }

    /**
     * Saves the Json to a file.
     *
     * <p>
     *
     * @param json_file File to save to
     * @param encrypt Whether to encrypt data
     * @throws IOException thrown while performing I/O
     */
    public void save(File json_file, boolean encrypt) throws IOException {
        String contents = toString();
        if (encrypt) { // encrypting!
            contents = Base64.encodeBase64String(contents.getBytes());
        }

        FileWriter writer = new FileWriter(json_file);
        writer.append(contents);
        writer.close();
    }

    /**
     * Saves the Json to a file.
     *
     * <p>
     *
     * @param json_file File to save to
     * @throws IOException thrown while performing I/O
     */
    public void save(File json_file) throws IOException {
        save(json_file, false);
    }

    /**
     * Returns the Json as a String.
     *
     * <p>
     *
     * @return Json to string
     */
    @Override
    public String toString() {
        try {
            StringWriter string_writer = new StringWriter();
            JsonWriter json_writer = new JsonWriter(string_writer);
            json_writer.setLenient(true);
            json_writer.setHtmlSafe(getOptions().htmlSafe());
            json_writer.setIndent(getOptions().indent());
            json_writer.setSerializeNulls(getOptions().serializeNulls());

            Streams.write(getHandle(), json_writer);
            return string_writer.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return getHandle().equals(obj);
    }

    @Override
    public int hashCode() {
        return getHandle().hashCode();
    }
}
