 package com.example.state;

import org.springframework.statemachine.StateMachineContext;
//import org.springframework.statemachine.data.RepositoryStateMachinePersist;
//import org.springframework.statemachine.data.StateMachineRepository;

import com.example.state.StateApplication.Events;
import com.example.state.StateApplication.States;

public class MyStateMachinePersist {// extends RepositoryStateMachinePersist<StateMachineEntity, States, Events>{

//	private StateMachineEntityRepository repository;
//	
//	public MyStateMachinePersist(StateMachineEntityRepository repository) {
//		this.repository = repository;
//	}
//	
//	@Override
//	protected StateMachineRepository<StateMachineEntity> getRepository() {
//		return null;
//	}
//
//	@Override
//	protected StateMachineEntity build(StateMachineContext<States, Events> context, Object contextObj,
//			byte[] serialisedContext) {
//		StateMachineEntity stateMachineEntity = new StateMachineEntity();
//		stateMachineEntity.setMachineId(context.getId());
//		stateMachineEntity.setState(context.getState() != null ? context.getState().toString() : null);
//		stateMachineEntity.setStateMachineContext(serialisedContext);
//		return stateMachineEntity;
//	}

}