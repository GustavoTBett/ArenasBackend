package com.projetoWeb.Arenas.security;

import com.projetoWeb.Arenas.model.User;
import com.projetoWeb.Arenas.service.TokenService;
import com.projetoWeb.Arenas.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class GoogleOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userService.getUserByEmail(email);
        if (user == null) {
            user = userService.createUserByGoogle(email);
        }

        String token = tokenService.generateToken(user.getEmail());

        ResponseCookie cookie = tokenService.generateResponseCookieLogin(token);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/login-success");
    }
}