package it.demo.twitterlike.server.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Iterables;

/**
 * A user.
 */
@Entity
@Table(name = "T_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity<String>  {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 0, max = 50)
	@Id
	@Column(length = 50)
	private String login;

	@JsonIgnore
	@Size(min = 0, max = 100)
	@Column(length = 100)
	private String password;

	@Size(min = 0, max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Size(min = 0, max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Size(min = 0, max = 100)
	@Column(length = 100)
	private String email;

	@JsonIgnore
	private boolean activated = false;

	@Size(min = 2, max = 5)
	@Column(name = "lang_key", length = 5)
	private String langKey;

	@JsonIgnore
	@Size(min = 0, max = 20)
	@Column(name = "activation_key", length = 20)
	private String activationKey;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "T_USER_AUTHORITY", joinColumns = { @JoinColumn(name = "login", referencedColumnName = "login") }, inverseJoinColumns = { @JoinColumn(name = "name", referencedColumnName = "name") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Authority> authorities = new HashSet<>();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<PersistentToken> persistentTokens = new HashSet<>();

	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "T_USER_FOLLOWER", joinColumns = { @JoinColumn(name = "login") }, inverseJoinColumns = { @JoinColumn(name = "following") })
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<User> followers;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "followers")
	private Set<User> following = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<Message> messages = new ArrayList<>();
	
	
	public User() {
		
	}
	

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	public String getLangKey() {
		return langKey;
	}

	public void setLangKey(String langKey) {
		this.langKey = langKey;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<PersistentToken> getPersistentTokens() {
		return persistentTokens;
	}

	public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
		this.persistentTokens = persistentTokens;
	}

	public Set<User> getFollowing() {
		return following;
	}

	public void setFollowing(Set<User> following) {
		this.following = following;
	}

	public Set<User> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<User> followers) {
		this.followers = followers;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public User clearAuthorities() {
		this.authorities.clear();
		return this;
	}
	
	public User addAuthorities(Iterable<Authority> authorities) {
		return addAuthorities(Iterables.toArray(authorities, Authority.class));
	}
	
	
	public User addAuthorities(Authority... authorities) {
		if (authorities != null) {
			for (Authority current : authorities) {
				this.authorities.add(current);
			}

		}
		return this;
	}
	
	public User removeAuthorities(Iterable<Authority> authorities) {
		return removeAuthorities(Iterables.toArray(authorities, Authority.class));
	}
	
	
	public User removeAuthorities(Authority... authorities) {
		if (authorities != null) {
			for (Authority current : authorities) {
				this.authorities.remove(current);
			}

		}
		return this;
	}
	
	
	public User addFollowing(User... persons) {
		if (persons != null) {
			for (User currentPerson : persons) {
				this.following.add(currentPerson);
			}

		}
		return this;
	}

	public User addFollowers(Iterable<User> persons) {
		return addFollowers(Iterables.toArray(persons, User.class));
	}

	public User addFollowers(User... persons) {
		if (persons != null) {
			for (User currentPerson : persons) {
				this.followers.add(currentPerson);
				currentPerson.getFollowing().add(this);
			}

		}
		return this;
	}

	public User removeFollowing(User... persons) {
		if (persons != null) {
			if (this.following != null) {
				for (User currentPerson : persons) {
					this.following.remove(currentPerson);
				}
			}

		}
		return this;
	}

	public User removeFollowers(Iterable<User> persons) {
		return removeFollowers(Iterables.toArray(persons, User.class));
	}

	public User removeFollowers(User... persons) {
		if (persons != null) {
			if (this.followers != null) {
				for (User currentPerson : persons) {
					this.followers.remove(currentPerson);
				}
			}

		}
		return this;
	}

	@Override
	public String getId() {
		return getLogin();
	}	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;

		if (!login.equals(user.login)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return login.hashCode();
	}

	@Override
	public String toString() {
		return "User{" + "login='" + login + '\'' + ", password='" + password
				+ '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
				+ lastName + '\'' + ", email='" + email + '\''
				+ ", activated='" + activated + '\'' + ", langKey='" + langKey
				+ '\'' + ", activationKey='" + activationKey + '\'' + "}";
	}


	
}
