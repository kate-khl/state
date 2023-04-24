package com.example.state;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
//import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
//import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
//import org.springframework.statemachine.data.jpa.JpaStateRepository;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.state.StateApplication.Events;
import com.example.state.StateApplication.States;
//import com.example.state.repository.ARepository;
import com.example.state.repository.StateMachineEntityRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequiredArgsConstructor
public class Controller {

	private final StateMachineFactory<States, Events> factory;
	private final StateMachinePersister<States, Events, String> persister;
	private final StateMachineEntityRepository stateMachineEntityRepository;
	
	@Data
	static class MyContextObject implements Serializable{
		private static final long serialVersionUID = 1L;
		private int age;
	}
	
	@SneakyThrows
	@GetMapping("/create/{id:\\w+}")
	public void create(@PathVariable String  id) {
		if(stateMachineEntityRepository.existsById(id)) {
			throw new IllegalStateException("sm already exists!");
		} else {
			StateMachine<States, Events> gg = factory.getStateMachine(id);
			gg.getExtendedState().getVariables().put("var", new ProtoClass());
			persister.persist(gg, id);
		}
	}
	
	
	@SneakyThrows
	@GetMapping("/process/{id:\\w+}")
	public void пятница(@PathVariable String  id, @RequestParam Events eve) {
		StateMachine<States, Events> sm = factory.getStateMachine(id);
		persister.restore(sm, id.toString());
		sm.sendEvent(eve);
		persister.persist(sm, id);
	}
	
}
