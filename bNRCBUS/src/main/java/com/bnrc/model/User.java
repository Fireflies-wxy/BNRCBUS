package com.bnrc.model;

public class User {

	// 用户ID
	public int uId;
	// 登录用户名
	public String name;
	// 用户密码
	public String password;
	// 用户邮箱
	public String email;
	// 用户权限,0表示管理员，1表示教员，2表示学员,3中介
	public int rights;
	// 登录次数
	public int loginCount;
	// 提示问题
	public String question;
	// 问题答案
	public String answer;
	// 是否有头像
	public int hasPhoto;
	// 头像地址
	public String photoUrl;
	// 创建时间
	private String createTime;
	// 学分，评定星级
	public int point;
	// 等级分，会员等级
	public int level;
	// 是否认证 0未认证，1已认证
	public int auth;

	public User() {

	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public int getuId() {
		return uId;
	}

	public void setuId(int uId) {
		this.uId = uId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRights() {
		return rights;
	}

	public void setRights(int rights) {
		this.rights = rights;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(int hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}
	
}
