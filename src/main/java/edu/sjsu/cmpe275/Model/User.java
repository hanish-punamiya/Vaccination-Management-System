package edu.sjsu.cmpe275.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@XmlRootElement
@Table(name = "user")
@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class User implements UserDetails{
	
	

	  public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	  
	  
	public User(String firstName, String lastName, AppUserRole appUserRole, Date dateOfBirth, String gender,
			Boolean verified, String role, String password, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.appUserRole = appUserRole;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.verified = verified;
		this.role = role;
		this.password = password;
		this.email = email;
	}



	@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MRN")
	    @SequenceGenerator(name = "MRN", sequenceName = "MRN", initialValue = 100, allocationSize = 1)
	    @Column(name = "id", nullable = false)
	    private Long id;
	
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
	private AppUserRole appUserRole;
  
    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_ID")
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user","clinic","vaccinations"})
    private List<Appointment> appointments;
    
    private Boolean locked = false;

	private Boolean enabled = false;
	
	private String password;
	
	private String email;


	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}


    public AppUserRole getAppUserRole() {
		return appUserRole;
	}

	public void setAppUserRole(AppUserRole appUserRole) {
		this.appUserRole = appUserRole;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
		return Collections.singletonList(authority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}


}