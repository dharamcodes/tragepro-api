package com.tragepro.api.security.service;

import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.request.LoginRequest;
import com.tragepro.api.security.model.request.ResetPasswordRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import com.tragepro.api.security.model.response.LoginResponse;

public interface AuthenticationService {

    /**
     * Authenticates a user with username and password credentials.
     * Returns a signed JWT token on success.
     *
     * @param loginRequest contains {@code userName} and {@code password}
     * @return {@link LoginResponse} with the generated JWT token and username
     * @throws com.tragepro.api.exception.AppException USER_NOT_FOUND if the user is inactive or does not exist
     * @throws com.tragepro.api.exception.AppException ACCESS_DENIED if the password is incorrect
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * Registers a new user in the system.
     * Encodes the password with BCrypt and persists the entity.
     * If {@code userName} is blank, it defaults to the {@code email} value.
     *
     * @param authenticationRequest full user registration payload
     * @return {@link AuthenticationResponse} with persisted user details
     * @throws com.tragepro.api.exception.AppException DATA_EXISTS if the username is already taken
     */
    AuthenticationResponse signup(AuthenticationRequest authenticationRequest);

    /**
     * Retrieves a user's authentication details by their username.
     *
     * @param userName the unique username to look up
     * @return {@link AuthenticationResponse} for the matched user
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no user matches the given username
     */
    AuthenticationResponse getByUserName(String userName);

    /**
     * Updates mutable fields on an existing user's authentication record.
     * Uses a merge strategy — only non-null fields in the request are applied.
     *
     * @param userName               the username identifying the record to update
     * @param authenticationRequest  fields to apply to the existing record
     * @return {@link AuthenticationResponse} reflecting the updated state
     * @throws com.tragepro.api.exception.AppException USER_NOT_FOUND if the username does not exist
     */
    AuthenticationResponse updateAuthenticationDetails(String userName, AuthenticationRequest authenticationRequest);

    /**
     * Changes the authenticated user's password after verifying that
     * {@code password} and {@code confirmPassword} match.
     *
     * @param resetPasswordRequest contains {@code userName}, {@code password}, and {@code confirmPassword}
     * @throws com.tragepro.api.exception.AppException PASSWORD_MISMATCH if the two password fields differ
     * @throws com.tragepro.api.exception.AppException USER_NOT_FOUND if no active user is found
     */
    void changePassword(ResetPasswordRequest resetPasswordRequest);

    /**
     * Soft-deletes a user account by setting {@code isActive = false}.
     * The record is retained in the database but can no longer authenticate.
     *
     * @param userName the username of the account to deactivate
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no active user is found
     */
    void deactivateAuthentication(String userName);

    /**
     * Initiates the password-reset flow for the given user.
     * Generates a short-lived (15 min) reset token and dispatches it via email.
     *
     * @param userName the username whose password needs to be reset
     * @throws com.tragepro.api.exception.AppException USER_NOT_FOUND if the user is inactive or does not exist
     */
    void resetPassword(String userName);
}
