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
package comum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Realiza todas as operações de transformação do objeto, seja em Json ou Strem.
 *
 * @author ffmj
 */
public class Marshaller {

    // Aqui é como as operações são tratadas dos dois lados
    static final String write = "w";
    static final String read = "r";
    static final String take = "t";
    static final String space = "s";
    private Gson gson;

    /**
     * Cria um gson que vai ser responsável pela maioria das conversões
     */
    public Marshaller() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    /**
     * Gera um json adicionado de uma letra (w,r,t) que indica qual é a operação
     * a ser realizada.
     *
     * @param o O objeto a ser enviado
     * @param operation Write, Read, Take
     * @return Um jason com uma letra (w,r,t) adicionada ao início
     */
    public String marshall(Object o, String operation) {
        String in = gson.toJson(o);
        String out = operation.substring(0, 1) + in;
        return out;
    }

    /**
     * Gera um json do objeto a ser enviado para o space
     *
     * @param obj O objeto a ser serializado
     * @return Um JSON
     */
    public String marshall(Object obj) {
        Gson g = new Gson();
        String json = g.toJson(obj);
        return json;

    }

    /**
     * Gera uma instância de objeto usando o recipiente do objeto enviado e a
     * JSON retornado do SPACE.
     *
     * @param obj O objeto enviado
     * @param json A JSON que chega so Space
     * @return Uma instância do mesmo objeto enviado
     */
    public Object unmarshall(Object obj, String json) {
        Object ret = gson.fromJson(json, obj.getClass());
        return ret;
    }

    /**
     * Retira a letra indicativa de operação do json. Tem que melhorar para que
     * ele só tire se for (w,r,t)
     *
     * @param json Um JSON com letra inicial (w,r,t)
     * @return Um JSON sem a letra (w,r,t)
     */
    public Object removeOperationLetter(String json) {
        String ret = json;
        ret = ret.substring(1, ret.length());
        return ret;
    }

    /**
     * Pega a letra inicial que indica a operação a ser realizada com o JSON
     * para que ela seja utilizada em algum procedimento
     *
     * @param operationJson Um json com a letra de operação
     * @return Uma letra que indica a operação a ser realizada.
     */
    public String getJsonOperation(String operationJson) {
        String ret = operationJson;
        ret = ret.substring(0, 1);
        return ret;
    }

    /**
     * Pega a letra indicativa de operação na mensagem para que ela seja usada
     * em algum procedimento.
     *
     * @param json Um json iniciado com a letra de operação
     * @return
     */
    public String useOperationLetter(String json) {
        String ret = json.substring(0, 1);
        return ret;
    }

    /**
     * Retira bytes nulos da mensagem.
     *
     * @param json Um json recebido por UDP
     * @return Um json sem caracteres nulos
     */
    public String cleanMsg(String json) {
        char c = 0;
        return json.replaceAll(String.valueOf(c), "");
    }

}
