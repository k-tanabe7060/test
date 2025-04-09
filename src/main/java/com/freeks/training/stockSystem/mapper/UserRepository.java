package com.freeks.training.stockSystem.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.freeks.training.stockSystem.entity.LoginInfoEntity;

@Repository
public interface UserRepository extends JpaRepository<LoginInfoEntity, Long> {
	
	//ユーザーネーム検索
	LoginInfoEntity findByUsername(String username);
	
}
