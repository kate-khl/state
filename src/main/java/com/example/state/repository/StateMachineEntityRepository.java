package com.example.state.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.state.entity.StateMachineEntity;

public interface StateMachineEntityRepository extends JpaRepository<StateMachineEntity, String>{

}