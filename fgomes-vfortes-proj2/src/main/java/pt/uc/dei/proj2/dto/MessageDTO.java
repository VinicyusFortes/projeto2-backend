package pt.uc.dei.proj2.dto;

import jakarta.json.JsonObject;

/**
* Esta classe serve para criar um DTO chamado message por exemplo, que será usado para inserir as mensagens de resposta aos pedidos
 **/

public class MessageDTO {
    private String message;
    private JsonObject json;

    public MessageDTO(String message, JsonObject jsonObject) {
        this.message = message;
        this.json = jsonObject;
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
