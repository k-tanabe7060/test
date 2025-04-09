package com.freeks.training.stockSystem.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.freeks.training.stockSystem.entity.LoginInfoEntity;

//SpringでUser情報を扱う為のクラス
public class UserPrincipal implements UserDetails {

	private LoginInfoEntity loginInfoEntity;
	
	public UserPrincipal(LoginInfoEntity loginInfoEntity) {
		this.loginInfoEntity = loginInfoEntity;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO 自動生成されたメソッド・スタブ
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		// TODO 自動生成されたメソッド・スタブ
		return loginInfoEntity.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO 自動生成されたメソッド・スタブ
		return loginInfoEntity.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
    // アカウントがロックされていないことを示すために、常にtrueを返します。
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 資格情報（ここではパスワード）が有効期限切れでないことを示すために、常にtrueを返します。
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // アカウントが有効であることを示すために、常にtrueを返します。
    @Override
    public boolean isEnabled() {
        return true;
    }
}
