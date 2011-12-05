package com.uw.watcarpool.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginInfo implements Serializable {

  private boolean loggedIn = false;
  private String loginUrl;
  private String logoutUrl;
  private String emailAddress;
  private String nickname;
  private String fedId;
  private String userId;

  public boolean isLoggedIn() {
    return loggedIn;
  }

  public void setLoggedIn(boolean loggedIn) {
    this.loggedIn = loggedIn;
  }

  public String getLoginUrl() {
    return loginUrl;
  }

  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }

  public String getLogoutUrl() {
    return logoutUrl;
  }

  public void setLogoutUrl(String logoutUrl) {
    this.logoutUrl = logoutUrl;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress= emailAddress;
  }

  public String getFedratedID() {
	    return fedId;
  }

  public void setFedaratedIdentity(String fedratedIdentity) {
    this.fedId = fedratedIdentity;
  }
  
  public String getUserID() {
	    return userId;
  }

  public void setUserID(String userid) {
	  this.userId=userid;
	}
  
  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }
}