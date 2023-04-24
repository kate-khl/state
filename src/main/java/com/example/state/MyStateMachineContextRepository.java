package com.example.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineContextRepository;
import org.springframework.statemachine.kryo.KryoStateMachineSerialisationService;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import com.example.state.StateApplication.Events;
import com.example.state.StateApplication.States;
import com.example.state.entity.StateMachineEntity;
import com.example.state.entity.type.JsonField;
import com.example.state.repository.StateMachineEntityRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MyStateMachineContextRepository implements StateMachineContextRepository<States, Events, StateMachineContext<States, Events>>{

	private final StateMachineEntityRepository repo;
	private final List<VariableSerializer<Object>> serializers;
	private KryoStateMachineSerialisationService<States, Events> serializer = new KryoStateMachineSerialisationService<>();
	
	private final static ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Override
	public void save(StateMachineContext<States, Events> context, String id) {
		try {
			StateMachineEntity entity = new StateMachineEntity();
			entity.setId(id);
			entity.setState(context.getState() != null ? context.getState() : null);
			Map<Object, Object> allVariables = context.getExtendedState().getVariables();
			entity.setVariables(mapToJsonField(allVariables));
			context.getExtendedState().getVariables().clear();
			byte[] bytes = serializer.serialiseStateMachineContext(context);
			entity.setStateMachineContext(bytes);
			repo.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private JsonField mapToJsonField(Map<Object, Object> smVars){
		Map<String, JsonNode> vars = new HashMap<>();
		smVars.forEach((k, v) -> {
			JsonNode node = null;
			VariableSerializer<Object> serializer = serializers.stream()
					.filter(s -> s.canSerialize(v.getClass())).findFirst().orElse(null);
			if(serializer == null) {
				node = objectMapper.valueToTree(v);
			} else {
				node = serializer.serialize(v);
			}
			vars.put(k.toString(), node);
		});
		return new JsonField().setVars(vars);
	} 

	private Map<Object, Object> mapToSmVars(JsonField field){
		Map<Object, Object> vars = new HashMap<>();
		Map<String, JsonNode> persistedVars = field.getVars();
		persistedVars.forEach((k, v) -> {
			if(v.isObject()) {
				Object deserialised = v;
				String className = v.get("@class").asText();
				if(!className.isEmpty()) {
					try {
						Class<?> clazz = Class.forName(className);
						VariableSerializer<Object> serializer = serializers.stream().filter(s -> s.canSerialize(clazz)).findFirst().orElse(null);
						if(serializer != null) {
							deserialised = serializer.deSerialize(v);
						}
					} catch (ClassNotFoundException ingnore) {}
				}
				vars.put(k, deserialised);
			}
		});
		vars.putAll(vars);
		return vars; 
	} 

	@Override
	@SneakyThrows
	public StateMachineContext<States, Events> getContext(String id) {
		StateMachineEntity entity = repo.findById(id).orElse(null);
		
		if (entity != null) {
			StateMachineContext<States, Events> context = serializer
					.deserialiseStateMachineContext(entity.getStateMachineContext());
			Map<Object, Object> vars = context.getExtendedState().getVariables();
			vars.putAll(mapToSmVars(entity.getVariables()));
			if (context != null && context.getChilds() != null && context.getChilds().isEmpty()
					&& context.getChildReferences() != null) {
				List<StateMachineContext<States, Events>> contexts = new ArrayList<>();
				for (String childRef : context.getChildReferences()) {
					entity = repo.findById(childRef).orElse(null);
					if (entity != null) {
						StateMachineContext<States, Events> childContext = serializer.deserialiseStateMachineContext(entity.getStateMachineContext());
						Map<Object, Object> childVars = childContext.getExtendedState().getVariables();
						childVars.putAll(mapToSmVars(entity.getVariables()));
						contexts.add(childContext);
					}
				}
				return new DefaultStateMachineContext<States, Events>(contexts, entity.getState(), context.getEvent(),
						context.getEventHeaders(), context.getExtendedState(), context.getHistoryStates(),
						context.getId());
			} else {
				return context;
			}

		}
		return null;
	}

}
