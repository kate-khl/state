package com.example.state;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Component
public class ProtoClassSerializer implements VariableSerializer<Object>{

	private final static ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Override
	public JsonNode serialize(Object val) {
		return objectMapper.valueToTree(val);
	}

	@Override
	@SneakyThrows
	public Object deSerialize(JsonNode node) {
		return objectMapper.treeToValue(node, ProtoClass.class);
	}

	@Override
	public boolean canSerialize(Class<?> clazz) {
		return clazz.isAssignableFrom(ProtoClass.class);
	}

}
