package com.tragepro.api.domain.identity.entity;

import com.tragepro.api.common.model.entity.BaseEntity;
import com.tragepro.api.domain.identity.constant.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "authentication")
public class AuthenticationEntity extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Indexed(unique = true)
    @NotBlank(message = "Identifier cannot be blank")
    @Size(min = 5, max = 15, message = "Identifier must be between 6 and 20 characters")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    private RoleType role;
    private Boolean isActive;
    private Set<String> identifiers;
}
