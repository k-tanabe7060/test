package com.freeks.training.stockSystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity //このクラスはデータベースのテーブルでございます
@Table(name = "users", schema = "stock_system")//DBのusersテーブルと紐づいてます
public class LoginInfoEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番の場合
    @Column(name = "id") // テーブルのカラム名
	private Integer id;
	
	// "username" カラム。各ユーザーのユーザー名を表します。同じ名前のユーザーは存在できません
	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;
	
	
	@Column(name = "logical_delete_flg", nullable = false)
	private boolean logicalDeleteFlg = false;  // 削除フラグのデフォルト値を 0 にする

	@Column(name = "version" , nullable = false)
	@Version
	private Integer version = 1; 
	

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setLogicalDeleteFlg(boolean logicalDeleteFlg) {
	    this.logicalDeleteFlg = logicalDeleteFlg;
	}

	public void setVersion(Integer version) {
	    this.version = version;
	}
}
