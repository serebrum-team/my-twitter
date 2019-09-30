package uz.serebrum.mytwitter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.serebrum.mytwitter.entity.embeddable.OtherUserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "TWITTER_USER")
@Entity
@Data
@JsonIgnoreProperties({"userPosts","userFollowers","userVieweds","password"})
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true,nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;


    @CreationTimestamp
    private LocalDateTime createdDate;

    @Embedded
    private OtherUserDetails otherUserDetails;

    @OneToMany(mappedBy = "postAuthor",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    private List<Post> userPosts;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private List<Follower> userFollowers;

    @OneToMany(mappedBy = "viewedUser")
    private List<Viewed> userVieweds;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(joinColumns = @JoinColumn(name = "userId"),inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles;


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", otherUserDetails=" + otherUserDetails +
                ", userPosts=" + userPosts +
                ", userFollowers=" + userFollowers +
                ", userVieweds=" + userVieweds +
                '}';
    }

    public User(String userName, String password,List<Role> roles,OtherUserDetails otherUserDetails) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.otherUserDetails = otherUserDetails;
    }

    public User() {
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> {
            return new SimpleGrantedAuthority(role.getRoleName());
        }).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return userName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
