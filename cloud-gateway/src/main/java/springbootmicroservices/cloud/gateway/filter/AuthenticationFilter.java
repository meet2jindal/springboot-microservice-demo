package springbootmicroservices.cloud.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import springbootmicroservices.cloud.gateway.util.JwtUtil;

import java.util.List;
import java.util.Optional;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;
    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config {
        // ...
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){
                final HttpHeaders headers = exchange.getRequest().getHeaders();
                if(headers.containsKey(HttpHeaders.AUTHORIZATION)){
                    final List<String> authHeaders = headers.get(HttpHeaders.AUTHORIZATION);
                    if(!CollectionUtils.isEmpty(authHeaders)){
                        String authHeader = authHeaders.get(0);
                        if(authHeader.startsWith("Bearer ")){
                            String jwtToken= authHeader.substring(7);
                            String username =  exchange.getRequest().getQueryParams().getFirst("username");
                            if(!JwtUtil.validateToken(jwtToken, username)){
                                throw new RuntimeException("Jwt Token is not valid!!!");
                            }
                        }
                    }
                }
                throw new RuntimeException("Authorization Header is missing!!!");
            }
            return chain.filter(exchange);
        }));
    }
}
