package org.zibble.dbedwars.utils.json;

/** Represents options for Json. */
public class JsonOptions {

    private boolean html_safe;
    private String indent;
    private boolean serialize_nulls;

    /** Constructs the {@link JsonOptions}. */
    public JsonOptions() {
        this.html_safe = false;
        this.indent = null;
        this.serialize_nulls = true;
    }

    /**
     * Returns the html safe option.
     *
     * <p>
     *
     * @return Html safe option
     */
    public boolean htmlSafe() {
        return html_safe;
    }

    /**
     * Sets the html safe option.
     *
     * <p>
     *
     * @param html_safe Option value
     * @return This Object, for chaining
     */
    public JsonOptions htmlSafe(boolean html_safe) {
        this.html_safe = html_safe;
        return this;
    }

    /**
     * Returns the indent of the Json.
     *
     * <p>
     *
     * @return Indent of Json
     */
    public String indent() {
        return (indent == null ? new String() : indent);
    }

    /**
     * Sets the indent of the Json.
     *
     * <p>
     *
     * @param indent Indent to set
     * @return This Object, for chaining
     */
    public JsonOptions indent(String indent) {
        this.indent = ((indent != null && indent.length() == 0) ? null : indent);
        return this;
    }

    /**
     * Returns the Serialize Nulls option.
     *
     * <p>
     *
     * @return Serialize Nulls
     */
    public boolean serializeNulls() {
        return serialize_nulls;
    }

    /**
     * Sets the Serialize Nulls option.
     *
     * <p>
     *
     * @param serialize_nulls Option value
     * @return This Object, for chaining
     */
    public JsonOptions serializeNulls(boolean serialize_nulls) {
        this.serialize_nulls = serialize_nulls;
        return this;
    }
}
