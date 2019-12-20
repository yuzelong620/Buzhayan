package com.kratos.game.herphone.signIn.bean;

import com.kratos.game.herphone.signIn.entity.SignInEntity;

import lombok.Data;

@Data
public class DoSigninRes {
   int currentPower;
   int addPower;
	SignInEntity signin;

	public DoSigninRes(int currentPower, int addPower, SignInEntity signin) { 
		this.currentPower = currentPower;
		this.addPower = addPower;
		this.signin = signin;
	}
}
