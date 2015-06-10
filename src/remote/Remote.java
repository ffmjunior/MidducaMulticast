/*
 * Copyright (C) 2015 junior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package remote;

import comum.Marshaller;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import comum.JsonVector;
import comum.Space;

/**
 * Works Space's data.
 *
 * @author ffmj
 */
public class Remote {

    private Marshaller marshaller;
    public static Space space = new Space(100);

    public Remote() {
        marshaller = new Marshaller();
    }

    /**
     * Writes object in Space
     *
     * @param o A object to write
     */
    public void write(Object o) {
        if (verifyJsonVectors(toJsonVector((String) o))) {
            if (space.size() > 0) {
                int i = 0;
                for (Object x : space) {
                    if (o.toString().compareTo(x.toString()) < 0) {
                        i = space.indexOf(x);
                        break;
                    } else {
                        i = space.indexOf(x) + 1;
                    }
                }
                space.insertElementAt(o, i);
            } else {
                space.add(o);
            }
        }
        msg(o.toString());
    }

    /**
     *
     * Search for an object in Space that is compatible with the provided
     * template and if it finds returns a copy .
     *
     * @param o A object to compare
     * @return A copy from matched object.
     */
    public Object read(Object o) {
        String inner = null;
        String ret = null;
        JsonVector in = null;
        JsonVector out = toJsonVector((String) o);
        for (Object json : space) {
            inner = (String) json;
            in = toJsonVector(inner);
            if (matchJsonVectors(in, out)) {
                ret = (String) json;
                break;
            }
        }
        msg(o.toString());
        return ret;
    }

    /**
     * Search for an object in Space that is compatible with the template
     * provided and if it finds , removes the object from the Space and returns
     * it.
     *
     * @param o The object to be matched
     * @return The matched object in space
     *
     */
    public Object take(Object o) {
        String inner = null;
        String ret = null;
        JsonVector in = null;
        JsonVector out = toJsonVector((String) o);
        for (Object star : space) {
            inner = (String) star;
            in = toJsonVector(inner);
            if (matchJsonVectors(in, out)) {
                ret = (String) star;
                if (!space.remove(star)) {
                    System.err.println("Error! Object not removed!");
                }
                break;
            }
        }
        msg(o.toString());
        return ret;
    }

    /**
     * Creates a {@link JsonVector} with name:value. These pairs contain the
     * names and values from atributes. It is the mais responsible for matching
     *
     * @param json The JSON to be transformed in Vector
     * @return A Vector containing name:value pairs
     */
    public JsonVector toJsonVector(String json) {
        String data = json;
        String regex = "(?:,|\\{)?([^:]*):(\"[^\"]*\"|\\{[^}]*\\}|[^},]*)";
        boolean mandatory = false;
        Pattern p = Pattern.compile(regex);
        Matcher match = p.matcher(data);
        JsonVector ret = new JsonVector();
        while (match.find()) {
            String[] pair = new String[2];
            String name = match.group(1);
            String value = match.group(2).toString().trim();
            pair[0] = name;
            pair[1] = value;
            ret.add(pair);
        }

        return ret;
    }

    /**
     * Compare the name: value pairs two JsonVectors. It is understood that, as
     * they are of the same class vectors have the same size.
     *
     * @param inner The {@link JsonVector} from Object in Space
     * @param outer The {@link JsonVector} from queried Object
     * @return True in case of compatible objects
     */
    public boolean matchJsonVectors(JsonVector inner, JsonVector outer) {
        boolean match = false;
        int innerSize = inner.size();
        if (innerSize == outer.size()) {
            String[] in = null;
            String[] out = null;
            int i = 0;
            int matchVarName = 0;
            int matchVarValue = 0;
            int matchNull = 0;
            for (Object o : inner) {
                in = (String[]) o;
                out = (String[]) outer.get(i);
                if (in[0].equalsIgnoreCase(out[0])) {
                    matchVarName++;
                }
                if (in[1].equalsIgnoreCase(out[1])) {
                    matchVarValue++;
                }
                if (out[1].equalsIgnoreCase("null")) {
                    matchNull++;
                }
                i++;
            }
            if (matchVarName == innerSize & (matchNull == innerSize || matchVarName == (matchVarValue + matchNull))) {
                match = true;
            }
        }
        return match;

    }

    /**
     * Check JSON searching for NULLs.
     *
     * @param inner
     * @return True if there's no nulls
     */
    public boolean verifyJsonVectors(JsonVector inner) {
        boolean valid = false;
        int tam = inner.size();
        int verify = 0;
        for (Object o : inner) {
            String[] in = (String[]) o;
            if (in[1].equalsIgnoreCase("null")) {
                verify++;
            }
        }
        if (verify < tam) {
            valid = true;
        }
        return valid;

    }

    @Deprecated
    private void showSpace(Object o) {
        String log = "Objeto [" + space.size() + "] " + o.toString() + " adicionado ao Space\n\nSPACE";
        for (Object star : space) {
            log = log + "\n" + star.toString();
        }
    }

    @Deprecated
    private void showSpace() {
        String log = "";
        for (Object star : space) {
            log = log + star.toString();
        }
    }

    @Deprecated
    private void showJsonSpace() {
        System.out.println(marshaller.marshall(space));
    }

    @Deprecated
    private void msg(String msg) {
        System.out.println(msg);
    }

}
