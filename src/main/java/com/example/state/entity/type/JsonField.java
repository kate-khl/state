package com.example.state.entity.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JsonField implements Serializable{
	private static final long serialVersionUID = 1L;
	private Map<String, JsonNode> vars = new HashMap<>();
}
