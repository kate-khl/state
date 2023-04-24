package com.example.state;

import java.util.EnumSet;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachine;
//import org.springframework.statemachine.data.jpa.JpaRepositoryStateMachinePersist;
//import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;

import com.example.state.StateApplication.Events;
import com.example.state.StateApplication.States;
import com.example.state.repository.StateMachineEntityRepository;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

	
	@Bean
	public StateMachinePersister<States, Events, String> presister(StateMachineEntityRepository repo, List<VariableSerializer<Object>> serializers) {
		return new MyStateMachinePersister(new RepositoryStateMachinePersist<States, Events>(new MyStateMachineContextRepository(repo, serializers)));
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
		config.withConfiguration().autoStartup(true).listener(listener());
	}

	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states.withStates().initial(States.КОВЫРЯЮ_В_НОСУ).states(EnumSet.allOf(States.class));
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions.withExternal()
			.source(States.ПЬЮ_ПИВО)
			.target(States.КОВЫРЯЮ_В_НОСУ)
			.event(Events.НАСТУПИЛ_ПОНЕДЕЛЬНИК)
			.and().withExternal()
			.source(States.КОВЫРЯЮ_В_НОСУ)
			.target(States.РАБОТАЮ)
			.event(Events.НАСТУПИЛ_ВТОРНИК)
			.and().withExternal()
			.source(States.РАБОТАЮ)
			.target(States.ПЬЮ_ПИВО)
			.event(Events.НАСТУПИЛА_ПЯТНИЦА);
	}

	@Bean
	public StateMachineListener<States, Events> listener() {
		return new StateMachineListenerAdapter<States, Events>() {
			@Override
			public void stateChanged(State<States, Events> from, State<States, Events> to) {
				System.out.println("State change to " + to.getId());
			}
		};
	}
}
