package com.freeks.training.stockSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.freeks.training.stockSystem.entity.LoginInfoDto;
import com.freeks.training.stockSystem.entity.LoginInfoEntity;
import com.freeks.training.stockSystem.mapper.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired // Springが自動的にPasswordEncoderの実装を注入します
    private PasswordEncoder passwordEncoder;
    
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		LoginInfoEntity user = userRepository.findByUsername(username);
		if(user == null) {
			//ユーザーが見つからなかった時の例外
			throw new UsernameNotFoundException("User not found");
		}
		return new UserPrincipal(user);//ユーザーが見つからなかったらUserPrincipalを作成し返却
	}
	
	//ユーザー名でユーザー検索をし返す
	public LoginInfoEntity findByUserName(String username) {
		return userRepository.findByUsername(username);
	}
	
	//ユーザー情報を登録する処理
	//メソッドが成功したらコミット！
	//エラーが出たらロールバックされる！
	public void save(LoginInfoDto loginInfoDto) {
		LoginInfoEntity loginInfoEntity = new LoginInfoEntity();
		loginInfoEntity.setUsername(loginInfoDto.getUsername());
		//パスワードをハッシュ化してからセット
		loginInfoEntity.setPassword(passwordEncoder.encode(loginInfoDto.getPassword()));
		//DBへ保存
		userRepository.save(loginInfoEntity);
	}

}
