package com.cmpe275.cusr.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity(name = "user")
@Table(name = "user")
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Email  cannot be empty")
	@Id
	private String email;

	@NotNull(message = "First Name cannot be empty")
	private String firstName;

	@NotNull(message = "Last Name cannot be empty")
	private String lastName;

	private String password;

	@Transient
	private String passwordConfirm;
	private String provider;
	private String image;

	public String getEmail() {
		return email;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
