package com.fyelci.sorumania.web.rest.dto;

import com.fyelci.sorumania.domain.Authority;
import com.fyelci.sorumania.domain.Lov;
import com.fyelci.sorumania.domain.User;

import com.fyelci.sorumania.util.SoruManiaUtil;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    private String langKey;

    @Size(max = 511)
    private String profileImageUrl;

    private Set<String> authorities;

    private String fullName;

    private Long totalScore;

    private Long userTypeId;

    private String userTypeName;

    private Long preparingForId;

    private String preparingForName;

    private String userTarget;

    private Long userGraduateStatusId;

    private String userGraduateStatusName;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), null, user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()),
            user.getProfileImageUrl(),
            user.getTotalScore(),
            user.getUserType() == null ? null : user.getUserType().getId(),
            user.getUserType() == null ? null : user.getUserType().getName(),
            user.getPreparingFor() == null ? null : user.getPreparingFor().getId(),
            user.getPreparingFor() == null ? null : user.getPreparingFor().getName(),
            user.getUserTarget(),
            user.getUserGraduateStatus() == null ? null : user.getUserGraduateStatus().getId(),
            user.getUserGraduateStatus() == null ? null : user.getUserGraduateStatus().getName());
    }

    public UserDTO(Long id, String login, String password, String firstName, String lastName,
        String email, boolean activated, String langKey, Set<String> authorities, String profileImageUrl,
                   Long totalScore, Long userTypeId, String userTypeName, Long preparingForId, String preparingForName,
                   String userTarget, Long userGraduateStatusId, String userGraduateStatusName) {

        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.authorities = authorities;
        this.profileImageUrl = profileImageUrl;
        this.fullName = SoruManiaUtil.getFullName(firstName, lastName, login);
        this.totalScore = totalScore;
        this.userTypeId = userTypeId;
        this.userTypeName = userTypeName;
        this.preparingForId = preparingForId;
        this.preparingForName = preparingForName;
        this.userTarget = userTarget;
        this.userGraduateStatusId = userGraduateStatusId;
        this.userGraduateStatusName = userGraduateStatusName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public Long getUserTypeId() {
        return userTypeId;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public Long getPreparingForId() {
        return preparingForId;
    }

    public String getPreparingForName() {
        return preparingForName;
    }

    public String getUserTarget() {
        return userTarget;
    }

    public Long getUserGraduateStatusId() {
        return userGraduateStatusId;
    }

    public String getUserGraduateStatusName() {
        return userGraduateStatusName;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            ", password='" + password + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", profileImageUrl='" + profileImageUrl + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}
