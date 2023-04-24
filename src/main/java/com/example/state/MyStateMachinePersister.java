package com.example.state;

import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.AbstractStateMachinePersister;

import com.example.state.StateApplication.Events;
import com.example.state.StateApplication.States;

public class MyStateMachinePersister extends AbstractStateMachinePersister<States, Events, String> {

	public MyStateMachinePersister(StateMachinePersist<States, Events, String> stateMachinePersist) {
		super(stateMachinePersist);
	}

}
