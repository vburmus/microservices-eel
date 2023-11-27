package com.epam.esm.credentials.model;

import com.epam.esm.model.Provider;
import com.epam.esm.model.Role;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "credentials")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Credentials implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Type(value = PostgreSQLEnumType.class)
    @Builder.Default
    private Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    @Type(value = PostgreSQLEnumType.class)
    @Builder.Default
    private Provider provider = Provider.LOCAL;
    @Builder.Default
    private Boolean isEnabled = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Credentials user = (Credentials) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, provider, isEnabled);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void enableAccount() {
        isEnabled = true;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public Provider getProvider() {
        return provider;
    }
}