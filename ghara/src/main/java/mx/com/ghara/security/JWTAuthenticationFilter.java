package mx.com.ghara.security;

import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${app.security.jwt.access-token-validity}")
    private Long accessTokenValidity;

    @Value("${app.security.jwt.secret}")
    private String accessTokenSecret;

    /**
     * Método para realizar la autenticación del usuario
     * @param req la solicitud
     * @param res la respuesta a la solicitud
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        String username = null;
        String password = null;

        try {
            // deserializar el cuerpo de la solicitud en un mapa
            HashMap data = new Gson().fromJson(req.getReader(), HashMap.class);
            // obtener las credenciales del usuario a partir del mapa
            username = (String) data.get("username");
            password = (String) data.get("password");
        } catch (Exception e) {
        }

        // delegar la comprobación de credenciales al authentication manager
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
        );
    }

    /**
     * Método que se ejecuta si la autenticación ha sido válida
     * @param req la solicitud
     * @param res la respuesta
     * @param chain un objeto para invocar el siguiente filtro
     * @param auth representa token de la autenticación
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException, AuthenticationException {
        // obtiene los datos del usuario que ha iniciado sesión
        final AppUserDetails user = (AppUserDetails) auth.getPrincipal();

        // crea un token que será enviado al cliente
        final String token = createToken(user, accessTokenValidity);

        // agrega el token al encabezado de la respuesta http
        res.addHeader(Constantes.AUTH_HEADER_NAME, Constantes.TOKEN_PREFIX + token);

        // [opcional]: escribe los principales datos del usuario
        // en el cuerpo de la respuesta http
        writeSessionData(res, user, token, accessTokenValidity);

        // llamar al mismo método del padre
        super.successfulAuthentication(req, res, chain, auth);
    }

    private String createToken(AppUserDetails userDetails, long accessTokenValiditySeconds) {
        Map<String, Object> extra = new HashMap<>();

            extra.put("userId", userDetails.getId());
            extra.put("name", userDetails.getName());
            extra.put("role", userDetails.getRol());
            extra.put("authorities", AuthorityUtils.authorityListToSet(userDetails.getAuthorities()));

            long expirationTime = accessTokenValiditySeconds * 1000;
            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

            return Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setExpiration(expirationDate)
                    .addClaims(extra)
                    .signWith(Keys.hmacShaKeyFor(accessTokenSecret.getBytes()))
                    .compact();

    }

    private void writeSessionData(HttpServletResponse response, AppUserDetails userDetails, String token, long expiration) throws IOException {
        if(userDetails.getStatus()==1){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            HashMap<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("userId", userDetails.getId());
            jsonMap.put("name", userDetails.getName());
            jsonMap.put("role", userDetails.getRol());
            jsonMap.put("token", token);
            jsonMap.put("expiresIn", expiration);
            jsonMap.put("authorities", AuthorityUtils.authorityListToSet(userDetails.getAuthorities()));

            Gson gson = new Gson();
            String jsonString = gson.toJson(jsonMap);
            PrintWriter out = response.getWriter();
            out.write(jsonString);
            out.flush();
        }

    }

}
