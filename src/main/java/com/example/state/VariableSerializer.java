package com.example.state;

import com.fasterxml.jackson.databind.JsonNode;

public interface VariableSerializer<T> {
	JsonNode serialize(T val);
	T deSerialize(JsonNode node);
	boolean canSerialize(Class<?> clazz);
}
