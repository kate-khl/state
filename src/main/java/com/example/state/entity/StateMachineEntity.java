package com.example.state.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.example.state.StateApplication.States;
import com.example.state.entity.type.JsonField;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter @Setter
@Accessors(chain = true)
@Table(schema = "public", name = "state_machine2")
public class StateMachineEntity {

	@Id
	private String id;
	@Enumerated(EnumType.STRING)
	private States state;
	@Type(type = "json")
	private JsonField variables;
	
	@Lob
	private byte[] stateMachineContext;
	private boolean locked;
	
}