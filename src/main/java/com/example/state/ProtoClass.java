package com.example.state;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
@JsonTypeInfo(include = JsonTypeInfo.As.PROPERTY ,use = JsonTypeInfo.Id.CLASS, visible = false)
public class ProtoClass {
	private String name = "лапка";
}