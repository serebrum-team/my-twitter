package uz.serebrum.mytwitter.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.serebrum.mytwitter.configuration.Constants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JWTAutFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JWTAutFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // Fetching the authorization header from the request.
        String authenticationHeader = httpServletRequest.getHeader(Constants.HEADER);

        try {
            SecurityContext context = SecurityContextHolder.getContext();

            if (authenticationHeader != null && authenticationHeader.startsWith("Bearer")) {

                final String bearerTkn = authenticationHeader.replaceAll(Constants.BEARER_TOKEN, "");
                System.out.println("Following token is received from the protected url= " + bearerTkn);

                try {
                    // Parsing the jwt token.
                    Jws<Claims> claims = Jwts.parser().requireIssuer(Constants.ISSUER).setSigningKey(Constants.SECRET_KEY).parseClaimsJws(bearerTkn);

                    // Obtaining the claims from the parsed jwt token.
                    String user = (String) claims.getBody().get("usr");
                    String roles = (String) claims.getBody().get("rol");
                    System.out.println(roles);
                    String[] rolesArray = roles.split(" ");

                    // Creating the list of granted-authorities for the received roles.
                    List<GrantedAuthority> authority = new ArrayList<>();
                    for (String s : rolesArray
                    ) {
                        authority.add(new SimpleGrantedAuthority(s));
                    }

                    // Creating an authentication object using the claims.
                    Token authenticationTkn = new Token(user, null, authority);
                    // Storing the authentication object in the security context.
                    context.setAuthentication(authenticationTkn);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
            context.setAuthentication(null);
        } catch (AuthenticationException ex) {

        }

    }
}
