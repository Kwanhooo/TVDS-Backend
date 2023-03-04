package org.csu.tvds.core.io;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Template {
    protected String template;
    protected String[] values;

    public Template(String template) {
        this.template = template;
    }

    public Template(String template, String... values) {
        this.template = template;
        this.values = values;
    }

    public String resolve() {
        String result = template;
        for (int i = 0; i < values.length; i++) {
            result = result.replace("{" + i + "}", values[i]);
        }
        return result;
    }
}
